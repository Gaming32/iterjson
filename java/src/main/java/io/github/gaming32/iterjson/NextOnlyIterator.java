package io.github.gaming32.iterjson;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This implemention is usded only for reference. Due to Java not having multiple
 * inheritance, this code needed to be copied into CollectionJsonValue.java for
 * actual use
 */
public abstract class NextOnlyIterator<E> implements Iterator<E> {
    private boolean valueReady;
    private E next;

    @Override
    public boolean hasNext() {
        if (!valueReady) {
            try {
                next = next0();
                valueReady = true;
            } catch (NoSuchElementException e) {
                valueReady = false;
            }
        }
        return valueReady;
    }

    @Override
    public E next() {
        if (!valueReady) {
            next = next0();
        }
        valueReady = false;
        return next;
    }

    protected abstract E next0();
}
