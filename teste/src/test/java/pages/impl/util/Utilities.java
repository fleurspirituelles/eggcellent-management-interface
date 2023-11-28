package pages.impl.util;

import java.io.IOException;
import java.util.Properties;

public final class Utilities {
    private static final String PAGES_CONFIG_PROPERTIES_PATH = "tests.config.properties";

    public static String getPagesDirectory() {
        var directory = getPropertiesResource().getProperty("pages.directory.name");

        if (directory == null) throw new MissingConfigurationException(
            "It is missing pages.directory.name property which should detail the absolute path to the pages folder."
        );

        return directory;
    }

    private static Properties getPropertiesResource() {
        var properties = new Properties();
        var loader = Utilities.class.getClassLoader();

        try (var stream = loader.getResourceAsStream(PAGES_CONFIG_PROPERTIES_PATH)) {
            properties.load(stream);
        }
        catch (IOException e) {
            throw new MissingConfigurationException(
                "It is missing tests.config.properties files at resources folder detailing required configurations " +
                    "to correctly find the pages folder. Please create file: src/test/resources/tests.config.properties"
            );
        }

        return properties;
    }
}
