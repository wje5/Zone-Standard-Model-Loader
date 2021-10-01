package com.zonesoft.zsml.model.gltf;

import java.nio.ByteBuffer;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL46;

import com.mojang.blaze3d.platform.GlStateManager;
import com.zonesoft.zsml.model.gltf.bean.Accessor;
import com.zonesoft.zsml.model.gltf.bean.Image;
import com.zonesoft.zsml.model.gltf.bean.Material;
import com.zonesoft.zsml.model.gltf.bean.Material.PBRMetallicRoughness;
import com.zonesoft.zsml.model.gltf.bean.Primitive;
import com.zonesoft.zsml.model.gltf.bean.Texture;
import com.zonesoft.zsml.model.gltf.bean.TextureInfo;

import net.minecraft.util.ResourceLocation;

public class BufferedPrimitive {
	private int vboId, eboId, vaoId, indicesCount;
	private ResourceLocation baseColorLocation;

	public BufferedPrimitive(ModelGLTF model, Primitive primitive) {
		Map<String, Integer> attributes = primitive.getAttributes();
		Integer posAccessorIndex = attributes.get("POSITION");
		if (posAccessorIndex != null) {
			vaoId = GL46.glGenVertexArrays();
			GL46.glBindVertexArray(vaoId);

			Accessor posAccessor = model.getAccessors().get(posAccessorIndex);
			byte[] bytes = AccessorHelper.viewBytes(model, posAccessor).getData();

			Integer normalAccessorIndex = attributes.get("NORMAL");
			byte[] normalBytes = null;
			if (normalAccessorIndex != null) {
				Accessor normalAccessor = model.getAccessors().get(normalAccessorIndex);
				normalBytes = AccessorHelper.viewBytes(model, normalAccessor).getData();
			}
			Integer texcoordAccessorIndex = attributes.get("TEXCOORD_0");
			byte[] texcoordBytes = null;
			if (texcoordAccessorIndex != null) {
				Accessor texcoordlAccessor = model.getAccessors().get(texcoordAccessorIndex);
				texcoordBytes = AccessorHelper.viewBytes(model, texcoordlAccessor).getData();
			}
			ByteBuffer buffer;
			int verticesCount = bytes.length / 12;

			int byteStride = 12 + (normalBytes == null ? 0 : 12) + (texcoordBytes == null ? 0 : 8);
			if (byteStride > 12) {
				buffer = ByteBuffer.allocateDirect(verticesCount * byteStride);
				buffer.clear();
				for (int i = 0; i < verticesCount; i++) {
					buffer.put(bytes, i * 12, 12);
					if (normalBytes != null) {
						buffer.put(normalBytes, i * 12, 12);
					}
					if (texcoordBytes != null) {
						buffer.put(texcoordBytes, i * 8, 8);
					}
				}
			} else {
				buffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
			}
			buffer.clear();
			vboId = GL46.glGenBuffers();
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buffer, GL46.GL_STATIC_DRAW);

			int indicesAccessorIndex = primitive.getIndices();
			if (eboId == 0) {
				eboId = GL46.glGenBuffers();
			}
			Accessor indicesAccessor = model.getAccessors().get(indicesAccessorIndex);
			bytes = AccessorHelper.viewBytes(model, indicesAccessor).getData();
			ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
			indicesBuffer.clear();
			indicesCount = bytes.length / 4;
			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboId);
			GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);
			GL46.glIndexPointer(GL46.GL_UNSIGNED_INT, 0, 0);
			GL46.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, byteStride, 0);
			if (normalBytes != null) {
				GL46.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, byteStride, 12);
			}
			if (texcoordBytes != null) {
				GL46.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, byteStride, 12 + (normalBytes == null ? 0 : 12));
			}
			GL46.glBindVertexArray(0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);

			// MATERIAL START

			int materialIndex = primitive.getMaterial();
			Material material = materialIndex >= 0 ? model.getMaterials().get(materialIndex) : new Material();
			PBRMetallicRoughness pbr = material.getPbrMetallicRoughness();
			if (pbr != null) {
				TextureInfo baseColorTextureInfo = pbr.getBaseColorTexture();
				if (baseColorTextureInfo != null) {
					Texture baseColorTexture = model.getTextures().get(baseColorTextureInfo.getIndex());
					int baseColorSource = baseColorTexture.getSource();
					if (baseColorSource >= 0) {
						Image baseColorImage = model.getImages().get(baseColorTexture.getSource());
						baseColorLocation = AccessorHelper.getModelDirFile(model, baseColorImage.getUri());
					}
				}
			}
		}
	}

	public void doRender() {
		GlStateManager.enableCull();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnableClientState(GL46.GL_INDEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
		GL46.glBindVertexArray(vaoId);
		GL46.glEnableVertexAttribArray(0);
		GL46.glEnableVertexAttribArray(1);
//		GL46.glEnableVertexAttribArray(2);
//		Minecraft.getInstance().textureManager.bindTexture(baseColorLocation);
		GL11.nglDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);

		GL46.glDisableVertexAttribArray(0);
		GL46.glDisableVertexAttribArray(1);
//		GL46.glDisableVertexAttribArray(2);
		GL46.glBindVertexArray(0);
		GL46.glDisableClientState(GL46.GL_INDEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
	}
}
