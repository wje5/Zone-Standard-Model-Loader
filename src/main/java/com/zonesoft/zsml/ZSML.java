package com.zonesoft.zsml;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zonesoft.zsml.demo.DemoRenderer;
import com.zonesoft.zsml.demo.EntityLoader;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("zsml")
public class ZSML {
	public static final Logger LOGGER = LogManager.getLogger();
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "zsml");
	public static RegistryObject<Item> trumpet = ITEMS.register("trumpet",
			() -> new Item(new Properties().group(ItemGroup.MISC).maxStackSize(1).setISTER(() -> ITESRTrumpet::new)));

	public ZSML() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.register(this);
		EntityLoader.ENTITYS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
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
		@SubscribeEvent
		public static void onModelBaked(ModelBakeEvent event) {
			Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
			ModelResourceLocation location = new ModelResourceLocation(trumpet.get().getRegistryName(), "inventory");
			event.getModelRegistry().put(location, new TrumpetBakedModel(modelRegistry.get(location)));
		}
	}

	public static class TrumpetBakedModel implements IBakedModel {
		private IBakedModel old;

		public TrumpetBakedModel(IBakedModel old) {
			this.old = old;
		}

		@Override
		public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
			return old.getQuads(state, side, rand);
		}

		@Override
		public boolean isAmbientOcclusion() {
			return old.isAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return old.isGui3d();
		}

		@Override
		public boolean isSideLit() {
			return old.isSideLit();
		}

		@Override
		public boolean isBuiltInRenderer() {
			return true;
		}

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return old.getParticleTexture();
		}

		@Override
		public ItemOverrideList getOverrides() {
			return old.getOverrides();
		}
	}
}
