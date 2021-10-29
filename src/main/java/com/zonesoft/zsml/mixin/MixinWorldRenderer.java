package com.zonesoft.zsml.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.zonesoft.zsml.GLManager;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Matrix4f;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/IProfiler;endStartSection(Ljava/lang/String;)V", args = "ldc=blockentities"), cancellable = true)
	private void injectUpdateCameraAndRender(MatrixStack matrixStackIn, float partialTicks, long finishTimeNano,
			boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn, GameRenderer gameRendererIn,
			LightTexture lightmapIn, Matrix4f projectionIn, CallbackInfo info) {
		GLManager.finishRenders();
	}

//	@Overwrite
//	public void updateCameraAndRender(MatrixStack matrixStackIn, float partialTicks, long finishTimeNano,
//			boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn, GameRenderer gameRendererIn,
//			LightTexture lightmapIn, Matrix4f projectionIn) {
//		System.out.println("DRRRRRRRRRRRRRRRRRRRRRRRRRRRR!");
//	}
}
