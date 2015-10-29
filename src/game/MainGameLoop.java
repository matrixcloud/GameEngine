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
import utils.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
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
		ModelTexture rocksTex = new ModelTexture(loader.loadTexture("rocks"));
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
		TextureModel rocksModel = new TextureModel(OBJLoader.load("rocks", loader), rocksTex);
		Player player = new Player(playerModel, new Vector3f(185, 0, -185), 0, 0, 0, 1);
		Camera camera = new Camera(player);
		entites.add(player);
		entites.add(new Entity(rocksModel, new Vector3f(75, 4.8f, -75), 0, 0, 0, 70));
		//*****************Generate Entity***************//
		Random rd = new Random();
		for(int i = 0; i < 20; i++){
			//add tree
			float x = rd.nextFloat() * 150;
			float z = rd.nextFloat() * -150;
			float y = terrain.getHeightOfTerrain(x, z);
			if(y < 0) continue;
			Entity tree = new Entity(treeModel, new Vector3f(x, y, z), 0, 0, 0, rd.nextFloat() * 10 + 0.1f);
			entites.add(tree);
			//add grass
			x = rd.nextFloat() * 150;
			z = rd.nextFloat() * -150;
			y = terrain.getHeightOfTerrain(x, z);
			if(y < 0) continue;
			Entity grass = new Entity(grassModel, new Vector3f(x, y, z), 0, 0, 0, 1);
			entites.add(grass);
			//add fern
			x = rd.nextFloat() * 150;
			z = rd.nextFloat() * -150;
			y = terrain.getHeightOfTerrain(x, z);
			if(y < 0) continue;
			Entity fern = new Entity(fernModel, rd.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.6f);
			entites.add(fern);
		}
		//****************Load GUI textures****************//
		//***************Generate Lights********************//
		List<Light> lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
		lights.add(new Light(new Vector3f(185, 10, -200), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(300, 10, -200), new Vector3f(0, 0, 10), new Vector3f(1, 0.01f, 0.002f)));
		//**************Create Renderer********************//
		MasterRenderer renderer = new MasterRenderer(loader);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMat4());
		
		//*************Setup Water Renderer***************//
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMat4());
		List<WaterTile> waters = new ArrayList<>();
		waters.add(new WaterTile(75, -75, 0));
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		List<GuiTexture> guis = new ArrayList<>();
		GuiTexture guiTex = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		guis.add(guiTex);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		while(!Display.isCloseRequested()){
			camera.move();
			player.move(terrain);
			picker.update();
			
			fbos.bindReflectionFrameBuffer();
			renderer.processTerrain(terrain);
			for (Entity entity : entites) {
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);
			fbos.unbindCurrentFrameBuffer();
			
			renderer.processTerrain(terrain);
			for (Entity entity : entites) {
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);
			waterRenderer.render(waters, camera);
			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
		}
		
		waterShader.cleanup();
		renderer.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
