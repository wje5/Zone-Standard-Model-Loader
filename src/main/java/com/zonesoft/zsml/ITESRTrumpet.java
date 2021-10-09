package com.zonesoft.zsml;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.zonesoft.zsml.demo.DemoRenderer;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class ITESRTrumpet extends ItemStackTileEntityRenderer {
	@Override
	public void func_239207_a_(ItemStack stack, TransformType type, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay) {
		GL11.glPushMatrix();
		GlStateManager.multMatrix(matrixStack.getLast().getMatrix());
		ModelLoader.doRender(matrixStack, DemoRenderer.LOCATION);
		GL11.glPopMatrix();
	}
}
