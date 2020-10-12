package net.latinplay.anticheat.handler;

import net.latinplay.anticheat.LatinAC;
import net.latinplay.anticheat.check.ICheck;
import net.latinplay.anticheat.data.PlayerData;
import net.latinplay.anticheat.handler.interfaces.MovementHandler;
import net.latinplay.anticheat.util.BlockUtil;
import net.latinplay.anticheat.util.MathUtil;
import net.latinplay.anticheat.util.PlayerUtil;
import net.latinplay.anticheat.util.update.RotationUpdate;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class CustomMovementHandler implements MovementHandler {
    private final LatinAC plugin;

    @Override
    public void handleUpdateLocation(Player player, Location to, Location from) {
        if (player.hasPermission("latinac.bypass")) return;
        if (LatinAC.getInstance().getLag().getTPS() < 19.8) return;

        if(to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()) {
            if (player.getAllowFlight() || player.getGameMode() == GameMode.CREATIVE || player.isInsideVehicle() || to.getY() < 2.0) return;
            if (!player.getLocation().getChunk().isLoaded()) return;

            PlayerData playerData = LatinAC.getInstance().getPlayerDataManager().getPlayerData(player);
            if (playerData == null) return;
            if (playerData.isBanned()) return;

            boolean isOnGround = PlayerUtil.isOnGround(player.getLocation());

            playerData.setWasOnGround(playerData.isOnGround());
            playerData.setWasInLiquid(playerData.isInLiquid());
            playerData.setWasUnderBlock(playerData.isUnderBlock());
            playerData.setWasInWeb(playerData.isInWeb());
            playerData.setOnGround(isOnGround);
            playerData.setInLiquid(PlayerUtil.isOnLiquid(player.getLocation()));
            playerData.setInWeb(PlayerUtil.isOnWeb(player.getLocation()));
            playerData.setOnIce(PlayerUtil.isOnIce(player.getLocation()));
            playerData.setOnStairs(PlayerUtil.isOnStairs(player.getLocation()));
            playerData.setOnCarpet(PlayerUtil.isOnCarpet(player.getLocation()));
            playerData.setUnderBlock(isOnGround);
            playerData.setInSlab(BlockUtil.isSlab(player.getLocation().getBlock()) || BlockUtil.isSlab(player.getLocation().getBlock().getRelative(BlockFace.DOWN)));
            playerData.setInSlime(BlockUtil.isSlime(player.getLocation().getBlock()) || BlockUtil.isSlime(player.getLocation().getBlock().getRelative(BlockFace.DOWN)));
            playerData.setOnLadder(PlayerUtil.isOnLadder(player.getLocation()));
            playerData.setWasOnLadder(PlayerUtil.isOnLadder(player.getLocation()));
            playerData.setBelowBlock(PlayerUtil.isOnSolid(player.getLocation()));
            playerData.setWasBelowBlock(PlayerUtil.isOnSolid(player.getLocation()));
            playerData.tick();

            if (playerData.isOnIce()) {
                playerData.setMovementsSinceIce(0);
            } else {
                playerData.setMovementsSinceIce(playerData.getMovementsSinceIce() + 1);
            }
            if (playerData.isOnGround()) {
                playerData.setLastGroundY(to.getY());
                playerData.getLastGroundY();
            }
            if (playerData.isUnderBlock()) {
                playerData.setMovementsSinceUnderBlock(0);
            } else {
                playerData.setMovementsSinceUnderBlock(playerData.getMovementsSinceUnderBlock() + 1);
            }

            if (to.getY() != from.getY() && playerData.getVelocityV() > 0) {
                playerData.setVelocityV(playerData.getVelocityV() - 1);
            }
            if (MathUtil.hypot(to.getX() - from.getX(), to.getZ() - from.getZ()) > 0.0 && playerData.getVelocityH() > 0) {
                playerData.setVelocityH(playerData.getVelocityH() - 1);
            }
            if (playerData.getVelocityY() > 0.0 && to.getY() > from.getY()) {
                playerData.setVelocityY(0.0);
            }
            boolean onGround = playerData.isOnGround();
            if (onGround) {
                playerData.setGroundTicks(playerData.getGroundTicks() + 1);
                playerData.setAirTicks(0);
                if (playerData.getAboveBlockTicks() < 60) {
                    playerData.setAboveBlockTicks(playerData.getAboveBlockTicks() + 1);
                }
            } else if (playerData.getAboveBlockTicks() > 0) {
                playerData.setAboveBlockTicks(playerData.getAboveBlockTicks() - 1);
            } else {
                playerData.setAirTicks(playerData.getAirTicks() + 1);
                playerData.setGroundTicks(0);
            }

            if ((System.currentTimeMillis() - playerData.getLastJoinTime() > 500L)) {
                playerData.setMoveSpeed(PlayerUtil.getHorizontalDistance(from, to));
            }

            if(!player.isOnGround() && from.getY() > to.getY()) {
                double Falling = playerData.getFallDistance();
                Falling += from.getY() - to.getY();
                playerData.setFallDistance(Falling);
            }
        }
    }

    @Override
    public void handleUpdateRotation(Player player, Location to, Location from) {
        if (player.hasPermission("latinac.bypass")) return;
        if (this.plugin.getLag().getTPS() < 19.8) return;
        if (player.getAllowFlight()) return;

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData == null) return;
        if (playerData.isBanned()) return;

        if(to.getYaw() != from.getYaw() || to.getPitch() != from.getPitch()) {
            for (Class<? extends ICheck> checkClass : PlayerData.CHECKS) {
                ICheck check = playerData.getCheck(checkClass);
                if (check != null && check.getType() == RotationUpdate.class) {
                    check.handleCheck(player, new RotationUpdate(player, to, from));
                }
            }
        }
    }

    public CustomMovementHandler(LatinAC plugin) {
        this.plugin = plugin;
    }
}
