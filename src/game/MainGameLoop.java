package game;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import render.DisplayManager;
import render.Loader;
import render.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		Camera camera = new Camera(new Vector3f(44, 11, -0.42f));
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		ModelTexture texture = new ModelTexture(loader.loadTexture("grass"));
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		
		
		Terrain terrain1 = new Terrain(0, -1, loader, texture);
		Terrain terrain2 = new Terrain(1, -1, loader, texture);
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			camera.move();
			renderer.processTerrain(terrain1);
			renderer.processTerrain(terrain2);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
