package com.zonesoft.zsml.model.gltf.bean;

public class Texture {
	private int sampler = -1;
	private int source = -1;
	private String name;

	public Texture() {

	}

	public int getSampler() {
		return sampler;
	}

	public int getSource() {
		return source;
	}

	public String getName() {
		return name;
	}
}
