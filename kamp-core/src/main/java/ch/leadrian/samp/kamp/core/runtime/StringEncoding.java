package ch.leadrian.samp.kamp.core.runtime;

import java.nio.charset.Charset;

public final class StringEncoding {

    private static Charset charset = Charset.forName("Cp1252");

    private StringEncoding() {
    }

    public static Charset getCharset() {
        return charset;
    }

    public static void setCharset(Charset charset) {
        StringEncoding.charset = charset;
    }
}
