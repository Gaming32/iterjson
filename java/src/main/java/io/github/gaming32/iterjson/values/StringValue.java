package io.github.gaming32.iterjson.values;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.github.gaming32.iterjson.DataReader;
import io.github.gaming32.iterjson.JsonFormatException;

public final class StringValue extends SimpleJsonValue<String> {
    private static final Map<Character, Character> CONTROL_CODES = new HashMap<Character, Character>() {{
        put('"', '"');
        put('\\', '\\');
        put('/', '/');
        put('b', '\b');
        put('f', '\f');
        put('n', '\n');
        put('r', '\r');
        put('t', '\t');
    }};

    public StringValue(char first, DataReader reader) {
        super(first, reader);
    }

    @Override
    protected String read0() throws IOException {
        StringBuilder result = new StringBuilder();
        char c;
        while ((c = reader.readChar()) != '"') {
            if (c == '\\') {
                char control = reader.readChar();
                if (control == 'u') {
                    String uni = reader.readString(4);
                    if (uni.length() < 4) {
                        throw new JsonFormatException("EOF during unicode escape");
                    }
                    result.append((char)Integer.parseUnsignedInt(uni, 16));
                } else {
                    Character escape = CONTROL_CODES.get(control);
                    if (escape == null) {
                        JsonFormatException.throwOnCharacter(control);
                    }
                    result.append(escape);
                }
                continue;
            }
            result.append(c);
        }
        return result.toString();
    }
}
