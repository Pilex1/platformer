package entities;

import processing.core.*;
import util.*;

public class Mystery extends Npc {

	public Mystery() {
		super(new PVector(14200, 8820), "???");
		color = Color.LightOrange;
		conversations.add("Happy birthday Mai!");
		conversations.add("I hope this was as fun for you as it was for me to make.");
		conversations.add("I have to say though, it feels really strange talking to you like this.");
		conversations.add("Anyways, enjoy your birthday!");
	}

}
