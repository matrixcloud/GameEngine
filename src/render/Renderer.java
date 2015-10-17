package render;

import org.lwjgl.opengl.Display;
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

public class Renderer {
	private static final float FOV = 70.0f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 100.0f;
	private Matrix4f projectionMat4;
	
	public Renderer(){
		createProjectionMat4();
	}
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0, 0, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Entity entity, StaticShader shader){
		TextureModel textureModel = entity.getTextureModel();
		RawModel model = textureModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		Matrix4f transformationMat4 = Maths.createTransformMatrix4(
				entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(),
				entity.getScale());
		shader.setTransformMat4(transformationMat4);
		shader.setProjectionMat4(projectionMat4);
		ModelTexture texture = textureModel.getTexture();
		shader.setShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureModel.getTexture().getTextureID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
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
}
