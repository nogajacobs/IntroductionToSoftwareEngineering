/**
 *
 */
package renderer;

import org.junit.jupiter.api.Test;

import static java.awt.Color.*;

import renderer.ImageWriter;
import lighting.*;
import geometries.*;
import primitives.*;
import renderer.*;
import Scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 *
 * @author dzilb
 */
class ReflectionRefractionTests {
	private Scene scene = new Scene.SceneBuilder("Test scene").build();

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheres() {
		Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(150, 150).setVPDistance(1000);

		scene.getGeometries().add( //
				new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setkD(0.4).setkS(0.3).setnShininess(100).setkT(new Double3(0.3))),
				new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)));
		scene.getLights().add( //
				new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setkL(0.0004).setkQ(0.0000006));

		camera.setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500)) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage(); //
		camera.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheresOnMirrors() {
		Camera camera = new Camera(new Point(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(2500, 2500).setVPDistance(10000); //

		Scene scene = new Scene.SceneBuilder("Test scene").setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.1))).build();

		scene.getGeometries().add( //
				new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 0, 100)) //
						.setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20).setkT(new Double3(0.5))),
				new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 20, 20)) //
						.setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20)),
				new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), new Point(670, 670, 3000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(new Double3(1))),
				new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
						new Point(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(new Double3(0.5))));

		scene.getLights().add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setkL(0.00001).setkQ(0.000005));

		ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirrored", 500, 500);
		camera.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage(); //
		camera.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light with a partially
	 * transparent Sphere producing partial shadow
	 */
	@Test
	public void trianglesTransparentSphere() {
		Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(200, 200).setVPDistance(1000);

		Scene scene = new Scene.SceneBuilder("Test scene").setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15))).build();

		scene.getGeometries().add( //
				new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)), //
				new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)), //
				new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(30).setkT(new Double3(0.6))));

		scene.getLights().add(new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1)) //
				.setkL(4E-5).setkQ(2E-7));

		ImageWriter imageWriter = new ImageWriter("refractionShadow", 600, 600);
		camera.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage() ;//
		camera.writeToImage();
	}
	@Test
	public void test() {
		Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(200, 200).setVPDistance(1000);
		Scene scene = new Scene.SceneBuilder("Test scene").setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15))).build();

		scene.getGeometries().add( //
				new Triangle(new Point(-40,50, 0), new Point(-30,30,15), new Point(90,40,40))
						.setEmission(new Color(YELLOW)).setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(new Double3(100))),
		         new Triangle(new Point(-40,50, 00), new Point(10,70,55), new Point(90,40,40))
				.setEmission(new Color(RED))//
			    .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)),
				new Triangle(new Point(40,-50, 00), new Point(-10,-70,-55), new Point(90,40,40))
						.setEmission(new Color(GREEN))//
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)),
				new Triangle(new Point(40,-50, 00), new Point(-10,-70,-55), new Point(-90,-40,-40))
						.setEmission(new Color(BLUE))//
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)),
				new Triangle(new Point(-40,50, 00), new Point(0,-45,-30), new Point(-85,-35,-35))
				.setEmission(new Color(255,0,255))//
				.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60)));


		scene.getLights().add(new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1)) //
				.setkL(4E-5).setkQ(2E-7));

		ImageWriter imageWriter = new ImageWriter("222", 600, 600);
		camera.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage() ;//
		camera.writeToImage();
	}
	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void testnoga() {
		Camera camera = new Camera(new Point(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(2500, 2500).setVPDistance(10000); //

		Scene scene = new Scene.SceneBuilder("Test scene").setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.1))).build();

		scene.getGeometries().add( //
				new Triangle(new Point(600, -600, 0),new Point(1000,-600,400),new Point(550,-970,-200)).setEmission(new Color(0, 0, 128)) //
						.setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20)),
				new Triangle(new Point(-950, -900, -1000), new Point(1000,900,1000),new Point(-1000,900,1000)).setEmission(new Color(35,67,55)) //
						.setMaterial(new Material().setkD(0.25).setkS(0.25).setnShininess(20)),
				new Sphere(new Point(0, 0, 0), 200) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(new Double3(1))),
				new Sphere(new Point(-700, -600, -500), 100) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(new Double3(0.5))),
				new Sphere(new Point(700, 700, 500), 300) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(new Double3(0.5))),
				new Sphere(new Point(950, -700, -200), 50) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(new Double3(1.5))),
				new Sphere(new Point(790, -800, -200), 50) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(new Double3(0.90))),
				new Sphere(new Point(630, -900, -200), 50) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(new Double3(0.5))),
				new Sphere(new Point(1100, -1100, -200), 250) //
						.setEmission(new Color(255, 242, 0)) //
						.setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(30)));

		scene.getLights().add(
				new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setkL(0.00001).setkQ(0.000005));
		scene.getLights().add(
						new SpotLight(new Color(400, 240, 0), new Point(1100, -1100, -200), new Vector(-2100, 2000, 1200)) //
								.setkL(1E-5).setkQ(1.5E-7));
		ImageWriter imageWriter = new ImageWriter("nogatest", 500, 500);
		camera.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage(); //
		camera.writeToImage();
	}

}