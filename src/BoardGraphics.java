import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

class BackPanel extends JPanel {

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageObserver a = null;

		//draws squares
		g.setColor(new Color(138,204,255));
		g.fillRoundRect(150, 150, 200, 200, 15, 15);
		g.fillRoundRect(375, 150, 200, 200, 15, 15);
		g.fillRoundRect(600, 150, 200, 200, 15, 15);

		g.fillRoundRect(150, 375, 200, 200, 15, 15);
		g.fillRoundRect(375, 375, 200, 200, 15, 15);
		g.fillRoundRect(600, 375, 200, 200, 15, 15);

		g.fillRoundRect(150, 600, 200, 200, 15, 15);
		g.fillRoundRect(375, 600, 200, 200, 15, 15);
		g.fillRoundRect(600, 600, 200, 200, 15, 15);

		g.fillRoundRect(1000, 150, 400, 650, 15, 15);
		//draws title
		g.drawImage((new ImageIcon(BoardGraphics.class.getResource("/newTitle.png"))).getImage(), 250, 10, a);
	}
}

public class BoardGraphics implements MouseListener, ActionListener {

	private JFrame frame;
	private GeneratePopup genPopup;
	private JPanel contentPane;
	private SpringLayout layout = new SpringLayout();
	private ButtonPanel buttonPanel;
	private CellGraphics[][] cellGs;
	private CellGraphics selectedCellG;
	private Board board;

	public static final int ENTRYMODE = 0;
	public static final int SOLVINGMODE = 1;
	private int gameMode = ENTRYMODE;
	private int numHints = 0;

	private ArrayList<CellGraphics[][]> pastStates = new ArrayList<CellGraphics[][]>();
	private ArrayList<CellGraphics[][]> futureStates = new ArrayList<CellGraphics[][]>();
	private ArrayList<Cell> hintsAdded = new ArrayList<Cell>();

	private XMLHandler xmlHandler = new XMLHandler();
	private String lastAction = "Entry Mode";

	private JButton hiddenGenOK = new JButton();
	private JButton hiddenGenCancel = new JButton();

	public static void main(String[] args) {
		BoardGraphics fml = new BoardGraphics(new Board());
	}

