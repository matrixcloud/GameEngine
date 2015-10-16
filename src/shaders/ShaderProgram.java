package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	private static FloatBuffer mat4Buffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertFile, String fragFile){
		vertexShaderID = loadShader(vertFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUiformLocations();
	}
	
	public void start(){
		GL20.glUseProgram(programID);
	}

	public void stop(){
		GL20.glUseProgram(0);
	}
	
	public void cleanup(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	protected abstract void getAllUiformLocations();
	
	protected void bindAttributes(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected void setFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	protected void setVector(int location, Vector3f v3){
		GL20.glUniform3f(location, v3.x, v3.y, v3.z);
	}
	
	protected void setBoolean(int location, boolean value){
		float v = 0;
		if(value){
			v  = 1;
		}
		GL20.glUniform1f(location, v);
	}
	
	protected void setMatrix4(int location, Matrix4f mat4){
		mat4.store(mat4Buffer);
		mat4Buffer.flip();
		GL20.glUniformMatrix4(location, false, mat4Buffer);
	}
	
	private static int loadShader(String shaderFile, int type){
		System.out.println("Loading " + shaderFile);
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(shaderFile));
			String line;
			while(null != (line = reader.readLine())){
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could't read " + shaderFile);
			e.printStackTrace();
			System.exit(-1);
		} 
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could't compile " + shaderFile);
			System.exit(-1);
		}
		return shaderID;
	}
}
