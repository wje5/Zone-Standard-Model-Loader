package com.zonesoft.zsml.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zonesoft.zsml.model.gltf.GLTFReader;

import net.minecraft.util.ResourceLocation;

public class ModelLoader {
	private static Set<IModelReader> readers = new HashSet<IModelReader>();

	private static Map<ResourceLocation, AbstractModel> models = new HashMap<ResourceLocation, AbstractModel>();

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
}
