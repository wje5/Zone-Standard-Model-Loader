package com.zonesoft.zsml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.zonesoft.zsml.model.gltf.GLTFReader;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;

public class ModelLoader {
	private static Set<IModelReader> readers = new HashSet<IModelReader>();

	private static Map<ResourceLocation, AbstractModel> models = new HashMap<ResourceLocation, AbstractModel>();

	private static Map<AbstractModel, ModelRenderer> renderers = new HashMap<AbstractModel, ModelRenderer>();

	public static void init() {
		registerReader(new GLTFReader());
	}

	public static void registerReader(IModelReader reader) {
		readers.add(reader);
	}

	public static AbstractModel getModel(ResourceLocation location) {
		AbstractModel model = models.get(location);
		if (model != null) {
			return model;
		}
		for (IModelReader reader : readers) {
			model = reader.read(location);
			if (model != null) {
				models.put(location, model);
				return model;
			}
		}
		return null;
	}

	public static ModelRenderer getRenderer(AbstractModel model) {
		ModelRenderer renderer = renderers.get(model);
		if (renderer == null) {
			renderer = model.createNewRenderer();
			renderers.put(model, renderer);
		}
		return renderer;
	}

	public static void doRender(MatrixStack stack, IRenderTypeBuffer buffer, AbstractModel model, int lightLevel) {
		getRenderer(model).doRender(stack, buffer, lightLevel);
	}

	public static void doRender(MatrixStack stack, IRenderTypeBuffer buffer, ResourceLocation location,
			int lightLevel) {
		doRender(stack, buffer, getModel(location), lightLevel);
	}
}
