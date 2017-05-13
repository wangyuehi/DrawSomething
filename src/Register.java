import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;

import javax.swing.*;
/*
 * 
 *                  ɽ��2012�� ���8��  ���h
 * 
 * 
                                                                   */
public class Register extends JFrame implements WindowListener, ActionListener {
	TextField nametf = new TextField(8);
	TextField pwdtf1 = new TextField(8);
	TextField pwdtf2 = new TextField(8);
	JButton cancel;
	JButton ok;
	Connection conn;
	String uname, pwd;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		String url = "jdbc:mysql://localhost:3306/list";
		String usrname = "root";
		String password = "wang";
		try {
			conn = DriverManager.getConnection(url, usrname, password);
			if (conn != null) {
				System.out.println("���ݿ����ӳɹ�����");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public void register() {
		setTitle("ע��");
		setSize(300, 400);
		setLocation(500, 500);
		setLayout(new GridLayout(5, 2));
		add(new JLabel("�û�����"));
		add(nametf);
		add(new JLabel("�Ա�"));
		String[] sex = { "male", "female" };
		add(new JComboBox(sex));
		add(new JLabel("���룺"));
		add(pwdtf1);
		add(new JLabel("ȷ�����룺"));
		add(pwdtf2);
		cancel = new JButton("���ص�½��");
		ok = new JButton("�ύ��");
		cancel.addActionListener(this);
		ok.addActionListener(this);
		addWindowListener(this);
		add(cancel);
		add(ok);
		pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Register().register();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("0");
		if (e.getSource() == cancel) {
			this.dispose();

		} else if (e.getSource() == ok) {
			System.out.println("1");

			if (pwdtf1.getText().equals(pwdtf2.getText())) {

				Register reg = new Register();
				conn = reg.getConn();
				uname = nametf.getText();
			    pwd = pwdtf1.getText();
				
				System.out.println("2");
				String sql = "insert into userlist values("+"'"+uname+"'"+","+ "'"+pwd+"'"+")";
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					stmt.executeUpdate(sql);
					System.out.println("3");
					JOptionPane.showMessageDialog(getContentPane(), "ע��ɹ���",
							"��ʾ", JOptionPane.INFORMATION_MESSAGE);
					nametf.setText("");
					pwdtf1.setText("");
					pwdtf2.setText("");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				

			} else {
				JOptionPane.showMessageDialog(getContentPane(), "���벻һ�£����������롣",
						"����", JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	@Override
	public void windowClosed(WindowEvent e) {
		new LogIn().launchframe();

	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
