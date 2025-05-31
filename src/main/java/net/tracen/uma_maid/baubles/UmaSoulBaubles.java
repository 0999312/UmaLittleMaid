package net.tracen.uma_maid.baubles;

import java.util.UUID;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tracen.uma_maid.network.NetworkPacketHandler;
import net.tracen.uma_maid.network.SyncBaublePacket;
import net.tracen.uma_maid.utils.IPreviousItemHandler;
import net.tracen.uma_maid.utils.TLMUtils;
import net.tracen.umapyoi.UmapyoiConfig;
import net.tracen.umapyoi.events.SettingPropertyEvent;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

public class UmaSoulBaubles implements IMaidBauble {
	public static final UUID UMASOUL_MAID_UUID = UUID.fromString("90d6e90a-3340-43f5-9cd5-4b232b48608e");

	public UmaSoulBaubles() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onTrackingPlayer(PlayerEvent.StartTracking event)  {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        if (target instanceof EntityMaid maid) {
            BaubleItemHandler handler = maid.getMaidBauble();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack bauble = handler.getStackInSlot(i);
                if(bauble.is(ItemRegistry.UMA_SOUL.get())) {
                	IPreviousItemHandler previousHandler = (IPreviousItemHandler) handler;
	                SyncBaublePacket packet = new SyncBaublePacket(maid.getId(), i, bauble, previousHandler.getPreviousStack());
	                
	                NetworkPacketHandler.sendToClientPlayer(packet, player);
	                return;
                }
            }
        }
	}

	@SubscribeEvent
	public void tick(LivingEvent.LivingTickEvent event) {
		if (event.getEntity() instanceof Mob mob) {
			IMaid maid = IMaid.convert(mob);
			if (maid == null) 
				return;
			
			EntityMaid maidEntity = maid.asStrictMaid();
			if (maidEntity == null) 
				return;
			
			Multimap<Attribute, AttributeModifier> attributeModifiers;
			
	        IPreviousItemHandler handler = (IPreviousItemHandler) maidEntity.getMaidBauble();
	        ItemStack soul = TLMUtils.getBaubleItemInMaid(maidEntity, this);
	        ItemStack previousStack = handler.getPreviousStack();
	        
	        if(!ItemStack.matches(soul, previousStack)) {
	        	if(!previousStack.isEmpty()) {
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

	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EntityMaid maid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
		if(!stack.is(ItemRegistry.UMA_SOUL.get()))
			return atts;
		if (UmaSoulUtils.getGrowth(stack) == Growth.UNTRAINED)
			return atts;

		atts.put(Attributes.MOVEMENT_SPEED,
				new AttributeModifier(UMASOUL_MAID_UUID, "speed_running_bonus",
						getExactProperty(stack, maid, StatusType.SPEED, UmapyoiConfig.UMASOUL_MAX_SPEED.get()),
						UmapyoiConfig.UMASOUL_SPEED_PRECENT_ENABLE.get() ? AttributeModifier.Operation.MULTIPLY_TOTAL
								: AttributeModifier.Operation.ADDITION));

		atts.put(ForgeMod.SWIM_SPEED.get(),
				new AttributeModifier(UMASOUL_MAID_UUID, "speed_swiming_bonus",
						getExactProperty(stack, maid, StatusType.SPEED, UmapyoiConfig.UMASOUL_MAX_SPEED.get()),
						UmapyoiConfig.UMASOUL_SPEED_PRECENT_ENABLE.get() ? AttributeModifier.Operation.MULTIPLY_TOTAL
								: AttributeModifier.Operation.ADDITION));

		atts.put(Attributes.ATTACK_DAMAGE,
				new AttributeModifier(UMASOUL_MAID_UUID, "strength_attack_bonus",
						getExactProperty(stack, maid, StatusType.STRENGTH,
								UmapyoiConfig.UMASOUL_MAX_STRENGTH_ATTACK.get()),
						UmapyoiConfig.UMASOUL_STRENGTH_PRECENT_ENABLE.get() ? AttributeModifier.Operation.MULTIPLY_TOTAL
								: AttributeModifier.Operation.ADDITION));
		atts.put(Attributes.MAX_HEALTH,
				new AttributeModifier(UMASOUL_MAID_UUID, "strength_attack_bonus",
						getExactProperty(stack, maid, StatusType.STAMINA,
								UmapyoiConfig.UMASOUL_MAX_STAMINA_HEALTH.get()),
						UmapyoiConfig.UMASOUL_STAMINA_PRECENT_ENABLE.get() ? AttributeModifier.Operation.MULTIPLY_TOTAL
								: AttributeModifier.Operation.ADDITION));
		atts.put(Attributes.ARMOR,
				new AttributeModifier(UMASOUL_MAID_UUID, "guts_armor_bonus",
						getExactProperty(stack, maid, StatusType.GUTS, UmapyoiConfig.UMASOUL_MAX_GUTS_ARMOR.get()),
						UmapyoiConfig.UMASOUL_GUTS_PRECENT_ENABLE.get() ? AttributeModifier.Operation.MULTIPLY_TOTAL
								: AttributeModifier.Operation.ADDITION));
		atts.put(Attributes.ARMOR_TOUGHNESS,
				new AttributeModifier(UMASOUL_MAID_UUID, "guts_armor_toughness_bonus",
						getExactProperty(stack, maid, StatusType.GUTS,
								UmapyoiConfig.UMASOUL_MAX_GUTS_ARMOR_TOUGHNESS.get()),
						UmapyoiConfig.UMASOUL_GUTS_PRECENT_ENABLE.get() ? AttributeModifier.Operation.MULTIPLY_TOTAL
								: AttributeModifier.Operation.ADDITION));
		return atts;
	}

	public double getExactProperty(ItemStack soul, LivingEntity user, StatusType status, double limit) {
		int num = status.getId();
		var retiredValue = UmaSoulUtils.getGrowth(soul) == Growth.RETIRED ? 1.0D : 0.25D;
		var propertyRate = 1.0D + (UmaSoulUtils.getPropertyRate(soul)[num] / 100.0D);
		var totalProperty = propertyPercentage(soul, num);
		SettingPropertyEvent event = new SettingPropertyEvent(user, soul, retiredValue, propertyRate, totalProperty);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getResultProperty() * limit;
	}

	private double propertyPercentage(ItemStack soul, int num) {
		var x = UmaSoulUtils.getProperty(soul)[num];
		var statLimit = UmapyoiConfig.STAT_LIMIT_VALUE.get();
		var denominator = 1 + Math.pow(Math.E,
				(x > statLimit ? (-0.125 * UmapyoiConfig.STAT_LIMIT_REDUCTION_RATE.get()) : -0.125) * (x - statLimit));
		return 1 / denominator;
	}
}
