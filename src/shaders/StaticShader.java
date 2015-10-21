package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import utils.Maths;

public class StaticShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/shaders/basic.vert";
	private static final String FRAGMENT_FILE = "src/shaders/basic.frag";
	private static final int MAX_LIGHTS = 4;
	private int transformMat4Loc;
	private int projectionMat4Loc;
	private int viewMat4Loc;
	private int lightPosition_worldspaceLoc[];
	private int lightColorLoc[];
	private int shineDamperLoc;
	private int reflectivityLoc;
	private int useFakeLightingLoc;
	private int skyColorLoc;
	private int numberOfRowsLoc;
	private int offsetLoc;
	private int attenuationLoc[];
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position_modelspace");
		super.bindAttributes(1, "textureCoords");
		super.bindAttributes(2, "normal");
	}

	@Override
	protected void getAllUiformLocations() {
		transformMat4Loc = super.getUniformLocation("transformMat4");
		projectionMat4Loc = super.getUniformLocation("projectionMat4");
		viewMat4Loc = super.getUniformLocation("viewMat4");
		shineDamperLoc = super.getUniformLocation("shineDamper");
		reflectivityLoc = super.getUniformLocation("reflectivity");
		useFakeLightingLoc = super.getUniformLocation("useFakeLighting");
		skyColorLoc = super.getUniformLocation("skyColor");
		numberOfRowsLoc = super.getUniformLocation("numberOfRows");
		offsetLoc = super.getUniformLocation("offset");
	
		lightPosition_worldspaceLoc = new int[MAX_LIGHTS];
		lightColorLoc = new int[MAX_LIGHTS];
		attenuationLoc = new int[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS; i++){
			lightPosition_worldspaceLoc[i] = 
					this.getUniformLocation("lightPosition_worldspace[" + i + "]");
			lightColorLoc[i] = this.getUniformLocation("lightColor[" + i + "]");
			attenuationLoc[i] = this.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	public void setShineVariables(float damper, float reflectivity){
		super.setFloat(shineDamperLoc, damper);
		super.setFloat(reflectivityLoc, reflectivity);
	}

	public void setTransformMat4(Matrix4f mat4){
		super.setMatrix4(transformMat4Loc, mat4);
	}
	
	public void setProjectionMat4(Matrix4f mat4){
		super.setMatrix4(projectionMat4Loc, mat4);;
	}
	
	public void setViewMat4(Camera camera){
		Matrix4f viewMat4 = Maths.createViewMatrix4(camera);
		super.setMatrix4(viewMat4Loc, viewMat4);
	}
	
	public void setLights(List<Light> lights){
		for(int i = 0; i < MAX_LIGHTS; i++){
			if(i < lights.size()){
				Light light = lights.get(i);
				super.setVector(lightPosition_worldspaceLoc[i], light.getPosition());
				super.setVector(lightColorLoc[i], light.getColor());
				super.setVector(attenuationLoc[i], light.getAttenuation());
			}else{
				super.setVector(lightPosition_worldspaceLoc[i], new Vector3f(0, 0, 0));
				super.setVector(lightColorLoc[i], new Vector3f(0, 0, 0));
				super.setVector(attenuationLoc[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void setUseFakeLighting(boolean useFake){
		super.setBoolean(useFakeLightingLoc, useFake);
	}
	
	public void setSkyColor(float r, float g, float b){
		super.setVector(skyColorLoc, new Vector3f(r, g, b));
	}
	
	public void setNumberOfRows(int numberOfRows){
		super.setFloat(numberOfRowsLoc, numberOfRows);
	}
	
	public void setOffset(float x, float y){
		super.setVector2D(offsetLoc, x, y);
	}
}
