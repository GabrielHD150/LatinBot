package net.latinplay.latinbot.bot.utils;

import net.latinplay.latinbot.jda.api.entities.User;

public class MessageUtil {

    public static String getTag(User user){
        return user.getName() + "#" + user.getDiscriminator();
    }

    public static String getVersion(int version) {
        if(version > 701) {
            return "v1.16.X";
        } else if(version >= 550) {
            return "v1.15.X";
        } else if(version >= 441) {
            return "v1.14.X";
        } else if(version >= 341) {
            return "v1.13.X";
        } else if(version >= 317) {
            return "v1.12.X";
        } else if(version >= 301) {
            return "v1.11.X";
        } else if(version >= 201) {
            return "v1.10.X";
        } else if(version >= 48) {
            return "v1.9.X";
        } else if(version >= 6) {
            return "v1.8.X";
        } else if(version >= 3) {
            return "v1.15.X";
        }
        return "No definido";
    }

}
