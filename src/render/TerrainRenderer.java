package render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.TerrainTexturePack;
import utils.Maths;

public class TerrainRenderer {
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMat4){
		this.shader = shader;
		shader.start();
		shader.setProjectionMat4(projectionMat4);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(List<Terrain> terrains){
		for(Terrain terrain : terrains){
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTextureModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain){
		RawModel model = terrain.getModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.setShineVariables(1, 0);
	}
	
	private void bindTextures(Terrain terrain){
		TerrainTexturePack texPack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texPack.getBackgroundTex().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texPack.getrTex().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texPack.getgTex().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texPack.getbTex().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendTex().getTextureID());
	}
	
	private void unbindTextureModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain){
		Matrix4f transformationMat4 = Maths.createTransformMatrix4(new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				0, 0, 0, 1);
		shader.setTransformMat4(transformationMat4);
	}
}
