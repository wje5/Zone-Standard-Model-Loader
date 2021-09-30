package com.zonesoft.zsml.model.gltf.bean;

public class Accessor {
	public static final int BYTE = 5120;
	public static final int UNSIGNED_BYTE = 5121;
	public static final int SHORT = 5122;
	public static final int UNSIGNED_SHORT = 5123;
	public static final int UNSIGNED_INT = 5125;
	public static final int FLOAT = 5126;

	public static final String SCALAR = "SCALAR";
	public static final String VEC2 = "VEC2";
	public static final String VEC3 = "VEC3";
	public static final String VEC4 = "VEC4";
	public static final String MAT2 = "MAT2";
	public static final String MAT3 = "MAT3";
	public static final String MAT4 = "MAT4";

	private int bufferView = -1;
	private int byteOffset = 0;
	private int componentType;
	private String type;
	private int count;
	private float[] max;
	private float[] min;
	private Sparse sparse;

	public Accessor() {

	}

	public int getBufferView() {
		return bufferView;
	}

	public int getByteOffset() {
		return byteOffset;
	}

	public int getComponentType() {
		return componentType;
	}

	public String getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	public float[] getMax() {
		return max;
	}

	public float[] getMin() {
		return min;
	}

	public Sparse getSparse() {
		return sparse;
	}
}
