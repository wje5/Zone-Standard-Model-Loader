package com.zonesoft.zsml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zonesoft.zsml.model.ModelLoader;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
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
	}

	private void setup(final FMLCommonSetupEvent event) {

	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		ModelLoader.init();
		System.out.println(ModelLoader.getModel(new ResourceLocation("zsml:models/scene.gltf")));
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

	}
}
