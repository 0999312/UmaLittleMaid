package net.tracen.uma_maid.client;

import java.util.Map;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.tracen.uma_maid.utils.TLMClientUtils;

public class TLMResourceLoader extends SimpleJsonResourceReloadListener {
    public TLMResourceLoader(String path) {
        super(CustomPackLoader.GSON, path);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
    	TLMClientUtils.MODEL_MAP.clear();
        for(var entry : map.entrySet()) {
            TLMClientUtils.loadModel(entry.getKey(), entry.getValue());
        }
    }
}
