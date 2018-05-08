package util;

import processing.core.PImage;
import static main.MainApplet.P;

import main.Applet;

public class Images {

	public static PImage Background;
	public static PImage Player;
	public static PImage Platform;
	public static PImage HBounce;
	public static PImage VBounce;
	public static PImage SensorOn;
	public static PImage SensorOff;
	public static PImage AndOn, AndOff;

	public static void load() {
		Background = P.loadImage("res/scifi background.png");
		Background.resize(Applet.WIDTH, Applet.HEIGHT);

		Platform = P.loadImage("res/platform.png");
		HBounce = P.loadImage("res/platform hbounce.png");
		VBounce = P.loadImage("res/platform vbounce.png");
		SensorOn = P.loadImage("res/sensor on.png");
		SensorOff = P.loadImage("res/sensor off.png");
		AndOn = P.loadImage("res/and on.png");
		AndOff = P.loadImage("res/and off.png");

		Platform.resize(50, 50);
		HBounce.resize(50, 50);
		VBounce.resize(50, 50);
		SensorOn.resize(50, 50);
		SensorOff.resize(50, 50);
		AndOn.resize(50, 50);
		AndOff.resize(50, 50);

		Player = P.loadImage("res/player.png");
		Player.resize(20, 40);
	}

}
