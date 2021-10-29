package com.zonesoft.zsml.demo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.zonesoft.zsml.GLManager;
import com.zonesoft.zsml.ModelLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
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
//		stack.push();
		GL46.glPushMatrix();
//		GlStateManager.multMatrix(stack.getLast().getMatrix());
//		ModelLoader.doRender(stack, buffer, LOCATION);

//		stack.translate(0.0D, 0.1F, 0.0D);
//		stack.rotate(this.renderManager.getCameraOrientation());
//		stack.rotate(Vector3f.YP.rotationDegrees(180.0F));
		GL46.glColor4f(1, 1, 1, 1);
//		ModelLoader.doRender(stack, buffer, LOCATION);
		GL46.glPopMatrix();
//		renderOrb(entity, yaw, partialTicks, stack, buffer, light);
		GLManager.addRenderRunnable(() -> {
			stack.push();
//			stack.translate(entity.getPosX() / 100F, entity.getPosY() / 100F, entity.getPosZ() / 100F);
			GL46.glPushMatrix();
//			ModelLoader.doRender(stack, buffer, LOCATION);
//			renderOrb(entity, yaw, partialTicks, stack, buffer, light);
			GL46.glPopMatrix();
			stack.pop();
		});
//		GLManager.addRenderRunnable(() -> {
//			GlStateManager.disableCull();
//			stack.push();
//			stack.rotate(renderManager.getCameraOrientation());
//			stack.rotate(Vector3f.YP.rotationDegrees(180.0F));
//			stack.translate(entity.getPosX(), entity.getPosY(), entity.getPosZ());
//			GL46.glPushMatrix();
//			GlStateManager.multMatrix(stack.getLast().getMatrix());
//			GL46.glBegin(GL11.GL_QUADS);
//			GL46.glVertex3f(-0.5F, -0.25F, 0);
//			GL46.glVertex3f(0.5F, -0.25F, 0);
//			GL46.glVertex3f(0.5F, 0.75F, 0);
//			GL46.glVertex3f(-0.5F, 0.75F, 0);
//			GL46.glEnd();
//			GL46.glPopMatrix();
//			stack.pop();
//		});
		GLManager.addRenderRunnable(() -> {
			GlStateManager.enableDepthTest();
//			GlStateManager.enableCull();
			GL46.glPushMatrix();
			GlStateManager.multMatrix(stack.getLast().getMatrix());
//			GL46.glLoadIdentity();
			PlayerEntity player = Minecraft.getInstance().player;
			GlStateManager.translated(entity.getPosX() - player.getPosX(), entity.getPosY() - player.getPosY(),
					entity.getPosZ() - player.getPosZ());
//			System.out.println((entity.getPosX() - player.getPosX()) + "|" + (entity.getPosY() - player.getPosY()) + "|"
//					+ (entity.getPosZ() - player.getPosZ()));
			GlStateManager.color4f(1, 1, 1, 1);
//			drawCube(0.1F);
//			drawCubeWithGL46(0.5F);
//			ModelLoader.doRender(stack, buffer, LOCATION);
			GL46.glPopMatrix();
		});
//		stack.pop();
		GL46.glPushMatrix();
		GlStateManager.multMatrix(stack.getLast().getMatrix());
		ModelLoader.doRender(stack, buffer, LOCATION);
		GL46.glPopMatrix();
//		renderOrb(entity, yaw, partialTicks, stack, buffer, light);

		super.render(entity, yaw, partialTicks, stack, buffer, light);
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
