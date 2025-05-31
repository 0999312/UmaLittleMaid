package net.tracen.uma_maid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.tracen.uma_maid.baubles.UmaSoulBaubles;
import net.tracen.umapyoi.item.ItemRegistry;

@LittleMaidExtension
public class UmaMaidExtension implements ILittleMaid {
    public static final UmaSoulBaubles UMA_SOUL_BAUBLES = new UmaSoulBaubles();

    @Override
    public void bindMaidBauble(BaubleManager manager) {
        manager.bind(ItemRegistry.UMA_SOUL, UMA_SOUL_BAUBLES);
    }
}
