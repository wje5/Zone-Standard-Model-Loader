package com.zonesoft.zsml.model.gltf;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
	private List<Primitive> primitives = new ArrayList<Primitive>();

	public Mesh() {

	}

	public List<Primitive> getPrimitives() {
		return primitives;
	}
}
