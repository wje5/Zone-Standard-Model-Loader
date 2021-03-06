package com.zonesoft.zsml.model.gltf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.zonesoft.zsml.AbstractModel;
import com.zonesoft.zsml.IModelReader;
import com.zonesoft.zsml.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GLTFReader implements IModelReader {
	@Override
	public AbstractModel read(ResourceLocation location) {
		String suffix = Util.getSuffix(location);
		if (!suffix.equals("gltf")) {
			return null;
		}
		try (InputStream stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream()) {
			Gson gson = new Gson();
			ModelGLTF model = gson.fromJson(new InputStreamReader(stream), ModelGLTF.class);
			model.setPath(location);
			return model;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
