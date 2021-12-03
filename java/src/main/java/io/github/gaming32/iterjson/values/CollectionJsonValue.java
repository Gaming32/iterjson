package io.github.gaming32.iterjson.values;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import io.github.gaming32.iterjson.DataReader;
import io.github.gaming32.iterjson.NullableOptional;

public abstract class CollectionJsonValue<T, I> extends JsonValue<T> implements Iterable<I>, Iterator<I> {
    boolean done;
    NullableOptional<I> current;
    private boolean valueReady;
    private I next;

    public CollectionJsonValue(char first, DataReader reader) {
        super(first, reader);
        current = NullableOptional.empty();
    }

    public Iterator<I> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (!valueReady) {
            try {
                next = next0();
                valueReady = true;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (NoSuchElementException e) {
                valueReady = false;
            }
        }
        return valueReady;
    }

    @Override
    public I next() {
        if (!valueReady) {
            try {
                next = next0();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        valueReady = false;
        return next;
    }

    protected abstract I next0() throws IOException;
}
