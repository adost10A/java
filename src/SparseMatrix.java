
import java.util.*;

/**
 * Created by adost1 on 10/9/2016.
 */
// Sparse Matrix class: 2D grid of linked nodes; elements which are
// the fillElem do not have nodes present. Retains a dense array of
// row and column pointers to speed some operations and ease
// implementation.
//
// Target Space Complexity: O(E + R + C)
//   E: number of non-fill elements in the matrix
//   R: number of rows in the matrix
//   C: number of cols in the matrix
public class SparseMatrix<T>{
    protected int rows;
    protected int cols;
    protected int element_count;
    protected T fillElem;
    protected List<Head<T>> theRows,theCols;
    // Suggested internal class to represent Row and Column
    // Headers. Tracks indices of the col or row, pointer to the start
    // of row/column. This is a separate class from Node to enable
    // efficient changing of row numbers: all Nodes point to a row Head
    // and col Head to determine their own row/column. You may modify
    // this class as you see fit.
    protected static class Head<X> {

        public int index;             // Index of row/col

        public Node<X> nodes;         // Dummy node at start of row/column; add data nodes after the dummy

        public int element_count;
        public Head(int i)           // Constructor for a new Head of a node list, i is the row or column number for this Head
        {
            this.index = i;

        }
        public void inc_index()
        {
            index++;
        }
        public void int_element(){element_count++;}


    }

    // Suggested class to store data. Contains single links to next
    // nodes to the right and down. Also contains links to the row and
    // col Heads which store its row# and col#.  You may modify this
    // class as you see fit.
    protected static class Node<Y>{

        public Head<Y> rowHead;     // My row head

        public Head<Y> colHead;     // My col head

        public Node<Y> right;       // Next Node to the right

        public Node<Y> down;        // Next node down

        public Y data;              // Data associated with the node

        Node<Y> next;
        public Node(Y data)
        {
            this.data = data;
        }
        public Node()
        {
            data = null;
        }
        public int rindex()
        {
            return rowHead.index;
        }
        public int cindex()
        {
            return colHead.index;
        }


        public String toString()
        {
            StringBuilder s1 = new StringBuilder();
            s1.append(data);
            return s1.toString();
        }
        public String debugtoString()
        {
            StringBuilder b1 = new StringBuilder();
            b1.append("Data: " + data);
            b1.append("\nRow: " + rowHead.index);
            b1.append("\nCol: " + colHead.index);
            return b1.toString();
        }



    }

    // Constructor to create a SparseMatrix with the given number of
    // rows and columns with the given fillElem. The matrix starts out
    // empty: all elements are presumed to be the fillElem.
    //
    // Target Complexity: O(R+C)
    //   R: number of rows in the matrix
    //   C: number of cols in the matrix

    public SparseMatrix(int r, int c, T fillElem)
    {
        rows = r;
        cols = c;
        theRows = new ArrayList<Head<T>>();
        theCols = new ArrayList<Head<T>>();
        this.fillElem = fillElem;
        for(int i =0;i<rows;i++)
        {
            theRows.add(new Head<T>(i));
        }

        for(int i=0;i<cols;i++)
        {
            theCols.add(new Head<T>(i));
        }
        element_count =0;


    }

    // Constructor to create a 0 by 0 SparseMatrix with the given
    // fillElem
    public SparseMatrix(T fillElem)
    {
        rows = 0;
        cols = 0;
        theRows = new ArrayList<Head<T>>();
        theCols = new ArrayList<Head<T>>();
        element_count = 0;
        this.fillElem = fillElem;

    }

    // Return the number of non-fill elements in the matrix which have
    // been explictily set. Corresponds to the number of non-dummy
    // nodes.
    //
    // Target Complexity: O(1)

    public int elementCount()
    {
        return element_count;
    }

    // Return the number of rows in the Matrix which is the last indexed
    // row+1.
    //
    // Target Complexity: O(1)
    public int rows(){return rows;}


    // Return the number of cols in the Matrix which is the last indexed
    // col+1.
    //
    // Target Complexity: O(1)
    public int cols(){return cols;}

