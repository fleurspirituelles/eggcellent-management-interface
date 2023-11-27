package util;

import pages.impl.MissingConfigurationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Utilities {
    private static final String PAGES_CONFIG_PROPERTIES_PATH = System.getProperty("user.dir") +
            "/src/test/resources/pages/impl/pages-config.properties";

    public static String getPagesDirectory() {
        var properties = new Properties();
        System.out.println(PAGES_CONFIG_PROPERTIES_PATH);
        try (var stream = new FileInputStream(PAGES_CONFIG_PROPERTIES_PATH)) {
            properties.load(stream);
        }
        catch (IOException e) {
            throw new MissingConfigurationException("It is missing pages-config file detailing pages.directory.name " +
                    "prop which is intended to be use to facilitate tests configuration.");
        }

        var directory = properties.getProperty("pages.directory.name");
        if (directory == null)
            throw new MissingConfigurationException("It is missing pages.directory.name property detailing the " +
                    "absolute path to the pages folder");

        return directory;
    }
}
