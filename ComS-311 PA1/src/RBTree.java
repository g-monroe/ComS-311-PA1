import java.util.Stack;

/**
 * Team members:
 * @author Christopher Woods
 * @author Andrew Tran
 * @author Gavin Monroe
 * 
 * RBTree class, maintains operations on RBTree.
 */
public class RBTree {

    Node root;
    static Node nil;
    int size;

	/**
	 * RB Tree constructor. It initializes nil node as well.
	 */
	public RBTree() {
        //set root to nil node
		//nil = new Node(0, 0, 0, 0, 0, nil, nil, nil, null, null);
		nil = new Node(0, 0);
		nil.parent = nil;
		nil.left = nil;
		nil.right = nil;
        this.root = this.nil;
        this.size = 0;
	}
	
	/**
	 * Returns the root of the tree.
	 * @return
	 */
	public Node getRoot() {
		return root;
	}
	
	/**
	 * Returns reference for the nil node, for the rbTree.
	 * @return
	 */
	public Node getNILNode() {
		return nil;
	}
	
	/**
	 * Returns the number of internal nodes in the tree.
	 * @return
	 */
	public int getSize() {
		return size;
	}
	
	
	/**
	 * Returns the height of the tree.
	 *
	 * NOTE: This height is only updated properly through the use of the wrappper class "Intervals", using only the
	 * insert and delete methods in this "RBTree" class will leave a height of 0. Since insert() and delete() are not
	 * included in the provided skeleton for this PA1, height only updating on insertInterval and deleteInterval
	 * shouldn't be a problem.
	 *
	 * @return
	 */
	public int getHeight() {
		return root.height;
	}
	
	//Add more functions as  you see fit.

	/**
	 * Classic RBInsert from CLRS, only it additionally creates a stack containing all nodes that
	 * have moved around from rotates, which is used to update the val, maxval, emax, and height
	 * values in the Intervals class. This allows the tree to be updated properly on each insert
	 * while maintaining a O(log n) runtime.
	 *
	 * @param n The node that is to be inserted.
	 * @return The stack of nodes that were moved, with the inserted node at the very bottom.
	 */
    public Stack<Node> insert(Node n) {
		Node y = nil;
		Node x = root;
	    while(x != nil) {
			y = x;
			if (n.key < x.key || (n.key == x.key && n.p == 1)) {
				x = x.left;
			} else {
				x = x.right;
			}
		}
		n.parent = y;
		if(y == nil) {
			root = n;
		}
		else if(n.key < y.key || (n.key == y.key && n.p == 1)) {
			y.left = n;
		}
		else {
			y.right = n;
		}
		n.left = nil;
		n.right = nil;
		n.color = 0;
		size++;
		return insertFixup(n);
    }

