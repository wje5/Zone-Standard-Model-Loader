package com.zonesoft.zsml.render;

import java.nio.ByteBuffer;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.zonesoft.zsml.model.gltf.Accessor;
import com.zonesoft.zsml.model.gltf.AccessorHelper;
import com.zonesoft.zsml.model.gltf.Mesh;
import com.zonesoft.zsml.model.gltf.ModelGLTF;
import com.zonesoft.zsml.model.gltf.Node;
import com.zonesoft.zsml.model.gltf.Scene;

public class GLTFRenderer extends ModelRenderer {
	private int vboId, vboiId;

	public GLTFRenderer(ModelGLTF model) {
		super(model);
	}

	@Override
	public void doRender(MatrixStack stack) {
		ModelGLTF model = (ModelGLTF) this.model;
//		if (displayList == 0) {
//			long start = System.nanoTime();
//			displayList = GL11.glGenLists(1);
//		GL11.glEnable(GL11.GL_CULL_FACE);
//			GL11.glNewList(displayList, GL11.GL_COMPILE_AND_EXECUTE);
		GL11.glDisable(GL11.GL_CULL_FACE);
		model.getScenes().forEach(e -> renderScene(stack, model, e));
//			GL11.glEndList();
//			System.out.println(displayList + "|" + (System.nanoTime() - start));
//		} else {
//			GL11.glDisable(GL11.GL_CULL_FACE);
//			GL11.glCallList(displayList);
//		}

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
				Map<String, Integer> attributes = e.getAttributes();
				int posAccessorIndex = attributes.get("POSITION");
				if (posAccessorIndex >= 0) {
					Accessor posAccessor = model.getAccessors().get(posAccessorIndex);
					byte[] bytes = AccessorHelper.viewBytes(model, posAccessor).getData();
					ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
					buffer.clear();
					GL11.glColor4f(1, 1, 1, 1);

					if (vboId == 0) {
						vboId = GL46.glGenBuffers();
					}
					GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
					GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
//					GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buffer, GL46.GL_STATIC_DRAW);
					GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

					int indicesAccessorIndex = e.getIndices();
					if (vboiId == 0) {
						vboiId = GL46.glGenBuffers();
					}
					Accessor indicesAccessor = model.getAccessors().get(indicesAccessorIndex);
					bytes = AccessorHelper.viewBytes(model, indicesAccessor).getData();
					ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
					indicesBuffer.clear();
//					System.out.println(indicesBuffer.remaining());
//					GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, vboiId);

//					GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);
//					GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, count / 12);
					GL11.glDrawElements(GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_SHORT, indicesBuffer);
//					GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);
					GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);

					GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

				}
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
