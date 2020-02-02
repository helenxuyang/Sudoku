import java.io.*;
import javax.xml.bind.*;
import javax.swing.*;
import javax.swing.filechooser.*;

//Any errors with opening and saving files is actually handled in try-catch, when we ready and delete the printStackTrace
//statements, they'll never know anything happened besides for the pop-up error message describing a failure
public class XMLHandler {

	private JFileChooser fileChooser;

	public XMLHandler() {
		
	}
	
	private void setUpFileChooser() {
		fileChooser = new JFileChooser("Z://"); //Starting directory
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML File", "xml")); //Filters for only XML Files
		fileChooser.setAcceptAllFileFilterUsed(false); //Eliminates all file option being available 
		fileChooser.setSelectedFile(null);
	}

	/**
	 * <h1>saveGameFile</h1>
	 * 
	 * <p>Prompts a {@link JFileChooser} to save a sudoku Board as an XML file</p>
	 * 
	 * @param frame - Desired {@link JFrame} to overlay the {@link JFileChooser} on
	 * @param board - Instance of Board class
	 */
	public void saveGameFile(JFrame frame, Board board) {
		setUpFileChooser();
		int userChoice = fileChooser.showSaveDialog(frame);
		if(userChoice == JFileChooser.APPROVE_OPTION) {
			/*System.out.println("name: " + fileChooser.getSelectedFile().getName());
			System.out.println("path: " + fileChooser.getSelectedFile().getPath());
			System.out.println("ext: " + fileChooser.getTypeDescription(fileChooser.getSelectedFile()));*/
			try {
				writeXmlFile(board);
			}catch (Exception e) {
				JOptionPane.showConfirmDialog(frame, "An error occurred saving. "+fileChooser.getSelectedFile()+" was unable to be saved.", "ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				//e.printStackTrace();
			}
		}
	}

	private void writeXmlFile(Board board) throws JAXBException {
		JAXBContext classContent = JAXBContext.newInstance(Board.class, Cell.class);
		Marshaller marshaller = classContent.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(board, new File(fileChooser.getSelectedFile()+".xml"));
	}


	/**
	 * <h1>openGameFile</h1>
	 * <p>Uses a {@link JFileChooser} to retrieve a formerly saved XML file of a sudoku game.
	 * It turn the XML file into an instance of the Board class</p>
	 * @param frame - Desired {@link JFrame} to overlay the {@link JFileChooser} on
	 * @return Instance of Board class recovered from the chosen XML file
	 */
	public Board openGameFile(JFrame frame) {
		setUpFileChooser();
		int userChoice = fileChooser.showOpenDialog(frame);
		if(userChoice == JFileChooser.APPROVE_OPTION) {
			/*System.out.println("name: " + fileChooser.getSelectedFile().getName());
			System.out.println("path: " + fileChooser.getSelectedFile().getPath());
			System.out.println("ext: " + fileChooser.getTypeDescription(fileChooser.getSelectedFile()));*/
			try {
				Board newBoard = openXmlFile();
				if(newBoard != null)
					return newBoard;
				else {
					return null;
				}
			}catch (Exception e) {
				JOptionPane.showConfirmDialog(frame, "An error occurred opening. "+fileChooser.getSelectedFile()+" was unable to be opened.", "ERROR", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				//e.printStackTrace();
				return null;
			}
		}else {
			return null;
		}
	}

	private Board openXmlFile() throws JAXBException {
		JAXBContext classContent = JAXBContext.newInstance(Board.class, Cell.class);
		Unmarshaller unmarshaller = classContent.createUnmarshaller();
		Board newGameBoard = (Board) unmarshaller.unmarshal(fileChooser.getSelectedFile());
		return newGameBoard;
	}

}