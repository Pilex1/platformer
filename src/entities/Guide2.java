package entities;

import main.*;
import processing.core.*;
import util.*;

public class Guide2 extends Npc {
	public Guide2() {
		super(new PVector(3210, Game.Floor), "Guide");
		color = Color.LightPurple;
		conversations.add("Oh hey, it's you again.");
		conversations.add("Wait a moment... how did you get here?");
		conversations.add("Wait... how did I get here?");
		conversations.add("Wasn't I just talking to you a moment ago?");
		conversations.add("Like somewhere else?");
		conversations.add("...");
		conversations.add("Wow... it's as if I've teleported through space.");
		conversations.add("Maybe if I try hard enough, I can teleport again...");
		conversations.add("...");
		conversations.add("Hmmm... how strange.");
		conversations.add("...");
		conversations.add("Anyways... I was supposed to be...");
		conversations.add("Oh yes... you should have some of these brownies. They're really nice.");
		conversations.add("I promise!");
		conversations.add("...");
		conversations.add("See?");
		conversations.add("...");
		conversations.add("Here have another one.");
		conversations.add("...");
		conversations.add("They're really nice, aren't they?");
		conversations.add("...");
		conversations.add("Mmm... delicious.");
	}
}
