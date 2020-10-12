package net.latinplay.latinbot.bot.yamlconfig;

import com.google.common.io.ByteStreams;
import net.latinplay.latinbot.bot.Main;

import java.io.*;

public class ConfigCreator
{
    private static ConfigCreator instance;

    public void setup(Main plugin, String configname) {
        File configFile = new File(plugin.getDataFolder(), configname + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                Throwable t = null;
                try {
                    try (InputStream is = plugin.getResourceAsStream(configname + ".yml")) {
                        try (OutputStream os = new FileOutputStream(configFile)) {
                            ByteStreams.copy(is, os);
                        }
                        if (is != null) {
                            is.close();
                        }
                    } finally {
                        if (t == null) {
                            Throwable t2 = t = null;
                        } else {
                            Throwable t2 = null;
                            if (t != t2) {
                                t.addSuppressed(t2);
                            }
                        }
                    }
                }
                finally {
                    if (t == null) {
                        Throwable t3 = t = null;
                    }
                    else {
                        Throwable t3 = null;
                        if (t != t3) {
                            t.addSuppressed(t3);
                        }
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException("Error on create " + configname + " please contact with Developer: GabrielHD55", e);
            }
        }
    }

    public static ConfigCreator get() {
        if (ConfigCreator.instance == null) {
            ConfigCreator.instance = new ConfigCreator();
        }
        return ConfigCreator.instance;
    }
}
