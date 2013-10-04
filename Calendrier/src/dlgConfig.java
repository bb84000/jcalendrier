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
import java.awt.Point;

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
import java.util.Properties;

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
import java.awt.Rectangle;



public class dlgConfig extends JDialog {

	
	// Configuration variables
	public String workingDirectory;
	public Dimension size = new Dimension (1200,720);
	public Point location = new Point(0,0);
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

	private JButton okButton ;
	private JCheckBox cbPos;
	private JCheckBox cbMoon;
	private JCheckBox cbVacScol;
	private JButton btnBack;
	private JButton btnSunday;
	private JButton btnZoneA;
	private JButton btnZoneB;
	private JButton btnZoneC;
	private JComboBox<String> cbTown ;
	private JTextField tfLatitude;
	private JTextField tfLongitude;
	
	
	// Config dialog constructor
	public dlgConfig(JFrame frm) {
		setResizable(false);
		setTitle("Pr\u00E9f\u00E9rences");
		// reinit color buttons
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				ncolback= colback;
				btnBack.setBackground(colback);
				ncolsun= colsun;
				btnSunday.setBackground(colsun);
				ncolvacA= colvacA;
				btnZoneA.setBackground(colvacA);
				ncolvacB= colvacB;
				btnZoneB.setBackground(colvacB);
				ncolvacC= colvacC;
				btnZoneC.setBackground(colvacC);
				//System.out.println(e);
			}


		});
		// dialog initialization
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{434, 0};
		gridBagLayout.rowHeights = new int[]{232, 33, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		{
			// Main panel
			JPanel panel_main = new JPanel();
			GridBagConstraints gbc_panel_main = new GridBagConstraints();
			gbc_panel_main.insets = new Insets(0, 0, 5, 0);
			gbc_panel_main.fill = GridBagConstraints.BOTH;
			gbc_panel_main.gridx = 0;
			gbc_panel_main.gridy = 0;
			getContentPane().add(panel_main, gbc_panel_main);
			GridBagLayout gbl_panel_main = new GridBagLayout();
			gbl_panel_main.columnWidths = new int[]{220, 220, 0};
			gbl_panel_main.rowHeights = new int[]{94, 116, 0, 0};
			gbl_panel_main.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_panel_main.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
			panel_main.setLayout(gbl_panel_main);
			
			{	// Colors panel
				JPanel panel_colors = new JPanel();
				panel_colors.setBorder(new TitledBorder(null, "Couleurs", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
				panel_colors.setLayout(null);
				GridBagConstraints gbc_panel_colors = new GridBagConstraints();
				gbc_panel_colors.insets = new Insets(5, 5, 0, 0);
				gbc_panel_colors.fill = GridBagConstraints.BOTH;
				gbc_panel_colors.gridx = 0;
				gbc_panel_colors.gridy = 0;
				panel_main.add(panel_colors, gbc_panel_colors);
				
				// Event processor for all color buttons
				MouseAdapter macolor = new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent arg0) {
						choosecolor(arg0.getComponent());
					}
				};
				
				// Color labels and buttons
				JLabel lblBack = new JLabel("Fond");
				lblBack.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblBack.setBounds(10, 19, 46, 14);
				panel_colors.add(lblBack);
				
				btnBack = new JButton("");
				btnBack.setMinimumSize(new Dimension(33, 7));
				btnBack.setName("back");
				btnBack.addMouseListener(macolor);
				btnBack.setBounds(62, 15, 39, 20);
				btnBack.setBackground(colback);
				panel_colors.add(btnBack);

				JLabel lblSunday = new JLabel("Dimanches");
				lblSunday.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblSunday.setBounds(10, 42, 57, 14);
				panel_colors.add(lblSunday);

				btnSunday = new JButton("");
				btnSunday.setName("sund");
				btnSunday.addMouseListener(macolor);
				btnSunday.setBounds(62, 38, 39, 20);
				btnSunday.setBackground(colsun);
				panel_colors.add(btnSunday);

				JLabel lblZoneA = new JLabel("Zone A");
				lblZoneA.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblZoneA.setBounds(115, 19, 46, 14);
				panel_colors.add(lblZoneA);

				btnZoneA = new JButton("");
				btnZoneA.setName("vacA");
				btnZoneA.addMouseListener(macolor);
				btnZoneA.setBackground(colvacA);
				btnZoneA.setBounds(163, 15, 39, 20);
				panel_colors.add(btnZoneA);


				JLabel lblZoneB = new JLabel("Zone B");
				lblZoneB.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblZoneB.setBounds(115, 42, 46, 14);
				panel_colors.add(lblZoneB);	

				btnZoneB = new JButton("");
				btnZoneB.setName("vacB");
				btnZoneB.addMouseListener(macolor);
				btnZoneB.setBackground(colvacB);
				btnZoneB.setBounds(163, 38, 39, 20);
				panel_colors.add(btnZoneB);


				JLabel lblZoneC = new JLabel("Zone C");
				lblZoneC.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblZoneC.setBounds(115, 65, 46, 14);
				panel_colors.add(lblZoneC);

				btnZoneC = new JButton("");
				btnZoneC.setName("vacC");
				btnZoneC.addMouseListener(macolor);
				btnZoneC.setBackground(colvacC);
				btnZoneC.setBounds(163, 61, 39, 20);
				panel_colors.add(btnZoneC);


			}
			// Panel Display 
			JPanel panel_display = new JPanel();
			panel_display.setBorder(new TitledBorder(null, "Affichage", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
			panel_display.setLayout(null);
			GridBagConstraints gbc_panel_display = new GridBagConstraints();
			gbc_panel_display.insets = new Insets(5, 3, 0, 2);
			gbc_panel_display.fill = GridBagConstraints.BOTH;
			gbc_panel_display.gridx = 1;
			gbc_panel_display.gridy = 0;
			panel_main.add(panel_display, gbc_panel_display);

			cbPos = new JCheckBox("Sauvegarde position et taille");
			cbPos.setSelected(false);
			cbPos.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbPos.setBounds(6, 15, 200, 23);
			panel_display.add(cbPos);

			cbMoon = new JCheckBox("Sauvegarde phases de la Lune");
			cbMoon.setSelected(false);
			cbMoon.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbMoon.setBounds(6, 38, 200, 23);
			panel_display.add(cbMoon);

			cbVacScol = new JCheckBox("Sauvegarde des vacances scolaires");
			cbVacScol.setSelected(false);
			cbVacScol.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbVacScol.setBounds(6, 61, 200, 23);
			panel_display.add(cbVacScol);

			JPanel panel_coord = new JPanel();
			panel_coord.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_coord.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Coordonn\u00E9es pour le lever et le coucher du soleil",
					TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
			panel_coord.setLayout(null);
			GridBagConstraints gbc_panel_coord = new GridBagConstraints();
			gbc_panel_coord.gridwidth = 2;
			gbc_panel_coord.insets = new Insets(0, 5, 0, 2);
			gbc_panel_coord.fill = GridBagConstraints.BOTH;
			gbc_panel_coord.gridx = 0;
			gbc_panel_coord.gridy = 1;
			panel_main.add(panel_coord, gbc_panel_coord);

			JLabel lblTown = new JLabel("Ville");
			lblTown.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblTown.setBounds(10, 28, 50, 14);
			panel_coord.add(lblTown);

			// Combo box towns

			cbTown = new JComboBox<String>();
			cbTown.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbTown.setBounds(65, 25, 202, 20);
			cbTown.setPreferredSize(new Dimension(120, 20));
			cbTown.setMaximumRowCount(100);
			panel_coord.add(cbTown);			
			
			// Event processing cb town
			cbTown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					changetown();

				}
			});

			// Labels and tect boxes for latitude and longitude

			JLabel lblLatitude = new JLabel("Latitude");
			lblLatitude.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLatitude.setBounds(10, 53, 50, 14);
			panel_coord.add(lblLatitude);

			tfLatitude = new JTextField();
			tfLatitude.setBounds(65, 50, 202, 20);
			panel_coord.add(tfLatitude);
			tfLatitude.setColumns(10);

			JLabel lblLongitude = new JLabel("Longitude");
			lblLongitude.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLongitude.setBounds(10, 78, 50, 14);
			panel_coord.add(lblLongitude);

			tfLongitude = new JTextField();
			tfLongitude.setColumns(10);
			tfLongitude.setBounds(65, 75, 202, 20);
			panel_coord.add(tfLongitude);
		}
		{
			// Button pane
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 1;

			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				okButton = new JButton("OK");
				okButton.setFont(new Font("Tahoma", Font.BOLD, 11));
				okButton.setPreferredSize(new Dimension(65, 23));
				okButton.setMinimumSize(new Dimension(65, 23));
				okButton.setMaximumSize(new Dimension(65, 23));
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
						saveConfig();
						setVisible(false);
					}

				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.setMargin(new Insets(2, 5, 2, 5));
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 11));
				cancelButton.setPreferredSize(new Dimension(65, 23));
				cancelButton.setMinimumSize(new Dimension(65, 23));
				cancelButton.setMaximumSize(new Dimension(65, 23));
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
msg += "Standard : Les donn�es de configuration sont stocl�es dans le r�pertoire utilisateur.\n";
msg += "Portable : les donn�es de configuration sont stock�es dans le r�pertoire courant.";
int ret = JOptionPane.showOptionDialog(null, msg, "Calendrier", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, BtnCaptions, "");
if (ret==0)
// store in user folder otherwise store in current folder*/

				File folderExisting = new File(workingDirectory);
				if (!folderExisting.exists()) {
					boolean success = (new File(workingDirectory)).mkdirs();
					if (success) config_file= workingDirectory+"/"+config_file;
					else JOptionPane.showMessageDialog(null, "Impossible de cr�er le dossier de l'application");
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
			String [] element = it.next();
			cbTown.addItem(element [0]);
		}
		
		// Read version infos properties 
		Properties versinfo = new Properties();
		try {
			InputStream in = Calendrier.class.getResourceAsStream("/resources/version.properties");
			versinfo.load(in);
			version = versinfo.getProperty("Specification-Version","n/a");
			build = versinfo.getProperty("Implementation-Version");
			String bldate = versinfo.getProperty("Build-Date","");
			vendor = versinfo.getProperty("Specification-Vendor","");
			in.close();
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			builddate= formatter.parseDateTime(bldate);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// end version info properties

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
						if (event==Event.VALUE_NUMBER) location.x = jr.getInt();	
					}
					else if (jr.getString().equals("locatY")) {
						event = jr.next();
						if (event==Event.VALUE_NUMBER) location.y = jr.getInt();	
					}
					else if (jr.getString().equals("sizeW")) {
						event = jr.next();
						if (event==Event.VALUE_NUMBER) size.width = jr.getInt();	
					}
					else if (jr.getString().equals("sizeH")) {
						event = jr.next();
						if (event==Event.VALUE_NUMBER) size.height = jr.getInt();	
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
						btnBack.setBackground(colback);
					}
					else if (jr.getString().equals("colsun")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colsun = new Color(Integer.parseInt(jr.getString(),16));	
						btnSunday.setBackground(colsun);
					}
					else if (jr.getString().equals("colvacA")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colvacA = new Color(Integer.parseInt(jr.getString(),16));	
						btnZoneA.setBackground(colvacA);
					}
					else if (jr.getString().equals("colvacB")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colvacB = new Color(Integer.parseInt(jr.getString(),16));
						btnZoneB.setBackground(colvacB);
					}
					else if (jr.getString().equals("colvacC")) {
						event = jr.next();
						if (event==Event.VALUE_STRING) colvacC = new Color(Integer.parseInt(jr.getString(),16));	
						btnZoneC.setBackground(colvacC);
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
			.write("locatX", location.x)
			.write("locatY", location.y)
			.write ("sizeW", size.width)
			.write ("sizeH", size.height)
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

	
}