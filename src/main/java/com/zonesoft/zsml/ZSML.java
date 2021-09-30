package com.zonesoft.zsml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zonesoft.zsml.demo.DemoRenderer;
import com.zonesoft.zsml.demo.EntityLoader;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("zsml")
public class ZSML {
	public static final Logger LOGGER = LogManager.getLogger();

	public ZSML() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.register(this);
		EntityLoader.ENTITYS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	private void setup(final FMLCommonSetupEvent event) {

	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		ModelLoader.init();
		RenderingRegistry.registerEntityRenderingHandler(EntityLoader.demo.get(), (EntityRendererManager manager) -> {
			return new DemoRenderer(manager);
		});
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

	}
}
