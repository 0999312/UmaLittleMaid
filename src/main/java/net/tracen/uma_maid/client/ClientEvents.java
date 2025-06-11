package net.tracen.uma_maid.client;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTypeNameEvent;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.tracen.uma_maid.UmaMaidConfig;
import net.tracen.uma_maid.UmaMaidExtension;
import net.tracen.uma_maid.utils.TLMClientUtils;
import net.tracen.uma_maid.utils.TLMUtils;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

@EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(value = Dist.CLIENT)
public class ClientEvents {
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMaidRendering(RenderMaidEvent event) {
        if (!UmaMaidConfig.RENDER_UMAMUSUME.get()) {
            return;
        }
        EntityMaid maid = event.getMaid().asStrictMaid();
        if (maid == null) {
            return;
        }
        ItemStack soul = TLMUtils.getBaubleItemInMaid(maid, UmaMaidExtension.UMA_SOUL_BAUBLES);
        if (soul.isEmpty()) {
            return;
        }
        ResourceLocation name = UmaSoulUtils.getName(soul);
        UmamusumeTLMModel model = TLMClientUtils.MODEL_MAP.get(name);
        if (model == null) {
            return;
        }
        event.getModelData().setModel(model);
        CustomPackLoader.MAID_MODELS.getAnimation(DEFAULT_MODEL_ID).ifPresent(animation -> event.getModelData().setAnimations(animation));
        event.getModelData().setInfo(new UmaMaidModelInfo(name));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onShowMaidName(MaidTypeNameEvent event) {
        if (!UmaMaidConfig.RENDER_UMAMUSUME.get()) {
            return;
        }
        EntityMaid maid = event.getMaid();
        ItemStack stack = TLMUtils.getBaubleItemInMaid(maid, UmaMaidExtension.UMA_SOUL_BAUBLES);
        if (!stack.isEmpty()) {
            MutableComponent name = Component.translatable(Util.makeDescriptionId("umadata", UmaSoulUtils.getName(stack)));
            event.setTypeName(name);
        }
    }

    private static class UmaMaidModelInfo extends MaidModelInfo {
        private final ResourceLocation name;

        public UmaMaidModelInfo(ResourceLocation name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name.toLanguageKey();
        }

        @Override
        public ResourceLocation getTexture() {
            return ClientUtils.getTexture(name);
        }
    }
}
