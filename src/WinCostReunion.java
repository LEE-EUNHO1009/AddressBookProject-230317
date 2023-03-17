import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;

public class WinCostReunion extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfID;
	private JTextField tfMoney;
	private JTable table;
	private int count;
	private JTextField tfName;
	private JTextField tfMobile;
	private JPanel panel;
	private JTable tableSearch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinCostReunion dialog = new WinCostReunion();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinCostReunion() {
		setTitle("회비입금");
		setBounds(100, 100, 958, 449);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblIdx = new JLabel("Idx :  ????");
		lblIdx.setBounds(29, 10, 139, 15);
		contentPanel.add(lblIdx);
		
		JLabel lblID = new JLabel("ID:");
		lblID.setBounds(29, 35, 39, 15);
		contentPanel.add(lblID);
		
		tfID = new JTextField();
		tfID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					tfMoney.requestFocus();
				}					
			}
		});
		tfID.setHorizontalAlignment(SwingConstants.RIGHT);
		tfID.setBounds(80, 32, 136, 21);
		contentPanel.add(tfID);
		tfID.setColumns(10);
		
		JButton btnSearch = new JButton("찾기");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(true);
			}
		});
		btnSearch.setBounds(224, 31, 97, 23);
		contentPanel.add(btnSearch);
		
		JComboBox cbYear = new JComboBox();
		cbYear.setBounds(79, 69, 109, 23);
		contentPanel.add(cbYear);
		
		Calendar now = Calendar.getInstance();
		int y = now.get(Calendar.YEAR);
		for(; y>=2010; y--)
			cbYear.addItem(y+"년");
		
		JComboBox cbMonth = new JComboBox();
		cbMonth.setBounds(234, 69, 85, 23);
		contentPanel.add(cbMonth);
		for(int m=1; m<=12; m++)
			cbMonth.addItem(m + "월");
		int m = now.get(Calendar.MONTH);
		cbMonth.setSelectedIndex(m);
		
		JLabel lblMoney = new JLabel("Money:");
		lblMoney.setBounds(29, 115, 57, 15);
		contentPanel.add(lblMoney);
		
		tfMoney = new JTextField();
		tfMoney.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(real(tfID.getText())) {
						count++;
						
						Vector <String> vec = new Vector<>();
						vec.add(count+"번째");
						vec.add(tfID.getText());
						vec.add(cbYear.getSelectedItem().toString());
						vec.add(cbMonth.getSelectedItem().toString());
						vec.add(tfMoney.getText());
						vec.add(getDateTime()); //날짜_시간 정보
						DefaultTableModel dtm = (DefaultTableModel)table.getModel();
						dtm.addRow(vec);	
					}else {
						tfID.requestFocus();
						tfID.setSelectionStart(0);
						tfID.setSelectionEnd(tfID.getText().length());
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_F2)
					tfMoney.setText(tfMoney.getText() + "0000");
				else if(e.getKeyCode() == KeyEvent.VK_F3)
					tfMoney.setText(tfMoney.getText() + "000");
				else if(e.getKeyCode() == KeyEvent.VK_F4)
					tfMoney.setText(tfMoney.getText() + "00");
			}

			private String getDateTime() {
				Calendar now = Calendar.getInstance();
				int year = now.get(Calendar.YEAR);
				int month = now.get(Calendar.MONTH)+1;
				int day = now.get(Calendar.DATE);
				int hour = now.get(Calendar.HOUR_OF_DAY);
				int minute = now.get(Calendar.MINUTE);
				int second = now.get(Calendar.SECOND);
				
				return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
			}
		});
		tfMoney.setHorizontalAlignment(SwingConstants.RIGHT);
		tfMoney.setBounds(79, 112, 242, 21);
		contentPanel.add(tfMoney);
		tfMoney.setColumns(10);
		
		JButton btnMoney = new JButton("입금");
		btnMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				count = 0;
				insertRecords();
				DefaultTableModel dtm = (DefaultTableModel)table.getModel();
				dtm.setRowCount(0);
			}
		});
		btnMoney.setBounds(347, 35, 97, 99);
		contentPanel.add(btnMoney);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 159, 918, 241);
		contentPanel.add(scrollPane);
		String columns[]= {"순서","학번","연도","월","금액","시간"};
		DefaultTableModel dtm = new DefaultTableModel(columns, 0);
		
		table = new JTable(dtm);
		scrollPane.setViewportView(table);
		
		panel = new JPanel();
		panel.setBounds(456, 16, 474, 135);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		panel.setVisible(false);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 19, 39, 15);
		panel.add(lblName);
		
		tfName = new JTextField();
		tfName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String sName = tfName.getText();
					showSearchRecord(sName, 0);
				}
			}
		});
		tfName.setHorizontalAlignment(SwingConstants.RIGHT);
		tfName.setColumns(10);
		tfName.setBounds(63, 13, 97, 21);
		panel.add(tfName);
		
		JLabel lblMobile = new JLabel("Mobile:");
		lblMobile.setBounds(172, 19, 57, 15);
		panel.add(lblMobile);
		
		tfMobile = new JTextField();
		tfMobile.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String sMobile = tfMobile.getText();
					showSearchRecord(sMobile, 1);
				}
			}
		});
		tfMobile.setHorizontalAlignment(SwingConstants.RIGHT);
		tfMobile.setColumns(10);
		tfMobile.setBounds(241, 13, 97, 21);
		panel.add(tfMobile);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 43, 450, 82);
		panel.add(scrollPane_1);
		String columnNames[]= {"학번","이름","졸업년도","생년월일"};
		DefaultTableModel sDTM = new DefaultTableModel(columnNames, 0); 
		tableSearch = new JTable(sDTM);
		tableSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tableSearch.getSelectedRow();
				if(row != -1) {
					tfID.setText(tableSearch.getValueAt(row, 0).toString());
					tfMoney.requestFocus();
				}
			}
		});
		scrollPane_1.setViewportView(tableSearch);
		
		JComboBox cbEtc = new JComboBox();
		cbEtc.setModel(new DefaultComboBoxModel(new String[] {"주소", "이메일", "졸업년도", "생년월일"}));
		cbEtc.setBounds(365, 11, 97, 23);
		panel.add(cbEtc);
	}

	protected boolean real(String sID) { // 해당 sID(학번)이 있는가? true or false 
		// DB 연동
		boolean bExist = false;	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");
			
			//=============================================		
			String sql = "select count(*) from addrBookTBL where nID=" + sID;				
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				if(rs.getInt(1) == 1)
					bExist = true;
				else
					bExist = false;
			}
			//==============================================
			con.close();
		} catch (ClassNotFoundException e1) {
			System.out.println("JDBC 드라이버 로드 에러");
		} catch (SQLException e1) {
			System.out.println("DB 연결 오류");
		} finally {
			return bExist;
		}
	}

	protected void insertRecords() {
		// DB 연동
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");
			
			//=============================================		
			String sql = "insert into reunionTBL values(null,?,?,?,?,?)";				
			PreparedStatement pstmt = con.prepareStatement(sql);
			for(int row=0; row < table.getModel().getRowCount(); row++) {
				for(int col=1; col < table.getModel().getColumnCount(); col++) {
					String temp = table.getValueAt(row, col).toString();
					if(col==2) // 연도
						pstmt.setInt(col, Integer.parseInt(temp.substring(0,4)));
					else if(col==3) { //월						
						int idx = temp.indexOf("월");
						pstmt.setInt(col, Integer.parseInt(temp.substring(0,idx)));
					}else if(col==5)
						pstmt.setString(col,temp);
					else
						pstmt.setInt(col, Integer.parseInt(temp));
				}
				pstmt.executeUpdate();
			}			
			pstmt.close();
			//==============================================
			con.close();
		} catch (ClassNotFoundException e1) {
			System.out.println("JDBC 드라이버 로드 에러");
		} catch (SQLException e1) {
			System.out.println("DB 연결 오류");
		}	
	}

	protected void showSearchRecord(String sTemp, int type) {
		// DB 연동
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");
			
			//=============================================		
			String sql = "";
			if(type==0) {  // 이름 검색
				sql = "select nID, Name, gradYear, birth from addrBookTBL where Name='";
				sql = sql + sTemp + "'";
			}else if(type==1) { // 전화번호 검색
				sql = "select nID, Name, gradYear, birth from addrBookTBL where mobile like '%";
				sql = sql + sTemp + "%'";				
			}			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			DefaultTableModel dtm = (DefaultTableModel)tableSearch.getModel();	
			dtm.setRowCount(0);
			while(rs.next()) {
				Vector <String> vec = new Vector<>();
				for(int i=1;i<=4;i++)
					vec.add(rs.getString(i));
				dtm.addRow(vec);
			}
			rs.close();
			stmt.close();
			//==============================================
			con.close();
		} catch (ClassNotFoundException e1) {
			System.out.println("JDBC 드라이버 로드 에러");
		} catch (SQLException e1) {
			System.out.println("DB 연결 오류");
		}	
		
	}
}
