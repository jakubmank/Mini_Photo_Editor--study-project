import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class OptionFrame2 {
	static int option;
	//public OptionFrame()
	int GetSelectedOption(){
		return option;
	}
	public static void main(String args[]) {
		JFrame parent = new JFrame();
		int selectedOption = JOptionPane.showConfirmDialog(null,
		"Do you want to save the result?",
		"Choose",
		JOptionPane.YES_NO_OPTION);
		option= selectedOption;
		if (selectedOption == JOptionPane.NO_OPTION) {
			parent.dispose();
		}
	}
}