package com.zonesoft.zsml.model.gltf;

import java.nio.ByteBuffer;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;

import com.mojang.blaze3d.systems.RenderSystem;
import com.zonesoft.zsml.model.gltf.bean.Accessor;
import com.zonesoft.zsml.model.gltf.bean.Image;
import com.zonesoft.zsml.model.gltf.bean.Material;
import com.zonesoft.zsml.model.gltf.bean.Material.PBRMetallicRoughness;
import com.zonesoft.zsml.model.gltf.bean.Primitive;
import com.zonesoft.zsml.model.gltf.bean.Texture;
import com.zonesoft.zsml.model.gltf.bean.TextureInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BufferedPrimitive {
	private int vboId, eboId, vaoId, indicesCount;
	private ResourceLocation baseColorLocation;
	private ModelGLTF model;
	private Primitive primitive;

	public BufferedPrimitive(ModelGLTF model, Primitive primitive) {
		this.model = model;
		this.primitive = primitive;
		initData();
	}

	public void initData() {
		Map<String, Integer> attributes = primitive.getAttributes();
		Integer posAccessorIndex = attributes.get("POSITION");
		if (posAccessorIndex != null) {
			vaoId = GL46.glCreateVertexArrays();

			Accessor posAccessor = model.getAccessors().get(posAccessorIndex);
			byte[] bytes = AccessorHelper.viewBytes(model, posAccessor).getData();
			ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
			buffer.clear();
			vboId = GL46.glCreateBuffers();
			GL46.glNamedBufferStorage(vboId, buffer, GL46.GL_MAP_WRITE_BIT);
			GL46.glVertexArrayAttribFormat(vaoId, 0, 3, GL46.GL_FLOAT, false, 0);
			GL46.glVertexArrayAttribBinding(vaoId, 0, 0);
			GL46.glEnableVertexArrayAttrib(vaoId, 0);
			GL46.glVertexArrayVertexBuffer(vaoId, 0, vboId, 0, 12);

			Integer texcoordAccessorIndex = attributes.get("TEXCOORD_0");
			byte[] texcoordBytes = null;
			if (texcoordAccessorIndex != null) {
				Accessor texcoordlAccessor = model.getAccessors().get(texcoordAccessorIndex);
				texcoordBytes = AccessorHelper.viewBytes(model, texcoordlAccessor).getData();
				buffer = ByteBuffer.allocateDirect(bytes.length).put(texcoordBytes);
				buffer.clear();
				int texVboId = GL46.glCreateBuffers();
				GL46.glNamedBufferStorage(texVboId, buffer, GL46.GL_MAP_WRITE_BIT);
				GL46.glVertexArrayAttribFormat(vaoId, 1, 2, GL46.GL_FLOAT, false, 0);
				GL46.glVertexArrayAttribBinding(vaoId, 1, 1);
				GL46.glEnableVertexArrayAttrib(vaoId, 1);
				GL46.glVertexArrayVertexBuffer(vaoId, 1, texVboId, 0, 8);
				GL46.glVertexArrayAttribBinding(vaoId, 2, 1);
				GL46.glEnableVertexArrayAttrib(vaoId, 2);
				GL46.glVertexArrayAttribBinding(vaoId, 3, 1);
				GL46.glEnableVertexArrayAttrib(vaoId, 3);
			}

			Integer normalAccessorIndex = attributes.get("NORMAL");
			byte[] normalBytes = null;
			if (normalAccessorIndex != null) {
				Accessor normalAccessor = model.getAccessors().get(normalAccessorIndex);
				normalBytes = AccessorHelper.viewBytes(model, normalAccessor).getData();
			}

			int verticesCount = bytes.length / 12;

//			int byteStride = 12 + (texcoordBytes == null ? 0 : 8) + (normalBytes == null ? 0 : 12);
//			if (byteStride > 12) {
//				buffer = ByteBuffer.allocateDirect(verticesCount * byteStride);
//				buffer.clear();
//				for (int i = 0; i < verticesCount; i++) {
//					buffer.put(bytes, i * 12, 12);
//					if (texcoordBytes != null) {
//						buffer.put(texcoordBytes, i * 8, 8);
//					}
//					if (normalBytes != null) {
//						buffer.put(normalBytes, i * 12, 12);
//					}
//				}
//			} else {

//			}

//			GL46.glVertexPointer(3, GL11.GL_FLOAT, byteStride, 0);
//			GL46.glVertexArrayAttribBinding(verticesCount, byteStride, indicesAccessorIndex);

//			int offset = 12;
//			if (texcoordBytes != null) {
//				GL46.glTexCoordPointer(2, GL11.GL_FLOAT, byteStride, offset);
//				offset += 8;
//			}
//			if (normalBytes != null) {
//				GL46.glNormalPointer(GL11.GL_FLOAT, byteStride, offset);
//				offset += 12;
//			}

			int indicesAccessorIndex = primitive.getIndices();
//			eboId = GL46.glGenBuffers();
			eboId = GL46.glCreateBuffers();
			Accessor indicesAccessor = model.getAccessors().get(indicesAccessorIndex);
			bytes = AccessorHelper.viewBytes(model, indicesAccessor).getData();
			ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
			indicesBuffer.clear();
			indicesCount = bytes.length / 4;
//			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboId);
//			GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);
			GL46.glNamedBufferStorage(eboId, indicesBuffer, GL46.GL_MAP_WRITE_BIT);
//			GL46.glIndexPointer(GL46.GL_INT, 0, 0);
			GL46.glVertexArrayElementBuffer(vaoId, eboId);

//			GL46.glBindVertexArray(0);
//			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);

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
//		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		GL46.glColor4f(1, 1, 1, 1);
		GL46.glBindVertexArray(vaoId);

//		GL11.glEnableClientState(GL46.GL_INDEX_ARRAY);
//		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
//		GL11.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//		GL11.glEnableClientState(GL46.GL_NORMAL_ARRAY);

		if (baseColorLocation != null) {
//			GL11.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
			Minecraft.getInstance().textureManager.bindTexture(baseColorLocation);
		} else {
			GL46.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
		}
		GL46.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);

		GL46.glBindVertexArray(0);
//		GL46.glDisableClientState(GL46.GL_INDEX_ARRAY);
//		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
//		GL11.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//		GL11.glDisableClientState(GL46.GL_NORMAL_ARRAY);
		System.out.println(1 / 0);
	}
}
