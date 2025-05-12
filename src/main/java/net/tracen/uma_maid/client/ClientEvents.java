package net.tracen.uma_maid.client;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
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
		
		if(!UmaMaidConfig.renderEnable)
			return;
		IMaid maid = event.getMaid();
		if (maid == null) 
			return;
		ItemStack soul = TLMUtils.getBaubleItemInMaid(maid.asStrictMaid(), UmaMaidExtension.UMA_SOUL_BAUBLES);
		if(soul.isEmpty())
			return;
		
		BedrockModelPOJO pojo = TLMClientUtils.MODEL_MAP.get(UmaSoulUtils.getName(soul));
		if(pojo == null)
			return;
		BedrockModel<Mob> model = new UmamusumeTLMModel(pojo);
		
		event.getModelData().setModel(model);
		event.getModelData().setAnimations(CustomPackLoader.MAID_MODELS.getAnimation(DEFAULT_MODEL_ID).orElseThrow());
		event.getModelData().setInfo(new MaidModelInfo() {
			@Override
			public String getName() {
				return UmaSoulUtils.getName(soul).toLanguageKey();
			}
			
			@Override
			public ResourceLocation getTexture() {
				return ClientUtils.getTexture(UmaSoulUtils.getName(soul));
			}
		});
		event.setCanceled(true);
	}

}
