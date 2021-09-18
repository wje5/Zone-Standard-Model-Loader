package com.zonesoft.zsml.model;

import net.minecraft.util.ResourceLocation;

public interface IModelReader {
	public AbstractModel read(ResourceLocation location);
}