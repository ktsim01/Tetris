package assignment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * This is the interface - you should implement TetrisPiece.
 */
public interface Piece {

    /**
     * The possible piece types.
     */
    public enum PieceType {
        /**
         * The T tetromino.
         */
        T(new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) }, 3, 3, new Color(138, 43, 223)),

        /**
         * The square tetromino (also called the "O" tetromino).
         */
        SQUARE(new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }, 2, 2, Color.YELLOW),

        /**
         * The stick tetromino (also called the "I" tetromino).
         */
        STICK(new Point[] { new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2) }, 4, 4, Color.CYAN),

        /**
         * The left-facing "L" tetromino.
         */
        LEFT_L(new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) }, 3, 3, Color.BLUE),

        /**
         * The right-facing "L" tetromino.
         */
        RIGHT_L(new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) }, 3, 3, Color.ORANGE),

        /**
         * The left dog tetromino (also called the "Z" tetromino).
         */
        LEFT_DOG(new Point[] { new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(2, 1) }, 3, 3, Color.RED),

        /**
         * The right dog tetromino (also called the "S" tetromino).
         */
        RIGHT_DOG(new Point[] { new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2) }, 3, 3, Color.GREEN);

        // The body of the tetromino; these points are relative to the lower-left hand corner
        // of the bounding box of the piece, and represent the piece in it's spawn position.
        private Point[] spawnBody;

        // The color of this tetromino.
        private Color color;

        // The width and height of the SRS bounding box.
        private int boundingBoxWidth, boundingBoxHeight;

        // Private constructor which provides the body.
        PieceType(Point[] body, int width, int height, Color color) {
            this.spawnBody = body;
            this.boundingBoxWidth = width;
            this.boundingBoxHeight = height;
            this.color = color;
        }

        /**
         * Return the body points of the tetromino in it's spawn state. These
         * points are relative to the lower-left corner of the bounding box of
         * the piece.
         */
        public Point[] getSpawnBody() { return spawnBody; }

        /**
         * Return a dimension representing the total size of the SRS bounding box.
         */
        public Dimension getBoundingBox() { return new Dimension(boundingBoxWidth, boundingBoxHeight); }

        /**
         * Return the color of this tetromino type.
         */
        public Color getColor() { return color; }
    }

    /**
     * Contains a list of 5 positions that should be checked for clockwise wall kicks of the J, L, S, T, and Z
     * tetromonies, per the wall kick specification.  This list is indexed by the SOURCE rotation index (i.e., if you're
     * rotating O -> R, the source rotation index is 0).
     */
    public static final Point[][] NORMAL_CLOCKWISE_WALL_KICKS = new Point[][] {
        // 0->R 	( 0, 0) 	(-1, 0) 	(-1,+1) 	( 0,-2) 	(-1,-2)
        new Point[] { new Point(0, 0), new Point(-1, 0), new Point(-1, 1), new Point(0, -2), new Point(-1, -2) },
        // R->2 	( 0, 0) 	(+1, 0) 	(+1,-1) 	( 0,+2) 	(+1,+2)
        new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 2), new Point(1, 2) },
        // 2->L 	( 0, 0) 	(+1, 0) 	(+1,+1) 	( 0,-2) 	(+1,-2)
        new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, -2), new Point(1, -2) },
        // L->0 	( 0, 0) 	(-1, 0) 	(-1,-1) 	( 0,+2) 	(-1,+2)
        new Point[] { new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, 2), new Point(-1, 2) }
    };

    /**
     * Contains the list of 5 positions that should be checked for counterclockwise wall kicks of the J, L, S, T, and Z
     * tetrominoes, per the wall kick specification. This list is indexed by the SOURCE rotation index (i.e., if you're
     * rotation O -> L, the source rotation index is 0).
     */
    public static final Point[][] NORMAL_COUNTERCLOCKWISE_WALL_KICKS = new Point[][] {
        // 0->L 	( 0, 0) 	(+1, 0) 	(+1,+1) 	( 0,-2) 	(+1,-2)
        new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, -2), new Point(1, -2) },
        // R->0 	( 0, 0) 	(+1, 0) 	(+1,-1) 	( 0,+2) 	(+1,+2)
        new Point[] { new Point(0, 0), new Point(1, 0), new Point(-1, -1), new Point(0, 2), new Point(1, 2) },
        // 2->R 	( 0, 0) 	(-1, 0) 	(-1,+1) 	( 0,-2) 	(-1,-2)
        new Point[] { new Point(0, 0), new Point(-1, 0), new Point(-1, 1), new Point(0, -2),  new Point(-1, -2) },
        // L->2 	( 0, 0) 	(-1, 0) 	(-1,-1) 	( 0,+2) 	(-1,+2)
        new Point[] { new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, 2), new Point(-1, 2) },
    };

    /**
     * Contains the list of 5 positions that should be checked for clockwise wall kicks for the I/stick tetromino, per the
     * wall kick specifications. This list is indexed by the SOURCE rotation index (i.e,. if your rotation O -> R, the
     * source rotation index is 0).
     */
    public static final Point[][] I_CLOCKWISE_WALL_KICKS = new Point[][] {
        // 0->R 	( 0, 0) 	(-2, 0) 	(+1, 0) 	(-2,-1) 	(+1,+2)
        new Point[] { new Point(0, 0), new Point(-2, 0), new Point(1, 0), new Point(-2, -1), new Point(1, 2) },
        // R->2 	( 0, 0) 	(-1, 0) 	(+2, 0) 	(-1,+2) 	(+2,-1)
        new Point[] { new Point(0, 0), new Point(-1, 0), new Point(2, 0), new Point(-1, 2), new Point(2, 1) },
        // 2->L 	( 0, 0) 	(+2, 0) 	(-1, 0) 	(+2,+1) 	(-1,-2)
        new Point[] { new Point(0, 0), new Point(2, 0), new Point(-1, 0), new Point(2, 1), new Point(-1, -2) },
        // L->0 	( 0, 0) 	(+1, 0) 	(-2, 0) 	(+1,-2) 	(-2,+1)
        new Point[] { new Point(0, 0), new Point(1, 0), new Point(-2, 0), new Point(1, -2), new Point(-2, 1) }
    };

    /**
     * Contains the list of 5 positions that should be checked for counterclockwise wall kicks for the I/stick tetromino, per
     * the wall kick specifications. This list is indexed by the SOURCE rotation index (i.e., if your rotation is O ->
     * L, the source rotation index is 0).
     */
    public static final Point[][] I_COUNTERCLOCKWISE_WALL_KICKS = new Point[][] {
        // 0->L 	( 0, 0) 	(-1, 0) 	(+2, 0) 	(-1,+2) 	(+2,-1)
        new Point[] { new Point(0, 0), new Point(-1, 0), new Point(2, 0), new Point(-1, 2), new Point(2, 1) },
        // R->0 	( 0, 0) 	(+2, 0) 	(-1, 0) 	(+2,+1) 	(-1,-2)
        new Point[] { new Point(0, 0), new Point(2, 0), new Point(-1, 0), new Point(2, 1), new Point(-1, -2) },
        // 2->R 	( 0, 0) 	(+1, 0) 	(-2, 0) 	(+1,-2) 	(-2,+1)
        new Point[] { new Point(0, 0), new Point(1, 0), new Point(-2, 0), new Point(1, -2), new Point(-2, 1) },
        // L->2 	( 0, 0) 	(-2, 0) 	(+1, 0) 	(-2,-1) 	(+1,+2)
        new Point[] { new Point(0, 0), new Point(-2, 0), new Point(1, 0), new Point(-2, -1), new Point(1, 2) }
    };

    /**
     * Returns the type of this piece.
     */
    PieceType getType();

    /**
     * Return the rotation index of this piece, which is the index of it's current rotation state.
     * An index of 0 refers to the "spawn" orientation, an index of 1 refers to one clockwise
     * rotation of the "spawn" orientation, an index of 2 refers to two clockwise rotations, and
     * an index of 3 refers to three clockwise rotations.
     * 
     * Indexes should always fall within [0, 3], and wrap around; i.e., rotating index
     * 3 clockwise produces rotation index 0, and rotating index 0 counterclockwise produecs rotation
     * index 3.
     */
    int getRotationIndex();

    /**
     * Returns a piece that is 90 degrees clockwise rotated from this piece,
     * according to the Super Rotation System.
     * 
     * This method should not mutate this piece, but rather return a different piece object.
     */
    Piece clockwisePiece();

    /**
     * Returns the piece that is 90 degrees counterclockwise rotated from this piece,
     * according to the Super Rotation System.
     * 
     * This method should not mutate this piece, but rather return a different piece object.
     */
    Piece counterclockwisePiece();

    /**
     * Returns the width of the piece's SRS bounding box, measured in blocks.
     */
    int getWidth();

    /**
     * Returns the height of the piece's SRS bounding box, measured in blocks.
     */
    int getHeight();

    /**
     * Returns the points in the piece's body, relative to the lower-left hand corner
     * of the piece's SRS bounding box.
     * 
     * These points should reflect the current rotation of the piece - the body
     * of a stick, and a 90-degree clockwise rotated stick are different!
     */
    Point[] getBody();

    /**
     * Returns the piece's skirt. For each x value across the piece, the skirt
     * gives the lowest y value in the body relative to the bottom of the SRS
     * bounding box. If there is no block in a given column, the skirt for that column
     * should be Integer.MAX_VALUE.
     */
    int[] getSkirt();

    /**
     * Returns true if two pieces are the same - they are the same type and rotation.
     */
    boolean equals(Object other);
}
