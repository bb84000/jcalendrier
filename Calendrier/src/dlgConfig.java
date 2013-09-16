/*
 * Configuration dialog and processing
 * Load configuration json file at startup
 * Save configuration json file on OK press
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;



public class dlgConfig extends JDialog {

	// Configuration variables
	public String  workingDirectory; 
	public Color colvacA= new Color(255,204,0);
	public Color colvacB= new Color(255,0,0); 
	public Color colvacC= new Color(0,153,0);
	public boolean savePos = false;
	public boolean saveMoon = false;
	public boolean dispMoon = false;
	public boolean saveVacScol = false;
	public boolean dispVacA = false;
	public boolean dispVacB = false;
	public boolean dispVacC = false;
	//public dlgConfig dialog;
	public String config_file = "config.json";
	
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
	public JCheckBox cbMoon;
	public JCheckBox cbVacScol;
	public int mresult;
	public dlgConfig() {

		// load config file
		try {
			loadConfig();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
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
					cbPos.setSelected(savePos);
					panel_1.add(cbPos);
				}
				{
					cbMoon = new JCheckBox("Sauvegarde phases de la Lune");
					cbMoon.setSelected(saveMoon);
					panel_1.add(cbMoon);
				}
				{
					cbVacScol = new JCheckBox("Sauvegarde des vacances scolaires");
					cbVacScol.setSelected(saveVacScol);
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
					saveMoon = cbMoon.isSelected();
					saveVacScol = cbVacScol.isSelected();
					//colvacB= new Color(0,0,255); 
					// Todo launch Configuration file save
					
					saveConfig();
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
	

	public  boolean loadConfig(){
		
		
		try {
		
			FileInputStream json = new FileInputStream(config_file);
			JsonParser jr = Json.createParser(json);
			Event event = null;
			// Advance to "savePos" key
			while(jr.hasNext()) {
				event = jr.next();
				try {
					if (jr.getString().equals("savePos")) {
						event = jr.next();
						savePos = (event==Event.VALUE_TRUE);					
						//cbPos.setSelected(savePos);
					}
					else if (jr.getString().equals("saveMoon")) {
						event = jr.next();
						saveMoon = (event==Event.VALUE_TRUE);					
						//cbMoon.setSelected(saveMoon);
					}
					
					else if (jr.getString().equals("dispMoon")) {
						event = jr.next();
						dispMoon = (event==Event.VALUE_TRUE);					
						//cbMoon.setSelected(saveMoon);
					}
					else if (jr.getString().equals("saveVacScol")) {
						event = jr.next();
						saveVacScol = (event==Event.VALUE_TRUE);					
						//cbVacScol.setSelected(saveVacScol);
					}
					else if (jr.getString().equals("dispVacA")) {
						event = jr.next();
						dispVacA = (event==Event.VALUE_TRUE);					
						//cbVacScol.setSelected(saveVacScol);
					}
					else if (jr.getString().equals("dispVacB")) {
						event = jr.next();
						dispVacB = (event==Event.VALUE_TRUE);					
						//cbVacScol.setSelected(saveVacScol);
					}
					else if (jr.getString().equals("dispVacC")) {
						event = jr.next();
						dispVacC = (event==Event.VALUE_TRUE);					
						//cbVacScol.setSelected(saveVacScol);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();System.out.println(jr.toString());
				}
				
				//if(event == Event.KEY_NAME && "savePos".equals(jr.getString())) {
				//	System.out.println(event.equals(true));
				//	event = jr.next();
				//	break;
				//System.out.println(jr.getString());
				//} 
				// Output contents of "address" object
				
				
				
				
				
			}	
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}   
		
		
	}
	
	public boolean saveConfig() {
		try {
			Map<String, Object> properties = new HashMap<String, Object>(1);
			properties.put(JsonGenerator.PRETTY_PRINTING, true);
			JsonGeneratorFactory factory = Json.createGeneratorFactory(properties);
			//JsonGenerator generator = factory.createGenerator(System.out);
			PrintWriter pw = new PrintWriter(config_file);
			//writer  f = new File("config.json");
			JsonGenerator jg = factory.createGenerator(pw );
			jg.writeStartObject()                    
			.write("savePos", savePos)				// Window state and position
			.write("saveMoon", saveMoon)			// Moon phases save
			.write("dispMoon", dispMoon)			// Moon phases display
			.write("saveVacScol", saveVacScol)		// Scolar holidays save
			.write("dispVacA", dispVacA)			// Zone A display
			.write("dispVacB", dispVacB)
			.write("dispVacC", dispVacC)
			/*.writeStartObject("address")         //    "address":{
			.write("type", 1)                //        "type":1,
			.write("street", "1 A Street")   //        "street":"1 A Street",
			.writeNull("city")               //        "city":null,
			.write("verified", false)        //        "verified":false
			.writeEnd()                          //    },
			.writeStartArray("phone-numbers")    //    "phone-numbers":[
			.writeStartObject()              //        {
			.write("number", "555-1111") //            "number":"555-1111",
			.write("extension", "123")   //            "extension":"123"
			.writeEnd()                      //        },
			.writeStartObject()              //        {
			.write("number", "555-2222") //            "number":"555-2222",
			.writeNull("extension")      //            "extension":null
			.writeEnd()                      //        }
			.writeEnd()                          //    ]*/
			.writeEnd()                              // }
			.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		
		
		
		
	}


}
