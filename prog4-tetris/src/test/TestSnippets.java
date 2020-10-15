package test;

/*
 * Various code snippets that were inserted into various classes.
 * Due to private methods, there was no easy way to test these with an external class.
 * Everything is commented out so no compile errors will clog up anything.
 */

/*

Tetris Piece tests
// TetrisPiece method to make sure both equality and the copy constructor work
public void assertConstructorCopy() {
	if(!map.get(type)[rotIndex].equals(this))
		System.out.println("Constructor copy check failed!");
	if(equals(!map.get(type)[rotIndex]))
		System.out.println("Constructor copy check failed!");
}


// TetrisPiece method to make sure rotations are handled properly
public void checkRotateCW() {
		Point[] arr = {new Point(0,0), new Point(0,1), new Point(0,2), new Point(1,0), new Point(1,1), new Point(1,2), new Point(2,0), new Point(2,1), new Point(2,2)};
		Point[] ans = rotateCW(arr, 3, 3);
		System.out.println(Arrays.toString(ans));
		// 0,0 -> 0,2
		// 0,1 -> 1,2
		// 0,2 -> 2,2
		// 1,0 -> 0,1
		// 1,1 -> 1,1
		// 1,2 -> 2,1
		// 2,0 -> 0,0
		// 2,1 -> 1,0
		// 2,2 -> 2,0	
}

*/

/*
TetrisBoard tests

// Snippet at the end of the copy constructor to make sure both equality and copy constructor work

    	System.out.println(equals(copy));

// Collision tests

public static void testCollisons() {
	//iterate over all pieces and rotations
	for(PieceType p : PieceType.values()) {
		Piece piece = new TetrisPiece(p);
		for(int rotIndex = 0; rotIndex < 4; rotIndex++) {
			
			//generate board for basic testing
			Dimension d = p.getBoundingBox();
			
			//basic collision test
			{
		    	TetrisBoard b = new TetrisBoard(d.width, d.height);
		    	for(Piece.PieceType[] a : b.board) Arrays.fill(a, p);
		    	
		    	//other piece collision
		    	if(!b.checkPieceCollision(piece, new Point(0,0)))
		    		System.out.println("Collision check failed!");
	
		    	//out of bounds collisions
		    	if(!b.checkPieceCollision(piece, new Point(-100,-100)))
		    		System.out.println("Collision check failed!");
		    	if(!b.checkPieceCollision(piece, new Point(-100,100)))
		    		System.out.println("Collision check failed!");
		    	if(!b.checkPieceCollision(piece, new Point(100,-100)))
		    		System.out.println("Collision check failed!");
		    	if(!b.checkPieceCollision(piece, new Point(100,100)))
		    		System.out.println("Collision check failed!");
		    	
		    	for(Point point : piece.getBody())
		    		b.board[point.y][point.x] = null;
		    	if(b.checkPieceCollision(piece, new Point(0,0)))
		    		System.out.println("Collision check failed!");
		    	
			}
			
			//clockwise kick test
	    	for(Point kick : getClockwiseKickTable(piece)) {
	    		TetrisBoard b = new TetrisBoard(d.width, d.height);
		    	for(Piece.PieceType[] a : b.board) Arrays.fill(a, p);
		    	for(Point point : piece.clockwisePiece().getBody())
		    		b.board[point.y][point.x] = null;
		    	b.curPiece = piece;
		    	b.curPos = new Point(-kick.x, -kick.y);
		    	if(b.move(Action.CLOCKWISE) != Result.SUCCESS)
		    		System.out.println("Clockwise kick failed!");
	    	}
			
			//counterclockwise kick test
	    	for(Point kick : getCounterclockwiseKickTable(piece)) {
	    		TetrisBoard b = new TetrisBoard(d.width, d.height);
		    	for(Piece.PieceType[] a : b.board) Arrays.fill(a, p);
		    	for(Point point : piece.counterclockwisePiece().getBody())
		    		b.board[point.y][point.x] = null;
		    	b.curPiece = piece;
		    	b.curPos = new Point(-kick.x, -kick.y);
		    	if(b.move(Action.COUNTERCLOCKWISE) != Result.SUCCESS)
		    		System.out.println("Counterclockwise kick failed!");
	    	}
	    		
	    	
	    	piece = piece.clockwisePiece();
		}
	}
}
*/