	public BoardGraphics(Board b){
		board = b;
		selectedCellG = null;
		initializeBoardAndCGs();

		frame = new JFrame("Sudoku");
		frame.addMouseListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = new BackPanel();
		buttonPanel = new ButtonPanel();
		buttonPanel.addButtonListeners(this);
		contentPane.add(buttonPanel);

		hiddenGenOK.setActionCommand("Generate OK");
		hiddenGenOK.addActionListener(this);
		hiddenGenCancel.setActionCommand("Generate Cancel");
		hiddenGenCancel.addActionListener(this);
		switchToEntry();

		contentPane.setLayout(layout);
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		contentPane.setBackground(new Color(99,172,229));
		layout.putConstraint(SpringLayout.NORTH, buttonPanel, 30, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, buttonPanel, 975, SpringLayout.WEST, contentPane);
		drawAllCells();
		frame.setContentPane(contentPane);

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(frame, 
						"Are you sure you want to exit the game? Any unsaved files will be lost.", "Exit Sudoku", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});
	}

	private void resetGraphics() {
		frame.getContentPane().removeAll();

		contentPane = new BackPanel();
		contentPane.add(buttonPanel);

		contentPane.setLayout(layout);
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		contentPane.setBackground(new Color(99,172,229));
		layout.putConstraint(SpringLayout.NORTH, buttonPanel, 30, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, buttonPanel, 975, SpringLayout.WEST, contentPane);
		drawAllCells();
		frame.setContentPane(contentPane);

		frame.revalidate();
		frame.repaint();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);

		repaintAll();
	}

	private void initializeBoardAndCGs() {
		board = new Board();
		cellGs = new CellGraphics[9][9];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				CellGraphics cellG = new CellGraphics(row, col, new Cell(row, col));
				board.setCell(row, col, cellG.getCell());
				cellGs[row][col] = cellG;
			}
		}
	}

	private void updateBoard() {
		/*board = new Board();
		board.setMode(gameMode);*/
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				board.setCell(row, col, cellGs[row][col].getCell());
			}
		}
	}

	private void updateCGs() {
		cellGs = new CellGraphics[9][9];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				CellGraphics cellG = new CellGraphics(row, col, board.getCell(row, col));
				cellGs[row][col] = cellG;
			}
		}
	}

	private void repaintAll() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				cellGs[row][col].repaint();
			}
		}
	}

	private void drawAllCells() {
		int[] cellXStarts = {110, 170, 230, 335, 395, 455, 560, 620, 680};
		int[] cellYStarts = {140, 200, 260, 365, 425, 485, 590, 650, 710};

		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				int xPos = cellXStarts[col];
				int yPos = cellYStarts[row];
				drawCell(cellGs[row][col], layout, contentPane, xPos, yPos);
			}
		}
	}

	private void drawCell(CellGraphics a, SpringLayout b, JPanel c, int x, int y) {
		b.putConstraint(SpringLayout.WEST, a, x, SpringLayout.WEST, contentPane);
		b.putConstraint(SpringLayout.NORTH, a, y, SpringLayout.NORTH, contentPane);
		c.add(a);
	}

	private void updateHighlights() {
		unhighlightAll();
		highlightInvalid();
		highlightSelected();
		highlightHints();

		repaintAll();
	}

	private void unhighlightAll() {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				cellGs[r][c].unhighlight();
			}
		}
	}

	private void highlightInvalid() {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				ArrayList<Cell> invalidCells = board.checkCellValid(board.getGameBoard(), board.getCell(r, c));
				if (invalidCells.size() > 0) {
					cellGs[r][c].highlightRed();
				}
			}
		}
	}

	private void highlightSelected() {
		if (selectedCellG != null) {
			selectedCellG = cellGs[selectedCellG.getRow()][selectedCellG.getCol()];
			board.setSelectedCell(selectedCellG.getCell());
			selectedCellG.highlight();
			selectedCellG.repaint();
		}
	}

	private void highlightHints() {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (board.getCell(r, c).isHint()) {
					//System.out.println("cell " + r + c + " is hint");
					cellGs[r][c].highlightHint();
				}
			}
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setGenPopup(GeneratePopup genP) {
		genPopup = genP;
	}
	public JButton getOK() {
		return hiddenGenOK;
	}

	public JButton getCancel() {
		return hiddenGenCancel;
	}

	public CellGraphics[][] getCellGs() {
		return cellGs;
	}

	private CellGraphics[][] copyOfCGs() {
		CellGraphics[][] copy = new CellGraphics[9][9];
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				copy[r][c] = new CellGraphics(cellGs[r][c]);
			}
		}
		return copy;
	}

	public Board getBoard() {
		return board;
	}

	public ButtonPanel getButtonPanel() {
		return buttonPanel;
	}

	private int getSelectedRow(int mouseY) {
		int[] cellYStarts = {140, 200, 260, 365, 425, 485, 590, 650, 710};
		int selectedRow = -1;
		for (int i = 0; i < cellYStarts.length; i++) {
			cellYStarts[i] += 53;
			if (mouseY > cellYStarts[i] && mouseY < cellYStarts[i] + 50) {
				selectedRow = i;
			}
		}
		return selectedRow;
	}

	private int getSelectedCol(int mouseX) {
		int[] cellXStarts = {110, 170, 230, 335, 395, 455, 560, 620, 680};
		int selectedCol = -1;
		for (int i = 0; i < cellXStarts.length; i++) {
			cellXStarts[i] += 60;
			if (mouseX > cellXStarts[i] && mouseX < cellXStarts[i] + 50) {
				selectedCol = i;
			}
		}
		return selectedCol;
	}

	public void setAllAvailable(boolean bool) {
		for (CellGraphics[] cellGRow : cellGs) {
			for (CellGraphics cellG : cellGRow) {
				for (int i = 1; i < 10; i++) {
					cellG.getCell().setAvailableTo(i, bool);
				}
			}
		}
	}

	public void setAllEditable(boolean bool) {
		for (CellGraphics[] cellGRow : cellGs) {
			for (CellGraphics cellG : cellGRow) {
				cellG.getCell().setEditable(bool);
			}
		}
	}

	private void updateUndoRedoButtons() {
		if (pastStates.size() > 0) {
			buttonPanel.enableUndo();
		}
		else {
			buttonPanel.disableUndo();
		}
		if (futureStates.size() > 0) {
			buttonPanel.enableRedo();
		}
		else {
			buttonPanel.disableRedo();
		}
	}

	private void resetStateLists() {
		pastStates = new ArrayList<CellGraphics[][]>();
		futureStates = new ArrayList<CellGraphics[][]>();
	}

	private void addHint() {
		ArrayList<Cell> possibleHints = new ArrayList<Cell>();
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (board.getCell(r, c).getBigNum() != board.getSolutionCell(r, c).getBigNum()) {
					possibleHints.add(board.getSolutionCell(r, c));
				}
			}
		}

		int randomIndex = (int)(Math.random() * possibleHints.size());
		Cell hintToAdd = possibleHints.get(randomIndex);
		if (gameMode == SOLVINGMODE) {
			hintToAdd.addAsHint();
		}
		int hintRow = hintToAdd.getRow();
		int hintCol = hintToAdd.getCol();

		CellGraphics cg = new CellGraphics(hintRow, hintCol, new Cell(hintToAdd));
		cellGs[hintRow][hintCol] = cg;

		board.setCell(hintRow, hintCol, cg.getCell());
		board.getCell(hintRow, hintCol).setEditable(false);

		for (CellGraphics[][] array : pastStates) {
			array[hintRow][hintCol] = new CellGraphics(board.getCell(hintRow, hintCol));
		}
		for (CellGraphics[][] array : futureStates) {
			array[hintRow][hintCol] = new CellGraphics(board.getCell(hintRow, hintCol));
		}
		resetGraphics();
		if (board.getSelectedCell() != null && board.getSelectedCell().getRow() == hintRow && board.getSelectedCell().getCol() == hintCol) {
			board.setSelectedCell(null);
			selectedCellG = null;
		}
		repaintAll();
	}

	private void switchToEntryFromOpen() {
		board.setSelectedCell(null);
		for (CellGraphics[] row : cellGs) {
			for (CellGraphics cellG : row) {
				for (int i = 1; i < 10; i++) {
					cellG.getCell().setAvailableTo(i, cellG.getCell().isAvailable(i));
				}
			}
		}

		gameMode = ENTRYMODE;
		resetStateLists();
		highlightInvalid();
		int[] availableButtons = {ButtonPanel.OPEN, ButtonPanel.GENERATE, ButtonPanel.SAVE};
		buttonPanel.setButtons(availableButtons);
		buttonPanel.getEntryModeButton().setSelected(true);
		buttonPanel.disableAllNums();
	}

	private void switchToEntry() {
		Cell selected = board.getSelectedCell();
		gameMode = ENTRYMODE;
		resetStateLists();
		int[] availableButtons = {ButtonPanel.OPEN, ButtonPanel.GENERATE, ButtonPanel.SAVE};
		buttonPanel.setButtons(availableButtons);
		for (CellGraphics[] row : cellGs) {
			for (CellGraphics cellG : row) {
				for (int i = 1; i < 10; i++) {
					cellG.getCell().setAvailableTo(i, false);
					cellG.repaint();
					cellG.getCell().setEditable(true);
				}
			}
		}
		buttonPanel.getEntryModeButton().setSelected(true);
		buttonPanel.updateNumButtons(gameMode, selected);
	}

	private void switchToSolvingFromOpen() {
		board.setSelectedCell(null);
		for (CellGraphics[] row : cellGs) {
			for (CellGraphics cellG : row) {
				if (!cellG.getCell().isBig()) {
					for (int i = 1; i < 10; i++) {
						cellG.getCell().setAvailableTo(i, cellG.getCell().isAvailable(i));
					}
				}
			}
		}

		gameMode = SOLVINGMODE;
		resetStateLists();
		highlightInvalid();
		int[] availableButtons = {ButtonPanel.HINT, ButtonPanel.SOLVE, ButtonPanel.SAVE};
		buttonPanel.setButtons(availableButtons);
		buttonPanel.getSolvingModeButton().setSelected(true);
		buttonPanel.disableAllNums();
	}

	private void switchToSolving() {
		Cell selected = board.getSelectedCell();
		if (gameMode == ENTRYMODE) {
			for (CellGraphics[] row : cellGs) {
				for (CellGraphics cellG : row) {
					if (cellG.getCell().isBig()) {
						cellG.getCell().setEditable(false);
					}
					else {
						for (int i = 1; i < 10; i++) {
							cellG.getCell().setAvailableTo(i, true);
						}
					}
				}
			}
		}
		gameMode = SOLVINGMODE;
		resetStateLists();
		int[] availableButtons = {ButtonPanel.HINT, ButtonPanel.SOLVE, ButtonPanel.SAVE};
		buttonPanel.setButtons(availableButtons);

		board.solveSudoku(board.getSolution(), new int[] {-1, -1}, 0);

		highlightInvalid();
		buttonPanel.getSolvingModeButton().setSelected(true);
		buttonPanel.updateNumButtons(gameMode, selected);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	public void actionPerformed(ActionEvent e) {
		lastAction = e.getActionCommand();
		Cell selected = board.getSelectedCell();

		//if the button clicked was a number
		if (Character.isDigit(lastAction.charAt(0))) {

			int num = Integer.parseInt(lastAction);

			pastStates.add(copyOfCGs());
			selected.changeAvailable(num);
			if (gameMode == ENTRYMODE) {
				if (selected.isAvailable(num)) {
					board.getSolutionCell(selected.getRow(), selected.getCol()).setBig(num);
				}
				else {
					board.getSolutionCell(selected.getRow(), selected.getCol()).setBig(0);	
				}
				//System.out.println(board.toStringSolution());
			}
			buttonPanel.updateNumButtons(gameMode, selected);

			//System.out.println(board.toStringCells());
			futureStates = new ArrayList<CellGraphics[][]>();
			updateUndoRedoButtons();
			updateHighlights();
		}


		else {
			switch(lastAction) {

			case "Undo":
				futureStates.add(copyOfCGs());
				cellGs = pastStates.get(pastStates.size() - 1);
				pastStates.remove(pastStates.size() - 1);
				updateBoard();
				resetGraphics();
				updateHighlights();
				buttonPanel.updateNumButtons(gameMode, board.getSelectedCell());
				break;


			case "Redo":
				pastStates.add(copyOfCGs());
				cellGs = futureStates.get(futureStates.size() - 1);
				futureStates.remove(futureStates.size() - 1);
				updateBoard();
				resetGraphics();
				updateHighlights();
				buttonPanel.updateNumButtons(gameMode, board.getSelectedCell());
				break;


			case "Hint":
				if (JOptionPane.showConfirmDialog(frame, "Are you sure?", "",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					addHint();
					updateHighlights();
					buttonPanel.updateNumButtons(gameMode, board.getSelectedCell());
					if (board.allCellsValid(board.getGameBoard())) {
						resetStateLists();
						buttonPanel.setButtons(new int[] {});
						buttonPanel.disableAllNums();
					}
				}
				break;


			case "Solve":
				//System.out.println("SOLVE");
				//System.out.println(board.toStringSolution());
				if (JOptionPane.showConfirmDialog(frame, "Are you sure?", "",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					for (int r = 0; r < 9; r++) {
						for (int c = 0; c < 9; c++) {
							board.getCell(r, c).setFinal(board.getSolutionCell(r, c).getBigNum());
						}
					}
					resetGraphics();
					if (selectedCellG != null) {
						selectedCellG.unhighlight();
					}
					resetStateLists();
					buttonPanel.setButtons(new int[] {});
					buttonPanel.disableAllNums();
				}
				break;


			case "Generate":
				frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				initializeBoardAndCGs();
				//clears board so anything entered before disappears
				for (int r = 0; r < 9; r++) {
					for (int c = 0; c < 9; c++) {
						for (int n = 1; n < 10; n++) {
							board.getCell(r, c).setAvailableTo(n, false);
						}
					}
				}
				board.generatePuzzle();
				int numFilled = 0;
				for (int r = 0; r < 9; r++) {
					for (int c = 0; c < 9; c++) {
						if (board.getCell(r, c).isBig()) {
							numFilled++;
						}
					}
				}
				resetGraphics();
				int maxHints = 81 - numFilled;
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				genPopup = new GeneratePopup(this, maxHints);
				break;


			case "Generate OK": 
				numHints = genPopup.getNumHints();
				for (int r = 0; r < 9; r++) {
					for (int c = 0; c < 9; c++) {
						if (board.getCell(r, c).isBig()) {
							board.getCell(r, c).setEditable(false);
						}
						else {
							board.getCell(r, c).setEditable(true);
						}
					}
				}
				for (int i = 0; i < numHints; i++) {
					addHint();
				}
				updateCGs();
				resetGraphics();
				buttonPanel.getSolvingModeButton().doClick();
				break;


			case "Generate Cancel":
				numHints = 0;
				switchToEntry();
				break;

			case "Save":
				board.setMode(gameMode);
				xmlHandler.saveGameFile(frame, board);
				break;


			case "Open":
				Board temporaryBoard = xmlHandler.openGameFile(frame);
				if(temporaryBoard!=null) {
					board = temporaryBoard;
					updateCGs();
					resetGraphics();
					if (board.getMode() == SOLVINGMODE) {
						gameMode = SOLVINGMODE;
						switchToSolvingFromOpen();
						updateHighlights();
						repaintAll();
					}
					else {
						gameMode = ENTRYMODE;
						switchToEntryFromOpen();
						updateHighlights();
						repaintAll();
					}
				}
				break;


			case "Entry Mode":
				if (gameMode != ENTRYMODE) {
					if (JOptionPane.showConfirmDialog(frame, "Do you want to save your puzzle first?", "",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
						buttonPanel.getOtherButton(ButtonPanel.SAVE).doClick();
						initializeBoardAndCGs();
						resetGraphics();
						switchToEntry();
						repaintAll();
					}
					else {
						initializeBoardAndCGs();
						resetGraphics();
						switchToEntry();
						repaintAll();
					}
				}
				break;


			case "Solving Mode":
				//after entering puzzle manually
				if (gameMode == ENTRYMODE && !lastAction.equals("ok")) {
					//puzzle does does not violate anything
					if (board.entryBoardValid()) {
						//check entered puzzle
						if (board.solveSudoku(board.copyCell(), new int[] {-1, -1}, 0)) {
							switchToSolving();
							repaintAll();
						} else {
							//create pop up informing user that puzzle is not solvable
							JOptionPane.showMessageDialog(contentPane, "This puzzle is not solvable, enter a valid puzzle to play.");
							buttonPanel.getEntryModeButton().setSelected(true);
						}
					} else {
						//entered puzzle violates rules
						JOptionPane.showMessageDialog(contentPane, "The entered puzzle is invalid, enter a valid puzzle to play.");
						buttonPanel.getEntryModeButton().setSelected(true);
					}
				}

				/*System.out.println("Entered Puzzle");
				System.out.println(board.toStringPuzzleSquish());
				System.out.println("Solving Mode Generated Solution");
				System.out.println(board.toStringSolutionSquish());*/
				break;


				//in event of error
			default:
				//System.out.println("button action is bad");
				break;
			}
			if (board.allCellsValid(board.getGameBoard())) {
				buttonPanel.setButtons(new int[] {});
				buttonPanel.disableAllNums();
			}
		}
		updateUndoRedoButtons();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		//System.out.println(mouseX + " " + mouseY);
		int cellRow;
		int cellCol;
		cellRow = getSelectedRow(mouseY);
		cellCol = getSelectedCol(mouseX);
		//System.out.println(cellRow + " " + cellCol);

		//if click on nothing
		if (cellRow == -1 || cellCol == -1) {
			if (selectedCellG != null) {
				selectedCellG.unhighlight();
			}
			highlightInvalid();
			selectedCellG = null;
			board.setSelectedCell(null);
		}
		//if click on cell that was already selected, deselect
		else if (selectedCellG != null && selectedCellG.getRow() == cellRow && selectedCellG.getCol() == cellCol) {
			selectedCellG.unhighlight();
			highlightInvalid();
			selectedCellG = null;
			board.setSelectedCell(null);
		}

		//if click on a different cell, deselect original if any, select new
		else {
			if (cellGs[cellRow][cellCol].getCell().isEditable()) {
				for (int i = 0; i < 9; i++) {
					buttonPanel.enableButton(buttonPanel.getNumButtons()[i]);
				}
				if (selectedCellG != null) {
					selectedCellG.unhighlight();
				}
				highlightInvalid();

				selectedCellG = cellGs[cellRow][cellCol];
				board.setSelectedCell(selectedCellG.getCell());
				selectedCellG.highlight();
			}
		}
		buttonPanel.updateNumButtons(gameMode, board.getSelectedCell());
		//System.out.println("selected: " + board.getSelectedCell());
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}