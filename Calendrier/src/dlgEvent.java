import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.toedter.calendar.JDateChooser;
import javax.swing.SpinnerModel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;


public class dlgEvent extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean recall;
	private MutableDateTime Curdate= new MutableDateTime();
	private final JPanel contentPanel = new JPanel();
	private JDateChooser dtEvChooser;
	private JSpinner tiEvBegin;
	private ChangeListener cleb;
	private SpinnerDateModel sdm ;
	private JButton okButton ;
	private JComboBox<String> cbDuration;  
	private ActionListener cbdal ;
	private JSpinner tiEvEnd;
	private ChangeListener clee;
	private DateTime begDate;
	private DateTime endDate;
	private DateTime recDate;
	private JComboBox <String> cbRecall;
	private ActionListener cbral ;
	private PropertyChangeListener pcldc;
	private JSpinner tiEvRec;
	private JTextField tfReference;
	private JTextField tfLocation;
	private JComboBox<String> cbType;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			dlgEvent dialog = new dlgEvent();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDate(DateTime dat) {
		// initialize to the selected day at 12h00
		Curdate= new MutableDateTime(dat);
		Curdate.setSecondOfMinute(0);
		Curdate.setMillisOfSecond(0);
		dtEvChooser.setDate(Curdate.toDate());
		begDate= new DateTime(Curdate);
		tiEvBegin.setValue(begDate.toDate());
		endDate= new DateTime(Curdate).plusMinutes(30);
		tiEvEnd.setValue(endDate.toDate());
		tiEvRec.setValue(begDate.toDate());
	}
	
	/**
	 * Create the dialog.
	 */
	public dlgEvent() {
		setTitle("Nouvel \u00E9v\u00E9nement");
		setBounds(100, 100, 594, 301);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new CompoundBorder());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		// Date and time picker
		JLabel lblEvDate = new JLabel("Date :");
		lblEvDate.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEvDate.setBounds(230, 17, 46, 14);
		contentPanel.add(lblEvDate);
		
		dtEvChooser = new JDateChooser();
		
		
		dtEvChooser.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dtEvChooser.setDateFormatString("dd/MM/yyyy");
		dtEvChooser.setBounds(295, 14, 120, 20);
		contentPanel.add(dtEvChooser);
		dtEvChooser.setDate(Curdate.toDate());		
		
		//cbDuration strings
		final String [] cbdarr = {"30 minutes", "1 heure", "1 h 30", "2 heures", "4 heures", "8 heures", "12 heures", "Choisir heure de fin"};
		final int [][] cbdcalend = {{0,1,1,2,4,8,12,0}, {30,0,30,0,0,0,0,0}}; 
		
		//cbRecall strings
		final String [] cbrarr = {"Aucun", "Heure de début", "5 minutes avant", "10 minutes avant", "15 minutes avant", "30 minutes avant", "1 heure avant", "Autre"};
		final int [][] cbrcalend = {{0,0,0,0,0,0,1,0}, {0,0,5,10,15,30,0,0}};
		
		//cbType strings
		
		final String [] cbtarr = {"Evenement", "Fête", "Anniversaire"};		
				
		
		
		sdm = new SpinnerDateModel();
		tiEvBegin = new JSpinner(sdm);
		

		

		JSpinner.DateEditor de_tiEvBegin = new JSpinner.DateEditor(tiEvBegin, "HH:mm");
		tiEvBegin.setSize(new Dimension(0, 22));
		tiEvBegin.setPreferredSize(new Dimension(98, 24));
		tiEvBegin.setMinimumSize(new Dimension(98, 22));
		tiEvBegin.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tiEvBegin.setBounds(474, 14, 56, 20);
		tiEvBegin.setEditor(de_tiEvBegin);
		
		// change begin time
		cleb = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSpinner js = (JSpinner) e.getSource();
				
				// date in chooser
				MutableDateTime tmpdat= new MutableDateTime(dtEvChooser.getDate());
				// hour in spinner
				MutableDateTime tmptim = new MutableDateTime((Date) js.getModel().getValue());
				tmpdat.setHourOfDay(tmptim.getHourOfDay());
				tmpdat.setMinuteOfHour(tmptim.getMinuteOfHour());
				tmpdat.setSecondOfMinute(0);
				tmpdat.setMillisOfSecond(0);
				// new begin date
				begDate= new DateTime(tmpdat);
				// get duration in combo to change end time 
				int i = cbDuration.getSelectedIndex();
				if (i!=cbdarr.length-1) {
					endDate= new DateTime(begDate);
					endDate= endDate.plusHours(cbdcalend[0][i]);
					endDate= endDate.plusMinutes(cbdcalend[1][i]);
					tiEvEnd.removeChangeListener(clee);
					tiEvEnd.setValue(endDate.toDate());
					tiEvEnd.addChangeListener(clee);
				}
				else {
					// use preset end time
					tmptim= new MutableDateTime((Date) tiEvEnd.getModel().getValue()); 
					tmpdat.setHourOfDay(tmptim.getHourOfDay());
					tmpdat.setMinuteOfHour(tmptim.getMinuteOfHour());
					endDate= new DateTime(tmpdat);
				}
				// get recall data to change recall time
				// todo
			}
		};
		
		tiEvBegin.addChangeListener(cleb);
		
		
		
		contentPanel.add(tiEvBegin);
		// change date choosen
		pcldc = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				 if ("date".equals(e.getPropertyName())) {
					 MutableDateTime tmpdat= new MutableDateTime((Date) e.getNewValue());
					// hour in spinner
					MutableDateTime tmptim = new MutableDateTime((Date) tiEvBegin.getModel().getValue());
					tmpdat.setHourOfDay(tmptim.getHourOfDay());
					tmpdat.setMinuteOfHour(tmptim.getMinuteOfHour());
					tmpdat.setSecondOfMinute(0);
					tmpdat.setMillisOfSecond(0);
					// new begin date
					begDate= new DateTime(tmpdat);
					// get duration in combo
					int i = cbDuration.getSelectedIndex();
					if (i!=cbdarr.length-1) {
						endDate= new DateTime(begDate);
						endDate= endDate.plusHours(cbdcalend[0][i]);
						endDate= endDate.plusMinutes(cbdcalend[1][i]);
						tiEvEnd.removeChangeListener(clee);
						tiEvEnd.setValue(endDate.toDate());
						tiEvEnd.addChangeListener(clee);
					}
					else {
						// use preset end time
						tmptim= new MutableDateTime((Date) tiEvEnd.getModel().getValue()); 
						tmpdat.setHourOfDay(tmptim.getHourOfDay());
						tmpdat.setMinuteOfHour(tmptim.getMinuteOfHour());
						endDate= new DateTime(tmpdat);
					}
		         }
			}
		};
		dtEvChooser.addPropertyChangeListener(pcldc);
		

		// list of days'sevents 
		
		JPanel evLstPanel = new JPanel();
		evLstPanel.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		evLstPanel.setBounds(10, 14, 206, 164);
		contentPanel.add(evLstPanel);
		evLstPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane evLstScrl = new JScrollPane();
		evLstScrl.setBorder(null);
		evLstPanel.add(evLstScrl);
		
		JList <String> evList = new JList<String>();
		evLstScrl.setViewportView(evList);
		evList.setBounds(248, 92, 153, 148);
		
		evList.setFont(new Font("Tahoma", Font.PLAIN, 11));
		evList.setVisibleRowCount(5);
		String [] lstarr = {"Evenement 1","Evenement 2","Evenement 3"};
		evList.setListData(lstarr);
		
		JLabel lblEvDur = new JLabel("Dur\u00E9e :");
		lblEvDur.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEvDur.setBounds(230, 47, 46, 14);
		contentPanel.add(lblEvDur);
		
		// Event duration combo box
		cbDuration = new JComboBox <String> ();
		// Populate with array 
		for(String str : cbdarr)  cbDuration.addItem(str);
		cbDuration.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cbDuration.setBounds(295, 44, 120, 20);
		contentPanel.add(cbDuration);
		

	    SpinnerDateModel sdm1 = new SpinnerDateModel();
		sdm1.setCalendarField(0);
		tiEvEnd = new JSpinner(sdm1);
		
		clee = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSpinner js = (JSpinner) e.getSource();
				// date in chooser
				MutableDateTime tmpdat= new MutableDateTime(dtEvChooser.getDate());
				// hour in spinner
				MutableDateTime tmptim = new MutableDateTime((Date) js.getModel().getValue());
				tmpdat.setHourOfDay(tmptim.getHourOfDay());
				tmpdat.setMinuteOfHour(tmptim.getMinuteOfHour());
				tmpdat.setSecondOfMinute(0);
				tmpdat.setMillisOfSecond(0);
				endDate= new DateTime(tmpdat);
			}
		};
		tiEvEnd.addChangeListener(clee);
		tiEvEnd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tiEvEnd.setEnabled(false);
		JSpinner.DateEditor de_tiEvEnd = new JSpinner.DateEditor(tiEvEnd, "HH:mm");
		tiEvEnd.setBounds(474, 43, 56, 20);
		tiEvEnd.setEditor(de_tiEvEnd);
		
		contentPanel.add(tiEvEnd);
		JLabel lblEvBeg = new JLabel("D\u00E9but :");
		lblEvBeg.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEvBeg.setBounds(424, 17, 36, 14);
		contentPanel.add(lblEvBeg);
		
		JLabel lblEvEnd = new JLabel("Fin :");
		lblEvEnd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEvEnd.setBounds(424, 46, 46, 14);
		contentPanel.add(lblEvEnd);
		
	
		// Recall combo box
		cbRecall = new JComboBox <String> ();
		// Populate with array 
		for(String str : cbrarr)  cbRecall.addItem(str);
		cbRecall.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cbRecall.setBounds(295, 74, 120, 20);
		contentPanel.add(cbRecall);
		
		JLabel lbRecall = new JLabel("Rappel :");
		lbRecall.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbRecall.setBounds(230, 77, 46, 14);
		contentPanel.add(lbRecall);
		
		// Recall hour spinner
		SpinnerDateModel sdm2 = new SpinnerDateModel();
		sdm2.setCalendarField(0);
		tiEvRec = new JSpinner(sdm2);
		tiEvRec.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tiEvRec.setEnabled(false);
		JSpinner.DateEditor de_tiEvRec = new JSpinner.DateEditor(tiEvRec, "HH:mm");
		tiEvRec.setEnabled(false);
		tiEvRec.setBounds(474, 74, 56, 20);
		tiEvRec.setEditor(de_tiEvRec);
		contentPanel.add(tiEvRec);
		
		JLabel lblHrRec = new JLabel("Heure :");
		lblHrRec.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblHrRec.setBounds(424, 77, 46, 14);
		contentPanel.add(lblHrRec);
		
		JLabel lblRef = new JLabel("R\u00E9f\u00E9rence :");
		lblRef.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblRef.setBounds(230, 107, 65, 14);
		contentPanel.add(lblRef);
		
		tfReference = new JTextField();
		tfReference.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		tfReference.setBounds(295, 104, 273, 20);
		contentPanel.add(tfReference);
		tfReference.setColumns(10);
		
		JLabel lblLoc = new JLabel("Lieu :");
		lblLoc.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblLoc.setBounds(230, 137, 65, 14);
		contentPanel.add(lblLoc);
		
		tfLocation = new JTextField();
		tfLocation.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		tfLocation.setColumns(10);
		tfLocation.setBounds(295, 134, 273, 20);
		contentPanel.add(tfLocation);
		
		JTextArea taComment = new JTextArea();
		taComment.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		taComment.setBounds(295, 164, 273, 52);
		contentPanel.add(taComment);
		
		JLabel lblComment = new JLabel("Lieu :");
		lblComment.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblComment.setBounds(230, 164, 65, 14);
		contentPanel.add(lblComment);
		
		JLabel lblNewLabel = new JLabel("Type : ");
		lblNewLabel.setBounds(10, 198, 46, 14);
		contentPanel.add(lblNewLabel);
		
		cbType = new JComboBox<String>();
		for(String str : cbtarr)  cbType.addItem(str);
		cbType.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cbType.setBounds(66, 196, 150, 20);
		contentPanel.add(cbType);
		
		
			
		// Action on change in duration combo box
		cbdal = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = cbDuration.getSelectedIndex();
				tiEvEnd.setEnabled(false);
				if (i==cbdarr.length-1) tiEvEnd.setEnabled(true);
				else {
					endDate= new DateTime(begDate);
					endDate= endDate.plusHours(cbdcalend[0][i]);
					endDate= endDate.plusMinutes(cbdcalend[1][i]);
					tiEvEnd.setValue(endDate.toDate());
				};
			}
		};
		cbDuration.addActionListener(cbdal);
		
		// Action on change in recall combo box 
		cbral = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = cbRecall.getSelectedIndex();
				tiEvRec.setEnabled(false);
				if (i==cbdarr.length-1) tiEvRec.setEnabled(true);
				else if (i==0)  recall= false;
				else {
					recall= true;
					recDate= new DateTime(begDate);
					recDate= recDate.minusHours(cbrcalend[0][i]);
					recDate= recDate.minusMinutes(cbrcalend[1][i]);
					tiEvRec.setValue(recDate.toDate());
				}
			}
		};
		cbRecall.addActionListener(cbral);
		
		// Buttons processing
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			{
				okButton = new JButton("OK");
				okButton.setBounds(new Rectangle(0, 0, 65, 23));
				okButton.setFont(new Font("Tahoma", Font.BOLD, 11));
				okButton.setPreferredSize(new Dimension(65, 23));
				okButton.setMinimumSize(new Dimension(65, 23));
				okButton.setMaximumSize(new Dimension(65, 23));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					   //Todo	
						setVisible(false);	
						//System.out.println(begDate);
						//System.out.println(endDate);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			
			JButton cancelButton = new JButton("Annuler");
			cancelButton.setPreferredSize(new Dimension(65, 23));
			cancelButton.setMinimumSize(new Dimension(65, 23));
			cancelButton.setMaximumSize(new Dimension(65, 23));
			cancelButton.setMargin(new Insets(2, 5, 2, 5));
			cancelButton.setFont(new Font("Tahoma", Font.BOLD, 11));
			buttonPane.add(cancelButton);
			// Cancel button event processing
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});		
		}
	}
}
