package ch.leadrian.samp.kamp.runtime.types;

import org.jetbrains.annotations.Nullable;

public class ReferenceString {

    @SuppressWarnings("WeakerAccess")
    @Nullable
    String value = null;

    @Nullable
    public String getValue() {
        return value;
    }

}
