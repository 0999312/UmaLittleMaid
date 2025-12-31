package net.tracen.uma_maid.baubles;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tracen.uma_maid.UmaMaid;
import net.tracen.uma_maid.network.SyncBaublePacket;
import net.tracen.uma_maid.utils.IPreviousItemHandler;
import net.tracen.uma_maid.utils.TLMUtils;
import net.tracen.umapyoi.UmapyoiConfig;
import net.tracen.umapyoi.events.SettingPropertyEvent;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

public class UmaSoulBaubles implements IMaidBauble {
    public static final ResourceLocation UMASOUL_MAID_ID = ResourceLocation.fromNamespaceAndPath(UmaMaid.MODID, "umasoul_maid");

    public UmaSoulBaubles() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTrackingPlayer(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        if(player instanceof ServerPlayer serverPlayer) {
	        if (!(target instanceof EntityMaid maid)) {
	            return;
	        }
	        BaubleItemHandler handler = maid.getMaidBauble();
	        for (int i = 0; i < handler.getSlots(); i++) {
	            ItemStack bauble = handler.getStackInSlot(i);
	            if (!bauble.is(ItemRegistry.UMA_SOUL.get())) {
	                continue;
	            }
	            IPreviousItemHandler previousHandler = (IPreviousItemHandler) handler;
	            SyncBaublePacket packet = new SyncBaublePacket(maid.getId(), i, bauble, previousHandler.getPreviousStack());
	            PacketDistributor.sendToPlayer(serverPlayer, packet);
	            return;
	        }
        }
    }

    @SubscribeEvent
    public void tick(MaidTickEvent event) {
        if (event.getEntity() instanceof EntityMaid maidEntity) {
        	Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers;

            IPreviousItemHandler handler = (IPreviousItemHandler) maidEntity.getMaidBauble();
            ItemStack soul = TLMUtils.getBaubleItemInMaid(maidEntity, this);
            ItemStack previousStack = handler.getPreviousStack();

            if (!ItemStack.matches(soul, previousStack)) {
                if (!previousStack.isEmpty()) {
                    attributeModifiers = this.getAttributeModifiers(maidEntity, previousStack);
                    maidEntity.getAttributes().removeAttributeModifiers(attributeModifiers);
                    handler.setPreviousStack(ItemStack.EMPTY);
                }
            }

            if (!TLMUtils.getBaubleItemInMaid(maidEntity, this).isEmpty()) {
                attributeModifiers = this.getAttributeModifiers(maidEntity, soul);
                maidEntity.getAttributes().addTransientAttributeModifiers(attributeModifiers);
                handler.setPreviousStack(soul);
            }
        }
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(EntityMaid maid, ItemStack stack) {
    	Multimap<Holder<Attribute>, AttributeModifier> atts = LinkedHashMultimap.create();
        if (!stack.is(ItemRegistry.UMA_SOUL.get())) {
            return atts;
        }

        atts.put(Attributes.MOVEMENT_SPEED,
                new AttributeModifier(UMASOUL_MAID_ID, getExactProperty(stack, maid, StatusType.SPEED,
               		 UmapyoiConfig.UMASOUL_MAX_SPEED.get()),
                        UmapyoiConfig.UMASOUL_SPEED_PRECENT_ENABLE.get() ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));
       
        atts.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(UMASOUL_MAID_ID, getExactProperty(stack, maid, StatusType.STRENGTH,
               		 UmapyoiConfig.UMASOUL_MAX_STRENGTH_ATTACK.get()),
                        UmapyoiConfig.UMASOUL_STRENGTH_PRECENT_ENABLE.get() ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));
        atts.put(Attributes.MAX_HEALTH, 
                new AttributeModifier(UMASOUL_MAID_ID, getExactProperty(stack, maid, StatusType.STAMINA,
               		 UmapyoiConfig.UMASOUL_MAX_STAMINA_HEALTH.get()),
                        UmapyoiConfig.UMASOUL_STAMINA_PRECENT_ENABLE.get() ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));
        atts.put(Attributes.ARMOR,
                new AttributeModifier(UMASOUL_MAID_ID, getExactProperty(stack, maid, StatusType.GUTS,
               		 UmapyoiConfig.UMASOUL_MAX_GUTS_ARMOR.get()),
                        UmapyoiConfig.UMASOUL_GUTS_PRECENT_ENABLE.get() ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));
        atts.put(Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(UMASOUL_MAID_ID, getExactProperty(stack, maid, StatusType.GUTS,
               		 UmapyoiConfig.UMASOUL_MAX_GUTS_ARMOR_TOUGHNESS.get()),
                        UmapyoiConfig.UMASOUL_GUTS_PRECENT_ENABLE.get() ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));
        return atts;
    }

    public double getExactProperty(ItemStack stack, LivingEntity user, StatusType type, double limit) {
        var retiredValue = !(stack.has(DataComponentsTypeRegistry.UMADATA_TRAINING)) ? 1.0D : 0.25D;
        int rate = 0;
    	switch (type) {
			case SPEED -> rate = UmaSoulUtils.getPropertyRate(stack).speed();
			case STAMINA -> rate = UmaSoulUtils.getPropertyRate(stack).stamina();
			case STRENGTH -> rate = UmaSoulUtils.getPropertyRate(stack).strength();
			case GUTS -> rate = UmaSoulUtils.getPropertyRate(stack).guts();
			case WISDOM -> rate = UmaSoulUtils.getPropertyRate(stack).wisdom();
		}
        var propertyRate = 1.0D + (rate / 100.0D);
        var totalProperty = propertyPercentage(stack, type);
        SettingPropertyEvent event = new SettingPropertyEvent(user, stack, retiredValue, propertyRate, totalProperty);
        NeoForge.EVENT_BUS.post(event);
        return event.getResultProperty() * limit;
    }

    private double propertyPercentage(ItemStack stack, StatusType type) {
    	int x = 0;
    	switch (type) {
			case SPEED -> x = UmaSoulUtils.getProperty(stack).speed();
			case STAMINA -> x = UmaSoulUtils.getProperty(stack).stamina();
			case STRENGTH -> x = UmaSoulUtils.getProperty(stack).strength();
			case GUTS -> x = UmaSoulUtils.getProperty(stack).guts();
			case WISDOM -> x = UmaSoulUtils.getProperty(stack).wisdom();
		}
        
        var statLimit = UmapyoiConfig.STAT_LIMIT_VALUE.get();
        var denominator = 1 + Math.pow(Math.E, 
                (x > statLimit ? (-0.125 * UmapyoiConfig.STAT_LIMIT_REDUCTION_RATE.get()) : -0.125) * 
                (x - statLimit));
        return 1 / denominator;
    }
}
