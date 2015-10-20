package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
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
		Terrain terrain = new Terrain(0, -1, loader, texPack, blendTex, "heightMap");
		
		//*****************Load entity textures**************//
		ModelTexture playerTex = new ModelTexture(loader.loadTexture("playerTexture"));
		ModelTexture treeTex = new ModelTexture(loader.loadTexture("tree"));
		ModelTexture grassTex = new ModelTexture(loader.loadTexture("grassTexture"));
		ModelTexture fernTexAtlases = new ModelTexture(loader.loadTexture("fern"));
		grassTex.setTransparent(true);
		grassTex.setUseFakeLighting(true);
		fernTexAtlases.setTransparent(true);
		fernTexAtlases.setNumberOfRows(2);
		
		//*******************Load Entity Model*************//
		List<Entity> entites = new ArrayList<>();
		TextureModel treeModel = new TextureModel(OBJLoader.load("tree", loader), treeTex);
		TextureModel grassModel = new TextureModel(OBJLoader.load("grassModel", loader), grassTex);
		TextureModel fernModel = new TextureModel(OBJLoader.load("fern", loader), fernTexAtlases);
		TextureModel playerModel = new TextureModel(OBJLoader.load("person", loader), playerTex);
		Player player = new Player(playerModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
		Camera camera = new Camera(player);
		entites.add(player);
		//*****************Generate Entity***************//
		Random rd = new Random();
		for(int i = 0; i < 500; i++){
			//add tree
			float x = rd.nextFloat() * 800;
			float z = rd.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x, z);
			Entity tree = new Entity(treeModel, new Vector3f(x, y, z), 0, 0, 0, rd.nextFloat() * 10 + 0.1f);
			entites.add(tree);
			//add grass
			x = rd.nextFloat() * 800;
			z = rd.nextFloat() * -600;
			y = terrain.getHeightOfTerrain(x, z);
			Entity grass = new Entity(grassModel, new Vector3f(x, y, z), 0, 0, 0, 1);
			entites.add(grass);
			//add fern
			x = rd.nextFloat() * 800;
			z = rd.nextFloat() * -600;
			y = terrain.getHeightOfTerrain(x, z);
			Entity fern = new Entity(fernModel, rd.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.6f);
			entites.add(fern);
		}
		//****************Load GUI textures****************//
		List<GuiTexture> guis = new ArrayList<>();
		GuiTexture logoTex = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		GuiTexture thinmatTex = new GuiTexture(loader.loadTexture("thinmatrix"),new Vector2f(0.3f, 0.4f), new Vector2f(0.25f, 0.25f));
		guis.add(logoTex);
		guis.add(thinmatTex);
		
		MasterRenderer renderer = new MasterRenderer();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		while(!Display.isCloseRequested()){
			camera.move();
			player.move(terrain);
			renderer.processTerrain(terrain);
			for (Entity entity : entites) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
