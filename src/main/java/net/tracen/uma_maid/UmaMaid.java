package net.tracen.uma_maid;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(UmaMaid.MODID)
public class UmaMaid {
	public static final String MODID = "uma_maid";
	private static final Logger LOGGER = LogUtils.getLogger();

	public UmaMaid() {
//		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

//		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	public static Logger getLogger() {
		return LOGGER;
	}

}
