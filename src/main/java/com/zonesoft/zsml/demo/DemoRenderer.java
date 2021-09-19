package com.zonesoft.zsml.demo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.zonesoft.zsml.model.ModelLoader;

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
		ModelLoader.doRender(LOCATION);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityDemo entity) {
		return null;
	}
}
