package io.github.gaming32.iterjson.values;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.github.gaming32.iterjson.DataReader;
import io.github.gaming32.iterjson.JsonFormatException;
import io.github.gaming32.iterjson.NullableOptional;

public final class ArrayValue extends CollectionJsonValue<List<Object>, JsonValue<Object>> {
    public ArrayValue(char first, DataReader reader) {
        super(first, reader);
    }

    @Override
    protected JsonValue<Object> next0() throws IOException {
        if (done) {
            throw new NoSuchElementException();
        }
        if (current.isPresent()) {
            current.get().read(); // Ensure we've moved on
        }
        char c = reader.readPastWhitespace();
        if (c == ']') {
            done = true;
            throw new NoSuchElementException();
        }
        if (current.isPresent()) {
            if (c != ',') {
                JsonFormatException.throwOnCharacter(c);
            }
            current = NullableOptional.of(reader.readValue(reader.readPastWhitespace()));
        } else {
            current = NullableOptional.of(reader.readValue(c));
        }
        return current.get();
    }

    @Override
    protected List<Object> read0() throws IOException {
        List<Object> result = new ArrayList<>();
        try {
            for (JsonValue<Object> value : this) {
                result.add(value.read());
            }
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
        if (!done) {
            throw new JsonFormatException("EOF");
        }
        return result;
    }
}
