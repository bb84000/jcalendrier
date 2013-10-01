import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dialog.ModalityType;

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
	public JLabel lblicon;
	JLabel lblProgname;
	JLabel lblVersion;
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
		
		setTitle("A propos du Calendrier");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 360, 286);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblicon = new JLabel("");
			lblicon.setBounds(20, 24, 32, 32);
			contentPanel.add(lblicon);
		}
		
		lblProgname = new JLabel("text");
		lblProgname.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgname.setHorizontalTextPosition(SwingConstants.CENTER);
		lblProgname.setBounds(89, 24, 188, 14);
		contentPanel.add(lblProgname);
		{
			lblVersion = new JLabel("text");
			lblVersion.setHorizontalTextPosition(SwingConstants.CENTER);
			lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
			lblVersion.setBounds(89, 49, 188, 14);
			contentPanel.add(lblVersion);
		}
		{
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
}
