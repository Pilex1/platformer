package entities;

import main.*;
import processing.core.*;
import util.*;

public class Guide3 extends Npc {

	public Guide3() {
		super(new PVector(5260, Game.Floor), "Guide");
		color = Color.LightPurple;
		conversations.add("Hey! It's you again!");
		conversations.add("What a coincidence!");
		conversations.add("...");
		conversations.add("Hmmm... something fishy is definitely going on here.");
		conversations.add("Wow look, I've teleported again.");
		conversations.add("...");
		conversations.add("Why do I get the feeling that I'm being controlled by someone...?");
		conversations.add("As if everything I'm saying has been scripted by someone already...");
		conversations.add("As if someone has had this planned out all along...");
		conversations.add("...");
		conversations.add("What?");
		conversations.add("...");
		conversations.add("What did I just say?");
		conversations.add("...");
		conversations.add("Nothing.");
		conversations.add("I said nothing.");
		conversations.add("Must be just your imagination.");
		conversations.add("Yes. That's right.");
		conversations.add("Just your imagination.");
		conversations.add("...");
		conversations.add("Oh... did you say you wanted some pearl milk tea?");
		conversations.add("Yes, it's very nice, I promise!");
		conversations.add("Here... have some...");
		conversations.add("...");
		conversations.add("It's nice, isn't it?");
		conversations.add("...");
		conversations.add("Mmm... delicious!");

	}

}
