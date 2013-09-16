/*
 * Configuration dialog and processing
 * Load configuration file at startup
 * Save configuration file on OK press
 */
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class dlgConfig extends JDialog {

	// Configuration variables
	public String  workingDirectory; 
	public Color colvacA= new Color(255,204,0);
	public Color colvacB= new Color(255,0,0); 
	public Color colvacC= new Color(0,153,0);
	public boolean savePos;
	public boolean saveMoon;
	public boolean saveVacScol;
	//public dlgConfig dialog;

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	//public void showdlg ()
	{
		try {
			dlgConfig dialog = new dlgConfig();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setVisible(true);
	//		return true;
		} catch (Exception e) {
			//e.printStackTrace();
//			return false;
		}
	}

	
	public int showDlg (boolean b){
		
	   setVisible(b);
	   return mresult;
	}
	
	/**
	 * Create the dialog.
	 */
	public JButton okButton ;
	public JCheckBox cbPos;
	public JCheckBox cbLune;
	public JCheckBox cbVacScol;
	public int mresult;
	public dlgConfig() {
		
		
		

		
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{434, 0};
		gridBagLayout.rowHeights = new int[]{232, 33, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			getContentPane().add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{216, 0, 0};
			gbl_panel.rowHeights = new int[]{103, 20, 0, 0};
			gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				JPanel panel_1 = new JPanel();
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(BorderFactory.createTitledBorder("Fenêtre"));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.gridx = 1;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				panel_1.setLayout(new GridLayout(0, 1, 0, 0));
				{
					cbPos = new JCheckBox("Sauvegarde position et taille");
					panel_1.add(cbPos);
				}
				{
					cbLune = new JCheckBox("Sauvegarde phases de la Lune");

					panel_1.add(cbLune);
				}
				{
					cbVacScol = new JCheckBox("Sauvegarde des vacances scolaires");

					panel_1.add(cbVacScol);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 1;
			
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					savePos = cbPos.isSelected();
					saveMoon = cbLune.isSelected();
					saveVacScol = cbVacScol.isSelected();
					//colvacB= new Color(0,0,255); 
					// Todo launch Configuration file save
					
				mresult=1;
					setVisible(false);
					
				}
			
			});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					dispose();
				
					}
				});
				
					
				
				buttonPane.add(cancelButton);
			}
		}
	}


}
