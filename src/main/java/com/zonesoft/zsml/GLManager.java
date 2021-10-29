package com.zonesoft.zsml;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.annotation.Nonnull;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GLManager {
	private static Queue<Runnable> renderRunnables = new ConcurrentLinkedDeque<Runnable>();

	public static void addRenderRunnable(Runnable runnable) {
		renderRunnables.offer(runnable);
	}

	public static void finishRenders() {
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		Runnable e;
		while (true) {
			e = renderRunnables.poll();
			if (e == null) {
				return;
			}
			e.run();
		}
	}

	@SubscribeEvent
	public static void onRenderTick(@Nonnull TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
//			finishRenders();
		}
	}
}
