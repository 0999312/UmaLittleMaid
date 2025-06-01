package net.tracen.uma_maid.mixin;

import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import net.minecraft.world.item.ItemStack;
import net.tracen.uma_maid.utils.IPreviousItemHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BaubleItemHandler.class, priority = 10)
public class BaubleItemHandlerMixin implements IPreviousItemHandler {
    private ItemStack previousItems = initPrevious();

    private ItemStack initPrevious() {
        return ItemStack.EMPTY;
    }

    @Override
    public void setPreviousStack(ItemStack stack) {
        previousItems = stack;
    }

    @Override
    public ItemStack getPreviousStack() {
        return previousItems;
    }
}
