package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import render.DisplayManager;
import shaders.ShaderProgram;
import utils.Maths;

public class SkyboxShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/skybox/skybox.vert";
	private static final String FRAGMENT_FILE = "src/skybox/skybox.frag";
	private static final float ROTATION_SPEED = 1f;
	private float rotation = 0f;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_blendFactor;
	private int location_cubeMap1;
	private int location_cubeMap2;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void connectTextureUnits(){
		super.setInt(location_cubeMap1, 0);
		super.setInt(location_cubeMap2, 1);
	}
	
	public void setProjectionMatrix(Matrix4f matrix) {
		super.setMatrix4(location_projectionMatrix, matrix);
	}

	public void setViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix4(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += ROTATION_SPEED * DisplayManager.getDeltaTime();
		if(rotation >= 180)	rotation = 0;
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		super.setMatrix4(location_viewMatrix, matrix);
	}

	public void setFogColor(float r, float g, float b){
		super.setVector(location_fogColor, new Vector3f(r, g, b));
	}

	public void setBlendFactor(float bendFactor){
		super.setFloat(location_blendFactor, bendFactor);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position");
	}

	@Override
	protected void getAllUiformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_cubeMap1 = super.getUniformLocation("cubeMap1");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
	}

}
