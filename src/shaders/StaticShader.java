package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import utils.Maths;

public class StaticShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/shaders/basic.vert";
	private static final String FRAGMENT_FILE = "src/shaders/basic.frag";
	private int transformMat4Loc;
	private int projectionMat4Loc;
	private int viewMat4Loc;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position_modelspace");
		super.bindAttributes(1, "textureCoords");
	}

	@Override
	protected void getAllUiformLocations() {
		transformMat4Loc = super.getUniformLocation("transformMat4");
		projectionMat4Loc = super.getUniformLocation("projectionMat4");
		viewMat4Loc = super.getUniformLocation("viewMat4");
	}

	public void setTransformMat4(Matrix4f mat4){
		super.setMatrix4(transformMat4Loc, mat4);
	}
	
	public void setProjectionMat4(Matrix4f mat4){
		super.setMatrix4(projectionMat4Loc, mat4);;
	}
	
	public void setViewMat4(Camera camera){
		Matrix4f viewMat4 = Maths.createViewMatrix4(camera);
		super.setMatrix4(viewMat4Loc, viewMat4);
	}
}
