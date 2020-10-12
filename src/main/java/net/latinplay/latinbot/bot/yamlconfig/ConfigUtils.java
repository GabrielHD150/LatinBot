package net.latinplay.latinbot.bot.yamlconfig;

import net.latinplay.latinbot.bot.Main;

import java.io.File;
import java.io.IOException;

public class ConfigUtils
{
    public Configuration getConfig(Main plugin, String configname) {
        File pluginDir = plugin.getDataFolder();
        File configFile = new File(pluginDir, configname + ".yml");
        Configuration configuration = null;
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    public Configuration getConfig(String configname) {
        File pluginDir = Main.getInstance().getDataFolder();
        File configFile = new File(pluginDir+"/logs/", String.valueOf(configname));
        Configuration configuration = null;
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    public Configuration getConfigurationSection(Main plugin, String configname, String section) {
        return this.getConfig(plugin, configname).getSection(section);
    }

    public File getFile(Main plugin, String configname) {
        File pluginDir = plugin.getDataFolder();
        return new File(pluginDir, configname + ".yml");
    }
}
