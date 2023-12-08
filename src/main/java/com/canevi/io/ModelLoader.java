package com.canevi.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import com.canevi.graphics.Material;
import com.canevi.graphics.Mesh;
import com.canevi.graphics.Vertex;
import com.canevi.maths.Vector2f;
import com.canevi.maths.Vector3f;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelLoader {
	public static Mesh loadModel(String filePath, String texturePath, int meshIndex, boolean shouldFlip) {
		String absolutePath = ResourceLoader.load(filePath);
		/// aiImportFile fails on Windows if the path starts with a slash
		if (System.getProperty("os.name").contains("Windows")) {
			if (absolutePath.startsWith("/"))
				absolutePath = absolutePath.substring(1);
		}
		AIScene scene = Assimp.aiImportFile(absolutePath,
				Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate);

		if (scene == null) {
			log.info("Couldn't load model at " + filePath + "\nError: " + Assimp.aiGetErrorString());
			return null;
		}
		AIMesh mesh = AIMesh.create(scene.mMeshes().get(meshIndex));
		int vertexCount = mesh.mNumVertices();

		AIVector3D.Buffer vertices = mesh.mVertices();
		AIVector3D.Buffer normals = mesh.mNormals();

		Vertex[] vertexList = new Vertex[vertexCount];

		for (int i = 0; i < vertexCount; i++) {
			AIVector3D vertex = vertices.get(i);
			Vector3f meshVertex = new Vector3f(vertex.x(), vertex.y(), vertex.z());

			AIVector3D normal = normals.get(i);
			Vector3f meshNormal = new Vector3f(normal.x(), normal.y(), normal.z());

			Vector2f meshTextureCoord = new Vector2f(0, 0);
			if (mesh.mNumUVComponents().get(0) != 0) {
				AIVector3D texture = mesh.mTextureCoords(0).get(i);
				meshTextureCoord.setX(texture.x());
				meshTextureCoord.setY(texture.y());
			}

			vertexList[i] = new Vertex(meshVertex, meshNormal, meshTextureCoord);
		}

		int faceCount = mesh.mNumFaces();
		AIFace.Buffer indices = mesh.mFaces();
		int[] indicesList = new int[faceCount * 3];

		for (int i = 0; i < faceCount; i++) {
			AIFace face = indices.get(i);
			indicesList[i * 3 + 0] = face.mIndices().get(0);
			indicesList[i * 3 + 1] = face.mIndices().get(1);
			indicesList[i * 3 + 2] = face.mIndices().get(2);
		}
		Material material = new Material(texturePath, shouldFlip);

		return new Mesh(vertexList, indicesList, material);
	}

	public static List<Mesh> loadModel(String filePath, List<String> textures, boolean shouldFlip) {
		String absolutePath = ResourceLoader.load(filePath);

		// aiImportFile fails on Windows if the path starts with a slash
		if (System.getProperty("os.name").contains("Windows")) {
			if (absolutePath.startsWith("/"))
				absolutePath = absolutePath.substring(1);
		}
		AIScene scene = Assimp.aiImportFile(absolutePath,
				Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_JoinIdenticalVertices);
		// Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate
		// Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_JoinIdenticalVertices

		if (scene == null) {
			log.info("Couldn't load model at " + filePath + "\nError: " + Assimp.aiGetErrorString());
			return Collections.emptyList();
		}

		List<Mesh> meshes = new ArrayList<>();
		List<Material> materials = new ArrayList<>();
		PointerBuffer p = scene.mMeshes();
		int meshAmount = scene.mNumMeshes();

		/*
		 * if (scene.mMaterials() != null) {
		 * for (int i = 0; i < scene.mNumMaterials(); i++) {
		 * AIMaterial aiMaterial = AIMaterial.create(scene.mMaterials().get(i));
		 * Material material = processMaterial(aiMaterial, filePath);
		 * materials.add(material);
		 * }
		 * }
		 */
		for (int i = 0; i < meshAmount; i++) {
			AIMesh aiMesh = AIMesh.create(p.get(i));
			meshes.add(loadModelNew(aiMesh, textures.get(i), materials, shouldFlip));
		}

		return meshes;
	}

	private static Mesh loadModelNew(AIMesh aiMesh, String texturePath, List<Material> materials, boolean shouldFlip) {
		int vertexCount = aiMesh.mNumVertices();
		int materialIndex = aiMesh.mMaterialIndex();

		AIVector3D.Buffer vertices = aiMesh.mVertices();
		AIVector3D.Buffer normals = aiMesh.mNormals();

		Vertex[] vertexList = new Vertex[vertexCount];

		for (int j = 0; j < vertexCount; j++) {
			AIVector3D vertex = vertices.get(j);
			Vector3f meshVertex = new Vector3f(vertex.x(), vertex.y(), vertex.z());

			AIVector3D normal = normals.get(j);
			Vector3f meshNormal = new Vector3f(normal.x(), normal.y(), normal.z());

			Vector2f meshTextureCoord = new Vector2f(0, 0);
			if (aiMesh.mNumUVComponents().get(0) != 0) {
				AIVector3D.Buffer textureCoords = aiMesh.mTextureCoords(0);
				AIVector3D texture = textureCoords.get(j);
				meshTextureCoord.setX(texture.x());
				meshTextureCoord.setY(texture.y());
			}

			vertexList[j] = new Vertex(meshVertex, meshNormal, meshTextureCoord);
		}

		int faceCount = aiMesh.mNumFaces();
		AIFace.Buffer indices = aiMesh.mFaces();
		List<Integer> indicesList = new ArrayList<>();
		/*
		 * int[] indicesList = new int[faceCount * 3];
		 * 
		 * for (int j = 0; j < faceCount; j++) {
		 * AIFace face = indices.get(j);
		 * indicesList[j * 3 + 0] = face.mIndices().get(0);
		 * indicesList[j * 3 + 1] = face.mIndices().get(1);
		 * indicesList[j * 3 + 2] = face.mIndices().get(2);
		 * if (face.mIndices().array().length == 4) {
		 * indicesList[j * 3 + 3] = face.mIndices().get(3);
		 * }
		 * }
		 */
		for (int j = 0; j < faceCount; j++) {
			AIFace face = indices.get(j);
			indicesList.add(face.mIndices().get(0));
			indicesList.add(face.mIndices().get(1));
			indicesList.add(face.mIndices().get(2));
			if (face.mNumIndices() == 4) {
				indicesList.add(face.mIndices().get(0));
				indicesList.add(face.mIndices().get(1));
				indicesList.add(face.mIndices().get(3));
				indicesList.add(face.mIndices().get(1));
				indicesList.add(face.mIndices().get(2));
				indicesList.add(face.mIndices().get(3));
			}
		}
		int[] indicesArray = new int[indicesList.size()];
		for (int j = 0; j < indicesList.size(); j++) {
			indicesArray[j] = indicesList.get(j);
		}

		Material material = new Material(texturePath, shouldFlip); // Update the path accordingly
		/*
		 * if (materialIndex >= 0 && materialIndex < materials.size()) {
		 * material = materials.get(materialIndex);
		 * }
		 */
		return new Mesh(vertexList, indicesArray, material);
	}

	/*
	 * private static Material processMaterial(AIMaterial aiMaterial, String
	 * texturesDir) {
	 * 
	 * // diffuse Texture
	 * AIString diffPath = AIString.calloc();
	 * Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0,
	 * diffPath, (IntBuffer) null, null, null,
	 * null, null, null);
	 * String diffTexPath = diffPath.dataString();
	 * 
	 * GLTexture diffuseTexture = null;
	 * if (diffTexPath != null && diffTexPath.length() > 0) {
	 * diffuseTexture = new Texture(texturesDir + "/" + diffTexPath,
	 * SamplerFilter.Trilinear);
	 * }
	 * 
	 * // normal Texture
	 * AIString normalPath = AIString.calloc();
	 * Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_NORMALS, 0,
	 * normalPath, (IntBuffer) null, null,
	 * null, null, null, null);
	 * String normalTexPath = normalPath.dataString();
	 * 
	 * GLTexture normalTexture = null;
	 * if (normalTexPath != null && normalTexPath.length() > 0) {
	 * normalTexture = new TextureImage2D(texturesDir + "/" + normalTexPath,
	 * SamplerFilter.Trilinear);
	 * }
	 * 
	 * AIColor4D color = AIColor4D.create();
	 * 
	 * Vec3f diffuseColor = null;
	 * int result = Assimp.aiGetMaterialColor(aiMaterial,
	 * Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0,
	 * color);
	 * if (result == 0) {
	 * diffuseColor = new Vec3f(color.r(), color.g(), color.b());
	 * }
	 * 
	 * Material material = new Material();
	 * material.setDiffusemap(diffuseTexture);
	 * material.setNormalmap(normalTexture);
	 * material.setColor(diffuseColor);
	 * 
	 * return material;
	 * }
	 */
}
