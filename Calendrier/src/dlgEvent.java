/*
 * dlgEvent
 * Dialog to create, modify or delete user events
 * bb - november 2013
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.toedter.calendar.JDateChooser;

import static bb.constants.constants.*;

public class dlgEvent extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Event structure : index,begin date,end date,recall,recall alert,type,reference,location,comment  
	public class Event {
		public int index;
		public DateTime begDate ;
		public DateTime endDate;
		public DateTime recDate;
		public boolean recall;
		public String type;
		public String reference;
		public String location;
		public String comment;
		public Event (int eindex, DateTime ebegdate, DateTime eenddate, DateTime erecdate, boolean erecall,
				      String etype, String ereference, String elocation, String ecomment) {
			index= eindex;
			begDate= ebegdate;
			endDate= eenddate;
			recDate= erecdate;
			recall= erecall;
			type= etype;
			reference= ereference;
			location= elocation;
			comment= ecomment;
		}
		
		// create event from array
		public void fromArray (String [] arr) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY/MM/dd-HH:mm");
			try {
				index = Integer.parseInt(arr[0]);
			} catch (NumberFormatException e) {
				index= 0;
			}
			try {
				begDate=  formatter.parseDateTime(arr[1]);
			} catch (Exception e) {
				begDate=  formatter.parseDateTime("2000/01/01-00:00");
			}
			try {
				endDate=  formatter.parseDateTime(arr[2]);
			} catch (Exception e) {
				endDate=  formatter.parseDateTime("2000/01/01-00:00");
			}
			try {
				recDate=  formatter.parseDateTime(arr[3]);
			} catch (Exception e) {
				recDate=  formatter.parseDateTime("2000/01/01-00:00");
			}
			try {
				recall = (arr[4].equals("true"));
			} catch (Exception e) {
				recall = false;
			}
			type= arr[5];
			reference= arr[6];
			location= arr[7];
			comment= arr[8];
		}
		
		// populate an array with event parameters
		public String [] toArray () {
			String [] arr = new String [9];
			try {
				arr [0] = String.valueOf(index);
			} catch (Exception e) {
				arr [0] = "0";
			} 
			try {
				arr [1] = begDate.toString("YYYY/MM/dd-HH:mm");
			} catch (Exception e) {
				arr [1] = "2000/01/01-00:00";
			}
			try {
				arr [2] = endDate.toString("YYYY/MM/dd-HH:mm");
			} catch (Exception e) {
				arr [2] = "2000/01/01-00:00";
			}
			try {
				arr [3] = recDate.toString("YYYY/MM/dd-HH:mm");
			} catch (Exception e) {
				arr [3] = "2000/01/01-00:00";
			}
			if (recall) arr [4]= "true";
			else arr [4]= "false";
			arr [5] = type;
			arr [6]= reference;
			arr [7]= location;
			arr [8]= comment;
			return arr;
		}
	}

	public ArrayList <String[]> arrEvents= new ArrayList <String[]> ();
	public Event event = new Event(-2, null, null, null, false, "E", "Nouvel évenement", "", "");
	public boolean changed;
	//public boolean mrOK= false;
	public int [] deleted = new int [50];
	public int nextEventIndex;
	private final JPanel contentPanel = new JPanel();
	private JDateChooser dtEvChooser;
	private JSpinner tiEvBegin;
	private ChangeListener cleb;
	private SpinnerDateModel sdm ;

	//cbDuration strings
	private String [] cbdarr = {"30 minutes", "1 heure", "1 h 30", "2 heures", "4 heures", "8 heures", "12 heures", "Choisir heure de fin"};
	private int [] cbdmins = {30,60,90,120,240,480,720,0}; 
	private JComboBox<String> cbDuration;  
	private ActionListener cbdal ;
	private SpinnerDateModel sdm1; 
	private JSpinner tiEvEnd;
	private ChangeListener clee;
	//cbRecall strings
	private String [] cbrarr = {"Aucun", "Heure de début", "5 minutes avant", "10 minutes avant", "15 minutes avant", "30 minutes avant", "1 heure avant", "Autre"};
	private int [] cbrmins =  {0,0,5,10,15,30,60,0};
	private JComboBox <String> cbRecall;
	// Event type string
	private String [] cbtarr = {"Evenement", "Fête", "Anniversaire"};	
	private char [] cbtchar = {'E', 'F', 'A'};
	private JComboBox<String> cbType;	
	private ActionListener cbral ;
	private PropertyChangeListener pcldc;
	private JSpinner tiEvRec;
	private JTextField tfReference;
	private JTextField tfLocation;
	private JTextArea taComment;

	private JButton delButton;
	private JButton okButton;
	private JButton cancelButton;
	private ActionListener albtn;
	private JList <String> evList;
	private DefaultListModel<String> lm;
	private ListSelectionListener llev; 
	private ChangeListener cler;
	private DocumentListener dl;
	private int modalresult;
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

	public int open() {
		setVisible (true);
		return modalresult;
	}
	// set date
	public void setDate(DateTime dat) {
		// initialize to the selected day at 12h00
		MutableDateTime Curdate= new MutableDateTime(dat);
		Curdate.setSecondOfMinute(0);
		Curdate.setMillisOfSecond(0);
		// Create new event
		event= new Event (-2, new DateTime(Curdate), new DateTime(Curdate).plusMinutes(30), new DateTime(Curdate), false, "E", "Nouvel évenement", "", "");
		arrEvents.clear();
		arrEvents.add(event.toArray());
		evList.removeListSelectionListener(llev);
		lm.clear();
		lm.addElement("<Nouvel évenement>");
		evList.addListSelectionListener(llev);
		setBegdate(Curdate);
		setBegtime(event.begDate);
		setEndtime(event.endDate);
		setRectime(event.recDate);
		changeState(false);
	}

	// populate event list with day events
	public void setEvents (String [] arr) {
		arrEvents.add(arr);
		evList.removeListSelectionListener(llev);
		lm.addElement(arr[6]); 
		evList.addListSelectionListener(llev);
	}
	
	/**
	 * Create the dialog.
	 */
	public dlgEvent() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				//mrOK= false;
				modalresult=mrNone;
				evList.setSelectedIndex(0);
				for (int i=0; i<deleted.length; i+=1) deleted [i]=-1;
				
			}
		});
		
		event= new Event (-2, new DateTime(), new DateTime().plusMinutes(30), new DateTime(), false, "E", "Nouvel évenement", "", "");
		arrEvents.add(event.toArray());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Nouvel \u00E9v\u00E9nement");
		setBounds(100, 100, 594, 307);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new CompoundBorder());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		// Date label and chooser
		JLabel lblEvDate = new JLabel("Date :");
		lblEvDate.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEvDate.setBounds(230, 17, 46, 14);
		contentPanel.add(lblEvDate);
		
		dtEvChooser = new JDateChooser();
		dtEvChooser.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dtEvChooser.setDateFormatString("dd/MM/yyyy");
		dtEvChooser.setBounds(295, 14, 120, 20);
		contentPanel.add(dtEvChooser);
		dtEvChooser.setDate(new Date());	
		
		// change date choosen
		pcldc = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				 if ("date".equals(e.getPropertyName())) {
					 event.begDate= changeBegdate();
					 event.endDate= changeEndDate(event.begDate);
					 event.recDate= changeRecDate(event.begDate);
					 changeState(true);
		         }
			}
		};
		dtEvChooser.addPropertyChangeListener(pcldc);
		
		// begin time label and Spinner 
		JLabel lblEvBeg = new JLabel("D\u00E9but :");
		lblEvBeg.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEvBeg.setBounds(424, 17, 36, 14);
		contentPanel.add(lblEvBeg);
		sdm = new SpinnerDateModel();
		tiEvBegin = new JSpinner(sdm);
		JSpinner.DateEditor de_tiEvBegin = new JSpinner.DateEditor(tiEvBegin, "HH:mm");
		tiEvBegin.setSize(new Dimension(0, 22));
		tiEvBegin.setPreferredSize(new Dimension(98, 24));
		tiEvBegin.setMinimumSize(new Dimension(98, 22));
		tiEvBegin.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tiEvBegin.setBounds(474, 14, 56, 20);
		tiEvBegin.setEditor(de_tiEvBegin);
		contentPanel.add(tiEvBegin);
		
		// change begin, end and rec time processing
		cleb = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (e.getSource().equals(tiEvBegin)) {
					event.begDate= changeBegdate();
					event.endDate= changeEndDate(event.begDate);
					event.recDate=changeRecDate(event.begDate);
					changeState(true);
				}
				else if (e.getSource().equals(tiEvEnd)) {
					event.endDate= changeEndDate(event.begDate);
					changeState(true);
				}
				else if (e.getSource().equals(tiEvRec)) {
					event.recDate= changeRecDate(event.begDate);
					changeState(true);
				}
				//arrEvents.set(evList.getSelectedIndex(), event.toArray());
			}
		};
		
		tiEvBegin.addChangeListener(cleb);
		
		// list of days'sevents 
		JPanel evLstPanel = new JPanel();
		evLstPanel.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		evLstPanel.setBounds(10, 14, 206, 164);
		contentPanel.add(evLstPanel);
		evLstPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane evLstScrl = new JScrollPane();
		evLstScrl.setBorder(null);
		evLstPanel.add(evLstScrl);
		
		lm = new DefaultListModel<String>();  
		evList = new JList<String>(lm);
		evList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		evLstScrl.setViewportView(evList);
		evList.setBounds(248, 92, 153, 148);
		
		evList.setFont(new Font("Tahoma", Font.PLAIN, 11));
		evList.setVisibleRowCount(5);
		
		lm.addElement("<Nouvel événement>");
		
		evList.setSelectedIndex(0);
		
		// List item selection processing 
		llev = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
			//retrieve event parameters
				try {
					event.fromArray(arrEvents.get(evList.getSelectedIndex()));
					// set chooser and spinners valuesr
					setBegdate(event.begDate);
					setBegtime(event.begDate);
					setEndtime(event.endDate);
					setRectime(event.recDate);
					// set cbduration proper item
					cbDuration.removeActionListener(cbdal);
					Minutes diff = Minutes.minutesBetween(event.begDate, event.endDate);
					cbDuration.setSelectedIndex(cbdmins.length-1);
					tiEvEnd.setEnabled(true);
					for (int i=0; i < cbdmins.length; i+= 1) {
						if (diff.getMinutes()==cbdmins[i]) {
							cbDuration.setSelectedIndex(i);
							tiEvEnd.setEnabled(false);
							break;
						}
					}	
					cbDuration.addActionListener(cbdal);
					// set cbRecall proper item
					cbRecall.removeActionListener(cbral);
					if (!event.recall) {
						cbRecall.setSelectedIndex(0);
					}
					else {
						diff = Minutes.minutesBetween(event.recDate, event.begDate);
						for (int i=1; i < cbrmins.length; i+= 1) {
							if (diff.getMinutes()==cbrmins[i]) {
								cbRecall.setSelectedIndex(i);
								break;
							}
							else {
							
							}
						}
					}
					cbRecall.addActionListener(cbral);
					// set text fields
					tfReference.getDocument().removeDocumentListener(dl);
					tfLocation.getDocument().removeDocumentListener(dl);
					taComment.getDocument().removeDocumentListener(dl);
					tfReference.setText(event.reference);
					tfLocation.setText(event.location);
					taComment.setText(event.comment);
					tfReference.getDocument().addDocumentListener(dl);
					tfLocation.getDocument().addDocumentListener(dl);
					taComment.getDocument().addDocumentListener(dl);
					// todo enable type combo box
				} catch (Exception e) {
					//Nothing to do invalid date
				}
			}
		};
		evList.addListSelectionListener(llev);
		
		// Event duration label and combo box		
		JLabel lblEvDur = new JLabel("Dur\u00E9e :");
		lblEvDur.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEvDur.setBounds(230, 47, 46, 14);
		contentPanel.add(lblEvDur);
		
		cbDuration = new JComboBox <String> ();
		// Populate with array 
		for(String str : cbdarr)  cbDuration.addItem(str);
		cbDuration.setFont(new Font("Tahoma", Font.PLAIN, 11));
		cbDuration.setBounds(295, 44, 120, 20);
		contentPanel.add(cbDuration);

	    // end time label and spinner
		JLabel lblEvEnd = new JLabel("Fin :");
		lblEvEnd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEvEnd.setBounds(424, 46, 46, 14);
		contentPanel.add(lblEvEnd);
				
		sdm1 = new SpinnerDateModel();

		tiEvEnd = new JSpinner(sdm1);
		tiEvEnd.addChangeListener(cleb);
		tiEvEnd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tiEvEnd.setEnabled(false);
		JSpinner.DateEditor de_tiEvEnd = new JSpinner.DateEditor(tiEvEnd, "HH:mm");
		tiEvEnd.setBounds(474, 43, 56, 20);
		tiEvEnd.setEditor(de_tiEvEnd);
		contentPanel.add(tiEvEnd);
		
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
		//sdm2.setCalendarField(0);
		tiEvRec = new JSpinner(sdm2);
		cler = new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				event.recDate= new DateTime((Date) tiEvRec.getModel().getValue()); 
			}
		};
		tiEvRec.addChangeListener(cler);
		
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
		tfReference.setText("Nouvel \u00E9venement");
		tfReference.getDocument().putProperty("owner", tfReference);
		tfReference.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		tfReference.setBounds(295, 104, 273, 20);
		contentPanel.add(tfReference);
		tfReference.setColumns(10);
		
		JLabel lblLoc = new JLabel("Lieu :");
		lblLoc.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblLoc.setBounds(230, 137, 65, 14);
		contentPanel.add(lblLoc);
		
		tfLocation = new JTextField();
		tfLocation.getDocument().putProperty("owner", tfLocation);
		tfLocation.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		tfLocation.setColumns(10);
		tfLocation.setBounds(295, 134, 273, 20);
		contentPanel.add(tfLocation);
		
		taComment = new JTextArea();
		taComment.getDocument().putProperty("owner", taComment);
		taComment.setIgnoreRepaint(true);
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
				event.endDate= changeEndDate(event.begDate);
				//if (cbDuration.getSelectedIndex()==cbDuration.c
				changeState(true);
			}
		};
		cbDuration.addActionListener(cbdal);
		
		// Action on change in recall combo box 
		cbral = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		
				
				
				
				event.recDate= changeRecDate(event.begDate);
				changeState(true);
				//arrEvents.set(evList.getSelectedIndex(), event.toArray());
			}
		};
		
		
		cbRecall.addActionListener(cbral);
		
		// Reference field changed, allow OK button
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
				Object owner = e.getDocument().getProperty("owner");
				if(owner != null){
					if (owner.equals(tfReference)) {
						changed= true;
						okButton.setEnabled(changed);
						event.reference= tfReference.getText();
					}
					else if (owner.equals(tfLocation)) {
						changed= true;
						okButton.setEnabled(changed);
						event.location= tfLocation.getText();
					}
					else if (owner.equals(taComment)) {
						changed= true;
						okButton.setEnabled(changed);
						event.comment= taComment.getText();
					}
					changeState(true);
					//arrEvents.set(evList.getSelectedIndex(), event.toArray());
				}
			}
		};
		tfReference.getDocument().addDocumentListener(dl);
		tfLocation.getDocument().addDocumentListener(dl);
		taComment.getDocument().addDocumentListener(dl);

		
		// Buttons processing
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			// OK Button
			{
				okButton = new JButton("OK");
				okButton.setBounds(new Rectangle(0, 0, 65, 23));
				okButton.setFont(new Font("Tahoma", Font.BOLD, 11));
				okButton.setPreferredSize(new Dimension(75, 23));
				okButton.setMinimumSize(new Dimension(65, 23));
				okButton.setMaximumSize(new Dimension(65, 23));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				albtn = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (e.getSource().equals(okButton)) {
							modalresult= mrOK;
							//mrOK= true;
							setVisible(false);	
						}
						else if (e.getSource().equals(cancelButton)) {
							modalresult= mrCancel;
							changed= false;
							dispose();
						}
						else if (e.getSource().equals(delButton)) {
							try {
								// Index in the jlist
								int i= evList.getSelectedIndex();
								// Real index in the event list 
								int j= Integer.parseInt(arrEvents.get(i)[0]);
								if (j == -2) {
									// don't delete this one, it is not really set
								}
								else if (j==-1) {
									// Changed but not yet created : reset to -2
									arrEvents.get(i)[0]= "-2";
								}
								else {
									deleted[0]= j; 
									evList.removeListSelectionListener(llev);
									lm.remove(i); 
									arrEvents.remove(i);
									evList.addListSelectionListener(llev);
									changed= true;
									okButton.setEnabled(true);
									if (lm.size()== 0) delButton.setEnabled(false);
									
								}
								
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								//e1.printStackTrace();
							}
						}
						
					}
				};
				okButton.addActionListener(albtn);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			
			delButton = new JButton("Supprimer");
			delButton.setSize(new Dimension(75, 23));
			delButton.setBounds(new Rectangle(0, 0, 75, 23));
			delButton.setPreferredSize(new Dimension(75, 23));
			delButton.setMinimumSize(new Dimension(75, 23));
			delButton.setMaximumSize(new Dimension(75, 23));
			delButton.setMargin(new Insets(2, 5, 2, 5));
			delButton.setFont(new Font("Tahoma", Font.BOLD, 11));
			buttonPane.add(delButton);
			delButton.addActionListener(albtn);
			
			cancelButton = new JButton("Annuler");
			cancelButton.setPreferredSize(new Dimension(75, 23));
			cancelButton.setMinimumSize(new Dimension(65, 23));
			cancelButton.setMaximumSize(new Dimension(65, 23));
			cancelButton.setMargin(new Insets(2, 5, 2, 5));
			cancelButton.setFont(new Font("Tahoma", Font.BOLD, 11));
			buttonPane.add(cancelButton);
			// Cancel button event processing
			cancelButton.addActionListener(albtn);		
		}
	}
	
	
	// Disable event processing and set date to chooser
	private void setBegdate(DateTime begdat) {
		dtEvChooser.removePropertyChangeListener(pcldc);
		dtEvChooser.setDate(begdat.toDate());
		dtEvChooser.addPropertyChangeListener(pcldc);
	}
	
	private void setBegdate(MutableDateTime begdat) {
		setBegdate(new DateTime(begdat));
	}
	
	// Disable event processing and set time to begin spinner  
	private void setBegtime(DateTime begtime) {
		tiEvBegin.removeChangeListener(cleb);
		tiEvBegin.setValue(begtime.toDate());
		tiEvBegin.addChangeListener(cleb);
	}
	
	@SuppressWarnings("unused")
	private void setBegtime(MutableDateTime begtime) {
		setBegtime(new DateTime(begtime));
	}
	
	// Disable event processing and set time to end spinner  
	private void setEndtime(DateTime endtime) {
		tiEvEnd.removeChangeListener(cleb);
		tiEvEnd.setValue(endtime.toDate());
		tiEvEnd.addChangeListener(cleb);
	}
	
	@SuppressWarnings("unused")
	private void setEndtime(MutableDateTime endtime) {
		setEndtime(new DateTime(endtime));
	}
	
	// Disable event processing and set time to recall spinner  
	private void setRectime(DateTime rectime) {
		tiEvRec.removeChangeListener(cler);
		tiEvRec.setValue(rectime.toDate());	
		tiEvRec.addChangeListener(cler);
	}
	
	@SuppressWarnings("unused")
	private void setRectime(MutableDateTime rectime) {
		setRectime(new DateTime(rectime));
	}
	
	// Processing begin date change in chooser or spinner
	private DateTime changeBegdate() {
		// date in chooser
		MutableDateTime tmpdat= new MutableDateTime(dtEvChooser.getDate());
		// hour in spinner
		MutableDateTime tmptim = new MutableDateTime((Date) tiEvBegin.getModel().getValue());
		tmpdat.setHourOfDay(tmptim.getHourOfDay());
		tmpdat.setMinuteOfHour(tmptim.getMinuteOfHour());
		tmpdat.setSecondOfMinute(0);
		tmpdat.setMillisOfSecond(0);
		// new begin date
		
		return (new DateTime(tmpdat));

	}
	
	private DateTime changeEndDate (DateTime begdat) {
		DateTime result;
		// get duration in combo
		int i = cbDuration.getSelectedIndex();
		if (i!=cbdarr.length-1) {
			DateTime enddat= new DateTime(begdat);
			enddat= enddat.plusMinutes(cbdmins[i]);
			tiEvEnd.removeChangeListener(clee);
			tiEvEnd.setEnabled(false);
			tiEvEnd.setValue(enddat.toDate());
			tiEvEnd.addChangeListener(clee);
			result= new DateTime(enddat);
		}
		else {
			// use preset end time
			tiEvEnd.setEnabled(true);
			DateTime tmptim= new DateTime((Date) tiEvEnd.getModel().getValue()); 
			MutableDateTime enddat= new MutableDateTime(begdat);
			enddat.setHourOfDay(tmptim.getHourOfDay());
			enddat.setMinuteOfHour(tmptim.getMinuteOfHour());
			enddat.setSecondOfMinute(0);
			enddat.setMillisOfSecond(0);
			result= new DateTime(enddat);
			// Cannot end before begin !!!
			if (result.isBefore(begdat)) {
				result= begdat;
				tiEvEnd.setValue(begdat.toDate());
			}
		}
		return result;
	}
	
	private DateTime changeRecDate(DateTime begdat) {
		DateTime result= new DateTime(begdat);
		int i = cbRecall.getSelectedIndex();
		tiEvRec.setEnabled(false);
		event.recall= true;
		if (i==0)  {
			event.recall= false;
		}
		else if  (i==cbdarr.length-1)
		{
			tiEvRec.setEnabled(true);
			result= new DateTime((Date) tiEvRec.getModel().getValue()); 
		}
		else {
				result= result.minusMinutes(cbrmins[i]);
				tiEvRec.setValue(result.toDate());
		}
				// Cannot recall after event ? Not implemented
		//
		return result;
	}
	
	private void changeState(boolean state) {
		int curindex = evList.getSelectedIndex();
		// if the new event is modified index is -1 else index is -2
		if (evList.getSelectedIndex()==0) {
		    event.index= -1; 
		}
		try {
			if (state) arrEvents.set(curindex, event.toArray());
		} catch (Exception e) {
			//System.out.println(curindex);
			
		}
		changed= state;
		okButton.setEnabled(state);
	}
}