    // Return the fill element with which this matrix was initialized
    //
    // Target Complexity: O(1)

    public T getFillElem(){return this.fillElem;}

    // Add an empty row on to the bottom of the matrix.
    //
    // Target Complexity: O(1) amortized

    public void addRow()
    {
        this.theRows.add(new Head<T>(rows));
        rows++;
    }

    // Add an empty col on right side of the matrix.
    //
    // Target Complexity: O(1) amortized
    public void addCol()
    {
        this.theCols.add(new Head<T>(cols));
        cols++;
    }

    // Insert an empty row at position i. Later rows are "shifted down"
    // to a higher index.  Importantly, Nodes should not need
    // adjustments; only the Head indices should need alteration.
    //
    // Target Complexity: O(R)
    //   R: number of rows in the matrix

    public void insertRow(int i)
    {

        theRows.add(i,new Head<T>(i));
        /*
        Update the row value from all rows shifted down
         */
        rows++;
        for(int j=i+1;j<theRows.size();j++)
        {
            theRows.get(j).inc_index();
        }
    }

    // Insert an empty col at position i. Later cols are "shifted right"
    // to a higher index.  Importantly, Nodes should not need
    // adjustments; only the Head indices should need alteration.
    //
    // Target Complexity: O(C)
    //   C: number of cols in the matrix
    public void insertCol(int i)
    {
        theCols.add(i,new Head<T>(i));
        cols++;
        Head<T> h;
        for(int j=i+1;j<theCols.size();j++)
        {
            theCols.get(j).inc_index();
        }

    }

    // Retrieve the element at position (i,j) in the matrix. If the
    // position is out of bounds, throw an IndexOutOfBoundsException
    // with an appropriate message. Otherwise, access the target row or
    // column and walk the list to locate the element. If no node for
    // the element exists, return the fillElem for this
    // matrix. Otherwise return the data found in the target node.
    //
    // Target Complexity: O(E)
    //   E: number of non-fill elements in the matrix

