package io.github.gaming32.iterjson.values;

import java.io.IOException;

import io.github.gaming32.iterjson.DataReader;
import io.github.gaming32.iterjson.JsonFormatException;

public final class NumberValue extends SimpleJsonValue<Number> {
    private static final String DIGITS = "0123456789";

    private boolean hasValue;
    private double doubleValue;
    private long longValue;
    private boolean isDouble;

    public NumberValue(char first, DataReader reader) {
        super(first, reader);
    }

    public double doubleValue() throws IOException {
        if (!hasValue) {
            read0();
        }
        return isDouble ? doubleValue : (double)longValue;
    }

    public float floatValue() throws IOException {
        if (!hasValue) {
            read0();
        }
        return isDouble ? (float)doubleValue : (float)longValue;
    }

    public long longValue() throws IOException {
        if (!hasValue) {
            read0();
        }
        return isDouble ? (long)doubleValue : longValue;
    }

    public int intValue() throws IOException {
        if (!hasValue) {
            read0();
        }
        return isDouble ? (int)doubleValue : (int)longValue;
    }

    public short shortValue() throws IOException {
        if (!hasValue) {
            read0();
        }
        return isDouble ? (short)doubleValue : (short)longValue;
    }

    public byte byteValue() throws IOException {
        if (!hasValue) {
            read0();
        }
        return isDouble ? (byte)doubleValue : (byte)longValue;
    }

    public Number longOrDoubleValue() throws IOException {
        if (!hasValue) {
            return read0();
        }
        return isDouble ? Double.valueOf(doubleValue) : Long.valueOf(longValue);
    }

    public Number intOrDoubleValue() throws IOException {
        if (!hasValue) {
            read0();
        }
        return isDouble ? Double.valueOf(doubleValue) : (
            longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE ?
                Double.valueOf(longValue) : Integer.valueOf((int)longValue)
        );
    }

    @Override
    public Number read() throws IOException {
        if (!hasValue) {
            return read0();
        }
        return isDouble ? Double.valueOf(doubleValue) : Long.valueOf(longValue);
    }

    @Override
    protected Number read0() throws IOException {
        hasValue = true;
        if (first == 'N') {
            String s;
            if (!"aN".equals(s = reader.readString(2))) {
                throw new JsonFormatException(s);
            }
            isDouble = true;
            doubleValue = Double.NaN;
            return Double.valueOf(doubleValue);
        }
        int sign;
        char c;
        if (first == '-') {
            sign = -1;
            c = reader.readChar();
        } else {
            sign = 1;
            c = first;
        }
        if (c == 'I') {
            String s;
            if (!"nfinity".equals(s = reader.readString(7))) {
                throw new JsonFormatException(s);
            }
            isDouble = true;
            doubleValue = Double.POSITIVE_INFINITY * sign;
            return Double.valueOf(doubleValue);
        }
        StringBuilder result = new StringBuilder();
        isDouble = false;
        if (c != '0') {
            if (DIGITS.indexOf(c) == -1) {
                JsonFormatException.throwOnCharacter(c);
            }
            do {
                result.append(c);
                c = reader.readCharOrNull();
            } while (c != '\0' && DIGITS.indexOf(c) != -1);
        } else {
            result.append('0');
            c = reader.readCharOrNull();
        }
        if (c == '.') {
            isDouble = true;
            do {
                result.append(c);
                c = reader.readCharOrNull();
            } while (c != '\0' && DIGITS.indexOf(c) != -1);
        }
        if (c == 'e' || c == 'E') {
            isDouble = true;
            result.append(c);
            c = reader.readChar();
            if (c != '-' && c != '+' && DIGITS.indexOf(c) == -1) {
                throw new JsonFormatException("Exponential notation missing exponent");
            }
            do {
                result.append(c);
                c = reader.readCharOrNull();
            } while (c != '\0' && DIGITS.indexOf(c) != -1);
        }
        reader.setBuffer(c);
        String s = result.toString();
        if (!isDouble) {
            try {
                longValue = Long.parseLong(s) * sign;
                return Long.valueOf(longValue);
            } catch (NumberFormatException e) {
                isDouble = true; // Use a double if the number is to big to fit into a long
            }
        }
        doubleValue = Double.parseDouble(s) * sign;
        return Double.valueOf(doubleValue);
    }
}
