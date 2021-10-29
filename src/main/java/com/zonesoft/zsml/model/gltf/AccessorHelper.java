package com.zonesoft.zsml.model.gltf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL46;

import com.zonesoft.zsml.model.gltf.bean.Accessor;
import com.zonesoft.zsml.model.gltf.bean.Buffer;
import com.zonesoft.zsml.model.gltf.bean.BufferView;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class AccessorHelper {
	private static Map<Buffer, BufferData> cachedData = new HashMap<Buffer, BufferData>();
	private static Map<BufferView, BufferData> cachedBufferView = new HashMap<BufferView, BufferData>();

//	public static Vector3f getVec3(ModelGLTF model, Accessor accessor) {
//		byte[] data = viewBytes(model, accessor).getData();
//		int offset = accessor.getByteOffset();
//		float x = byteToFloat(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]);
//		float y = byteToFloat(data[offset + 4], data[offset + 5], data[offset + 6], data[offset + 7]);
//		float z = byteToFloat(data[offset + 8], data[offset + 9], data[offset + 10], data[offset + 11]);
//		return new Vector3f(x, y, z);
//	}

	public static float byteToFloat(byte b1, byte b2, byte b3, byte b4) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(4);
		buffer.put(b4);
		buffer.put(b3);
		buffer.put(b2);
		buffer.put(b1);
		buffer.flip();
		return buffer.getFloat();
	}

	public static int byteToInt(byte b1, byte b2, byte b3, byte b4) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(4);
		buffer.put(b4);
		buffer.put(b3);
		buffer.put(b2);
		buffer.put(b1);
		buffer.flip();
		return buffer.getInt();
	}

	public static short byteToShort(byte b1, byte b2) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(2);
		buffer.put(b2);
		buffer.put(b1);
		buffer.flip();
		return buffer.getShort();
	}

	public static BufferData viewBytes(ModelGLTF model, Accessor accessor) {
		System.out.println(getComponentLength(accessor.getComponentType()) + "|" + getComponentCount(accessor.getType())
				+ "|" + accessor.getCount());
		return viewBytes(model, model.getBufferViews().get(accessor.getBufferView()), accessor.getByteOffset(),
				getComponentLength(accessor.getComponentType()) * getComponentCount(accessor.getType())
						* accessor.getCount());
	}

	public static int getComponentLength(int componentType) {
		switch (componentType) {
		case GL46.GL_BYTE:
			return 1;
		case GL46.GL_UNSIGNED_BYTE:
			return 1;
		case GL46.GL_SHORT:
			return 2;
		case GL46.GL_UNSIGNED_SHORT:
			return 2;
		case GL46.GL_UNSIGNED_INT:
			return 4;
		case GL46.GL_FLOAT:
			return 4;
		}
		throw new IllegalArgumentException();
	}

	public static int getComponentCount(String type) {
		switch (type) {
		case "SCALAR":
			return 1;
		case "VEC2":
			return 2;
		case "VEC3":
			return 3;
		case "VEC4":
			return 4;
		case "MAT2":
			return 4;
		case "MAT3":
			return 9;
		case "MAT4":
			return 16;
		}
		throw new IllegalArgumentException();
	}

	public static BufferData viewBytes(ModelGLTF model, BufferView bufferView, int accessorOffset, int accessorLength) {
		System.out.println("bytes:" + bufferView.getByteOffset() + "|" + bufferView.getByteLength() + "|"
				+ accessorOffset + "|" + accessorLength);
		BufferData data = cachedBufferView.get(bufferView);
		if (data != null) {
			return data;
		}
		int offset = bufferView.getByteOffset();
		data = new BufferData(
				Arrays.copyOfRange(getBufferData(model, model.getBuffers().get(bufferView.getBuffer())).data,
						offset + accessorOffset, offset + accessorOffset + accessorLength));
		System.out.println("buffer" + bufferView.getBuffer() + ":"
				+ getBufferData(model, model.getBuffers().get(bufferView.getBuffer())).data.length);
		cachedBufferView.put(bufferView, data);
		return data;
	}

	public static BufferData getBufferData(ModelGLTF model, Buffer buffer) {
		BufferData bufferData = cachedData.get(buffer);
		if (bufferData != null) {
			return bufferData;
		}
		ResourceLocation location = getModelDirFile(model, buffer.getUri());
		InputStream stream = null;
		try {
			stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
			byte[] data = new byte[buffer.getByteLength()];
//			System.out.println(data.length + "||||||||" + stream.available());
//			stream.read(data);
//			System.out.println(data.length + "||||||||" + stream.available());
//			for (byte b : data) {
//				System.out.println(b);
//			}
//			System.out.println("END_______________");
			ByteBuffer bb = TextureUtil.readToBuffer(stream);
			bb.flip();
			bb.get(data);
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

	public static ResourceLocation getModelDirFile(ModelGLTF model, String filePath) {
		String[] a = model.getPath().getPath().split("/");
		a[a.length - 1] = filePath;
		return new ResourceLocation(model.getPath().getNamespace(), String.join("/", a));
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
