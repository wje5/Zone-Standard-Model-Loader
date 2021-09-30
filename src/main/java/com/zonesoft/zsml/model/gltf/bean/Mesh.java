package com.zonesoft.zsml.model.gltf.bean;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
	private String name;
	private List<Primitive> primitives = new ArrayList<Primitive>();

	public Mesh() {

	}

	public String getName() {
		return name;
	}

	public List<Primitive> getPrimitives() {
		return primitives;
	}
}
