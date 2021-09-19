package com.zonesoft.zsml.model.gltf;

public class Node {
	int[] children;
	int camera = -1;
	float[] matrix;
	int mesh = -1;
	float[] rotation;
	float[] translation;
	float[] scale;

	public Node() {

	}

	public int[] getChildren() {
		return children;
	}

	public int getCamera() {
		return camera;
	}

	public float[] getMatrix() {
		return matrix;
	}

	public int getMesh() {
		return mesh;
	}

	public float[] getRotation() {
		return rotation;
	}

	public float[] getTranslation() {
		return translation;
	}

	public float[] getScale() {
		return scale;
	}
}
