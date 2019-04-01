## COMPUTER SCIENCE 311

## SPRING 2019

## PROGRAMMING ASSIGNMENT 1

## MAINTAINING A POINT OF MAXIMUM OVERLAP

## DUE: 11:59 P.M., MARCH 31

1. Overview
A _closed interval_ is an ordered pair of real numbers [a,b], witha ≤b. The interval [a,b]
represents the set{x∈R:a≤x≤b}, whereRdenotes the set of all real numbers. Suppose we
are given a collection of closed intervalsI={I 1 ,I 2 ,...,In}, whereIj =[aj,bj]. We say that an
intervalIj _overlaps_ a pointxin the real line ifaj ≤x≤bj. We say thatxis a _point of maximum
overlap_ if it has largest number of intervals inIoverlapping it (see Figure 1). Note that, in general,
there may be multiple points of maximum overlap.
The problem of finding a point of maximum overlap has several applications. Suppose, for
example, that you run a discussion board on the web. Users log in and log out throughout the
day. Each user has an associated time interval, which is given by the start and end times of the
user’s activity. Finding the peak usage time for the discussion board reduces to finding a point of
maximum overlap for this set of intervals.
The goal of this assignment is to design a data structure based on red-black trees that efficiently
maintains a point of maximum overlap in a collection of intervalsIunder a series of insertions
and (optionally) deletions of intervals.

**Teamwork.** For this programming assignment, you should work in teams of two or three. It is
your responsibility to assemble a team.

**Reading material.** To solve this project, it will be helpful to review Chapters 12, 13, and 14 of

```
Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest and Clifford Stein,
Introduction to Algorithms (3rd edition), MIT Press, 2009.
Copies of these chapters are available on Canvas.
```
2. The Key Idea
The data structure that you will implement is based on the following easy to verify fact.
_There always exists a point of maximum overlap that is an endpoint of one of the
inrevals._

See Figure 1 for an illustration.
Based on the above observation, our goal is to find, among the endpoints of the intervals inI,
an endpoint that is a point of maximum overlap. To do this, associate with each endpointean
associated valuep(e):

```
p(e)=
```
## 

## 

## 

## 

```
+1 ifeis a left endpoint and
−1 ifeis a right endpoint.
```

```
+1 +2 +3 +2 +1 +2 +1 0
```
```
k
∑ i = 1 p ( ei ):
```
```
0 1 2 3 4 5 6 7 8 9 10 11
ek : e 1 e 2 e 3 e 4 e 5 e 6 e 7 e 8
```
```
Figure 1. A collection of closed intervalsI ={I 1 ,I 2 ,I 3 ,I 4 }. The endpoints are
〈e 1 ,e 2 ,e 3 ,e 4 ,e 5 ,e 6 ,e 7 ,e 8 〉=〈 0 , 1 , 3 , 4 , 6 , 7 , 9 , 11 〉. The maximum overlap is achieved
by any pointx∈[3,4]. Observe thats( 1 ,j)is maximized forj=3 (s( 1 , 3 )=3),
indicating thate 3 is a point of maximum overlap.
```
Lete 1 ,e 2 ,...,e 2 nbe the sorted sequence of endpoints in the set of intervalsI. For 1≤i≤j≤
2 n, let us defines(i,j)as

```
s(i,j)=p(ei)+p(ei+ 1 )+···+p(ej).
```
Any endpointeithat maximizess( 1 ,i)is a point of maximum overlap (see Figure 1).

