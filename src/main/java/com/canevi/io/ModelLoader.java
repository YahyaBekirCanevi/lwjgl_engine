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
	public static Mesh loadModel(String filePath, String texturePath, int meshIndex) {
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
		Material material = new Material(texturePath);

		return new Mesh(vertexList, indicesList, material);
	}

	public static List<Mesh> loadModel(String filePath, List<String> textures) {
		String absolutePath = ResourceLoader.load(filePath);

		// aiImportFile fails on Windows if the path starts with a slash
		if (System.getProperty("os.name").contains("Windows")) {
			if (absolutePath.startsWith("/"))
				absolutePath = absolutePath.substring(1);
		}
		AIScene scene = Assimp.aiImportFile(absolutePath,
				Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_JoinIdenticalVertices);
		//Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate
		//Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_JoinIdenticalVertices

		if (scene == null) {
			log.info("Couldn't load model at " + filePath + "\nError: " + Assimp.aiGetErrorString());
			return Collections.emptyList();
		}

		List<Mesh> meshes = new ArrayList<>();
		PointerBuffer p = scene.mMeshes();
		int meshAmount = scene.mNumMeshes();
		log.info("Mesh Amount : " + meshAmount);

		for (int i = 0; i < meshAmount; i++) {
			long address = p.get(i);

			meshes.add(loadModelNew(address, textures.get(i)));
		}

		return meshes;
	}

	private static Mesh loadModelNew(long address, String texturePath) {
		AIMesh mesh = AIMesh.create(address);
		int vertexCount = mesh.mNumVertices();

		AIVector3D.Buffer vertices = mesh.mVertices();
		AIVector3D.Buffer normals = mesh.mNormals();

		Vertex[] vertexList = new Vertex[vertexCount];

		for (int j = 0; j < vertexCount; j++) {
			AIVector3D vertex = vertices.get(j);
			Vector3f meshVertex = new Vector3f(vertex.x(), vertex.y(), vertex.z());

			AIVector3D normal = normals.get(j);
			Vector3f meshNormal = new Vector3f(normal.x(), normal.y(), normal.z());

			Vector2f meshTextureCoord = new Vector2f(0, 0);
			if (mesh.mNumUVComponents().get(0) != 0) {
				AIVector3D.Buffer textureCoords = mesh.mTextureCoords(0);
				AIVector3D texture = textureCoords.get(j);
				meshTextureCoord.setX(texture.x());
				meshTextureCoord.setY(texture.y());
			}

			vertexList[j] = new Vertex(meshVertex, meshNormal, meshTextureCoord);
		}

		int faceCount = mesh.mNumFaces();
		AIFace.Buffer indices = mesh.mFaces();
		int[] indicesList = new int[faceCount * 3];

		for (int j = 0; j < faceCount; j++) {
			AIFace face = indices.get(j);
			indicesList[j * 3 + 0] = face.mIndices().get(0);
			indicesList[j * 3 + 1] = face.mIndices().get(1);
			indicesList[j * 3 + 2] = face.mIndices().get(2);
		}

		Material material = new Material(texturePath); // Update the path accordingly
		return new Mesh(vertexList, indicesList, material);
	}
}
