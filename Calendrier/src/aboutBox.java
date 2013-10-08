import java.awt.BorderLayout;
import java.awt.Desktop;
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
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.Color;
import javax.swing.border.EtchedBorder;


public class aboutBox extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel lblicon;
	public JLabel lblprogname;
	public JLabel lblVersion;
	public JLabel lblvendor;
	public JLabel lblWebsite;
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
		setBounds(100, 100, 360, 230);
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
		lblprogname.setBounds(0, 30, 334, 14);
		panel.add(lblprogname);
		
		
		// Version
		lblVersion = new JLabel("text");
		lblVersion.setHorizontalTextPosition(SwingConstants.CENTER);
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersion.setBounds(0, 55, 334, 14);
		panel.add(lblVersion);
		
		
		lblvendor = new JLabel("text");
		lblvendor.setHorizontalTextPosition(SwingConstants.CENTER);
		lblvendor.setHorizontalAlignment(SwingConstants.CENTER);
		lblvendor.setBounds(0, 80, 334, 14);
		panel.add(lblvendor);
		
		JPanel panel_website = new JPanel();
		panel_website.setAlignmentY(0.0f);
		panel_website.setAlignmentX(0.0f);
		panel_website.setBounds(0, 105, 334, 14);
		panel.add(panel_website);
		((FlowLayout)panel_website.getLayout()).setVgap(0);
		
		JLabel lblsite = new JLabel("Site Web :");
		lblsite.setBounds(new Rectangle(0, 0, 0, 14));
		lblsite.setHorizontalTextPosition(SwingConstants.CENTER);
		lblsite.setAlignmentY(0.0f);
		panel_website.add(lblsite);
		
		lblWebsite = new JLabel("New label");
		lblWebsite.setForeground(Color.BLUE);
		lblWebsite.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblWebsite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
	            JLabel jl = (JLabel) ev.getComponent();
				try {
	                Desktop.getDesktop().browse(new URI(jl.getText()));
	            } catch (IOException | URISyntaxException e) {
	                // TODO Auto-generated catch block
	            }
			}
		});
		panel_website.add(lblWebsite);

		// Button pane
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
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
