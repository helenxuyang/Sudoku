import java.util.ArrayList;
import java.util.Collections;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="Board")
@XmlAccessorType(XmlAccessType.FIELD)
public class Board {
	private Cell[][] cells = new Cell[9][9];
	private Cell selectedCell;
	private Cell[][] solution = new Cell[9][9];
	private int gameMode;

	Board() {
		for(int row = 0;row<9;row++) {
			for(int col = 0;col<9;col++) {
				cells[row][col] = new Cell(row, col);
				solution[row][col] = new Cell(row,col);
			}
		}
	}

	/**
	 * <h1>checkCellValid</h1>
	 * <p>Checks to see if a given Cell is valid on a board according to Sudoku rules</p>
	 * @param board - The Sudoku board to check the cell on
	 * @param check - The cell to check it's validity
	 * @return An {@code ArrayList<Cell>} of all the violating cells in regard to the passed check cell
	 */
	public ArrayList<Cell> checkCellValid(Cell[][] board, Cell check) {
		ArrayList<Cell> bads = new ArrayList<Cell>();
		int checkNum = check.getBigNum();
		int row = check.getRow();
		int col = check.getCol();

		for (int i = 0; i < 9; i++) {
			if (i != col && board[row][i].getBigNum() == checkNum && checkNum !=0) {
				bads.add(board[row][i]);
			}
			if (i != row && board[i][col].getBigNum() == checkNum && checkNum !=0) {
				bads.add(board[i][col]);
			}
		}

		int squareRow = row / 3;
		int squareCol = col / 3;

		for (int rowToCheck = squareRow * 3; rowToCheck < (squareRow * 3) + 3; rowToCheck++) {
			for (int colToCheck = squareCol * 3; colToCheck < (squareCol * 3) + 3; colToCheck++) {
				Cell cellToCheck = board[rowToCheck][colToCheck];
				if (rowToCheck != row && colToCheck != col && cellToCheck.getBigNum() == checkNum && checkNum !=0) {
					bads.add(cellToCheck);
				}
			}
		}

		return bads;
	}

