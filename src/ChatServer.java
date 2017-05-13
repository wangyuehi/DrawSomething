import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChatServer  extends JFrame{
	static int i = 0;
	boolean started = false;
	ServerSocket ss = null;
	String uname;
	List<Client> clients = new ArrayList<Client>();
	Connection conn = null;
	ChatServer server;
    String key = null;
    TextField tf = new TextField();
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.start();

	}
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ChatServer(){
		 
		ImageIcon img = new ImageIcon("/bg/textback.jpg");//这是背景图片 
		JLabel imgLabel = new JLabel(img);//将背景图放在标签里。 
		this.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));//注意这里是关键，将背景标签添加到jfram的LayeredPane面板里。 
		imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());//设置背景标签的位置 
	    this.setLayout(new BorderLayout ());
	
		JButton add1 = new JButton("添加词语");
		add(add1,BorderLayout.SOUTH);
		add(tf,BorderLayout.NORTH);
		add1.addActionListener(new Action());

      setVisible(true);
}
	public class Action implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {

		//	ChatServer cs = new ChatServer();
			
			conn = server.getConn();

			String word = tf.getText();
			int x = (int) (Math.random() * 15 + 1);

			String sql = "insert into words values(" + "'" + word + "'" + "," + "'"
					+ x + "'" + ")";
			Statement stmt = null;

			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			System.out.println("3");
			JOptionPane.showMessageDialog(getContentPane(), "添加成功。", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			tf.setText("");

		
			
		}
		
	}
	public Connection getConn() {
		String url = "jdbc:mysql://localhost:3306/list";
		String usrname = "root";
		String password = "wang";
		try {

			conn = DriverManager.getConnection(url, usrname, password);
			if (conn != null) {
				System.out.println("服务器 : 数据库连接成功。！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	
	public void start() {

		
		try {
			ss = new ServerSocket(8888);
			started = true;
		} catch (BindException e) {
			System.out.println("端口使用中....");
			System.out.println("请关掉相关程序并重新运行服务器！");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (started) {
				Socket s = ss.accept();
				Client c = new Client(s);
				System.out.println("a client connected!");

				new Thread(c).start();
				clients.add(c);
				
				// dis.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class Client implements Runnable {
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;

		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void send(String str) {
			try {

				dos.writeUTF(str);
				dos.flush();
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("对方退出了！我从List里面去掉了！");
				// e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (bConnected) {
					String str = dis.readUTF();
					System.out.println(str);

				if (str.equals("go")) {
						new Guess().start();
						new timeThread().start();
                        System.out.println("用户点击开始，游戏开始！");
						
					} else {

					
						if(str.equals(key)){
							
						System.out.println("有人猜对了。");
						dos.writeUTF("bingo");
						dos.flush();
						for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						c.send("已经有人猜对了，加油哦~");
						}
					}
					else if(str.equals("color")){
						String co = dis.readUTF();
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send("color");
							c.send(co);
							}
						
					}
					else{
						String other = dis.readUTF();
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send(other);
						}}
					
				}}
			} catch (EOFException e) {
				System.out.println("Client closed!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (dis != null)
						dis.close();
					if (dos != null)
						dos.close();
					if (s != null) {
						s.close();
					}
   	} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}

	}

public class Guess extends Thread {
//		String str;
//		Statement stmt = null;
//		String sql = "select *  from words";
//		ResultSet res;
	Statement stmt = null;
	int id;			
	ResultSet res;

			public void Guess(int n) {
				try {
				id =(int) (Math.random()*10);
			
				System.out.println(id);
			String sql = "select * from words where id = "+" "+id;
					server = new ChatServer();
					conn = server.getConn();
					stmt = conn.createStatement();
		System.out.println("数据库进入，随机选词。");
				    res = stmt.executeQuery(sql);
			
					try {
						
						while(res.next()){	
							key = res.getString("keys");
							System.out.println("已选择词语。"+key);
							
							}
					Client c1 = clients.get(n);   
		
					c1.send(" ++++++现在由你画图"+"词语："+key+"++++++ ");	
		
					
					} catch (SQLException e) {
				e.printStackTrace();
					}
				} catch (SQLException e1) {

					e1.printStackTrace();
				}

			for (int x = 0; x < i; x++) {
				Client c1 = clients.get(x);
				c1.send(" ++++++现在有你来猜。++++++ ");

			}for (int x = i+1; x < clients.size(); x++) {
				Client c1 = clients.get(x);
				c1.send(" ++++++现在由你来猜。++++++ ");

			}
			
		}

		@Override
		public void run() {

			Guess(i);
			if (i < clients.size()-1) {
				
				i++;
			//	System.out.println("现在i等于"+i);
			}
			else{
				i=0;
				
			}
		}

	}
class timeThread extends Thread {
	@SuppressWarnings("deprecation")
	public void run() {
		int k = 20;
		for (int y = k; y >= 0; y--) {
			if (y != 0) {

				String s = y + "秒";
				for (int x = 0; x < clients.size(); x++) {

				
					Client c =clients.get(x);
					c.send("time");
					c.send(s);
				}
				try {
					sleep(1000);
				} catch (InterruptedException e1) {
					System.out.println("休眠失败1");
				}
			}

			else if (y == 0) {

				for (int x = 0; x < clients.size(); x++) {
					Client c =clients.get(x);
					c.send("time");
					c.send("本轮结束。");
				}

			}

		}
	}
}

}
