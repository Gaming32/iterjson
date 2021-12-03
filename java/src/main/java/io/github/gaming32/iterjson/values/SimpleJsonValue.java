package io.github.gaming32.iterjson.values;

import io.github.gaming32.iterjson.JsonReader;

public abstract class SimpleJsonValue<T> extends JsonValue<T> {
    public SimpleJsonValue(char first, JsonReader reader) {
        super(first, reader);
    }
}
