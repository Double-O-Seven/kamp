package ch.leadrian.samp.kamp.core.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
public class KampLauncher {

    private static final Logger log = LoggerFactory.getLogger(KampLauncher.class);

    private static Server server = null;

    public static synchronized void launch() {
        if (server != null) {
            return;
        }
        try {
            server = Server.start(new SAMPNativeFunctionExecutorImpl());
        } catch (Exception e) {
            log.error("Failed to launch server", e);
        }
    }

    public static SAMPCallbacks getCallbacksInstance() {
        requireNonNull(server, "Server has not been initialized");
        return requireNonNull(server.getCallbackProcessor(), "Server has not been initialized");
    }
}
