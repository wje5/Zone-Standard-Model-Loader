package com.zonesoft.zsml.model.gltf;

import java.util.ArrayList;
import java.util.List;

import com.zonesoft.zsml.AbstractModel;
import com.zonesoft.zsml.ModelRenderer;
import com.zonesoft.zsml.model.gltf.bean.Accessor;
import com.zonesoft.zsml.model.gltf.bean.Buffer;
import com.zonesoft.zsml.model.gltf.bean.BufferView;
import com.zonesoft.zsml.model.gltf.bean.Image;
import com.zonesoft.zsml.model.gltf.bean.Material;
import com.zonesoft.zsml.model.gltf.bean.Mesh;
import com.zonesoft.zsml.model.gltf.bean.Node;
import com.zonesoft.zsml.model.gltf.bean.Sampler;
import com.zonesoft.zsml.model.gltf.bean.Scene;
import com.zonesoft.zsml.model.gltf.bean.Texture;

import net.minecraft.util.ResourceLocation;

public class ModelGLTF extends AbstractModel {
	private List<String> extensionsUsed = new ArrayList<String>();
	private List<String> extensionsRequired = new ArrayList<String>();
	private List<Accessor> accessors = new ArrayList<Accessor>();
	// animations
	// asset
	private List<Buffer> buffers = new ArrayList<Buffer>();
	private List<BufferView> bufferViews = new ArrayList<BufferView>();
	// cameras
	private List<Image> images = new ArrayList<Image>();
	private List<Material> materials = new ArrayList<Material>();
	private List<Mesh> meshes = new ArrayList<Mesh>();
	private List<Node> nodes = new ArrayList<Node>();
	private List<Sampler> samplers = new ArrayList<Sampler>();
	private int scene;
	private List<Scene> scenes = new ArrayList<Scene>();
	// skins
	private List<Texture> textures = new ArrayList<Texture>();

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

	public List<String> getExtensionsUsed() {
		return extensionsUsed;
	}

	public List<String> getExtensionsRequired() {
		return extensionsRequired;
	}

	public List<Accessor> getAccessors() {
		return accessors;
	}

	public List<Buffer> getBuffers() {
		return buffers;
	}

	public List<BufferView> getBufferViews() {
		return bufferViews;
	}

	public List<Image> getImages() {
		return images;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Sampler> getSamplers() {
		return samplers;
	}

	public int getScene() {
		return scene;
	}

	public List<Scene> getScenes() {
		return scenes;
	}

	public List<Texture> getTextures() {
		return textures;
	}
}
