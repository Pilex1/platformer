package util;

import processing.core.PImage;
import static main.MainApplet.P;

public class Images {

	public static PImage background;
	public static PImage player;
	public static PImage platform;
	public static PImage platform_hbounce;
	public static PImage platform_vbounce;
	
	public static void load() {
		background = P.loadImage("res/scifi background.png");
		
		platform = P.loadImage("res/platform.png");
		platform_hbounce = P.loadImage("res/platform hbounce.png");
		platform_vbounce = P.loadImage("res/platform vbounce.png");
		platform.resize(50, 50);
		platform_hbounce.resize(50, 50);
		platform_vbounce.resize(50, 50);
		
		player = P.loadImage("res/player.png");
		player.resize(20,40);
	}
	
}
