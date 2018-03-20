/*
 * Configuration dialog and processing
 * read property file containing version informations
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
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

import bb.arraylist.bArrayList;



// Config dialog main class
public class dlgConfig extends JDialog {
	
	// Configuration variables
	public String OS;
	public String workingDirectory;
	public String execDirectory;
	public String lastImgDirectory;
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
	public Color colvacK= new Color(0,0,255);
	private Color ncolvacK;
	public boolean savePos = false;
	public int saveState= 0;
	public boolean saveMoon = false;
	public boolean dispMoon = false;
	public boolean saveVacScol = false;
	public boolean dispVacA = false;
	public boolean dispVacB = false;
	public boolean dispVacC = false;
	public boolean dispVacK = false;
	public boolean chknewver = true;
	public boolean loadstart = false;
	public boolean startmini = false;
	public boolean nstartmini;
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
	public String town;
	private String ntown;
	private int townind;
	private int ntownind;
	public ArrayList <String[]> manifest= null;
	private String config_file = "calendrier.config.xml";
	private ActionListener al;
	private DocumentListener ndl;
	private static final long serialVersionUID = 1L;
	private String iconFile= "";
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
	private JButton btnZoneK;
	private JComboBox<String> cbTown ;
	private JTextField tfLatitude;
	private JTextField tfLongitude;
	private JLabel lblpath;
	private JCheckBox cbChknewver;
	private JCheckBox cbStartup;
	private JCheckBox cbStartMini; 
	private JLabel lblStatus;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rbDec;
	private JRadioButton rbDeg;
	private JTextField tfLatdeg;
	private JTextField tfLatmn;
	private JLabel lblLatmn;
	private JTextField tfLats;
	private JLabel lblS;
	private JTextField tfLatns;
	private JTextField tfLondeg;
	private JLabel lbllondeg;
	private JTextField tfLonmn;
	private JLabel lbllonmn;
	private JTextField tfLons;
	private JLabel lbllons;
	private JTextField tfLoneo;

	
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
				ncolvacK= colvacK;
				nLatitude= Latitude;
				nLongitude= Longitude;
				tfLatitude.getDocument().removeDocumentListener(ndl);
				tfLongitude.getDocument().removeDocumentListener(ndl);
				tfLatitude.setText(String.valueOf(Latitude));
				filldegLat(Latitude);
				tfLongitude.setText(String.valueOf(Longitude));
				filldegLon(Longitude);
				tfLatitude.getDocument().addDocumentListener(ndl);
				tfLongitude.getDocument().addDocumentListener(ndl);	
				ntownind= townind;
				cbTown.removeActionListener(al);
				try {
					cbTown.setSelectedIndex(townind);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				cbTown.addActionListener(al);
				cbPos.setSelected(savePos);
				cbMoon.setSelected(saveMoon);
				cbVacScol.setSelected(saveVacScol);
				cbStartup.setSelected(loadstart);
				cbStartMini.setEnabled(loadstart);
				cbStartMini.setSelected(startmini);
				nstartmini= startmini;
				cbChknewver.setSelected(chknewver);
			}
		});
		
		// dialog initialization
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 402);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{434, 0};
		gridBagLayout.rowHeights = new int[]{295, 33, 15, 0};
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
			gbl_panel_main.rowHeights = new int[]{110, 110, 96, 0};
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
				
				JLabel label = new JLabel("Corse");
				label.setFont(new Font("Tahoma", Font.PLAIN, 11));
				label.setBounds(14, 68, 46, 14);
				panel_colors.add(label);
				
				btnZoneK = new JButton("");
				btnZoneK.setBackground(colvacK);
				
				btnZoneK.setName("vacK");
				btnZoneK.addMouseListener(macolor);
				
				btnZoneK.setBackground(new Color(0, 0, 255));
				btnZoneK.setBounds(62, 64, 39, 20);
				panel_colors.add(btnZoneK);
				
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
			cbMoon.setBounds(6, 40, 200, 23);
			panel_display.add(cbMoon);

			cbVacScol = new JCheckBox("Sauvegarde des vacances scolaires");
			cbVacScol.setSelected(false);
			cbVacScol.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbVacScol.setBounds(6, 65, 200, 23);
			panel_display.add(cbVacScol);

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
			cbTown.setBounds(65, 25, 175, 20);
			cbTown.setPreferredSize(new Dimension(120, 20));
			cbTown.setMaximumRowCount(25);
			panel_coord.add(cbTown);			
			// Event processing of towns combo box
			al = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					changetown();
				}
			};
			

		
			// Labels and text boxes for latitude and longitude

			JLabel lblLatitude = new JLabel("Latitude");
			lblLatitude.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLatitude.setBounds(10, 53, 50, 14);
			panel_coord.add(lblLatitude);

			
			tfLatitude = new JTextField();
			tfLatitude.setName("declat");
			tfLatitude.setBounds(65, 50, 100, 20);
			tfLatitude.setColumns(10);
			panel_coord.add(tfLatitude);
			tfLatitude.getDocument().putProperty("owner", tfLatitude); 
			tfLatitude.getDocument().addDocumentListener(ndl);
			

			JLabel lblLongitude = new JLabel("Longitude");
			lblLongitude.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLongitude.setBounds(10, 78, 50, 14);
			panel_coord.add(lblLongitude);
			

			tfLongitude = new JTextField();
			tfLongitude.setName("declon");
			tfLongitude.setColumns(10);
			tfLongitude.setBounds(65, 75, 100, 20);
			panel_coord.add(tfLongitude);			
			tfLongitude.getDocument().putProperty("owner", tfLongitude); 
			tfLongitude.getDocument().addDocumentListener(ndl);
			
			rbDec = new JRadioButton("Degr\u00E9s d\u00E9c.");
			rbDec.setVisible(false);
			buttonGroup.add(rbDec);
			rbDec.setSelected(true);
			rbDec.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			rbDec.setFont(new Font("Tahoma", Font.PLAIN, 11));
			rbDec.setBounds(250, 24, 83, 23);
			panel_coord.add(rbDec);
			
			rbDeg = new JRadioButton("Degr\u00E9s, mn, s");
			rbDeg.setVisible(false);
			buttonGroup.add(rbDeg);
			rbDeg.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			rbDeg.setFont(new Font("Tahoma", Font.PLAIN, 11));
			rbDeg.setBounds(320, 24, 109, 23);
			panel_coord.add(rbDeg);
			
			tfLatdeg = new JTextField();
			tfLatdeg.setName("deglat");
			tfLatdeg.setColumns(10);
			tfLatdeg.setBounds(210, 50, 30, 20);
			panel_coord.add(tfLatdeg);
			tfLatdeg.putClientProperty("return", Latitude);
			tfLatdeg.getDocument().putProperty("owner", tfLatdeg); 
			tfLatdeg.getDocument().addDocumentListener(ndl);
			
			JLabel lbllatdeg = new JLabel("deg.");
			lbllatdeg.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lbllatdeg.setBounds(243, 53, 25, 14);
			panel_coord.add(lbllatdeg);
			
			tfLatmn = new JTextField();
			tfLatmn.setName("deglat");
			tfLatmn.setColumns(10);
			tfLatmn.setBounds(270, 50, 30, 20);
			panel_coord.add(tfLatmn);
			tfLatmn.getDocument().putProperty("owner", tfLatmn); 
			tfLatmn.getDocument().addDocumentListener(ndl);
			
			lblLatmn = new JLabel("mn");
			lblLatmn.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblLatmn.setBounds(303, 53, 25, 14);
			panel_coord.add(lblLatmn);
			
			tfLats = new JTextField();
			tfLats.setName("deglat");
			tfLats.setColumns(10);
			tfLats.setBounds(322, 50, 70, 20);
			panel_coord.add(tfLats);
			tfLats.getDocument().putProperty("owner", tfLats);
			tfLats.getDocument().addDocumentListener(ndl);
			
			lblS = new JLabel("s");
			lblS.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblS.setBounds(398, 53, 10, 14);
			panel_coord.add(lblS);
			
			tfLatns = new JTextField();
			tfLatns.setName("deglat");
			tfLatns.setColumns(1);
			tfLatns.setBounds(410, 50, 15, 20);
			panel_coord.add(tfLatns);
			tfLatns.getDocument().putProperty("owner", tfLatns);
			tfLatns.getDocument().addDocumentListener(ndl);
			
			lbllondeg = new JLabel("deg.");
			lbllondeg.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lbllondeg.setBounds(243, 78, 25, 14);
			panel_coord.add(lbllondeg);			
			
			tfLondeg = new JTextField();
			tfLondeg.setName("deglon");
			tfLondeg.setColumns(10);
			tfLondeg.setBounds(210, 75, 30, 20);
			panel_coord.add(tfLondeg);
			tfLondeg.putClientProperty("return", Longitude);
			tfLondeg.getDocument().putProperty("owner", tfLondeg);
			tfLondeg.getDocument().addDocumentListener(ndl);
			
			lbllonmn = new JLabel("mn");
			lbllonmn.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lbllonmn.setBounds(303, 78, 25, 14);
			panel_coord.add(lbllonmn);
			
			tfLonmn = new JTextField();
			tfLonmn.setName("deglon");
			tfLonmn.setColumns(10);
			tfLonmn.setBounds(270, 75, 30, 20);
			panel_coord.add(tfLonmn);
			tfLonmn.getDocument().putProperty("owner", tfLonmn);
			tfLonmn.getDocument().addDocumentListener(ndl);
			
			lbllons = new JLabel("s");
			lbllons.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lbllons.setBounds(398, 78, 10, 14);
			panel_coord.add(lbllons);
			
			tfLons = new JTextField();
			tfLons.setName("deglon");
			tfLons.setColumns(10);
			tfLons.setBounds(322, 75, 70, 20);
			panel_coord.add(tfLons);
			tfLons.getDocument().putProperty("owner", tfLons);
			tfLons.getDocument().addDocumentListener(ndl);
			
			tfLoneo = new JTextField();
			tfLoneo.setName("deglon");
			tfLoneo.setColumns(1);
			tfLoneo.setBounds(410, 75, 15, 20);
			panel_coord.add(tfLoneo);
			tfLoneo.getDocument().putProperty("owner", tfLoneo);
			tfLoneo.getDocument().addDocumentListener(ndl);
			
			JPanel panel_system = new JPanel();
			panel_system.setFont(new Font("Tahoma", Font.BOLD, 11));
			panel_system.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Syst\u00E8me", TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11), null));
			panel_system.setLayout(null);
			GridBagConstraints gbc_panel_system = new GridBagConstraints();
			gbc_panel_system.gridwidth = 2;
			gbc_panel_system.insets = new Insets(0, 5, 3, 4);
			gbc_panel_system.fill = GridBagConstraints.BOTH;
			gbc_panel_system.gridx = 0;
			gbc_panel_system.gridy = 2;
			panel_main.add(panel_system, gbc_panel_system);
			
			lblpath = new JLabel("Path");
			lblpath.setBounds(118, 20, 307, 14);
			lblpath.setFont(new Font("Tahoma", Font.PLAIN, 11));
			panel_system.add(lblpath);
			
			cbChknewver = new JCheckBox("Recherche de mise \u00E0 jour");
			cbChknewver.setSelected(false);
			cbChknewver.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbChknewver.setBounds(225, 41, 200, 23);
			panel_system.add(cbChknewver);
			
			JLabel lblpathcaption = new JLabel("Dossier utilisateur");
			lblpathcaption.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblpathcaption.setBounds(15, 20, 93, 14);
			panel_system.add(lblpathcaption);
			

			cbStartup = new JCheckBox("Lancer au d\u00E9marrage");
			cbStartup.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					cbStartMini.setEnabled(cbStartup.isSelected());
				}
			});
			//cbStartup.setSelected(false);
			cbStartup.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbStartup.setBounds(13, 41, 200, 23);
			panel_system.add(cbStartup);
			
			cbStartMini = new JCheckBox("D\u00E9marrage nimimis\u00E9");
			cbStartMini.setEnabled(false);
			cbStartMini.setFont(new Font("Tahoma", Font.PLAIN, 11));
			cbStartMini.setBounds(13, 63, 200, 23);
			panel_system.add(cbStartMini);
			
			MaskFormatter latDegMinSecFormatter = null;
			MaskFormatter lonDegMinSecFormatter = null;
			try
			{
				latDegMinSecFormatter = new MaskFormatter("##'°##''##'.####'''' U");
				latDegMinSecFormatter.setValidCharacters("0123456789NS");
				
				lonDegMinSecFormatter = new MaskFormatter("##'°##''##'.####'''' U");
				lonDegMinSecFormatter.setValidCharacters("0123456789EW");
			}
			catch (ParseException e)
			{
				//ErrorHandler.handleFatalError(e);
			}
			
			
			ndl = new DocumentListener() {
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
					
					changeCoord(e);
					//String syear = YearField.getText();
						//yearchanged(syear);
				}
			};

			
			
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
						colvacK= ncolvacK;
						town = ntown;
						Latitude = nLatitude;
						Longitude = nLongitude;
						townind= ntownind;
						nstartmini = cbStartMini.isSelected();
						// load at startup changed 
						if (loadstart != cbStartup.isSelected()) {
							loadstart= cbStartup.isSelected();
							startmini= nstartmini;
							if (loadstart) processStartup(true);
							else processStartup(false);
						}
						// load at startup not changed but startmini has changed
						// we change startup only if it is activated
						else if (startmini != nstartmini) {
							startmini= nstartmini;
							if (loadstart)  processStartup(true);
						}
						
						 
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
				
				lblStatus = new JLabel("New label");
				lblStatus.setPreferredSize(new Dimension(435, 12));
				lblStatus.setBounds(new Rectangle(0, 0, 0, 15));
				lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
				lblStatus.setHorizontalTextPosition(SwingConstants.LEFT);
				lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
				panel_status.add(lblStatus);
				
				
			}
		} // end button pane

	} // end constructor dlgConfig
	
	
	private void processStartup(boolean load){
		if (load) {
			String progname = "calendrier.jar";
			String param = "";
			if (startmini) param= "MINI" ;
			// Windows. Create a shortcut in user startup
			// Todo check if jar or exe
			if (OS.contains("WIN")) {
				shortcut.createWinShortcut(shortcut.sh_Type.U_STARTUP, execDirectory,progname, param, "calendrier.lnk", workingDirectory+"/"+iconFile) ;	
			}
			else if (OS.contains("NUX")) {
				shortcut.createLinuxShortcut(execDirectory, progname, param, "Calendrier.desktop", workingDirectory+"/"+iconFile);
			}
			else if (OS.contains("MAC")) {
				shortcut.createOSXShortcut(execDirectory, progname, param, "com.sdtp.calendrier", workingDirectory+"/"+iconFile);
			}
		}
		else {
			
			if (OS.contains("WIN")) {
				shortcut.deleteWinShortcut(shortcut.sh_Type.U_STARTUP, "calendrier.lnk");
			}
			else if (OS.contains("NUX")) {
				shortcut.deleteLinuxShortcut("Calendrier.desktop");
			}
			else if (OS.contains("MAC")) {
				shortcut.deleteOSXShortcut("com.sdtp.calendrier");
			}
		}
		
	
	}
	
	// user has changed some coordinate field
	private void changeCoord(DocumentEvent e) {
		
		// retreive owner and get value
		JTextField tf = (JTextField) e.getDocument().getProperty("owner");
		String curTxt = tf.getText();
		double value= 0;
		try {
			value = Double.parseDouble(curTxt);
		} catch (NumberFormatException e1) {
		}
		
		double tmpLat = nLatitude;
		double tmpLon = nLongitude;
		String tfname= tf.getName();
		// Decimal latitude changed
		if (tfname.equals("declat")) {
			
            nLatitude= filldegLat(value);
		}
		// Decimal longitude changed
		if (tfname.equals("declon")) {
			nLongitude= value;
			filldegLon(nLongitude); 
		}
		// degree/min/s latitude changed
		if (tfname.equals("deglat")) {
			nLatitude = filldecLat();
		}
		// degree/min/s longitude cxhanged
		if (tfname.equals("deglon")) {
			nLongitude = filldecLon();
		}
		// If anything changed, then display no town
		if ((nLatitude!=tmpLat) || (nLongitude != tmpLon)) { 
		cbTown.removeActionListener(al);
		ntownind= 0;
		try {
			cbTown.setSelectedIndex(ntownind);
			ntown= towns.get(ntownind)[0];
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		cbTown.addActionListener(al);
		}
	}
	
	// recompute latitude from degrees fields
	// and update decimal field
	private double filldecLat () {
		tfLatitude.getDocument().removeDocumentListener(ndl);
		int sign = 1;
		double result=0;
		try {
			result= Math.abs(Double.parseDouble(tfLatdeg.getText()));
		} catch (Exception e1) {
		}
		try {
			result += Double.parseDouble(tfLatmn.getText())/60.0;
		} catch (Exception e1) {
		}
		try {
			result += Double.parseDouble(tfLats.getText())/3600.0;
		} catch (Exception e1) {
		}
		try {
			sign = (tfLatns.getText().toUpperCase().equals("N")) ? 1 : -1;
		} catch (Exception e) {
		}
		result= result*sign;
		if (Math.abs(result)>90) {
			result= nLatitude;
			tfLatitude.getDocument().addDocumentListener(ndl);
			tfLatitude.setText(String.format(Locale.ENGLISH, "%.6f", result));
		}
		else {
			tfLatitude.setText(String.format(Locale.ENGLISH, "%.6f", result));
			tfLatitude.getDocument().addDocumentListener(ndl);
		}
		
		return result;
	}
	
	// recompute longitude fromd egree fields
	// and update decimal field
	private double filldecLon () {
		tfLongitude.getDocument().removeDocumentListener(ndl);
		int sign = 1;
		double result=0;
		try {
			result= Double.parseDouble(tfLondeg.getText());
		} catch (Exception e1) {
		}
		try {
			result += Double.parseDouble(tfLonmn.getText())/60.0;
		} catch (Exception e1) {
		}
		try {
			result += Double.parseDouble(tfLons.getText())/3600.0;
		} catch (Exception e1) {
		}
		try {
			sign = (tfLoneo.getText().toUpperCase().equals("E")) ? 1 : -1;
		} catch (Exception e) {
		}
		result= result*sign;
		if (Math.abs(result)>180) {
			result= nLongitude;
			tfLongitude.getDocument().addDocumentListener(ndl);
			tfLongitude.setText(String.format(Locale.ENGLISH, "%.6f", result));
		}
		else {
			tfLongitude.setText(String.format(Locale.ENGLISH, "%.6f", result));
			tfLongitude.getDocument().addDocumentListener(ndl);
		}
		return result;
	}
	

	// compute degree latitude and update the fields

	private double filldegLat (double lat) {
		// We use tfLatdeg client property "return"to return value as
		// we cannot use a local variable

		final double latit= lat;
		Runnable doAssist = new Runnable() {
			@Override
            public void run() {
				if (Math.abs(latit) > 90) {
                	tfLatitude.setText(String.format(Locale.ENGLISH, "%.6f", Latitude));
                	tfLatdeg.putClientProperty("return", Latitude);
                } else {
                	tfLatdeg.getDocument().removeDocumentListener(ndl);
        			tfLatmn.getDocument().removeDocumentListener(ndl);
        			tfLats.getDocument().removeDocumentListener(ndl);
        			tfLatns.getDocument().removeDocumentListener(ndl);
        			tfLatdeg.setText(String.format(Locale.ENGLISH, "%.0f", coordDecToDeg(latit)[0]));
        			tfLatmn.setText(String.format(Locale.ENGLISH, "%.0f", coordDecToDeg(latit)[1]));
        			tfLats.setText(String.format(Locale.ENGLISH, "%.3f", coordDecToDeg(latit)[2]));
        			tfLatns.setText((coordDecToDeg(latit)[3]	< 0) ? "S" : "N");
        			tfLatdeg.getDocument().addDocumentListener(ndl);
        			tfLatmn.getDocument().addDocumentListener(ndl);
        			tfLats.getDocument().addDocumentListener(ndl);
        			tfLatns.getDocument().addDocumentListener(ndl);
                	tfLatdeg.putClientProperty("return", latit);
                }
            }
        };
        SwingUtilities.invokeLater(doAssist); 	
		return (double) tfLatdeg.getClientProperty("return");
	}
	
	

	// compute degree longitude and update the fields
	
	private double filldegLon(double lon) {
		// We use tfLondeg client property "return"to return value as
		// we cannot use a local variable
		final double longit= lon;
		Runnable doAssist = new Runnable() {
            @Override
            public void run() { 
            	if (Math.abs(longit) > 180) {
            		tfLongitude.setText(String.format(Locale.ENGLISH, "%.6f", Longitude));
                	tfLondeg.putClientProperty("return", Longitude);
            	}
            	else {
            		tfLondeg.getDocument().removeDocumentListener(ndl);
            		tfLonmn.getDocument().removeDocumentListener(ndl);
            		tfLons.getDocument().removeDocumentListener(ndl);
            		tfLoneo.getDocument().removeDocumentListener(ndl);
            		tfLondeg.setText(String.format(Locale.ENGLISH, "%.0f", coordDecToDeg(longit)[0]));
            		tfLonmn.setText(String.format(Locale.ENGLISH, "%.0f", coordDecToDeg(longit)[1]));
            		tfLons.setText(String.format(Locale.ENGLISH, "%.3f", coordDecToDeg(longit)[2]));
            		tfLoneo.setText((coordDecToDeg(longit)[3]	< 0) ? "O" : "E");
            		tfLondeg.getDocument().addDocumentListener(ndl);
            		tfLonmn.getDocument().addDocumentListener(ndl);
            		tfLons.getDocument().addDocumentListener(ndl);
            		tfLoneo.getDocument().addDocumentListener(ndl);
            		tfLondeg.putClientProperty("return", longit);
            	}
            }
            
        };
		SwingUtilities.invokeLater(doAssist); 	
		return (double) tfLondeg.getClientProperty("return");
	}
	
	// town has changed
	private void changetown () {
		tfLatitude.getDocument().removeDocumentListener(ndl);
		tfLongitude.getDocument().removeDocumentListener(ndl);		
		if (!towns.isEmpty()) {

			ntownind = cbTown.getSelectedIndex();
			ntown= towns.get(ntownind)[0];
			if (ntownind > 0) {
				tfLatitude.setText(towns.get(ntownind)[2]);
	
				tfLongitude.setText(towns.get(ntownind)[3]);
				nLatitude = Double.parseDouble(String.valueOf(towns.get(ntownind)[2]));
				filldegLat(nLatitude);
				nLongitude = Double.parseDouble(String.valueOf(towns.get(ntownind)[3]));
				filldegLon(nLongitude);
				
			}
		}
		tfLatitude.getDocument().addDocumentListener(ndl);
		tfLongitude.getDocument().addDocumentListener(ndl);
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
		else if (c.getName().equals("vacK")) ncolvacK = col;
		c.setBackground(col);
	}

	// Find and/or create the configuration directory
	public void set_config_file(String s) {
		config_file = s;
		// Exec directory
		execDirectory= ClassLoader.getSystemClassLoader().getResource(".").getPath();
		try {
			execDirectory = URLDecoder.decode(execDirectory, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			//
		}
		
		// Working directory
		String os = System.getProperty("os.name");
		OS = os.toUpperCase();
		iconFile= "calendrier.png";
		if (OS.contains("WIN")) {
			workingDirectory = System.getenv("AppData");	// Win location of the "AppData" folder
			// remove leading "/" if windows
			try {
				if (execDirectory.charAt(0)=='/') execDirectory= execDirectory.substring(1);
				iconFile= "calendrier.ico";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				iconFile="";
			}
		}
		else if (OS.contains("MAC")) {
			workingDirectory = System.getProperty("user.home")+"/Library/Application Support"; // Mac, look for "Application Support"
			iconFile= "calendrier.icns";
		}
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
		
		// Write icon
		if (!(new File(workingDirectory+"/"+iconFile).exists())) {
			try {
				InputStream bpng = ClassLoader.class.getResourceAsStream("/resources/"+iconFile);
				FileOutputStream fpng = new FileOutputStream(new File(workingDirectory+"/"+iconFile));
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = bpng.read(bytes)) != -1) {
					fpng.write(bytes, 0, read);
				}
				fpng.close();
			
			} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			}
		}	
		// SEt status text
		String st = os;
		st += " v"+System.getProperty("os.version");
		st += " ("+System.getProperty("os.arch")+")";
		st += " - Java runtime v"+System.getProperty("java.version");
		
		lblStatus.setText(st);
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
		//tfLatitude.getDocument().addDocumentListener(dl);
		//tfLongitude.getDocument().addDocumentListener(dl);
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
	            	else if (cNode.getNodeName().equals("sizeH")) size.height = Integer.parseInt(s); 
	            	else if (cNode.getNodeName().equals("saveState")) saveState = Integer.parseInt(s); 
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
	            	else if (cNode.getNodeName().equals("dispVacK")) dispVacK = s.equalsIgnoreCase("true");
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
	            	else if (cNode.getNodeName().equals("colvacK")) {
	            		colvacK = new Color(Integer.parseInt(s,16));	
	            		btnZoneK.setBackground(colvacK);
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
	            	else if (cNode.getNodeName().equals("loadstart")) {
	            		loadstart = s.equalsIgnoreCase("true");
	            		cbStartup.setSelected(loadstart);
	            	}
	            	else if (cNode.getNodeName().equals("startmini")) {
	            		startmini = s.equalsIgnoreCase("true");
	            		cbStartMini.setSelected(startmini);
	            	}
	            	else if (cNode.getNodeName().equals("imgdirectory")) lastImgDirectory = s;  
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
				el.appendChild(createXMLEntry(configXML,"saveState", "int", saveState)); 
				el.appendChild(createXMLEntry(configXML,"saveMoon", "boolean", saveMoon));	// Moon phases save
				el.appendChild(createXMLEntry(configXML,"dispMoon", "boolean", dispMoon));	// Moon phases display
				el.appendChild(createXMLEntry(configXML,"saveVacScol", "boolean", saveVacScol));	// Scolar holidays save
				el.appendChild(createXMLEntry(configXML,"dispVacA", "boolean", dispVacA));	// Zone A display
				el.appendChild(createXMLEntry(configXML,"dispVacB", "boolean", dispVacB));
				el.appendChild(createXMLEntry(configXML,"dispVacC", "boolean", dispVacC));
				el.appendChild(createXMLEntry(configXML,"dispVacK", "boolean", dispVacK));
				el.appendChild(createXMLEntry(configXML,"colback", "hex", String.format("%06X", colback.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colsun", "hex", String.format("%06X", colsun.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colvacA", "hex", String.format("%06X", colvacA.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colvacB", "hex", String.format("%06X", colvacB.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colvacC", "hex", String.format("%06X", colvacC.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"colvacK", "hex", String.format("%06X", colvacK.getRGB()& 0xffffff)));
				el.appendChild(createXMLEntry(configXML,"latitude", "double", Latitude));
				el.appendChild(createXMLEntry(configXML,"longitude", "double", Longitude));
				el.appendChild(createXMLEntry(configXML,"town", "string", town));
				el.appendChild(createXMLEntry(configXML,"chknewver", "boolean", chknewver));
				el.appendChild(createXMLEntry(configXML,"lastupdchk", "string", lastupdchk.toString("yyyy-MM-dd")));
				el.appendChild(createXMLEntry(configXML,"loadstart", "boolean", loadstart));
				el.appendChild(createXMLEntry(configXML,"startmini", "boolean", startmini));
				el.appendChild(createXMLEntry(configXML,"imgdirectory", "string", lastImgDirectory));
				
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
	
	private double[] coordDecToDeg (double coord) {
		double [] result= {0,0,0,0};
		try {
			if (coord < 0) result[3] = -1;
			else result[3]=1;			
			coord = Math.abs(coord);
			result[0] = Math.floor(coord);
			double dmn = (coord - result[0])*60 ;
			result[1] =  Math.floor(dmn);
			result[2] = (dmn - result[1])*60 ;
		} catch (Exception e) {
		}
		return result; 
	}
}