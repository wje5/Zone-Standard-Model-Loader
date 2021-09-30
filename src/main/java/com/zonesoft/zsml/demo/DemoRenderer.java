package com.zonesoft.zsml.demo;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.zonesoft.zsml.ModelLoader;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class DemoRenderer extends EntityRenderer<EntityDemo> {
	public static ResourceLocation LOCATION = new ResourceLocation("zsml:models/scene.gltf");

	public DemoRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EntityDemo entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer,
			int light) {
		super.render(entity, yaw, partialTicks, stack, buffer, light);
		GL11.glPushMatrix();
		GlStateManager.multMatrix(stack.getLast().getMatrix());
		ModelLoader.doRender(stack, LOCATION);
		GL11.glPopMatrix();
	}

	@Override
	public ResourceLocation getEntityTexture(EntityDemo entity) {
		return null;
	}
}
