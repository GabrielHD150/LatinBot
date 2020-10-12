package net.latinplay.latinbot.bot.yamlconfig;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class ConfigurationProvider
{
    private static Map<Class<? extends ConfigurationProvider>, ConfigurationProvider> providers;

    public static ConfigurationProvider getProvider(Class<? extends ConfigurationProvider> provider) {
        return ConfigurationProvider.providers.get(provider);
    }

    public abstract void save(Configuration p0, File p1) throws IOException;

    public abstract void save(Configuration p0, Writer p1);

    public abstract Configuration load(File p0) throws IOException;

    public abstract Configuration load(File p0, Configuration p1) throws IOException;

    public abstract Configuration load(Reader p0);

    public abstract Configuration load(Reader p0, Configuration p1);

    public abstract Configuration load(InputStream p0);

    public abstract Configuration load(InputStream p0, Configuration p1);

    public abstract Configuration load(String p0);

    public abstract Configuration load(String p0, Configuration p1);

    static {
        (providers = new HashMap<Class<? extends ConfigurationProvider>, ConfigurationProvider>()).put(YamlConfiguration.class, new YamlConfiguration());
    }
}