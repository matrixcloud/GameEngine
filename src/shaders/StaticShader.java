package shaders;

import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/shaders/basic.vert";
	private static final String FRAGMENT_FILE = "src/shaders/basic.frag";
	private int transformMat4Loc;
	
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
	}

	public void setTransformMat4(Matrix4f mat4){
		super.setMatrix4(transformMat4Loc, mat4);
	}
}
