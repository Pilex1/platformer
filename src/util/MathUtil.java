package util;

import processing.core.PVector;

public class MathUtil {

	public static PVector clampAbsolute(PVector p, PVector bounds) {
		PVector  r = p.copy();
		r.x = Math.signum(p.x) * Math.min(Math.abs(p.x), Math.abs(bounds.x));
		r.y = Math.signum(p.y) * Math.min(Math.abs(p.y), Math.abs(bounds.y));
		return r;
	}

}
