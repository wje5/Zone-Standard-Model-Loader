package com.zonesoft.zsml;

import com.mojang.blaze3d.matrix.MatrixStack;

public class ModelRenderer {
	protected AbstractModel model;

	public ModelRenderer(AbstractModel model) {
		this.model = model;
	}

	public void doRender(MatrixStack stack) {

	}
}
