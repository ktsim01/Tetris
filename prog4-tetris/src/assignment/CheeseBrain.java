package assignment;

import java.util.*;

public class CheeseBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;
    
    //how much each score should be weighted in the score calculation
    private static final double CHEESE_WEIGHT = 35;
    private static final double HEIGHT_WEIGHT = 5;
    private static final double ROW_WEIGHT = -46;
    private static final double TEXTURE_WEIGHT =5;
    private static final double HOLE_WEIGHT = 1;
    private static final double IDEAL_TEXTURE = 4;
    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
    	//printScore(currentBoard);
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateRotations(currentBoard, null);

        double best = Double.POSITIVE_INFINITY;
        int bestIndex = 0;

        // Check all of the options and get the one with the best score
        for (int i = 0; i < options.size(); i++) {
        	double score = scoreBoard(options.get(i));
            // best score is now the minimum score
            if (score < best) {
                best = score;
                bestIndex = i;
            }
        }
        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can move the piece to.
     */
    private void enumeratePositions(Board currentBoard, Board.Action previous) {
    	// We can always drop our current Piece
        options.add(currentBoard.testMove(Board.Action.DROP));
        if(previous == null) firstMoves.add(Board.Action.DROP);
        else firstMoves.add(previous);

        // Now we'll add all the places to the left we can DROP
        Board left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            if(previous == null) firstMoves.add(Board.Action.LEFT);
            else firstMoves.add(previous);
            left.move(Board.Action.LEFT);
        }

        // And then the same thing to the right
        Board right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            if(previous == null) firstMoves.add(Board.Action.RIGHT);
            else firstMoves.add(previous);
            right.move(Board.Action.RIGHT);
        }
    }
    
    /**
     * Test all rotations, and the result to the possible options.
     */
    private void enumerateRotations(Board currentBoard, Board.Action previous) {
    	//drop the piece
    	enumeratePositions(currentBoard, null);
    	
    	//consider all clockwise turns
    	Board clockwiseBoard = currentBoard.testMove(Board.Action.CLOCKWISE);
    	enumeratePositions(clockwiseBoard, Board.Action.CLOCKWISE);
    	clockwiseBoard = clockwiseBoard.testMove(Board.Action.CLOCKWISE);
    	enumeratePositions(clockwiseBoard, Board.Action.CLOCKWISE);
    	clockwiseBoard = clockwiseBoard.testMove(Board.Action.CLOCKWISE);
    	enumeratePositions(clockwiseBoard, Board.Action.CLOCKWISE);


    	//consider all counterclockwise turns
    	Board counterclockwiseBoard = currentBoard.testMove(Board.Action.COUNTERCLOCKWISE);
    	enumeratePositions(counterclockwiseBoard, Board.Action.COUNTERCLOCKWISE);
    	counterclockwiseBoard = counterclockwiseBoard.testMove(Board.Action.COUNTERCLOCKWISE);
    	enumeratePositions(counterclockwiseBoard, Board.Action.COUNTERCLOCKWISE);
    	counterclockwiseBoard = counterclockwiseBoard.testMove(Board.Action.COUNTERCLOCKWISE);
    	enumeratePositions(counterclockwiseBoard, Board.Action.COUNTERCLOCKWISE);
    }
    
    /**
     * Combine cheese score, texture score, and max height for the total score.
     */
    private double scoreBoard(Board newBoard) {
        return HEIGHT_WEIGHT*heightScore(newBoard)
        		+ CHEESE_WEIGHT*cheeseScore(newBoard) 
        		+ TEXTURE_WEIGHT*textureScore(newBoard) 
        		+ ROW_WEIGHT*rowScore(newBoard)  
        		+ HOLE_WEIGHT*holeScore(newBoard);
    }
    
    //print the weighted scores for a given board - debug method
    private void printScore(Board newBoard) {
    	System.out.println(HEIGHT_WEIGHT*heightScore(newBoard)
        		+ " " + CHEESE_WEIGHT*cheeseScore(newBoard) 
        		+ " " +  TEXTURE_WEIGHT*textureScore(newBoard) 
        		+ " " +  ROW_WEIGHT*rowScore(newBoard)  
        		+ " " +  HOLE_WEIGHT*holeScore(newBoard));
    }
    /*
     * Scores cheesiness of a board.
     */
    private double cheeseScore(Board newBoard) {
    	double score = 1;
    	int width = newBoard.getWidth(), height = newBoard.getHeight();
    	
    	//iterate over all columns
    	for(int x = 0; x < width; x++) {
    		
    		//maintain counts of filled cells and consecutive empty cells
    		int aboveCnt = 0;
    		int emptyCnt = 0;
    		for(int y = height-1; y >= 0; y--) {
    			//empty cell
    			if(newBoard.getGrid(x, y) == null) {
    				
    				//decreasing penalty for filled cells that cover this cheese
    				score += Math.sqrt(aboveCnt);
    				emptyCnt++;
    			} else {
    				//incorporate some penalty for long cheese holes
    				if(aboveCnt != 0) score += Math.sqrt(emptyCnt);
    				emptyCnt = 0;
    				aboveCnt++;
    			}
    		}
    	}
    	return score;
    }
    
    /*
     * Scores the number of holes in a board.
     */
    
    boolean[][] mat;
    boolean[][] vis;
    private double holeScore(Board newBoard) {
    	int holeCnt = 0;
    	int width = newBoard.getWidth();
    	int height = newBoard.getHeight();
    	
    	//instantiate some matrices for floodfill
    	mat = new boolean[width][height];
    	vis = new boolean[width][height];
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    			mat[i][j] = newBoard.getGrid(i, j) == null;
    	
    	//attempt to block off some of the uppermost empty space to avoid long interconnected holes
    	for(int j = height-1; j >= 0; j--) {
    		if(newBoard.getRowWidth(j) == 0)
    			for(int i = 0; i < width; i++)
    				vis[i][j] = true;
    		else
    			break;
    	}
    	
    	//floodfill to count distinct holes
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    			if(mat[i][j] && !vis[i][j]) {
    				holeCnt++;
    				fill(i,j);
    			}
    	
    	//square the result for increasing penalty with more holes
    	return holeCnt*holeCnt;
    }
    
    /*
     * Floodfill algorithm to visit every part of a hole
     */
    private void fill(int i, int j) {
    	if(i < 0 || j < 0 || i >= mat.length || j >= mat[0].length || !mat[i][j] || vis[i][j]) 
    		return;
    	vis[i][j] = true;
    	fill(i+1, j);
    	fill(i-1, j);
    	fill(i, j+1);
    	fill(i, j-1);
    }
    /*
     * Scores the texture of a board.
     */
    private double textureScore(Board newBoard) {
    	double score = 0;
    	
    	//compute squared difference of heights between each adjacent pair of columns
    	for(int i = 1; i < newBoard.getWidth(); i++)
    		score += Math.pow(Math.abs(newBoard.getColumnHeight(i) - newBoard.getColumnHeight(i-1)), 2);
    	
    	//take the difference between the score we found and the ideal score we want
    	//since 0 corresponds to a perfectly flat board, which is not necessarily ideal
    	return Math.abs(score - IDEAL_TEXTURE);
    }
    
    /*
     * Scores the number of rows cleared. More specifically, provides incentive to clear rows.
     */
    private double rowScore(Board newBoard) {
    	return newBoard.getRowsCleared();
    }
    
    /*
     * Scores how high the board is.
     */
    private double heightScore(Board newBoard) {
    	//if below half the board, do not incur too much score
    	if(newBoard.getMaxHeight() < newBoard.getHeight()/2) return newBoard.getMaxHeight();
    	
    	//otherwise, square the amount of lines above the halfway point to quickly increase penalty
    	return newBoard.getMaxHeight()+Math.pow(newBoard.getMaxHeight() - newBoard.getHeight()/2, 2);
    }
}
