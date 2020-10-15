package assignment;

import java.awt.*;
import java.util.Arrays;

import assignment.Piece.PieceType;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    
	private Piece.PieceType[][] board;
	private Piece curPiece;
	private Point curPos;
	private int width, height, rowClearCnt, maxHeight;
	private int[] heights, widths;
	private Action lastAction;
	private Result lastResult;

    public TetrisBoard(int width, int height) {
    	board = new Piece.PieceType[height][width];
    	this.width = width;
    	this.height = height;
    	this.heights = new int[width];
    	this.widths = new int[height];
    }

    // Copies the information of an entire board
    private TetrisBoard(TetrisBoard copy) {
    	width = copy.getWidth();
    	height = copy.getHeight();
    	rowClearCnt = copy.getRowsCleared();
    	curPiece = copy.getCurrentPiece();
    	curPos = copy.getCurrentPiecePosition();
    	lastAction = copy.getLastAction();
    	lastResult = copy.getLastResult();
    	maxHeight = copy.getMaxHeight();
    	
        //deep copy to avoid pointer issues
    	board = new Piece.PieceType[height][];
    	for(int y = 0; y < height; y++)
    		board[y] = Arrays.copyOf(copy.board[y], width);
    	
    	heights = Arrays.copyOf(copy.heights, width);
    	widths = Arrays.copyOf(copy.widths, height);
    }
    
    // Checks if a given piece and position will collide with anything
    private boolean checkPieceCollision(Piece piece, Point pos) {
    	Point[] body = piece.getBody();

        // Make sure each point in the body is empty
    	for(Point bodyPoint : body)
    		if(checkPointCollision(new Point(pos.x+bodyPoint.x, pos.y+bodyPoint.y)))
    			return true;
    	return false;
    }
    
    // Checks if a given position contains a block (used for collisions)
    private boolean checkPointCollision(Point pos) {
    	int x = pos.x, y = pos.y;
    	if(x < 0 || x >= width || y < 0 || y >= height)
    		return true;
    	return getGrid(x,y) != null;
    }
    

    // Gets kick tables as provided by Piece interface
    private static Point[] getClockwiseKickTable(Piece p) {
    	if(p.getType() == Piece.PieceType.STICK)
    		return Piece.I_CLOCKWISE_WALL_KICKS[p.getRotationIndex()];
    	
		return Piece.NORMAL_CLOCKWISE_WALL_KICKS[p.getRotationIndex()];
    }
    private static Point[] getCounterclockwiseKickTable(Piece p) {
    	if(p.getType() == Piece.PieceType.STICK)
    		return Piece.I_COUNTERCLOCKWISE_WALL_KICKS[p.getRotationIndex()];
    	
		return Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[p.getRotationIndex()];
    }
    
    // Scans through board and clears lines, shifting when appropriate and updating count
    private void clearLines() {
        //how far to shift each line
    	int shiftY = 0;
    	for(int y = 0; y < height; y++) {
            //check if a line is completely filled
    		if(getRowWidth(y) == width) {
                //update clear count and shift
    			shiftY++;
    			rowClearCnt++;
    		} else {
                //shift row down, and replace the old row with an empty one
    			Piece.PieceType[] row = board[y];
    			board[y] = new Piece.PieceType[width];
    			board[y-shiftY] = row;
    		}
    	}
    }
    
    private void updateValues() {
    	//iterate over each column and update the height
    	for(int x = 0; x < width; x++) {
    		boolean found = false;
	        //loop until we find a nonempty square
	    	for(int y = height-1; y >= 0; y--) {
	    		if(getGrid(x,y) != null) {
	    			heights[x] = y+1;
	    			found = true;
	    			break;
	    		}
	    	}
	    	//the column was entirely empty
	    	if(!found)
	    		heights[x] = 0;
    	}
    	maxHeight = 0;
    	for(int x = 0; x < width; x++)
    		maxHeight = Math.max(heights[x], maxHeight);
    	
    	//iterate over each row and update the width
    	for(int y = 0; y < height; y++) {
	    	widths[y] = 0;
	        //count nonempty cells in the row
	    	for(int x = 0; x < width; x++)
	    		if(getGrid(x,y) != null)
	    			widths[y]++;
    	}
    }
    
    private void placePiece() {
    	Point[] body = curPiece.getBody();
    	Piece.PieceType type = curPiece.getType();
    	for(Point p : body) {
            //combine relative locations with the location of bounding box
    		int x = p.x + curPos.x;
    		int y = p.y + curPos.y;
    		board[y][x] = type;
    	}
    	curPiece = null;
    	curPos = null;
    }
    
    @Override
    public Result move(Action act) {
        //ensure that there is a piece to work with
    	Result res = null;
    	if(curPiece == null)
    		res = Result.NO_PIECE;
    	else {
	    	switch(act) {
	    	case CLOCKWISE:
	    		Piece rotated = curPiece.clockwisePiece();
	    		Point[] kicks = getClockwiseKickTable(curPiece);
	    		
	    		boolean foundLoc = false;
	            //look through the kick table and find a suitable situation
	    		for(Point p : kicks) {
	    			Point newPos = new Point(curPos.x + p.x, curPos.y + p.y);
	    			if(!checkPieceCollision(rotated, newPos)) {
	                    //update current piece/position information
	    				curPiece = rotated;
	    				curPos = newPos;
	    				foundLoc = true;
	    				break;
	    			}
	    		}
	    		
	            //if no location was found, then this is out of bounds
	    		if(foundLoc) res = Result.SUCCESS;
	    		else res = Result.OUT_BOUNDS;
	    		break;
	    		
	    	case COUNTERCLOCKWISE:
	    		rotated = curPiece.counterclockwisePiece();
	    		kicks = getCounterclockwiseKickTable(curPiece);
	
	    		foundLoc = false;
	            //look through the kick table and find a suitable situation
	    		for(Point p : kicks) {
	    			Point newPos = new Point(curPos.x + p.x, curPos.y + p.y);
	    			if(!checkPieceCollision(rotated, newPos)) {
	                    //update current piece/position information
	    				curPiece = rotated;
	    				curPos = newPos;
	    				foundLoc = true;
	    				break;
	    			}
	    		}
	
	            //if no location was found, then this is out of bounds
	    		if(foundLoc) res = Result.SUCCESS;
	    		else res = Result.OUT_BOUNDS;
	    		break;
	    	
	        //move the piece in the appropriate direction and check if a collision occurs
	    	case LEFT:
	    		if(checkPieceCollision(curPiece, new Point(curPos.x-1, curPos.y))) {
	    			res = Result.OUT_BOUNDS;
	    		} else {
		    		curPos = new Point(curPos.x-1, curPos.y);
		    		res = Result.SUCCESS;
	    		}
	    		break;
	    		
	    	case RIGHT:
	    		if(checkPieceCollision(curPiece, new Point(curPos.x+1, curPos.y)))
	    			res = Result.OUT_BOUNDS;
	    		else {
		    		curPos = new Point(curPos.x+1, curPos.y);
		    		res = Result.SUCCESS;
	    		}
	    		break;
	    	case DOWN:
	    		if(checkPieceCollision(curPiece, new Point(curPos.x, curPos.y-1))) {
	    			placePiece();
	    			res = Result.PLACE;
	    		} else {
		    		curPos = new Point(curPos.x, curPos.y-1);
		    		res = Result.SUCCESS;
	    		}
	    		break;
	    		
	    	case DROP:
	    		int yOffset = 0;
	            //continually move the piece down until it collides with something
	    		while(true) {
	                //upon collision, move back one more and place the piece
	    			if(checkPieceCollision(curPiece, new Point(curPos.x, curPos.y-yOffset))) {
	    				curPos = new Point(curPos.x, curPos.y-yOffset+1);
	    				placePiece();
	    				res = Result.SUCCESS;
	    				break;
	    			}
	    			yOffset++;
	    		}
	    		break;
	    	case NOTHING:
	    		res = Result.SUCCESS;
	    		break;
	    		
	        //provide error message when an unknown action occurs
	    	default:
	    		System.err.println("Action " + act + " not supported.");
	    		res = Result.SUCCESS;
	    	}
    	}
 
        //always check if lines can be cleared
    	clearLines();
    	
    	//update heights after line clears
    	updateValues();

        //update the most recent action and result
    	lastAction = act;
    	lastResult = res;
    	
        //return result
    	return res;
    }

    @Override
    public Board testMove(Action act) {
        //create a new board with the copy, and change its state using move
    	Board newBoard = new TetrisBoard(this);
    	newBoard.move(act);
    	return newBoard;
    }

    @Override
    public Piece getCurrentPiece() {
    	return curPiece;
    }

    @Override
    public Point getCurrentPiecePosition() {
    	return curPos;
    }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
    	if(checkPieceCollision(p, spawnPosition))
    		throw new IllegalArgumentException("Piece could not be spawned");
    	
    	curPiece = p;
    	curPos = spawnPosition;
    }

    @Override
    public boolean equals(Object other) {
        //must be a board
    	if(!(other instanceof Board)) return false;

        //compare current piece
    	Board b = (Board) other;
    	if(curPiece == null) return b.getCurrentPiece() == null;
    	if(!curPiece.equals(b.getCurrentPiece())) return false;

        //compare current piece position
    	if(curPos == null) return b.getCurrentPiecePosition() == null;
    	if(!curPos.equals(b.getCurrentPiecePosition())) return false;

        //compare dimensions
    	if(b.getWidth() != getWidth()) return false;
    	if(b.getHeight() != getHeight()) return false;

        //compare items in the grid
    	for(int x = 0; x < width; x++)
    			for(int y = 0; y < height; y++)
    				if(getGrid(x,y) != b.getGrid(x, y)) return false;
    	return true;
    }

    @Override
    public Result getLastResult() {
    	return lastResult;
    }

    @Override
    public Action getLastAction() {
    	return lastAction;
    }

    @Override
    public int getRowsCleared() {
    	return rowClearCnt;
    }

    @Override
    public int getWidth() {
    	return width;
    }

    @Override
    public int getHeight() {
    	return height;
    }

    @Override
    public int getMaxHeight() {
    	//use precomputed values;
    	return maxHeight;
    }

    @Override
    public int dropHeight(Piece piece, int x) {
    	int[] skirt = piece.getSkirt();
    	int maxHeight = 0;
        
        //find maxHeight of the bounding box position using both skirt and column heights
    	for(int i = 0; i < skirt.length; i++)
    		maxHeight = Math.max(maxHeight, getColumnHeight(x+i) - skirt[i]);
    	return maxHeight;
    }

    @Override
    public int getColumnHeight(int x) {
    	//use precomputed values
    	return heights[x];
    }

    @Override
    public int getRowWidth(int y) {
    	return widths[y];
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
    	if(x < 0 || y < 0 || x >= width || y >= height)
    		return null;
    	return board[y][x]; 
    }
    
}
