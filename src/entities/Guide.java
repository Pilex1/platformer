package entities;

import main.*;
import processing.core.*;
import util.*;

public class Guide extends Npc {

	// 400 pixels to the left of the checkpoint

	public Guide() {
		super(new PVector(550, Game.Floor), "Guide");
		color = Color.LightPurple;
		conversations.add("I hear they're celebrating someone's birthday up ahead.");
		conversations.add("If I recall, it's someone named... M... Ma...");
		conversations.add("Mmmm... I really can't remember. I would go check it out myself.");
		conversations.add("But I'm just a random purple rectangle.");
		conversations.add("I can't even move around... see I'm trying...");
		conversations.add("I'm trying really hard to move around, but I just can't.");
		conversations.add("I'll just stand here all day... it's fine.");
		conversations.add("But you... what a lucky person you are, with the power of freedom embodied into you.");
		conversations.add("Go check out what's up ahead!");
	}

}
