package io.github.gaming32.iterjson.values;

import io.github.gaming32.iterjson.DataReader;

public abstract class SimpleJsonValue<T> extends JsonValue<T> {
    public SimpleJsonValue(char first, DataReader reader) {
        super(first, reader);
    }
}
