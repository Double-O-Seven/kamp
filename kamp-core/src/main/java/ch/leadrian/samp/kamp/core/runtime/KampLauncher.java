package ch.leadrian.samp.kamp.core.runtime;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public class KampLauncher {

    @Nullable
    private static SAMPCallbacks callbacksInstance = null;

    public static void launch() {
        if (callbacksInstance != null) {
            return;
        }
        // TODO set callbacks instance, initialize gamemode, load configuration, etc.
    }

    @NotNull
    public static SAMPCallbacks getCallbacksInstance() {
        return requireNonNull(callbacksInstance, "SAMPCallbacks instance has not been set yet.");
    }
}
