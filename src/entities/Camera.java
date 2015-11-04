package entities;

import org.lwjgl.util.vector.Vector3f;

public abstract class Camera {
	protected Vector3f position = new Vector3f(0,0,0);
	protected float pitch;
	protected float yaw;
	protected float roll;

	public abstract void move();
	
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
