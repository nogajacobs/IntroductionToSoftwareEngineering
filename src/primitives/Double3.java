/**
 *
 */
package primitives;

/**
 * Double3 class represents a triad of three double-precision numbers.
 * It provides methods for basic arithmetic operations and comparisons.
 *
 * Authors: Noga Jacobs and Noa
 */
public class Double3 {
	final double d1;
	final double d2;
	final double d3;

	/**
	 * Zero triad (0,0,0)
	 */
	public static final Double3 ZERO = new Double3(0, 0, 0);

	/**
	 * Ones triad (1,1,1)
	 */
	public static final Double3 ONE = new Double3(1, 1, 1);

	/**
	 * Constructor to initialize a Double3 object with its three number values.
	 *
	 * @param d1 First number value
	 * @param d2 Second number value
	 * @param d3 Third number value
	 */
	public Double3(double d1, double d2, double d3) {
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
	}

	/**
	 * Constructor to initialize Double3 based object the same number values
	 *
	 * @param value number value for all 3 numbers
	 */
	public Double3(double value) {
		this.d1 = value;
		this.d2 = value;
		this.d3 = value;
	}

	// ***************** Override methods ********************** //

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Double3 other)
			return Util.isZero(d1 - other.d1) && Util.isZero(d2 - other.d2) && Util.isZero(d3 - other.d3);
		return false;
	}

	@Override
	public int hashCode() {
		return (int) Math.round(d1 + d2 + d3);
	}

	@Override
	public String toString() {
		return "(" + d1 + "," + d2 + "," + d3 + ")";
	}

		// ***************** Arithmetic operations ********************** //

	/**
	 * Sum two floating point triads into a new triad where each couple of numbers
	 * is summarized
	 *
	 * @param rhs right handle side operand for addition
	 * @return result of add
	 */
	public Double3 add(Double3 rhs) {
		return new Double3(d1 + rhs.d1, d2 + rhs.d2, d3 + rhs.d3);
	}

	/**
	 * Subtract two floating point triads into a new triad where each couple of
	 * numbers is subtracted
	 *
	 * @param rhs right handle side operand for addition
	 * @return result of add
	 */
	public Double3 subtract(Double3 rhs) {
		return new Double3(d1 - rhs.d1, d2 - rhs.d2, d3 - rhs.d3);
	}

	/**
	 * Scale (multiply) floating point triad by a number into a new triad where each
	 * number is multiplied by the number
	 *
	 * @param rhs right handle side operand for scaling
	 * @return result of scale
	 */
	public Double3 scale(double rhs) {
		return new Double3(d1 * rhs, d2 * rhs, d3 * rhs);
	}

	/**
	 * Reduce (divide) floating point triad by a number into a new triad where each
	 * number is divided by the number
	 *
	 * @param rhs right handle side operand for reducing
	 * @return result of scale
	 */
	public Double3 reduce(double rhs) {
		return new Double3(d1 / rhs, d2 / rhs, d3 / rhs);
	}

	/**
	 * Product two floating point triads into a new triad where each couple of
	 * numbers is multiplied
	 *
	 * @param rhs right handle side operand for product
	 * @return result of product
	 */
	public Double3 product(Double3 rhs) {
		return new Double3(d1 * rhs.d1, d2 * rhs.d2, d3 * rhs.d3);
	}

	// ***************** Comparison operation ********************** //

	/**
	 * Checks whether all the numbers are lower than a test number.
	 *
	 * @param k The test number
	 * @return True if all the numbers are less than k, false otherwise
	 */
	public boolean lowerThan(double k) {
		return d1 < k && d2 < k && d3 < k;
	}

}
