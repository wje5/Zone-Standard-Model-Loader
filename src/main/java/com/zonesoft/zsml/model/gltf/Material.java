package com.zonesoft.zsml.model.gltf;

public class Material {
	private String name;
	private PBRMetallicRoughness pbrMetallicRoughness;

	public Material() {

	}

	public String getName() {
		return name;
	}

	public static class PBRMetallicRoughness {
		private float[] baseColorFactor = new float[] { 1, 1, 1, 1 };
		private TextureInfo baseColorTexture;
		private float metallicFactor = 1;
		private float roughnessFactor = 1;
		private TextureInfo metallicRoughnessTexture;
		private NormalTextureInfo normalTexture;
		private OcclusionTextureInfo occlusionTexture;
		private TextureInfo emissiveTexture;
		private float[] emissiveFactor = new float[] { 0, 0, 0 };
		private String alphaMode = "OPAQUE";
		private float alphaCutoff = 0.5F;
		private boolean doubleSided;

		public PBRMetallicRoughness() {

		}

		public float[] getBaseColorFactor() {
			return baseColorFactor;
		}

		public TextureInfo getBaseColorTexture() {
			return baseColorTexture;
		}

		public float getMetallicFactor() {
			return metallicFactor;
		}

		public float getRoughnessFactor() {
			return roughnessFactor;
		}

		public TextureInfo getMetallicRoughnessTexture() {
			return metallicRoughnessTexture;
		}

		public NormalTextureInfo getNormalTexture() {
			return normalTexture;
		}

		public OcclusionTextureInfo getOcclusionTexture() {
			return occlusionTexture;
		}

		public TextureInfo getEmissiveTexture() {
			return emissiveTexture;
		}

		public float[] getEmissiveFactor() {
			return emissiveFactor;
		}

		public String getAlphaMode() {
			return alphaMode;
		}

		public float getAlphaCutoff() {
			return alphaCutoff;
		}

		public boolean isDoubleSided() {
			return doubleSided;
		}
	}

	public static class NormalTextureInfo extends TextureInfo {
		private float scale = 1;

		public NormalTextureInfo() {

		}

		public float getScale() {
			return scale;
		}
	}

	public static class OcclusionTextureInfo extends TextureInfo {
		private float strength = 1;

		public OcclusionTextureInfo() {

		}

		public float getStrength() {
			return strength;
		}
	}
}
