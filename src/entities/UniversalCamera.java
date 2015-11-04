package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import render.DisplayManager;

public class UniversalCamera extends Camera {
	private final static float SPEED = 10.0f;
	
	@Override
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.position.z -= SPEED * DisplayManager.getDeltaTime();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.position.z += SPEED * DisplayManager.getDeltaTime();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.position.x -= SPEED * DisplayManager.getDeltaTime();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.position.x += SPEED * DisplayManager.getDeltaTime();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			this.position.y += SPEED * DisplayManager.getDeltaTime();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			this.position.y -= SPEED * DisplayManager.getDeltaTime();
		}
		
		calculatePitch();
		calculateYaw();
	}

	public void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
	}
	
	public void calculatePitch(){
		if(Mouse.isButtonDown(1)){//the right mouse button
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	public void calculateYaw(){
		if(Mouse.isButtonDown(0)){
			float yawChange = Mouse.getDX() * 0.1f;
			yaw -= yawChange;
		}
	}
}
