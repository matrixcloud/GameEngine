package game;

import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TextureModel;
import render.DisplayManager;
import render.Loader;
import render.Renderer;
import shaders.ShaderProgram;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
//		float[] vertices = {
//			-0.5f, 0.5f, 0f,
//			-0.5f, -0.5f, 0f,
//			0.5f, -0.5f, 0f,
//			0.5f, -0.5f, 0f,
//			0.5f, 0.5f, 0f,
//			-0.5f, 0.5f, 0f
//		};
		float[] vertices = {
			-0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f
		};
		int[] indices = {
			0, 1, 3,
			3, 1, 2
		};
		float[] textureCoords ={
			0,0,
			0,1,
			1,1,
			1,0
		};
		
		
		RawModel model = loader.load2VAO(vertices, indices, textureCoords);
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		TextureModel textureModel = new TextureModel(model, texture);
		ShaderProgram program = new StaticShader();
		
		while(!Display.isCloseRequested()){
			renderer.prepare();
			program.start();
			renderer.render(textureModel);
			program.stop();
			DisplayManager.updateDisplay();
		}
		
		program.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}
}
