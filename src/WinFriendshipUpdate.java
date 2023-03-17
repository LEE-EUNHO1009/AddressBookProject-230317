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
import javax.swing.DefaultComboBoxModel;

public class WinFriendshipUpdate extends JDialog {
	private JTable table;
	private JTextField tfSearch;
	private DefaultTableModel dtm;
	private JComboBox cbSearchType;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinFriendshipUpdate dialog = new WinFriendshipUpdate();
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
	public WinFriendshipUpdate() {
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
		
		JLabel lblSearchType = new JLabel("검색 방법:");
		panel.add(lblSearchType);
		
		cbSearchType = new JComboBox();
		cbSearchType.setModel(new DefaultComboBoxModel(new String[] {"학번", "이름", "주소", "전화번호", "이메일", "졸업년도", "출생연도"}));
		cbSearchType.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(cbSearchType.getSelectedIndex() != -1 )
					showRecords(cbSearchType.getSelectedItem().toString(), 3);
			}
		});
		cbSearchType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		panel.add(cbSearchType);
		
		tfSearch = new JTextField();
		tfSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(KeyEvent.VK_ENTER == e.getKeyCode()) {
					String searchWord = tfSearch.getText();
					if(searchWord.equals(""))
						showRecords(searchWord, 0);
					else {						
						switch(cbSearchType.getSelectedItem().toString()) {					
						case "학번": 		showRecords(searchWord, 1); break;
						case "주소": 		showRecords(searchWord, 2); break;
						case "졸업년도": 	showRecords(searchWord, 3); break;
						case "이름": 		showRecords(searchWord, 4); break;
						case "전화번호":	showRecords(searchWord, 5); break;
						case "이메일": 	showRecords(searchWord, 6); break;
						case "출생연도": 	showRecords(searchWord, 7); break; // 년도만
						}
					}
				}
			}
		});
		panel.add(tfSearch);
		tfSearch.setColumns(10);
		
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
				WinFriendshipDetailUpdate winFriendshipDetailUpdate = new WinFriendshipDetailUpdate(sNid);
				winFriendshipDetailUpdate.setModal(true);
				winFriendshipDetailUpdate.setVisible(true);
			}
		});
		
		scrollPane.setViewportView(table);

		
		showRecords("",0);		
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
			
			else if(type == 4) // 이름 검색
				sql = "select * from addrBookTBL where name like '%" + search + "%'";
			else if(type == 5) // 전화번호 검색
				sql = "select * from addrBookTBL where mobile like '%" + search + "%'";
			else if(type == 6) // 이메일 검색
				sql = "select * from addrBookTBL where email like '%" + search + "%'";
			else if(type == 7) // 출생년도 검색
				sql = "select * from addrBookTBL where birth like '" + search + "%'";
			
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
