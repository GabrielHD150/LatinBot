package net.latinplay.latinbot.bot.listeners;

import net.latinplay.latinbot.bot.Main;
import net.latinplay.latinbot.bot.managers.CerrarManager;
import net.latinplay.latinbot.bot.managers.TicketManager;
import net.latinplay.latinbot.bot.managers.types.TicketTypeEnum;
import net.latinplay.latinbot.bot.utils.EmbedUtil;
import net.latinplay.latinbot.jda.api.EmbedBuilder;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.Message;
import net.latinplay.latinbot.jda.api.events.message.MessageReceivedEvent;
import net.latinplay.latinbot.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class TicketCreationListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        Member member = event.getMember();

        if (msg.getContentRaw().equalsIgnoreCase("!apelacion") || msg.getContentRaw().equalsIgnoreCase("!apelación")) {
            new Thread(() -> {
                if(!this.hasTicket(event.getMember().getIdLong(), TicketTypeEnum.APELACION)) {
                    new TicketManager(TicketTypeEnum.APELACION, event.getMember());
                    msg.delete().complete();
                } else {
                    EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                            .setColor(Color.RED)
                            .setFooter("LatinPlay Network | mc.latinplay.net")
                            .setDescription(member.getAsMention()+" Ya tienes una apelación abierta.");
                    event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                    msg.delete().complete();
                }
            }).start();
            return;
        }

        if (msg.getContentRaw().equalsIgnoreCase("!ticket")) {
            new Thread(() -> {
                if(!this.hasTicket(event.getMember().getIdLong(), TicketTypeEnum.TICKET)) {
                    new TicketManager(TicketTypeEnum.TICKET, event.getMember());
                    msg.delete().complete();
                } else {
                    EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                            .setColor(Color.RED)
                            .setFooter("LatinPlay Network | mc.latinplay.net")
                            .setDescription(member.getAsMention()+" Ya tienes un ticket abierto.");
                    event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                    msg.delete().complete();
                }
            }).start();
            return;
        }

        if (msg.getContentRaw().equalsIgnoreCase("!reporte")) {
            new Thread(() -> {
                if(!this.hasTicket(event.getMember().getIdLong(), TicketTypeEnum.REPORTE)) {
                    new TicketManager(TicketTypeEnum.REPORTE, event.getMember());
                    msg.delete().complete();
                } else {
                    EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                            .setColor(Color.RED)
                            .setFooter("LatinPlay Network | mc.latinplay.net")
                            .setDescription(member.getAsMention()+" Ya tienes un reporte abierto.");
                    event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                    msg.delete().complete();
                }
            }).start();
            return;
        }

        if(msg.getContentRaw().toLowerCase(Locale.ROOT).startsWith("!cerrar")) {
            if(msg.getContentRaw().equalsIgnoreCase("!cerrar") || msg.getContentRaw().equalsIgnoreCase("!cerrar ")) {
                msg.delete().queue();
                EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                        .setColor(Color.RED)
                        .setFooter("LatinPlay Network | mc.latinplay.net")
                        .setDescription("Por favor usa: !cerrar (razón)");
                event.getTextChannel().sendMessage(memberSuccess.build()).queue();
                return;
            }
            new Thread(() -> new CerrarManager(event)).start();
        }
    }

    public boolean hasTicket(Long id, TicketTypeEnum type) {
        if (type == TicketTypeEnum.APELACION) {
            try {
                ResultSet rs = Main.getInstance().getMysql().query("SELECT * FROM Apelaciones_ WHERE IDUser='" + id + "'");
                return (rs != null && rs.next()) && rs.getString("IDUser") != null;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        } else if(type == TicketTypeEnum.TICKET) {
            try {
                ResultSet rs = Main.getInstance().getMysql().query("SELECT * FROM Tickets_ WHERE IDUser='" + id + "'");
                return (rs != null && rs.next()) && rs.getString("IDUser") != null;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        } else if(type == TicketTypeEnum.REPORTE){
            try {
                ResultSet rs = Main.getInstance().getMysql().query("SELECT * FROM Reportes_ WHERE IDUser='" + id + "'");
                return (rs != null && rs.next()) && rs.getString("IDUser") != null;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
