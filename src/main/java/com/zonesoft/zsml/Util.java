package com.zonesoft.zsml;

import net.minecraft.util.ResourceLocation;

public class Util {
	public static String getSuffix(ResourceLocation location) {
		return getSuffix(location.getPath());
	}

	public static String getSuffix(String path) {
		String[] a = path.split("/");
		path = a[a.length - 1];
		int index = path.lastIndexOf(".");
		if (index != -1 && index < path.length() - 1) {
			return path.substring(index + 1);
		}
		return "";
	}
}
