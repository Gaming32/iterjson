package io.github.gaming32.iterjson.values;

import java.io.IOException;

import io.github.gaming32.iterjson.DataReader;
import io.github.gaming32.iterjson.JsonFormatException;

public final class ConstantValue extends SimpleJsonValue<Boolean> {
    public ConstantValue(char first, DataReader reader) {
        super(first, reader);
    }

    @Override
    protected Boolean read0() throws IOException {
        String req;
        Boolean val;
        switch (first) {
            case 'n':
                req = "ull";
                val = null;
                break;
            case 't':
                req = "rue";
                val = Boolean.TRUE;
                break;
            case 'f':
                req = "alse";
                val = Boolean.FALSE;
                break;
            default:
                throw new IllegalStateException("first not in 'n', 't', 'f'");
        }
        String s;
        if (!req.equals(s = reader.readString(req.length()))) {
            throw new JsonFormatException(s);
        }
        return val;
    }
}
