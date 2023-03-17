import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Vector;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WinFriendshipDetailUpdate extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfNid;
	private JTextField tfName;
	private JTextField tfMobile;
	private JTextField tfAddress;
	private JTextField tfEmail;
	private JTextField tfBirth;
	protected String filePath="";
	private JComboBox cbGradYear;
	private JCheckBox ckSLType;
	private JButton btnFriendshipUpdate;
	private JLabel lblPic;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinFriendshipDetailUpdate dialog = new WinFriendshipDetailUpdate();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinFriendshipDetailUpdate() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
			}
		});
		setResizable(false);
		setTitle("친구 변경");
		setBounds(100, 100, 461, 291);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblPic = new JLabel("");
		lblPic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("그림파일","png","jpg","gif","bmp");
					chooser.setFileFilter(filter);
					int ret = chooser.showOpenDialog(null);
					if(ret == JFileChooser.APPROVE_OPTION) {
						filePath = chooser.getSelectedFile().getPath();
						ImageIcon icon = new ImageIcon(filePath);
						Image img = icon.getImage();
						img = img.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
						ImageIcon image = new ImageIcon(img);
						lblPic.setIcon(image);
						filePath = filePath.replaceAll("\\\\", "\\\\\\\\");
					}
				}
			}
		});
		lblPic.setToolTipText("더블클릭하여 사진 선택");
		lblPic.setBackground(new Color(255, 255, 0));
		lblPic.setBounds(12, 10, 80, 100);
		lblPic.setOpaque(true);
		contentPanel.add(lblPic);
		
		JLabel lblNid = new JLabel("학번:");
		lblNid.setBounds(148, 10, 57, 15);
		contentPanel.add(lblNid);
		
		tfNid = new JTextField();
		tfNid.setEditable(false);
		tfNid.setEnabled(false);
		tfNid.setBounds(217, 7, 116, 21);
		contentPanel.add(tfNid);
		tfNid.setColumns(10);
		
		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(217, 35, 116, 21);
		contentPanel.add(tfName);
		
		JLabel lblName = new JLabel("이름:");
		lblName.setBounds(148, 38, 57, 15);
		contentPanel.add(lblName);
		
		tfMobile = new JTextField();
		tfMobile.setColumns(10);
		tfMobile.setBounds(217, 63, 116, 21);
		contentPanel.add(tfMobile);
		
		JLabel lblMobile = new JLabel("전화번호:");
		lblMobile.setBounds(148, 66, 57, 15);
		contentPanel.add(lblMobile);
		
		tfAddress = new JTextField();
		tfAddress.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					tfEmail.requestFocus(); 
				}
			}
		});
		tfAddress.setColumns(10);
		tfAddress.setBounds(81, 120, 252, 21);
		contentPanel.add(tfAddress);
		
		JLabel lblAddress = new JLabel("주소:");
		lblAddress.setBounds(12, 123, 57, 15);
		contentPanel.add(lblAddress);
		
		tfEmail = new JTextField();
		tfEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(tfEmail.getText().matches("\\w+@\\w+\\.\\w+(\\.\\w+)?")) //정규표현식
						tfBirth.requestFocus();
					else {
						tfEmail.setSelectionStart(0);
						tfEmail.setSelectionEnd(tfEmail.getText().length());						
					}
				}
			}
		});
		tfEmail.setColumns(10);
		tfEmail.setBounds(81, 148, 252, 21);
		contentPanel.add(tfEmail);
		
		JLabel lblEmail = new JLabel("이메일:");
		lblEmail.setBounds(12, 151, 57, 15);
		contentPanel.add(lblEmail);
		
		tfBirth = new JTextField();
		tfBirth.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(tfBirth.getText().matches("^\\d{4}-([1-9]|1[0-2])-([1-9]|1[0-9]|2[0-9]|3[0-1])")) //정규표현식
						btnFriendshipUpdate.requestFocus();
					else {
						JOptionPane.showMessageDialog(null, "날짜형식이 잘못 되었습니다\n2023-02-20");
						tfBirth.setSelectionStart(0);
						tfBirth.setSelectionEnd(tfBirth.getText().length());						
					}
				}
			}
		});
		tfBirth.setColumns(10);
		tfBirth.setBounds(81, 211, 116, 21);
		contentPanel.add(tfBirth);
		
		JLabel lblGradYear = new JLabel("졸업년도:");
		lblGradYear.setBounds(12, 182, 57, 15);
		contentPanel.add(lblGradYear);
		
		JLabel lblBirth = new JLabel("생일:");
		lblBirth.setBounds(12, 213, 57, 15);
		contentPanel.add(lblBirth);
		
		ckSLType = new JCheckBox("양력");
		ckSLType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED)
					ckSLType.setText("음력");
				else
					ckSLType.setText("양력");
			}
		});
		ckSLType.setBounds(194, 210, 57, 23);
		contentPanel.add(ckSLType);
		
		cbGradYear = new JComboBox();
		cbGradYear.setBounds(81, 182, 115, 20);
		Calendar today = Calendar.getInstance();
		int lastYear = today.get(Calendar.YEAR);
		for(int y=1990; y <= lastYear; y++)
			cbGradYear.addItem(y);
		cbGradYear.setSelectedItem(lastYear);
		
		contentPanel.add(cbGradYear);
		
		JButton btnAddressFinder = new JButton("찾기...");
		btnAddressFinder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String doro = JOptionPane.showInputDialog("도로명 입력:");
				WinDoroList winDoroList = new WinDoroList(doro);
				winDoroList.setModal(true);
				winDoroList.setVisible(true);
				tfAddress.setText(winDoroList.getAddress());
				tfAddress.requestFocus();  // 커서를 주는 이유는 상세 주소를 입력하기 위해서
			}
		});
		btnAddressFinder.setBounds(345, 119, 97, 23);
		contentPanel.add(btnAddressFinder);
		
		JButton btnCalendar = new JButton("선택...");
		btnCalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinCalendar winCalendar = new WinCalendar();
				winCalendar.setModal(true);
				winCalendar.setVisible(true);
				tfBirth.setText(winCalendar.getDate());
			}
		});
		btnCalendar.setBounds(253, 210, 80, 23);
		contentPanel.add(btnCalendar);
		
		btnFriendshipUpdate = new JButton("변경");
		btnFriendshipUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tfNid.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "학번을 확인하세요");
					tfNid.requestFocus();
				}else if(tfName.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "이름을 확인하세요");
					tfName.requestFocus();
				}else if(!tfMobile.getText().matches("^\\d{2,3}-\\d{3,4}-\\d{4}")) {
					JOptionPane.showMessageDialog(null, "전화번호를 확인하세요");
					tfMobile.requestFocus();
				}else if(tfAddress.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "주소를 확인하세요");
					tfAddress.requestFocus();
				}else if(!tfEmail.getText().matches("\\w+@\\w+\\.\\w+(\\.\\w+)?")) {
					JOptionPane.showMessageDialog(null, "이메일를 확인하세요");
					tfEmail.requestFocus();
				}else if(!tfBirth.getText().matches("^\\d{4}-([1-9]|1[0-2])-([1-9]|1[0-9]|2[0-9]|3[0-1])")) {
					JOptionPane.showMessageDialog(null, "생년월일을 확인하세요");
					tfBirth.requestFocus();
				}else if(filePath.equals("") ) {
					JOptionPane.showMessageDialog(null, "이미지 선택을 확인하세요");
					
				}else {
					updateRecord();// DB 처리(추가)
					dispose();
				}
			}
		});
		btnFriendshipUpdate.setBounds(345, 178, 97, 55);
		contentPanel.add(btnFriendshipUpdate);
	}	

	protected void updateRecord() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");	
			
			//=============================================		
			String sql = "update addrBookTBL set name=?, mobile=?, address=?, email=?,";
			sql = sql + "pic=?, gradYear=?, birth=?, sltype=? where nid=" + tfNid.getText() ;			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, tfName.getText());
			pstmt.setString(2, tfMobile.getText());
			pstmt.setString(3, tfAddress.getText());
			pstmt.setString(4, tfEmail.getText());
			pstmt.setString(5, filePath);
			pstmt.setInt(6, (int)cbGradYear.getSelectedItem());
			pstmt.setString(7, tfBirth.getText());
			if(ckSLType.isSelected())
				pstmt.setInt(8, 0); // 음력
			else
				pstmt.setInt(8, 1); // 양력
			
			pstmt.executeUpdate();			
			pstmt.close();
			//==============================================
			con.close();
		} catch (ClassNotFoundException e1) {
			System.out.println("JDBC 드라이버 로드 에러");
		} catch (SQLException e1) {
			System.out.println("DB 연결 오류");
		} 		
		dispose();
	}

	public WinFriendshipDetailUpdate(String sNid) {
		this(); // 생성자(매개변수가 없는) 호출 => 컴포넌트가 배치
		showRecord(sNid);
	}

	protected void showRecord(String sNid) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");	
			
			//=============================================		
			
			String sql = "";
			if(sNid.equals(""))
				sql = "select * from addrBookTBL"; 
			else
				sql = "select * from addrBookTBL where nid=" + sNid ;
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				tfNid.setText(sNid);
				tfName.setText(rs.getString("name"));
				tfMobile.setText(rs.getString("mobile"));
				tfAddress.setText(rs.getString("address"));
				tfEmail.setText(rs.getString("email"));
				cbGradYear.setSelectedItem(rs.getString("gradYear"));
				tfBirth.setText(rs.getString("birth"));
				if(rs.getInt("SLType") == 0) {
					ckSLType.setSelected(true);
					ckSLType.setText("음력");
				}else {
					ckSLType.setSelected(false);
					ckSLType.setText("양력");
				}
				filePath = rs.getString("pic");
				ImageIcon icon = new ImageIcon(filePath);
				Image img = icon.getImage();
				img = img.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
				ImageIcon image = new ImageIcon(img);
				lblPic.setIcon(image);
			}				
			
			stmt.close();
			//==============================================
			con.close();
		} catch (ClassNotFoundException e1) {
			System.out.println("JDBC 드라이버 로드 에러");
		} catch (SQLException e1) {
			System.out.println("DB 연결 오류");
		} 
	}

	protected boolean isInteger(String sNumber) {// 숫자면 true, 숫자가 아니면 false 반환하는 함수  isInteger		
		String regExp = "^[0-9]+$";
		if(sNumber.matches(regExp))
			return true;
		else 
			return false;
	}
}
