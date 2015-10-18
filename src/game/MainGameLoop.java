package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TextureModel;
import render.DisplayManager;
import render.Loader;
import render.MasterRenderer;
import render.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		Camera camera = new Camera(new Vector3f(0, 10, -1));
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		ModelTexture grassTex = new ModelTexture(loader.loadTexture("grass"));
		ModelTexture treeTex = new ModelTexture(loader.loadTexture("tree"));
		grassTex.setShineDamper(10);
		grassTex.setReflectivity(0.1f);
		treeTex.setReflectivity(10);
		treeTex.setReflectivity(0.1f);
		
		//*******************Entiy*************//
		TextureModel treeModel = new TextureModel(OBJLoader.load("tree", loader), treeTex);
		List<Entity> trees = new ArrayList<>();
		Random rd = new Random();
		for(int i = 0; i < 500; i++){
			float x = rd.nextFloat() * 800 - 400;
			float z = rd.nextFloat() * -600;
			
			Entity e = new Entity(treeModel, new Vector3f(x, 0, z), 0, 0, 0, rd.nextFloat() * 10);
			trees.add(e);
		}
		
		Terrain terrain1 = new Terrain(0, -1, loader, grassTex);
		Terrain terrain2 = new Terrain(-1, -1, loader, grassTex);
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			camera.move();
			renderer.processTerrain(terrain1);
			renderer.processTerrain(terrain2);
			for (Entity tree : trees) {
				renderer.processEntity(tree);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
