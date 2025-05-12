package net.tracen.uma_maid.utils;

import java.util.HashMap;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockModelPOJO;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TLMClientUtils {
    // 很蠢的做法，但是就这样吧
	// 由于和马儿蹦跳使用同一模型，所以不需要额外输出日志判定
	public static final HashMap<ResourceLocation, BedrockModelPOJO> MODEL_MAP = Maps.newHashMap();

	@OnlyIn(Dist.CLIENT)
	public static void loadModel(ResourceLocation modelLocation, JsonElement element) {
		BedrockModelPOJO pojo = CustomPackLoader.GSON.fromJson(element, BedrockModelPOJO.class);
		if (pojo.getFormatVersion() == null) {
			return;
		} else {
			if (BedrockVersion.isLegacyVersion(pojo)) {
				if (pojo.getGeometryModelLegacy() != null) {
					MODEL_MAP.put(modelLocation, pojo);
					return;
				} else {
					return;
				}
			}

			if (BedrockVersion.isNewVersion(pojo)) {
				if (pojo.getGeometryModelNew() != null) {
					MODEL_MAP.put(modelLocation, pojo);
					return;
				} else {
					return;
				}
			}

		}
	}
}
