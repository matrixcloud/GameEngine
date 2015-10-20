package render;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	private static long lastTime;
	private static float deltaTime; 
	
	
	public static void createDisplay(){
		ContextAttribs attribs = new ContextAttribs(3, 2);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Game Engine");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastTime = getCurrTime();
	}
	
	public static void updateDisplay(){
		Display.sync(FPS_CAP);
		Display.update();
		long currTime = getCurrTime();
		deltaTime = (currTime - lastTime)/1000f;
		lastTime = currTime;
	}
	
	public static void closeDisplay(){
		Display.destroy();
	}
	
	//秒级
	private static long getCurrTime(){
		return Sys.getTime()*1000 / Sys.getTimerResolution();
	}
	
	//毫秒级
	public static float getDeltaTime(){
		return deltaTime;
	}
}
