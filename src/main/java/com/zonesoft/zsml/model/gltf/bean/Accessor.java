package com.zonesoft.zsml.model.gltf.bean;

import java.util.Map;

public class Accessor {
	private int bufferView = -1;
	private int byteOffset = 0;
	private int componentType;
	private boolean normalized;
	private int count;
	private String type;
	private float[] max;
	private float[] min;
	private Sparse sparse;
	private String name;

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

	public boolean isNormalized() {
		return normalized;
	}

	public int getCount() {
		return count;
	}

	public String getType() {
		return type;
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

	public String getName() {
		return name;
	}

	public static class Sparse {
		int count;
		Map<String, Integer> indices;
		Map<String, Integer> values;

		public Sparse() {

		}
	}
}
