package com.zonesoft.zsml.model.gltf;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.zonesoft.zsml.ModelRenderer;
import com.zonesoft.zsml.model.gltf.bean.Mesh;
import com.zonesoft.zsml.model.gltf.bean.Node;
import com.zonesoft.zsml.model.gltf.bean.Primitive;
import com.zonesoft.zsml.model.gltf.bean.Scene;

import net.minecraft.client.renderer.IRenderTypeBuffer;

public class GLTFRenderer extends ModelRenderer {
	private Map<Primitive, BufferedPrimitive> bufferedPrimitives = new HashMap<Primitive, BufferedPrimitive>();

	public GLTFRenderer(ModelGLTF model) {
		super(model);
	}

	@Override
	public void doRender(MatrixStack stack, IRenderTypeBuffer buffer) {
		ModelGLTF model = (ModelGLTF) this.model;
//		GL11.glDisable(GL11.GL_CULL_FACE);
		model.getScenes().forEach(e -> renderScene(stack, buffer, model, e));
	}

	public void renderScene(MatrixStack stack, IRenderTypeBuffer buffer, ModelGLTF model, Scene scene) {
		for (int i : scene.getNodes()) {
			renderNode(stack, buffer, model, model.getNodes().get(i));
		}
	}

	public void renderNode(MatrixStack stack, IRenderTypeBuffer buffer, ModelGLTF model, Node node) {
		int meshIndex = node.getMesh();
		if (meshIndex >= 0) {
			Mesh mesh = model.getMeshes().get(meshIndex);
			mesh.getPrimitives().forEach(e -> {
				BufferedPrimitive bufferedPrimitive = bufferedPrimitives.get(e);
				if (bufferedPrimitive == null) {
					bufferedPrimitive = new BufferedPrimitive(model, e);
					bufferedPrimitives.put(e, bufferedPrimitive);
				}
				bufferedPrimitive.doRender(stack, buffer);
			});
		}
		int[] childen = node.getChildren();
		if (childen != null) {
			for (int i : node.getChildren()) {
				renderNode(stack, buffer, model, model.getNodes().get(i));
			}
		}
	}
}
