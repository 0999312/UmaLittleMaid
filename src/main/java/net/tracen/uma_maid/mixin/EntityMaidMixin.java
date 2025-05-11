package net.tracen.uma_maid.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.tracen.uma_maid.UmaMaidExtension;
import net.tracen.uma_maid.utils.TLMUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@Mixin(value = EntityMaid.class, priority = 10)
public class EntityMaidMixin {

    @Inject(method = "getTypeName", at = @At(value = "HEAD"), cancellable = true)
    private void getTypeNameMixin(CallbackInfoReturnable<Component> cir) {
    	EntityMaid maid = (EntityMaid) (Object) this;
        ItemStack stack = TLMUtils.getBaubleItemInMaid(maid, UmaMaidExtension.UMA_SOUL_BAUBLES);
    	if(!stack.isEmpty()) {
    		cir.setReturnValue(Component.translatable(Util.makeDescriptionId("umadata", UmaSoulUtils.getName(stack))));
    	}
    }
}
