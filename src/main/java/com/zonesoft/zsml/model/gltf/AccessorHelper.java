package com.zonesoft.zsml.model.gltf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class AccessorHelper {
	private static Map<Buffer, BufferData> cachedData = new HashMap<Buffer, BufferData>();
	private static Map<BufferView, BufferData> cachedBufferView = new HashMap<BufferView, BufferData>();

	public static Vector3f getVec3(ModelGLTF model, Accessor accessor) {
		BufferView bufferview = model.getBufferViews().get(accessor.getBufferView());
		byte[] data = viewBytes(model, bufferview).getData();
		int offset = accessor.getByteOffset();
		float x = byteToFloat(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]);
		float y = byteToFloat(data[offset + 4], data[offset + 5], data[offset + 6], data[offset + 7]);
		float z = byteToFloat(data[offset + 8], data[offset + 9], data[offset + 10], data[offset + 11]);
		return new Vector3f(x, y, z);
	}

	public static float byteToFloat(byte b1, byte b2, byte b3, byte b4) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(4);
		buffer.put(b4);
		buffer.put(b3);
		buffer.put(b2);
		buffer.put(b1);
		buffer.flip();
		return buffer.getFloat();
	}

	public static BufferData viewBytes(ModelGLTF model, Accessor accessor) {
		return viewBytes(model, model.getBufferViews().get(accessor.getBufferView()));
	}

	public static BufferData viewBytes(ModelGLTF model, BufferView bufferView) {
		BufferData data = cachedBufferView.get(bufferView);
		if (data != null) {
			return data;
		}
		int offset = bufferView.getByteOffset();
		int length = bufferView.getByteLength();
		data = new BufferData(Arrays.copyOfRange(
				getBufferData(model, model.getBuffers().get(bufferView.getBuffer())).data, offset, offset + length));
		cachedBufferView.put(bufferView, data);
		return data;
	}

	public static BufferData getBufferData(ModelGLTF model, Buffer buffer) {
		BufferData bufferData = cachedData.get(buffer);
		if (bufferData != null) {
			return bufferData;
		}
		String[] a = model.getPath().getPath().split("/");
		a[a.length - 1] = buffer.getUri();
		String path = String.join("/", a);
		ResourceLocation location = new ResourceLocation(model.getPath().getNamespace(), path);
		InputStream stream = null;
		try {
			stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
			byte[] data = new byte[buffer.getByteLength()];
			stream.read(data);
			bufferData = new BufferData(data);
			cachedData.put(buffer, bufferData);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bufferData;
	}

	public static class BufferData {
		private byte[] data;

		public BufferData(byte[] data) {
			this.data = data;
		}

		public byte[] getData() {
			return data;
		}
	}
}
