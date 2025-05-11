package net.tracen.uma_maid;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = UmaMaid.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UmaMaidConfig {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.BooleanValue RENDER_UMAMUSUME = BUILDER
            .comment("Whether to render umamusume when maid equiped the soul.")
            .define("renderUmamusume", true);
	public static final ForgeConfigSpec SPEC = BUILDER.build();
	public static boolean renderEnable;
	@SubscribeEvent
	public static void onLoad(final ModConfigEvent event) {
		renderEnable = RENDER_UMAMUSUME.get();
	}
}
