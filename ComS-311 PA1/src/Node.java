/**
 * Team members:
 * @author Christopher Woods
 * @author Andrew Tran
 * @author Gavin Monroe
 *
 * Node class for RBTree.
 */
public class Node {

	public int color;
	public int key;
	public int p;
	public int val;
	public int maxval;
	public int height;
	public Node left;
	public Node right;
	public Node parent;
	public Endpoint e;
	public Endpoint emax;

	public Node(int key, int p){
		this.key = key;
		this.p = p;
		color = 1;
		left = RBTree.nil;
		right = RBTree.nil;
		parent = RBTree.nil;
		height = 0;
		e = new Endpoint(key);
		emax = e;
	}

	/**
	 * Returns the parent of this node.
	 * @return
	 */
	public Node getParent() {
		return this.parent;
	}

	/**
	 * Returns the left child.
	 * @return
	 */
	public Node getLeft() {
		return this.left;
	}

	/**
	 * Returns the right child.
	 * @return
	 */
	public Node getRight() {
		return this.right;
	}

	/**
	 * Returns the endpoint value, which is an integer.
	 * @return
	 */
	public int getKey() {
		return this.key;
	}

	/**
	 * Returns the value of the functionpbased on this endpoint.
	 * @return
	 */
	public int getP() {
		return this.p;
	}

	/**
	 * Returns the val of the node as described in this assignment.
	 * @return
	 */
	public int getVal() {
		return this.val;
	}

	/**
	 * Returns themaxvalof the node as described in this assignment.
	 * @return
	 */
	public int getMaxVal() {
		return this.maxval;
	}

	/**
	 * Returns theEndpointobject that this node represents.
	 * @return
	 */
	public Endpoint getEndpoint() {
		return this.e;
	}

	/**
	 * Returns anEndpointobject that represents emax.
	 * Calling this method on the root node will give the End point object whose getValue()
	 * provides a point of maximum overlap.
	 * @return
	 */
	public Endpoint getEmax() {
		return this.emax;
	}

	/**
	 * Returns 0 if red. Returns 1 if black.
	 * @return
	 */
	public int getColor() {
		return this.color;
	}
}
