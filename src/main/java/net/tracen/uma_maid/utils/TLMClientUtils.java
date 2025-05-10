package net.tracen.uma_maid.utils;

import java.util.HashMap;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockModelPOJO;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import cn.mcmod_mmf.mmlib.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TLMClientUtils {
    
    // 很蠢的做法，但是就这样吧
	public static final HashMap<ResourceLocation, BedrockModelPOJO> MODEL_MAP = Maps.newHashMap();

	@OnlyIn(Dist.CLIENT)
	public static void loadModel(ResourceLocation modelLocation, JsonElement element) {
		
		BedrockModelPOJO pojo = CustomPackLoader.GSON.fromJson(element, BedrockModelPOJO.class);

		if (pojo.getFormatVersion() == null) {
			Main.getLogger().error("Failed to load model: {}, it's not a Bedrock Model!", modelLocation);
			return;
		} else {
			// 先判断是不是 1.10.0 版本基岩版模型文件
			if (BedrockVersion.isLegacyVersion(pojo)) {
				// 如果 model 字段不为空
				if (pojo.getGeometryModelLegacy() != null) {
					Main.getLogger().info("Loaded 1.10.0 version model : {}", modelLocation);
					MODEL_MAP.put(modelLocation, pojo);
					return;
				} else {
					// 否则日志给出提示
					Main.getLogger().warn("{} model file don't have model field", modelLocation);
					return;
				}
			}

			// 判定是不是 1.12.0 版本基岩版模型文件
			if (BedrockVersion.isNewVersion(pojo)) {
				// 如果 model 字段不为空
				if (pojo.getGeometryModelNew() != null) {
					MODEL_MAP.put(modelLocation, pojo);
					Main.getLogger().info("Loaded {} version model : {}", pojo.getFormatVersion(), modelLocation);
					return;
				} else {
					// 否则日志给出提示
					Main.getLogger().warn("{} model file don't have model field", modelLocation);
					return;
				}
			}

			Main.getLogger().error("{} model version is not 1.10.0 or new version bedrock model", modelLocation);
		}
	}
}
