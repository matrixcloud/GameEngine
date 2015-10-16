package game;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TextureModel;
import render.DisplayManager;
import render.Loader;
import render.Renderer;
import shaders.ShaderProgram;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
//		float[] vertices = {
//			-0.5f, 0.5f, 0f,
//			-0.5f, -0.5f, 0f,
//			0.5f, -0.5f, 0f,
//			0.5f, -0.5f, 0f,
//			0.5f, 0.5f, 0f,
//			-0.5f, 0.5f, 0f
//		};
		float[] vertices = {
			-0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f
		};
		int[] indices = {
			0, 1, 3,
			3, 1, 2
		};
		float[] textureCoords ={
			0,0,
			0,1,
			1,1,
			1,0
		};
		
		
		RawModel model = loader.load2VAO(vertices, indices, textureCoords);
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		TextureModel textureModel = new TextureModel(model, texture);
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer();
		Entity entity = new Entity(textureModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()){
//			entity.increasePosition(0, 0, -0.01f);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.setViewMat4(camera);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}