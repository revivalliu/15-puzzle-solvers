package AStar;

import java.awt.Point;
import java.util.Vector;

public class PuzzleNode implements Comparable {

    private PuzzleNode theParent = null; /*This Node Parent*/
    private int[][] puzzle;
    private Point spaceCell;
    private int movesFromStart = 0;     /*Node Depth (Optimize to Minimum That Number)*/
    private int movesToGoal = 0;        /*Estimate based on The heuristic*/
    private Vector<Point> validMoves;   /*Space Cell can (Valid Move) move to any of this Points*/

    
    public PuzzleNode(int[][] puzzle, PuzzleNode theParent, Point spaceCell) {

        /*Init The puzzle*/
        this.puzzle = puzzle;

        /*Init The Current Parent*/
        this.theParent = theParent;

        /*Init The Space Cell postion*/
        this.spaceCell = spaceCell;

        /*Calculate Current Valid moves*/
        this.validMoves = generateValidMoves();

        /*Update Current movesFromStart*/
        if (this.theParent != null) {
            movesFromStart = this.theParent.movesFromStart + 1;
        }

        /*Estimate moves to goal from current State*/
        this.movesToGoal = getMovesToGoal();
    }
            
    
    public Vector<Point> generateValidMoves() {

        /*The SpaceCell can move either left , right , up , down*/
        Vector<Point> result = new Vector<Point>();

        if (spaceCell.x - 1 >= 0) {
            result.add(new Point(spaceCell.x - 1, spaceCell.y));
        }
        if (spaceCell.x + 1 < PuzzleGame.puzzleDimension) {
            result.add(new Point(spaceCell.x + 1, spaceCell.y));
        }
        if (spaceCell.y - 1 >= 0) {
            result.add(new Point(spaceCell.x, spaceCell.y - 1));
        }
        if (spaceCell.y + 1 < PuzzleGame.puzzleDimension) {
            result.add(new Point(spaceCell.x, spaceCell.y + 1));
        }
        return result;
    }
   
    public int getMovesToGoal() {
        return PuzzleGame.getManahtanDistance(this);
    }

    public boolean isVisitedBefore() {

        /*If its the solution Then its ok to stuck on it*/
        if (this.movesToGoal == 0) {
            return false;
        }

        boolean isVisited = PuzzleGame.prevPos.contains(this);
        if (!isVisited) {
            PuzzleGame.prevPos.add(this); /*Not visited Then add it And return False*/
            return false;
        } else {
            return true; /*Visited Before*/
        }    
}

    public int[][] makeMoveOnPuzzle(Point moveToMake){        
        int[][] result = (int[][])getPuzzle().clone(); /*Copy Current*/
        result[spaceCell.x][spaceCell.y] = getPuzzle()[moveToMake.x][moveToMake.y];
        result[moveToMake.x][moveToMake.y] = 0;
        return result;        
    }
            
    
    
    public int compareTo(Object o) {

        PuzzleNode other = (PuzzleNode) o;
        for (int i = 0; i < getPuzzle().length; i++) {
            for (int j = 0; j < getPuzzle().length; j++) {
                int[][] otherPuzzle = other.getPuzzle();
                if (this.getPuzzle()[i][j] != otherPuzzle[i][j]) {
                    return 1; /*Diffrent*/
                }
            }
        }
        return 0; /*The Same*/
    }
          
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return (this.compareTo(other) == 0);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.getPuzzle() != null ? this.getPuzzle().hashCode() : 0);
        hash = 67 * hash + (this.spaceCell != null ? this.spaceCell.hashCode() : 0);
        return hash;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public int getNodeDepth(){
        int res = 0;
        PuzzleNode node = this;
        while (node != null) {
            node = node.theParent;
            res++;
        }
        return res;
    }
    
    
}
