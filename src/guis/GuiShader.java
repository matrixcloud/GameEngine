package guis;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/shaders/gui.vert";
	private static final String FRAGMENT_FILE = "src/shaders/gui.frag";
	private int transformMat4Loc;
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position");
	}

	@Override
	protected void getAllUiformLocations() {
		transformMat4Loc = this.getUniformLocation("transformMat4");
	}
	
	public void setTransformMat4(Matrix4f mat4){
		this.setMatrix4(transformMat4Loc, mat4);
	}
}
