package com.canevi.objects;

import java.util.List;

import com.canevi.graphics.Mesh;
import com.canevi.graphics.Renderer;
import com.canevi.graphics.Shader;
import com.canevi.io.Window;
import com.canevi.maths.Transform;

import lombok.Getter;

@Getter
public class GameObject {
	private Transform transform;
	private List<Mesh> meshList;
	public Renderer renderer;
	
	public GameObject(Transform transform, List<Mesh> meshList, Window window, Shader shader) {
		this.transform = transform;
		this.meshList = meshList;
		
		renderer = new Renderer(window, shader);
	}
	
	public void update() {
		transform.update();
	}

    public void create() {
		if (meshList == null || meshList.isEmpty()) return;
		meshList.forEach(mesh -> mesh.create());
    }

    public void destroy() {
		if (meshList == null || meshList.isEmpty()) return;
		meshList.forEach(mesh -> mesh.destroy());
    }

	public void renderObject(Camera camera) {
		if (meshList == null || meshList.isEmpty()) return;
		meshList.forEach(mesh -> renderer.renderMesh(mesh, transform, camera));
	}

}
