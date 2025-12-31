package net.tracen.uma_maid;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(UmaMaid.MODID)
public class UmaMaid {
    public static final String MODID = "uma_maid";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UmaMaid(IEventBus modEventBus, ModContainer modContainer) {
    	modContainer.registerConfig(ModConfig.Type.COMMON, UmaMaidConfig.init());
        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }
}
