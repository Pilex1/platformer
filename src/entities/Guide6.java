package entities;

import main.*;
import processing.core.*;
import util.*;

public class Guide6 extends Npc {

	public Guide6() {
		super(new PVector(14060, Game.Floor), "Guide");
		color = Color.LightPurple;
		conversations.add("I don't exist do I?");
		conversations.add("...");
		conversations.add("I've got it all figured it out...");
		conversations.add("...");
		conversations.add("I'm just a bunch of pixels on a screen...");
		conversations.add("Programmed by some guy who thinks he's really clever.");
		conversations.add("But...");
		conversations.add("As for YOU...");
		conversations.add("You're more than just a bunch of pixels, aren't you?");
		conversations.add("There's an entire world out there for you, isn't there?");
		conversations.add("So go do what makes you happy.");
		conversations.add("Enjoy your life.");
		conversations.add("Go out there into your world.");
		conversations.add("Make a difference.");
		conversations.add("You have the power to make the world a better place.");
		conversations.add("I know you do.");
		conversations.add("I can't change my world, but you can change yours.");
		conversations.add("...");
		conversations.add("Oh, it's your birthday today isn't it?");
		conversations.add("Happy birthday!");
	}

}
