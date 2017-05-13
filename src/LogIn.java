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
public class LogIn extends JFrame implements ActionListener , WindowListener{
	TextField nametf = new TextField(8);
	TextField pwdtf = new TextField(8);
	String username, upwd, dbpwd;
	JLabel urname = new JLabel("                �û�����");
	JLabel pwd1 = new JLabel("                ����     ��");
	JButton login = new JButton("��½");
	JButton cancel = new JButton("ȡ��");
	JButton register = new JButton("û���û��������ע�ᡣ");
	Connection conn = null;
	//LogIn log = new LogIn();
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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

	public void launchframe() {
		setTitle("��½");
		setSize(500, 500);
		setLocation(500, 500);
		setLayout(new GridLayout(4, 2));

		add(urname);
		add(nametf);

		add(pwd1);
		pwdtf.setEchoChar('*');
		add(pwdtf);
        
		login.addActionListener(this);
		cancel.addActionListener(this);
		register.addActionListener(this);
		
		add(login);
		add(cancel);

		add(register);
        addWindowListener (this);
		pack();
		setVisible(true);
      
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		public void run() {
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			try {
			
				UIManager
						.setLookAndFeel(new org.jvnet.substance.skin.SubstanceAutumnLookAndFeel());
				} catch (Exception e) {
	
				e.printStackTrace();
			}
		//---------------����һ���ͻ�����Ϸ���档
			LogIn log = new LogIn();
			new LogIn().launchframe();
		}
	});
	}

	public String getUname(){
		return username;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {
			LogIn log = new LogIn();
			conn = log.getConn();
			username = nametf.getText();
			upwd = pwdtf.getText();
			// String sql ="insert into userlist values(uname,pwd)";
			String sql = "select pwd from userlist where uname ="+"'"+username+"'" ;
			Statement stmt = null;
			try {
				stmt = conn.createStatement();
				System.out.println("1");
				ResultSet res = stmt.executeQuery(sql);
				System.out.println("2");
				while(res.next()){dbpwd = res.getString(1);}
				
				
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			if (upwd.equals(dbpwd)) {
				System.out.println("��½�ɹ���");
			
				JOptionPane.showMessageDialog(getContentPane(), "��½�ɹ�����",
						"��½��Ϣ", JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
			}else{
				JOptionPane.showMessageDialog(getContentPane(), "�������",
					"��½��Ϣ", JOptionPane.INFORMATION_MESSAGE);
				
			}
			// try {
			// stmt = conn.createStatement();
			// stmt.executeUpdate(sql);
			//
			// } catch (SQLException e1) {
			//
			// e1.printStackTrace();
			// }

		} else if (e.getSource() == register) {
			Register rg = new Register();//ע�����
			rg.register();
			this.setVisible(false);
 // this.dispose();
		}
		else if(e.getSource() ==cancel){
			pwdtf.setText("");
			nametf.setText("");
		}

	}
	//���ڼ�����ת���ɲ�ͬ���ڡ�
	public void windowClosed(WindowEvent e){
//		if(e.getSource().equals(register)){
//			Register rg = new Register();//ע�����
//		rg.register();
//		}
//		else{
			//ChatClient cc = new ChatClient(getUname());//��Ϸ����
			//���ߣ�
			ChatClient cc = new ChatClient(username);
	
		}
	//}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
