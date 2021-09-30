package com.zonesoft.zsml;

import net.minecraft.util.ResourceLocation;

public interface IModelReader {
	public AbstractModel read(ResourceLocation location);
}