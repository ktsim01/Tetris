package assignment;

import java.awt.*;
import java.util.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {

	/**
	 * Construct a tetris piece of the given type. The piece should be in it's spawn
	 * orientation, i.e., a rotation index of 0.
	 * 
	 * You may freely add additional constructors, but please leave this one - it is
	 * used both in the runner code and testing code.
	 */
	private final PieceType type;
	private Piece nextCW;
	private Piece nextCCW;
	private final Point[] points;
	private final int width;
	private final int height;
	private final int rotIndex;
	private final int[] skirt;
	private static HashMap<PieceType, TetrisPiece[]> map;
	private static final int ROTATIONS = 4;
	
    //precompute all possible pieces and rotations
	static {
		map = new HashMap<>();
        //iterate over all piece types
		for (PieceType type : PieceType.values()) {
            //array for the different orientations
			TetrisPiece[] pieces = new TetrisPiece[ROTATIONS];

            //define the base piece using given constants in type
			int width = (int) type.getBoundingBox().getWidth();
			int height = (int) type.getBoundingBox().getHeight();
			Point[] spawnBody = type.getSpawnBody();
			pieces[0] = new TetrisPiece(type, 0, spawnBody, width, height, computeSkirt(spawnBody, width));

            //rotate the body for each rotated piece
			for (int i = 1; i < ROTATIONS; i++) {
				Point[] body = rotateCW(pieces[i - 1].getBody(), width, height);
				pieces[i] = new TetrisPiece(type, i, body, width, height, computeSkirt(body, width));
			}
		    
            //add the circular linkedlist links
			for (int i = 0; i < ROTATIONS; i++) {
				pieces[i].nextCW = pieces[(i + 1) % ROTATIONS];
				pieces[i].nextCCW = pieces[(i - 1 + ROTATIONS) % ROTATIONS];
			}

            //add the precomputed pieces to the map
			map.put(type, pieces);

		}
	}

    //rotate the points given the size of the bounding box
	private static Point[] rotateCW(Point[] arr, int width, int height) {
		Point[] rotated = new Point[arr.length];

		for (int i = 0; i < arr.length; i++) {
			int x = (int) arr[i].getX();
			int y = (int) arr[i].getY();

			rotated[i] = new Point(y, width - x - 1);

		}
		return rotated;
	}
	
	private static int[] computeSkirt(Point[] arr, int width) {
		int[] skirt = new int[width];
        //skirt initially "empty"
		Arrays.fill(skirt, Integer.MAX_VALUE);

		for (int i = 0; i < arr.length; i++) {
			int x = (int) arr[i].getX();
			int y = (int) arr[i].getY();
            //update the skirt for the column of the current point
			skirt[x] = Math.min(skirt[x], y);
		}
		return skirt;
	}

	public TetrisPiece(PieceType type) {
        //use other constructor
		this(type, 0);

	}
    
	private TetrisPiece(PieceType type, int rotIndex) {
        //copy information from the precomputed piece
		this.type = type;
		this.rotIndex = rotIndex;
		TetrisPiece copyPiece = map.get(type)[rotIndex];
		this.points = Arrays.copyOf(copyPiece.getBody(), copyPiece.getBody().length);
		this.width = copyPiece.getWidth();
		this.height = copyPiece.getHeight();
		this.nextCCW = copyPiece.counterclockwisePiece();
		this.nextCW = copyPiece.clockwisePiece();
		this.skirt = Arrays.copyOf(copyPiece.getSkirt(), copyPiece.getWidth());
		
	}

	private TetrisPiece(PieceType type, int rotIndex, Point[] arr, int width, int height, int[] skirt) {
        //set values based on provided parameters
		this.type = type;
		this.rotIndex = rotIndex;
		this.points = arr;
		this.width = width;
		this.height = height;
		this.skirt = skirt;
	}

	@Override
	public PieceType getType() {
		return type;
	}

	@Override
	public int getRotationIndex() {
		return rotIndex;
	}

	@Override
	public Piece clockwisePiece() {
		return nextCW;

	}

	@Override
	public Piece counterclockwisePiece() {
		return nextCCW;
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
	public Point[] getBody() {

		return points;
	}

	@Override
	public int[] getSkirt() {
		return skirt;
	}

	@Override
	public boolean equals(Object other) {
		// Ignore objects which aren't also tetris pieces.
		if (!(other instanceof TetrisPiece))
			return false;
		TetrisPiece otherPiece = (TetrisPiece) other;
        //type and rotIndex uniquely define a piece
		if (otherPiece.type.equals(this.type) && otherPiece.rotIndex == this.rotIndex) {
			return true;
		}
		return false;
	}
}
