package net.latinplay.anticheat.handler.interfaces;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface MovementHandler {

    void handleUpdateLocation(Player player, Location to, Location from);

    void handleUpdateRotation(Player player, Location to, Location from);
}