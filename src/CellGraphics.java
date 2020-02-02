import java.awt.*;
import javax.swing.*;

public class CellGraphics extends JPanel {

	private Cell cell;
	private int row;
	private int col;
	private boolean valid = true;
	
	public static final int CELLSIZE = 60;
	public static final int CELLBORDER = 5;
	private static int col1X = 12;
	private static int col2X = 27;
	private static int col3X = 42;
	private static int row1Y = 20;
	private static int row2Y = 35;
	private static int row3Y = 50;
	private int[] numXPositions = {col1X, col2X, col3X, col1X, col2X, col3X, col1X, col2X, col3X};
	private int[] numYPositions = {row1Y, row1Y, row1Y, row2Y, row2Y, row2Y, row3Y, row3Y, row3Y};
	
	private Color squareColor;
	public static final Color HIGHLIGHTCOLOR = new Color(165, 123, 252);
	public static final Color REDCOLOR = new Color(206, 80, 75);
	public static final Color HINTCOLOR = new Color(255, 250, 119);
	
	public CellGraphics(int row, int col, Cell c) {
		cell = c;
		this.row = row;
		this.col = col;
		setBackground(new Color(138,204,255));
		setPreferredSize(new Dimension(CELLSIZE, CELLSIZE));
		squareColor = Color.WHITE;
	}
	
	public CellGraphics(CellGraphics cg) {
		cell = new Cell(cg.getCell());
		row = cg.getRow();
		col = cg.getCol();
		setBackground(new Color(138,204,255));
		setPreferredSize(new Dimension(CELLSIZE, CELLSIZE));
		squareColor = cg.getSquareColor();
	}
	
	public CellGraphics(Cell c) {
		cell = c;
		row = c.getRow();
		col = c.getCol();
		setBackground(new Color(138,204,255));
		setPreferredSize(new Dimension(CELLSIZE, CELLSIZE));
		squareColor = Color.WHITE;
	}

	public Cell getCell() {
		return cell;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public Color getSquareColor() {
		return squareColor;
	}
	
	public void highlight() {
		squareColor = HIGHLIGHTCOLOR;
		repaint();
	}
	
	public void highlightRed() {
		squareColor = REDCOLOR;
		repaint();
	}

	public void highlightHint() {
		squareColor = HINTCOLOR;
		repaint();
	}
	
	public void setValid(boolean bool) {
		valid = bool;
	}
	public boolean isValid() {
		return valid;
	}
	public void unhighlight() {
		squareColor = Color.WHITE;
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(squareColor);
		g.fillRoundRect(CELLBORDER, CELLBORDER, CELLSIZE-CELLBORDER*2, CELLSIZE-CELLBORDER*2, 10, 10);
//		g.fillRect(CELLBORDER, CELLBORDER, CELLSIZE-CELLBORDER*2, CELLSIZE-CELLBORDER*2);

		if (cell.isBig() && cell.getBigNum() != 0) {
			drawBig(g, cell.getBigNum());
		}
		else {
			boolean[] available = cell.getAvailable();
			for (int i = 0; i < available.length; i++) {
				if (available[i]) {
					drawSmall(g, i + 1);
				}
			}
		}
	}

	//takes num from 1 to 9
	public void drawSmall(Graphics g, int num) {
		g.setColor(new Color(200, 200, 200));
		g.setFont(new Font("Tahoma", Font.PLAIN, 12));
		int index = num - 1;
		g.drawString(Integer.toString(num), numXPositions[index], numYPositions[index]);
	}

	public void drawBig(Graphics g, int num) {
		g.setColor(new Color(0,0,0));
		if (!cell.isEditable()) {
			g.setFont(new Font("Tahoma", Font.BOLD, 30));
		}
		else {
			g.setFont(new Font("Tahoma", Font.PLAIN, 30));
		}
		g.drawString(Integer.toString(num), 22, 40);

	}
}