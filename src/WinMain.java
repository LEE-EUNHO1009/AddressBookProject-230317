import java.awt.EventQueue;

import javax.swing.JDialog;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WinMain extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinMain dialog = new WinMain();
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
	public WinMain() {		
		setResizable(false);
		setTitle("동창회 주소록 관리 프로그램");
		setBounds(100, 100, 246, 232);
		getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnFriendshipInsert = new JButton("");
		btnFriendshipInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				WinFriendshipInsert winFriendshipInsert = new WinFriendshipInsert();
				winFriendshipInsert.setModal(true);
				winFriendshipInsert.setVisible(true);				
			}
		});
		btnFriendshipInsert.setIcon(new ImageIcon(WinMain.class.getResource("/images/memberAdd.png")));
		getContentPane().add(btnFriendshipInsert);
		
		JButton btnFriendshipDelete = new JButton("");
		btnFriendshipDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinFriendshipDelete winFriendshipDelete = new WinFriendshipDelete();
				winFriendshipDelete.setModal(true);
				winFriendshipDelete.setVisible(true);
			}
		});
		btnFriendshipDelete.setIcon(new ImageIcon(WinMain.class.getResource("/images/memberRemove.png")));
		getContentPane().add(btnFriendshipDelete);
		
		JButton btnFriendshipUpdate = new JButton("");
		btnFriendshipUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinFriendshipUpdate winFriendshipUpdate = new WinFriendshipUpdate();
				winFriendshipUpdate.setModal(true);
				winFriendshipUpdate.setVisible(true);
			}
		});
		btnFriendshipUpdate.setIcon(new ImageIcon(WinMain.class.getResource("/images/memberUpdate.png")));
		getContentPane().add(btnFriendshipUpdate);
		
		JButton btnFriendshipSelect = new JButton("");
		btnFriendshipSelect.setIcon(new ImageIcon(WinMain.class.getResource("/images/memberSearch.png")));
		getContentPane().add(btnFriendshipSelect);
		
		JButton btnTotalMoney = new JButton("");
		btnTotalMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinCostReunion winCostReunion = new WinCostReunion();
				winCostReunion.setModal(true);
				winCostReunion.setVisible(true);
			}
		});
		btnTotalMoney.setIcon(new ImageIcon(WinMain.class.getResource("/images/money.png")));
		getContentPane().add(btnTotalMoney);
		
		JButton btnShow = new JButton("");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMoneyShow winMoneyShow = new WinMoneyShow();
				winMoneyShow.setModal(true);
				winMoneyShow.setVisible(true);
			}
		});
		btnShow.setIcon(new ImageIcon(WinMain.class.getResource("/images/moneyShow.png")));
		getContentPane().add(btnShow);

	}

}
