package entities;

import main.*;
import processing.core.*;
import util.*;

public class Guide4 extends Npc {

	public Guide4() {
		super(new PVector(8380, Game.Floor), "Guide");
		color = Color.LightPurple;
		conversations.add("Hey...");
		conversations.add("You're here again.");
		conversations.add("...");
		conversations.add("There's something really strange going on here. I know it.");
		conversations.add("...");
		conversations.add("Something's wrong...");
		conversations.add("Really wrong...");
		conversations.add("...");
		conversations.add("Where am I?");
		conversations.add("...");
		conversations.add("Who am I?");
		conversations.add("...");
		conversations.add("What am I?");
		conversations.add(".");
		conversations.add("..");
		conversations.add("...");
		conversations.add("Who are you?");
		conversations.add(".");
		conversations.add("..");
		conversations.add("...");
		conversations.add("Here, have some pudding.");
	}

}
