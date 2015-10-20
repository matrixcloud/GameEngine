package render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TextureModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

public class MasterRenderer {
	private static final float FOV = 70.0f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 100.0f;
	//the sky color
	private static final float RED = 0.5f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.5f;
	private Matrix4f projectionMat4;
	private StaticShader shader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	private EntityRenderer rendererer;
	private Map<TextureModel,List<Entity>> entityMap = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
	
	
	public MasterRenderer(){
		enableCulling();
		createProjectionMat4();
		rendererer = new EntityRenderer(shader, projectionMat4);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMat4);
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(Light light, Camera camera){
		this.prepare();
		shader.start();
		shader.setLight(light);
		shader.setViewMat4(camera);
		shader.setSkyColor(RED, GREEN, BLUE);
		rendererer.render(entityMap);
		shader.stop();
		
		terrainShader.start();
		terrainShader.setLight(light);
		terrainShader.setViewMat4(camera);
		terrainShader.setSkyColor(RED, GREEN, BLUE);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		entityMap.clear();
		terrains.clear();
	}
	
	public void processEntity(Entity entity){
		TextureModel model = entity.getTextureModel();
		List<Entity> batch = entityMap.get(model);
		if(batch != null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entityMap.put(model, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	private void createProjectionMat4(){
		float aspectRatio = (float)Display.getWidth() / (float)Display.getHeight();
		float yScale = (float) ((1f / Math.toRadians(FOV/2)) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		projectionMat4 = new Matrix4f();
		projectionMat4.m00 = xScale;
		projectionMat4.m11 = yScale;
		projectionMat4.m22 = -((FAR_PLANE - NEAR_PLANE) / frustumLength);
		projectionMat4.m23 = -1;
		projectionMat4.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projectionMat4.m33 = 0;
	}
	
	public void cleanup(){
		shader.cleanup();
		terrainShader.cleanup();
	}
}
