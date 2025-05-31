package net.tracen.uma_maid;

import net.minecraftforge.common.ForgeConfigSpec;

public class UmaMaidConfig {
    public static ForgeConfigSpec.BooleanValue RENDER_UMAMUSUME;

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Whether to render umamusume when maid equipped the soul.");
        RENDER_UMAMUSUME = builder.define("renderUmamusume", true);

        return builder.build();
    }
}