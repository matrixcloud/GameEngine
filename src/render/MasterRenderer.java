package render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TextureModel;
import shaders.StaticShader;

public class MasterRenderer {
	private StaticShader shader = new StaticShader();
	private Renderer rendererer = new Renderer(shader);
	
	private Map<TextureModel,List<Entity>> entityMap = new HashMap<>();

	public void render(Light light, Camera camera){
		rendererer.prepare();
		shader.start();
		shader.setLight(light);
		shader.setViewMat4(camera);
		rendererer.render(entityMap);
		shader.stop();
		entityMap.clear();
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
	
	public void cleanup(){
		shader.cleanup();
	}
}
