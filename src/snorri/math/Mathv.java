package snorri.math;

import org.bukkit.util.Vector;

public class Mathv {
	
	public static Vector getForceOn(Vector position, Vector center, double flux) {
		Vector newVector = position.subtract(center);
		double scalingFactor = -Math.pow(newVector.lengthSquared(), -1.5);
		return newVector.multiply(scalingFactor * flux);
	}
	
	public static double getAngleBetween(Vector v1, Vector v2) {
		return Math.acos(v1.dot(v2) / (v1.length() * v2.length()));
	}
	
	public static double getWorkTerm(Vector force, Vector motion) {
		return force.dot(motion);
	}

}
