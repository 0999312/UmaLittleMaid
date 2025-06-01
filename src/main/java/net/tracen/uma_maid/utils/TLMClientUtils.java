package net.tracen.uma_maid.utils;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockModelPOJO;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tracen.uma_maid.client.UmamusumeTLMModel;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class TLMClientUtils {
    // 很蠢的做法，但是就这样吧
    // 由于和马儿蹦跳使用同一模型，所以不需要额外输出日志判定
    public static final HashMap<ResourceLocation, UmamusumeTLMModel> MODEL_MAP = Maps.newHashMap();

    @OnlyIn(Dist.CLIENT)
    public static void loadModel(ResourceLocation modelLocation, JsonElement element) {
        BedrockModelPOJO pojo = CustomPackLoader.GSON.fromJson(element, BedrockModelPOJO.class);
        if (pojo.getFormatVersion() == null) {
            return;
        }
        if (BedrockVersion.isLegacyVersion(pojo) && pojo.getGeometryModelLegacy() != null) {
            MODEL_MAP.put(modelLocation, new UmamusumeTLMModel(pojo));
            return;
        }
        if (BedrockVersion.isNewVersion(pojo) && pojo.getGeometryModelNew() != null) {
            MODEL_MAP.put(modelLocation, new UmamusumeTLMModel(pojo));
        }
    }
}
