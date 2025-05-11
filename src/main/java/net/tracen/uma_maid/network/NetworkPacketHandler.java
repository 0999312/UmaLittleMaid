package net.tracen.uma_maid.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tracen.uma_maid.UmaMaid;

public class NetworkPacketHandler {
	public static SimpleChannel INSTANCE;
	public static final String PROTOCOL_VERSION = "1.0";
	private static int ID = 0;

	public static int nextID() {
		return ID++;
	}

	public static void registerMessage() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(UmaMaid.MODID, "network"),
				() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

		INSTANCE.messageBuilder(SyncBaublePacket.class, nextID(), NetworkDirection.PLAY_TO_CLIENT)
				.encoder(SyncBaublePacket::toBytes).decoder(SyncBaublePacket::new)
				.consumerNetworkThread(SyncBaublePacket::handle).add();
	}
	
    public static void sendToClientPlayer(Object message, Player player) {
    	INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }
}
