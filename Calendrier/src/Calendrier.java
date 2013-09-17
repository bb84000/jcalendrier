import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.joda.time.DateTime;


public class Calendrier {

	private JFrame frmCalendrier;
	private JPanel pane_bottom;


    private JScrollPane scrollPane;
    private JTabbedPane tabpane;
    private JPanel pane_h1;
    private JPanel pane_h2;
    private JPanel pane_q1;
    private JPanel pane_q2;
    private JPanel pane_q3;
    private JPanel pane_q4;
    private JTable table_q1;
    private JTable table_q2;
    private JTable table_q3;
    private JTable table_q4;
	private int Year;
	private Boolean Init;
	
	// Config variables	are in dlgConfig class
	private dlgConfig Config = new dlgConfig();
	
	private JLabel lblNewLabel_1;
	private JPopupMenu pMnuGen;
	private JMenuItem pMnuConfig;

	private JTextField YearField;
	private JPanel pane_center_h1;
	private JPanel pane_center_h2;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel1;
	
	private DayCalRenderer Quarter = new DayCalRenderer();
	private JCheckBox cbMoon = new JCheckBox("Phases de la lune");
	private JCheckBox cbVacA = new JCheckBox("Vacances Zone A");
	private JCheckBox cbVacB = new JCheckBox("Vacances Zone B");
	private JCheckBox cbVacC = new JCheckBox("Vacances Zone C");
	CheckBoxIcon checkedA = new CheckBoxIcon();
	CheckBoxIcon checkedB = new CheckBoxIcon();
	CheckBoxIcon checkedC = new CheckBoxIcon();
	// Needed to restore location after modal dialog
	Point CurLoc = new Point();
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calendrier window = new Calendrier();
					window.frmCalendrier.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Calendrier() {
		// Année en cours
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void applyConfig(){
		
		if (Config.savePos) {
			frmCalendrier.setSize(Config.sizeW, Config.sizeH);
			frmCalendrier.setLocation(Config.locatX, Config.locatY); 
		}
		else frmCalendrier.setLocationRelativeTo(null);
		Quarter.colA = Config.colvacA;
		checkedA.colFillu = Config.colvacA;
		Quarter.colB = Config.colvacB;
		checkedB.colFillu = Config.colvacB;
		Quarter.colC = Config.colvacC;
		checkedC.colFillu = Config.colvacC;
		if (Config.saveMoon) {
			cbMoon.setSelected(Config.dispMoon);
			Quarter.ShowMoon= cbMoon.isSelected();
		}
		if (Config.saveVacScol) {
			cbVacA.setSelected(Config.dispVacA);
			Quarter.ShowVacA= cbVacA.isSelected();
			cbVacB.setSelected(Config.dispVacB);
			Quarter.ShowVacB= cbVacB.isSelected();
			cbVacC.setSelected(Config.dispVacC);
			Quarter.ShowVacC= cbVacC.isSelected();
		}
		
		
	}
	
	// Some initialization routines
	private void initialize() {
		Init=true;
		Config.config_file= "config.json";
		
		// Working directory
		String OS = (System.getProperty("os.name")).toUpperCase();
		if (OS.contains("WIN")) Config.workingDirectory = System.getenv("AppData");											// Win location of the "AppData" folder
		else if (OS.contains("MAC")) Config.workingDirectory = System.getProperty("user.home")+"/Library/Application Support"; // Mac, look for "Application Support"
		else Config.workingDirectory = System.getProperty("user.home");    													//Otherwise, we assume Linux 
		Config.workingDirectory += "/calendrier";	
		// check where is the config file
		File f = new File(Config.config_file);
		if (f.exists()) Config.loadConfig();
		else {
			f = new File (Config.workingDirectory+"/"+Config.config_file);
		    if (f.exists()) {
		    	Config.config_file= Config.workingDirectory+"/"+Config.config_file;
		    	Config.loadConfig();
		    }
		    else {
		    	//config file not found. Ask user if it wants standard or portable operation
		    	 String BtnCaptions[]={ "Standard", "Portable"};
		    	 String msg = new String("Choix du mode de fonctionnement\n");
		    	 msg += "Standard : Les données de configuration sont stoclées dans le répertoire utilisateur.\n";
		    	 msg += "Portable : les données de configuration sont stockées dans le répertoire courant.";
		    	 int ret = JOptionPane.showOptionDialog(null, msg, "Calendrier", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, BtnCaptions, "");
		    	 if (ret==0) {								// store in user folder otherwise store in current folder
		    		 File folderExisting = new File(Config.workingDirectory); 
		    		 if (!folderExisting.exists()) {  
		    			 boolean success = (new File(Config.workingDirectory)).mkdirs();
		    			 if (success) Config.config_file= Config.workingDirectory+"/"+Config.config_file;
		    			 else JOptionPane.showMessageDialog(null, "Impossible de créer le dossier de l'application");
		    		 }
		    		 else Config.config_file= Config.workingDirectory+"/"+Config.config_file;
		    	 }
		   	 } 
		
		}		

		DateTime dt = new DateTime();
		Year = dt.getYear();
		//Icone de l'application
		Image MainIcon = Toolkit.getDefaultToolkit().getImage(Calendrier.class.getResource("/resources/calendrier.png"));
		
		// Création de la forme

		frmCalendrier = new JFrame() 
        // Maximum size bug
		{ 		 
			private static final long serialVersionUID = 1L;

				@Override
	            public void paint(Graphics g) {
	                Dimension d = getSize();
	                Dimension m = getMaximumSize();
	                boolean resize = d.width > m.width || d.height > m.height;
	                d.width = Math.min(m.width, d.width);
	                d.height = Math.min(m.height, d.height);
	                if (resize) {
	                    Point p = getLocation();
	                    setVisible(false);
	                    setSize(d);
	                    setLocation(p);
	                    setVisible(true);
	                }
	                super.paint(g);
	            }
	        };
	        
	    // Save user config data on close    
		frmCalendrier.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Point p = new Point(frmCalendrier.getLocation());
				Config.locatX = p.x;
				Config.locatY = p.y;
				Dimension d = new Dimension(frmCalendrier.getSize());
				Config.sizeW = d.width;
				Config.sizeH = d.height;
				Config.saveConfig();
			}
		});
		
		frmCalendrier.setSize(new Dimension(1182, 728));
		frmCalendrier.setMaximumSize(new Dimension(1180, 728));
		frmCalendrier.getContentPane().setMaximumSize(new Dimension(1182, 2147483647));
		frmCalendrier.setIconImage(MainIcon);
		frmCalendrier.setBounds(100, 100, 450, 300);
		frmCalendrier.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCalendrier.setSize(1180, 719);
		
		// Apply configuration parameters
		applyConfig();
		String syear ="";
		syear += Year;
		frmCalendrier.setTitle("Calendrier - "+syear);
		pane_bottom = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pane_bottom.getLayout();
		flowLayout.setHgap(15);
		pane_bottom.setMaximumSize(new Dimension(1182, 32767));
		pane_bottom.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pane_bottom.setPreferredSize(new Dimension(10, 40));
		frmCalendrier.getContentPane().add(pane_bottom, BorderLayout.SOUTH);
		
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(1180, 2));
		scrollPane.setMaximumSize(new Dimension(1180, 32767));
		frmCalendrier.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		tabpane = new JTabbedPane(JTabbedPane.TOP);
		scrollPane.setColumnHeaderView(tabpane);
		
		// 1st half pane
		pane_h1 = new JPanel();
		tabpane.addTab("1er semestre", null, pane_h1, null);
		GridBagLayout gbl_pane_h1 = new GridBagLayout();
		gbl_pane_h1.columnWidths = new int[]{307, 532, 307, 0};
		gbl_pane_h1.rowHeights = new int[]{574, 0, 0};
		gbl_pane_h1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pane_h1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		pane_h1.setLayout(gbl_pane_h1);
		
		// 1st quarter table
		pane_q1 = new JPanel();
		pane_q1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q1.setPreferredSize(new Dimension(307, 613));
		pane_q1.setMinimumSize(new Dimension(307, 613));
		pane_q1.setMaximumSize(new Dimension(307, 613));	
		GridBagConstraints gbc_pane_q1 = new GridBagConstraints();
		gbc_pane_q1.anchor = GridBagConstraints.NORTHWEST;
		gbc_pane_q1.gridx = 0;
		gbc_pane_q1.gridy = 0;
		pane_h1.add(pane_q1, gbc_pane_q1);
		
				
				Quarter.setYear(Year);
				table_q1 = new JTable();
				table_q1.setRowMargin(0);
				table_q1.setPreferredScrollableViewportSize(new Dimension(307, 612));
				table_q1.setSize(new Dimension(307, 612));
				table_q1.setFont(new Font("Tahoma", Font.PLAIN, 10));
				table_q1.setMaximumSize(new Dimension(307, 612));
				table_q1.setMinimumSize(new Dimension(307, 612));
				table_q1.setPreferredSize(new Dimension(307, 612));
				table_q1.setName("1");
				table_q1.setDefaultRenderer(Object.class, Quarter);
				table_q1.setRowHeight(19);
				table_q1.setRowMargin(1);
				table_q1.setRowSelectionAllowed(false);
				table_q1.setModel(new DefaultTableModel(
					new Object[][] {
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
					},
					new String[] {
						"Janvier", "F\u00E9vrier", "Mars"
					}
				));
				table_q1.getColumnModel().getColumn(1).setResizable(false);
				// Add the header
				JTableHeader header_q1 = table_q1.getTableHeader();
				pane_q1.setLayout(new BorderLayout());
				pane_q1.add(header_q1, BorderLayout.NORTH);
				pane_q1.add(table_q1, BorderLayout.CENTER);
				
				
				
				// 2nd quarter
				pane_q2 = new JPanel();
				pane_q2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				pane_q2.setMinimumSize(new Dimension(307, 613));
				pane_q2.setMaximumSize(new Dimension(307, 613));
				pane_q2.setPreferredSize(new Dimension(307, 613));
				GridBagConstraints gbc_pane_q2 = new GridBagConstraints();
				gbc_pane_q2.anchor = GridBagConstraints.NORTHWEST;
				gbc_pane_q2.gridx = 2;
				gbc_pane_q2.gridy = 0;
				pane_h1.add(pane_q2, gbc_pane_q2);
				
				
				table_q2 = new JTable();
				table_q2.setRowMargin(0);
				table_q2.setRowSelectionAllowed(false);
				table_q2.setMinimumSize(new Dimension(307, 612));
				table_q2.setMaximumSize(new Dimension(307, 612));
				table_q2.setPreferredSize(new Dimension(307, 612));
				table_q2.setFont(new Font("Tahoma", Font.PLAIN, 10));
				table_q2.setName("2");
				table_q2.setDefaultRenderer(Object.class, Quarter);
				table_q2.setRowHeight(19);
				table_q2.setRowMargin(1);
				table_q2.setPreferredScrollableViewportSize(new Dimension(307, 612));
				table_q2.setModel(new DefaultTableModel(
					new Object[][] {
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
					},
					new String[] {
						"Avril", "Mai", "Juin"
					}
				));
				table_q2.getColumnModel().getColumn(1).setResizable(false);
				// Ajoute le header car pas de scrollpanel autour
				JTableHeader header_q2 = table_q2.getTableHeader();
				pane_q2.setLayout(new BorderLayout());
				pane_q2.add(header_q2, BorderLayout.NORTH);
				pane_q2.add(table_q2, BorderLayout.CENTER);
				
				// Panneau central 1er seme.
				pane_center_h1 = new JPanel();
				pane_center_h1.setMinimumSize(new Dimension(532, 400));
				pane_center_h1.setSize(new Dimension(532, 400));
				pane_center_h1.setPreferredSize(new Dimension(532, 582));
				pane_center_h1.setMaximumSize(new Dimension(532, 582));
				GridBagConstraints gbc_pane_center_h1 = new GridBagConstraints();
				gbc_pane_center_h1.anchor = GridBagConstraints.NORTH;
				gbc_pane_center_h1.gridx = 1;
				gbc_pane_center_h1.gridy = 0;
				pane_h1.add(pane_center_h1, gbc_pane_center_h1);
				GridBagLayout gbl_pane_center_h1 = new GridBagLayout();
				gbl_pane_center_h1.columnWidths = new int[]{266, 266, 0};
				gbl_pane_center_h1.rowHeights = new int[]{400, 182, 0};
				gbl_pane_center_h1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				gbl_pane_center_h1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				pane_center_h1.setLayout(gbl_pane_center_h1);
				
				lblNewLabel = new JLabel("");
				lblNewLabel.setMaximumSize(new Dimension(530, 400));
				lblNewLabel.setMinimumSize(new Dimension(530, 400));
				lblNewLabel.setSize(new Dimension(530, 400));
				lblNewLabel.setPreferredSize(new Dimension(530, 400));
				lblNewLabel.setIcon(new StretchIcon("image.jpg"));
				GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
				gbc_lblNewLabel.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
				gbc_lblNewLabel.gridwidth = 2;
				gbc_lblNewLabel.gridx = 0;
				gbc_lblNewLabel.gridy = 0;
				pane_center_h1.add(lblNewLabel, gbc_lblNewLabel);
	   
		// 2nd half
		pane_h2 = new JPanel();
		tabpane.addTab("2\u00E8me semestre", null, pane_h2, null);
		GridBagLayout gbl_pane_h2 = new GridBagLayout();
		gbl_pane_h2.columnWidths = new int[]{307, 532, 307, 0};
		gbl_pane_h2.rowHeights = new int[]{574, 0};
		gbl_pane_h2.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pane_h2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pane_h2.setLayout(gbl_pane_h2);
		
		pane_q3 = new JPanel();
		pane_q3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q3.setPreferredSize(new Dimension(307, 613));
		pane_q3.setMinimumSize(new Dimension(307, 613));
		pane_q3.setMaximumSize(new Dimension(307, 613));
		GridBagConstraints gbc_pane_q3 = new GridBagConstraints();
		gbc_pane_q3.anchor = GridBagConstraints.NORTHWEST;
		//gbc_pane_q3.fill = GridBagConstraints.VERTICAL;
		gbc_pane_q3.gridx = 0;
		gbc_pane_q3.gridy = 0;
		pane_h2.add(pane_q3, gbc_pane_q3);
		
		table_q3 = new JTable();
		table_q3.setPreferredScrollableViewportSize(new Dimension(307, 612));
		table_q3.setFont(new Font("Tahoma", Font.PLAIN, 10));
		table_q3.setMaximumSize(new Dimension(307, 612));
		table_q3.setMinimumSize(new Dimension(307, 612));
		table_q3.setPreferredSize(new Dimension(307, 612));
		table_q3.setName("3");
		table_q3.setDefaultRenderer(Object.class, Quarter);
		table_q3.setRowHeight(19);
		table_q3.setRowMargin(1);
		table_q3.setRowSelectionAllowed(false);
		table_q3.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
			},
			new String[] {
				"Juillet", "Ao\u00FBt", "Septembre"
			}
		));
		table_q3.getColumnModel().getColumn(1).setResizable(false);
		// Ajoute le header car pas de scrollpanel autour
		JTableHeader header_q3 = table_q3.getTableHeader();
		pane_q3.setLayout(new BorderLayout());
		pane_q3.add(header_q3, BorderLayout.NORTH);
		pane_q3.add(table_q3, BorderLayout.CENTER);
		
		
		// 4eme trimestre
		pane_q4 = new JPanel();
		pane_q4.setPreferredSize(new Dimension(307, 613));
		pane_q4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q4.setMinimumSize(new Dimension(307, 613));
		pane_q4.setMaximumSize(new Dimension(307, 613));
		GridBagConstraints gbc_pane_q4 = new GridBagConstraints();
		gbc_pane_q4.anchor = GridBagConstraints.NORTHWEST;
		gbc_pane_q4.gridx = 2;
		gbc_pane_q4.gridy = 0;
		pane_h2.add(pane_q4, gbc_pane_q4);
		
		// 4eme trimestre	
		
		table_q4 = new JTable();
		table_q4.setMinimumSize(new Dimension(307, 612));
		table_q4.setMaximumSize(new Dimension(307, 612));
		table_q4.setPreferredSize(new Dimension(307, 612));
		table_q4.setRowMargin(0);
		table_q4.setFont(new Font("Tahoma", Font.PLAIN, 10));
		table_q4.setName("4");
		table_q4.setDefaultRenderer(Object.class, Quarter);
		table_q4.setRowHeight(19);
		table_q4.setRowMargin(1);
		table_q4.setPreferredScrollableViewportSize(new Dimension(307, 612));
		table_q4.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
				{null, null, null},
			},
			new String[] {
				"Octobre", "Novembre", "D\u00E9cembre"
			}
		));
		table_q4.getColumnModel().getColumn(1).setResizable(false);
		// Ajoute le header car pas de scrollpanel autour
		JTableHeader header_q4 = table_q4.getTableHeader();
		pane_q4.setLayout(new BorderLayout());
		pane_q4.add(header_q4, BorderLayout.NORTH);
		pane_q4.add(table_q4, BorderLayout.CENTER);
		
		// Panneau central 2er seme.
				pane_center_h2 = new JPanel();
				pane_center_h2.setMinimumSize(new Dimension(532, 400));
				pane_center_h2.setSize(new Dimension(532, 400));
				pane_center_h2.setPreferredSize(new Dimension(532, 400));
				pane_center_h2.setMaximumSize(new Dimension(532, 400));
				GridBagConstraints gbc_pane_center_h2 = new GridBagConstraints();
				gbc_pane_center_h2.anchor = GridBagConstraints.NORTH;
				gbc_pane_center_h2.gridx = 1;
				gbc_pane_center_h2.gridy = 0;
				pane_h2.add(pane_center_h2, gbc_pane_center_h2);
				GridBagLayout gbl_pane_center_h2 = new GridBagLayout();
				gbl_pane_center_h2.columnWidths = new int[]{266, 266, 0};
				gbl_pane_center_h2.rowHeights = new int[]{0, 0};
				gbl_pane_center_h2.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				gbl_pane_center_h2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
				pane_center_h2.setLayout(gbl_pane_center_h2);
				
				lblNewLabel1 = new JLabel("");
				lblNewLabel1.setMaximumSize(new Dimension(530, 400));
				lblNewLabel1.setMinimumSize(new Dimension(530, 400));
				lblNewLabel1.setSize(new Dimension(530, 400));
				lblNewLabel1.setPreferredSize(new Dimension(530, 400));
				lblNewLabel1.setIcon(new StretchIcon("image.jpg"));
				GridBagConstraints gbc_lblNewLabel1 = new GridBagConstraints();
				gbc_lblNewLabel1.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
				gbc_lblNewLabel1.gridwidth = 2;
				gbc_lblNewLabel1.gridx = 0;
				gbc_lblNewLabel1.gridy = 0;
				pane_center_h2.add(lblNewLabel1, gbc_lblNewLabel1);
		// Active le scroll sur le tappane
		scrollPane.setViewportView(tabpane);

		
				
		// Moon phases checkboc		
		cbMoon.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbMoon.setSize(new Dimension(0, 24));
		cbMoon.setMaximumSize(new Dimension(0, 24));
		cbMoon.setMinimumSize(new Dimension(120, 24));
		cbMoon.setPreferredSize(new Dimension(120, 24));
		cbMoon.setIcon(new ImageIcon(this.getClass().getResource("/resources/cbmu.png")));
		cbMoon.setSelectedIcon(new ImageIcon(this.getClass().getResource("/resources/cbmc.png")));
		// check box event
		cbMoon.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Config.dispMoon= cbMoon.isSelected();
				Quarter.ShowMoon= cbMoon.isSelected();
				frmCalendrier.repaint();
			}
		});
		pane_bottom.add(cbMoon);
		
		//
		lblNewLabel_1 = new JLabel("                                                                                         ");
		pane_bottom.add(lblNewLabel_1);
		


		// Year field
		YearField = new JTextField();
		YearField.getDocument().addDocumentListener(new DocumentListener() {
		  public void changedUpdate(DocumentEvent e) {
			 warn();
		  }
		  public void removeUpdate(DocumentEvent e) {
		     warn();
		  }
		  public void insertUpdate(DocumentEvent e) {
		     warn();
		  }
	      public void warn() {
			if (!Init ) {
			 	
				String syear =YearField.getText();
				if (syear.length() > 0) {
					Year= Integer.parseInt(syear);
					Quarter.setYear(Year);
					frmCalendrier.setTitle("Calendrier - "+syear);
					Quarter.repaint();
			    }
			  }
		   }
		 }
		);
		
		// Previous half button
		Image arrowl = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/arrowl.png"));
		JButton btnPrevious = new JButton("");
		btnPrevious.setPreferredSize(new Dimension(33, 25));
		btnPrevious.setIcon(new ImageIcon(arrowl));
		btnPrevious.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			yearbtnpressed(-1);
			
		}
		});
		pane_bottom.add(btnPrevious);
		YearField.setHorizontalAlignment(SwingConstants.CENTER);
		YearField.setColumns(4);

		pane_bottom.add(YearField);
		YearField.setText(syear);
		
		// Bouton Semestre suivant
		Image arrowr = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/arrowr.png"));							
		JButton btnNext = new JButton("");
		btnNext.setPreferredSize(new Dimension(33, 25));
		btnNext.setIcon(new ImageIcon(arrowr));
		btnNext.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
				yearbtnpressed(1) ;
		}
		});
		pane_bottom.add(btnNext);
		
		// Holidays checkboxes
		ImageIcon unchecked = new ImageIcon(this.getClass().getResource("/resources/cbmu.png"));
		cbVacA.setIcon(unchecked);
		checkedA.colEdgeu = new Color(122,138,153);
		cbVacA.setSelectedIcon(checkedA);
		cbVacA.setToolTipText("Caen, Clermont-Ferrand, Grenoble, Lyon, Montpellier, Nancy-Metz, Nantes, Rennes, Toulouse");
		cbVacA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbVacA.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Config.dispVacA= cbVacA.isSelected();
				Quarter.ShowVacA= cbVacA.isSelected();
				frmCalendrier.repaint();
			}
		});
		pane_bottom.add(cbVacA);
		
		cbVacB.setIcon(unchecked);
		checkedB.colEdgeu = new Color(122,138,153);
		cbVacB.setSelectedIcon(checkedB);
		cbVacB.setToolTipText("Aix-Marseille, Amiens, Besan\u00E7on, Dijon, Lille, Limoges, Nice, Orl\u00E9ans-Tours, Poitiers, Reims, Rouen, Strasbourg");
		cbVacB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbVacB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Config.dispVacB= cbVacB.isSelected();
				Quarter.ShowVacB= cbVacB.isSelected();
				frmCalendrier.repaint();
			}
		});
		pane_bottom.add(cbVacB);
		
		cbVacC.setIcon(unchecked);
		checkedC.colEdgeu = new Color(122,138,153);
		cbVacC.setSelectedIcon(checkedC);
		cbVacC.setToolTipText("Bordeaux, Cr\u00E9teil, Paris, Versailles");
		cbVacC.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbVacC.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Config.dispVacC= cbVacC.isSelected();
				Quarter.ShowVacC= cbVacC.isSelected();
				frmCalendrier.repaint();
			}
		});
		
		pane_bottom.add(cbVacC);
	
	
		// Met le calendrier au semestre courant
		
		int month =dt.getMonthOfYear();
		if ( month > 5) {
				tabpane.setSelectedIndex(1);
				

			}
		
		// Launch configuration dialog;
		pMnuGen = new JPopupMenu();
		pMnuGen.setLabel("Config");
		addPopup(pane_bottom, pMnuGen);
		
		
		// Launch config pôpup menu
		pMnuConfig = new JMenuItem("Config");
		pMnuConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Needs to memorize current location
				CurLoc = frmCalendrier.getLocation();
				Config.setVisible(true);
			}
		});
			
		// Configuration dialog closed with OK
		Config.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
			  // Re initialize vars and repaint;
				applyConfig();
				frmCalendrier.repaint();
				frmCalendrier.setLocation(CurLoc);
			}
		});
		
		pMnuGen.add(pMnuConfig);
		//System.out.print(Quarter.M);	
	Init = false;	
	}
	
	private void yearbtnpressed(int btn) {
	    int curindex = 0;
		int previndex = 1;
		if (btn > 0) {
			curindex =  1;
			previndex = 0;
		} ;
              Init= true;
		      if (tabpane.getSelectedIndex() == curindex) {
			  Year = Year+btn; 			 
			  Quarter.setYear(Year);
			  String syear ="";
			  syear += Year;
			  YearField.setText(syear);
			  frmCalendrier.setTitle("Calendrier - "+syear);
			  frmCalendrier.repaint();
			  tabpane.setSelectedIndex(previndex);
			  Init = false;
		  } else {
			  tabpane.setSelectedIndex(curindex);
		  }
	}
	// Popup menu to set configuration
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
