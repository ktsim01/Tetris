package assignment;

import java.awt.Point;

/**
 * An abstraction for a Tetris board, which allows for querying it's state and
 * applying actions.
 */
public interface Board {

    /**
     * Possible results of applying a board action.
     */
    enum Result {
        /**
         * The action was a success (eg, applied successfuly).
         */
        SUCCESS,

        /**
         * The action would cause the piece to go out of bounds, or collide with
         * another piece.
         */
        OUT_BOUNDS,

        /**
         * There is no piece on the board to apply an action to.
         */
        NO_PIECE,

        /**
         * The last move caused a new piece to be placed.
         */
        PLACE
    }

    /**
     * The valid actions that can be taken on the board.
     */
    enum Action {
        /**
         * Attempt to move the piece one position to the left; this should never accidentally place
         * the piece (only DOWN and DROP can place a piece).
         */
        LEFT,

        /**
         * Attempt to move the piece one position to the right; this should never accidentally
         * place the piece (only DOWN and DROP can place a piece).
         */
        RIGHT,

        /**
         * Attempt to move the piece one position down; if this movement would cause the piece to
         * intersect with the stack, the piece should instead be placed at it's current position.
         */
        DOWN,

        /**
         * Attempt to drop the piece all the way, placing it wheverever it lands.
         */
        DROP,

        /**
         * Attempt to rotate the piece clockwise, applying wall-kicks if neccessary. If the wall kicks
         * could not be successfully applied, return Result.OUT_BOUNDS.
         */
        CLOCKWISE,

        /**
         * Attempt to rtate the piece counter-clockwise, applying wall-kicks if neccessary. If the
         * wall kicks could not be successfully applied, return Result.OUT_BOUNDS.
         */
        COUNTERCLOCKWISE,

        /**
         * Do nothing.
         */
        NOTHING,

        /**
         * "Hold" a piece until a later time, or "unhold" the piece so it
         * returns to play.
         *
         * Used only in karma; you can treat this as "NOTHING" if you are not
         * implementing it.
         */
        HOLD
    }

    /**
     * Applies the given action to the board, mutating it's state.
     */
    Result move(Action act);

    /**
     * Returns a new board whose state is equal to what the state of this
     * board would be after the input Action. This operation does not mutate
     * the current board, making it useful for speculatively testing actions
     * in an AI implementation.
     */
    Board testMove(Action act);

    /**
     * Return the current piece on the board, or null if there is no current piece.
     */
    Piece getCurrentPiece();

    /**
     * Return the position of the lower-left hand corner of the current piece's
     * bounding box, or null if there is no current piece.
     * 
     * Note that, in the board coordinate system, (0, 0) is the lower left hand corner
     * of the board, where Y increases upwards and X increases to the left. Due to SRS
     * bounding boxes being bigger than the piece, the "current piece position" may technically
     * be outside the bounds of the grid even if the piece itself is in bounds.
     */
    Point getCurrentPiecePosition();

    /**
     * Give a piece to the board to use as its next piece, placing the lower left hand corner
     * of the piece's bounding box at the given position.
     *
     * If the piece would be placed in a position that would cause it to intersect with existing pieces
     * or go out of the bounds of the board, an {@link IllegalArgumentException} should be thrown.
     */
    void nextPiece(Piece p, Point startingPosition);

    /**
     * Return true if the given object is equal to this object. You are free to assume that the
     * other object is another board; for safety, you should probably verify before casting
     * with an instanceof, though!
     *
     * This should measure the "semantic equality" of two boards - they should be considered equal
     * if they have the same current piece in the same position and have the same grid, but should
     * be oblivious to other internal state like the last action taken/last result returned.
     */
    boolean equals(Object other);

    /**
     * Returns the result of the last action given to the board.
     */
    Result getLastResult();

    /**
     * Returns the last action given to the board.
     */
    Action getLastAction();

    /**
     * Returns the number of rows cleared by the last action; should return 0 if no rows
     * were cleared.
     */
    int getRowsCleared();

    /**
     * Returns the width of the board in blocks.
     */
    int getWidth();

    /**
     * Returns the height of the board in blocks.
     */
    int getHeight();

    /**
     * Returns the max column height present in the board (as computed by {@link getColumnHeight(int)}).
     * For an empty board this is 0. This should not take the current piece into consideration.
     */
    int getMaxHeight();

    /**
     * Given a piece and an x, returns the y value where the piece would come to
     * rest if it were dropped straight down at that x.
     */
    int dropHeight(Piece piece, int x);

    /**
     * Returns the height of the given column -- i.e. the y value of the highest
     * block + 1.  The height is 0 if the column contains no blocks; this should
     * not take the current piece into consideration.
     */
    int getColumnHeight(int x);

    /**
     * Returns the number of filled blocks in the given row; this should not take
     * the current piece into consideration.
     */
    int getRowWidth(int y);

    /**
     * Returns the type of piece that exists at the given position (due to being placed there)
     * - if no placed piece exists at the given position or the given position is out of
     * bounds, return null.
     *
     * This method should not take the current piece into consideration - i.e., calling
     * `getGrid` on a position where the current piece it should simply return null.
     *
     * Note that, in the board coordinate system, (0, 0) is the lower left hand corner
     * of the board, where Y increases upwards and X increases to the left.
     */
    Piece.PieceType getGrid(int x, int y);
}
