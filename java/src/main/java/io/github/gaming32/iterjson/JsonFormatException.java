package io.github.gaming32.iterjson;

public class JsonFormatException extends IllegalArgumentException {
    public static void throwOnCharacter(int c) {
        String errorChar = c == -1 ? "EOF" : Character.toString((char)c);
        throw new JsonFormatException(errorChar);
    }

    /**
     * Constructs an <code>JsonFormatException</code> with no
     * detail message.
     */
    public JsonFormatException() {
        super();
    }

    /**
     * Constructs an <code>JsonFormatException</code> with the
     * specified detail message.
     *
     * @param   s   the detail message.
     */
    public JsonFormatException(String s) {
        super(s);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link Throwable#getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <code>null</code> value
     *         is permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public JsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <code>(cause==null ? null : cause.toString())</code> (which
     * typically contains the class and detail message of <code>cause</code>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <code>null</code> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public JsonFormatException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = -4524525839715507460L;
}
