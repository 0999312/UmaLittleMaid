package net.tracen.uma_maid.network;

import javax.annotation.ParametersAreNonnullByDefault;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tracen.uma_maid.UmaMaid;
import net.tracen.uma_maid.utils.IPreviousItemHandler;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public record SyncBaublePacket(int id, int index, ItemStack bauble, ItemStack previous) implements CustomPacketPayload {
	public static final Type<SyncBaublePacket> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(UmaMaid.MODID, "sync_bauble"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncBaublePacket> STREAM_CODEC = StreamCodec
			.composite(
					ByteBufCodecs.INT, SyncBaublePacket::id, 
					ByteBufCodecs.INT, SyncBaublePacket::index,
					ItemStack.STREAM_CODEC, SyncBaublePacket::bauble,
					ItemStack.STREAM_CODEC, SyncBaublePacket::previous,
					SyncBaublePacket::new
					);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
	
	public static void handle(SyncBaublePacket payload, IPayloadContext context) {
		if(context.flow().isClientbound()) {
			 context.enqueueWork(() -> syncItem(payload));
		}
	}
	
    @OnlyIn(Dist.CLIENT)
    private static void syncItem(SyncBaublePacket message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(message.id);
        if (e instanceof EntityMaid maid && maid.isAlive()) {
            maid.getMaidBauble().setStackInSlot(message.index, message.bauble);
            IPreviousItemHandler handler = (IPreviousItemHandler) maid.getMaidBauble();
            handler.setPreviousStack(message.previous);
        }
    }
}
