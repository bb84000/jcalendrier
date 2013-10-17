/*
 * Configuration dialog and processing
 * read property file containing version informations
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bb.arraylist.*;

// Config dialog main class
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
	public boolean chknewver = true;
	public DateTime  lastupdchk = new DateTime(2013,10,1,0,0,1);
	private double ParisLat = 48.86223;	//Paris
	public double Latitude = ParisLat;
	private double ParisLon = 2.351074;	//Paris
	public double Longitude = ParisLon;
	private double nLatitude;
	private double nLongitude;
	public String version = "";
	public String build ="";
	public DateTime builddate = null;
	public String vendor = "";
	private bArrayList towns;
	private String town;
	private String ntown;
	private int townind;
	private int ntownind;
	public ArrayList <String[]> manifest= null;
	private String config_file = "calendrier.config.xml";
	private ActionListener al;
	private DocumentListener dl;
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
	private JCheckBox cbChknewver;
	private JCheckBox cbVacScol;
	private JButton btnBack;
	private JButton btnSunday;
	private JButton btnZoneA;
	private JButton btnZoneB;
	private JButton btnZoneC;
	private JComboBox<String> cbTown ;
	private JTextField tfLatitude;
	private JTextField tfLongitude;
	private JLabel lblpath;
	
	// Config dialog constructor
	public dlgConfig(JFrame frm) {
		getContentPane().setPreferredSize(new Dimension(0, 15));
		setResizable(false);
		setTitle("Pr\u00E9f\u00E9rences");
		
		// reinit settings at launch
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
				nLatitude= Latitude;
				nLongitude= Longitude;
				tfLatitude.getDocument().removeDocumentListener(dl);
				tfLongitude.getDocument().removeDocumentListener(dl);		
				tfLatitude.setText(String.valueOf(Latitude));
				tfLongitude.setText(String.valueOf(Longitude));
				tfLatitude.getDocument().addDocumentListener(dl);
				tfLongitude.getDocument().addDocumentListener(dl);	
				ntownind= townind;
				cbTown.removeActionListener(al);
				try {
					cbTown.setSelectedIndex(townind);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				cbTown.addActionListener(al);
				
			}
		});
		
		// dialog initialization
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 366);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{434, 0};
		gridBagLayout.rowHeights = new int[]{232, 33, 15, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
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
			gbl_panel_main.rowHeights = new int[]{94, 110, 50, 0};
			gbl_panel_main.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_panel_main.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			panel_main.setLayout(gbl_panel_main);
			
			{	// Colors panel
				JPanel panel_colors = new JPanel();
				panel_colors.setBorder(new TitledBorder(null, "Couleurs", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
				panel_colors.setLayout(null);
				GridBagConstraints gbc_panel_colors = new GridBagConstraints();
				gbc_panel_colors.insets = new Insets(5, 5, 5, 5);
				gbc_panel_colors.fill = GridBagConstraints.BOTH;
				gbc_panel_colors.gridx = 0;
				gbc_panel_colors.gridy = 0;
				panel_main.add(panel_colors, gbc_panel_colors);
				
				// Event processor for all color buttons
				// compnent passed by parameter to choosecolor method
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
			} // end color panel
			
			// Panel Display 
			JPanel panel_display = new JPanel();
			panel_display.setBorder(new TitledBorder(null, "Affichage", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
			panel_display.setLayout(null);
			GridBagConstraints gbc_panel_display = new GridBagConstraints();
			gbc_panel_display.insets = new Insets(5, 3, 5, 4);
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
			cbMoon.setBounds(6, 35, 200, 23);
			panel_display.add(cbMoon);

			cbVacScol = new JCheckBox("Sauvegarde des vacances scolaires");
			cbVacScol.setSelected(false);
			cbVacScol.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbVacScol.setBounds(6, 55, 200, 23);
			panel_display.add(cbVacScol);
			
			cbChknewver = new JCheckBox("Recherche de mise \u00E0 jour");
			cbChknewver.setSelected(false);
			cbChknewver.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbChknewver.setBounds(6, 75, 200, 23);
			panel_display.add(cbChknewver);

			JPanel panel_coord = new JPanel();
			panel_coord.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_coord.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Coordonn\u00E9es pour le lever et le coucher du soleil",
					TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
			panel_coord.setLayout(null);
			GridBagConstraints gbc_panel_coord = new GridBagConstraints();
			gbc_panel_coord.gridwidth = 2;
			gbc_panel_coord.insets = new Insets(0, 5, 5, 4);
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
			al = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					changetown();
				}
			};
			// Event processing of towns combo box


			dl = new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					warn(e);
				}

				public void removeUpdate(DocumentEvent e) {
					warn(e);
				}

				public void insertUpdate(DocumentEvent e) {
					warn(e);
				}

				public void warn(DocumentEvent e) {
					//System.out.println(e.getDocument().getProperty("owner"));
						
					changecoord(e); 
					//String syear = YearField.getText();
						//yearchanged(syear);
				}
			};
			//YearField.getDocument().addDocumentListener(dl);
			
			// Labels and text boxes for latitude and longitude

			JLabel lblLatitude = new JLabel("Latitude");
			lblLatitude.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLatitude.setBounds(10, 53, 50, 14);
			panel_coord.add(lblLatitude);

			tfLatitude = new JTextField();
			tfLatitude.setBounds(65, 50, 202, 20);
			panel_coord.add(tfLatitude);
			tfLatitude.setColumns(10);
			tfLatitude.getDocument().putProperty("owner", "lat"); 
			

			JLabel lblLongitude = new JLabel("Longitude");
			lblLongitude.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLongitude.setBounds(10, 78, 50, 14);
			panel_coord.add(lblLongitude);

			tfLongitude = new JTextField();
			tfLongitude.setColumns(10);
			tfLongitude.setBounds(65, 75, 202, 20);
			tfLongitude.getDocument().putProperty("owner", "lon"); 
			panel_coord.add(tfLongitude);
			
			JPanel panel_path = new JPanel();
			panel_path.setFont(new Font("Tahoma", Font.BOLD, 11));
			panel_path.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Emplacement des fichiers de configuration", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
			panel_path.setLayout(null);
			GridBagConstraints gbc_panel_path = new GridBagConstraints();
			gbc_panel_path.gridwidth = 2;
			gbc_panel_path.insets = new Insets(0, 5, 3, 4);
			gbc_panel_path.fill = GridBagConstraints.BOTH;
			gbc_panel_path.gridx = 0;
			gbc_panel_path.gridy = 2;
			panel_main.add(panel_path, gbc_panel_path);
			
			lblpath = new JLabel("Path");
			lblpath.setBounds(10, 20, 415, 14);
			lblpath.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_path.add(lblpath);
		} // end main panel
		
		{	// Buttons pane
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 1;
			getContentPane().add(buttonPane, gbc_buttonPane);
			
			{ // OK button 
				okButton = new JButton("OK");
				okButton.setFont(new Font("Tahoma", Font.BOLD, 11));
				okButton.setPreferredSize(new Dimension(65, 23));
				okButton.setMinimumSize(new Dimension(65, 23));
				okButton.setMaximumSize(new Dimension(65, 23));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				// OK button event processing 
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						savePos = cbPos.isSelected();
						saveMoon = cbMoon.isSelected();
						saveVacScol = cbVacScol.isSelected();
						chknewver = cbChknewver.isSelected();
						colback= ncolback;
						colsun= ncolsun;
						colvacA= ncolvacA;
						colvacB= ncolvacB;
						colvacC= ncolvacC;
						town = ntown;
						Latitude = nLatitude;
						Longitude = nLongitude;
						townind= ntownind;
						//saveConfig();
						setVisible(false);
					}
				});
			} // end OK button
			
			{ // Cancel button
				JButton cancelButton = new JButton("Annuler");
				cancelButton.setMargin(new Insets(2, 5, 2, 5));
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 11));
				cancelButton.setPreferredSize(new Dimension(65, 23));
				cancelButton.setMinimumSize(new Dimension(65, 23));
				cancelButton.setMaximumSize(new Dimension(65, 23));
				buttonPane.add(cancelButton);
				
				// Cancel button event processing
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});		
				
				JPanel panel_status = new JPanel();
				panel_status.setBounds(new Rectangle(0, 0, 0, 13));
				FlowLayout fl_panel_status = (FlowLayout) panel_status.getLayout();
				fl_panel_status.setAlignment(FlowLayout.LEFT);
				GridBagConstraints gbc_panel_status = new GridBagConstraints();
				gbc_panel_status.anchor = GridBagConstraints.SOUTH;
				gbc_panel_status.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel_status.gridx = 0;
				gbc_panel_status.gridy = 2;
				getContentPane().add(panel_status, gbc_panel_status);
				
				JLabel lblStatus = new JLabel("New label");
				lblStatus.setPreferredSize(new Dimension(435, 12));
				lblStatus.setBounds(new Rectangle(0, 0, 0, 15));
				lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblStatus.setHorizontalTextPosition(SwingConstants.LEFT);
				lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
				panel_status.add(lblStatus);
				String s = System.getProperty("os.name");
				s += " v"+System.getProperty("os.version");
				s += " ("+System.getProperty("os.arch")+")";
				s += " - Java runtime v"+System.getProperty("java.version");
				
				lblStatus.setText(s);
				
			}
		} // end button pane

	} // end constructor dlgConfig
	
	

	private void changecoord(DocumentEvent e) {
		//if //System.out.println(e.getDocument().getProperty("owner"));	
		cbTown.removeActionListener(al);
		try {
			if (e.getDocument().getProperty("owner").equals("lat")) nLatitude= Double.parseDouble(tfLatitude.getText());
			if (e.getDocument().getProperty("owner").equals("lon")) nLongitude= Double.parseDouble(tfLongitude.getText());
			ntownind= 0;
			cbTown.setSelectedIndex(ntownind);
			ntown= towns.get(ntownind)[0];
		} catch (NumberFormatException e1) {
			//Invalid value do nothing
		}
		cbTown.addActionListener(al);
		
	}
	
	private void changetown () {
		tfLatitude.getDocument().removeDocumentListener(dl);
		tfLongitude.getDocument().removeDocumentListener(dl);		
		if (!towns.isEmpty()) {

			ntownind = cbTown.getSelectedIndex();
			ntown= towns.get(ntownind)[0];
			if (ntownind > 0) {
				tfLatitude.setText(towns.get(ntownind)[2]);
				tfLongitude.setText(towns.get(ntownind)[3]);
				nLatitude = Double.parseDouble(String.valueOf(towns.get(ntownind)[2]));
				nLongitude = Double.parseDouble(String.valueOf(towns.get(ntownind)[3]));
			}
		}
		tfLatitude.getDocument().addDocumentListener(dl);
		tfLongitude.getDocument().addDocumentListener(dl);
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
				/* Discarded as it doesn't work on linux and mac
				//config file not found. Ask user if it wants standard or portable operation not working in Linux
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
		// display path
		String wd = workingDirectory.replace('\\', '/');
		lblpath.setText(wd);
		// In case the text is too long, set tooltip
		lblpath.setToolTipText(wd);
		
		// Read towns file and populate combo box
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
			// Do nothing, likely erreor in the file
		}
		//loadConfig();
		cbTown.addActionListener(al);
		tfLatitude.getDocument().addDocumentListener(dl);
		tfLongitude.getDocument().addDocumentListener(dl);
	}// end version info properties
	
		
	// Read XML configuration file
	public boolean loadConfigXML() {
        try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
		    Document configXML = builder.parse(new File(config_file));
		    // Here we get the root element of XML 
		    // to check if we are in the proper file
	        Element rootElement = configXML.getDocumentElement();
	        if (!rootElement.getAttribute("name").equals("Calendrier")) throw new Exception();
	        NodeList list = rootElement.getChildNodes();
	        for (int i = 0; i < list.getLength(); i++) {
	            Node cNode = list.item(i);
	            if (cNode.getNodeType() == Node.ELEMENT_NODE) {
	            	String s = cNode.getTextContent();
	            	if (cNode.getNodeName().equals("savePos")) {
	            		savePos = s.equalsIgnoreCase("true");
	            		cbPos.setSelected(savePos);
	            	}
	            	else if (cNode.getNodeName().equals("locatX")) location.x = Integer.parseInt(s);
	            	else if (cNode.getNodeName().equals("locatY")) location.y = Integer.parseInt(s);
	            	else if (cNode.getNodeName().equals("sizeW")) size.width = Integer.parseInt(s);
	            	else if (cNode.getNodeName().equals("sizeW")) size.width = Integer.parseInt(s); 
	            	else if (cNode.getNodeName().equals("saveMoon")) {
	            		saveMoon = s.equalsIgnoreCase("true");
	            		cbMoon.setSelected(saveMoon);  
	            	}
	            	else if (cNode.getNodeName().equals("saveVacScol")) {
	            		saveVacScol = s.equalsIgnoreCase("true");
	            		cbVacScol.setSelected(saveVacScol);
	            	}
	            	else if (cNode.getNodeName().equals("dispVacA")) dispVacA = s.equalsIgnoreCase("true");
	            	else if (cNode.getNodeName().equals("dispVacB")) dispVacB = s.equalsIgnoreCase("true");
	            	else if (cNode.getNodeName().equals("dispVacC")) dispVacC = s.equalsIgnoreCase("true");
	            	else if (cNode.getNodeName().equals("dispMoon")) dispMoon = s.equalsIgnoreCase("true");
	            	else if (cNode.getNodeName().equals("colback")) colback = new Color(Integer.parseInt(s,16));
	            	else if (cNode.getNodeName().equals("colsun")) colsun = new Color(Integer.parseInt(s,16));
	            	else if (cNode.getNodeName().equals("colvacA")) {
	            		colvacA = new Color(Integer.parseInt(s,16));	
	            		btnZoneA.setBackground(colvacA);
	            	}
	            	else if (cNode.getNodeName().equals("colvacB")) {
	            		colvacB = new Color(Integer.parseInt(s,16));	
	            		btnZoneB.setBackground(colvacB);
	            	}
	            	else if (cNode.getNodeName().equals("colvacC")) {
	            		colvacC = new Color(Integer.parseInt(s,16));	
	            		btnZoneC.setBackground(colvacC);
	            	}
	            	else if (cNode.getNodeName().equals("latitude")) nLatitude = Double.parseDouble(s);	
	            	else if (cNode.getNodeName().equals("longitude")) nLongitude = Double.parseDouble(s);	
	            	else if (cNode.getNodeName().equals("town")) ntown = s;
	            	else if (cNode.getNodeName().equals("chknewver")) {
	            		chknewver= s.equalsIgnoreCase("true");
	            		cbChknewver.setSelected(chknewver);
	            	}
	            	else if (cNode.getNodeName().equals("lastupdchk")) {
	            		try {
							DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
							lastupdchk= formatter.parseDateTime(s);
						} catch (Exception e) {
							// date absent or invalid
							lastupdchk= new DateTime(2013,10,1,0,0,1);
						}
	            		 
	            	}
	            }
	        }
	        // Search towns list for current town
	     	if (!towns.isEmpty()) {
	     		for (int i= 0; i< towns.size(); i+=1) {
	     			if (ntown.equals(towns.get(i)[0])) {
	     				town=towns.get(i)[0];
	     				townind= i;
	     				cbTown.setSelectedIndex(i);
	     				if (i > 0) {
	     					tfLatitude.setText(towns.get(i)[2]);
	     					tfLongitude.setText(towns.get(i)[3]);
	     					Latitude = Double.parseDouble(String.valueOf(towns.get(i)[2]));
	     					Longitude = Double.parseDouble(String.valueOf(towns.get(i)[3]));
	     				}
	     				// Aucune selected at first line
	     				else {
	     					tfLatitude.setText(String.valueOf(nLatitude));
	     					tfLongitude.setText(String.valueOf(nLongitude));
	     					Latitude= nLatitude;
	     					Longitude= nLongitude;
	     				}	
	     				break;
	     			}
	     		}
	     	}
       } catch (Exception e) {
			return false;
		}
	  return true;	
	}
	
	// Save config file to XML
	public boolean saveConfigXML()  {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    // create a new XML document
			DocumentBuilder builder = factory.newDocumentBuilder();
		    Document configXML = builder.newDocument();
		    // To identify right config
		        Element el = configXML.createElement("config");
		        el.setAttribute("name", "Calendrier");
		        el.appendChild(createXMLEntry(configXML, "savePos", "boolean", savePos));
		        el.appendChild(createXMLEntry(configXML,"locatX", "int",location.x));
				el.appendChild(createXMLEntry(configXML,"locatY", "int",location.y));
				el.appendChild(createXMLEntry(configXML,"sizeW", "int",size.width));
				el.appendChild(createXMLEntry(configXML,"sizeH", "int",size.height));
				el.appendChild(createXMLEntry(configXML,"saveMoon", "boolean", saveMoon));	// Moon phases save
				el.appendChild(createXMLEntry(configXML,"dispMoon", "boolean", dispMoon));	// Moon phases display
				el.appendChild(createXMLEntry(configXML,"saveVacScol", "boolean", saveVacScol));	// Scolar holidays save
				el.appendChild(createXMLEntry(configXML,"dispVacA", "boolean", dispVacA));	// Zone A display
				el.appendChild(createXMLEntry(configXML,"dispVacB", "boolean", dispVacB));
				el.appendChild(createXMLEntry(configXML,"dispVacC", "boolean", dispVacC));
				el.appendChild(createXMLEntry(configXML,"colback", "hex", String.format("%06X", colback.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colsun", "hex", String.format("%06X", colsun.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colvacA", "hex", String.format("%06X", colvacA.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colvacB", "hex", String.format("%06X", colvacB.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colvacC", "hex", String.format("%06X", colvacC.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"latitude", "double", Latitude));
				el.appendChild(createXMLEntry(configXML,"longitude", "double", Longitude));
				el.appendChild(createXMLEntry(configXML,"town", "string", town));
				el.appendChild(createXMLEntry(configXML,"chknewver", "boolean", chknewver));
				el.appendChild(createXMLEntry(configXML,"lastupdchk", "string", lastupdchk.toString("yyyy-MM-dd")));
		        configXML.appendChild(el);
		        // The XML document we created above is still in memory
		        //  create DOM source, then sagve it to file
		        DOMSource source = new DOMSource(configXML);
		        try {
					PrintStream ps = new PrintStream(config_file);
					StreamResult result = new StreamResult(ps);
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer(); 
					transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // line breaks			
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // indent
					transformer.transform(source, result);
				} catch (Exception e) {
					return false;
				}
		} catch (ParserConfigurationException e) {
			return false;
		}
		return true;
	}
	
	private Element createXMLEntry(Document xml, String name, String type, Object value) {
		Element e = xml.createElement(name);
		e.setAttribute("type", type);
		String svalue= String.valueOf(value);
		e.setTextContent(svalue);
		return  e;
	}
}