package net.latinplay.latinbot.bot.managers;

import net.latinplay.latinbot.bot.Main;
import net.latinplay.latinbot.bot.managers.types.TicketTypeEnum;
import net.latinplay.latinbot.bot.utils.EmbedUtil;
import net.latinplay.latinbot.jda.api.EmbedBuilder;
import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.Role;
import net.latinplay.latinbot.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.*;

public class TicketManager {

    public static HashMap<Long, TicketManager> tickets = new HashMap<>();

    private Logger logger;

    public TicketManager(TicketTypeEnum type, Member member) {
        if(Main.getInstance().isEnabled()) {
            Role role = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("673643743122948099"));
            if (type == TicketTypeEnum.APELACION) {
                TextChannel textChannel = Main.getInstance().getJDAClient().getTextChannelById(Main.getInstance().getJDAClient().getCategoryById(Long.parseLong("648920173541326867")).createTextChannel("Apelación-" + member.getUser().getName()).complete().getIdLong());
                if(textChannel.getPermissionOverride(member) == null) {
                    textChannel.createPermissionOverride(member).setAllow(
                            Permission.VIEW_CHANNEL,
                            Permission.MESSAGE_WRITE,
                            Permission.MESSAGE_READ,
                            Permission.MESSAGE_HISTORY,
                            Permission.MESSAGE_EMBED_LINKS,
                            Permission.MESSAGE_ATTACH_FILES,
                            Permission.MESSAGE_ADD_REACTION,
                            Permission.MESSAGE_EXT_EMOJI
                    ).queue();
                }
                EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                        .setColor(Color.GREEN)
                        .setFooter("LatinPlay Network | mc.latinplay.net")
                        .setDescription("           :newspaper: Formato apelaciones :newspaper:\n" +
                                "\n" +
                                "**Para apelar debes de copiar y sustituir los datos por los tuyos en la siguiente plantilla:**\n" +
                                "\n" +
                                "» (Tu nick:) DiosDelPiviPi \n" +
                                "» (Nick del staff que te sancionó:) iTzStaff\n" +
                                "» (Fecha del ban/mute/jail:) 03/06/19\n" +
                                "» (Breve descripción de los hechos:) (Escribe qué fue lo que pasó y cuenta los hechos)\n" +
                                "» (Foto del ban/mute/jail:) (Coloca la foto) \n" +
                                "\n" +
                                "**Lo máximo para apelar un baneo por consola es de 2 días.**\n" +
                                "**Lo máximo para apelar un baneo de un staff es de 7 días.**");
                textChannel.sendMessage(role.getAsMention()).queue();
                textChannel.sendMessage(member.getAsMention()).queue();
                textChannel.sendMessage(memberSuccess.build()).queue();
                textChannel.sendMessage(" **Bienvenido, por favor complete la plantilla y aguarde a que un policía disponible se encargue de su apelación.**").queue();
                Main.debug("LatinBot", member.getEffectiveName()+" Creo una apelación.");
                if(!tickets.containsKey(textChannel.getIdLong())) tickets.put(textChannel.getIdLong(), this);

                new Thread(() -> {
                    String s = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    Main.getInstance().getMysql().update("INSERT INTO Apelaciones_ (IDUser, IDChannel, Date) VALUES ('" + member.getIdLong() + "', '"+textChannel.getIdLong()+"', '"+s+"');");
                }).start();
            } else if(type == TicketTypeEnum.REPORTE) {
                TextChannel textChannel = Main.getInstance().getJDAClient().getTextChannelById(Main.getInstance().getJDAClient().getCategoryById(Long.parseLong("648927058164187176")).createTextChannel("Reporte-" + member.getUser().getName()).complete().getIdLong());

                if(textChannel.getPermissionOverride(member) == null) {
                    textChannel.createPermissionOverride(member).setAllow(
                            Permission.VIEW_CHANNEL,
                            Permission.MESSAGE_WRITE,
                            Permission.MESSAGE_READ,
                            Permission.MESSAGE_HISTORY,
                            Permission.MESSAGE_EMBED_LINKS,
                            Permission.MESSAGE_ATTACH_FILES,
                            Permission.MESSAGE_ADD_REACTION,
                            Permission.MESSAGE_EXT_EMOJI
                    ).queue();
                }
                EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                        .setColor(Color.GREEN)
                        .setFooter("LatinPlay Network | mc.latinplay.net")
                        .setDescription(":warning: Reporte :warning:\n" +
                                "\n" +
                                "**Para reportar debes completar la siguiente plantilla:**\n" +
                                "\n" +
                                "» (Nombre del infractor (IGN):) \n" +
                                "» (Razón:) \n" +
                                "» (Modalidad:) \n" +
                                "» (Pruebas:) (En caso de Hackers o campers subir un video.)");
                textChannel.sendMessage(role.getAsMention()).queue();
                textChannel.sendMessage(member.getAsMention()).queue();
                textChannel.sendMessage(memberSuccess.build()).queue();
                textChannel.sendMessage(" **Bienvenido, por favor complete la plantilla y aguarde a que un policía disponible vea su reporte.**").queue();
                Main.debug("LatinBot", member.getEffectiveName()+" Creo un reporte.");
                if(!tickets.containsKey(textChannel.getIdLong())) tickets.put(textChannel.getIdLong(), this);

                new Thread(() -> {
                    String s = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    Main.getInstance().getMysql().update("INSERT INTO Reportes_ (IDUser, IDChannel, Date) VALUES ('" + member.getIdLong() + "', '"+textChannel.getIdLong()+"', '"+s+"');");
                }).start();
            } else if(type == TicketTypeEnum.TICKET) {
                TextChannel textChannel = Main.getInstance().getJDAClient().getTextChannelById(Main.getInstance().getJDAClient().getCategoryById(Long.parseLong("646365245979492402")).createTextChannel("Ticket-" + member.getUser().getName()).complete().getIdLong());

                if(textChannel.getPermissionOverride(member) == null) {
                    textChannel.createPermissionOverride(member).setAllow(
                            Permission.VIEW_CHANNEL,
                            Permission.MESSAGE_WRITE,
                            Permission.MESSAGE_READ,
                            Permission.MESSAGE_HISTORY,
                            Permission.MESSAGE_EMBED_LINKS,
                            Permission.MESSAGE_ATTACH_FILES,
                            Permission.MESSAGE_ADD_REACTION,
                            Permission.MESSAGE_EXT_EMOJI
                    ).queue();
                }
                EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                        .setColor(Color.GREEN)
                        .setFooter("LatinPlay Network | mc.latinplay.net")
                        .setDescription(":chart_with_upwards_trend: Ticket :chart_with_upwards_trend:\n" +
                                "\n" +
                                "**Comentanos que necesitas y un staff te ayudara lo mas pronto posible**");
                textChannel.sendMessage(role.getAsMention()).queue();
                textChannel.sendMessage(member.getAsMention()).queue();
                textChannel.sendMessage(memberSuccess.build()).queue();
                Main.debug("LatinBot", member.getEffectiveName()+" Creo un ticket.");
                if(!tickets.containsKey(textChannel.getIdLong())) tickets.put(textChannel.getIdLong(), this);

                new Thread(() -> {
                    String s = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    Main.getInstance().getMysql().update("INSERT INTO Tickets_ (IDUser, IDChannel, Date) VALUES ('" + member.getIdLong() + "', '"+textChannel.getIdLong()+"', '"+s+"');");
                }).start();
            }

            this.logger = Logger.getLogger("Log-"+type.name().toUpperCase()+"-"+member.getUser().getName().toLowerCase());
            FileHandler fh;
            try {
                fh = new FileHandler(Main.getInstance().getDataFolder()+"/logs/"+"Log-"+type.name().toUpperCase()+"-"+member.getUser().getName().toLowerCase()+".log");
                fh.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return "Log: "+record.getMessage()+" \n";
                    }
                });
                this.logger.addHandler(fh);
                this.logger.setUseParentHandlers(false);
            } catch (SecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Logger getLogger() {
        return this.logger;
    }
}