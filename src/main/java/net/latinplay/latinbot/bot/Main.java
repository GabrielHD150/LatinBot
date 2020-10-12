package net.latinplay.latinbot.bot;

import net.latinplay.latinbot.bot.database.MySQL;
import net.latinplay.latinbot.bot.listeners.LogsListener;
import net.latinplay.latinbot.bot.listeners.MessageListener;
import net.latinplay.latinbot.bot.listeners.TicketCreationListener;
import net.latinplay.latinbot.bot.yamlconfig.ConfigCreator;
import net.latinplay.latinbot.bot.yamlconfig.ConfigUtils;
import net.latinplay.latinbot.bot.yamlconfig.Configuration;
import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.JDABuilder;
import net.latinplay.latinbot.jda.api.OnlineStatus;
import net.latinplay.latinbot.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.File;

public class Main extends App {

    private JDA jdaclient;
    private static Main instance;
    private boolean isEnabled;
    private final ConfigUtils configUtils;
    private MySQL mysql;

    public Main() {
        this.isEnabled = false;
        this.configUtils = new ConfigUtils();
    }

    @Override
    public void onEnable() {
        ConfigCreator.get().setup(this, "Settings");
        instance = this;
        this.connect();
        this.connectDatabase();
        File dir =  new File(Main.getInstance().getDataFolder()+"/logs/");
        if(!dir.exists()) dir.mkdir();
        this.sendMessage("LatinBot Started.");

        App.debug("LatinBot", "Bot iniciado correctamente.");
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void sendMessage(String s) {
        this.jdaclient.getTextChannelById(Long.parseLong("580151852843270145")).sendMessage(s).queue();
    }

    public static Main getInstance() {
        return instance;
    }

    public JDA getJDAClient() {
        return this.jdaclient;
    }

    public Configuration getConfig(String name) {
        return this.configUtils.getConfig(this, name);
    }

    public MySQL getMysql() {
        return this.mysql;
    }

    private void connect() {
        try {
            (this.jdaclient = new JDABuilder("NzIyODMzMTI2Njk0MzIyMTk2.XvufbQ.BEEf4oZiIlMUjDM-IoZipjoDzd4")
                    .setAutoReconnect(true)
                    .addEventListeners(new TicketCreationListener())
                    .addEventListeners(new MessageListener())
                    .addEventListeners(new LogsListener())
                    .build()).awaitReady();
            this.isEnabled = true;
        } catch (LoginException | InterruptedException e) {
            App.debug("Error", e.getMessage());
            e.printStackTrace();
            this.isEnabled = false;
        }
        this.getJDAClient().getPresence().setPresence(OnlineStatus.ONLINE, Activity.of(Activity.ActivityType.WATCHING, "LatinPlay NetWork", "mc.latinplay.net"));
    }

    private void connectDatabase() {
        if(this.getConfig("Settings").getBoolean("MySQL.Enable")) {
            String host = this.getConfig("Settings").getString("MySQL.Host");
            String port = this.getConfig("Settings").getString("MySQL.Port");
            String database = this.getConfig("Settings").getString("MySQL.Database");
            String username = this.getConfig("Settings").getString("MySQL.Username");
            String password = this.getConfig("Settings").getString("MySQL.Password");
            this.mysql = new MySQL(host, port, database, username, password);
        }
    }

    public ConfigUtils getConfigUtils() {
        return this.configUtils;
    }
}
