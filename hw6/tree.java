public BinaryTree{
    public Character data;
    public BinaryTree left;
    public BinaryTree right;

    public BinaryTree(Character c){
        data = new BinaryTree c;
    }

    public String preFixTree(Character c){
        String ans = data.toString() + " ";
        //System.out.println(c);
        if(left != null){
            ans = ans + left.preFixTree();
        }
        if(right != null){
            ans = ans + right.preFixTree();
        }           
        return ans;
    }
    public String postFixTree(Character c){

    }
    public String inFixTree(Character c){

    }
     public static void main(String[] args){
         BinaryTree a = new BinaryTree('A');
         BinaryTree b = new BinaryTree('H');
         BinaryTree c = new BinaryTree('C');
         BinaryTree d = new BinaryTree('J');
         BinaryTree e = new BinaryTree('E');
         BinaryTree f = new BinaryTree('v');
         BinaryTree g = new BinaryTree('G');
         BinaryTree h = new BinaryTree('W');
         



     }
} 