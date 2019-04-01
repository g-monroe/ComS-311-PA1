/**
 * Team members:
 * @author Christopher Woods
 * @author Andrew Tran
 * @author Gavin Monroe
 * 
 * Endpoint class for Node.
 */
public class Endpoint {
	public int num;
    /**
     * Constructor with no parameters.
     */
    public Endpoint(int input) {
        num = input;
    }
	/**
	 * returns the endpoint value.  For example if the
	 * End point object represents the left end point of the 
	 * interval [1,3], this would return 1.
	 * @return
	 */

	public int getValue() {
		return num;
	}
}