	public boolean entryBoardValid() {
		for(int row = 0; row < 9 ; row++) {
			for(int col = 0; col < 9; col++) {
				if((cells[row][col].getBigNum() != 0) && (checkCellValid(cells, cells[row][col]).size() > 0)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean allCellsValid(Cell[][] board) {
		for(int row=0; row<board.length; row++) {
			for(int col=0; col<board[0].length; col++) {
				if(checkCellValid(board, board[row][col]).size()!=0 || board[row][col].getBigNum()==0)
					return false;
			}
		}
		return true;
	}

	public Cell[][] getGameBoard() {
		return cells;
	}

	public Cell[][] getSolution() {
		return solution;
	}

	public void setSelectedCell(Cell cell) {
		selectedCell = cell;
	}
	public void setSelectedCell(int row, int col) {
		selectedCell = cells[row][col];
	}

	public Cell getSelectedCell() {
		return selectedCell;
	}

	public void setCell(int row, int column, Cell c) {
		cells[row][column] = c;
	}

	public Cell getCell(int row, int col) {
		return cells[row][col];
	}

	public Cell getSolutionCell(int row, int col) {
		return solution[row][col];
	}
	
	public void setMode(int mode) {
		gameMode = mode;
	}
	
	public int getMode() {
		return gameMode;
	}
	
	//Public to use for checking validity of opened game file
	public Cell[][] copyCell(){
		Cell[][] answer = new Cell[9][9];
		for(int row = 0; row < 9; row++) {
			for(int col = 0; col < 9; col++) {
				answer[row][col] = new Cell(row, col);
				answer[row][col].setBig(cells[row][col].getBigNum());
			}
		}
		return answer;
	}

	/**
	 * <h1>generatePuzzle</h1>
	 * <p>Generates a new puzzle</p>
	 * <p>Updates instance of Board to store a new puzzle.  That new puzzle accessible through
	 * {@code public Cell[][] getGameBoard()}</p>
	 * 
	 */
	public void generatePuzzle() {

		generateSolution();
		Cell[][] puzzleTester1;
		//System.out.println(allCellsValid(solution));
		ArrayList<Cell> shuffledList = new ArrayList<Cell>();
		for(int row = 0; row < 9; row++) {
			for(int col = 0; col < 9; col++) {
				shuffledList.add(solution[row][col]);
				cells[row][col].setBig(solution[row][col].getBigNum());
				cells[row][col].setEditable(true);
			}
		}
		
		Collections.shuffle(shuffledList);
		while(shuffledList.size() > 0) {
			int nextRow = shuffledList.get(0).getRow();
			int nextCol = shuffledList.get(0).getCol();
			int big = shuffledList.get(0).getBigNum();
			shuffledList.remove(0);

			cells[nextRow][nextCol].setBig(0);
			printCell(cells);
			
			puzzleTester1 = copyCell();
			boolean solved = solveSudoku(puzzleTester1, new int[] {nextRow,nextCol}, big);
			
			//Board not solved without number that was removed, therefore unique solution
			if(!solved) {
			
			}else {
				//Puts number back since not unique
				cells[nextRow][nextCol].setBig(big);
			}
		}
		makeNewBoardFinal();
	}
	
	private void makeNewBoardFinal() {
		for(int row=0; row<cells.length; row++) {
			for(int col=0; col<cells[0].length; col++) {
				if(cells[row][col].getBigNum()!=0)
					cells[row][col].setFinal(cells[row][col].getBigNum());
				else
					cells[row][col].setEditable(true);
			}
		}
	}

	private ArrayList<Integer> generateAllNumbers(){
		ArrayList<Integer> answer = new ArrayList<Integer>();
		for(int i = 1; i <=9; i++) {
			answer.add(i);
		}
		return answer;
	}
	
	/**
	 * <h1>{@code generateSolution}</h1>
	 * <p>Generates a random solution for a new puzzle</p>
	 * <p>Do not use to solve a board</p>
	 * <p>Use concurrently with {@code public void generatePuzzle()}</p>
	 */
	public void generateSolution() {

		ArrayList<Integer> nums = generateAllNumbers();
		Collections.shuffle(nums);
		for(int col = 0; col < 9; col++) {
			solution[0][col].setFinal(nums.get(col));
		}
		for(int row = 1; row < 9; row++) {
			for(int col = 0; col < 9; col++) {
				solution[row][col].setEditable(true);
			}
		}
		solveSudoku(solution, new int[]{-1,-1}, 0);
	}

	//FOR TESTING
	private String printCell(Cell[][] puzzle) {
		String answer = "---------\n   0 1 2 3 4 5 6 7 8\n\n";
		for(int row = 0; row<9; row++) {
			answer+= row + "  ";
			for(int col = 0; col<9; col++) {
				answer+=puzzle[row][col] + " ";
			}
			answer+="\n";
		}
		return answer;
	}

	//For TESTING
	public String toString() {
		String answer = "---------\n   0 1 2 3 4 5 6 7 8\n\n";
		for(int row = 0; row<9; row++) {
			answer+= row + "  ";
			for(int col = 0; col<9; col++) {
				answer+=solution[row][col] + " ";
			}
			answer+="\n";
		}
		return answer;
	}

	//For TESTING
	public String toStringCells() {
		String answer = "---------\n   0 1 2 3 4 5 6 7 8\n\n";
		for(int row = 0; row<9; row++) {
			answer+= row + "  ";
			for(int col = 0; col<9; col++) {
				answer+=cells[row][col] + " ";
			}
			answer+="\n";
		}
		return answer;
	}

	//For TESTING
	public String toStringSolution() {
		String answer = "---------\n   0 1 2 3 4 5 6 7 8\n\n";
		for(int row = 0; row<9; row++) {
			answer+= row + "  ";
			for(int col = 0; col<9; col++) {
				answer+=solution[row][col] + " ";
			}
			answer+="\n";
		}
		return answer;
	}
	
	//For TESTING
	public String toStringPuzzleSquish() {
		String answer = "";
		for(int row = 0; row<9; row++) {
			for(int col = 0; col<9; col++) {
				if(cells[row][col].getBigNum() == 0) 
					answer+=" ";
				else
					answer+=cells[row][col];
			}
		}
		return answer;
	}
	
	//For TESTING
		public String toStringSolutionSquish() {
			String answer = "";
			for(int row = 0; row<9; row++) {
				for(int col = 0; col<9; col++) {
					if(solution[row][col].getBigNum() == 0) 
						answer+=" ";
					else
						answer+=solution[row][col];
				}
			}
			return answer;
		}

	/**
	 * <h1>solveSudoku</h1>
	 * <p>Solves the passed boardToSolve</p>
	 * <p>If solving board with no restrictions,
	 * pass {@code forbiddenCoordinate} as new int[] {-1,-1} and {@code forbiddenNum} as 0</p>
	 * 
	 * @param boardToSolve - Sudoku board of type Cell[][] to solve
	 * @param forbiddenCoordinate - Grid location (x,y) in type int[] coordinate form that was removed. Only used while generating puzzle
	 * @param forbiddenNum - Integer that was just removed at the forbidden coordinate.  Only used while generating puzzle.
	 * @return Returns a boolean
	 * <p>True: The board can be solved without the forbidden coordinate and number</p>
	 * <p>False: The board could not be solved given the parameters</p>
	 * 
	 * 
	 */

	public boolean solveSudoku(Cell[][] boardToSolve, int[] forbiddenCoordinate, int forbiddenNum) {
		int[] coordinate = findEmptyCell(boardToSolve);
		if(coordinate==null) {
			return true;
		}
		
		//Loops through all potential numbers 1-9 for the cell at the coordinate
		for(int potentialNum=1; potentialNum<=9; potentialNum++) {
			boardToSolve[coordinate[0]][coordinate[1]].setBig(potentialNum);
			if(coordinate[0]==forbiddenCoordinate[0] && coordinate[1]==forbiddenCoordinate[1] && potentialNum==forbiddenNum) {
				boardToSolve[coordinate[0]][coordinate[1]].setBig(0);
			
			}else if(checkCellValid(boardToSolve,boardToSolve[coordinate[0]][coordinate[1]]).size()==0) {

				if(solveSudoku(boardToSolve, forbiddenCoordinate, forbiddenNum)) {
					return true;
				}
				boardToSolve[coordinate[0]][coordinate[1]].setBig(0);
			}
			boardToSolve[coordinate[0]][coordinate[1]].setBig(0);
		}
		return false;
	}

	private int[] findEmptyCell(Cell[][] board) {
			for(int row=0; row<board.length; row++) {
				for(int col = 0; col<board[0].length; col++) {
					if(board[row][col].isEditable() && board[row][col].getBigNum()==0) {
						return new int[] {row,col};
					}
				}
			}
		return null;
	}

}