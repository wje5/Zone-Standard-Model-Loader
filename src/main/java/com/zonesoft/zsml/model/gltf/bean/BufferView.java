package com.zonesoft.zsml.model.gltf.bean;

public class BufferView {
	private int buffer = -1;
	private int byteOffset;
	private int byteLength;
	private int target;

	public BufferView() {

	}

	public int getBuffer() {
		return buffer;
	}

	public int getByteOffset() {
		return byteOffset;
	}

	public int getByteLength() {
		return byteLength;
	}

	public int getTarget() {
		return target;
	}
}
