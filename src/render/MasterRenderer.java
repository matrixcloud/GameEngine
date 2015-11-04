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
import skybox.SkyboxRenderer;
import terrains.Terrain;

public class MasterRenderer {
	private static final float FOVY = 70.0f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 100.0f;
	//the sky color
	private static final float RED = 0.5f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.5f;
	private Matrix4f projectionMat4;
	private StaticShader entityShader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	private EntityRenderer entityRendererer;
	private SkyboxRenderer skyboxRenderer;
	private Map<TextureModel,List<Entity>> entityMap = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
	
	
	public MasterRenderer(Loader loader){
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		enableCulling();
		createProjectionMat4();
		entityRendererer = new EntityRenderer(entityShader, projectionMat4);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMat4);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMat4);
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(List<Light> lights, Camera camera){
		this.prepare();
		entityShader.start();
		entityShader.setLights(lights);
		entityShader.setViewMat4(camera);
		entityShader.setSkyColor(RED, GREEN, BLUE);
		entityRendererer.render(entityMap);
		entityShader.stop();
		
		terrainShader.start();
		terrainShader.setLights(lights);
		terrainShader.setViewMat4(camera);
		terrainShader.setSkyColor(RED, GREEN, BLUE);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		
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
		float tanValue = (float) Math.tan(FOVY/2);
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		projectionMat4 = new Matrix4f();
		projectionMat4.m00 = 1f/tanValue;
		projectionMat4.m11 = aspectRatio/(tanValue);
		projectionMat4.m22 = -((FAR_PLANE - NEAR_PLANE) / frustumLength);
		projectionMat4.m23 = -1;
		projectionMat4.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projectionMat4.m33 = 0;
	}
	
	public void cleanup(){
		entityShader.cleanup();
		terrainShader.cleanup();
	}

	public Matrix4f getProjectionMat4() {
		return projectionMat4;
	}
}
