package io.github.gaming32.iterjson.values;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import io.github.gaming32.iterjson.DataReader;
import io.github.gaming32.iterjson.JsonFormatException;
import io.github.gaming32.iterjson.NullableOptional;

public final class ObjectValue extends CollectionJsonValue<Map<String, Object>, Map.Entry<String, JsonValue<Object>>> {
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

    @Override
    protected Map<String, Object> read0() throws IOException {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, JsonValue<Object>> entry : this) {
            result.put(entry.getKey(), entry.getValue().read());
        }
        return result;
    }
}
