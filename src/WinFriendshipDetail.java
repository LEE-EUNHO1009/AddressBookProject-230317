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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WinFriendshipDetail extends JDialog {

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
	private JButton btnFriendshipDelete;
	private JLabel lblPic;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinFriendshipDetail dialog = new WinFriendshipDetail();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinFriendshipDetail() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
			}
		});
		setResizable(false);
		setTitle("친구 삭제");
		setBounds(100, 100, 369, 291);
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
		tfAddress.setColumns(10);
		tfAddress.setBounds(81, 120, 252, 21);
		contentPanel.add(tfAddress);
		
		JLabel lblAddress = new JLabel("주소:");
		lblAddress.setBounds(12, 123, 57, 15);
		contentPanel.add(lblAddress);
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setBounds(81, 148, 252, 21);
		contentPanel.add(tfEmail);
		
		JLabel lblEmail = new JLabel("이메일:");
		lblEmail.setBounds(12, 151, 57, 15);
		contentPanel.add(lblEmail);
		
		tfBirth = new JTextField();
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
		ckSLType.setBounds(194, 210, 55, 23);
		contentPanel.add(ckSLType);
		
		cbGradYear = new JComboBox();
		cbGradYear.setBounds(81, 182, 115, 20);
		Calendar today = Calendar.getInstance();
		int lastYear = today.get(Calendar.YEAR);
		for(int y=1990; y <= lastYear; y++)
			cbGradYear.addItem(y);
		cbGradYear.setSelectedItem(lastYear);
		
		contentPanel.add(cbGradYear);
		
		btnFriendshipDelete = new JButton("삭제");
		btnFriendshipDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ret = JOptionPane.showConfirmDialog(null, "정말 삭제할까요?", "삭제", 
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
				if(ret == JOptionPane.OK_OPTION) {				
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = 
								DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");
						//=============================================		
						String sql = "delete from addrBookTBL where nid=" + tfNid.getText();
						System.out.println(sql);
						Statement stmt = con.createStatement();
						if(stmt.executeUpdate(sql) > 0)
							dispose();
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
		});
		btnFriendshipDelete.setBounds(253, 177, 80, 55);
		contentPanel.add(btnFriendshipDelete);
	}

	public WinFriendshipDetail(String sNid) {
		// TODO Auto-generated constructor stub
		this();
		showRecord(sNid);
	}

	protected void showRecord(String sNid) {
		// DB Query : select * from addrBookTBL where nid=??
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB", "root","1234");
			//=============================================		
			String sql = "select * from addrBookTBL where nid=" + sNid;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				tfNid.setText(sNid);
				tfName.setText(rs.getString("Name"));
				tfMobile.setText(rs.getString("mobile"));
				tfAddress.setText(rs.getString("address"));
				tfEmail.setText(rs.getString("email"));
				cbGradYear.setSelectedItem(rs.getInt("gradYear"));
				tfBirth.setText(rs.getString("birth"));
				if(rs.getInt("SLType") == 1) {
					ckSLType.setSelected(false);
					ckSLType.setText("양력");
				}else {
					ckSLType.setSelected(true);
					ckSLType.setText("음력");
				}
				String filePath = rs.getString("pic");
				ImageIcon icon = new ImageIcon(filePath);
				Image img = icon.getImage();
				img = img.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
				ImageIcon image = new ImageIcon(img);
				lblPic.setIcon(image);
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
