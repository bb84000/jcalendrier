import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class aboutBox extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel lblicon;
	public JLabel lblprogname;
	public JLabel lblVersion;
	public JLabel lblvendor;
	
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			aboutBox dialog = new aboutBox(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public aboutBox(JFrame frm) {

		setResizable(false);
		setBounds(100, 100, 360, 286);
		//setTitle("A propos du Calendrier");
		


		setModalityType(ModalityType.APPLICATION_MODAL);
		

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 334, 206);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		// program icon
		lblicon = new JLabel("");
		lblicon.setBounds(20, 23, 32, 32);
		panel.add(lblicon);
		
		// Program name 
		lblprogname = new JLabel("text");
		lblprogname.setHorizontalTextPosition(SwingConstants.CENTER);
		lblprogname.setHorizontalAlignment(SwingConstants.CENTER);
		lblprogname.setBounds(73, 29, 206, 14);
		panel.add(lblprogname);
		
		
		// Version
		lblVersion = new JLabel("text");
		lblVersion.setHorizontalTextPosition(SwingConstants.CENTER);
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersion.setBounds(73, 54, 206, 14);
		panel.add(lblVersion);
		
		
		lblvendor = new JLabel("text");
		lblvendor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblvendor.setHorizontalAlignment(SwingConstants.CENTER);
		lblvendor.setBounds(73, 80, 206, 14);
		panel.add(lblvendor);

		// Button pane
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				okButton.setHorizontalTextPosition(SwingConstants.CENTER);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		
	}
}
