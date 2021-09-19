package com.zonesoft.zsml.render;

import java.util.Map;

import com.zonesoft.zsml.model.gltf.Accessor;
import com.zonesoft.zsml.model.gltf.AccessorHelper;
import com.zonesoft.zsml.model.gltf.Mesh;
import com.zonesoft.zsml.model.gltf.ModelGLTF;
import com.zonesoft.zsml.model.gltf.Node;
import com.zonesoft.zsml.model.gltf.Scene;

import net.minecraft.util.math.vector.Vector3f;

public class GLTFRenderer extends ModelRenderer {
	public GLTFRenderer(ModelGLTF model) {
		super(model);
	}

	@Override
	public void doRender() {
		ModelGLTF model = (ModelGLTF) this.model;
		model.getScenes().forEach(e -> renderScene(model, e));
	}

	public void renderScene(ModelGLTF model, Scene scene) {
		for (int i : scene.getNodes()) {
			renderNode(model, model.getNodes().get(i));
		}
	}

	public void renderNode(ModelGLTF model, Node node) {
		int meshIndex = node.getMesh();
		if (meshIndex >= 0) {
			Mesh mesh = model.getMeshes().get(meshIndex);
			mesh.getPrimitives().forEach(e -> {
				Map<String, Integer> attributes = e.getAttributes();
				int posAccessorIndex = attributes.get("POSITION");
				if (posAccessorIndex >= 0) {
					Accessor posAccessor = model.getAccessors().get(posAccessorIndex);
					Vector3f vec3 = AccessorHelper.getVec3(model, posAccessor);
					System.out.println(vec3);
				}
			});
		}
		int[] childen = node.getChildren();
		if (childen != null) {
			for (int i : node.getChildren()) {
				renderNode(model, model.getNodes().get(i));
			}
		}
	}
}
