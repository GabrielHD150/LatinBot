package net.latinplay.latinbot.bot;

import java.io.File;
import java.io.InputStream;

public class App {

    private static App app;

    public static void main(String[] args) {
        if(App.app == null) {
            App.app = new App();
        }
        debug("LatinBot", "Iniciando bot...");
        Main main = new Main();
        main.onEnable();
    }

    public void onEnable() {}

    public App getApp() {
        return App.app;
    }

    public File getDataFolder() {
        return new File("./");
    }

    public InputStream getResourceAsStream(final String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }

    public static void debug(String prefix, String message) {
        System.out.println("["+prefix+"] "+message);
    }
}
