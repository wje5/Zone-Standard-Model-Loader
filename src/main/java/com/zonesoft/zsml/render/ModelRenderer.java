package com.zonesoft.zsml.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.zonesoft.zsml.model.AbstractModel;

public class ModelRenderer {
	protected AbstractModel model;

	public ModelRenderer(AbstractModel model) {
		this.model = model;
	}

	public void doRender(MatrixStack stack) {

	}
}
