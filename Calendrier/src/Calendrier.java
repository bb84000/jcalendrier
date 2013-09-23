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
import java.beans.Beans;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.awt.Insets;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

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

	// Config variables are in dlgConfig class
	private dlgConfig Config = new dlgConfig();

	private JLabel lblNewLabel_1;
	private JPopupMenu pMnuGen;
	private JMenuItem pMnuConfig;

	private JTextField YearField;

	private DayCalRenderer Quarter = new DayCalRenderer();
	private JCheckBox cbMoon = new JCheckBox("Phases de la lune");
	private JCheckBox cbVacA = new JCheckBox("Vacances Zone A");
	private JCheckBox cbVacB = new JCheckBox("Vacances Zone B");
	private JCheckBox cbVacC = new JCheckBox("Vacances Zone C");
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
	private JLabel labelToday_1;
	private JLabel labelSelected_1;
	private JLabel lblSelected_2;
	private JLabel lblSeasons_2a;
	private JLabel lblSeasons_1a;
	// some vars
	private astro Astro;
	private JLabel lblSeasons_2b;
	private JLabel lblSeasons_1b;
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

	private void applyConfig() {

		if (Config.savePos) {
			frmCalendrier.setSize(Config.sizeW, Config.sizeH);
			frmCalendrier.setLocation(Config.locatX, Config.locatY);
		} else
			frmCalendrier.setLocationRelativeTo(null);
		Quarter.colA = Config.colvacA;
		checkedA.colFillu = Config.colvacA;
		Quarter.colB = Config.colvacB;
		checkedB.colFillu = Config.colvacB;
		Quarter.colC = Config.colvacC;
		checkedC.colFillu = Config.colvacC;
		if (Config.saveMoon) {
			cbMoon.setSelected(Config.dispMoon);
			Quarter.ShowMoon = cbMoon.isSelected();
		}
		if (Config.saveVacScol) {
			cbVacA.setSelected(Config.dispVacA);
			Quarter.ShowVacA = cbVacA.isSelected();
			cbVacB.setSelected(Config.dispVacB);
			Quarter.ShowVacB = cbVacB.isSelected();
			cbVacC.setSelected(Config.dispVacC);
			Quarter.ShowVacC = cbVacC.isSelected();
		}

	}

	// Some initialization routines
	private void initialize() {
		Init = true;
		// Set config file name and load config if exists
		// if not exists ask user to choose standard or portable
		if (!Beans.isDesignTime()) {

			Config.set_config_file("config.json");
			Config.loadConfig();
		}
		DateTime dt = new DateTime();
		Year = dt.getYear();
		// Icone de l'application
		Image MainIcon = Toolkit.getDefaultToolkit().getImage(
				Calendrier.class.getResource("/resources/calendrier.png"));

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
		frmCalendrier.getContentPane().setMaximumSize(
				new Dimension(1182, 2147483647));
		frmCalendrier.setIconImage(MainIcon);
		frmCalendrier.setBounds(100, 100, 450, 300);
		frmCalendrier.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCalendrier.setSize(1180, 719);

		// Apply configuration parameters
		applyConfig();
		String syear = "";
		syear += Year;
		frmCalendrier.setTitle("Calendrier - " + syear);
		Quarter.setYear(Year);
		
		// bottom panel for buttons
		pane_bottom = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pane_bottom.getLayout();
		flowLayout.setHgap(15);
		pane_bottom.setMaximumSize(new Dimension(1182, 32767));
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
		tabpane.addTab("1er semestre", null, pane_h1, null);
		GridBagLayout gbl_pane_h1 = new GridBagLayout();
		gbl_pane_h1.columnWidths = new int[] { 307, 266, 266, 307, 0 };
		gbl_pane_h1.rowHeights = new int[] { 400, 140, 34, 0 };
		gbl_pane_h1.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pane_h1.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pane_h1.setLayout(gbl_pane_h1);

		// 1st quarter table
		pane_q1 = new JPanel();
		pane_q1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q1.setPreferredSize(new Dimension(307, 613));
		pane_q1.setMinimumSize(new Dimension(307, 613));
		pane_q1.setMaximumSize(new Dimension(307, 613));
		GridBagConstraints gbc_pane_q1 = new GridBagConstraints();
		gbc_pane_q1.insets = new Insets(0, 0, 0, 0);
		gbc_pane_q1.gridheight = 3;
		gbc_pane_q1.anchor = GridBagConstraints.NORTHWEST;
		gbc_pane_q1.gridx = 0;
		gbc_pane_q1.gridy = 0;
		pane_h1.add(pane_q1, gbc_pane_q1);

		// 1st quarter table
		// Disable cell edition to have a read only table
		table_q1 = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

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

		// Add image
		// Todo user selected images
		lblImage_1 = new JLabel("");
		lblImage_1.setMaximumSize(new Dimension(530, 400));
		lblImage_1.setMinimumSize(new Dimension(530, 400));
		lblImage_1.setSize(new Dimension(530, 400));
		lblImage_1.setPreferredSize(new Dimension(530, 400));
		lblImage_1.setIcon(new StretchIcon("image.jpg"));
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
		pane_q2.setMinimumSize(new Dimension(307, 613));
		pane_q2.setMaximumSize(new Dimension(307, 613));
		pane_q2.setPreferredSize(new Dimension(307, 613));
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

		table_q2.setMinimumSize(new Dimension(307, 612));
		table_q2.setMaximumSize(new Dimension(307, 612));
		table_q2.setPreferredSize(new Dimension(307, 612));
		table_q2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		table_q2.setName("2");
		table_q2.setDefaultRenderer(Object.class, Quarter);
		table_q2.setRowHeight(19);
		table_q2.setRowMargin(1);
		table_q2.setPreferredScrollableViewportSize(new Dimension(307, 612));
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
		panelToday_1.setPreferredSize(new Dimension(10, 140));
		panelToday_1.setSize(new Dimension(0, 120));
		panelToday_1.setMinimumSize(new Dimension(10, 120));
		panelToday_1.setMaximumSize(new Dimension(32767, 120));
		GridBagConstraints gbc_panelToday_1 = new GridBagConstraints();
		gbc_panelToday_1.insets = new Insets(0, 0, 0, 0);
		gbc_panelToday_1.anchor = GridBagConstraints.NORTH;
		gbc_panelToday_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelToday_1.gridx = 1;
		gbc_panelToday_1.gridy = 1;
		pane_h1.add(panelToday_1, gbc_panelToday_1);
		
		// Label today
		labelToday_1 = new JLabel("Today");
		labelToday_1.setVerticalTextPosition(SwingConstants.TOP);
		labelToday_1.setVerticalAlignment(SwingConstants.TOP);
		labelToday_1.setPreferredSize(new Dimension(240, 110));
		labelToday_1.setHorizontalTextPosition(SwingConstants.LEFT);
		labelToday_1.setHorizontalAlignment(SwingConstants.LEFT);
		labelToday_1.setAlignmentY(0.0f);
		panelToday_1.add(labelToday_1);

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
		GridBagConstraints gbc_panelSelected_1 = new GridBagConstraints();
		gbc_panelSelected_1.insets = new Insets(0, 0, 0, 0);
		gbc_panelSelected_1.fill = GridBagConstraints.BOTH;
		gbc_panelSelected_1.gridx = 2;
		gbc_panelSelected_1.gridy = 1;
		pane_h1.add(panelSelected_1, gbc_panelSelected_1);
		
		// label selected
		labelSelected_1 = new JLabel("Selected");
		labelSelected_1.setVerticalTextPosition(SwingConstants.TOP);
		labelSelected_1.setVerticalAlignment(SwingConstants.TOP);
		labelSelected_1.setPreferredSize(new Dimension(240, 110));
		labelSelected_1.setHorizontalTextPosition(SwingConstants.LEFT);
		labelSelected_1.setHorizontalAlignment(SwingConstants.LEFT);
		labelSelected_1.setAlignmentY(0.0f);
		panelSelected_1.add(labelSelected_1);

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
		lblSeasons_1a.setMinimumSize(new Dimension(255, 30));
		lblSeasons_1a.setSize(new Dimension(255, 30));
		lblSeasons_1a.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSeasons_1a.setVerticalTextPosition(SwingConstants.TOP);
		lblSeasons_1a.setVerticalAlignment(SwingConstants.TOP);
		lblSeasons_1a.setPreferredSize(new Dimension(255, 35));
		GridBagConstraints gbc_lblSeasons_1a = new GridBagConstraints();
		gbc_lblSeasons_1a.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSeasons_1a.insets = new Insets(5, 8, 0, 5);
		gbc_lblSeasons_1a.gridx = 0;
		gbc_lblSeasons_1a.gridy = 0;
		panelSeasons_1.add(lblSeasons_1a, gbc_lblSeasons_1a);

		// label seasons right
		lblSeasons_1b = new JLabel("Seasons");
		lblSeasons_1b.setSize(new Dimension(255, 30));
		lblSeasons_1b.setPreferredSize(new Dimension(255, 35));
		lblSeasons_1b.setMinimumSize(new Dimension(255, 30));
		lblSeasons_1b.setMaximumSize(new Dimension(40, 30));
		lblSeasons_1b.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSeasons_1b.setVerticalTextPosition(SwingConstants.TOP);
		lblSeasons_1b.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_lblSeasons_1b = new GridBagConstraints();
		gbc_lblSeasons_1b.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSeasons_1b.gridx = 1;
		gbc_lblSeasons_1b.gridy = 0;
		panelSeasons_1.add(lblSeasons_1b, gbc_lblSeasons_1b);

		// 2nd half panel
		pane_h2 = new JPanel();
		tabpane.addTab("2\u00E8me semestre", null, pane_h2, null);
		GridBagLayout gbl_pane_h2 = new GridBagLayout();
		gbl_pane_h2.columnWidths = new int[] { 307, 266, 266, 307, 0 };
		gbl_pane_h2.rowHeights = new int[] { 400, 140, 34, 0 };
		gbl_pane_h2.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pane_h2.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		pane_h2.setLayout(gbl_pane_h2);

		pane_q3 = new JPanel();
		pane_q3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q3.setPreferredSize(new Dimension(307, 613));
		pane_q3.setMinimumSize(new Dimension(307, 613));
		pane_q3.setMaximumSize(new Dimension(307, 613));
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
		lblImage_2.setMaximumSize(new Dimension(530, 400));
		lblImage_2.setMinimumSize(new Dimension(530, 400));
		lblImage_2.setSize(new Dimension(530, 400));
		lblImage_2.setPreferredSize(new Dimension(530, 400));
		lblImage_2.setIcon(new StretchIcon("image.jpg"));
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
		pane_q4.setPreferredSize(new Dimension(307, 613));
		pane_q4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pane_q4.setMinimumSize(new Dimension(307, 613));
		pane_q4.setMaximumSize(new Dimension(307, 613));
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
		panelToday_2.setPreferredSize(new Dimension(10, 140));
		panelToday_2.setBorder(new TitledBorder(null, "Aujourd'hui",
				TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelToday_2.setMinimumSize(new Dimension(10, 120));
		GridBagConstraints gbc_panelToday_2 = new GridBagConstraints();
		gbc_panelToday_2.insets = new Insets(0, 0, 0, 0);
		gbc_panelToday_2.anchor = GridBagConstraints.NORTH;
		gbc_panelToday_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelToday_2.gridx = 1;
		gbc_panelToday_2.gridy = 1;
		pane_h2.add(panelToday_2, gbc_panelToday_2);
		lblToday_2 = new JLabel("Today");
		lblToday_2.setPreferredSize(new Dimension(240, 110));
		lblToday_2.setAlignmentY(Component.TOP_ALIGNMENT);
		lblToday_2.setVerticalTextPosition(SwingConstants.TOP);
		lblToday_2.setVerticalAlignment(SwingConstants.TOP);
		lblToday_2.setHorizontalTextPosition(SwingConstants.LEFT);
		lblToday_2.setHorizontalAlignment(SwingConstants.LEFT);
		panelToday_2.add(lblToday_2);

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
		GridBagConstraints gbc_panelSelected_2 = new GridBagConstraints();
		gbc_panelSelected_2.insets = new Insets(0, 0, 0, 0);
		gbc_panelSelected_2.fill = GridBagConstraints.BOTH;
		gbc_panelSelected_2.gridx = 2;
		gbc_panelSelected_2.gridy = 1;
		pane_h2.add(panelSelected_2, gbc_panelSelected_2);
		lblSelected_2 = new JLabel("Selected");
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
		lblSeasons_2a = new JLabel("Seasons");
		lblSeasons_2a.setMaximumSize(new Dimension(40, 30));
		lblSeasons_2a.setMinimumSize(new Dimension(255, 30));
		lblSeasons_2a.setSize(new Dimension(255, 30));
		lblSeasons_2a.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSeasons_2a.setVerticalTextPosition(SwingConstants.TOP);
		lblSeasons_2a.setVerticalAlignment(SwingConstants.TOP);
		lblSeasons_2a.setPreferredSize(new Dimension(255, 35));
		GridBagConstraints gbc_lblSeasons_2a = new GridBagConstraints();
		gbc_lblSeasons_2a.insets = new Insets(5, 8, 0, 5);
		gbc_lblSeasons_2a.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSeasons_2a.gridx = 0;
		gbc_lblSeasons_2a.gridy = 0;
		panelSeasons_2.add(lblSeasons_2a, gbc_lblSeasons_2a);
		lblSeasons_2b = new JLabel("Seasons");
		lblSeasons_2b.setVerticalTextPosition(SwingConstants.TOP);
		lblSeasons_2b.setVerticalAlignment(SwingConstants.TOP);
		lblSeasons_2b.setSize(new Dimension(255, 30));
		lblSeasons_2b.setPreferredSize(new Dimension(255, 35));
		lblSeasons_2b.setMinimumSize(new Dimension(255, 30));
		lblSeasons_2b.setMaximumSize(new Dimension(40, 30));
		lblSeasons_2b.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_lblSeasons_2b = new GridBagConstraints();
		gbc_lblSeasons_2b.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSeasons_2b.gridx = 1;
		gbc_lblSeasons_2b.gridy = 0;
		panelSeasons_2.add(lblSeasons_2b, gbc_lblSeasons_2b);

		// Moon phases checkboc
		cbMoon.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbMoon.setSize(new Dimension(0, 24));
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
		pane_bottom.add(cbMoon);

		//
		lblNewLabel_1 = new JLabel(
				"                                                       ");
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
				if (!Init) {

					String syear = YearField.getText();
					if (syear.length() > 0) {
						Year = Integer.parseInt(syear);
						Quarter.setYear(Year);
						frmCalendrier.setTitle("Calendrier - " + syear);
						setSeasons(Year);
						Quarter.repaint();
					}
				}
			}
		});

		// Previous half button
		Image arrowl = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("resources/arrowl.png"));
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
		if (!Beans.isDesignTime()) {

			YearField.setText(syear);
		}
		// Bouton Semestre suivant
		Image arrowr = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("resources/arrowr.png"));
		JButton btnNext = new JButton("");
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
		// System.out.print(Quarter.M);
		Init = false;
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
			Init = false;
		} else {
			tabpane.setSelectedIndex(curindex);
		}
	}

	// Set seasons
	public void setSeasons(int year) {
		// Set seasons pane
		if (!Beans.isDesignTime()) {
			Astro = new astro();

			String[] Fdays = { "Dimanche", "Lundi", "Mardi", "Mercredi",
					"Jeudi", "Vendredi", "Samedi", "Dimanche" };
			String[] Fmonths = { "décembre", "janvier", "février", "mars",
					"avril", "mai", "juin", "juillet", "août", "septembre",
					"octobre", "novembre", "décembre" };

			DateTime prin, ete, aut, hiv;
			prin = Astro.GetSaisonDate(Year, 0);
			prin = prin.plusHours(1);
			ete = Astro.GetSaisonDate(Year, 1);
			ete = ete.plusHours(2);
			aut = Astro.GetSaisonDate(Year, 2);
			aut = aut.plusHours(2);
			hiv = Astro.GetSaisonDate(Year, 3);
			hiv = hiv.plusHours(1);

			if (!Beans.isDesignTime()) {
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
				String s = "<html>Printemps: " + Fdays[prin.getDayOfWeek()]
						+ " " + prin.getDayOfMonth() + " "
						+ Fmonths[prin.getMonthOfYear()] + " " + Year + " "
						+ prin.toString(fmt) + "<br>" + "Eté: "
						+ Fdays[ete.getDayOfWeek()] + " " + ete.getDayOfMonth()
						+ " " + Fmonths[ete.getMonthOfYear()] + " " + Year
						+ " " + ete.toString(fmt) + "</html>";
				lblSeasons_1a.setText(s);
				lblSeasons_2a.setText(s);
				s = "<html>Automne: " + Fdays[aut.getDayOfWeek()] + " "
						+ aut.getDayOfMonth() + " "
						+ Fmonths[aut.getMonthOfYear()] + " " + Year + " "
						+ aut.toString(fmt) + "<br>" + "Hiver: "
						+ Fdays[hiv.getDayOfWeek()] + " " + hiv.getDayOfMonth()
						+ " " + Fmonths[hiv.getMonthOfYear()] + " " + Year
						+ " " + hiv.toString(fmt) + "</html>";
				lblSeasons_1b.setText(s);
				lblSeasons_2b.setText(s);

			}
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