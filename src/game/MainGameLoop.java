package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TextureModel;
import render.DisplayManager;
import render.Loader;
import render.MasterRenderer;
import render.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		//*******************Load terrain textures*************//
		TerrainTexture backgroundTex = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTex = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTex = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTex = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendTex = new TerrainTexture(loader.loadTexture("blendMap"));
		TerrainTexturePack texPack = new TerrainTexturePack(backgroundTex, rTex, gTex, bTex);
		
		//*****************Load entity textures**************//
		ModelTexture playerTex = new ModelTexture(loader.loadTexture("playerTexture"));
		ModelTexture treeTex = new ModelTexture(loader.loadTexture("tree"));
		ModelTexture grassTex = new ModelTexture(loader.loadTexture("grassTexture"));
		ModelTexture fernTex = new ModelTexture(loader.loadTexture("fern"));
		grassTex.setTransparent(true);
		grassTex.setUseFakeLighting(true);
		fernTex.setTransparent(true);
		
		//*******************Load Entity Model*************//
		List<Entity> entites = new ArrayList<>();
		TextureModel treeModel = new TextureModel(OBJLoader.load("tree", loader), treeTex);
		TextureModel grassModel = new TextureModel(OBJLoader.load("grassModel", loader), grassTex);
		TextureModel fernModel = new TextureModel(OBJLoader.load("fern", loader), fernTex);
		TextureModel playerModel = new TextureModel(OBJLoader.load("person", loader), playerTex);
		Player player = new Player(playerModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
		Camera camera = new Camera(player);
		entites.add(player);
		
		Random rd = new Random();
		for(int i = 0; i < 500; i++){
			//add tree
			float x = rd.nextFloat() * 800 - 400;
			float z = rd.nextFloat() * -600;
			Entity tree = new Entity(treeModel, new Vector3f(x, 0, z), 0, 0, 0, rd.nextFloat() * 10 + 0.1f);
			entites.add(tree);
			//add grass
			x = rd.nextFloat() * 800 - 400;
			z = rd.nextFloat() * -600;
			Entity grass = new Entity(grassModel, new Vector3f(x, 0, z), 0, 0, 0, 1);
			entites.add(grass);
			//add fern
			x = rd.nextFloat() * 800 - 400;
			z = rd.nextFloat() * -600;
			Entity fern = new Entity(fernModel, new Vector3f(x, 0, z), 0, 0, 0, 0.6f);
			entites.add(fern);
		}

		Terrain terrain1 = new Terrain(0, -1, loader, texPack, blendTex, "heightMap");
		Terrain terrain2 = new Terrain(-1, -1, loader, texPack, blendTex, "heightMap");
		MasterRenderer renderer = new MasterRenderer();
		
		
		while(!Display.isCloseRequested()){
			camera.move();
			player.move();
			renderer.processTerrain(terrain1);
			renderer.processTerrain(terrain2);
			for (Entity entity : entites) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
