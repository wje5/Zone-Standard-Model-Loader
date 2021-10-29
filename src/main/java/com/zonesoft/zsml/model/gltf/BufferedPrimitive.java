package com.zonesoft.zsml.model.gltf;

import java.nio.FloatBuffer;
import java.util.Map;

import org.lwjgl.opengl.GL46;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.zonesoft.zsml.demo.DemoRenderer;
import com.zonesoft.zsml.model.gltf.bean.Accessor;
import com.zonesoft.zsml.model.gltf.bean.Image;
import com.zonesoft.zsml.model.gltf.bean.Material;
import com.zonesoft.zsml.model.gltf.bean.Material.PBRMetallicRoughness;
import com.zonesoft.zsml.model.gltf.bean.Primitive;
import com.zonesoft.zsml.model.gltf.bean.Texture;
import com.zonesoft.zsml.model.gltf.bean.TextureInfo;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;

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
//		initData();
		initDataGL46();
	}

//	public void initData() {
//		Map<String, Integer> attributes = primitive.getAttributes();
//		Integer posAccessorIndex = attributes.get("POSITION");
//		if (posAccessorIndex != null) {
//			vaoId = GL46.glGenVertexArrays();
//			GL46.glBindVertexArray(vaoId);
//
//			Accessor posAccessor = model.getAccessors().get(posAccessorIndex);
//			byte[] bytes = AccessorHelper.viewBytes(model, posAccessor).getData();
//			poses = bytes;
//			Integer texcoordAccessorIndex = attributes.get("TEXCOORD_0");
//			byte[] texcoordBytes = null;
//			if (texcoordAccessorIndex != null) {
//				Accessor texcoordlAccessor = model.getAccessors().get(texcoordAccessorIndex);
//				texcoordBytes = AccessorHelper.viewBytes(model, texcoordlAccessor).getData();
//			}
//
//			Integer normalAccessorIndex = attributes.get("NORMAL");
//			byte[] normalBytes = null;
//			if (normalAccessorIndex != null) {
//				Accessor normalAccessor = model.getAccessors().get(normalAccessorIndex);
//				normalBytes = AccessorHelper.viewBytes(model, normalAccessor).getData();
//			}
//			ByteBuffer buffer;
//			int verticesCount = bytes.length / 12;
//
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
//				buffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
//			}
//			buffer.clear();
//			vboId = GL46.glGenBuffers();
//			GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
//			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buffer, GL46.GL_STATIC_DRAW);
//
//			int indicesAccessorIndex = primitive.getIndices();
//			if (eboId == 0) {
//				eboId = GL46.glGenBuffers();
//			}
//			Accessor indicesAccessor = model.getAccessors().get(indicesAccessorIndex);
//			bytes = AccessorHelper.viewBytes(model, indicesAccessor).getData();
//			ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
//			indicesBuffer.clear();
////			indicesCount = bytes.length / 4;
//			indicesCount = bytes.length / 2;
//			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboId);
//			GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);
////			GL46.glIndexPointer(GL46.GL_INT, 0, 0);
//			GL46.glIndexPointer(GL46.GL_UNSIGNED_SHORT, 0, 0);
//			GL46.glVertexPointer(3, GL11.GL_FLOAT, byteStride, 0);
//			int offset = 12;
//			if (texcoordBytes != null) {
//				GL46.glTexCoordPointer(2, GL11.GL_FLOAT, byteStride, offset);
//				offset += 8;
//			}
//			if (normalBytes != null) {
//				GL46.glNormalPointer(GL11.GL_FLOAT, byteStride, offset);
//				offset += 12;
//			}
//
//			GL46.glBindVertexArray(0);
//			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);
//
//			// MATERIAL START
//
//			int materialIndex = primitive.getMaterial();
//			Material material = materialIndex >= 0 ? model.getMaterials().get(materialIndex) : new Material();
//			PBRMetallicRoughness pbr = material.getPbrMetallicRoughness();
//			if (pbr != null) {
//				TextureInfo baseColorTextureInfo = pbr.getBaseColorTexture();
//				if (baseColorTextureInfo != null) {
//					Texture baseColorTexture = model.getTextures().get(baseColorTextureInfo.getIndex());
//					int baseColorSource = baseColorTexture.getSource();
//					if (baseColorSource >= 0) {
//						Image baseColorImage = model.getImages().get(baseColorTexture.getSource());
//						baseColorLocation = AccessorHelper.getModelDirFile(model, baseColorImage.getUri());
//					}
//				}
//			}
//			ByteBuffer buf = ByteBuffer.wrap(poses);
//			fb = FloatBuffer.allocate(poses.length / 4);
//			while (buf.hasRemaining()) {
//				float f1 = AccessorHelper.byteToFloat(buf.get(), buf.get(), buf.get(), buf.get()),
//						f2 = AccessorHelper.byteToFloat(buf.get(), buf.get(), buf.get(), buf.get()),
//						f3 = AccessorHelper.byteToFloat(buf.get(), buf.get(), buf.get(), buf.get());
//				fb.put(f1);
//				fb.put(f2);
//				fb.put(f3);
//			}
//			indicesBuffer.clear();
//			fb.clear();
//
////			FloatBuffer fb2 = FloatBuffer.allocate(indicesBuffer.remaining() / 4 * 3);
//			FloatBuffer fb2 = FloatBuffer.allocate(indicesBuffer.remaining() / 2 * 3);
//			while (indicesBuffer.hasRemaining()) {
////				int index = AccessorHelper.byteToInt(indicesBuffer.get(), indicesBuffer.get(), indicesBuffer.get(),
////						indicesBuffer.get());
//				int index = AccessorHelper.byteToShort(indicesBuffer.get(), indicesBuffer.get());
//				fb2.put(fb.get(index * 3));
//				fb2.put(fb.get(index * 3 + 1));
//				fb2.put(fb.get(index * 3 + 2));
//			}
//			fb = fb2;
//			fb2.clear();
//		}
//		baseColorLocation = new ResourceLocation("zsml:models/textures/initialshadinggroup_basecolor.jpeg");
//	}

	public void initDataGL46() {
		Map<String, Integer> attributes = primitive.getAttributes();
		Integer posAccessorIndex = attributes.get("POSITION");
		if (posAccessorIndex != null) {
			vaoId = GL46.glCreateVertexArrays();

			Accessor posAccessor = model.getAccessors().get(posAccessorIndex);
			vboId = GL46.glCreateBuffers();
//			GL46.glNamedBufferStorage(vboId, buffer, GL46.GL_MAP_WRITE_BIT);
//			GL45C.nglNamedBufferStorage(vboId, posAccessor., indicesCount, eboId);
			AccessorHelper.storageAccessorToBuffer(vboId, model, posAccessor, GL46.GL_MAP_WRITE_BIT);
			GL46.glVertexArrayAttribFormat(vaoId, 0, 3, GL46.GL_FLOAT, false, 0);
			GL46.glVertexArrayAttribBinding(vaoId, 0, 0);
			GL46.glEnableVertexArrayAttrib(vaoId, 0);
			GL46.glVertexArrayVertexBuffer(vaoId, 0, vboId, 0, 12);
///////////////////////////////////////////////////////////////////////////////////////////////////////
//			Integer texcoordAccessorIndex = attributes.get("TEXCOORD_0");
//			byte[] texcoordBytes = null;
//			if (texcoordAccessorIndex != null) {
//				Accessor texcoordlAccessor = model.getAccessors().get(texcoordAccessorIndex);
//				texcoordBytes = AccessorHelper.viewBytes(model, texcoordlAccessor).getData();
//				buffer = ByteBuffer.allocateDirect(bytes.length).put(texcoordBytes);
//				buffer.clear();
//				int texVboId = GL46.glCreateBuffers();
//				GL46.glNamedBufferStorage(texVboId, buffer, GL46.GL_MAP_WRITE_BIT);
//				GL46.glVertexArrayAttribFormat(vaoId, 1, 2, GL46.GL_FLOAT, false, 0);
//				GL46.glVertexArrayAttribBinding(vaoId, 1, 1);
//				GL46.glEnableVertexArrayAttrib(vaoId, 1);
//				GL46.glVertexArrayVertexBuffer(vaoId, 1, texVboId, 0, 8);
//				GL46.glVertexArrayAttribBinding(vaoId, 2, 1);
//				GL46.glEnableVertexArrayAttrib(vaoId, 2);
//				GL46.glVertexArrayAttribBinding(vaoId, 3, 1);
//				GL46.glEnableVertexArrayAttrib(vaoId, 3);
//			}
//
//			Integer normalAccessorIndex = attributes.get("NORMAL");
//			byte[] normalBytes = null;
//			if (normalAccessorIndex != null) {
//				Accessor normalAccessor = model.getAccessors().get(normalAccessorIndex);
//				normalBytes = AccessorHelper.viewBytes(model, normalAccessor).getData();
//			}
///////////////////////////////////////////////////////////////////////////////////////////////////////////

			int indicesAccessorIndex = primitive.getIndices();
//			eboId = GL46.glGenBuffers();
			eboId = GL46.glCreateBuffers();
			Accessor indicesAccessor = model.getAccessors().get(indicesAccessorIndex);
			AccessorHelper.storageAccessorToBuffer(eboId, model, indicesAccessor, GL46.GL_MAP_WRITE_BIT);
//			AccessorHelper.storageAccessorToBuffer(eboId, model, indicesAccessor, GL46.GL_MAP_WRITE_BIT);
			indicesCount = indicesAccessor.getCount();
//			GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, eboId);
//			GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);
//			GL46.glNamedBufferStorage(eboId, indicesBuffer, GL46.GL_MAP_WRITE_BIT);
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

	public void doRender(MatrixStack stack, IRenderTypeBuffer buffer) {
////		RenderSystem.defaultBlendFunc();
//		RenderSystem.enableDepthTest();
//		GL11.glColor4f(1, 1, 1, 1);
//		GL46.glBindVertexArray(vaoId);
//		GL11.glEnableClientState(GL46.GL_INDEX_ARRAY);
//		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
//		GL11.glEnableClientState(GL46.GL_NORMAL_ARRAY);
//
//		if (baseColorLocation != null) {
//			GL11.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//			Minecraft.getInstance().textureManager.bindTexture(baseColorLocation);
//		} else {
//			GL11.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//		}
////		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
//		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, indicesCount);
//		ZSML.LOGGER.warn(indicesCount);
//		GL46.glBindVertexArray(0);
//		GL46.glDisableClientState(GL46.GL_INDEX_ARRAY);
//		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
//		GL11.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//		GL11.glDisableClientState(GL46.GL_NORMAL_ARRAY);

//		IVertexBuilder builder = buffer.getBuffer(
//				RenderType.makeType("pos", DefaultVertexFormats.POSITION, GL46.GL_QUADS, poses.length, true, true,
//						State.getBuilder().transparency(new RenderState.TransparencyState("no_transparency", () -> {
//							RenderSystem.disableBlend();
//						}, () -> {
//						})).diffuseLighting(new RenderState.DiffuseLightingState(true))
//								.lightmap(new RenderState.LightmapState(true))
//								.overlay(new RenderState.OverlayState(true)).build(false)));

//		System.out.println(builder);
//		builder.begin(GL46.GL_TRIANGLES, DefaultVertexFormats.POSITION);
//		((BufferBuilder) builder).putBulkData(ByteBuffer.wrap(poses));

//		doRenderWithVertex(stack, buffer);
//		doRenderWithGL(stack);
//		RenderSystem.enableCull();
		RenderSystem.disableCull();
		doRenderWithGL46(stack);
	}

	public void doRenderWithGL46(MatrixStack stack) {
//		GL46.glPushMatrix();
//		GlStateManager.multMatrix(stack.getLast().getMatrix());

//		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		GL46.glColor4f(1, 1, 1, 1);
		GL46.glBindVertexArray(vaoId);

//		GL11.glEnableClientState(GL46.GL_INDEX_ARRAY);
//		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
//		GL11.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//		GL11.glEnableClientState(GL46.GL_NORMAL_ARRAY);

//		if (baseColorLocation != null) {
//			GL11.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//			Minecraft.getInstance().textureManager.bindTexture(baseColorLocation);
//		} else {
//			GL46.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//		}
		GL46.glDrawElements(GL46.GL_TRIANGLES, indicesCount, GL46.GL_UNSIGNED_INT, 0);
//		GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, indicesCount);
		GL46.glBindVertexArray(0);
//		GL46.glDisableClientState(GL46.GL_INDEX_ARRAY);
//		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
//		GL11.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
//		GL11.glDisableClientState(GL46.GL_NORMAL_ARRAY);
//		GL46.glPopMatrix();
	}

	public void doRenderWithVertex(MatrixStack stack, IRenderTypeBuffer buffer) {
//		IVertexBuilder builder = buffer.getBuffer(DemoRenderer.RENDER_TYPE);
		IVertexBuilder builder = buffer.getBuffer(RenderType.getItemEntityTranslucentCull(baseColorLocation));
//		ByteBuffer buf = ByteBuffer.wrap(poses);
//
//		buf.clear();
		fb.clear();
//		GlStateManager.enableCull();
		Matrix4f matrix = stack.getLast().getMatrix();
		Matrix3f normal = stack.getLast().getNormal();
		boolean flag = false;
		GL46.glDisable(GL46.GL_CULL_FACE);
		float t1 = 0, t2 = 0, t3 = 0;
		while (fb.remaining() >= 9) {

			for (int i = 0; i < 3; i++) {
				float f1 = fb.get(), f2 = fb.get(), f3 = fb.get();
				if (!flag) {
//					System.out.println(f1 + "|" + f2 + "|" + f3);
				}
				if (i == 0) {
					t1 = f1;
					t2 = f2;
					t3 = f3;
//					continue;

				}
				if (i == 1) {
//					DemoRenderer.vertex(builder, matrix, normal, t1, t2, t3, 255, 255, 255,
//							i == 1 || i == 2 ? 0.25F : 0, i < 2 ? 0.25F : 0, 15728640);
				}
				DemoRenderer.vertex(builder, matrix, normal, f1, f2, f3, 255, 255, 255, i == 1 || i == 2 ? 0.25F : 0,
						i < 2 ? 0.25F : 0, 15728640);
//				builder.pos(matrix, f1, f2, f3).endVertex();
//				builder.pos(matrix, f1, f2, 0).color(255, 255, 255, 128)
//						.tex(i == 1 || i == 2 ? 0.25F : 0, i < 2 ? 0.25F : 0).overlay(OverlayTexture.NO_OVERLAY)
//						.lightmap(15).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
//				builder.pos(matrix, f1, f2, f3).endVertex();
//				System.out.println(f1 + "|" + f2 + "|" + f3);

				if (i == 2) {
//					builder.pos(matrix, f1, f2 + 0.001F, 0).color(255, 255, 255, 128)
//							.tex(i == 1 || i == 2 ? 0.25F : 0, i < 2 ? 0.25F : 0).overlay(OverlayTexture.NO_OVERLAY)
//							.lightmap(15).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
					DemoRenderer.vertex(builder, matrix, normal, f1, f2, f3, 255, 255, 255,
							i == 1 || i == 2 ? 0.25F : 0, i < 2 ? 0.25F : 0, 15728640);
				}
			}
			flag = true;
		}
//		DemoRenderer.vertex(builder, matrix, normal, 0.5F, -0.25F, 255, 255, 255, 0, 0.25F, 15728640);
//		DemoRenderer.vertex(builder, matrix, normal, 1.5F, -0.25F, 255, 255, 255, 0.25F, 0.25F, 15728640);
//		DemoRenderer.vertex(builder, matrix, normal, 0.5F, 0.75F, 255, 255, 255, 0.25F, 0, 15728640);
//		DemoRenderer.vertex(builder, matrix, normal, -0.5F, 0.75F, 255, 255, 255, 0, 0, 15728640);
//
//		DemoRenderer.vertex(builder, matrix, normal, -0.103768F, -0.202914F, 255, 255, 255, 0, 0.25F, 15728640);
//		DemoRenderer.vertex(builder, matrix, normal, -0.047785F, -0.202914F, 255, 255, 255, 0.25F, 0.25F, 15728640);
//		DemoRenderer.vertex(builder, matrix, normal, -0.047785F, -0.206661F, 255, 255, 255, 0.25F, 0, 15728640);
//		DemoRenderer.vertex(builder, matrix, normal, -0.047785F, -0.206661F, 255, 255, 255, 0, 0, 15728640);
	}
}
