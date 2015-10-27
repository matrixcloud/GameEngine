package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TextureModel;
import render.DisplayManager;
import terrains.Terrain;

public class Player extends Entity{
	private static final float RUN_SPEED = 100;
	private static final float TURN_SPEED = 160;
	private static final float JUMP_POWER = 30;
	private static final float GRAVITY = -50f;
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	private boolean inAir = false;
	
	public Player(TextureModel textureModel, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(textureModel, position, rotX, rotY, rotZ, scale);
	}

	public void move(Terrain terrain){
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDeltaTime(), 0);
		float distance = currentSpeed * DisplayManager.getDeltaTime();
		float angleInRadians = (float) Math.toRadians(super.getRotY());
		float dx = (float) (distance * Math.sin(angleInRadians));
		float dz = (float) (distance * Math.cos(angleInRadians));
		upwardsSpeed += GRAVITY * DisplayManager.getDeltaTime();
		super.increasePosition(dx, upwardsSpeed * DisplayManager.getDeltaTime(), dz);
		Vector3f position = this.getPosition();
		float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z);
		if(position.y < terrainHeight){
			inAir = false;
			upwardsSpeed = 0;
			position.y = terrainHeight;
		}
	}
	
	private void jump(){
		if(!inAir){
			this.upwardsSpeed = JUMP_POWER;
			inAir = true;
		}
	}
	
	private void checkInputs(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = RUN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = -RUN_SPEED;
		}else{
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.currentTurnSpeed = TURN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.currentTurnSpeed = -TURN_SPEED;
		}else{
			this.currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			jump();
		}
	}
}
