package ch.leadrian.samp.kamp.core.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.file.Files.newBufferedReader;
import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
public class KampLauncher {

    private static final Path DATA_DIRECTORY = Paths.get(".", "Kamp", "data");

    private static final String CONFIG_PROPERTIES_FILE = "config.properties";

    private static final Logger log = LoggerFactory.getLogger(KampLauncher.class);

    private static Server server = null;

    public static synchronized void launch() {
        if (server != null) {
            return;
        }
        try {
            Properties properties = loadConfigProperties();
            server = Server.start(new SAMPNativeFunctionExecutorImpl(), properties, DATA_DIRECTORY);
        } catch (Exception e) {
            log.error("Failed to launch server", e);
        }
    }

    private static Properties loadConfigProperties() throws IOException {
        try (Reader reader = newBufferedReader(DATA_DIRECTORY.resolve(CONFIG_PROPERTIES_FILE), ISO_8859_1)) {
            Properties properties = new Properties();
            properties.load(reader);
            return properties;
        }
    }

    public static SAMPCallbacks getCallbacksInstance() {
        requireNonNull(server, "Server has not been initialized");
        return requireNonNull(server.getCallbackProcessor(), "Server has not been initialized");
    }
}
