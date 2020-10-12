package net.latinplay.latinbot.bot.managers;

import net.latinplay.latinbot.bot.Main;
import net.latinplay.latinbot.bot.utils.EmbedUtil;
import net.latinplay.latinbot.jda.api.EmbedBuilder;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.Message;
import net.latinplay.latinbot.jda.api.entities.Role;
import net.latinplay.latinbot.jda.api.events.message.MessageReceivedEvent;
import net.latinplay.latinbot.jda.api.utils.AttachmentOption;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Handler;

public class CerrarManager {

    public static HashMap<Long, Boolean> porCerrar = new HashMap<>();

    public CerrarManager(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        Member member = event.getMember();
        if(porCerrar.containsKey(event.getTextChannel().getIdLong())) {
            return;
        }
        if(event.getTextChannel().getName().contains("apelación-") || event.getTextChannel().getName().contains("ticket-") || event.getTextChannel().getName().contains("reporte-")) {
            Role staff = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("624089838945173505"));
            if(member.getRoles().contains(staff)) {
                String[] d = event.getTextChannel().getName().split("-");
                String[] reason = msg.getContentRaw().split(" ");
                StringBuilder razon = new StringBuilder(reason[1]);
                for (int i = 2; i < reason.length; i++) {
                    razon.append(" ").append(reason[i]);
                }
                new Thread(() -> {
                    porCerrar.put(event.getTextChannel().getIdLong(), true);

                    if (Main.getInstance().getMysql() != null && Main.getInstance().getMysql().getConnection() != null) {
                        EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                                .setColor(Color.RED)
                                .setFooter("LatinPlay Network | mc.latinplay.net")
                                .setDescription(":star: Ticket cerrado :star:" +
                                        "\n" +
                                        "Ticket creado por: "+ d[1].toLowerCase() + "\n" +
                                        "Ticket cerrado por: " + member.getAsMention() + "\n" +
                                        "Tipo de ticket: " + d[0].toUpperCase() + "\n" +
                                        "Razón: " + razon);
                        event.getTextChannel().sendMessage(memberSuccess.build()).complete();
                        event.getTextChannel().sendMessage("**El ticket se cerrara en 7 segundos...**").complete();
                        Main.getInstance().getJDAClient().getTextChannelById(Long.parseLong("672291854485684237")).sendMessage(memberSuccess.build()).queue();
                        Main.debug("LatinBot", "Ticket de " + d[1].toLowerCase() + " cerrado por " + member.getEffectiveName());
                    }
                    try {
                        Thread.sleep(7000L);
                    } catch (InterruptedException e) {
                        Main.debug("LatinBot", "El cerrado fue cancelado.");
                    }

                    if(porCerrar.get(event.getTextChannel().getIdLong())) {
                        if(d[0].equalsIgnoreCase("apelación")) {
                            Main.getInstance().getMysql().update("DELETE FROM Apelaciones_ WHERE IDChannel='" + event.getTextChannel().getIdLong() + "';");
                        } else if(d[0].equalsIgnoreCase("ticket")) {
                            Main.getInstance().getMysql().update("DELETE FROM Tickets_ WHERE IDChannel='" + event.getTextChannel().getIdLong() + "';");
                        } else if(d[0].equalsIgnoreCase("reporte")) {
                            Main.getInstance().getMysql().update("DELETE FROM Reportes_ WHERE IDChannel='" + event.getTextChannel().getIdLong() + "';");
                        }

                        if(TicketManager.tickets.containsKey(event.getTextChannel().getIdLong())) {
                            Handler[] handlers = TicketManager.tickets.get(event.getTextChannel().getIdLong()).getLogger().getHandlers();
                            for(Handler handler : handlers) {
                                TicketManager.tickets.get(event.getTextChannel().getIdLong()).getLogger().removeHandler(handler);
                                handler.close();
                            }
                            TicketManager.tickets.remove(event.getTextChannel().getIdLong());
                        }

                        File file = new File(Main.getInstance().getDataFolder()+"/logs/"+"Log-"+d[0].toUpperCase().replaceAll("APELACIÓN", "APELACION")+"-"+d[1].toLowerCase()+".log");
                        if(file.exists()) {
                            Main.getInstance().getJDAClient().getTextChannelById(Long.parseLong("737467327683231745")).sendFile(file, "Log-"+d[0].toUpperCase().replaceAll("APELACIÓN", "APELACION")+"-"+d[1].toLowerCase()+".log", AttachmentOption.NONE).queue();
                            file.delete();
                            File file2 = new File(Main.getInstance().getDataFolder()+"/logs/"+"Log-"+d[0].toUpperCase().replaceAll("APELACIÓN", "APELACION")+"-"+d[1].toLowerCase()+".log.lck");
                            if(file2.exists()) file2.delete();
                        }

                        event.getTextChannel().delete().queue();
                    }
                    porCerrar.remove(event.getTextChannel().getIdLong());
                }).start();
            }
        }
    }
}
