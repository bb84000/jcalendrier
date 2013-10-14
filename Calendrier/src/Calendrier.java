/**
 * Base class for Calendrier application 
 * 
 * 
 * bb 95 - october 2013
 *  
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
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
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
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
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.awt.Insets;

import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.ListSelectionModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class Calendrier {

	private JFrame frmCalendrier;	private JPanel pane_bottom;
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
	private Timer labelTimer;

	// Config variables are in dlgConfig class
	private dlgConfig Config;
	// This is the class to display days in cell
	// and load diverse lists 
	private DayCalRenderer Quarter = new DayCalRenderer();
	// This is the class for diverse astronomic computations
	private astro Astro = new astro();
	// Class about box
	private aboutBox about;
	// popup menu to config and about
	private JPopupMenu pMnuGen;
	private JMenuItem pMnuConfig;
	private JMenuItem pMnuAbout;

	private JTextField YearField;
	private JButton btnNext;
	private JButton btnPrevious;

	private JCheckBox cbMoon = new JCheckBox("Phases de la lune");
	private JCheckBox cbVacA = new JCheckBox("Vacances Zone A");
	private JCheckBox cbVacB = new JCheckBox("Vacances Zone B");
	private JCheckBox cbVacC = new JCheckBox("Vacances Zone C");
	private Image MainIcon;
	CheckBoxIcon checkedA = new CheckBoxIcon();
	CheckBoxIcon checkedB = new CheckBoxIcon();
	CheckBoxIcon checkedC = new CheckBoxIcon();
	
	private JLabel lblImage_1;
	private JLabel lblImage_2;
	private JPanel panelToday_1;
	private JPanel panelToday_2;
	private JPanel panelSelected_1;
	private JPanel panelSelected_2;
	private JPanel panelSeasons_1;
	private JPanel panelSeasons_2;
	private JLabel lblToday_2;
	private JLabel lblToday_1;
	private JLabel lblSelected_1;
	private JLabel lblSelected_2;
	private JLabel lblSeasons_2a;
	private JLabel lblSeasons_1a;

	private JLabel lblSeasons_2b;
	private JLabel lblSeasons_1b;
	// Needed to restore location after modal dialog
	private Point CurLoc = new Point();
	private Dimension CurSize = new Dimension();
	// needed to know if the date has changed since program has been launched 
	private int iYear;
	private int iDay;
	private int icounter;
	private JLabel lblToday_1a;
	private JLabel lblToday_2a;
	//private JButton btnPrevious;
	private JMenuItem pMnuDelImage;
	private JMenuItem pMnuPrefs;
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String OS = (System.getProperty("os.name")).toUpperCase();
					// Color buttons are disabled in mac ! so force our look and feel
					if (OS.contains("MAC ")) UIManager.setLookAndFeel(
							UIManager.getCrossPlatformLookAndFeelClassName());
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

	private void applyConfig() {
		// set size and position
		if (Config.savePos) {
			frmCalendrier.setSize(Config.size);

			frmCalendrier.setLocation(Config.location);
		} else frmCalendrier.setLocationRelativeTo(null);
		
		// set diverse colors
		Quarter.colA = Config.colvacA;
		checkedA.colFillu = Config.colvacA;
		Quarter.colB = Config.colvacB;
		checkedB.colFillu = Config.colvacB;
		Quarter.colC = Config.colvacC;
		checkedC.colFillu = Config.colvacC;

		Quarter.colback= Config.colback;
		Quarter.colsun= Config.colsun;
		
		// set moons
		if (Config.saveMoon) {
			cbMoon.setSelected(Config.dispMoon);
			Quarter.ShowMoon = cbMoon.isSelected();
		}
		
		// set scolar holidays
		if (Config.saveVacScol) {
			cbVacA.setSelected(Config.dispVacA);
			Quarter.ShowVacA = cbVacA.isSelected();
			cbVacB.setSelected(Config.dispVacB);
			Quarter.ShowVacB = cbVacB.isSelected();
			cbVacC.setSelected(Config.dispVacC);
			Quarter.ShowVacC = cbVacC.isSelected();
		}
		
		
		
	}

	// Frame initialization 
	private void initialize() {
		Init = true;
		// Icone de l'application
		MainIcon = Toolkit.getDefaultToolkit().getImage(
		Calendrier.class.getResource("/resources/calendrier.png"));
		
		// Set config file name and load config if exists
		Config = new dlgConfig(frmCalendrier);
		Config.setIconImage(MainIcon);
		if (!Beans.isDesignTime()) {
			
			Config.set_config_file("calendrier.config.xml");
			try {
				Config.loadConfigXML();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
		}
		DateTime dt = new DateTime();
		Year = dt.getYear();
		iYear = Year;
		iDay = dt.getDayOfYear();
		
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
				Config.location = frmCalendrier.getLocation();
				Config.size = frmCalendrier.getSize();
				Config.saveConfigXML();
				// Sort half images list on year field and save it
				Quarter.imagesHalf.sort(0);
				Quarter.imagesHalf.writeCSVfile(Quarter.imgfile);
			}
		});
		
		//frmCalendrier.setSize(new Dimension(1200, 740));
		frmCalendrier.setMaximumSize(new Dimension(1200, 740));
		frmCalendrier.getContentPane().setMaximumSize(
				new Dimension(1240, 2147483647));
		frmCalendrier.setIconImage(MainIcon);
		frmCalendrier.setBounds(100, 100, 450, 300);
		frmCalendrier.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCalendrier.setSize(1200, 720);
		 
		String syear = "";
		syear += Year;
		frmCalendrier.setTitle("Calendrier - " + syear);
		
		
		// quarter intialization
		
		
		Quarter.workingDirectory = Config.workingDirectory;
		Quarter.init();	
		Quarter.setYear(Year);
		
		// bottom panel for buttons
		pane_bottom = new JPanel();

		pane_bottom.setMaximumSize(new Dimension(1200, 32767));
		pane_bottom.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		pane_bottom.setPreferredSize(new Dimension(10, 40));
		frmCalendrier.getContentPane().add(pane_bottom, BorderLayout.SOUTH);

		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(1180, 2));
		scrollPane.setMaximumSize(new Dimension(1180, 32767));
		frmCalendrier.getContentPane().add(scrollPane, BorderLayout.CENTER);

		// tabs panel and scroll
		tabpane = new JTabbedPane(JTabbedPane.TOP);
		scrollPane.setColumnHeaderView(tabpane);

		// 1st half pane
		pane_h1 = new JPanel();
		pane_h1.setName("pane_h1");
		tabpane.addTab("1er semestre", null, pane_h1, null);
		GridBagLayout gbl_pane_h1 = new GridBagLayout();
		gbl_pane_h1.columnWidths = new int[] { 313, 266, 266, 313, 0 };
		gbl_pane_h1.rowHeights = new int[] { 400, 150, 34, 0 };
		gbl_pane_h1.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pane_h1.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pane_h1.setLayout(gbl_pane_h1);

		// 1st quarter table
		pane_q1 = new JPanel();
		pane_q1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q1.setPreferredSize(new Dimension(313, 613));
		pane_q1.setMinimumSize(new Dimension(313, 613));
		pane_q1.setMaximumSize(new Dimension(313, 613));
		GridBagConstraints gbc_pane_q1 = new GridBagConstraints();
		gbc_pane_q1.insets = new Insets(0, 0, 0, 0);
		gbc_pane_q1.gridheight = 3;
		gbc_pane_q1.anchor = GridBagConstraints.NORTHWEST;
		gbc_pane_q1.gridx = 0;
		gbc_pane_q1.gridy = 0;
		pane_h1.add(pane_q1, gbc_pane_q1);

		// 1st quarter table
		// Disable cell edition to have a read only table
		// quarters names are "1", "2", "3", and "4" 
		// to allow retreiving their instances in events  
		table_q1 = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table_q1.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
		});
		table_q1.setCellSelectionEnabled(true);
		table_q1.setRowSelectionAllowed(false);
		table_q1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table_q1.setPreferredScrollableViewportSize(new Dimension(10, 612));
		table_q1.setSize(new Dimension(313, 612));
		table_q1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		table_q1.setMaximumSize(new Dimension(313, 612));
		table_q1.setMinimumSize(new Dimension(313, 612));
		table_q1.setPreferredSize(new Dimension(313, 612));
		table_q1.setName("1");
		table_q1.setDefaultRenderer(Object.class, Quarter);
		table_q1.setRowHeight(19);
		table_q1.setRowMargin(1);
		table_q1.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, }, new String[] { "Janvier",
				"F\u00E9vrier", "Mars" }));
		// Add the header
		JTableHeader header_q1 = table_q1.getTableHeader();
		pane_q1.setLayout(new BorderLayout());
		pane_q1.add(header_q1, BorderLayout.NORTH);
		pane_q1.add(table_q1, BorderLayout.CENTER);

		
		// Todo user selected images
		lblImage_1 = new JLabel("");
		lblImage_1.setName("halfimg1");
		lblImage_1.setOpaque(true);
		lblImage_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage_1.setToolTipText("Cliquez avec le bouton droit de la souris pour choisir une image");
		lblImage_1.setMaximumSize(new Dimension(530, 400));
		lblImage_1.setMinimumSize(new Dimension(530, 400));
		lblImage_1.setSize(new Dimension(530, 400));
		lblImage_1.setPreferredSize(new Dimension(530, 400));
		
		
		GridBagConstraints gbc_lblImage_1 = new GridBagConstraints();
		gbc_lblImage_1.insets = new Insets(0, 0, 0, 0);
		gbc_lblImage_1.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		gbc_lblImage_1.gridwidth = 2;
		gbc_lblImage_1.gridx = 1;
		gbc_lblImage_1.gridy = 0;
		pane_h1.add(lblImage_1, gbc_lblImage_1);

		// 2nd quarter
		pane_q2 = new JPanel();
		pane_q2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q2.setMinimumSize(new Dimension(313, 613));
		pane_q2.setMaximumSize(new Dimension(313, 613));
		pane_q2.setPreferredSize(new Dimension(313, 613));
		GridBagConstraints gbc_pane_q2 = new GridBagConstraints();
		gbc_pane_q2.gridheight = 3;
		gbc_pane_q2.anchor = GridBagConstraints.NORTHWEST;
		gbc_pane_q2.gridx = 3;
		gbc_pane_q2.gridy = 0;
		pane_h1.add(pane_q2, gbc_pane_q2);

		// 2nd quarter table
		table_q2 = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		table_q2.setMinimumSize(new Dimension(313, 612));
		table_q2.setMaximumSize(new Dimension(313, 612));
		table_q2.setPreferredSize(new Dimension(313, 612));
		table_q2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		table_q2.setName("2");
		table_q2.setDefaultRenderer(Object.class, Quarter);
		table_q2.setRowHeight(19);
		table_q2.setRowMargin(1);
		table_q2.setPreferredScrollableViewportSize(new Dimension(313, 612));
		table_q2.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, },
				new String[] { "Avril", "Mai", "Juin" }));
		// Add header
		JTableHeader header_q2 = table_q2.getTableHeader();
		pane_q2.setLayout(new BorderLayout());
		pane_q2.add(header_q2, BorderLayout.NORTH);
		pane_q2.add(table_q2, BorderLayout.CENTER);

		// Panel Today
		panelToday_1 = new JPanel();
		panelToday_1.setBorder(new TitledBorder(null, "Aujourd'hui",
				TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelToday_1.setPreferredSize(new Dimension(10, 150));
		panelToday_1.setSize(new Dimension(0, 120));
		panelToday_1.setMinimumSize(new Dimension(10, 127));
		panelToday_1.setMaximumSize(new Dimension(32767, 120));
		panelToday_1.setLayout(null);
		GridBagConstraints gbc_panelToday_1 = new GridBagConstraints();
		gbc_panelToday_1.insets = new Insets(0, 0, 0, 0);
		gbc_panelToday_1.anchor = GridBagConstraints.NORTH;
		gbc_panelToday_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelToday_1.gridx = 1;
		gbc_panelToday_1.gridy = 1;
		pane_h1.add(panelToday_1, gbc_panelToday_1);
		
		// Label today
		lblToday_1 = new JLabel("Today");
		lblToday_1.setBounds(10, 20, 245, 15);
		lblToday_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblToday_1.setVerticalTextPosition(SwingConstants.TOP);
		lblToday_1.setVerticalAlignment(SwingConstants.TOP);
		lblToday_1.setPreferredSize(new Dimension(245, 15));
		lblToday_1.setHorizontalTextPosition(SwingConstants.LEFT);
		lblToday_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblToday_1.setAlignmentY(0.0f);
		panelToday_1.add(lblToday_1);
		
		lblToday_1a = new JLabel("New label");
		lblToday_1a.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblToday_1a.setVerticalAlignment(SwingConstants.TOP);
		lblToday_1a.setBounds(10, 34, 245, 100);
		panelToday_1.add(lblToday_1a);

		// panel selected day
		panelSelected_1 = new JPanel();
		panelSelected_1.setSize(new Dimension(0, 120));
		panelSelected_1.setPreferredSize(new Dimension(10, 120));
		panelSelected_1.setMinimumSize(new Dimension(10, 120));
		panelSelected_1.setMaximumSize(new Dimension(32767, 120));
		panelSelected_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"),
				"Jour s\u00E9lectionn\u00E9", TitledBorder.LEFT,
				TitledBorder.TOP, null, null));
		panelSelected_1.setLayout(null);
		GridBagConstraints gbc_panelSelected_1 = new GridBagConstraints();
		gbc_panelSelected_1.insets = new Insets(0, 0, 0, 0);
		gbc_panelSelected_1.fill = GridBagConstraints.BOTH;
		gbc_panelSelected_1.gridx = 2;
		gbc_panelSelected_1.gridy = 1;
		pane_h1.add(panelSelected_1, gbc_panelSelected_1);
		
		// label selected
		lblSelected_1 = new JLabel("Selected");
		lblSelected_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSelected_1.setBounds(10, 20, 245, 110);
		lblSelected_1.setVerticalTextPosition(SwingConstants.TOP);
		lblSelected_1.setVerticalAlignment(SwingConstants.TOP);
		lblSelected_1.setPreferredSize(new Dimension(240, 110));
		lblSelected_1.setHorizontalTextPosition(SwingConstants.LEFT);
		lblSelected_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelected_1.setAlignmentY(0.0f);
		panelSelected_1.add(lblSelected_1);

		// panel seasons
		panelSeasons_1 = new JPanel();
		panelSeasons_1.setSize(new Dimension(525, 0));
		panelSeasons_1.setMaximumSize(new Dimension(525, 32767));
		panelSeasons_1.setPreferredSize(new Dimension(525, 10));
		panelSeasons_1.setBorder(new TitledBorder(null, "Saisons",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelSeasons_1 = new GridBagConstraints();
		gbc_panelSeasons_1.insets = new Insets(0, 0, 0, 0);
		gbc_panelSeasons_1.gridwidth = 2;
		gbc_panelSeasons_1.fill = GridBagConstraints.BOTH;
		gbc_panelSeasons_1.gridx = 1;
		gbc_panelSeasons_1.gridy = 2;
		pane_h1.add(panelSeasons_1, gbc_panelSeasons_1);
		GridBagLayout gbl_panelSeasons_1 = new GridBagLayout();
		gbl_panelSeasons_1.columnWidths = new int[] { 260, 266, 0 };
		gbl_panelSeasons_1.rowHeights = new int[] { 40, 0 };
		gbl_panelSeasons_1.columnWeights = new double[] { 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panelSeasons_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panelSeasons_1.setLayout(gbl_panelSeasons_1);

		// label seasons left
		lblSeasons_1a = new JLabel("Seasons");
		lblSeasons_1a.setMaximumSize(new Dimension(40, 30));
		lblSeasons_1a.setMinimumSize(new Dimension(245, 30));
		lblSeasons_1a.setSize(new Dimension(255, 30));
		lblSeasons_1a.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSeasons_1a.setVerticalTextPosition(SwingConstants.TOP);
		lblSeasons_1a.setVerticalAlignment(SwingConstants.TOP);
		lblSeasons_1a.setPreferredSize(new Dimension(245, 35));
		GridBagConstraints gbc_lblSeasons_1a = new GridBagConstraints();
		gbc_lblSeasons_1a.insets = new Insets(0, 8, 0, 0);
		gbc_lblSeasons_1a.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSeasons_1a.gridx = 0;
		gbc_lblSeasons_1a.gridy = 0;
		panelSeasons_1.add(lblSeasons_1a, gbc_lblSeasons_1a);

		// label seasons right
		lblSeasons_1b = new JLabel("Seasons");
		lblSeasons_1b.setSize(new Dimension(245, 30));
		lblSeasons_1b.setPreferredSize(new Dimension(245, 35));
		lblSeasons_1b.setMinimumSize(new Dimension(245, 30));
		lblSeasons_1b.setMaximumSize(new Dimension(40, 30));
		lblSeasons_1b.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSeasons_1b.setVerticalTextPosition(SwingConstants.TOP);
		lblSeasons_1b.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_lblSeasons_1b = new GridBagConstraints();
		gbc_lblSeasons_1b.insets = new Insets(0, 8, 0, 0);
		gbc_lblSeasons_1b.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSeasons_1b.gridx = 1;
		gbc_lblSeasons_1b.gridy = 0;
		panelSeasons_1.add(lblSeasons_1b, gbc_lblSeasons_1b);

		// 2nd half panel
		pane_h2 = new JPanel();
		pane_h2.setName("pane_h2");
		tabpane.addTab("2\u00E8me semestre", null, pane_h2, null);
		GridBagLayout gbl_pane_h2 = new GridBagLayout();
		gbl_pane_h2.columnWidths = new int[] { 313, 266, 266, 313, 0 };
		gbl_pane_h2.rowHeights = new int[] { 400, 150, 34, 0 };
		gbl_pane_h2.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pane_h2.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pane_h2.setLayout(gbl_pane_h2);

		pane_q3 = new JPanel();
		pane_q3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q3.setPreferredSize(new Dimension(313, 613));
		pane_q3.setMinimumSize(new Dimension(313, 613));
		pane_q3.setMaximumSize(new Dimension(313, 613));
		GridBagConstraints gbc_pane_q3 = new GridBagConstraints();
		gbc_pane_q3.insets = new Insets(0, 0, 0, 0);
		gbc_pane_q3.gridheight = 3;
		gbc_pane_q3.anchor = GridBagConstraints.NORTHWEST;
		// gbc_pane_q3.fill = GridBagConstraints.VERTICAL;
		gbc_pane_q3.gridx = 0;
		gbc_pane_q3.gridy = 0;
		pane_h2.add(pane_q3, gbc_pane_q3);

		table_q3 = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table_q3.setPreferredScrollableViewportSize(new Dimension(313, 612));
		table_q3.setFont(new Font("Tahoma", Font.PLAIN, 10));
		table_q3.setMaximumSize(new Dimension(313, 612));
		table_q3.setMinimumSize(new Dimension(313, 612));
		table_q3.setPreferredSize(new Dimension(313, 612));
		table_q3.setName("3");
		table_q3.setDefaultRenderer(Object.class, Quarter);
		table_q3.setRowHeight(19);
		table_q3.setRowMargin(1);
		table_q3.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, }, new String[] { "Juillet", "Ao\u00FBt",
				"Septembre" }));
		// Ajoute le header car pas de scrollpanel autour
		JTableHeader header_q3 = table_q3.getTableHeader();
		pane_q3.setLayout(new BorderLayout());
		pane_q3.add(header_q3, BorderLayout.NORTH);
		pane_q3.add(table_q3, BorderLayout.CENTER);

		// Ajoute image
		lblImage_2 = new JLabel("");
		lblImage_2.setName("halfimg2");
		lblImage_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage_2.setToolTipText("Cliquez avec le bouton droit de la souris pour choisir une image");
		lblImage_2.setMaximumSize(new Dimension(530, 400));
		lblImage_2.setMinimumSize(new Dimension(530, 400));
		lblImage_2.setSize(new Dimension(530, 400));
		lblImage_2.setPreferredSize(new Dimension(530, 400));
		
		
		GridBagConstraints gbc_lblImage_2 = new GridBagConstraints();
		gbc_lblImage_2.insets = new Insets(0, 0, 0, 0);
		gbc_lblImage_2.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		gbc_lblImage_2.gridwidth = 2;
		// gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblImage_2.gridx = 1;
		gbc_lblImage_2.gridy = 0;
		pane_h2.add(lblImage_2, gbc_lblImage_2);

		// 4eme trimestre
		pane_q4 = new JPanel();
		pane_q4.setPreferredSize(new Dimension(313, 613));
		pane_q4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q4.setMinimumSize(new Dimension(313, 613));
		pane_q4.setMaximumSize(new Dimension(313, 613));
		GridBagConstraints gbc_pane_q4 = new GridBagConstraints();
		gbc_pane_q4.gridheight = 3;
		gbc_pane_q4.anchor = GridBagConstraints.NORTHWEST;
		gbc_pane_q4.gridx = 3;
		gbc_pane_q4.gridy = 0;
		pane_h2.add(pane_q4, gbc_pane_q4);

		// 4eme trimestre

		table_q4 = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table_q4.setMinimumSize(new Dimension(313, 612));
		table_q4.setMaximumSize(new Dimension(313, 612));
		table_q4.setPreferredSize(new Dimension(313, 612));
		table_q4.setRowMargin(0);
		table_q4.setFont(new Font("Tahoma", Font.PLAIN, 10));
		table_q4.setName("4");
		table_q4.setDefaultRenderer(Object.class, Quarter);
		table_q4.setRowHeight(19);
		table_q4.setRowMargin(1);
		table_q4.setPreferredScrollableViewportSize(new Dimension(313, 612));
		table_q4.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, }, new String[] { "Octobre", "Novembre",
				"D\u00E9cembre" }));
		// Ajoute le header car pas de scrollpanel autour
		JTableHeader header_q4 = table_q4.getTableHeader();
		pane_q4.setLayout(new BorderLayout());
		pane_q4.add(header_q4, BorderLayout.NORTH);
		pane_q4.add(table_q4, BorderLayout.CENTER);
		// Active le scroll sur le pane_center_h2.add(lblNewLabel1, tappane
		scrollPane.setViewportView(tabpane);

		// Panel Today
		panelToday_2 = new JPanel();
		panelToday_2.setPreferredSize(new Dimension(10, 150));
		panelToday_2.setBorder(new TitledBorder(null, "Aujourd'hui",
				TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelToday_2.setMinimumSize(new Dimension(10, 120));
		panelToday_2.setLayout(null);
		GridBagConstraints gbc_panelToday_2 = new GridBagConstraints();
		gbc_panelToday_2.insets = new Insets(0, 0, 0, 0);
		gbc_panelToday_2.anchor = GridBagConstraints.NORTH;
		gbc_panelToday_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelToday_2.gridx = 1;
		gbc_panelToday_2.gridy = 1;
		pane_h2.add(panelToday_2, gbc_panelToday_2);
		lblToday_2 = new JLabel("Today");
		lblToday_2.setBounds(10, 20, 245, 15);
		lblToday_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblToday_2.setPreferredSize(new Dimension(245, 15));
		lblToday_2.setAlignmentY(Component.TOP_ALIGNMENT);
		lblToday_2.setVerticalTextPosition(SwingConstants.TOP);
		lblToday_2.setVerticalAlignment(SwingConstants.TOP);
		lblToday_2.setHorizontalTextPosition(SwingConstants.LEFT);
		lblToday_2.setHorizontalAlignment(SwingConstants.LEFT);
		panelToday_2.add(lblToday_2);
		
		lblToday_2a = new JLabel("New label");
		lblToday_2a.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblToday_2a.setVerticalAlignment(SwingConstants.TOP);
		lblToday_2a.setBounds(10, 34, 245, 93);
		panelToday_2.add(lblToday_2a);

		// panel selected day
		panelSelected_2 = new JPanel();
		panelSelected_2.setSize(new Dimension(0, 120));
		panelSelected_2.setPreferredSize(new Dimension(10, 120));
		panelSelected_2.setMinimumSize(new Dimension(10, 120));
		panelSelected_2.setMaximumSize(new Dimension(32767, 120));
		panelSelected_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"),
				"Jour s\u00E9lectionn\u00E9", TitledBorder.LEFT,
				TitledBorder.TOP, null, null));
		panelSelected_2.setLayout(null);
		GridBagConstraints gbc_panelSelected_2 = new GridBagConstraints();
		gbc_panelSelected_2.insets = new Insets(0, 0, 0, 0);
		gbc_panelSelected_2.fill = GridBagConstraints.BOTH;
		gbc_panelSelected_2.gridx = 2;
		gbc_panelSelected_2.gridy = 1;
		pane_h2.add(panelSelected_2, gbc_panelSelected_2);
		lblSelected_2 = new JLabel("Selected");
		lblSelected_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSelected_2.setBounds(10, 20, 245, 110);
		lblSelected_2.setVerticalTextPosition(SwingConstants.TOP);
		lblSelected_2.setVerticalAlignment(SwingConstants.TOP);
		lblSelected_2.setPreferredSize(new Dimension(240, 110));
		lblSelected_2.setHorizontalTextPosition(SwingConstants.LEFT);
		lblSelected_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblSelected_2.setAlignmentY(0.0f);
		panelSelected_2.add(lblSelected_2);

		// Seasons
		panelSeasons_2 = new JPanel();
		panelSeasons_2.setSize(new Dimension(525, 0));
		panelSeasons_2.setMaximumSize(new Dimension(525, 32767));
		panelSeasons_2.setPreferredSize(new Dimension(525, 10));
		panelSeasons_2.setBorder(new TitledBorder(null, "Saisons",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelSeasons_2 = new GridBagConstraints();
		gbc_panelSeasons_2.insets = new Insets(0, 0, 0, 0);
		gbc_panelSeasons_2.gridwidth = 2;
		gbc_panelSeasons_2.fill = GridBagConstraints.BOTH;
		gbc_panelSeasons_2.gridx = 1;
		gbc_panelSeasons_2.gridy = 2;
		pane_h2.add(panelSeasons_2, gbc_panelSeasons_2);
		GridBagLayout gbl_panelSeasons_2 = new GridBagLayout();
		gbl_panelSeasons_2.columnWidths = new int[] { 260, 266, 0 };
		gbl_panelSeasons_2.rowHeights = new int[] { 40, 0 };
		gbl_panelSeasons_2.columnWeights = new double[] { 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panelSeasons_2.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panelSeasons_2.setLayout(gbl_panelSeasons_2);
		// left label
		lblSeasons_2a = new JLabel("Seasons");
		lblSeasons_2a.setMaximumSize(new Dimension(40, 30));
		lblSeasons_2a.setMinimumSize(new Dimension(245, 30));
		lblSeasons_2a.setSize(new Dimension(255, 30));
		lblSeasons_2a.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSeasons_2a.setVerticalTextPosition(SwingConstants.TOP);
		lblSeasons_2a.setVerticalAlignment(SwingConstants.TOP);
		lblSeasons_2a.setPreferredSize(new Dimension(245, 35));
		GridBagConstraints gbc_lblSeasons_2a = new GridBagConstraints();
		gbc_lblSeasons_2a.insets = new Insets(0, 8, 0, 0);
		gbc_lblSeasons_2a.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSeasons_2a.gridx = 0;
		gbc_lblSeasons_2a.gridy = 0;
		panelSeasons_2.add(lblSeasons_2a, gbc_lblSeasons_2a);
		lblSeasons_2b = new JLabel("Seasons");
		lblSeasons_2b.setVerticalTextPosition(SwingConstants.TOP);
		lblSeasons_2b.setVerticalAlignment(SwingConstants.TOP);
		lblSeasons_2b.setSize(new Dimension(245, 30));
		lblSeasons_2b.setPreferredSize(new Dimension(245, 35));
		lblSeasons_2b.setMinimumSize(new Dimension(245, 30));
		lblSeasons_2b.setMaximumSize(new Dimension(40, 30));
		lblSeasons_2b.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblSeasons_2b = new GridBagConstraints();
		gbc_lblSeasons_2b.insets = new Insets(0, 8, 0, 0);
		gbc_lblSeasons_2b.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSeasons_2b.gridx = 1;
		gbc_lblSeasons_2b.gridy = 0;
		panelSeasons_2.add(lblSeasons_2b, gbc_lblSeasons_2b);
		
		
		
		// Popup half image
		
		ActionListener ppal = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JMenuItem jmi = (JMenuItem) e.getSource();
				String jminame = jmi.getName();
				try {
					// Choose an iamge
					if (jminame.equals("pmnuchoose")) chooseHalfImage(Year, tabpane.getSelectedIndex());
					// delete an image
					if (jminame.equals("pmnudelete")) {
						int index = tabpane.getSelectedIndex();
						removeHalfImage(Year, index);
					}
					// show preferences dialog
					if (jminame.equals("pmnuprefs")){
						// Needs to memorize current location
						CurLoc = frmCalendrier.getLocation();
					    CurSize = frmCalendrier.getSize();
					    
						Config.setVisible(true);
					}
					// Show about dialog
					if (jminame.equals("pmnuabout")) about.setVisible(true);
				} catch (Exception e1) {
					// do nothing item without name
				}				
			}
		};
		
		JPopupMenu pMnuImage_h1 = new JPopupMenu();
		
		addPopup(lblImage_1, pMnuImage_h1);
		addPopup(lblImage_2, pMnuImage_h1);
		
		JMenuItem pMnuSelImage = new JMenuItem("Choisir une image");
		pMnuSelImage.setName("pmnuchoose");
		pMnuSelImage.addActionListener(ppal);
			pMnuImage_h1.add(pMnuSelImage);
		
		pMnuDelImage = new JMenuItem("Effacer");
		pMnuDelImage.setName("pmnudelete");
		int index = tabpane.getSelectedIndex();
		String s = Quarter.HalfImages [index+1];
		pMnuDelImage.setText("Effacer "+s);
		pMnuDelImage.addActionListener(ppal);
		pMnuImage_h1.add(pMnuDelImage);
		
		pMnuPrefs = new JMenuItem("Pr\u00E9f\u00E9rences");
		pMnuPrefs.addActionListener(ppal);
		pMnuPrefs.setName("pmnuprefs");
		pMnuImage_h1.add(pMnuPrefs);
		
		JMenuItem pMnuAPropos = new JMenuItem("A propos");
		pMnuAPropos.addActionListener(ppal);
		pMnuAPropos.setName("pmnuabout");
		pMnuImage_h1.add(pMnuAPropos);
		
		
		setHalfImage(Year, 0);
		setHalfImage(Year, 1);
		cbMoon.setLocation(312, 8);
		

		// Moon phases checkboc
		cbMoon.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbMoon.setSize(new Dimension(121, 23));
		cbMoon.setMaximumSize(new Dimension(0, 24));
		cbMoon.setMinimumSize(new Dimension(120, 24));
		cbMoon.setPreferredSize(new Dimension(120, 24));
		cbMoon.setIcon(new ImageIcon(this.getClass().getResource(
				"/resources/cbmu.png")));
		cbMoon.setSelectedIcon(new ImageIcon(this.getClass().getResource(
				"/resources/cbmc.png")));
		// check box event
		cbMoon.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Config.dispMoon = cbMoon.isSelected();
				Quarter.ShowMoon = cbMoon.isSelected();
				frmCalendrier.repaint();
			}
		});
		pane_bottom.setLayout(null);
		pane_bottom.add(cbMoon);

		// Year field
		YearField = new JTextField();
		YearField.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				// center YearFiled
				Point yfloc = YearField.getLocation();
				
				YearField.setLocation(yfloc);
			}
		});
		YearField.setBounds(526, 9, 38, 20);
		
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
				if (!Init) {

					String syear = YearField.getText();
					yearchanged(syear);
					
				}
			}
		});

		// Previous half button
		Image arrowl = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("resources/arrowl.png"));
		btnPrevious = new JButton("");
		btnPrevious.setBounds(478, 7, 33, 25);
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
		if (!Beans.isDesignTime()) {

			YearField.setText(syear);
		}
		// Bouton Semestre suivant
		Image arrowr = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("resources/arrowr.png"));
		btnNext = new JButton("");
		btnNext.setBounds(579, 7, 33, 25);
		btnNext.setPreferredSize(new Dimension(33, 25));
		btnNext.setIcon(new ImageIcon(arrowr));
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yearbtnpressed(1);
			}
		});
		pane_bottom.add(btnNext);

		// Holidays checkboxes
		ImageIcon unchecked = new ImageIcon(this.getClass().getResource(
				"/resources/cbmu.png"));
		cbVacA.setBounds(627, 8, 121, 23);
		cbVacA.setIcon(unchecked);
		checkedA.colEdgeu = new Color(122, 138, 153);
		cbVacA.setSelectedIcon(checkedA);
		cbVacA.setToolTipText("Caen, Clermont-Ferrand, Grenoble, Lyon, Montpellier, Nancy-Metz, Nantes, Rennes, Toulouse");
		cbVacA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbVacA.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Config.dispVacA = cbVacA.isSelected();
				Quarter.ShowVacA = cbVacA.isSelected();
				frmCalendrier.repaint();
			}
		});
		pane_bottom.add(cbVacA);
		cbVacB.setBounds(763, 8, 121, 23);

		cbVacB.setIcon(unchecked);
		checkedB.colEdgeu = new Color(122, 138, 153);
		cbVacB.setSelectedIcon(checkedB);
		cbVacB.setToolTipText("Aix-Marseille, Amiens, Besan\u00E7on, Dijon, Lille, Limoges, Nice, Orl\u00E9ans-Tours, Poitiers, Reims, Rouen, Strasbourg");
		cbVacB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbVacB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Config.dispVacB = cbVacB.isSelected();
				Quarter.ShowVacB = cbVacB.isSelected();
				frmCalendrier.repaint();
			}
		});
		pane_bottom.add(cbVacB);
		cbVacC.setBounds(899, 8, 121, 23);

		cbVacC.setIcon(unchecked);
		checkedC.colEdgeu = new Color(122, 138, 153);
		cbVacC.setSelectedIcon(checkedC);
		cbVacC.setToolTipText("Bordeaux, Cr\u00E9teil, Paris, Versailles");
		cbVacC.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbVacC.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Config.dispVacC = cbVacC.isSelected();
				Quarter.ShowVacC = cbVacC.isSelected();
				frmCalendrier.repaint();
			}
		});

		pane_bottom.add(cbVacC);

		// Met le calendrier au semestre courant
		int month = dt.getMonthOfYear();
		if (month > 5) {
			tabpane.setSelectedIndex(1);
		}

		setSeasons(Year);

		// Config and about popup menus
		// Component listener to set the modal dialogs at the center of frame

		ComponentAdapter ppca = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				try {
					Point p = frmCalendrier.getLocation();
					p.x += (frmCalendrier.getWidth() / 2) - (arg0.getComponent().getWidth() / 2);
					p.y += (frmCalendrier.getHeight() / 2) - (arg0.getComponent().getHeight() / 2);
					arg0.getComponent().setLocation(p);
				} catch (Exception e) {
					//Do nothing, error !
				}
			}
			@Override
			public void componentHidden(ComponentEvent arg0) {
				// if config dialog, initialize vars and repaint;
				// else do nothing
				if (arg0.getComponent().getName().equals("dlgConfig")) {
					applyConfig();
					frmCalendrier.repaint();
					frmCalendrier.setLocation(CurLoc);
					frmCalendrier.setSize(CurSize);
					DateTime dt =new DateTime();
					setLabelToday(dt);
				}
			}
		};
		
		// Launch config popup menu;
		pMnuGen = new JPopupMenu();
		pMnuGen.setBounds(-10008, -10672, 111, 50);
		pMnuGen.setLabel("Config");
		addPopup(pane_bottom, pMnuGen);

		// Config dialog
		pMnuConfig = new JMenuItem("Pr\u00E9f\u00E9rences");
		pMnuConfig.setName("pmnuprefs");
		pMnuGen.add(pMnuConfig);
		
		// Launch config popup menu
		pMnuConfig.addActionListener(ppal);
		Config.setName("dlgConfig");
		Config.addComponentListener(ppca);


		// About dialogimplementationVersion
		about = new aboutBox(frmCalendrier);
		about.setTitle("A propos du Calendrier");
		about.setIconImage(MainIcon);
		about.setName("dlgAbout");
		about.lblicon.setIcon(new StretchIcon(MainIcon));		
		about.lblprogname.setText("Calendrier");
		about.lblVersion.setText("Version : "+Config.version+"."+Config.build);
		about.lblWebsite.setText("www.sdtp.com");
		about.urlUpdate = "www.sdtp.com/versions/version.php?program=jcalendrier&version="+Config.version+"."+Config.build;  
		about.addComponentListener(ppca);	
		String s0;
		try {
			s0 = " - ";
			s0 += Config.builddate.toString("dd/MM/yyyy HH:mm:ss");
		} catch (Exception e1) {
			// invalid or no date
			s0="";
		}
		about.lblvendor.setText(Config.vendor+s0);

		// About menu item
		pMnuAbout = new JMenuItem("A propos");
		pMnuAbout.setName("pmnuabout");
		
		// Display aboutbox
		pMnuAbout.addActionListener(ppal);
		pMnuGen.add(pMnuAbout);
		
		MouseMotionAdapter mma = new MouseMotionAdapter() {
			  @Override
		      public void mouseMoved(MouseEvent e) {
		        setTooltipOver(e.getComponent(), e.getPoint());
		      }
	    };
	
		// ClicK on a day in tables 
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					setLabelSelected (e.getComponent());
			}
		};
		
		table_q1.addMouseListener(ma);
		table_q2.addMouseListener(ma);
		table_q3.addMouseListener(ma);
		table_q4.addMouseListener(ma);
		
		table_q1.addMouseMotionListener(mma);
		table_q2.addMouseMotionListener(mma);
		table_q3.addMouseMotionListener(mma);
		table_q4.addMouseMotionListener(mma);
		
		// Change day with keyboard 
		KeyAdapter ka =new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				setLabelSelected(arg0.getComponent());	
			}
		};
		
		table_q1.addKeyListener(ka);
		table_q2.addKeyListener(ka);
		table_q3.addKeyListener(ka);
		table_q4.addKeyListener(ka);


		// set labels if running
		Init = false;
		if (!Beans.isDesignTime()) {
			setLabelToday(dt);
			lblSelected_1.setText("");
			lblSelected_2.setText("");
			
			startLabel2();
		}
		// Apply configuration parameters
		applyConfig();
		
		// Center buttons on botton pane
		pane_bottom.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				
				// Yearfield location
				Point yrp = YearField.getLocation();
				yrp.x = (pane_bottom.getWidth()/2)- (YearField.getWidth()/2) ;
				YearField.setLocation(yrp);
				// BtnNext
				Point bnp = btnNext.getLocation();
				bnp.x = yrp.x+ YearField.getWidth() + 10;
				btnNext.setLocation(bnp); 
				// btnPrevious
				Point bpp = btnPrevious.getLocation();
				bpp.x = yrp.x - btnPrevious.getWidth() - 10;
				btnPrevious.setLocation(bpp);
				// CB VacA
				Point vap = cbVacA.getLocation();
				vap.x= bnp.x + btnNext.getWidth() + 10;
				cbVacA.setLocation(vap);
				// CB VacB
				Point vbp = cbVacB.getLocation();
				vbp.x= vap.x + cbVacA.getWidth() + 10;
				cbVacB.setLocation(vbp);		
				// CB VacC
				Point vcp = cbVacC.getLocation();
				vcp.x= vbp.x + cbVacB.getWidth() + 10;
				cbVacC.setLocation(vcp);		
				// cbMoon
				Point mnp = cbMoon.getLocation();
				mnp.x = bpp.x- cbMoon.getWidth() -10;
				cbMoon.setLocation(mnp);

			}
		});
	
	} // end initialization method
	
	
	// st half year image
	private void setHalfImage(int year, int index) {
		JLabel jl;
		if (index == 0) jl = lblImage_1;
		else jl = lblImage_2;
		if (Quarter.HalfImages [index].length() > 0) {
			jl.setIcon(new StretchIcon(Quarter.HalfImages[index]));
			jl.setText("");
			String s1 = new File(Quarter.HalfImages [index]).getName();
			pMnuDelImage.setText("Effacer "+s1);
			pMnuDelImage.setVisible(true); 
		}
		else {
			jl.setIcon(new StretchIcon("image.jpg"));
			pMnuDelImage.setVisible(false);
		}
	}
	
	
	// choose half year label image
	private void chooseHalfImage(int year, int index) {
		try {
			
			JFileChooser chooser = new JFileChooser();
			ImagePreviewPanel preview = new ImagePreviewPanel();
			chooser.setAccessory(preview);
			chooser.addPropertyChangeListener(preview);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			    "Fichiers images", "gif", "jpg", "jpeg", "png");
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				
			   // update list of images
			   String [] arr = new String [3];
			   arr[0]=Integer.toString(year);
			   arr[1]=Integer.toString(index);
			   arr[2]=chooser.getSelectedFile().getAbsolutePath();
			   boolean found = false;
			   for (int i=0; i<Quarter.imagesHalf.size(); i+=1) {
				   if (Quarter.imagesHalf.get(i)[0].equals(arr[0])) {
					   // Year found, check half
					   if (Quarter.imagesHalf.get(i)[1].equals(arr[1])) {
						   // half found replace line
						   Quarter.imagesHalf.set(i, arr) ;
						   found= true;
						   break;
					   }
				   }
			   }
			   // item not in list ,add it
			   if (!found) Quarter.imagesHalf.add(arr);
			   Quarter.HalfImages[index]= chooser.getSelectedFile().getAbsolutePath();
			   setHalfImage(year, index);
			}
		} catch (Exception e) {
			 JOptionPane.showMessageDialog(null, "Erreur de chargement de l'image");
		}
	}
	
	// remove half year image
	private void removeHalfImage (int year, int tab)
	{
		// are you sure ?
		String s1 = new File(Quarter.HalfImages [tab]).getName();
		int reply = JOptionPane.showConfirmDialog(null, "Voulez vous supprimer "+s1, "Calendrier", JOptionPane.YES_NO_OPTION);
        if (reply != JOptionPane.YES_OPTION) {
           return;
        }
        
		String syear = String.valueOf(year);  //Quarter.HalfImages[tab]; 
		String stab = String.valueOf(tab); 
		boolean found = false;
		   for (int i=0; i<Quarter.imagesHalf.size(); i+=1) {
			  
			   if (Quarter.imagesHalf.get(i)[0].equals(syear)) {
				   // Year found, check half
				   if (Quarter.imagesHalf.get(i)[1].equals(stab)) {
					   // half found replace line
					   Quarter.imagesHalf.remove(i) ;
					   Quarter.HalfImages[tab]="";
					   found= true;
					   break;
				   }
			   }
		   }
		if (found) setHalfImage(year, tab);
		
	}
	// Update today Label every sec
    private void startLabel2() {
        labelTimer = new javax.swing.Timer(1000, updateLabel2());
        labelTimer.start();
        labelTimer.setRepeats(true);
    }
    
    private Action updateLabel2() {
        return new AbstractAction("Label action") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                //increment second counter
            	icounter += 1;
            	DateTime dt = new DateTime();
               	String s = Astro.DateTimeToString(dt);
                lblToday_1.setText(s);
            	lblToday_2.setText(s);
            	// check if year has changed every minute
            	if (icounter > 59) {
            		icounter= 0;  // reset
            		int yr = dt.getYear();
            		int dy = dt.getDayOfYear();
            		if (yr != iYear) {
            			iYear= yr;
            			yearchanged(String.valueOf(yr));
            			YearField.setText(String.valueOf(yr));
            			setLabelDay(dt);
            		}
            		// day changed ?
            		if (dy != iDay){
            			iDay = dy;
            			setLabelDay(dt);
            		}
            	}
            }
        };
    } // end updatelabel

    // set day lower panel
	private void setLabelToday (DateTime dt) {
		lblToday_1.setText(Astro.DateTimeToString(dt));
		lblToday_2.setText(Astro.DateTimeToString(dt));
		String s = setLabelDay(dt);
		lblToday_1a.setText(s);
	    lblToday_2a.setText(s);
	}
	
	
	// set hover tooltip;
	private void setTooltipOver (Component cp, Point p) {
		JTable jt = (JTable) cp;
        int month = jt.columnAtPoint(p)+1+(Integer.parseInt(jt.getName())-1)*3;
        try {
			DateTime dt = new DateTime(Year, month, jt.rowAtPoint(p)+1, 12,0,0);
			String s = "<html>";
			s += Astro.DateToString(dt)+"<br>";
			s += setLabelDay(dt);
			jt.setToolTipText(s);
		} catch (Exception e) {
			// invalid day
			jt.setToolTipText("");
		}
  	}
	// set selected label
	private void setLabelSelected (Component cp) {
		JTable jt = (JTable) cp;
		int month = jt.getSelectedColumn()+1+(Integer.parseInt(jt.getName())-1)*3;	
		try {
			DateTime dt = new DateTime(Year, month, jt.getSelectedRow()+1, 12,0,0);
			String s = "<html>"; 
			s += Astro.DateToString(dt)+"<br>";
			s += setLabelDay(dt);
			lblSelected_1.setText(s);
			lblSelected_2.setText(s);
		} catch (Exception e1) {
			// Invalid cell
		}

	}
	
	private String setLabelDay(DateTime dt) {
		String s = "<html>"; 
	    // Sun hours
		DateTime sunrise = Astro.calcSunrise(dt, Config.Latitude, Config.Longitude, true);
	    sunrise = sunrise.plusMinutes(Astro.getTZOff(sunrise));
	    DateTime sunset = Astro.calcSunrise(dt, Config.Latitude, Config.Longitude, false);
	    sunset = sunset.plusMinutes(Astro.getTZOff(sunset));
	    DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
	    int day = dt.getDayOfYear();
	    // saint, day of year, week of year
	    s += day+"e jour, "+dt.getWeekOfWeekyear()+"e semaine<br>";
	    s += Quarter.YearDays.get(day-1).lsaint+"<br>";
	    // s = s+" - "+day+"e jour, "+dt.getWeekOfWeekyear()+"e semaine<br>";
	    // ferie
	    String sf = Quarter.YearDays.get(day-1).ferie;
	    if (sf.length() > 0) {
	    	if (sf.charAt(0) == '.') sf = sf.substring(1);
	    	s += "Férié: "+sf+"<br>"; 
	    }
	    // moon
	    String sm = Quarter.YearDays.get(day-1).typelune ;
	    if (sm.length() > 0) {
	    	if (sm.contentEquals("NL")) sm = "Nouvelle lune"; 
	    	else if (sm.contentEquals("PQ")) sm = "Premier quartier"; 
	    	else if (sm.contentEquals("DQ")) sm = "Dernier quartier";
	    	else if (sm.contentEquals("PL")) sm = "Pleine lune";
	    	else  sm = "";		
	        if (sm.length() > 0) s += sm+" : "+Quarter.YearDays.get(day-1).timelune.toString(fmt)+"<br>";	
	    }
	    // Seasons
	    String ss = Quarter.YearDays.get(day-1).season;
	    if (ss.length() > 0) {
	    	s += ss+" à "+Quarter.YearDays.get(day-1).seasondate.toString(fmt)+ "<br>";
	    }
	    // Scolar holidays
	    String sh =  Quarter.YearDays.get(day-1).zonevacscol;
	    switch (sh.length()){
	    	case 1 :	s += "Vacances scolaires: zone "+sh+"<br>"; 
	    				break;
	    	case 2 : 	s += "Vacances scolaires: zones "+sh.substring(0,1)+ "," + sh.substring(1,2)+"<br>";
	    				break;
	    	case 3 :	s += "Vacances scolaires: zones "+sh.substring(0,1)+ "," + sh.substring(1,2)+","+sh.substring(2,3)+"<br>";
	    				break;
	    }
	    s += "Lever et coucher du soleil : "+sunrise.toString(fmt)+" - "+sunset.toString(fmt);	
	    return s;
	}
	
    private void yearchanged(String syear) {
    	if (syear.length() == 4) {
			int newYear = Integer.parseInt(syear);
			if (newYear > 1582) {
				Year= newYear;
				Quarter.setYear(Year);
				frmCalendrier.setTitle("Calendrier - " + syear);
				int curpane = tabpane.getSelectedIndex(); 
				setHalfImage(Year, curpane);
				setSeasons(Year);
				Quarter.repaint();
			}
		}
    }
	
	private void yearbtnpressed(int btn) {
		int curindex = 0;
		int previndex = 1;
		if (btn > 0) {
			curindex = 1;
			previndex = 0;
		}
		;
		Init = true;
		if (tabpane.getSelectedIndex() == curindex) {
			Year = Year + btn;
			Quarter.setYear(Year);
			String syear = "";
			syear += Year;
			YearField.setText(syear);
			setSeasons(Year);
			frmCalendrier.setTitle("Calendrier - " + syear);
			frmCalendrier.repaint();
			tabpane.setSelectedIndex(previndex);
			
		} else {
			tabpane.setSelectedIndex(curindex);
		}
		int curpane = tabpane.getSelectedIndex();
		
		setHalfImage(Year, curpane);
		
		// Gregorian calendar only 
		if ((tabpane.getSelectedIndex() == 0) && (Year == 1583)) btnPrevious.setEnabled(false);
		else btnPrevious.setEnabled(true);
		Init = false;
	}

	
	// Set seasons panels
	public void setSeasons(int year) {
		// Set seasons pane
		if (!Beans.isDesignTime()) {
			//Astro = new astro();
			DateTime prin, ete, aut, hiv;
			prin = Astro.GetSaisonDate(Year, 0);
			prin = prin.plusMinutes(Astro.getTZOff(prin));
			ete = Astro.GetSaisonDate(Year, 1);
			ete = ete.plusMinutes(Astro.getTZOff(ete));
			aut = Astro.GetSaisonDate(Year, 2);
			aut = aut.plusMinutes(Astro.getTZOff(aut));
			hiv = Astro.GetSaisonDate(Year, 3);
			hiv = hiv.plusMinutes(Astro.getTZOff(hiv));
			String s = "<html>Printemps: " +Astro.DateTimeToString(prin, "HH:mm")+"<br>"+
						"Eté: "+Astro.DateTimeToString(ete, "HH:mm")+"</html>";
			lblSeasons_1a.setText(s);
			lblSeasons_2a.setText(s);
			s = "<html>Automne: " +Astro.DateTimeToString(aut, "HH:mm")+"<br>"+
					"Hiver: "+Astro.DateTimeToString(hiv, "HH:mm")+"</html>";
			lblSeasons_1b.setText(s);
			lblSeasons_2b.setText(s);
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