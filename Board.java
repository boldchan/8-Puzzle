import edu.princeton.cs.algs4.Stack;
public class Board {
    private final char[] blocks;
    private final int dimension;
    private final int hamming, manhattan;
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new java.lang.IllegalArgumentException();
        this.dimension = blocks.length; 
        this.blocks = new char[this.dimension * this.dimension];
        for (int i = 0; i < this.blocks.length; i++) {
            this.blocks[i] = (char) blocks[i / this.dimension][i % this.dimension];
        }
        this.hamming = this.gethamming();
        this.manhattan = this.getmanhattan();
    }
    private Board(int[][] blocks, int manhattan, int hamming) {
        if (blocks == null)
            throw new java.lang.IllegalArgumentException();
        this.dimension = blocks.length; 
        this.blocks = new char[this.dimension * this.dimension];
        for (int i = 0; i < this.blocks.length; i++) {
            this.blocks[i] = (char) blocks[i / this.dimension][i % this.dimension];
        }
        this.manhattan = manhattan;
        this.hamming = hamming;
    }
    private int getEmptyPosition() {
        for (int i = 0; i < this.blocks.length; i++)
            if (blocks[i] == 0) {
                return i;
            }  
        return -1;
    }
    public int dimension() {
        return this.dimension;
    }
    private int getmanhattan() {
        int distance = 0;
        for (int i = 0; i < this.dimension * this.dimension; i++) {
                int target = (int) this.blocks[i];
                if (target > 0) {
                    distance += Math.abs(i / this.dimension - (target - 1) / this.dimension) + Math.abs(i % this.dimension - (target - 1) % this.dimension);
                }
            }
        return distance;
    }
    public int manhattan() {
        return this.manhattan;
    }
    private int gethamming() {
        int distance = 0;
        for (int i = 0; i < this.dimension * this.dimension; i++)
            if (this.blocks[i] != (char) (i + 1))
                distance++;
        return distance - 1;// exclude the blank square
    }
    public int hamming() {
        return this.hamming;
    }
    public boolean isGoal() {
        if (this.hamming == 0)
            return true;
        else
            return false;
    }
    public Board twin() {
        if ((this.blocks[0] != (char) 0) && this.blocks[1] != (char) 0)
            return swap(0, 1);
        else
            return swap(2, 3);
    }
    private Board swap(int pos1, int pos2) {
        int[][] newblock = new int[this.dimension][this.dimension];
        for (int i = 0; i < this.dimension; i++)
            for (int j = 0; j < this.dimension; j++)
                newblock[i][j] = (int) this.blocks[i * this.dimension + j];
        int temp;
        temp = newblock[pos1 / this.dimension][pos1 % this.dimension];
        newblock[pos1 /this.dimension][pos1 % this.dimension] = newblock[pos2 / this.dimension][pos2 % this.dimension];
        newblock[pos2 / this.dimension][pos2 % this.dimension] = temp;
        return new Board(newblock);
    }
    private Board exch(int pos, int empty) {
        int[][] newblock = new int[this.dimension][this.dimension];
        for (int i = 0; i < this.dimension; i++)
            for (int j = 0; j < this.dimension; j++)
                newblock[i][j] = (int) this.blocks[i * this.dimension + j];
        int temp;
        temp = newblock[pos / this.dimension][pos % this.dimension];
        newblock[pos /this.dimension][pos % this.dimension] = newblock[empty / this.dimension][empty % this.dimension];
        newblock[empty / this.dimension][empty % this.dimension] = temp;
        return new Board(newblock, this.manhattan + this.dmanhattan(pos, empty), this.hamming + this.dhamming(pos, empty));
    }
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if ((y.getClass() != this.getClass())) return false;
        Board b = (Board) y;
        if (this.dimension != b.dimension)
            return false;
        else {
            for (int i = 0; i < this.blocks.length; i++)
                    if (this.blocks[i] != b.blocks[i])
                        return false;
            return true;
        }
    }
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        int empty = this.getEmptyPosition();
        if (empty / this.dimension != 0) {
            neighbors.push(exch(empty - this.dimension, empty));//down
        }
        if (empty / this.dimension != this.dimension - 1) {
            neighbors.push(exch(empty + this.dimension, empty));//up
        }
        if (empty % this.dimension != this.dimension - 1) {
            neighbors.push(exch(empty + 1, empty));//left
        }
        if (empty % this.dimension != 0) {
            neighbors.push(exch(empty - 1, empty));//right
        }
        return neighbors;
    }
    private int dhamming(int pos, int empty) {
        return ((int) this.blocks[pos] == (empty + 1) ? 0 : 1)
                - ((int) this.blocks[pos] == pos + 1 ? 0 : 1);
    }
    private int dmanhattan(int pos, int empty) {
        int targetx, targety;
        targetx = ((int) this.blocks[pos] - 1) / this.dimension;
        targety = ((int) this.blocks[pos] - 1) % this.dimension;
        return (Math.abs(empty / this.dimension - targetx) + Math.abs(empty % this.dimension - targety) - Math.abs(pos / this.dimension - targetx) - Math.abs(pos % this.dimension - targety));
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dimension + "\n");
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                s.append(String.format("%4d ", (int) this.blocks[i * this.dimension + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    public static void main(String[] args) {
        int[][] test = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board b = new Board(test);
        for (Board neighbor : b.neighbors()) {
            System.out.println(neighbor.toString());
            System.out.println(Integer.toString(neighbor.hamming()));
        }
    }
}
