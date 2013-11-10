import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;

import com.toedter.calendar.JDateChooser;


public class dlgEvent extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DateTime Curdate= new DateTime();
	private final JPanel contentPanel = new JPanel();

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

	/**
	 * Create the dialog.
	 */
	public dlgEvent() {
		setTitle("Nouvel \u00E9v\u00E9nement");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblEvDate = new JLabel("Date :");
		lblEvDate.setBounds(10, 11, 46, 14);
		contentPanel.add(lblEvDate);
		
		

		
		SpinnerDateModel sm = 
				  new SpinnerDateModel();
		JSpinner spinner = new JSpinner(sm);
		spinner.setSize(new Dimension(0, 22));
		spinner.setPreferredSize(new Dimension(98, 24));
		spinner.setMinimumSize(new Dimension(98, 22));
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 11));
		spinner.setBounds(176, 8, 56, 20);
		 JSpinner.DateEditor de = new JSpinner.DateEditor(spinner, "HH:mm");
		  spinner.setEditor(de);
		contentPanel.add(spinner);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBorder(new CompoundBorder());
		dateChooser.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.setBounds(48, 8, 120, 20);
		contentPanel.add(dateChooser);
		dateChooser.setDate(Curdate.toDate());
		

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
