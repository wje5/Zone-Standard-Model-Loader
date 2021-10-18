package com.zonesoft.zsml.demo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.zonesoft.zsml.ModelLoader;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

	public DemoRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EntityDemo entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer,
			int light) {
		super.render(entity, yaw, partialTicks, stack, buffer, light);
		stack.push();
//		GL46.glPushMatrix();
////		GlStateManager.multMatrix(stack.getLast().getMatrix());
//		ModelLoader.doRender(stack, buffer, LOCATION);
//		GL46.glPopMatrix();
		stack.translate(0.0D, 0.1F, 0.0D);
		stack.rotate(this.renderManager.getCameraOrientation());
		stack.rotate(Vector3f.YP.rotationDegrees(180.0F));
		ModelLoader.doRender(stack, buffer, LOCATION);
		stack.pop();

//		renderOrb(entity, yaw, partialTicks, stack, buffer, light);

		super.render(entity, yaw, partialTicks, stack, buffer, light);
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
