import javax.xml.bind.annotation.*;

@XmlRootElement(name="Cell")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cell {
	private boolean big;
	private boolean valid = true;
	private boolean editable = true;
	private boolean[] available = new boolean[9];
	private int bigNum = 0;
	private int row;
	private int column;
	private boolean addedAsHint = false;

	public Cell() {

	}

	//makes copy of cell so no reference problems
	public Cell(Cell cellToCopy) {
		big = cellToCopy.isBig();
		valid = cellToCopy.isValid();
		editable = cellToCopy.isEditable();
		boolean[] availableCopy = cellToCopy.getAvailable();
		for (int i = 0; i < availableCopy.length; i++) {
			available[i] = availableCopy[i];
		}
		bigNum = cellToCopy.getBigNum();
		row = cellToCopy.getRow();
		column = cellToCopy.getCol();
		addedAsHint = cellToCopy.isHint();
	}

	public Cell(int row, int col) {
		for (int i = 0; i < available.length; i++) {
			available[i] = true;
		}
		this.row = row;
		this.column = col;
	}

	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.column;
	}

	public void setFinal(int num) {
		bigNum = num;
		editable = false;
		big = true;
		for(int i=0; i<available.length; i++)
			available[i] = false;
	}

	//Needed in Board for generating a puzzle to compare the values of cells within the board
	public void setBig(int num) {
		bigNum = num;
		if(num==0)
			big = false;
		else
			big =true;
	}

	public void addAsHint() {
		addedAsHint = true;
	}
	
	public boolean isHint() {
		return addedAsHint;
	}
	
	public void setAvailableTo(int num, boolean b) {
		available[num - 1] = b;
		updateBig();
	}
	
	public void changeAvailable(int num) {
		available[num - 1] = !(available[num - 1]);
		updateBig();
	}
	
	public boolean[] getAvailable() {
		return available;
	}

	//Used in Board Graphics for updating available through button clicks
	private void updateBig() {
		int possibleBigNum = 0;
		int howManyAvailable = 0;
		for (int i = 0; i < available.length; i++) {
			if (available[i]) {
				howManyAvailable++;
				possibleBigNum = i + 1;
			}
		}
		if (howManyAvailable == 1) {
			bigNum = possibleBigNum;
			big = true;
		}
		else {
			bigNum = 0;
			big = false;
		}
	}
	
	public boolean isBig() {
		return big;
	}

	public boolean isAvailable(int num) {
		return available[num - 1];
	}

	public void setValid(boolean newValid) {
		valid = newValid;
	}

	public boolean isValid() {
		return valid;
	}

	public void setEditable(boolean newEditable) {
		editable = newEditable;
	}

	public boolean isEditable() {
		return editable;
	}

	public int getBigNum() {
		return bigNum;
	}

//	public String toString() {
//		String s = "";
//		s = "r: " + row + " " + "c: " + column + " num: ";
//		if (bigNum == 0) {
//			s += "_ ";
//		} else {
//			s += bigNum + " ";
//		}
//		for (boolean b : available) {
//			if (b) {
//				s += "T";
//			}
//			else {
//				s += "F";
//			}
//		}
//		
//		return s;
//	}
	
	public String toString() {
		if(bigNum==0)
			return "_";
		else
			return ""+bigNum;
	}
}