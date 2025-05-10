package net.tracen.uma_maid.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {

	@SubscribeEvent
	public static void resourceLoadingListener(final RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(new TLMResourceLoader("models/umapyoi"));
	}

}
