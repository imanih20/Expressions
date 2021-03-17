
public class BinaryTree<T> {
    Node root;
    int [] positions;
    private int avail;
    private String inorder;
    private String postOrder;
    private String preOrder;

    BinaryTree(String root,int size){
        setPositions(size);
        setRoot(root);
        inorder="";
        preOrder="";
        postOrder="";
    }
    private void setPositions(int size){
        positions=new int[size];
        avail=0;
    }
    private void setRoot(String root){
        this.root=new Node(root);
        this.root.position=1;
        positions[avail]=1;
        avail++;
    }
    public Node find(Node node, int key){
        if (node==null)
            return null;
        if (node.position==key)
            return node;
        if (node.left!=null)
            return find(node.left,key);
        if (node.right!=null)
            return find(node.right, key);
        return null;
    }
    public void setLeft(int parent,String left){
        Node node=find(root,parent);
        Node lft=new Node(left);
        lft.position=parent*2;
        lft.parent=node;
        node.left=lft;
        positions[avail]=parent*2;
    }
    public void setRight(int parent,String right){
        Node node=find(root, parent);;
        Node rgt=new Node(right);
        rgt.position=parent*2+1;
        rgt.parent=node;
        node.right=rgt;
        positions[avail]=parent*2+1;
    }
    public int getRight(int parent){
        Node node=find(root, parent);
        if (node==null||node.right==null)
            return 0;
        return node.right.position;
    }
    public int getLeft(int parent){
        Node left=find(root,parent);
        if (left==null||left.left==null)
            return 0;
        return left.left.position;
    }
    public int getParent(int node){
        Node node1=find(root,node);
        if (node1==null||(node1).parent==null)
            return 0;
        return node1.parent.position;
    }
    public void setInorder(Node root){
        if (root==null)
            return;
        if (root.right!=null||root.left!=null)
            inorder+="( ";
        setInorder(root.left);
        inorder+=root.data+" ";
        setInorder(root.right);
        if (root.right!=null||root.left!=null)
            inorder+=") ";
    }
    public void setPostOrder(Node root){
        if (root==null)
            return;
        setPostOrder(root.left);
        setPostOrder(root.right);
        postOrder+=root.data+","+root.position+" ";
    }
    public void setPreOrder(Node root){
        if (root==null)
            return;
        preOrder+=root.data+" ";
        setPreOrder(root.left);
        setPreOrder(root.right);
    }
    public String getPreOrder() {
        return preOrder;
    }

    public String getInorder() {
        return inorder;
    }

    public String getPostOrder() {
        return postOrder;
    }
}
class Node{
    String data;
    int position;
    Node right,left,parent;
    public Node(String data){
        this.data=data;
        position=0;
        left=null;
        right=null;
        parent=null;
    }
    public String toString(){
        return (String) data;
    }
}
