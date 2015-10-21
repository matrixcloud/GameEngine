package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}
	public Vector3f getPosition() {
		return position;
	}
	public Vector3f getColor() {
		return color;
	}
	public Vector3f getAttenuation() {
		return attenuation;
	}
}
