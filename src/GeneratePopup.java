import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GeneratePopup implements ActionListener {

	private JDialog frame;
	private JPanel panel;
	private JButton up, down, ok, cancel;
	private JLabel hintPrompt = new JLabel();
	private JLabel hintLabel = new JLabel();
	private SpringLayout lay = new SpringLayout();
	private int numHints = 0;
	private int maxHints;
	private BoardGraphics boardG;

	GeneratePopup(BoardGraphics bg, int max) {
		boardG = bg;
		maxHints = max;
		frame = new JDialog(boardG.getFrame(), "Generate Puzzle", true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				frame.dispose();
				cancel.doClick();
			}
		});
		panel = new JPanel();

		panel.setLayout(lay);
		panel.setBackground(new Color(99,172,229));
		hintPrompt.setText("How many hints in the new puzzle?");
		hintLabel.setText("0");
		hintPrompt.setFont(new Font("Sans", Font.PLAIN, 20));
		hintLabel.setFont(new Font("Sans", Font.PLAIN, 30));

		up = new JButton();
		up.setActionCommand("add");
		up.setIcon(new ImageIcon(GeneratePopup.class.getResource("/upBtn.png")));
		up.setBorderPainted(false);
		up.setPreferredSize(new Dimension(60, 40));

		down = new JButton();
		down.setActionCommand("subtract");
		down.setIcon(new ImageIcon(GeneratePopup.class.getResource("/downBtn.png")));
		down.setBorderPainted(false);
		down.setPreferredSize(new Dimension(60, 40));

		ok = new JButton();
		ok.setActionCommand("ok");
		ok.setText("Okay");
		ok.setPreferredSize(new Dimension(100, 40));

		cancel = new JButton();
		cancel.setActionCommand("cancel");
		cancel.setText("Cancel");
		cancel.setPreferredSize(new Dimension(100, 40));

		addButtonListeners();

		panel.add(hintPrompt);
		panel.add(hintLabel);
		panel.add(up);
		panel.add(down);
		panel.add(ok);
		panel.add(cancel);

		lay.putConstraint(SpringLayout.NORTH, hintPrompt, 5, SpringLayout.NORTH, panel);
		lay.putConstraint(SpringLayout.WEST, hintPrompt, 45, SpringLayout.WEST, panel);

		lay.putConstraint(SpringLayout.NORTH, hintLabel, 60, SpringLayout.NORTH, hintPrompt);
		lay.putConstraint(SpringLayout.WEST, hintLabel, 120, SpringLayout.WEST, hintPrompt);

		lay.putConstraint(SpringLayout.NORTH, up, 45, SpringLayout.NORTH, hintPrompt);
		lay.putConstraint(SpringLayout.WEST, up, 160, SpringLayout.WEST, hintPrompt);

		lay.putConstraint(SpringLayout.NORTH, down, 90, SpringLayout.NORTH, hintPrompt);
		lay.putConstraint(SpringLayout.WEST, down, 160, SpringLayout.WEST, hintPrompt);

		lay.putConstraint(SpringLayout.NORTH, ok, 150, SpringLayout.NORTH, hintPrompt);
		lay.putConstraint(SpringLayout.WEST, ok, 95, SpringLayout.WEST, panel);

		lay.putConstraint(SpringLayout.NORTH, cancel, 150, SpringLayout.NORTH, hintPrompt);
		lay.putConstraint(SpringLayout.EAST, cancel, -95, SpringLayout.EAST, panel);

		panel.setPreferredSize(new Dimension(400, 200));
		
		frame.setLocationRelativeTo(null);
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public void addButtonListeners() {
		up.addActionListener(this);
		down.addActionListener(this);
		ok.addActionListener(this);
		cancel.addActionListener(this);
	}

	public int getNumHints() {
		return numHints;
	}

	public void setMaxHints(int max) {
		maxHints = max;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "add":
			if (numHints < maxHints) { 
				numHints++;
				hintLabel.setText("" + numHints);
			}
			break;


		case "subtract":
			if (numHints > 0){
				numHints--;
				hintLabel.setText("" + numHints);
			}
			break;


		case "ok":
			frame.dispose();
			boardG.setGenPopup(this);
			boardG.getOK().doClick();
			break;


		case "cancel":
			frame.dispose();
			boardG.getCancel().doClick();
			break;


		default:
			//System.out.println("button action is bad");
			break;
		}
	}
}