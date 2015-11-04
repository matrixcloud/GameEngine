package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class ThirdPersonCamera extends Camera{
	private Player player;
	private float distanceFromPlayer;
	private float angleAroundPlayer;
	
	public ThirdPersonCamera(Player player){
		this.player = player;
	}
	
	public void move(){
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		calculateCameraPosition();
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	public void calculateCameraPosition(){
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		float theta = angleAroundPlayer + player.getRotY();
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		Vector3f playerPos = player.getPosition();
		position.x = playerPos.x - offsetX;
		position.y = playerPos.y + verticalDistance;
		position.z = playerPos.z - offsetZ;
	}
	
	public float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	public float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	public void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	public void calculatePitch(){
		if(Mouse.isButtonDown(1)){//the right mouse button
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	public void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){//the left mouse button
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
