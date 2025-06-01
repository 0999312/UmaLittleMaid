package net.tracen.uma_maid;

import com.mojang.logging.LogUtils;
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
    public static final Logger LOGGER = LogUtils.getLogger();

    public UmaMaid() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UmaMaidConfig.init());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkPacketHandler::registerMessage);
    }
}
