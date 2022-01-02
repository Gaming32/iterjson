package io.github.gaming32.iterjson;

public final class NullableOptional<T> {
    private static final NullableOptional<?> EMPTY = new NullableOptional<>();

    private final T value;
    private final boolean present;

    private NullableOptional() {
        this.value = null;
        this.present = false;
    }

    private NullableOptional(T value) {
        this.value = value;
        this.present = true;
    }

    @SuppressWarnings("unchecked")
    public static <T> NullableOptional<T> empty() {
        return (NullableOptional<T>)EMPTY;
    }

    public static <T> NullableOptional<T> of(T value) {
        return new NullableOptional<>(value);
    }

    public boolean isPresent() {
        return present;
    }

    public T get() {
        if (!present) {
            throw new IllegalStateException("get() on empty NullableOptional");
        }
        return value;
    }
}
