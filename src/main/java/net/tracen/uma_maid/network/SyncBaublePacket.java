package net.tracen.uma_maid.network;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.tracen.uma_maid.utils.IPreviousItemHandler;

import java.util.function.Supplier;

public class SyncBaublePacket {
    private final int id;
    private final int index;
    private final ItemStack bauble;
    private final ItemStack previous;

    public SyncBaublePacket(FriendlyByteBuf buffer) {
        id = buffer.readInt();
        index = buffer.readInt();
        bauble = buffer.readItem();
        previous = buffer.readItem();
    }

    public SyncBaublePacket(int id, int index, ItemStack bauble, ItemStack previous) {
        this.id = id;
        this.index = index;
        this.bauble = bauble;
        this.previous = previous;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeInt(this.index);
        buf.writeItem(this.bauble);
        buf.writeItem(this.previous);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> syncItem(this));
        }
        context.setPacketHandled(true);
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