    public T get(int i, int j)
    {

        if(i>this.rows()-1 || j>this.cols()-1)
        {
            throw new IndexOutOfBoundsException("One of your variables was outside of the matrix");
        }
        Head<T> rowhead = theRows.get(i);
        if(rowhead.nodes==null)
        {
            return fillElem;
        }
        Node<T> rownode = rowhead.nodes.right;
        while(rownode.cindex()!=j && rownode!=null)
        {
            rownode = rownode.right;
            if(rownode==null)
            {
                return fillElem;
            }
        }
        return rownode.data;
    }
    /*
    // Set element at position (i,j) to be x.
    //
    // If x is the fillElem, throw an IllegalArgumentException with an
    // appropriate message.
    //
    // If position (i,j) already has an element present, alter that
    // element.
    //
    // Otherwise, allocate a new node and link it into any existing
    // nodes by traversing the appropriate row and column lists.
    //
    // This method automatically expands the size of the matrix via
    // repeated calls to addRow() and addCol() to ensure that position
    // (i,j) is available. It does not throw any
    // IndexOutOfBoundsExceptions.  If i or j is negative, also throw an
    // IllegalArgumentException.
    //
    // Target complexity:
    // O(E) when (i,j) is in bounds
    // O(E+R+C) when (i,j) is out of bounds requiring expansion
    //   E: number of non-fill elements
    //   R: number of rows in the matrix
    //   C: number of cols in the matrix
    */
    public void set(int i, int j, T x) {
        if (i < 0 || j < 0) {
            throw new IllegalArgumentException("The value put in set was negative.");
        }
        if(x==fillElem)
        {
            throw new IllegalArgumentException("Cannot Assign Fill Element to Sparse Matrix");
        }
        //If there's not enough rows/cols to set add more
        /* Make the following rows | i - theRows.size() = How many to make | 1 - (1-1) = 1 | make 1 row */
        int make_row = i-(theRows.size()-1);
        int make_col = j-(theCols.size()-1);
        for(int m=0;m<make_row;m++)
        {
            addRow();
        }
        for(int n=0;n<make_col;n++)
        {
            addCol();
        }

        /* Create Node to be inserted */
        Node<T> insert_node = new Node<T>(x);
        insert_node.data = x;
        insert_node.rowHead = theRows.get(i);
        insert_node.colHead = theCols.get(j);
        Head<T> row_modify = theRows.get(i);
        Head<T> col_modify = theCols.get(j);
        Node<T> walk_node;
        Node<T> prev;
        /* If the node already exists just replace it */
        if(!get(i,j).equals(fillElem))
        {
            walk_node = row_modify.nodes.right;
            while(walk_node.cindex()!=j)
            {
                walk_node = walk_node.right;
            }
            walk_node.data = x;
            return;
        }
        /* if the row is empty */
        if(row_modify.nodes==null)
        {
            row_modify.nodes = new Node<T>();
            row_modify.nodes.right = insert_node;
        }
        else
        {
            /* If Not Empty*/
            walk_node = row_modify.nodes.right;
            prev = row_modify.nodes;
            while(j>walk_node.cindex() && walk_node!=null)
            {
                prev = walk_node;
                walk_node = walk_node.right;
                if(walk_node==null)
                {
                    break;
                }
            }
            /*
            Case 1: Walk is Null so insert it at prev and assign the right of insert to null
            Case 2: it is inserted in between with no issues
            */
            if(walk_node==null)
            {
                prev.right = insert_node;
                insert_node.right = walk_node;
            }
            else
            {
                prev.right = insert_node;
                insert_node.right = walk_node;
            }
        }
        /* if the col is empty */
        if(col_modify.nodes==null)
        {
            col_modify.nodes = new Node<T>();
            col_modify.nodes.down = insert_node;
            element_count++;
        }
        else
        {
            walk_node = col_modify.nodes.down;
            prev = col_modify.nodes;
            while(i>walk_node.rindex() && walk_node!=null)
            {
                prev = walk_node;
                walk_node = walk_node.down;
                if(walk_node==null)
                {
                    break;
                }
            }
            if(walk_node==null) //j will be put in last
            {
                prev.down = insert_node;
                insert_node.down = walk_node;
                element_count++;
            }
            else
            {
                prev.down = insert_node;
                insert_node.down = walk_node;
                element_count++;
            }
        }
        /* Insert the modified head nodes back into the rows/cols */
        theRows.set(i,row_modify);
        theCols.set(j,col_modify);
    }


    // Set the element at position (i,j) to be the fill element.
    // Internally this should remove any node at that position by
    // unlinking it from row and column lists.  If no data exists at
    // (i,j), no changes are made to the matrix.  If (i,j) is out of
    // bounds, throw an IndexOutOfBoundsException with an appropriate
    // message.
    //
    // Target Complexity: O(E)
    //   E: number of non-fill elements in the matrix

