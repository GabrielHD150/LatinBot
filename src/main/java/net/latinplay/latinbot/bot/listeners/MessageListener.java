package net.latinplay.latinbot.bot.listeners;

import net.latinplay.latinbot.bot.Main;
import net.latinplay.latinbot.bot.managers.CerrarManager;
import net.latinplay.latinbot.bot.managers.TicketManager;
import net.latinplay.latinbot.bot.utils.EmbedUtil;
import net.latinplay.latinbot.jda.api.EmbedBuilder;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.Message;
import net.latinplay.latinbot.jda.api.entities.Role;
import net.latinplay.latinbot.jda.api.entities.TextChannel;
import net.latinplay.latinbot.jda.api.events.message.MessageReceivedEvent;
import net.latinplay.latinbot.jda.api.events.message.MessageUpdateEvent;
import net.latinplay.latinbot.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        Message msg = event.getMessage();
        if(TicketManager.tickets.containsKey(event.getTextChannel().getIdLong())) {
            String msj = msg.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " " + msg.getAuthor().getName() + ": " + msg.getContentRaw()+ " (Editado)";
            for(Member member1 : msg.getMentionedMembers()) {
                msj = msj.replaceAll(member1.getAsMention(), member1.getUser().getName());
            }
            for(TextChannel channel : msg.getMentionedChannels()) {
                msj = msj.replaceAll(channel.getAsMention(), channel.getName());
            }
            TicketManager.tickets.get(event.getTextChannel().getIdLong()).getLogger().info(msj);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        Member member = event.getMember();
        if(TicketManager.tickets.containsKey(event.getTextChannel().getIdLong())) {
            String msj = msg.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " " + msg.getAuthor().getName() + ": " + msg.getContentRaw();
            for(Member member1 : msg.getMentionedMembers()) {
                msj = msj.replaceAll(member1.getAsMention(), member1.getEffectiveName());
            }
            for(Role role : msg.getMentionedRoles()) {
                msj = msj.replaceAll(role.getAsMention(), role.getName());
            }
            for(TextChannel channel : msg.getMentionedChannels()) {
                msj = msj.replaceAll(channel.getAsMention(), channel.getName());
            }
            TicketManager.tickets.get(event.getTextChannel().getIdLong()).getLogger().info(msj);
        }

        if(msg.getContentRaw().equalsIgnoreCase("!cancelar") || msg.getContentRaw().equalsIgnoreCase("!cl") || msg.getContentRaw().equalsIgnoreCase("!cancel")) {
            Role mod = Main.getInstance().getJDAClient().getRoleById(Long.parseLong("624123349999550464"));
            if(member.getRoles().contains(mod)) {
                if (CerrarManager.porCerrar.containsKey(event.getTextChannel().getIdLong())) {
                    CerrarManager.porCerrar.replace(event.getTextChannel().getIdLong(), true, false);
                    event.getTextChannel().sendMessage("**El cierre del canal fue cancelado.**").complete();
                    Main.debug("LatinBot", "Cancelado..");
                }
            }
        }

        if(msg.getContentRaw().equalsIgnoreCase("ayuda") || msg.getContentRaw().equalsIgnoreCase("necesito ayuda") || msg.getContentRaw().toLowerCase(Locale.ROOT).startsWith("ayuda")) {
            EmbedBuilder memberSuccess = EmbedUtil.getEmbed(member.getUser())
                    .setColor(Color.BLUE)
                    .setFooter("LatinPlay Network | mc.latinplay.net")
                    .setDescription(":star: Hola, "+member.getAsMention()+" ! :star:\n" +
                            "\n" +
                            ":beginner: Si necesitas asistencia especifica, puedes abrir un ticket de la siguiente manera: :beginner:\n" +
                            "\n" +
                            "➽ !ticket\n" +
                            "\n" +
                            ":beginner: Si necesitas apelar, escribe el siguiente comando: :beginner:\n" +
                            "\n" +
                            "➽ !apelacion");
            event.getTextChannel().sendMessage(memberSuccess.build()).queue();
        }
        if(msg.getContentRaw().equalsIgnoreCase("!ds") || msg.getContentRaw().equalsIgnoreCase("!discord") || msg.getContentRaw().equalsIgnoreCase("discord")) {
            event.getTextChannel().sendMessage("Discord Oficial: https://discord.gg/qDd7EXM").queue();
        }
    }
}
