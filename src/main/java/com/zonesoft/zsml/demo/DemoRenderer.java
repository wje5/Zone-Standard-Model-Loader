package com.zonesoft.zsml.demo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.zonesoft.zsml.ModelLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class DemoRenderer extends EntityRenderer<EntityDemo> {
	public static ResourceLocation LOCATION = new ResourceLocation("zsml:models/scene.gltf");

	public static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation(
			"textures/entity/experience_orb.png");
	public static final RenderType RENDER_TYPE = RenderType.getItemEntityTranslucentCull(EXPERIENCE_ORB_TEXTURES);

	public static int vaoId, vboId, eboId;

	public DemoRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EntityDemo entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer,
			int light) {
		super.render(entity, yaw, partialTicks, stack, buffer, light);
		GL46.glPushMatrix();
		GlStateManager.multMatrix(stack.getLast().getMatrix());
		ModelLoader.doRender(stack, buffer, LOCATION, light);
//		renderLightMap();
//		renderLightMapGL();
//		renderLightMapGL46();
//		GuiUtils.drawTexturedModalRect(x, y, u, v, width, height, zLevel);
		GL46.glPopMatrix();
	}

	public static void renderLightMapGL() {
		float uScale = 1f / 0x100;
		float vScale = 1f / 0x100;
		int x = 0, y = 0, zLevel = 0;
		int width = 2, height = 2, u = 0, v = 0;
		GL46.glEnableClientState(GL46.GL_VERTEX_ARRAY);
		GL46.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
		Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
//		GL46.glDisable(GL46.GL_TEXTURE_2D);
//		GL46.glActiveTexture(GL46.GL_TEXTURE0);
//		GL46.glDisable(GL46.GL_TEXTURE_2D);
		GL46.glActiveTexture(GL46.GL_TEXTURE2);
		GL46.glEnable(GL46.GL_TEXTURE_2D);

		GL46.glBegin(GL46.GL_QUADS);
		GL46.glVertex3f(x, y + height, zLevel);
		GL46.glTexCoord2f(0, 1);
		GL46.glVertex3f(x + width, y + height, zLevel);
		GL46.glTexCoord2f(1, 1);
		GL46.glVertex3f(x + width, y, zLevel);
		GL46.glTexCoord2f(1, 0);
		GL46.glVertex3f(x, y, zLevel);
		GL46.glTexCoord2f(0, 0);

//		GL46.glActiveTexture(GL46.GL_TEXTURE2);
//		GL46.glEnable(GL46.GL_TEXTURE_2D);
		GL46.glEnd();
		GL46.glDisableClientState(GL46.GL_VERTEX_ARRAY);
		GL46.glDisableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
		Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();
	}

	public static void renderLightMapGL46() {
		float uScale = 1f / 0x100;
		float vScale = 1f / 0x100;
		int x = 0, y = 0, zLevel = 0;
		int width = 2, height = 2, u = 0, v = 0;
//		vboId = GL46.glGenBuffers();

//		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboId);
		float[] pos = new float[] { x, y + height, zLevel, x + width, y + height, zLevel, x + width, y, zLevel, x, y,
				zLevel };
		float[] tex = new float[] { 0, 1, 1, 1, 1, 0, 0, 0 };
		short[] tex2 = new short[] { 10, 10, 10, 10, 10, 10, 10, 10 };
		ByteBuffer buf = ByteBuffer.allocateDirect(80);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < 4; i++) {
			buf.putFloat(pos[i * 3]);
			buf.putFloat(pos[i * 3 + 1]);
			buf.putFloat(pos[i * 3 + 2]);
			buf.putFloat(tex[i * 2]);
			buf.putFloat(tex[i * 2 + 1]);
//			buf.putInt(tex2[i]);
//			buf.putShort(tex2[i * 2]);
//			buf.putShort(tex2[i * 2 + 1]);
		}
		buf.clear();
		RenderSystem.enableDepthTest();
		long pointer = MemoryUtil.memAddress(buf);
//		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buf, GL46.GL_STATIC_DRAW);
		Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
		GL46.glVertexPointer(3, GL46.GL_FLOAT, 20, pointer);
		GL46.glEnableClientState(GL46.GL_VERTEX_ARRAY);

