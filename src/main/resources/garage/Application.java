package it.nipe.garage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Application {
    Properties properties;

    public static void main(String[] args) {
        Application app = new Application();
    }

    public Application() {
        properties = readConfig();
        inject(properties);
    }


    private static Properties readConfig() {
        final Properties properties = new Properties();
        try (final InputStream stream =
                     Application.class.getResourceAsStream("/application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (final InputStream stream =
                     Application.class.getResourceAsStream("/application-filtered.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * Set required singletons into usecases.
     */
    private static void inject(Properties properties) {
    }

    /*
     * Exposed usecases...
     */
    public String getAppName() {
        return properties.getProperty("app.name");
    }

    public String getAppVersion() {
        return properties.getProperty("app.version");
    }
}
