package terrain;

import processing.core.*;
import util.*;

public class GuideRemovalPlatform extends Platform {

	public GuideRemovalPlatform(PVector pos) {
		super(pos);
		strokeColor = Color.Red;
		fillColor = Color.Transparent;
	}
}
