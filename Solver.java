import java.util.Comparator;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
public class Solver {
    private static final Comparator<Node> ByHamming = new byHamming();
//    private static final Comparator<Node> ByManhattan = new byManhattan();
    private MinPQ<Node> pq;
    private Node last;
    private boolean isSolvable;
    private class Node {
        public Board b;
        public int move;
        public boolean solvable;
        public Node previousNode;
        public int hamming;
        //public int manhattan;
        public Node(Board b, int move, boolean solvable, Node pn) {
            this.b = b;
            this.move = move;
            this.solvable = solvable;
            this.previousNode = pn;
            //this.manhattan = b.manhattan();
            this.hamming = b.hamming();
        }
    }
    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.IllegalArgumentException();
        pq = new MinPQ<Node>(ByHamming);
        pq.insert(new Node(initial, 0, true, null));
        pq.insert(new Node(initial.twin(), 0, false, null));
        while (true) {
            Node min = pq.delMin();
            if (min.b.isGoal()) {
                this.last = min;
                this.isSolvable = this.last.solvable;
                break;
            }
            for (Board neighbor : min.b.neighbors())
                if ((min.previousNode == null) || (!neighbor.equals(min.previousNode.b)))
                    pq.insert(new Node(neighbor, min.move + 1, min.solvable, min));
        }
    }
    private static class byHamming implements Comparator<Node> {
        public int compare (Node v, Node w) {
            return v.hamming + v.move - w.hamming - w.move + 1;
        }
    }
//    private static class byManhattan implements Comparator<Node> {
//        public int compare (Node v, Node w) {
//            return v.manhattan + v.move - w.manhattan - w.move + 1;
//        }
//    }
    public boolean isSolvable() {
        return this.isSolvable;
    }
    public int moves() {
        if (this.isSolvable)
            return this.last.move;
        else
            return -1;
    }
    public Iterable<Board> solution() {
        if (isSolvable) {
            Stack<Board> track = new Stack<Board>();
            Node current = this.last;
            while(current != null) {
                track.push(current.b);
                current = current.previousNode;
            }
            return track;
        }
        else
            return null;
    }
    public static void main(String[] args) {
     // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
