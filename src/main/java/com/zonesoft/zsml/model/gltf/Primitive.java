package com.zonesoft.zsml.model.gltf;

import java.util.HashMap;
import java.util.Map;

public class Primitive {
	private Map<String, Integer> attributes = new HashMap<String, Integer>();
	private int indices = -1;
	private int material = -1;
	private int mode = 4;

	public Primitive() {

	}

	public Map<String, Integer> getAttributes() {
		return attributes;
	}

	public int getIndices() {
		return indices;
	}

	public int getMaterial() {
		return material;
	}

	public int getMode() {
		return mode;
	}
}
