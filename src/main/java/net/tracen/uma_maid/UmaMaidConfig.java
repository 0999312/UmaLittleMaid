package net.tracen.uma_maid;

import net.neoforged.neoforge.common.ModConfigSpec;

public class UmaMaidConfig {
    public static ModConfigSpec.BooleanValue RENDER_UMAMUSUME;

    public static ModConfigSpec init() {
    	ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Whether to render umamusume when maid equipped the soul.");
        RENDER_UMAMUSUME = builder.define("renderUmamusume", true);

        return builder.build();
    }
}