    public void setToFill(int i, int j)
    {
        if(i>theRows.size() || j>theCols.size())
        {
            throw new IndexOutOfBoundsException("I or J was out of bounds");
        }
        Head<T> row = theRows.get(i);
        Head<T> col = theCols.get(j);
        Node<T> curr,prev;
        curr = row.nodes.right;
        prev = row.nodes;

        /*
        Get The Head and the Node of the Row/Col to be modified
        */
        while(curr!=null)
        {
            //Break if we hit the end OR we match the col index with what's requested
            if(curr.cindex()==j)
            {
                prev.right = curr.right;
                curr = null;
                element_count--;
                return;
            }
            prev = curr;
            curr = curr.right;
        }
    }
    /*


    // Create a display version of the sparse matrix. Each element
    // (including fills) is shown in a grid of elements. Each element
    // will be in a field of width 5 characters with a space after
    // it. Using String.format("%5s ",el) is useful to create the
    // string.  Each row is on its own line.
    //
    // Example:
    // SparseMatrix<Double> x = new SparseMatrix<Double>(5,4, 0.0);
    // x.set(1,1, 1.0);
    // x.set(2,1, 2.0);
    // x.set(0,3, 3.0);
    // System.out.println(x);
    //   0.0   0.0   0.0   3.0
    //   0.0   1.0   0.0   0.0
    //   0.0   2.0   0.0   0.0
    //   0.0   0.0   0.0   0.0
    //   0.0   0.0   0.0   0.0
    //
    // Target Complexity: O(R*C)
    //   R: number of rows in the matrix
    //   C: number of cols in the matrix
    //   E: number of non-fill elements in the matrix
    // Note: repeated calls to get(i,j) will not adhere to this
    // complexity
*/
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        //Loop over each head in the row
        for(int row =0;row<theRows.size();row++)
        {

            Head<T> head = theRows.get(row);
            if(head.nodes==null)
            {
                //If there are no nodes intitalized then autofill with fillElem
                for(int fill=0;fill<theCols.size();fill++)
                {
                    //str.append(fillElem + " ");
                    str.append(String.format("%5s ",fillElem));
                }
                str.append("\n");
            }
            else
            {
                Node<T> n = head.nodes.right;
                int k=0;
                //Increment until it reaches the end of the nodes
                while(n!=null)
                {
                    for(;k<n.colHead.index;k++)
                    {
                        //str.append(fillElem + " ");
                        str.append(String.format("%5s ",fillElem));
                    }
                    //str.append(n.data + " ");
                    str.append(String.format("%5s ",n.data));
                    k = n.colHead.index + 1;
                    n = n.right;
                }
                //If there are remaining spaces fill them with fillElem
                while(k<theCols.size())
                {
                    //str.append(fillElem + " ");
                    str.append(String.format("%5s ",fillElem));
                    k++;
                }
                str.append("\n");
            }
        }
        return str.toString();
    }



    // Required but may simply return "".  This method will be called
    // and the string it produces will be reported when tests fail to
    // aid in viewing the internal state of the SparseMatrix for
    // debugging.

    public String debugString()
    {
        return "";
    }

    // Produce a List of all elements as triplets of (i,j,data). The
    // List may be any kind (ArrayList/LinkedList) and in any order.
    //
    // Target Complexity: O(R + E)
    //   R: number of rows in the matrix
    //   E: number of non-fill elements in the matrix
    // Note: repeated calls to get(i,j) will not adhere to this
    // complexity
    public List<Triple<Integer,Integer,T>> allElements()
    {
        List<Triple<Integer,Integer,T>> list = new ArrayList<>();
        Head<T> row;
        Node<T> walk;
        for(int r=0;r<theRows.size();r++)
        {
            row = theRows.get(r);
            if(row.nodes!=null)
            {
                walk = row.nodes.right;
                while(walk!=null)
                {
                    list.add(new Triple(walk.rindex(),walk.cindex(),walk.data));
                    walk = walk.right;
                }
            }
        }
        return list;
    }

    // Add two sparse matrices of Doubles together in an elementwise
    // fashion and produce another SparseMatrix as the result.  The fill
    // element of the resulting matrix is the sum of the two fill
    // elements from matrices x and y.  If the sum of any two elements
    // in the matrices equals the resulting fill element, that should
    // not occupy a node in the resulting sparse matrix.
    //
    // Matrices must have the same size (rows,cols) to be added. Throw
    // an IllegalArgumentException with an appropriate message if not.
    //
    // Target Complexity: O(R + C + Ex + Ey)
    //   R: Number of rows in matrices x and y
    //   C: Number of cols in matrices x and y
    //   Ex, Ey: Number of non-fill elements in matrix x and y
    // Memory constraint: O(1)
    //   The memory constraint does not count the size of x, y, or the
    //   result matrix which is returned.
    /*
    public static SparseMatrix<Double> addFast(SparseMatrix<Double> x, SparseMatrix<Double> y);

*/
    public static void main(String args[])
    {
        SparseMatrix<Integer> x = new SparseMatrix<Integer>(1,1,0);
        x.set(1,0,11);
        x.set(2,0,22);
        x.setToFill(2,0);
        x.set(2,0,33);
        System.out.println(x);/*
        x.setToFill(1,0);
        x.set(0,0,44);
        */
    }
}
