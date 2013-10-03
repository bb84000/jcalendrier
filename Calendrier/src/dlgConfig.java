/*
 * Configuration dialog and processing
 * Load configuration json file at startup
 * Save configuration json file on OK press
 */
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.swing.JLabel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JTextField;



public class dlgConfig extends JDialog {

	// Configuration variables
	public String workingDirectory;
	public int locatX =100;
	public int locatY = 100;
	public int sizeW = 1180;
	public int sizeH = 720;
	public Color colback= new Color(255,255,255);
	private Color ncolback;
	public Color colsun= new Color(192,255,255);
	private Color ncolsun;
	public Color colvacA= new Color(255,204,0);
	private Color ncolvacA;
	public Color colvacB= new Color(255,0,0);
	private Color ncolvacB;
	public Color colvacC= new Color(0,153,0);
	private Color ncolvacC;
	public boolean savePos = false;
	public boolean saveMoon = false;
	public boolean dispMoon = false;
	public boolean saveVacScol = false;
	public boolean dispVacA = false;
	public boolean dispVacB = false;
	public boolean dispVacC = false;
	public double Latitude = 48.86223;	//Paris
	private double nLatitude;
	public double Longitude = 2.351074;	//Paris
	private double nLongitude;
	public String version = "";
	public String build ="";
	public DateTime builddate = null;
	public String vendor = "";
	private bArrayList towns;
	private String town;
	private String ntown;
	public ArrayList <String[]> manifest= null;
	private String config_file = "config.json";

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	//public void showdlg ()
	{
		try {
			dlgConfig dialog = new dlgConfig(null);

			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			//dialog.
			// return true;
		} catch (Exception e) {
			//e.printStackTrace();
			// return false;
		}
	}


	public int showDlg (boolean b){

		setVisible(b);
		return mresult;
	}

	/**
	 *
	 */


