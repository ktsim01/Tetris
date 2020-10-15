package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import java.util.*;
import assignment.*;
import java.awt.*;

@RunWith(Parameterized.class)
public class RotationTest {

	private Piece piece;
	private Piece.PieceType type;
	private int rotIndex, width, height;
	private HashSet<Point> body;
	private int[] skirt;

	public RotationTest(Piece.PieceType type, int rotIndex, int width, int height, Point[] body, int[] skirt) {
		this.type = type;
		this.rotIndex = rotIndex;
		this.width = width;
		this.height = height;
		this.body = new HashSet<>();
		for (Point p : body)
			this.body.add(p);
		this.skirt = skirt;

		piece = new TetrisPiece(type);
		for (int i = 0; i < rotIndex; i++)
			piece = piece.clockwisePiece();
	}

	@Parameters
	public static Collection<Object[]> getTestData() {
		Object[][] data = new Object[][] {
				// Square testing
				{ Piece.PieceType.SQUARE, 0, 2, 2,
						new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
						new int[] { 0, 0 } },
				{ Piece.PieceType.SQUARE, 1, 2, 2,
						new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
						new int[] { 0, 0 } },
				{ Piece.PieceType.SQUARE, 2, 2, 2,
						new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
						new int[] { 0, 0 } },
				{ Piece.PieceType.SQUARE, 3, 2, 2,
						new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
						new int[] { 0, 0 } },



				// T testing
				{ Piece.PieceType.T, 0, 3, 3,
						new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
						new int[] { 1, 1, 1 } },
				{ Piece.PieceType.T, 1, 3, 3,
						new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 1) },
						new int[] { Integer.MAX_VALUE, 0, 1 } },
				{ Piece.PieceType.T, 2, 3, 3,
						new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 0) },
						new int[] { 1, 0, 1 } },
				{ Piece.PieceType.T, 3, 3, 3,
						new Point[] { new Point(0, 1), new Point(1, 0), new Point(1, 1), new Point(1, 2) },
						new int[] { 1, 0, Integer.MAX_VALUE } },

				// Stick testing
				{ Piece.PieceType.STICK, 0, 4, 4,
						new Point[] { new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2) },
						new int[] { 2, 2, 2, 2 } },
				{ Piece.PieceType.STICK, 1, 4, 4,
						new Point[] { new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3) },
						new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, 0, Integer.MAX_VALUE } },
				{ Piece.PieceType.STICK, 2, 4, 4,
						new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
						new int[] { 1, 1, 1, 1 } },
				{ Piece.PieceType.STICK, 3, 4, 4,
						new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
						new int[] { Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE } },

				// Left_l testing
				{ Piece.PieceType.LEFT_L, 0, 3, 3,
						new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
						new int[] { 1, 1, 1 } },
				{ Piece.PieceType.LEFT_L, 1, 3, 3,
						new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
						new int[] { Integer.MAX_VALUE, 0, 2 } },

				{ Piece.PieceType.LEFT_L, 2, 3, 3,
						new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
						new int[] { 1, 1, 0 } },

				{ Piece.PieceType.LEFT_L, 3, 3, 3,
						new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2) },
						new int[] { 0, 0, Integer.MAX_VALUE } },

				// Right_l testing
				{ Piece.PieceType.RIGHT_L, 0, 3, 3,
						new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
						new int[] { 1, 1, 1 } },
				{ Piece.PieceType.RIGHT_L, 1, 3, 3,
						new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) },
						new int[] { Integer.MAX_VALUE, 0, 0 } },
				{ Piece.PieceType.RIGHT_L, 2, 3, 3,
						new Point[] { new Point(0, 1), new Point(0, 0), new Point(1, 1), new Point(2, 1) },
						new int[] { 0, 1, 1 } },
				{ Piece.PieceType.RIGHT_L, 3, 3, 3,
						new Point[] { new Point(0, 2), new Point(1, 0), new Point(1, 1), new Point(1, 2) },
						new int[] { 2, 0, Integer.MAX_VALUE } },
				
				// Left_dog testing
				{ Piece.PieceType.LEFT_DOG, 0, 3, 3,
						new Point[] { new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(2, 1) },
						new int[] { 2, 1, 1 } },
				{ Piece.PieceType.LEFT_DOG, 1, 3, 3,
						new Point[] { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
						new int[] { Integer.MAX_VALUE, 0, 1 } },
				{ Piece.PieceType.LEFT_DOG, 2, 3, 3,
						new Point[] { new Point(0, 1), new Point(1,1), new Point(1, 0), new Point(2, 0) },
						new int[] { 1, 0, 0 } },
				{ Piece.PieceType.LEFT_DOG, 3, 3, 3,
						new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
						new int[] { 0, 1, Integer.MAX_VALUE } },
				
				// Right_dog testing
				{ Piece.PieceType.RIGHT_DOG, 0, 3, 3,
						new Point[] { new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
						new int[] { 1, 1, 2 } },
				{ Piece.PieceType.RIGHT_DOG, 1, 3, 3,
						new Point[] { new Point(1, 1), new Point(1, 2), new Point(2, 0), new Point(2, 1) },
						new int[] { Integer.MAX_VALUE, 1, 0 } },
				{ Piece.PieceType.RIGHT_DOG, 2, 3, 3,
						new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
						new int[] { 0, 0, 1 } },
				{ Piece.PieceType.RIGHT_DOG, 3, 3, 3,
						new Point[] { new Point(0, 2), new Point(0, 1), new Point(1, 1), new Point(1, 0) },
						new int[] { 1, 0, Integer.MAX_VALUE } },

		};
		return Arrays.asList(data);
	}

	@Test
	public void testType() {
		assertEquals(type, piece.getType());
	}

	@Test
	public void testRotIndex() {
		assertEquals(rotIndex, piece.getRotationIndex());
	}

	@Test
	public void testDimensions() {
		assertEquals(width, piece.getWidth());
		assertEquals(height, piece.getHeight());
	}

	@Test
	public void testBody() {
		Point[] testBody = piece.getBody();
		HashSet<Point> hs = new HashSet<>();
		for (Point p : testBody)
			hs.add(p);
		assertEquals(hs, body);
	}

	@Test
	public void testSkirt() {
		int[] testSkirt = piece.getSkirt();
		for (int i = 0; i < testSkirt.length; i++)
			assertEquals(skirt[i], testSkirt[i]);
	}
}
