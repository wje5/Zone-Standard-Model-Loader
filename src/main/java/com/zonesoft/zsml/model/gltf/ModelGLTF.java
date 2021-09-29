package com.zonesoft.zsml.model.gltf;

import java.util.ArrayList;
import java.util.List;

import com.zonesoft.zsml.model.AbstractModel;
import com.zonesoft.zsml.render.GLTFRenderer;
import com.zonesoft.zsml.render.ModelRenderer;

import net.minecraft.util.ResourceLocation;

public class ModelGLTF extends AbstractModel {
	private List<Scene> scenes = new ArrayList<Scene>();
	private List<Node> nodes = new ArrayList<Node>();
	private List<Mesh> meshes = new ArrayList<Mesh>();
	private List<Buffer> buffers = new ArrayList<Buffer>();
	private List<BufferView> bufferViews = new ArrayList<BufferView>();
	private List<Accessor> accessors = new ArrayList<Accessor>();
	private List<Material> materials = new ArrayList<Material>();
	private ResourceLocation path;

	public ModelGLTF() {

	}

	@Override
	public ModelRenderer createNewRenderer() {
		return new GLTFRenderer(this);
	}

	public void setPath(ResourceLocation path) {
		this.path = path;
	}

	public ResourceLocation getPath() {
		return path;
	}

	public List<Scene> getScenes() {
		return scenes;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

	public List<Buffer> getBuffers() {
		return buffers;
	}

	public List<BufferView> getBufferViews() {
		return bufferViews;
	}

	public List<Accessor> getAccessors() {
		return accessors;
	}

	public List<Material> getMaterials() {
		return materials;
	}
}
