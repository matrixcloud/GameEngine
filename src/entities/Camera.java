package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0, 0, 5);
	private float pitch;
	private float yaw;
	private float roll;
	private float speed = 0.02f;
	
	public Camera(){}

	public Camera(Vector3f position){
		this.position = position;
	}
	
	public void move(){
//		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
//			position.z -= speed;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
//			position.z += speed;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
//			position.x -= speed;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
//			position.x += speed;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
//			position.y += speed;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
//			position.y -= speed;
//		}
//		
//		if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
//			speed -= 0.02f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_C)){
//			speed += 0.02f;
//		}
//		System.out.println("camera position----->>" + position);
	} 
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
