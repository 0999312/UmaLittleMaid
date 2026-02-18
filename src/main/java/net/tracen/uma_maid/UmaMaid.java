package net.tracen.uma_maid;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(UmaMaid.MODID)
public class UmaMaid {
    public static final String MODID = "uma_maid";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UmaMaid() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UmaMaidConfig.init());
    }
}
