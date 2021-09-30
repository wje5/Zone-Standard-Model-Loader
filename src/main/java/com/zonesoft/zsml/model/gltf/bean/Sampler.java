package com.zonesoft.zsml.model.gltf.bean;

public class Sampler {
	private int magFilter;
	private int minFilter;
	private int wrapS = 10497;
	private int wrapT = 10497;
	private String name;

	public Sampler() {

	}

	public int getMagFilter() {
		return magFilter;
	}

	public int getMinFilter() {
		return minFilter;
	}

	public int getWrapS() {
		return wrapS;
	}

	public int getWrapT() {
		return wrapT;
	}

	public String getName() {
		return name;
	}
}