//		GL46.glDisable(GL46.GL_TEXTURE_2D);
		GL46.glClientActiveTexture(GL46.GL_TEXTURE2);
		GL46.glTexCoordPointer(2, GL46.GL_FLOAT, 20, pointer + 12);
//		GL46.glTexCoordPointer(2, GL46.GL_SHORT, 16, pointer + 12);
		GL46.glEnableClientState(GL46.GL_TEXTURE_COORD_ARRAY);
		GL46.glClientActiveTexture(GL46.GL_TEXTURE0);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
//		GL46.glDisable(GL46.GL_TEXTURE_2D);
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
		GL46.glActiveTexture(GL46.GL_TEXTURE2);
		Minecraft.getInstance().textureManager.bindTexture(EXPERIENCE_ORB_TEXTURES);
		Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
		GL46.glEnable(GL46.GL_TEXTURE_2D);
//		GL46.glEnable(GL46.GL_TEXTURE_2D);

//		GL46.glActiveTexture(GL46.GL_TEXTURE2);

		GL46.glDrawArrays(GL46.GL_QUADS, 0, 4);
//		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
		Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();

	}

	public static void renderLightMap() {
		float uScale = 1f / 0x100;
		float vScale = 1f / 0x100;
		int x = -3, y = 0, zLevel = 0;
		int width = 2, height = 2, u = 0, v = 0;
		Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder wr = tessellator.getBuffer();
		wr.begin(GL11.GL_QUADS,
				new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(DefaultVertexFormats.POSITION_3F)
						.add(DefaultVertexFormats.TEX_2F).add(DefaultVertexFormats.TEX_2SB).build()));
		int l = 0xA000A0;
		wr.pos(x, y + height, zLevel).tex(u * uScale, ((v + height) * vScale)).lightmap(0xF0).endVertex();
		wr.pos(x + width, y + height, zLevel).tex((u + width) * uScale, ((v + height) * vScale)).lightmap(0xF000F0)
				.endVertex();
		wr.pos(x + width, y, zLevel).tex((u + width) * uScale, (v * vScale)).lightmap(0xF00000).endVertex();
		wr.pos(x, y, zLevel).tex(u * uScale, (v * vScale)).lightmap(0).endVertex();
		tessellator.draw();
		Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();
	}

	public static void drawCube(float size) {
		GL46.glBegin(GL11.GL_QUADS);
		GL46.glVertex3f(0, size, size);
		GL46.glVertex3f(size, size, size);
		GL46.glVertex3f(size, size, 0);
		GL46.glVertex3f(0, size, 0);

		GL46.glVertex3f(0, 0, 0);
		GL46.glVertex3f(size, 0, 0);
		GL46.glVertex3f(size, 0, size);
		GL46.glVertex3f(0, 0, size);

		GL46.glVertex3f(0, size, size);
		GL46.glVertex3f(size, size, size);
		GL46.glVertex3f(size, 0, size);
		GL46.glVertex3f(0, 0, size);

		GL46.glVertex3f(0, 0, 0);
		GL46.glVertex3f(size, 0, 0);
		GL46.glVertex3f(size, size, 0);
		GL46.glVertex3f(0, size, 0);

		GL46.glVertex3f(size, 0, size);
		GL46.glVertex3f(size, size, size);
		GL46.glVertex3f(size, size, 0);
		GL46.glVertex3f(size, 0, 0);

		GL46.glVertex3f(0, 0, 0);
		GL46.glVertex3f(0, size, 0);
		GL46.glVertex3f(0, size, size);
		GL46.glVertex3f(0, 0, size);
		GL46.glEnd();
	}

	public static void drawCubeWithGL46(float size) {
//		float[] vertices = new float[] { 0, size, size, size, size, size, size, size, 0, 0, size, 0, 0, 0, 0, size, 0,
//				0, size, 0, size, 0, 0, size, 0, size, size, size, size, size, size, 0, size, 0, 0, size, 0, 0, 0, size,
//				0, 0, size, size, 0, 0, size, 0, size, 0, size, size, size, size, size, size, 0, size, 0, 0, 0, 0, 0, 0,
//				size, 0, 0, size, size, 0, 0, size };
		float[] vertices = new float[] { 0, 0, 0, size, 0, 0, 0, size, 0, size, size, 0, 0, 0, size, size, 0, size, 0,
				size, size, size, size, size };
		int[] indices = new int[] { 6, 7, 3, 2, 0, 1, 5, 4, 6, 7, 5, 4, 0, 1, 3, 2, 5, 7, 3, 1, 0, 2, 6, 4 };
		if (vaoId == 0) {
			vaoId = GL46.glCreateVertexArrays();
			ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.asFloatBuffer().put(vertices);
			bb.clear();
			vboId = GL46.glCreateBuffers();
			GL46.glNamedBufferStorage(vboId, bb, GL46.GL_MAP_WRITE_BIT);
			GL46.glVertexArrayAttribFormat(vaoId, 0, 3, GL46.GL_FLOAT, false, 0);
			GL46.glVertexArrayAttribBinding(vaoId, 0, 0);
			GL46.glEnableVertexArrayAttrib(vaoId, 0);
			GL46.glVertexArrayVertexBuffer(vaoId, 0, vboId, 0, 12);

			bb = ByteBuffer.allocateDirect(vertices.length * 4);
			bb.order(ByteOrder.nativeOrder());
			bb.asIntBuffer().put(indices);
			bb.clear();
			eboId = GL46.glCreateBuffers();
			GL46.glNamedBufferStorage(eboId, bb, GL46.GL_MAP_WRITE_BIT);
			GL46.glVertexArrayElementBuffer(vaoId, eboId);
		}

		RenderSystem.enableDepthTest();
		GL46.glColor4f(1, 1, 1, 1);
		GL46.glBindVertexArray(vaoId);
//		GL46.glDrawArrays(GL46.GL_QUADS, 0, vertices.length / 3);
		GL46.glDrawElements(GL46.GL_TRIANGLES, indices.length, GL46.GL_UNSIGNED_INT, 0);
		GL46.glBindVertexArray(0);
	}

	public void renderOrb(EntityDemo entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer,
			int light) {
		stack.push();
		int i = 0;
		float f = (i % 4 * 16 + 0) / 64.0F;
		float f1 = (i % 4 * 16 + 16) / 64.0F;
		float f2 = (i / 4 * 16 + 0) / 64.0F;
		float f3 = (i / 4 * 16 + 16) / 64.0F;
		float f8 = (0 + partialTicks) / 2.0F;
		int j = (int) ((MathHelper.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
		int k = 255;
		int l = (int) ((MathHelper.sin(f8 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
		stack.translate(0.0D, 0.1F, 0.0D);
		stack.rotate(this.renderManager.getCameraOrientation());
		stack.rotate(Vector3f.YP.rotationDegrees(180.0F));
		float f9 = 0.3F;
		stack.scale(0.3F, 0.3F, 0.3F);
		IVertexBuilder ivertexbuilder = buffer.getBuffer(RENDER_TYPE);
		MatrixStack.Entry matrixstack$entry = stack.getLast();
		Matrix4f matrix4f = matrixstack$entry.getMatrix();
		Matrix3f matrix3f = matrixstack$entry.getNormal();
		vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, j, 255, l, f, f3, light);
		vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, j, 255, l, f1, f3, light);
		vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, j, 255, l, f1, f2, light);
		vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, j, 255, l, f, f2, light);

//		vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, j, 255, l, f, f3, light);
//		vertex(ivertexbuilder, matrix4f, matrix3f, 1.5F, -0.25F, j, 255, l, f1, f3, light);
//		vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, j, 255, l, f1, f2, light);
//		vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, j, 255, l, f, f2, light);
//		GL46.glPushMatrix();
//		GlStateManager.multMatrix(stack.getLast().getMatrix());

//		GL46.glPopMatrix();
		stack.pop();
	}

	public static void vertex(IVertexBuilder bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y,
			int red, int green, int blue, float texU, float texV, int packedLight) {
		bufferIn.pos(matrixIn, x, y, 0.0F).color(red, green, blue, 128).tex(texU, texV)
				.overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F)
				.endVertex();
	}

	public static void vertex(IVertexBuilder bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y,
			float z, int red, int green, int blue, float texU, float texV, int packedLight) {
		bufferIn.pos(matrixIn, x, y, z).color(red, green, blue, 128).tex(texU, texV).overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
	}

	@Override
	public ResourceLocation getEntityTexture(EntityDemo entity) {
		return null;
	}
}
