package net.latinplay.latinbot.bot.yamlconfig;

import com.google.common.base.Charsets;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.LinkedHashMap;

public class YamlConfiguration extends ConfigurationProvider
{
    private ThreadLocal<Yaml> yaml;

    @Override
    public void save(Configuration config, File file) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            this.save(config, writer);
        }
    }

    @Override
    public void save(Configuration config, Writer writer) {
        this.yaml.get().dump(config.self, writer);
    }

    @Override
    public Configuration load(File file) throws IOException {
        return this.load(file, null);
    }

    @Override
    public Configuration load(File file, Configuration defaults) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            return this.load(is, defaults);
        }
    }

    @Override
    public Configuration load(Reader reader) {
        return this.load(reader, null);
    }

    @Override
    public Configuration load(Reader reader, Configuration defaults) {
        LinkedHashMap map = this.yaml.get().loadAs(reader, LinkedHashMap.class);
        if (map == null) {
            map = new LinkedHashMap<String, Object>();
        }
        return new Configuration(map, defaults);
    }

    @Override
    public Configuration load(InputStream is) {
        return this.load(is, null);
    }

    @Override
    public Configuration load(InputStream is, Configuration defaults) {
        LinkedHashMap map = this.yaml.get().loadAs(is, LinkedHashMap.class);
        if (map == null) {
            map = new LinkedHashMap<String, Object>();
        }
        return new Configuration(map, defaults);
    }

    @Override
    public Configuration load(String string) {
        return this.load(string, null);
    }

    @Override
    public Configuration load(String string, Configuration defaults) {
        LinkedHashMap map = this.yaml.get().loadAs(string, LinkedHashMap.class);
        if (map == null) {
            map = new LinkedHashMap<String, Object>();
        }
        return new Configuration(map, defaults);
    }

    YamlConfiguration() {
        this.yaml = new ThreadLocal<Yaml>() {
            @Override
            protected Yaml initialValue() {
                Representer representer = new Representer() {
                    {
                        this.representers.put(Configuration.class, data -> represent(((Configuration)data).self));
                    }
                };
                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                return new Yaml(new Constructor(), representer, options);
            }
        };
    }
}
