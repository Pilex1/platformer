package main;

import processing.core.PImage;
import static main.MainApplet.P;

import main.Applet;

public class Images {

	public static PImage Background;
	public static PImage Player;
	public static PImage Platform;
	public static PImage HBounce, VBounce;
	public static PImage SensorOn, SensorOff;
	public static PImage AndOn, AndLeft, AndRight,AndOff;
	public static PImage InverterOn, InverterOff;

	public static void load() {
		Background = P.loadImage("res/scifi background.png");
		Background.resize(Applet.WIDTH, Applet.HEIGHT);

		Platform = P.loadImage("res/platform.png");
		HBounce = P.loadImage("res/platform hbounce.png");
		VBounce = P.loadImage("res/platform vbounce.png");
		SensorOn = P.loadImage("res/sensor on.png");
		SensorOff = P.loadImage("res/sensor off.png");
		AndOn = P.loadImage("res/and on both.png");
		AndOff = P.loadImage("res/and off.png");
		AndLeft = P.loadImage("res/and on left.png");
		AndRight=P.loadImage("res/and on right.png");
		InverterOn=P.loadImage("res/inverter on.png");
		InverterOff = P.loadImage("res/inverter off.png");

		Platform.resize(50, 50);
		HBounce.resize(50, 50);
		VBounce.resize(50, 50);
		SensorOn.resize(50, 50);
		SensorOff.resize(50, 50);
		AndOn.resize(50, 50);
		AndOff.resize(50, 50);
		AndLeft.resize(50, 50);
		AndRight.resize(50, 50);
		InverterOn.resize(50, 50);
		InverterOff.resize(50, 50);

		Player = P.loadImage("res/player.png");
		Player.resize(20, 40);
	}

}