	/**
	 * RBDelete from CLRS, and much like RBInsert it also returns a stack of all nodes
	 * that were moved around in the deletion process.
	 *
	 * @param z The node to be deleted.
	 * @return The stack of nodes that were moved.
	 */
    public Stack<Node> delete(Node z) {
		Node y = z;
		Node x;
		Stack<Node> toPercolate = new Stack<Node>();
		int yOriginalColor = y.color;
		if(z.left == nil){
			x = z.right;
			transplant(z, z.right);
		} else if(z.right == nil) {
			x = z.left;
			transplant(z, z.left);
		} else {
			y = z.right;
			while(y.left != nil) y = y.left;
			yOriginalColor = y.color;
			x = y.right;
			if(y.parent == z) x.parent = y;
			else {
				transplant(y, y.right);
				y.right = z.right;
				y.right.parent = y;
			}
			transplant(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
		}
		size--;
		toPercolate.add(y);
		toPercolate.add(x.parent);
		if(yOriginalColor == 1) toPercolate = deleteFixup(x, toPercolate);
		return toPercolate;
    }

	/**
	 * RBInsertFixup from CLRS. The stack returned is described in the Javadoc for insert.
	 *
	 * @param z The node to be fixed.
	 * @return The stack of all nodes that were moved.
	 */
	public Stack<Node> insertFixup(Node z) {
		Stack<Node> toPercolate = new Stack<Node>();
		toPercolate.add(z);
		while(z.parent.color == 0) {
			if(z.parent == z.parent.parent.left) {
				Node y = z.parent.parent.right;
				if(y.color == 0) {
					z.parent.color = 1;
					y.color = 1;
					z.parent.parent.color = 0;
					z = z.parent.parent;
				}
				else {
					if(z == z.parent.right) {
						z = z.parent;
						toPercolate.add(z);
						leftRotate(z);
					}
					z.parent.color = 1;
					z.parent.parent.color = 0;
					toPercolate.add(z.parent.parent);
					rightRotate(z.parent.parent);
				}
			}
			else {
				Node y = z.parent.parent.left;
				if(y.color == 0) {
					z.parent.color = 1;
					y.color = 1;
					z.parent.parent.color = 0;
					z = z.parent.parent;
				}
				else {
					if(z == z.parent.left) {
						z = z.parent;
						toPercolate.add(z);
						rightRotate(z);
					}
					z.parent.color = 1;
					z.parent.parent.color = 0;
					toPercolate.add(z.parent.parent);
					leftRotate(z.parent.parent);
				}
			}
		}
		root.color = 1;
		return toPercolate;
	}

	/**
	 * LeftRotate from CLRS.
	 *
	 * @param x The node to be rotated.
	 */
    public void leftRotate(Node x) {
		Node y = x.right;
		x.right = y.left;
		if(y.left != nil) {
			y.left.parent = x;
		}
		y.parent = x.parent;
		if(x.parent == nil) {
			root = y;
		}
		else if(x == x.parent.left) {
			x.parent.left = y;
		}
		else {
			x.parent.right = y;
		}
		y.left = x;
		x.parent = y;
	}

	/**
	 * RightRotate from CLRS.
	 *
	 * @param x The node to be rotated.
	 */
	public void rightRotate(Node x) {
		Node y = x.left;
		x.left = y.right;
		if(y.right != nil) {
			y.right.parent = x;
		}
		y.parent = x.parent;
		if(x.parent == nil) {
			root = y;
		}
		else if(x == x.parent.right) {
			x.parent.right = y;
		}
		else {
			x.parent.left = y;
		}
		y.right = x;
		x.parent = y;
	}

	/**
	 * RBTransplant from CLRS.
	 *
	 * @param u The node to transplant onto.
	 * @param v The node to transplant.
	 */
	public void transplant(Node u, Node v) {
		if(u.parent == nil) root = v;
		else if (u == u.parent.left) u.parent.left = v;
		else u.parent.right = v;
		v.parent = u.parent;
	}

	/**
	 * RBDeleteFixup from CLRS. The stack is described in the Javadoc for delete.
	 *
	 * Throughout deleteFixup at most 10 nodes are inserted into the stack since there
	 * are always at most 3 rotations done by fixup, and since updating a node takes O(1) time
	 * (constant time), use of the stack does not extend the time complexity of delete() beyond
	 * O(log n), since it's doing an O(1) algorithm (updating a node) at most 10 + log(n) times (full
	 * percolation up the tree), which is O(log n).
	 *
	 * This reasoning also applies to insertFixup, albeit on
	 * a much smaller scale, since insertFixup will have a maximum stack size of 3.
	 *
	 * @param x The node to fixup.
	 * @param toPercolate The stack to add rotated nodes to.
	 * @return The stack containing all moved nodes.
	 */
	public Stack<Node> deleteFixup(Node x, Stack<Node> toPercolate) {
		while (x != root && x.color == 1) {
			if (x == x.parent.left) {
				Node w = x.parent.right;
				if (w.color == 0) {
					w.color = 1; //Case 1
					x.parent.color = 0;
					/*toPercolate.add(x.parent.parent); //
					toPercolate.add(x.parent); //
					toPercolate.add(x); //*/
					leftRotate(x.parent);
					w = x.parent.right;
				}
				if (w.left.color == 1 && w.right.color == 1) {
					w.color = 0; //Case 2
					toPercolate.add(x.parent); //
					toPercolate.add(x); //
					toPercolate.add(w);
					x = x.parent;
				} else {
					if (w.right.color == 1) { //Case 3
						w.left.color = 1;
						w.color = 0;
						rightRotate(w);
						toPercolate.add(w.parent);
						toPercolate.add(w);
						w = x.parent.right;
					}
					w.color = x.parent.color; //Case 4
					x.parent.color = 1;
					w.right.color = 1;
					toPercolate.add(x.parent);
					leftRotate(x.parent);
					x = root;
				}
			} else {
				//above but mirrored
				Node w = x.parent.left;
				if (w.color == 0) {
					w.color = 1;
					x.parent.color = 0;
					/*toPercolate.add(x.parent.parent); //
					toPercolate.add(x.parent); //
					toPercolate.add(x); //*/
					rightRotate(x.parent);
					w = x.parent.left;
				}
				if (w.right.color == 1 && w.left.color == 1) {
					w.color = 0;
					toPercolate.add(x.parent); //
					toPercolate.add(x); //
					toPercolate.add(w);
					x = x.parent;
				} else {
					if (w.left.color == 1) {
						w.right.color = 1;
						w.color = 0;
						leftRotate(w);
						toPercolate.add(w.parent);
						toPercolate.add(w);
						w = x.parent.left;
					}
					w.color = x.parent.color;
					x.parent.color = 1;
					w.left.color = 1;
					toPercolate.add(x.parent);
					rightRotate(x.parent);
					x = root;
				}
			}
		}
		x.color = 1;
		return toPercolate;
	}

	/**
	 * Helper method that traverses and prints each node in the tree along with its height from the root in the tree,
	 * with the root being one.
	 *
	 * @param n The node to begin the recursive call (should initially be root).
	 * @param h The current height of the tree in the traversal (should initally be 0).
	 */
	static void printTraverse(Node n, int h){
		h++;
		if(n == RBTree.nil){
			//System.out.println("Found nil!");
			return;
		}
		printTraverse(n.left, h);
		System.out.println(h + ": " + n.key + ", " + n.color);
		printTraverse(n.right, h);
	}
}
