package ch.leadrian.samp.kamp.core.runtime.types;

import ch.leadrian.samp.kamp.core.runtime.StringEncoding;
import org.jetbrains.annotations.Nullable;

public class ReferenceString {

    @Nullable
    private byte[] value = null;

    public void setValue(@Nullable byte[] value) {
        this.value = value;
    }

    public void setValue(@Nullable String value) {
        this.value = value == null ? null : value.getBytes(StringEncoding.getCharset());
    }

    @Nullable
    public String getValue() {
        return value == null ? null : new String(value, StringEncoding.getCharset());
    }

}
