import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Person extends JPanel {
	private JTextField textField;
	public Person() {
		setBackground(new Color(128, 0, 0));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Person.class.getResource("/images/memberAdd.png")));
		lblNewLabel.setBackground(new Color(255, 255, 0));
		lblNewLabel.setBounds(12, 10, 61, 95);
		lblNewLabel.setOpaque(true);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(134, 33, 116, 21);
		add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("클릭");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "버튼 클릭!!!");
			}
		});
		btnNewButton.setBounds(134, 149, 116, 23);
		add(btnNewButton);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1999", "2000", "2001"}));
		comboBox.setBounds(134, 82, 116, 23);
		add(comboBox);
		
		JLabel lblNewLabel_1 = new JLabel("이름:");
		lblNewLabel_1.setBounds(134, 10, 57, 15);
		add(lblNewLabel_1);
		
		JButton btnNewButton_1 = new JButton("삽입");
		btnNewButton_1.setBounds(12, 210, 97, 23);
		add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("삭제");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnNewButton_2.setBounds(217, 210, 97, 23);
		add(btnNewButton_2);
	}
}
