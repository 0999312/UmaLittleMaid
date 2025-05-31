package net.tracen.uma_maid.utils;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import net.minecraft.world.item.ItemStack;

public final class TLMUtils {
	
    public static ItemStack getBaubleItemInMaid(EntityMaid maid, IMaidBauble bauble) {
        
    	BaubleItemHandler handler = maid.getMaidBauble();
        
        for (int i = 0; i < handler.getSlots(); i++) {
            IMaidBauble baubleIn = handler.getBaubleInSlot(i);
            if (baubleIn == bauble) {
                return handler.getStackInSlot(i);
            }
        }
        
        return ItemStack.EMPTY;
    }

}
