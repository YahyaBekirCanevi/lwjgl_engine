package com.canevi.engine.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.canevi.engine.enums.TextureFilterType;
import com.canevi.engine.enums.TextureWrapType;
import com.canevi.engine.graphics.Material;
import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Texture;
import com.canevi.engine.graphics.Vertex;
import com.canevi.engine.maths.Matrix4f;
import com.canevi.engine.maths.Vector2f;
import com.canevi.engine.maths.Vector3f;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelLoader {

	public static List<Mesh> loadModel(String filePath, List<String> textures, boolean shouldFlip) {
		String absolutePath = ResourceLoader.load(filePath);

		// aiImportFile fails on Windows if the path starts with a slash
		if (System.getProperty("os.name").contains("Windows")) {
			if (absolutePath.startsWith("/"))
				absolutePath = absolutePath.substring(1);
		}
		AIScene scene = Assimp.aiImportFile(absolutePath,
				Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_JoinIdenticalVertices
						| Assimp.aiProcess_OptimizeMeshes | Assimp.aiProcess_Triangulate);
		// Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate
		// Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_JoinIdenticalVertices

		if (scene == null) {
			log.info("Couldn't load model at " + filePath + "\nError: " + Assimp.aiGetErrorString());
			return Collections.emptyList();
		}

		List<Mesh> meshes = new ArrayList<>();
		List<Material> materials = new ArrayList<>();
		AINode rootNode = scene.mRootNode();

		/*
		 * if (scene.mMaterials() != null) {
		 * for (int i = 0; i < scene.mNumMaterials(); i++) {
		 * AIMaterial aiMaterial = AIMaterial.create(scene.mMaterials().get(i));
		 * Material material = processMaterial(aiMaterial, filePath);
		 * materials.add(material);
		 * }
		 * }
		 */
		// for (int i = 0; i < scene.mNumMeshes(); i++) {
		// AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(i));
		// meshes.add(processMesh(aiMesh, textures.get(i), materials, shouldFlip));
		// }
		processNode(rootNode, scene, meshes, materials, textures, shouldFlip, Matrix4f.identity());

		return meshes;
	}

	private static void processNode(AINode node, AIScene scene, List<Mesh> meshes, List<Material> materials,
			List<String> textures, boolean shouldFlip, Matrix4f parentTransform) {
		Matrix4f currentTransform = parentTransform.multiply(Matrix4f.fromAssimpMatrix4x4(node.mTransformation()));

		// Process each mesh in the current node
		for (int i = 0; i < node.mNumMeshes(); i++) {
			int meshIndex = node.mMeshes().get(i);
			AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(meshIndex));
			meshes.add(processMesh(aiMesh, textures.get(meshIndex), materials, shouldFlip, currentTransform));
		}

		// Recursively process child nodes
		for (int i = 0; i < node.mNumChildren(); i++) {
			AINode childNode = AINode.create(node.mChildren().get(i));
			processNode(childNode, scene, meshes, materials, textures, shouldFlip, currentTransform);
		}
	}

	private static Mesh processMesh(AIMesh aiMesh, String texturePath, List<Material> materials,
			boolean shouldFlip, Matrix4f transformMatrix) {
		int vertexCount = aiMesh.mNumVertices();
		// int materialIndex = aiMesh.mMaterialIndex();

		AIVector3D.Buffer vertices = aiMesh.mVertices();
		AIVector3D.Buffer normals = aiMesh.mNormals();

		Vertex[] vertexList = new Vertex[vertexCount];

		for (int j = 0; j < vertexCount; j++) {
			AIVector3D vertex = vertices.get(j);
			Vector3f meshVertex = new Vector3f(vertex.x(), vertex.y(), vertex.z());
			meshVertex = transformMatrix.translateVector3f(meshVertex);

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

		for (int j = 0; j < faceCount; j++) {
			AIFace face = indices.get(j);

			indicesList.add(face.mIndices().get(0));
			indicesList.add(face.mIndices().get(1));
			indicesList.add(face.mIndices().get(2));
			if (face.mNumIndices() == 4) {
				indicesList.add(face.mIndices().get(0));
				indicesList.add(face.mIndices().get(2));
				indicesList.add(face.mIndices().get(3));
			}
		}
		int[] indicesArray = new int[indicesList.size()];
		for (int j = 0; j < indicesList.size(); j++) {
			indicesArray[j] = indicesList.get(j);
		}
		Texture diffuseTexture = TextureLoader.getTexture(texturePath, shouldFlip,
				TextureWrapType.REPEAT, TextureFilterType.LINEAR);
		Material material = new Material(diffuseTexture, null, null);
		// Update the path accordingly
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
