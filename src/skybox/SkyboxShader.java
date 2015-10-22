package skybox;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import shaders.ShaderProgram;
import utils.Maths;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/skybox/skybox.vert";
	private static final String FRAGMENT_FILE = "src/skybox/skybox.frag";

	private int location_projectionMatrix;
	private int location_viewMatrix;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void setProjectionMatrix(Matrix4f matrix) {
		super.setMatrix4(location_projectionMatrix, matrix);
	}

	public void setViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix4(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		super.setMatrix4(location_viewMatrix, matrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position");
	}

	@Override
	protected void getAllUiformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

}
