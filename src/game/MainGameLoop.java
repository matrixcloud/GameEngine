package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		
		RawModel model = OBJLoader.load("cube", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("grass"));
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		TextureModel textureModel = new TextureModel(model, texture);
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer();
		
		List<Entity> cubes = new ArrayList<>();
		Random rd = new Random();
		for(int i = 0; i < 200; i++){
			float x = rd.nextFloat() * 100 - 50;
			float y = rd.nextFloat() * 100 - 50;
			float z = rd.nextFloat() * - 300;
			cubes.add(new Entity(textureModel, new Vector3f(x, y, z), rd.nextFloat() * 180, rd.nextFloat() * 180, 0, 1));
		}
		
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(0, 0, -25), new Vector3f(1, 1, 1));
		
		while(!Display.isCloseRequested()){
//			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();
			shader.start();
			shader.setViewMat4(camera);
			shader.setLight(light);
			for(Entity cube : cubes){
				renderer.render(cube, shader);
			}
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
