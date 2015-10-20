package render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import shaders.StaticShader;
import textures.ModelTexture;
import utils.Maths;

public class EntityRenderer {
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMat4){
		this.shader = shader;
		shader.start();
		shader.setProjectionMat4(projectionMat4);
		shader.stop();
	}
	
	public void render(Map<TextureModel, List<Entity>> entityMap){
		for(TextureModel model : entityMap.keySet()){
			prepareTextureModel(model);
			List<Entity> batch = entityMap.get(model);
			for(Entity e : batch){
				prepareInstance(e);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTextureModel();
		}
	}
	
	private void prepareTextureModel(TextureModel textureModel){
		RawModel model = textureModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = textureModel.getTexture();
		shader.setShineVariables(texture.getShineDamper(), texture.getReflectivity());
		shader.setNumberOfRows(texture.getNumberOfRows());
		if(texture.isTransparent()){
			MasterRenderer.disableCulling();
		}
		shader.setUseFakeLighting(texture.isUseFakeLighting());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}
	
	private void unbindTextureModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMat4 = Maths.createTransformMatrix4(
				entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(),
				entity.getScale());
		shader.setTransformMat4(transformationMat4);
		shader.setOffset(entity.getTextureXOffset(), entity.getTexureYOffset());
	}
}
