package com.zonesoft.zsml.model.gltf;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Map;

import org.lwjgl.opengl.GL46;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.zonesoft.zsml.model.gltf.bean.Accessor;
import com.zonesoft.zsml.model.gltf.bean.Image;
import com.zonesoft.zsml.model.gltf.bean.Material;
import com.zonesoft.zsml.model.gltf.bean.Material.PBRMetallicRoughness;
import com.zonesoft.zsml.model.gltf.bean.Primitive;
import com.zonesoft.zsml.model.gltf.bean.Texture;
import com.zonesoft.zsml.model.gltf.bean.TextureInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class BufferedPrimitive {
	private int vboId, eboId, vaoId, indicesCount;
	private ResourceLocation baseColorLocation;
	private ModelGLTF model;
	private Primitive primitive;
	private byte[] poses;
	private FloatBuffer fb;

	public BufferedPrimitive(ModelGLTF model, Primitive primitive) {
		this.model = model;
		this.primitive = primitive;
		initData();
	}

	public void initData() {
		Map<String, Integer> attributes = primitive.getAttributes();
		Integer posAccessorIndex = attributes.get("POSITION");
		if (posAccessorIndex != null) {
			Accessor posAccessor = model.getAccessors().get(posAccessorIndex);
			Accessor normalAccessor = null, texcoordAccessor = null, colorAccessor = null;

			byte[] posData = AccessorHelper.storageAccessorToByteArray(model, posAccessor);

			byte[] normalData = null, texData = null, colorData = null;
			int vboSize = posData.length;
			int posStride = AccessorHelper.getAccessorVertexSize(posAccessor);
			int normalStride = 0, texStride = 0, colorStride = 0;

			ByteBuffer normalBuffer = null;
			Integer normalAccessorIndex = attributes.get("NORMAL");
			if (normalAccessorIndex != null) {
				normalAccessor = model.getAccessors().get(normalAccessorIndex);
				normalBuffer = ByteBuffer.allocateDirect(vboSize);
				AccessorHelper.storageAccessorToByteBuffer(normalBuffer, model, normalAccessor);
				normalData = AccessorHelper.getByteBufferData(normalBuffer);
				vboSize += normalData.length;
				normalStride = AccessorHelper.getAccessorVertexSize(normalAccessor);
			}

			Integer texcoordAccessorIndex = attributes.get("TEXCOORD_0");
			if (texcoordAccessorIndex != null) {
				texcoordAccessor = model.getAccessors().get(texcoordAccessorIndex);
				texData = AccessorHelper.storageAccessorToByteArray(model, texcoordAccessor);
				vboSize += texData.length;
				texStride = AccessorHelper.getAccessorVertexSize(texcoordAccessor);
			}

			Integer colorAccessorIndex = attributes.get("COLOR_0");
			if (normalBuffer != null) {
				normalBuffer.clear();
				normalBuffer.order(ByteOrder.LITTLE_ENDIAN);
				FloatBuffer floatBuffer = normalBuffer.asFloatBuffer();
				if (colorAccessorIndex != null) {
					colorAccessor = model.getAccessors().get(colorAccessorIndex);
					ByteBuffer colorBuffer = ByteBuffer.allocateDirect(posData.length);
					AccessorHelper.storageAccessorToByteBuffer(colorBuffer, model, colorAccessor);
					vboSize += AccessorHelper.getAccessorSize(colorAccessor);
					colorStride = AccessorHelper.getAccessorVertexSize(colorAccessor);
					colorBuffer.clear();
					colorBuffer.order(ByteOrder.LITTLE_ENDIAN);
					FloatBuffer colorFloatBuffer = colorBuffer.asFloatBuffer();
					ByteBuffer colorBuffer2 = ByteBuffer.allocateDirect(posData.length);
					colorBuffer2.order(ByteOrder.LITTLE_ENDIAN);
					FloatBuffer colorFloatBuffer2 = colorBuffer.asFloatBuffer();
					while (floatBuffer.hasRemaining()) {
						float light = LightUtil.diffuseLight(floatBuffer.get(), floatBuffer.get(), floatBuffer.get());
						colorFloatBuffer2.put(colorFloatBuffer.get() * light);
						colorFloatBuffer2.put(colorFloatBuffer.get() * light);
						colorFloatBuffer2.put(colorFloatBuffer.get() * light);
					}
					colorData = AccessorHelper.getByteBufferData(colorBuffer2);
				} else {
					ByteBuffer colorBuffer = ByteBuffer.allocateDirect(posData.length);
					colorBuffer.order(ByteOrder.LITTLE_ENDIAN);
					FloatBuffer colorFloatBuffer = colorBuffer.asFloatBuffer();
					while (floatBuffer.hasRemaining()) {
						float light = LightUtil.diffuseLight(floatBuffer.get(), floatBuffer.get(), floatBuffer.get());
						colorFloatBuffer.put(light);
						colorFloatBuffer.put(light);
						colorFloatBuffer.put(light);
					}
					colorData = AccessorHelper.getByteBufferData(colorBuffer);
				}
				vboSize += colorData.length;
				colorStride += 12;
			}

			vboSize += posAccessor.getCount() * 8;
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vboSize);
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int verticesCount = posAccessor.getCount();
			int vboStride = posStride + normalStride + texStride
//					+ 8
					+ colorStride;
			for (int i = 0; i < verticesCount; i++) {
				byteBuffer.put(posData, i * posStride, posStride);
				if (normalData != null) {
					byteBuffer.put(normalData, i * normalStride, normalStride);
				}
				if (texData != null) {
					byteBuffer.put(texData, i * texStride, texStride);
//					byteBuffer.put((byte) 0);
//					byteBuffer.put((byte) blockLight);
//					byteBuffer.put((byte) 0);
//					byteBuffer.put((byte) skyLight);

//					byteBuffer.putFloat(0.1F);
//					byteBuffer.putFloat(1F);
				}
				byteBuffer.put(colorData, i * colorStride, colorStride);
			}

			vaoId = GL46.glGenVertexArrays();
			vboId = GL46.glGenBuffers();
			GL46.glBindVertexArray(vaoId);
			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
			byteBuffer.clear();
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, byteBuffer, GL46.GL_STATIC_DRAW);

			GL46.glVertexPointer(AccessorHelper.getComponentCount(posAccessor.getType()),
					posAccessor.getComponentType(), vboStride, 0);
			int offset = posStride;
			if (normalData != null) {
				GL46.glNormalPointer(normalAccessor.getComponentType(), vboStride, offset);
				offset += normalStride;
			}
			if (texData != null) {
				GL46.glActiveTexture(GL46.GL_TEXTURE0);
				GL46.glEnable(GL46.GL_TEXTURE_2D);
				GL46.glTexCoordPointer(AccessorHelper.getComponentCount(texcoordAccessor.getType()),
						texcoordAccessor.getComponentType(), vboStride, offset);
				offset += texStride;
//				GL46.glActiveTexture(GL46.GL_TEXTURE2);
//				GL46.glEnable(GL46.GL_TEXTURE_2D);
//
//				GL46.glTexCoordPointer(2, GL46.GL_FLOAT, vboStride, offset);
//				GL46.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//				offset += 8;
//				GL46.glActiveTexture(GL46.GL_TEXTURE0);
			}
			GL46.glColorPointer(3, GL46.GL_FLOAT, vboStride, offset);
			offset += colorStride;

			byteBuffer.clear();
			int indicesAccessorIndex = primitive.getIndices();
			Accessor indicesAccessor = model.getAccessors().get(indicesAccessorIndex);
			// XXX indecesIndex can be 0: use glDrawArrays

			byteBuffer = ByteBuffer.allocateDirect(AccessorHelper.getAccessorSize(indicesAccessor));
			AccessorHelper.storageAccessorToByteBuffer(byteBuffer, model, indicesAccessor);
			int eboId = GL46.glGenBuffers();
			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboId);
			byteBuffer.clear();
			GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, byteBuffer, GL46.GL_STATIC_DRAW);
			GL46.glBindVertexArray(0);
			indicesCount = indicesAccessor.getCount();
		}

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

	public void doRender(MatrixStack stack, IRenderTypeBuffer buffer) {
		doRenderWithGL(stack);
	}

	public void doRenderWithGL(MatrixStack stack) {
//		GL46.glPushMatrix();
//		GlStateManager.multMatrix(stack.getLast().getMatrix());

//		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		GL46.glColor4f(1, 1, 1, 1);
		GL46.glBindVertexArray(vaoId);

		GL46.glEnableClientState(GL46.GL_INDEX_ARRAY);
		GL46.glEnableClientState(GL46.GL_COLOR_ARRAY);
		GL46.glEnableClientState(GL46.GL_NORMAL_ARRAY);
		GL46.glEnableClientState(GL46.GL_VERTEX_ARRAY);
//		GL46.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);

		if (baseColorLocation != null) {
			GL46.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
			Minecraft.getInstance().textureManager.bindTexture(baseColorLocation);
		} else {
//			GL46.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
		}

//		Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();

//		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		GL46.glEnable(GL46.GL_TEXTURE_2D);
//		GL46.glDisable(GL46.GL_TEXTURE_2D);

//		GL46.glActiveTexture(GL46.GL_TEXTURE2);

//		GL46.glEnable(GL46.GL_TEXTURE_2D);
//		GL46.glDisable(GL46.GL_TEXTURE_2D);

//		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		Minecraft.getInstance().textureManager.bindTexture(baseColorLocation);

		GL46.glDrawElements(GL46.GL_TRIANGLES, indicesCount, GL46.GL_UNSIGNED_INT, 0);
		Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();

		GL46.glDisableClientState(GL46.GL_INDEX_ARRAY);
		GL46.glDisableClientState(GL46.GL_COLOR_ARRAY);
		GL46.glDisableClientState(GL46.GL_NORMAL_ARRAY);
		GL46.glDisableClientState(GL46.GL_VERTEX_ARRAY);
		GL46.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);

//		GL46.glActiveTexture(GL46.GL_TEXTURE0);
//		GL46.glEnable(GL46.GL_TEXTURE_2D);

		GL46.glBindVertexArray(0);
		GL46.glEnable(GL46.GL_TEXTURE_2D);

//		GL46.glPopMatrix();
	}
}
