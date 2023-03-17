import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WinMoneyShow extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private DefaultTableModel dtm;
	private JRadioButton rbYear;
	private JRadioButton rbYMonth;
	private JComboBox cbYear;
	private JComboBox cbMonth;
	private JLabel lblTotalMoney = new JLabel("");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinMoneyShow dialog = new WinMoneyShow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinMoneyShow() {
		setTitle("회비명부");
		setBounds(100, 100, 772, 615);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		String []columnNames = {"idx", "nID", "Year", "Month", "Money", "mDateTime"};
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				dtm = new DefaultTableModel(columnNames, 0);
				table = new JTable(dtm);
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				rbYear = new JRadioButton("연도만");
				rbYear.setSelected(true);
				buttonGroup.add(rbYear);
				panel.add(rbYear);
			}
			{
				rbYMonth = new JRadioButton("연도와 월");
				buttonGroup.add(rbYMonth);
				panel.add(rbYMonth);
			}
			{
				cbYear = new JComboBox();
				cbYear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showMetheMoney();
					}
				});
				cbYear.setEditable(true);
				panel.add(cbYear);
				for(int y=2023; y>=2010; y--)
					cbYear.addItem(y);
			}
			{
				JLabel lblYear = new JLabel("년도");
				panel.add(lblYear);
			}
			{
				cbMonth = new JComboBox();
				cbMonth.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showMetheMoney();
					}
				});
				cbMonth.setEditable(true);
				panel.add(cbMonth);
				for(int m=1;m<=12;m++)
					cbMonth.addItem(m);
			}
			{
				JLabel lblMonth = new JLabel("월");
				panel.add(lblMonth);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			{				
				lblTotalMoney.setFont(new Font("굴림", Font.BOLD, 20));
				panel.add(lblTotalMoney);
			}
		}
	}

	protected void showMetheMoney() {
		dtm.setRowCount(0);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");
			
			//=============================================		
			String sql = "select * from reunionTBL";			
			if(rbYear.isSelected()) {
				sql = "select * from reunionTBL where year=" + cbYear.getSelectedItem();
			}else {
				sql = "select * from reunionTBL where year=" + cbYear.getSelectedItem();
				sql = sql + " and month=" + cbMonth.getSelectedItem();
			}
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			double total = 0.;
			while(rs.next()) {
				String record[] = new String[6];
				for(int i=1;i<=record.length;i++) {
					record[i-1] = rs.getString(i);
					if(i==5)
						total = total + Double.parseDouble(record[i-1]);  
					//인티져로 해도됨. 값이 크지 않으니까 굳이 더블로 할 필요없음
				}
				dtm.addRow(record);
				//				
			}
			String amount = Double.toString(Math.round(total));
			amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
			
			lblTotalMoney.setText("전체 금액 :" + amount);
			//==============================================
			con.close();
		} catch (ClassNotFoundException e1) {
			System.out.println("JDBC 드라이버 로드 에러");
		} catch (SQLException e1) {
			System.out.println("DB 연결 오류");
		} 	
	}

}