3. The Data Structure
Your program must maintain a red-black treeTof all the endpoints. Letvbe the node ofTthat
stores endpointe. Nodevhas fieldsv.keyandv.p, which equaleandp(e), respectively. It also has
the usualparent,left,right, andcolorfields. Nodevwill contain additional fields in order
to maintain the point of maximum overlap. Let`vandrvdenote the indices of the leftmost and
rightmost endpoints in the subtree rooted atv. Then,xhas the following three fields (see Figure 2).
- v.valis the sum of thep-values of the nodes in the subtree rooted atv(includingvitself);
that is,v.val=s(`v,rv).
- v.maxvalis the maximum value obtained by the expressions(`v,i)for`v≤i≤rv.
- v.emaxis a reference to an endpointem, wheremis the value ofithat maximizess(`v,i)
over allisuch that`v≤i≤rv.
Assuming these fields are maintained correctly, the root of the tree,T.root, provides the answer
to a point of maximum overlap query inO( 1 )time: T.root.emaxis a reference to a point of
maximum overlap andT.root.maxvalis the number of intervals that overlap this point.
We can compute theval,maxval, andemaxfields in a bottom-up fashion. For example, we can
computev.valusing the following recursive definition:

```
v.val=
```
## 

## 

## 

## 

0 ifv==T.nil,
v.left.val+v.p+v.right.val otherwise.
To computev.maxval, there are two possibilities. Ifv == T.nil, thenv.maxval = 0.
Otherwise, there are three cases:

```
(1) the maximum is inv’s left subtree,
(2) the maximum is atv, or
(3) the maximum is inv’s right subtree.
```

```
횎횖횊횡
```
```
0 +
1 1
e 1
```
```
7 +
1 1
e 6
```
```
3 +
1 1
e 3
```
```
6 − 1
0 0
e 6
```
```
11 − 1
−1 0
T .nil
```
```
9 − 1
−2 0
e 6
```
```
1 +
3 3
e 3
```
```
4 − 1
0 3
e 3
```
```
횟횊횕 횖횊횡횟횊횕
```
```
횔횎횢 횙
```
```
e 1
```
```
e 2
```
```
e 3
```
```
e 4
```
```
e 5
```
```
e 6
```
```
e 7
```
```
e 8
```
```
Figure 2. A red-black tree representation of the set of intervals of Figure 1. Not
shown are theparent,left,right, andcolorfields.T.nilis also not shown.
```
This leads to the following expression forv.maxval:

```
v.maxval=max{v︸. left︷︷.maxval ︸
Case 1
```
```
,︸v. left.︷︷val +v.︸p
Case 2
```
```
,v︸. left.val+v.p︷︷+ v.right.maxval︸
Case 3
```
## .}

```
We leave the derivation of an expression forv.emaxas an exercise..
```
**Updating the fields dynamically.** When a new intervalI=[a,b] is added, we must insert two
new nodes intoT: one foraand one forb. In doing so, we must update theval,maxval, andemax
fields (in addition to theparent,left,right, andcolorfields) of various other nodes.

_You must implement the interval insertion so that it runs in_ O(logn) _time, where_
n _is the number of intervals. This time bound includes the work in updating all
necessary data fields in the tree._
To understand how to achieveO(logn)insertion time, it will be helpful to read Chapter 14 of
Cormen, Leiserson, Rivest, and Stein. Theorem 14.1 of that text outlines the approach to follow, and
Section 14.3 describes an application of red-black trees to a different problem involving intervals.

```
You are not required to implement interval deletion, but bonus points will be given to
those who do. If you do implement deletion, your code should handle it in O(logn)
time.
```
**Endpoints with the same value.** Your program should allow the possibility of multiple endpoints
having the same value. To handle such endpoints, you should give precedence to the left endpoints
over any right endpoints with the same value.

4. Specifications
Your submission must be a Java program that contains the four classes described in this section:
Intervals,Endpoint,Node, andRBTree. You _must_ make these classes and their methods public.
Each of the classes may contain additional methods apart from the required ones. Be judicious in
designing the classes and the data structures.


4.1. Intervals**.** TheIntervalsclass represents a collection of intervals. It must have the
following methods.

- Intervals(): Constructor with no parameters.
- void intervalInsert(int a, int b): Adds the interval with left endpointaand right
    endpointbto the collection of intervals. Each newly inserted interval must be assigned an
    ID. The IDs should be consecutive; that is, the ID of the interval inserted on theith call of
    this method should bei. For example ifintervalInsertis called successively to insert
    intervals [5,7],[4,9],[1,8], then the IDs of these intervals should be 1, 2 ,3, respectively.
    These IDs are permanent for the respective intervals. Keep track of the IDs, as multiple
    intervals that have the same endpoints on both sides can be added. intervalInsert
    should run inO(logn)time.
- boolean intervalDelete(int intervalID): Deletes the interval whose ID (gener-
    ated byintervalInsert) isintervalID. Returnstrueif deletion was successful. This
    method should run inO(logn)time.
       **Note.** TheintervalDeletemethod is _optional_ ; that is, you are not required
       to implement it. However, your code **_must_** provide anintervalDeletemethod
       even if you choose not to implement interval deletion. If you do not implement
       deletion, theintervalDeletemethod should consist of just one line that returns
       false.
- int findPOM(): Finds the endpoint that has maximum overlap and returns its value. This
    method should run in constant time.
- RBTree getRBTree(): Returns the red-black tree used, which is an object of typeRBTree.

4.2. Endpoint**.** TheEndpointclass represents an endpoint and its value. It must have the
following method:

- int getValue(): Returns the endpoint value. For example if theEndpoint object
    represents the left endpoint of the interval [1,3], this would return 1.

4.3. Node**.** TheNoderclass epresents the nodes of the red-black tree. Use 0 and 1 to represent the
colors red and black, respectively. The following methods must be provided.

- Node getParent(): Returns the parent of this node.
- Node getLeft(): Returns the left child.
- Node getRight(): Returns the right child.
- int getKey(): Returns the endpoint value, which is an integer.
- int getP(): Returns the value of the functionpbased on this endpoint.
- int getVal(): Returns thevalof the node as described in this assignment.
- int getMaxVal(): Returns themaxvalof the node as described in this assignment.
- Endpoint getEndpoint(): Returns theEndpointobject that this node represents.
- Endpoint getEmax(): Returns anEndpointobject that representsemax. Calling this
    method on the root node will give theEndpointobject whosegetValue()provides a
    point of maximum overlap.
- int getColor(): Returns 0 if red. Returns 1 if black.

4.4. RBTree**.** TheRBTreeclass represents the red-black tree. The following methods must be
provided.

- RBTree(): Constructor with no parameters.


- Node getRoot(): Returns the root node.
- Node getNILNode(): Returns thenilnode.
    **Note.** _A red-black tree_ T _must contain_ **_exactly one_** _instance of_ T.nil_._
- int getSize(): Returns the number of internal nodes in the tree.
- int getHeight(): Returns the height of the tree.
    5. Guidelines on Code Submission
- Use the Java default package (unnamed package). While this is not good coding practice
    for larger applications, it is more convenient for testing.
- Make all the methods and constructors explicitly public.
- You may design helper classes and methods in addition to those listed in Section 4. However,
    every class and method listed in Section 4 must be implemented **_exactly as specified_**. This
    includes
       **-** the names of methods and classes ( _remember that Java is case-sensitive_ ),
       **-** the return types of the methods, and
       **-** the types and order of parameters of each method/constructor.
    If you fail to follow these requirements, you will lose a significant portion of the points,
    even if your program is correct.
- You are not allowed to have external JARs as dependencies. You may use inbuilt packages
    such asjava.util.List.
- Please include all team members names as a JavaDoc comment in each of the Java files.
- Create the project folder as follows:<directory-name>/src/*.java. This folder should
    nclude _only_ .javafiles. Do not include any.classor other files.
- Create a zip file of your project folder.
- Upload your zip file on Canvas. Only one submission per team.
Your grade will depend on adherence to specifications, correctness, and efficiency.
_Programs that do not compile will receive zero credit._

```
Important Note
```
Some aspects of this specification are subject to change in response to issues detected by students
or the course staff. **_Check Canvas and Piazza regularly for updates and clarifications._**


