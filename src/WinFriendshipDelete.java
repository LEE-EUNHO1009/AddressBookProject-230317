import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowFocusListener;

public class WinFriendshipDelete extends JDialog {
	private JTable table;
	private JTextField tfNid;
	private JTextField tfAddress;
	private DefaultTableModel dtm;
	private JComboBox cbGradYear;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinFriendshipDelete dialog = new WinFriendshipDelete();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public WinFriendshipDelete() {
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				showRecords("",0);
			}
			public void windowLostFocus(WindowEvent e) {
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});			
		setTitle("전체 동창회 목록");
		setBounds(100, 100, 600, 444);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblGradYear = new JLabel("졸업년도:");
		panel.add(lblGradYear);
		
		cbGradYear = new JComboBox();
		cbGradYear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(cbGradYear.getSelectedIndex() != -1 )
					showRecords(cbGradYear.getSelectedItem().toString(), 3);
			}
		});
		cbGradYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		panel.add(cbGradYear);
		
		JLabel lblNewLabel = new JLabel("학번:");
		panel.add(lblNewLabel);
		
		tfNid = new JTextField();
		tfNid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(KeyEvent.VK_ENTER == e.getKeyCode()) {
					String sNid = tfNid.getText();
					if(sNid.equals(""))
						showRecords(sNid, 0);
					else
						showRecords(sNid, 1);
				}
			}
		});
		panel.add(tfNid);
		tfNid.setColumns(10);
		
		JLabel lblAddress = new JLabel("주소:");
		panel.add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(KeyEvent.VK_ENTER == e.getKeyCode()) {
					String sAddress = tfAddress.getText();
					showRecords(sAddress, 2);
				}
			}
		});
		tfAddress.setColumns(10);
		panel.add(tfAddress);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		String columns[] = {"학번","이름","전화번호","주소","이메일","졸업년도","생년월일"};
		dtm = new DefaultTableModel(columns,0);		
		table = new JTable(dtm);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				String sNid = table.getValueAt(row, 0).toString();
				WinFriendshipDetail winFriendshipDetail = new WinFriendshipDetail(sNid);
				winFriendshipDetail.setModal(true);
				winFriendshipDetail.setVisible(true);
			}
		});
		
		scrollPane.setViewportView(table);

		
		showRecords("",0);
		showGradYear();
	}

	private void showGradYear() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");
			
			//=============================================		
			String sql = "select distinct gradYear from addrBookTBL order by gradYear desc";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				cbGradYear.addItem(rs.getInt("gradYear"));
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

	
	private void showRecords(String search, int type) {
		dtm.setRowCount(0);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");
			
			//=============================================		
			String sql = "";
			if(type == 0)  // 전부
				sql = "select * from addrBookTBL";
			else if(type == 1) // 학번 검색
				sql = "select * from addrBookTBL where nid=" + search;
			else if(type == 2) // 주소 검색
				sql = "select * from addrBookTBL where address like '%" + search + "%'";
			else if(type == 3) // 졸업년도 검색
				sql = "select * from addrBookTBL where gradYear = " + search;
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				Vector <String> vec = new Vector<>();
				for(int i=1;i<=8;i++) {
					if(i==6) continue;
					vec.add(rs.getString(i));
				}
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
