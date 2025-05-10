package net.tracen.uma_maid.client;

import java.util.Map;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.gson.JsonElement;

import cn.mcmod_mmf.mmlib.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.tracen.uma_maid.utils.TLMClientUtils;

public class TLMResourceLoader extends SimpleJsonResourceReloadListener {
    private final String resource_path;
    public TLMResourceLoader(String path) {
        super(CustomPackLoader.GSON, path);
        this.resource_path = path;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
    	TLMClientUtils.MODEL_MAP.clear();
        Main.getLogger().info("Started Loading Bedrock Model for TLM from : {}", resource_path);
        if(map.isEmpty())
            Main.getLogger().error("{} is an empty folder!", resource_path);
        for(var entry : map.entrySet()) {
            Main.getLogger().info("Loading Bedrock Model for TLM Loading : {}", entry.getKey().toString());
            TLMClientUtils.loadModel(entry.getKey(), entry.getValue());
        }
    }
}
