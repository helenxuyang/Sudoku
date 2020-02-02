import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ButtonPanel extends JComponent {

	private static final long serialVersionUID = 1L;
	private SpringLayout lay = new SpringLayout();

	private JButton[] numButtons = new JButton[9];
	private boolean[] enabledNums = new boolean[9];
	private JButton undoButton = new JButton("Undo");
	private JButton redoButton = new JButton("Redo");
	private JButton hintButton = new JButton("Hint");
	private JButton solveButton = new JButton("Solve");
	private JButton generateButton = new JButton("Generate");
	private JButton saveButton = new JButton("Save");
	private JButton openButton = new JButton("Open");
	private JRadioButton entryModeButton = new JRadioButton("<html><font color='FFFFFF'>Entry Mode</font></html>");
	private JRadioButton solvingModeButton = new JRadioButton("<html><font color='FFFFFF'>Solving Mode</font></html>");
	private ButtonGroup modes = new ButtonGroup();
	private JButton[] otherButtons = {undoButton, redoButton, hintButton, solveButton, generateButton, saveButton, openButton};

	//for accessing other buttons more easily
	public static final int UNDO = 0;
	public static final int REDO = 1;
	public static final int HINT = 2;
	public static final int SOLVE = 3;
	public static final int GENERATE = 4;
	public static final int SAVE = 5;
	public static final int OPEN = 6;

	ButtonPanel() {
		this.setLayout(lay);
		modes.add(entryModeButton);
		entryModeButton.setSelected(true);
		modes.add(solvingModeButton);

		for(int i = 0; i<9; i++) {
			numButtons[i] = new JButton();
			numButtons[i].setActionCommand(""+(i+1));
			numButtons[i].setPreferredSize(new Dimension(60,60));
			//set color here

			numButtons[i].setFont(new Font("Sans", Font.PLAIN, 30));

			numButtons[i].setBackground(Color.WHITE);
			numButtons[i].setBorderPainted(false);
			numButtons[i].setIconTextGap(-50);
			numButtons[i].setText(""+(i+1));
			enabledNums[i] = true;
			this.add(numButtons[i]);
		}
		undoButton.setActionCommand("Undo");
		undoButton.setBackground(Color.WHITE);
		undoButton.setBorderPainted(false);
		this.add(undoButton);
		redoButton.setActionCommand("Redo");
		redoButton.setBackground(Color.WHITE);
		redoButton.setBorderPainted(false);
		this.add(redoButton);
		hintButton.setActionCommand("Hint");
		hintButton.setBackground(Color.WHITE);
		hintButton.setBorderPainted(false);
		this.add(hintButton);
		solveButton.setActionCommand("Solve");
		solveButton.setBackground(Color.WHITE);
		solveButton.setBorderPainted(false);
		this.add(solveButton);
		generateButton.setActionCommand("Generate");
		generateButton.setBackground(Color.WHITE);
		generateButton.setBorderPainted(false);
		this.add(generateButton);
		saveButton.setActionCommand("Save");
		saveButton.setBackground(Color.WHITE);
		saveButton.setBorderPainted(false);
		this.add(saveButton);
		openButton.setActionCommand("Open");
		openButton.setBackground(Color.WHITE);
		openButton.setBorderPainted(false);
		this.add(openButton);
		
		entryModeButton.setOpaque(false);
		solvingModeButton.setOpaque(false);
		entryModeButton.setActionCommand("Entry Mode");
		solvingModeButton.setActionCommand("Solving Mode");
		entryModeButton.setFont(new Font("Sans", Font.BOLD, 23));
		solvingModeButton.setFont(new Font("Sans", Font.BOLD, 23));

		this.add(entryModeButton);
		this.add(solvingModeButton);


		lay.putConstraint(SpringLayout.NORTH, entryModeButton, 0, SpringLayout.NORTH, this);
		lay.putConstraint(SpringLayout.WEST, entryModeButton, 25, SpringLayout.WEST, this);

		lay.putConstraint(SpringLayout.NORTH, numButtons[0], 150, SpringLayout.SOUTH, entryModeButton);
		lay.putConstraint(SpringLayout.WEST, numButtons[0], 45, SpringLayout.WEST, entryModeButton);
		for(int i = 1; i<3; i++) {
			lay.putConstraint(SpringLayout.NORTH, numButtons[i], 0, SpringLayout.NORTH, numButtons[i-1]);
			lay.putConstraint(SpringLayout.WEST, numButtons[i], 15, SpringLayout.EAST, numButtons[i-1]);
		}

		lay.putConstraint(SpringLayout.NORTH, numButtons[3], 15, SpringLayout.SOUTH, numButtons[0]);
		lay.putConstraint(SpringLayout.WEST, numButtons[3], 45, SpringLayout.WEST, entryModeButton);
		for(int i = 4; i<6; i++) {
			lay.putConstraint(SpringLayout.NORTH, numButtons[i], 0, SpringLayout.NORTH, numButtons[3]);
			lay.putConstraint(SpringLayout.WEST, numButtons[i], 15, SpringLayout.EAST, numButtons[i-1]);
		}
		lay.putConstraint(SpringLayout.NORTH, numButtons[6], 15, SpringLayout.SOUTH, numButtons[3]);
		lay.putConstraint(SpringLayout.WEST, numButtons[6], 45, SpringLayout.WEST, entryModeButton);
		for(int i = 7; i<9; i++) {
			lay.putConstraint(SpringLayout.NORTH, numButtons[i], 0, SpringLayout.NORTH, numButtons[6]);
			lay.putConstraint(SpringLayout.WEST, numButtons[i], 15, SpringLayout.EAST, numButtons[i-1]);
		}

		// TODO THESE DIMENSIONS MAY NEED TO BE CHANGED
		hintButton.setPreferredSize(new Dimension(90, 50));
		undoButton.setPreferredSize(new Dimension(90, 50));
		redoButton.setPreferredSize(new Dimension(90, 50));
		saveButton.setPreferredSize(new Dimension(90, 50));
		openButton.setPreferredSize(new Dimension(90, 50));
		generateButton.setPreferredSize(new Dimension(90, 50));
		solveButton.setPreferredSize(new Dimension(90, 50));

		//solving mode
		lay.putConstraint(SpringLayout.WEST, solvingModeButton, 170, SpringLayout.WEST, entryModeButton);
		lay.putConstraint(SpringLayout.NORTH, solvingModeButton, 0, SpringLayout.NORTH, entryModeButton);
		//undobutton
		lay.putConstraint(SpringLayout.WEST, undoButton, 5, SpringLayout.WEST, entryModeButton);
		lay.putConstraint(SpringLayout.NORTH, undoButton, 450, SpringLayout.NORTH, entryModeButton);
		//hintbutton
		lay.putConstraint(SpringLayout.WEST, hintButton, 105, SpringLayout.WEST, entryModeButton);
		lay.putConstraint(SpringLayout.NORTH, hintButton, 450, SpringLayout.NORTH, entryModeButton);
		//redobutton
		lay.putConstraint(SpringLayout.WEST, redoButton, 205, SpringLayout.WEST, entryModeButton);
		lay.putConstraint(SpringLayout.NORTH, redoButton, 450, SpringLayout.NORTH, entryModeButton);

		//solvebutton
		lay.putConstraint(SpringLayout.WEST, solveButton, 5, SpringLayout.WEST, entryModeButton);
		lay.putConstraint(SpringLayout.NORTH, solveButton, 525, SpringLayout.NORTH, entryModeButton);
		//openbutton
		lay.putConstraint(SpringLayout.WEST, openButton, 105, SpringLayout.WEST, entryModeButton);
		lay.putConstraint(SpringLayout.NORTH, openButton, 525, SpringLayout.NORTH, entryModeButton);
		//savebutton
		lay.putConstraint(SpringLayout.WEST, saveButton, 205, SpringLayout.WEST, entryModeButton);
		lay.putConstraint(SpringLayout.NORTH, saveButton, 525, SpringLayout.NORTH, entryModeButton);

		//generatebutton
		lay.putConstraint(SpringLayout.WEST, generateButton, 105, SpringLayout.WEST, entryModeButton);
		lay.putConstraint(SpringLayout.NORTH, generateButton, 600, SpringLayout.NORTH, entryModeButton);


		this.setPreferredSize(new Dimension(400, 800));
		//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//		frame.pack();
		//		frame.setVisible(true);
	}

	public void addButtonListeners(ActionListener a) {
		for (JButton b : numButtons) {
			b.addActionListener(a);
		}
		undoButton.addActionListener(a);
		redoButton.addActionListener(a);
		hintButton.addActionListener(a);
		solveButton.addActionListener(a);
		generateButton.addActionListener(a);
		saveButton.addActionListener(a);
		openButton.addActionListener(a);
		entryModeButton.addActionListener(a);
		solvingModeButton.addActionListener(a);
	}

	public void updateNumButtons(int mode, Cell selectedCell) {
		if (selectedCell == null) {
			for (JButton b : getNumButtons()) {
				disableButton(b);
			}
		}
		else {
			if (mode == BoardGraphics.SOLVINGMODE) {
				//disable the big number so can't deselect everything
				if (selectedCell.isBig()) {
					disableButton(getNumButtons()[selectedCell.getBigNum() - 1]);
					for (int i = 1; i < 10; i++) {
						JButton b = numButtons[i-1];
						if (i != selectedCell.getBigNum()) {
							b.setForeground(new Color(200, 200, 200));
						}
					}
				}
				else {
					for (int i = 0; i < 9; i++) {
						JButton b = numButtons[i];
						enableButton(b);
						if (selectedCell.isAvailable(i+1)) {
							b.setForeground(Color.BLACK);
						}
						else {
							b.setForeground(new Color(200, 200, 200));
						}
					}
				}
			}
			else if (mode == BoardGraphics.ENTRYMODE){
				//disable all other numbers so can't select more than one
				if (selectedCell.isBig()) {
					for (int i = 1; i < 10; i++) {
						if (i != selectedCell.getBigNum()) {
							disableButton(getNumButtons()[i - 1]);
						}
					}
				}
				else {
					for (JButton b : getNumButtons()) {
						enableButton(b);
					}
				}
			}
		}
	}
	
	public void setEnabledNums(boolean[] newEnabledNums) {
		enabledNums = newEnabledNums;
	}

	public void enableAllNums() {
		for (int i = 1; i < 10; i++) {
			enableButton(getNumButtons()[i - 1]);
		}
	}
	
	public void disableAllNums() {
		for (int i = 1; i < 10; i++) {
			disableButton(getNumButtons()[i - 1]);
		}
	}
	
	public int howManyEnabled() {
		int numEnabled = 0;
		for (boolean b : enabledNums) {
			if (b) {
				numEnabled++;
			}
		}
		return numEnabled;
	}

	public boolean isEnabled(int num) {
		return enabledNums[num - 1];
	}

	public void changeEnabled(int num) {
		enabledNums[num - 1] = !enabledNums[num - 1];
	}

	public JButton[] getNumButtons() {
		return numButtons;
	}

	public JButton getOtherButton(int index) {
		return otherButtons[index];
	}
	
	public JRadioButton getEntryModeButton() {
		return entryModeButton;
	}
	
	public JRadioButton getSolvingModeButton() {
		return solvingModeButton;
	}

	public void enableButton(JButton button) {
		button.setBackground(Color.WHITE);
		button.setEnabled(true);
	}

	public void disableButton(JButton button) {
		button.setBackground(Color.GRAY);
		button.setEnabled(false);
	}

	public void enableOtherButtons(int[] indices) {
		for (int i : indices) {
			enableButton(otherButtons[i]);
		}
	}

	public void disableOtherButtons(int[] indices) {
		for (int i : indices) {
			disableButton(otherButtons[i]);
		}
	}

	public void setButtons(int[] enabled) {
		for (JButton b : otherButtons) {
			disableButton(b);
		}
		for (int i : enabled) {
			enableButton(otherButtons[i]);
		}
	}

	public void enableUndo() {
		enableButton(undoButton);
	}

	public void disableUndo() {
		disableButton(undoButton);
	}

	public void enableRedo() {
		enableButton(redoButton);
	}

	public void disableRedo() {
		disableButton(redoButton);
	}
}