	public JButton okButton ;
	public JCheckBox cbPos;
	public JCheckBox cbMoon;
	public JCheckBox cbVacScol; 
	public JButton btnback;
	public JButton btnsunday;
	public JButton btnzoneA;
	public JButton btnzoneB;
	public JButton btnzoneC;
	public JComboBox<String> cbTown ;
	public int mresult;
	private JTextField tfLatitude;
	private JTextField tfLongitude;
	public dlgConfig(JFrame frm) {
		setResizable(false);
		setTitle("Pr\u00E9f\u00E9rences");
		// reinit color buttons
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				ncolback= colback;
				btnback.setBackground(colback);
				ncolsun= colsun;
				btnsunday.setBackground(colsun);
				ncolvacA= colvacA;
				btnzoneA.setBackground(colvacA);
				ncolvacB= colvacB;
				btnzoneB.setBackground(colvacB);
				ncolvacC= colvacC;
				btnzoneC.setBackground(colvacC);
				//System.out.println(e);
			}


		});
		setModalityType(ModalityType.APPLICATION_MODAL);
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
			gbl_panel.columnWidths = new int[]{220, 220, 0};
			gbl_panel.rowHeights = new int[]{94, 116, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				/*JPanel panel_1 = new JPanel();
GridBagConstraints gbc_panel_1 = new GridBagConstraints();
gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
gbc_panel_1.insets = new Insets(0, 0, 5, 5);
gbc_panel_1.gridx = 0;
gbc_panel_1.gridy = 0;
panel.add(panel_1, gbc_panel_1);*/
			}
			{
				JPanel panel_colors = new JPanel();
				panel_colors.setBorder(new TitledBorder(null, "Couleurs", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
				panel_colors.setLayout(null);
				GridBagConstraints gbc_panel_colors = new GridBagConstraints();
				gbc_panel_colors.insets = new Insets(5, 5, 0, 0);
				gbc_panel_colors.fill = GridBagConstraints.BOTH;
				gbc_panel_colors.gridx = 0;
				gbc_panel_colors.gridy = 0;
				panel.add(panel_colors, gbc_panel_colors);

				JLabel lblFond = new JLabel("Fond");
				lblFond.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblFond.setBounds(10, 19, 46, 14);
				panel_colors.add(lblFond);

				MouseAdapter ma = new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent arg0) {

						choosecolor(arg0.getComponent());
					}
				};
				btnback = new JButton("");
				btnback.setMinimumSize(new Dimension(33, 7));
				btnback.setName("back");
				btnback.addMouseListener(ma);
				btnback.setBounds(62, 15, 39, 20);
				btnback.setBackground(colback);
				panel_colors.add(btnback);

				JLabel lblDimanches = new JLabel("Dimanches");
				lblDimanches.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblDimanches.setBounds(10, 42, 57, 14);
				panel_colors.add(lblDimanches);

				btnsunday = new JButton("");
				btnsunday.setName("sund");
				btnsunday.addMouseListener(ma);
				btnsunday.setBounds(62, 38, 39, 20);
				btnsunday.setBackground(colsun);
				panel_colors.add(btnsunday);

				JLabel lblZoneA = new JLabel("Zone A");
				lblZoneA.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblZoneA.setBounds(115, 19, 46, 14);
				panel_colors.add(lblZoneA);

				btnzoneA = new JButton("");
				btnzoneA.setName("vacA");
				btnzoneA.addMouseListener(ma);
				btnzoneA.setBackground(colvacA);
				btnzoneA.setBounds(163, 15, 39, 20);
				panel_colors.add(btnzoneA);


				JLabel lblZoneB = new JLabel("Zone B");
				lblZoneB.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblZoneB.setBounds(115, 42, 57, 14);
				panel_colors.add(lblZoneB);	

				btnzoneB = new JButton("");
				btnzoneB.setName("vacB");
				btnzoneB.addMouseListener(ma);
				btnzoneB.setBackground(colvacB);
				btnzoneB.setBounds(163, 38, 39, 20);
				panel_colors.add(btnzoneB);


				JLabel lblZoneC = new JLabel("Zone C");
				lblZoneC.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblZoneC.setBounds(115, 65, 57, 14);
				panel_colors.add(lblZoneC);

				btnzoneC = new JButton("");
				btnzoneC.setName("vacC");
				btnzoneC.addMouseListener(ma);
				btnzoneC.setBackground(colvacC);
				btnzoneC.setBounds(163, 61, 39, 20);
				panel_colors.add(btnzoneC);


			}
			
			JPanel panel_3 = new JPanel();
			panel_3.setBorder(new TitledBorder(null, "Fen\u00EAtre", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
			panel_3.setLayout(null);
			GridBagConstraints gbc_panel_3 = new GridBagConstraints();
			gbc_panel_3.insets = new Insets(5, 3, 0, 2);
			gbc_panel_3.fill = GridBagConstraints.BOTH;
			gbc_panel_3.gridx = 1;
			gbc_panel_3.gridy = 0;
			panel.add(panel_3, gbc_panel_3);
			
			cbPos = new JCheckBox("Sauvegarde position et taille");
			cbPos.setSelected(false);
			cbPos.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbPos.setBounds(6, 15, 200, 23);
			panel_3.add(cbPos);
			
			cbMoon = new JCheckBox("Sauvegarde phases de la Lune");
			cbMoon.setSelected(false);
			cbMoon.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbMoon.setBounds(6, 38, 200, 23);
			panel_3.add(cbMoon);
			
			cbVacScol = new JCheckBox("Sauvegarde des vacances scolaires");
			cbVacScol.setSelected(false);
			cbVacScol.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbVacScol.setBounds(6, 61, 200, 23);
			panel_3.add(cbVacScol);
			
			JPanel panel_1 = new JPanel();
			panel_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Coordonn\u00E9es pour le lever et le coucher du soleil", 
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
			panel_1.setLayout(null);
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.gridwidth = 2;
			gbc_panel_1.insets = new Insets(0, 5, 0, 2);
			gbc_panel_1.fill = GridBagConstraints.BOTH;
			gbc_panel_1.gridx = 0;
			gbc_panel_1.gridy = 1;
			panel.add(panel_1, gbc_panel_1);
			
			JLabel lblTown = new JLabel("Ville");
			lblTown.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblTown.setBounds(10, 28, 50, 14);
			panel_1.add(lblTown);
					
			// Combo box towns
			
			cbTown = new JComboBox<String>();
			cbTown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					changetown();
					
				}
			});

			cbTown.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbTown.setBounds(65, 25, 202, 20);
			cbTown.setPreferredSize(new Dimension(120, 20));
			cbTown.setMaximumRowCount(100);
			panel_1.add(cbTown);
			
			JLabel lblLatitude = new JLabel("Latitude");
			lblLatitude.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLatitude.setBounds(10, 53, 50, 14);
			panel_1.add(lblLatitude);
			
			tfLatitude = new JTextField();
			tfLatitude.setBounds(65, 50, 202, 20);
			panel_1.add(tfLatitude);
			tfLatitude.setColumns(10);
			
			JLabel lblLongitude = new JLabel("Longitude");
			lblLongitude.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLongitude.setBounds(10, 78, 50, 14);
			panel_1.add(lblLongitude);
			
			tfLongitude = new JTextField();
			tfLongitude.setColumns(10);
			tfLongitude.setBounds(65, 75, 202, 20);
			panel_1.add(tfLongitude);
			
			

			
			
			JPanel panel_2 = new JPanel();
			GridBagConstraints gbc_panel_2 = new GridBagConstraints();
			gbc_panel_2.gridwidth = 2;
			gbc_panel_2.fill = GridBagConstraints.BOTH;
			gbc_panel_2.gridx = 0;
			gbc_panel_2.gridy = 2;
			panel.add(panel_2, gbc_panel_2);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
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
						colback= ncolback;
						colsun= ncolsun;
						colvacA= ncolvacA;
						colvacB= ncolvacB;
						colvacC= ncolvacC;
						town = ntown;
						Latitude = nLatitude;
						Longitude = nLongitude;
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
	
	private void changetown () {
		if (!towns.isEmpty()) {
			tfLatitude.setText(towns.get(cbTown.getSelectedIndex())[2]);
			nLatitude = Double.parseDouble(String.valueOf(towns.get(cbTown.getSelectedIndex())[2]));
			tfLongitude.setText(towns.get(cbTown.getSelectedIndex())[3]);
			nLongitude = Double.parseDouble(String.valueOf(towns.get(cbTown.getSelectedIndex())[3]));
			ntown= towns.get(cbTown.getSelectedIndex())[0];
			
		}
	}

	private void choosecolor (Component c) {
		Color col;
		//c.setBackground(colback);
		col = JColorChooser.showDialog(this, "Choix de couleur", null);

		if (c.getName().equals("back")) ncolback = col;
		else if (c.getName().equals("sund")) ncolsun = col;
		else if (c.getName().equals("vacA")) ncolvacA = col;
		else if (c.getName().equals("vacB")) ncolvacB = col;
		else if (c.getName().equals("vacC")) ncolvacC = col;
		c.setBackground(col);
	}

	// Find and/or create the configuration directory
	public void set_config_file(String s) {
		config_file = s;
		// Working directory
		String OS = (System.getProperty("os.name")).toUpperCase();
		if (OS.contains("WIN")) workingDirectory = System.getenv("AppData");	// Win location of the "AppData" folder
		else if (OS.contains("MAC")) workingDirectory = System.getProperty("user.home")+"/Library/Application Support"; // Mac, look for "Application Support"
		else workingDirectory = System.getProperty("user.home"); //Otherwise, we assume Linux
		workingDirectory += "/calendrier";	
		// check where is the config file
		File f = new File(config_file);
		if (!f.exists()) {
			f = new File (workingDirectory+"/"+config_file);
			if (f.exists()) config_file= workingDirectory+"/"+config_file;
			else {
				/*//config file not found. Ask user if it wants standard or portable operation not working in Linux
String BtnCaptions[]={ "Standard", "Portable"};
String msg = new String("Choix du mode de fonctionnement\n");
msg += "Standard : Les données de configuration sont stoclées dans le répertoire utilisateur.\n";
msg += "Portable : les données de configuration sont stockées dans le répertoire courant.";
int ret = JOptionPane.showOptionDialog(null, msg, "Calendrier", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, BtnCaptions, "");
if (ret==0)
// store in user folder otherwise store in current folder*/

				File folderExisting = new File(workingDirectory);
				if (!folderExisting.exists()) {
					boolean success = (new File(workingDirectory)).mkdirs();
					if (success) config_file= workingDirectory+"/"+config_file;
					else JOptionPane.showMessageDialog(null, "Impossible de créer le dossier de l'application");
				}
				else config_file= workingDirectory+"/"+config_file;

			}

		}
		
		// Read town file;
		towns = new bArrayList();
		towns.readCSVstream(ClassLoader.class.getResourceAsStream("/resources/villes.csv" ),"cp1252");
    	towns.sort(0);
		Iterator<String[]> it = towns.iterator();
    	while(it.hasNext()) {
    		String [] element =  it.next();
    		cbTown.addItem(element [0]);
    	}
		
		
		
		
		// Read Manifest file
		String bldate= "";
		manifest = readManifest("/META-INF/MANIFEST.MF");
		if (!manifest.isEmpty()){
			Iterator<String[]> itr = manifest.iterator();
			while(itr.hasNext()) {
				String [] element = itr.next();
				//System.out.println(element[0]);
				if (element[0].equals("Specification-Version")) version= element[1].trim();
				else if (element[0].equals("Implementation-Version")) build= element[1].trim();
				else if (element[0].equals("Build-Date")) bldate= element[1].trim();
				else if (element[0].equals("Specification-Vendor")) vendor= element[1].trim();
			}
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			builddate= new DateTime();
			try {
				builddate= formatter.parseDateTime(bldate);
			} catch (Exception e) {
				builddate = null;
				//e.printStackTrace();
			}
		}
	}

	public boolean loadConfig(){


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
						cbPos.setSelected(savePos);
					}
					else if (jr.getString().equals("locatX")) {
						event = jr.next();
						if (event==Event.VALUE_NUMBER) locatX = jr.getInt();	
					}
					else if (jr.getString().equals("locatY")) {
						event = jr.next();
						if (event==Event.VALUE_NUMBER) locatY = jr.getInt();	
					}
					else if (jr.getString().equals("sizeW")) {
						event = jr.next();
						if (event==Event.VALUE_NUMBER) sizeW = jr.getInt();	
					}
					else if (jr.getString().equals("sizeH")) {
						event = jr.next();
						if (event==Event.VALUE_NUMBER) sizeH = jr.getInt();	
					}
					else if (jr.getString().equals("saveMoon")) {
						event = jr.next();
						saveMoon = (event==Event.VALUE_TRUE);	
						cbMoon.setSelected(saveMoon);
					}

					else if (jr.getString().equals("dispMoon")) {
						event = jr.next();
						dispMoon = (event==Event.VALUE_TRUE);	
					}
					else if (jr.getString().equals("saveVacScol")) {
						event = jr.next();
						saveVacScol = (event==Event.VALUE_TRUE);	
						cbVacScol.setSelected(saveVacScol);
					}
					else if (jr.getString().equals("dispVacA")) {
						event = jr.next();
						dispVacA = (event==Event.VALUE_TRUE);	
					}
					else if (jr.getString().equals("dispVacB")) {
						event = jr.next();
						dispVacB = (event==Event.VALUE_TRUE);	
					}
					else if (jr.getString().equals("dispVacC")) {
						event = jr.next();
						dispVacC = (event==Event.VALUE_TRUE);	
					}
					else if (jr.getString().equals("colback")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colback = new Color(Integer.parseInt(jr.getString(),16));
						btnback.setBackground(colback);
					}
					else if (jr.getString().equals("colsun")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colsun = new Color(Integer.parseInt(jr.getString(),16));	
						btnsunday.setBackground(colsun);
					}
					else if (jr.getString().equals("colvacA")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colvacA = new Color(Integer.parseInt(jr.getString(),16));	
						btnzoneA.setBackground(colvacA);
					}
					else if (jr.getString().equals("colvacB")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colvacB = new Color(Integer.parseInt(jr.getString(),16));
						btnzoneB.setBackground(colvacB);
					}
					else if (jr.getString().equals("colvacC")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colvacC = new Color(Integer.parseInt(jr.getString(),16));	
						btnzoneC.setBackground(colvacC);
					}
					else if (jr.getString().equals("latitude")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) nLatitude = Double.parseDouble(jr.getString());	
					}
					else if (jr.getString().equals("longitude")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) nLongitude = Double.parseDouble(jr.getString());	
					}
					else if (jr.getString().equals("town")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) ntown = jr.getString();	
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();System.out.println(jr.toString());
				}
			}
			// Search towns list for town
			if (!towns.isEmpty()) {
				for (int i= 0; i< towns.size(); i+=1) { 
					if (ntown.equals(towns.get(i)[0])) {
						town=towns.get(i)[0];
						cbTown.setSelectedIndex(i);
						tfLatitude.setText(towns.get(i)[2]);
						tfLongitude.setText(towns.get(i)[3]);
						Latitude = Double.parseDouble(String.valueOf(towns.get(i)[2]));
						Longitude = Double.parseDouble(String.valueOf(towns.get(i)[3]));
						break;
					}
				}
			}	
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	public boolean saveConfig() {
		try {
			Map<String, Object> properties = new HashMap<String, Object>(1);
			properties.put(JsonGenerator.PRETTY_PRINTING, true);
			JsonGeneratorFactory factory = Json.createGeneratorFactory(properties);
			PrintWriter pw = new PrintWriter(config_file);
			JsonGenerator jg = factory.createGenerator(pw );
			jg.writeStartObject()
				.writeStartObject("config") // Window state and position
					.writeStartObject("Display")
						.writeStartObject("Window")
							.write("savePos", savePos)
							.write("locatX", locatX)
							.write("locatY", locatY)
							.write ("sizeW", sizeW)
							.write ("sizeH", sizeH)
						.writeEnd()	
						.writeStartObject("Moon")
							.write("saveMoon", saveMoon)	// Moon phases save
							.write("dispMoon", dispMoon)	// Moon phases display
						.writeEnd()
						.writeStartObject("ScolarHolidays")
							.write("saveVacScol", saveVacScol)	// Scolar holidays save
							.write("dispVacA", dispVacA)	// Zone A display
							.write("dispVacB", dispVacB)
							.write("dispVacC", dispVacC)
						.writeEnd()
					.writeEnd()
					.writeStartObject("Colors")
						.write("colback", String.format("%06X", colback.getRGB()& 0xffffff))
						.write("colsun", String.format("%06X", colsun.getRGB()& 0xffffff))
						.write("colvacA", String.format("%06X", colvacA.getRGB()& 0xffffff))
						.write("colvacB", String.format("%06X", colvacB.getRGB()& 0xffffff))
						.write("colvacC", String.format("%06X", colvacC.getRGB()& 0xffffff))
					.writeEnd()
					.writeStartObject("Location")
						.write("latitude", String.valueOf(Latitude))
						.write("longitude", String.valueOf(Longitude))
						.write("town", town)
					.writeEnd()
				.writeEnd()
			.writeEnd()   
			.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	// Manifest

	public ArrayList <String[]> readManifest (String filename){
		// create an arraylist ot get the fields
		ArrayList<String[]> list = new ArrayList<>();

		try {
			InputStream is = Calendrier.class.getResourceAsStream(filename);	
			InputStreamReader r = new InputStreamReader(is);
			BufferedReader MFFile = new BufferedReader(r);
			String dataRow = MFFile.readLine();
			while (dataRow.length()>0) {
				int p = dataRow.indexOf(":");
				if (p>=0) {
					String[] row = new String [2];
					row[0]= dataRow.substring(0, p);
					row[1]= dataRow.substring(p+1);
					list.add(row);
				}
				dataRow = MFFile.readLine(); // Read next line of data.
			}
			// Close the file once all data has been read.
			MFFile.close();
			return list;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return null;
		}


	}
}