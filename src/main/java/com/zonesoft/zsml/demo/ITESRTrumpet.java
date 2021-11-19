package com.zonesoft.zsml.demo;

import org.lwjgl.opengl.GL46;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.zonesoft.zsml.ModelLoader;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class ITESRTrumpet extends ItemStackTileEntityRenderer {
	@Override
	public void func_239207_a_(ItemStack stack, TransformType type, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay) {
		super.func_239207_a_(stack, type, matrixStack, buffer, combinedLight, combinedOverlay);
		GL46.glPushMatrix();
		GlStateManager.multMatrix(matrixStack.getLast().getMatrix());
		ModelLoader.doRender(matrixStack, buffer, DemoRenderer.LOCATION, combinedLight);
		GL46.glPopMatrix();
	}
}
