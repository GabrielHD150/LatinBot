package net.latinplay.latinbot.bot.listeners;

import net.latinplay.latinbot.bot.Main;
import net.latinplay.latinbot.bot.utils.EmbedUtil;
import net.latinplay.latinbot.bot.utils.MessageUtil;
import net.latinplay.latinbot.jda.api.EmbedBuilder;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.Message;
import net.latinplay.latinbot.jda.api.entities.Role;
import net.latinplay.latinbot.jda.api.events.message.MessageReceivedEvent;
import net.latinplay.latinbot.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class LogsListener extends ListenerAdapter {

    private long lastTime;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        Member member = event.getMember();
        if(msg.getContentRaw().toLowerCase(Locale.ROOT).startsWith("!check")) {
            msg.delete().queue();
            if (event.getTextChannel().getParent().getIdLong() == Long.parseLong("579864396486934528") || event.getTextChannel().getParent().getIdLong() == Long.parseLong("648920173541326867")) {
                long time = System.currentTimeMillis() - this.lastTime;
                if(time <= 5000L) {
                    long lefttime = 10 * 1000 - time;
                    event.getTextChannel().sendMessage("Por favor espera "+this.toSecMs(lefttime)+"s, para usar esto nuevamente.").complete();
                    return;
                }
                if(msg.getContentRaw().equalsIgnoreCase("!check") || msg.getContentRaw().equalsIgnoreCase("!check ")) {
                    EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                            .setColor(Color.RED)
                            .setFooter("LatinPlay Network | mc.latinplay.net")
                            .setDescription("Usa: !check (nombre del jugador) - para ver razón del baneo. \n"+
                                    "Usa: !logs (nombre del jugador) - para ver ultimas alertas.");
                    event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                    return;
                }
                Role admin = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("624076352928219136"));
                Role master = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("643585448614166528"));
                Role staff = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("624089838945173505"));

                if(member.getRoles().contains(staff) || member.getRoles().contains(master) || member.getRoles().contains(admin)) {
                    String[] jugador = msg.getContentRaw().split(" ");
                    if (Main.getInstance().getMysql() != null && Main.getInstance().getMysql().getConnection() != null) {
                        new Thread(() -> {
                            if(this.playerExists(jugador[1])) {
                                ResultSet resultSet = Main.getInstance().getMysql().query("SELECT * FROM Banned_ WHERE NameLowerCase='" + jugador[1].toLowerCase() + "'");
                                try {
                                    if (resultSet != null && resultSet.next()) {
                                        String playerName = resultSet.getString("PlayerName");
                                        String reason = resultSet.getString("Reason");
                                        String date = resultSet.getString("Date");
                                        double tps = resultSet.getDouble("TPS");
                                        int ping = resultSet.getInt("Ping");
                                        int version = resultSet.getInt("Version");
                                        EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                                                .setColor(Color.RED)
                                                .setFooter("LatinPlay Network | mc.latinplay.net")
                                                .setDescription("Logs de **" + playerName + "**\n" +
                                                        "\n" +
                                                        "   Razón: " + reason + "\n" +
                                                        "   Fecha: " + date + "\n" +
                                                        "   TPS: " + tps + "\n" +
                                                        "   Ping: " + ping + "ms\n" +
                                                        "   Version: " + MessageUtil.getVersion(version) + "\n" +
                                                        " ");
                                        event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                                    }
                                } catch (SQLException e) {
                                    EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                                            .setColor(Color.RED)
                                            .setFooter("LatinPlay Network | mc.latinplay.net")
                                            .setDescription("**No se encontraron datos de este jugador.**");
                                    event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                                }
                            } else {
                                EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                                        .setColor(Color.RED)
                                        .setFooter("LatinPlay Network | mc.latinplay.net")
                                        .setDescription("**No se encontraron datos de este jugador.**");
                                event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                            }
                            this.lastTime = System.currentTimeMillis();
                        }).start();
                        return;
                    }
                }
            }
        }
        if(msg.getContentRaw().toLowerCase(Locale.ROOT).startsWith("!logs")) {
            msg.delete().queue();
            if (event.getTextChannel().getParent().getIdLong() == Long.parseLong("579864396486934528") || event.getTextChannel().getParent().getIdLong() == Long.parseLong("648920173541326867")) {
                long time = System.currentTimeMillis() - this.lastTime;
                if(time <= 5000L) {
                    long lefttime = 10 * 1000 - time;
                    event.getTextChannel().sendMessage("Por favor espera "+this.toSecMs(lefttime)+"s, para usar esto nuevamente.").complete();
                    return;
                }
                if(msg.getContentRaw().equalsIgnoreCase("!logs") || msg.getContentRaw().equalsIgnoreCase("!logs ")) {
                    EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                            .setColor(Color.RED)
                            .setFooter("LatinPlay Network | mc.latinplay.net")
                            .setDescription("Usa: !check (nombre del jugador) - para ver razón del baneo. \n"+
                                    "Usa: !logs (nombre del jugador) - para ver ultimas alertas.");
                    event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                    return;
                }
                Role admin = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("624076352928219136"));
                Role master = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("643585448614166528"));
                Role staff = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("624089838945173505"));

                if(member.getRoles().contains(staff) || member.getRoles().contains(master) || member.getRoles().contains(admin)) {
                    String[] jugador = msg.getContentRaw().split(" ");
                    if (Main.getInstance().getMysql() != null && Main.getInstance().getMysql().getConnection() != null) {
                        new Thread(() -> {
                            if(this.playerExists(jugador[1])) {
                                ResultSet resultSet = Main.getInstance().getMysql().query("SELECT * FROM Banned_ WHERE NameLowerCase='" + jugador[1].toLowerCase() + "'");
                                try {
                                    if (resultSet != null && resultSet.next()) {
                                        String playerName = resultSet.getString("PlayerName");
                                        String logs = resultSet.getString("Logs").replaceAll(", ", "\n ");
                                        EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                                                .setColor(Color.RED)
                                                .setFooter("LatinPlay Network | mc.latinplay.net")
                                                .setDescription("Logs de **" + playerName + "**\n" +
                                                        "\n"+
                                                        " "+logs+"\n"+
                                                        " ");
                                        event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                                    }
                                } catch (SQLException e) {
                                    EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                                            .setColor(Color.RED)
                                            .setFooter("LatinPlay Network | mc.latinplay.net")
                                            .setDescription("**No se encontraron datos de este jugador.**");
                                    event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                                }
                            } else {
                                EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                                        .setColor(Color.RED)
                                        .setFooter("LatinPlay Network | mc.latinplay.net")
                                        .setDescription("**No se encontraron datos de este jugador.**");
                                event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                            }
                            this.lastTime = System.currentTimeMillis();
                        }).start();
                    }
                }
            }
        }
    }

    public String toSecMs(Long l) {
        double seconds = l / 1000.0;
        return String.format("%.1f", seconds).replaceAll(",", ".");
    }

    public boolean playerExists(String playerName) {
        try {
            ResultSet rs = Main.getInstance().getMysql().query("SELECT * FROM Banned_ WHERE NameLowerCase='" + playerName.toLowerCase() + "'");
            return (rs != null && rs.next()) && rs.getString("NameLowerCase") != null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
