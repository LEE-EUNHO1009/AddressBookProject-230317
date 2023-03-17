import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WinTax extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfPrice;
	private JLabel lblSellPrice;
	private JLabel lblTax;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinTax dialog = new WinTax();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinTax() {
		setTitle("영수증 출력");
		setBounds(100, 100, 326, 190);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblPrice = new JLabel("승인 금액:");
		lblPrice.setBounds(59, 27, 57, 15);
		contentPanel.add(lblPrice);
		
		tfPrice = new JTextField();
		tfPrice.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					int price = Integer.parseInt(tfPrice.getText());
					double sellprice = (double) price / 11 * 10;
					int tax = (int) (sellprice / 10);		
					lblSellPrice.setText(Integer.toString((int)Math.ceil(sellprice)));
					lblTax.setText(Integer.toString(tax));
				}
			}
		});
		tfPrice.setText("0");
		tfPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		tfPrice.setBounds(144, 24, 116, 21);
		contentPanel.add(tfPrice);
		tfPrice.setColumns(10);
		
		JLabel lblSellPrice2 = new JLabel("판매금액:");
		lblSellPrice2.setBounds(59, 85, 57, 15);
		contentPanel.add(lblSellPrice2);
		
		lblSellPrice = new JLabel("0");
		lblSellPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSellPrice.setFont(new Font("굴림", Font.BOLD, 12));
		lblSellPrice.setBounds(144, 85, 116, 15);
		contentPanel.add(lblSellPrice);
		
		lblTax = new JLabel("0");
		lblTax.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTax.setFont(new Font("굴림", Font.BOLD, 12));
		lblTax.setBounds(144, 110, 116, 15);
		contentPanel.add(lblTax);
		
		JLabel lblTax2 = new JLabel("부가세:");
		lblTax2.setBounds(59, 110, 57, 15);
		contentPanel.add(lblTax2);
	}
}
