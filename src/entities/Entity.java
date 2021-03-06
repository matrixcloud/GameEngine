package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TextureModel;

public class Entity {
	private TextureModel textureModel;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	private int textureIndex = 0;
	
	public Entity(TextureModel textureModel, int textureIndex, Vector3f position, 
			float rotX, float rotY, float rotZ, float scale) {
		this.textureModel = textureModel;
		this.textureIndex = textureIndex;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public Entity(TextureModel textureModel, Vector3f position, 
			float rotX, float rotY, float rotZ, float scale) {
		this.textureModel = textureModel;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public float getTextureXOffset(){
		float numberOfRows = textureModel.getTexture().getNumberOfRows();
		float column = textureIndex % numberOfRows;
		return column / numberOfRows;
	}
	
	public float getTexureYOffset(){
		float numberOfRows = textureModel.getTexture().getNumberOfRows();
		float column = textureIndex / numberOfRows;
		return column / numberOfRows;
	}
	
	public void increasePosition(float dx, float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	public TextureModel getTextureModel() {
		return textureModel;
	}

	public void setTextureModel(TextureModel textureModel) {
		this.textureModel = textureModel;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
