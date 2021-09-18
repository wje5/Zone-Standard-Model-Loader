package com.zonesoft.zsml.model.gltf;

import java.util.List;

import com.zonesoft.zsml.model.AbstractModel;

public class ModelGLTF extends AbstractModel {
	private List<Scene> scenes;
	private List<Node> nodes;
	private List<Buffer> buffers;
	private List<BufferView> bufferViews;
	private List<Accessor> accessors;

	public ModelGLTF() {

	}
}
