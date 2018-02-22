package entities;

import main.*;
import processing.core.*;
import util.*;

public class Guide5 extends Npc {

	public Guide5() {
		super(new PVector(10680, Game.Floor), "Guide");
		color = Color.LightPurple;
		conversations.add(".");
		conversations.add("..");
		conversations.add("...");
		conversations.add("Do I exist?");
		conversations.add(".");
		conversations.add("..");
		conversations.add("...");
		conversations.add("Do YOU exist?");
		conversations.add(".");
		conversations.add("..");
		conversations.add("...");
	}

}
