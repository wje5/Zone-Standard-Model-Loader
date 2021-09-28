package com.zonesoft.zsml.render;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.zonesoft.zsml.model.gltf.Mesh;
import com.zonesoft.zsml.model.gltf.ModelGLTF;
import com.zonesoft.zsml.model.gltf.Node;
import com.zonesoft.zsml.model.gltf.Primitive;
import com.zonesoft.zsml.model.gltf.Scene;
import com.zonesoft.zsml.model.gltf.render.BufferedPrimitive;

public class GLTFRenderer extends ModelRenderer {
	private Map<Primitive, BufferedPrimitive> bufferedPrimitives = new HashMap<Primitive, BufferedPrimitive>();

	public GLTFRenderer(ModelGLTF model) {
		super(model);
	}

	@Override
	public void doRender(MatrixStack stack) {
		ModelGLTF model = (ModelGLTF) this.model;
		GL11.glDisable(GL11.GL_CULL_FACE);
		model.getScenes().forEach(e -> renderScene(stack, model, e));
	}

	public void renderScene(MatrixStack stack, ModelGLTF model, Scene scene) {
		for (int i : scene.getNodes()) {
			renderNode(stack, model, model.getNodes().get(i));
		}
	}

	public void renderNode(MatrixStack stack, ModelGLTF model, Node node) {
		int meshIndex = node.getMesh();
		if (meshIndex >= 0) {
			Mesh mesh = model.getMeshes().get(meshIndex);
			mesh.getPrimitives().forEach(e -> {
				BufferedPrimitive bufferedPrimitive = bufferedPrimitives.get(e);
				if (bufferedPrimitive == null) {
					bufferedPrimitive = new BufferedPrimitive(model, e);
					bufferedPrimitives.put(e, bufferedPrimitive);
				}
				bufferedPrimitive.doRender();
			});
		}
		int[] childen = node.getChildren();
		if (childen != null) {
			for (int i : node.getChildren()) {
				renderNode(stack, model, model.getNodes().get(i));
			}
		}
	}
}
