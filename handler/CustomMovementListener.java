package net.latinplay.anticheat.handler;

import net.latinplay.anticheat.LatinAC;
import net.latinplay.anticheat.check.ICheck;
import net.latinplay.anticheat.data.PlayerData;
import net.latinplay.anticheat.util.update.PositionUpdate;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class CustomMovementListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("latinac.bypass")) return;
        if (LatinAC.getInstance().getLag().getTPS() < 19.8) return;
        Location to = event.getTo();
        Location from = event.getFrom();

        if(to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()) {
            if (player.getAllowFlight() || player.getGameMode() == GameMode.CREATIVE || player.isInsideVehicle() || to.getY() < 2.0)
                return;
            if (!player.getLocation().getChunk().isLoaded()) return;

            PlayerData playerData = LatinAC.getInstance().getPlayerDataManager().getPlayerData(player);
            if (playerData == null) return;
            if (playerData.isBanned()) return;

            for (Class<? extends ICheck> checkClass : PlayerData.CHECKS) {
                ICheck check = playerData.getCheck(checkClass);
                if (check != null && check.getType() == PositionUpdate.class) {
                    check.handleCheck(player, new PositionUpdate(player, to, from));
                }
            }
        }
    }
}
