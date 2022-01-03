package io.github.gaming32.iterjson.values;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import io.github.gaming32.iterjson.DataReader;
import io.github.gaming32.iterjson.JsonFormatException;
import io.github.gaming32.iterjson.NullableOptional;

public final class ObjectValue extends CollectionJsonValue<Map<String, Object>, Map.Entry<String, JsonValue<Object>>> {
    private boolean isOrdered;

    public ObjectValue(char first, DataReader reader) {
        super(first, reader);
    }

    @Override
    protected Map.Entry<String, JsonValue<Object>> next0() throws IOException {
        if (done) {
            throw new NoSuchElementException();
        }
        if (current.isPresent()) {
            current.get().getValue().read();
        }
        char c = reader.readPastWhitespace();
        if (c == '}') {
            done = true;
            throw new NoSuchElementException();
        }
        if (current.isPresent()) {
            if (c != ',') {
                JsonFormatException.throwOnCharacter(c);
            }
            current = NullableOptional.of(readEntry(reader.readPastWhitespace()));
        } else {
            current = NullableOptional.of(readEntry(c));
        }
        return current.get();
    }

    private Map.Entry<String, JsonValue<Object>> readEntry(char c) throws IOException {
        if (c != '"') {
            JsonFormatException.throwOnCharacter(c);
        }
        String name = new StringValue(c, reader).read();
        c = reader.readPastWhitespace();
        if (c != ':') {
            JsonFormatException.throwOnCharacter(c);
        }
        return new AbstractMap.SimpleImmutableEntry<>(name, reader.readValue());
    }

    public Map<String, Object> read(boolean ordered) throws IOException {
        if (!value.isPresent()) {
            value = NullableOptional.of(read0(ordered));
        }
        if (ordered && !isOrdered) {
            throw new IllegalStateException("Cannot access ordered JSON object when it was read unordered");
        }
        return value.get();
    }

    @Override
    protected Map<String, Object> read0() throws IOException {
        return read0(isOrdered);
    }

    protected Map<String, Object> read0(boolean ordered) throws IOException {
        isOrdered = ordered;
        Map<String, Object> result = ordered ? new LinkedHashMap<>() : new HashMap<>();
        for (Map.Entry<String, JsonValue<Object>> entry : this) {
            result.put(entry.getKey(), entry.getValue().read());
        }
        return result;
    }
}
