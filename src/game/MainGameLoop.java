package game;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TextureModel;
import render.DisplayManager;
import render.Loader;
import render.OBJLoader;
import render.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		RawModel model = OBJLoader.load("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("yellow"));
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		TextureModel textureModel = new TextureModel(model, texture);
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer();
		Entity entity = new Entity(textureModel, new Vector3f(0, 0, -30), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(0, 0, -25), new Vector3f(1, 1, 1));
		entity.increaseRotation(20, 180, 0);
		
		while(!Display.isCloseRequested()){
//			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.setViewMat4(camera);
			shader.setLight(light);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
