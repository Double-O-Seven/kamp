package ch.leadrian.samp.kamp.core.runtime;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.util.Objects;

public final class StringEncoding {

    @NotNull
    private static Charset charset = Charset.forName("Cp1252");

    private StringEncoding() {
    }

    @NotNull
    public static Charset getCharset() {
        return charset;
    }

    public static void setCharset(@NotNull Charset charset) {
        Objects.requireNonNull(charset);
        StringEncoding.charset = charset;
    }
}
