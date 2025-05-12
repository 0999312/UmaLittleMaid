package net.tracen.uma_maid;

import com.mojang.logging.LogUtils;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tracen.uma_maid.network.NetworkPacketHandler;

import org.slf4j.Logger;

@Mod(UmaMaid.MODID)
public class UmaMaid {
	public static final String MODID = "uma_maid";
	private static final Logger LOGGER = LogUtils.getLogger();

	public UmaMaid() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UmaMaidConfig.SPEC);
	}
	
    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkPacketHandler::registerMessage);
    }

	public static Logger getLogger() {
		return LOGGER;
	}

}
