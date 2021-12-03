package io.github.gaming32.iterjson.values;

import java.io.IOException;

import io.github.gaming32.iterjson.DataReader;
import io.github.gaming32.iterjson.NullableOptional;

public abstract class JsonValue<T> {
    final char first;
    final DataReader reader;
    NullableOptional<T> value;

    protected JsonValue() {
        this.first = '\0';
        this.reader = null;
    }

    public JsonValue(char first, DataReader reader) {
        this.first = first;
        this.reader = reader;
        this.value = NullableOptional.empty();
    }

    public T getValue() throws IOException {
        return read();
    }

    public T read() throws IOException {
        if (!value.isPresent()) {
            value = NullableOptional.of(read0());
        }
        return value.get();
    }

    protected abstract T read0() throws IOException;
}
