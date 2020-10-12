package net.latinplay.anticheat.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.latinplay.anticheat.LatinAC;
import net.latinplay.anticheat.check.ICheck;
import net.latinplay.anticheat.data.PlayerData;
import net.latinplay.anticheat.handler.interfaces.MovementHandler;
import net.latinplay.anticheat.util.CustomLocation;
import net.latinplay.anticheat.util.MathUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CustomPacketHandler {

    private final LatinAC plugin;
    private double lastPosX = Double.MAX_VALUE;
    private double lastPosY = Double.MAX_VALUE;
    private double lastPosZ = Double.MAX_VALUE;
    private float lastPitch = Float.MAX_VALUE;
    private float lastYaw = Float.MAX_VALUE;
    private boolean hasMoved;
    private static final List<String> INSTANT_BREAK_BLOCKS = Arrays.asList(
            "reeds", "waterlily", "deadbush", "flower", "doubleplant", "tallgrass"
    );

    public CustomPacketHandler(LatinAC plugin) {
        this.plugin = plugin;
        this.registerMovementHandler();
        this.registerHandleReceivedPacket();
        this.registerHandleSentPacket();
    }

    public void registerMovementHandler() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(LatinAC.getInstance(), PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.POSITION, PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (player.isOnline()) return;
                PlayerData playerData = CustomPacketHandler.this.plugin.getPlayerDataManager().getPlayerData(player);
                PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)event.getPacket().getHandle();
                if(playerData != null) {
                    if(playerData.isBanned()) return;
                    if(!playerData.isHasMoved()) {
                        Location curPos = player.getLocation();
                        CustomPacketHandler.this.lastPosX = curPos.getX();
                        CustomPacketHandler.this.lastPosY = curPos.getY();
                        CustomPacketHandler.this.lastPosZ = curPos.getZ();
                        CustomPacketHandler.this.lastYaw = curPos.getYaw();
                        CustomPacketHandler.this.lastPitch = curPos.getPitch();
                        playerData.setHasMoved(true);
                    }
                    Location from = new Location(player.getWorld(), CustomPacketHandler.this.lastPosX, CustomPacketHandler.this.lastPosY, CustomPacketHandler.this.lastPosZ, CustomPacketHandler.this.lastYaw, CustomPacketHandler.this.lastPitch);
                    Location to = player.getLocation().clone();
                    if((packetPlayInFlying.g()) && ((!packetPlayInFlying.g()) || (packetPlayInFlying.b() != -999.0D))) {
                        to.setX(packetPlayInFlying.a());
                        to.setX(packetPlayInFlying.b());
                        to.setX(packetPlayInFlying.c());
                    }
                    if(packetPlayInFlying.h()) {
                        to.setYaw(packetPlayInFlying.d());
                        to.setPitch(packetPlayInFlying.e());
                    }
                    double delta = Math.pow(CustomPacketHandler.this.lastPosX - to.getX(), 2.0D) + Math.pow(CustomPacketHandler.this.lastPosY - to.getY(), 2.0D) + Math.pow(CustomPacketHandler.this.lastPosZ - to.getZ(), 2.0D);
                    float deltaAngle = Math.abs(CustomPacketHandler.this.lastYaw - to.getYaw()) + Math.abs(CustomPacketHandler.this.lastPitch - to.getPitch());
                    if ((packetPlayInFlying.g()) && (delta > 0.0D) &&(!player.isDead())) {
                        for (MovementHandler handler : LatinAC.getInstance().getMovementHandlers()) {
                            try
                            {
                                handler.handleUpdateLocation(player, to, from);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    if ((packetPlayInFlying.h()) && (deltaAngle > 0.0F) && (!player.isDead())) {
                        for (MovementHandler handler : LatinAC.getInstance().getMovementHandlers()) {
                            try {
                                handler.handleUpdateRotation(player, to, from);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(LatinAC.getInstance(), PacketType.Play.Client.ABILITIES) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (!player.getAllowFlight()) {
                    player.kickPlayer("Ilegal Flying Packets");
                }
            }
        });
    }

    public void registerHandleReceivedPacket() {
        for(PacketType type : PacketType.Play.Client.getInstance().values()) {
            if(type.isSupported()) {
                ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(LatinAC.getInstance(), type) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        Player player = event.getPlayer();
                        Packet packet = (Packet)event.getPacket().getHandle();

                        if (player.hasPermission("latinac.bypass")) return;
                        if (CustomPacketHandler.this.plugin.getLag().getTPS() < 19.8) return;
                        if (player.getAllowFlight()) return;
                        if (player.isOnline()) return;
                        PlayerData playerData = CustomPacketHandler.this.plugin.getPlayerDataManager().getPlayerData(player);
                        if (playerData == null) return;
                        if (playerData.isBanned()) return;

                        if((packet instanceof PacketPlayInFlying) || (packet instanceof PacketPlayInFlying.PacketPlayInPosition) || (packet instanceof PacketPlayInFlying.PacketPlayInPositionLook) || (packet instanceof PacketPlayInFlying.PacketPlayInLook)) {
                            CustomPacketHandler.this.handleFlyPacket((PacketPlayInFlying) packet, playerData);
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        } else if((packet instanceof PacketPlayInKeepAlive)) {
                            CustomPacketHandler.this.handleKeepAlive((PacketPlayInKeepAlive) packet, playerData, player);
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        } else if((packet instanceof PacketPlayInUseEntity)) {
                            CustomPacketHandler.this.handleUseEntity((PacketPlayInUseEntity) packet, playerData, player);
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        } else if((packet instanceof PacketPlayInBlockPlace)) {
                            playerData.setPlacing(true);
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        } else if((packet instanceof PacketPlayInCloseWindow)) {
                            playerData.setInventoryOpen(false);
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        } else if((packet instanceof PacketPlayInSteerVehicle) || (packet instanceof PacketPlayInHeldItemSlot)) {
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        }
                        else if((packet instanceof PacketPlayInClientCommand)) {
                            if (((PacketPlayInClientCommand) packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
                                playerData.setInventoryOpen(true);
                            }
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        } else if((packet instanceof PacketPlayInEntityAction)) {
                            PacketPlayInEntityAction.EnumPlayerAction actionType = ((PacketPlayInEntityAction) packet).b();
                            if (actionType == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING) {
                                playerData.setSprinting(true);
                            }
                            if (actionType == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                                playerData.setSprinting(false);
                            }
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        } else if((packet instanceof PacketPlayInBlockDig)) {
                            PacketPlayInBlockDig.EnumPlayerDigType digType = ((PacketPlayInBlockDig) packet).c();
                            if (playerData.getFakeBlocks().contains(((PacketPlayInBlockDig) packet).a())) {
                                playerData.setInstantBreakDigging(false);
                                playerData.setFakeDigging(true);
                                playerData.setDigging(false);
                            } else {
                                playerData.setFakeDigging(false);

                                if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                                    Block block = ((CraftWorld) player.getWorld()).getHandle().c(
                                            ((PacketPlayInBlockDig) packet).a());

                                    String tile = block.a().replace("tile.", "");

                                    if (INSTANT_BREAK_BLOCKS.contains(tile)) {
                                        playerData.setInstantBreakDigging(true);
                                    } else {
                                        playerData.setInstantBreakDigging(false);
                                    }

                                    playerData.setDigging(true);
                                } else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK ||
                                        digType == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                                    playerData.setInstantBreakDigging(false);
                                    playerData.setDigging(false);
                                }
                            }
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        } else if((packet instanceof PacketPlayInArmAnimation)) {
                            playerData.setLastAnimationPacket(System.currentTimeMillis());
                            CustomPacketHandler.this.handleCheck(playerData, packet, player);
                        }
                    }
                });
            }
        }
    }

    public void handleCheck(PlayerData playerData, Packet packet, Player player) {
        for (Class<? extends ICheck> checkClass : PlayerData.CHECKS) {
            ICheck check = playerData.getCheck(checkClass);
            if (check != null && check.getType() == Packet.class) {
                check.handleCheck(player, packet);
            }
        }
    }

    public void registerHandleSentPacket() {
        for(PacketType type : PacketType.Play.Server.getInstance().values()) {
            if (type.isSupported()) {
                ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(LatinAC.getInstance(), type) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        Player player = event.getPlayer();
                        Packet packet = (Packet) event.getPacket().getHandle();
                        if (player.hasPermission("latinac.bypass")) return;
                        if (player.getAllowFlight()) return;
                        if (player.isOnline()) return;
                        PlayerData playerData = CustomPacketHandler.this.plugin.getPlayerDataManager().getPlayerData(player);
                        if (playerData == null) return;
                        if (playerData.isBanned()) return;

                        if((packet instanceof PacketPlayOutEntityVelocity)) {
                            CustomPacketHandler.this.handleVelocityOut((PacketPlayOutEntityVelocity) packet, playerData, player);
                        } else if((packet instanceof PacketPlayOutExplosion)) {
                            CustomPacketHandler.this.handleExplosionPacket((PacketPlayOutExplosion) packet, playerData);
                        } else if((packet instanceof PacketPlayOutEntity)) {
                            CustomPacketHandler.this.handleEntityPacket((PacketPlayOutEntity) packet, playerData, player);
                        } else if((packet instanceof PacketPlayOutEntityTeleport)) {
                            CustomPacketHandler.this.handleTeleportPacket((PacketPlayOutEntityTeleport) packet, playerData, player);
                        } else if((packet instanceof PacketPlayOutPosition)) {
                            CustomPacketHandler.this.handlePositionPacket((PacketPlayOutPosition) packet, playerData);
                        } else if((packet instanceof PacketPlayOutKeepAlive)) {
                            playerData.addKeepAliveTime(((PacketPlayOutKeepAlive) packet).getA());
                            playerData.setLastSentKeepAlive(System.currentTimeMillis());
                        } else if((packet instanceof PacketPlayOutCloseWindow)) {
                            if (!playerData.keepAliveExists(-1)) {
                                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutKeepAlive(-1));
                            }
                        } else if((packet instanceof PacketPlayOutMultiBlockChange)) {
                            for (PacketPlayOutMultiBlockChange.MultiBlockChangeInfo info : ((PacketPlayOutMultiBlockChange) packet).getB()) {
                                BlockPosition position = info.a();

                                String name = info.c().getBlock().toString().replace("Block{minecraft:", "").replace("}", "");
                                StackTraceElement[] elements = Thread.currentThread().getStackTrace();

                                if (elements.length == 19 && elements[3].getMethodName().equals("sendTo")) {
                                    if (name.equals("air")) {
                                        playerData.getFakeBlocks().remove(position);
                                    } else {
                                        playerData.getFakeBlocks().add(position);
                                    }
                                }
                            }
                        } else if((packet instanceof PacketPlayOutBlockChange)) {
                            BlockPosition position = ((PacketPlayOutBlockChange) packet).getPosition();

                            String name = ((PacketPlayOutBlockChange) packet).block.getBlock().toString().replace("Block{minecraft:", "").replace("}", "");
                            StackTraceElement[] elements = Thread.currentThread().getStackTrace();

                            if (elements.length == 13 && elements[3].getMethodName().equals("sendBlockChange")) {
                                if (name.equals("air")) {
                                    playerData.getFakeBlocks().remove(position);
                                } else {
                                    playerData.getFakeBlocks().add(position);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private void handleFlyPacket(PacketPlayInFlying packet, PlayerData playerData) {
        long now = System.currentTimeMillis();
        CustomLocation customLocation = new CustomLocation(packet.a(), packet.b(), packet.c(), packet.d(), packet.e());
        CustomLocation lastLocation = playerData.getLastMovePacket();
        if (lastLocation != null) {
            if (!packet.g()) {
                customLocation.setX(lastLocation.getX());
                customLocation.setY(lastLocation.getY());
                customLocation.setZ(lastLocation.getZ());
            }
            if (!packet.h()) {
                customLocation.setYaw(lastLocation.getYaw());
                customLocation.setPitch(lastLocation.getPitch());
            }
            if (System.currentTimeMillis() - lastLocation.getTimestamp() > 110L) {
                playerData.setLastDelayedMovePacket(System.currentTimeMillis());
            }
        }
        if (playerData.isSetInventoryOpen()) {
            playerData.setInventoryOpen(false);
            playerData.setSetInventoryOpen(false);
        }

        if (now - playerData.getLastFlying() >= 110L) {
            playerData.setLastDelayedPacket(now);
        }

        if (playerData.getPlayerLocations().size() > 50) {
            playerData.getPlayerLocations().clear();
        }
        if (playerData.getPlayerLocations().size() > 20) {
            playerData.getPlayerLocations().remove(0);
        }
        playerData.getPlayerLocations().add(customLocation);
        playerData.setTotalTicks(playerData.getTotalTicks() + 1);
        playerData.setLastFlying(now);
        playerData.setLastMovePacket(customLocation);
        playerData.setPlacing(false);
        playerData.setAllowTeleport(false);
        playerData.setLastLocation(customLocation);

        long diff = now - playerData.getLastFlying();
        if(diff > 80L) {
            playerData.setLastDelayed(now);
        }
        if (diff < 25L) {
            playerData.setLastFast(now);
        }
        if (packet instanceof PacketPlayInFlying.PacketPlayInPositionLook && playerData.allowTeleport(customLocation)) {
            playerData.setAllowTeleport(true);
        }
    }

    private void handleKeepAlive(PacketPlayInKeepAlive packet, PlayerData playerData, Player player) {
        int id = packet.a();
        playerData.setLastKeepAlive(System.currentTimeMillis());
        playerData.setReceivedKeepAlive(true);
        if (playerData.keepAliveExists(id)) {
            if (id == -1) {
                playerData.setSetInventoryOpen(true);
            } else {
                playerData.setPing((int)(System.currentTimeMillis() - playerData.getKeepAliveTime(id)));
                playerData.setAveragePing((playerData.getAveragePing() * 3 + playerData.getPing()) / 4);
            }
            playerData.removeKeepAliveTime(id);
        }
    }

    private void handleUseEntity(PacketPlayInUseEntity packet, PlayerData playerData, Player player) {
        if (packet.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            playerData.setLastAttackPacket(System.currentTimeMillis());
            if (!playerData.isAttackedSinceVelocity()) {
                playerData.setVelocityX(playerData.getVelocityX() * 0.6);
                playerData.setVelocityZ(playerData.getVelocityZ() * 0.6);
                playerData.setAttackedSinceVelocity(true);
            }
            Entity targetEntity = packet.a(((CraftEntity) player).getHandle().getWorld());
            if (targetEntity instanceof EntityPlayer) {
                Player target = (Player) targetEntity.getBukkitEntity();
                playerData.setLastTarget(target);
            }
            if (packet.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
                playerData.setInteract(System.currentTimeMillis());
            }
        }
    }

    private void handleVelocityOut(PacketPlayOutEntityVelocity packet, PlayerData playerData, Player player) {
        if (packet.getA() == player.getEntityId()) {
            double x = Math.abs(packet.getB() / 8000.0);
            double y = packet.getC() / 8000.0;
            double z = Math.abs(packet.getD() / 8000.0);
            if (x > 0.0 || z > 0.0) {
                playerData.setVelocityH((int) (((x + z) / 2.0 + 2.0) * 15.0));
            }
            if (y > 0.0) {
                playerData.setVelocityV((int) (Math.pow(y + 2.0, 2.0) * 5.0));
                if (playerData.isOnGround() && player.getLocation().getY() % 1.0 == 0.0) {
                    playerData.setVelocityX(x);
                    playerData.setVelocityY(y);
                    playerData.setVelocityZ(z);
                    playerData.setLastVelocity(System.currentTimeMillis());
                    playerData.setAttackedSinceVelocity(false);
                }
            }
            playerData.setVelocityTicks(Math.min(playerData.getVelocityTicks(), 0) - (int)Math.ceil(Math.pow(MathUtil.hypotSquared(x, y, z) * 2.0, 1.75) * 4.0));
        }
    }

    private void handleExplosionPacket(PacketPlayOutExplosion packet, PlayerData playerData) {
        float x = Math.abs(packet.getF());
        float y = packet.getG();
        float z = Math.abs(packet.getH());
        if (x > 0.0f || z > 0.0f) {
            playerData.setVelocityH((int) (((x + z) / 2.0f + 2.0f) * 15.0f));
        }
        if (y > 0.0f) {
            playerData.setVelocityV((int) (Math.pow(y + 2.0f, 2.0) * 5.0));
        }
    }

    private void handleEntityPacket(PacketPlayOutEntity packet, PlayerData playerData, Player player) {
        Entity targetEntity = ((CraftEntity) player).getHandle().getWorld().a(packet.getA());
        if (targetEntity instanceof EntityPlayer) {
            Player target = (Player) targetEntity.getBukkitEntity();
            CustomLocation customLocation = playerData.getLastPlayerPacket(target.getUniqueId(), 1);
            if (customLocation != null) {
                double x = packet.getB() / 32.0;
                double y = packet.getC() / 32.0;
                double z = packet.getD() / 32.0;
                float yaw = packet.getE() * 360.0f / 256.0f;
                float pitch = packet.getF() * 360.0f / 256.0f;
                if (!packet.isH()) {
                    yaw = customLocation.getYaw();
                    pitch = customLocation.getPitch();
                }
                playerData.addPlayerPacket(target.getUniqueId(), new CustomLocation(customLocation.getX() + x, customLocation.getY() + y, customLocation.getZ() + z, yaw, pitch));
            }
        }
    }

    private void handleTeleportPacket(PacketPlayOutEntityTeleport packet, PlayerData playerData, Player player) {
        Entity targetEntity = ((CraftEntity) player).getHandle().getWorld().a(packet.getA());
        if (targetEntity instanceof EntityPlayer) {
            Player target = (Player) targetEntity.getBukkitEntity();
            double x = packet.getB() / 32.0;
            double y = packet.getC() / 32.0;
            double z = packet.getD() / 32.0;
            float yaw = packet.getE() * 360.0f / 256.0f;
            float pitch = packet.getF() * 360.0f / 256.0f;

            if (playerData.getMisplace() != 0.0) {
                CustomLocation lastLocation = playerData.getLastMovePacket();
                float entityYaw = this.getAngle(x, z, lastLocation);
                double addX = Math.cos(Math.toRadians(entityYaw + 90.0f)) * playerData.getMisplace();
                double addZ = Math.sin(Math.toRadians(entityYaw + 90.0f)) * playerData.getMisplace();
                x -= addX;
                z -= addZ;
                packet.setB(MathHelper.floor(x * 32.0));
                packet.setD(MathHelper.floor(z * 32.0));
            }

            playerData.addPlayerPacket(target.getUniqueId(), new CustomLocation(x, y, z, yaw, pitch));
        }
    }

    private void handlePositionPacket(PacketPlayOutPosition packet, PlayerData playerData) {
        if (packet.getE() > 90.0f) {
            packet.setE(90.0f);
        } else if (packet.getE() < -90.0f) {
            packet.setE(-90.0f);
        } else if (packet.getE() == 0.0f) {
            packet.setE(0.492832f);
        }
        playerData.setVelocityY(0.0);
        playerData.setVelocityX(0.0);
        playerData.setVelocityZ(0.0);
        playerData.setAttackedSinceVelocity(false);
        playerData.addTeleportLocation(new CustomLocation(packet.getA(), packet.getB(), packet.getC(), packet.getD(), packet.getE()));
        playerData.setTeleportTicks(0);
    }

    private float getAngle(double posX, double posZ, CustomLocation location) {
        double x = posX - location.getX();
        double z = posZ - location.getZ();
        float newYaw = (float) Math.toDegrees(-Math.atan(x / z));
        if (z < 0.0 && x < 0.0) {
            newYaw = (float) (90.0 + Math.toDegrees(Math.atan(z / x)));
        } else if (z < 0.0 && x > 0.0) {
            newYaw = (float) (-90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        return newYaw;
    }
}
