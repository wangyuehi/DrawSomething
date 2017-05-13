import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ChatClient extends JFrame {
	TextField timeTa;
	TextField scoreTa;
	int fen=0;
	String cong = "++++++恭喜你答对了！加一分+++++";
	// /下面是chat的成员变量
	Socket cs = null;
	Socket ds = null;
	DataOutputStream Cdos = null;
	DataInputStream Cdis = null;
String t;
	private boolean cbConnected = false;
	private boolean dbConnected = false;
	String uname = null;
	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();
	Thread tRecv = new Thread(new RecvThread());

	// 下面是Draw类的成员变量
	private Icon icons[];
	JButton button[];
	String[] Tool = { "white", "red", "green", "yellow", "black","small","middle","eraser" };
	private PaintCanvas pc;
	Point start, end;
	BufferedImage image= new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g =  image.createGraphics();
	BasicStroke bs = new BasicStroke(3.0f);
	BasicStroke bs2 = new BasicStroke(5.0f);
	List<Point> px = new ArrayList<Point>();
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	Thread tDraw = new Thread(new DrawThread());
	Point p1 ,p2;
	 JButton beginbtn=  new JButton("点这里开始游戏。");
	// main方法
	public static void main(String[] args) {
		// -------------------------界面美化包---------------------

			
				new ChatClient("");
			

	}

	// 下面是chat的构造方法
	public ChatClient(String uname) {

		this.uname = uname;
		this.setTitle("-----猜。你。妹-----");
		this.setLayout(new BorderLayout());
		this.setBounds(400, 300, 800, 400);
		this.add(tfTxt, BorderLayout.SOUTH);
		this.add(taContent, BorderLayout.EAST);
		taContent.setSize(300, 1000);
		// pack();
        JPanel timer  =  new JPanel();
   
		JToolBar toolbar = new JToolBar();
		toolbar = new JToolBar();
		icons = new ImageIcon[Tool.length];
		button = new JButton[Tool.length];

		for (int i = 0; i < Tool.length; i++) {
			icons[i] = new ImageIcon(getClass().getResource(
					"/icon/" + Tool[i] + ".jpg"));
			button[i] = new JButton("", icons[i]);

			button[i].setBackground(Color.white);

			button[i].addActionListener(new ColorMonitor());
		
			toolbar.add(button[i]);
		}
		
		this.add(timer, BorderLayout.NORTH);
		timer.setLayout(new BorderLayout());
		timer.add(toolbar, BorderLayout.WEST);

		timer.add(beginbtn, BorderLayout.CENTER);
		JPanel score = new JPanel();
		score.setLayout(new GridLayout(2, 2));

		timeTa = new TextField();
		scoreTa = new TextField();
		scoreTa.setEditable(false);
		timeTa.setEnabled(false);
		
		score.add(new JLabel("倒计时："));
		score.add(timeTa);
		
		score.add(new JLabel("得   分："));
		score.add(scoreTa);
		scoreTa.setText(""+fen);

		timer.add(score, BorderLayout.EAST);
		beginbtn.addActionListener(new beginListener());
		
		pc = new PaintCanvas();
		this.add(pc,BorderLayout.WEST);
		this.setVisible(true);
		this.setResizable(false);

		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent arg0) {
				disconnect();
				System.exit(0);
			}
		});
		tfTxt.addActionListener(new TFListener());
		setVisible(true);
		ChatConnect();
		DrawConnect();
		tRecv.start();
		tDraw.start();
	}

	// 下面是Draw的 方法
	class beginListener implements ActionListener{
	      
		@Override
		public void actionPerformed(ActionEvent arg0) {
	     try {
			Cdos.writeUTF("go");
			Cdos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("点了开始按钮，没有成功开始！!");
		}
		}
		
		}
	class ColorMonitor implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == button[0]) {
				g.setColor(Color.white);
				try {
					Cdos.writeUTF("color");
					Cdos.writeUTF("white");
				} catch (IOException e1) {
				e1.printStackTrace();
				}
			} else if (e.getSource() == button[1]) {
				g.setColor(Color.red);	
				try {
					Cdos.writeUTF("color");
					Cdos.writeUTF("red");
				} catch (IOException e1) {
				e1.printStackTrace();
				}
			} else if (e.getSource() == button[2]) {
				g.setColor(Color.green);
				try {
					Cdos.writeUTF("color");
					Cdos.writeUTF("green");
				} catch (IOException e1) {
				e1.printStackTrace();
				}
			} else if (e.getSource() == button[3]) {
				g.setColor(Color.yellow);
				try {
					Cdos.writeUTF("color");
					Cdos.writeUTF("yellow");
				} catch (IOException e1) {
				e1.printStackTrace();
				}
			} else if (e.getSource() == button[4]) {
				g.setColor(Color.black);
				try {
					Cdos.writeUTF("color");
					Cdos.writeUTF("black");
				} catch (IOException e1) {
				e1.printStackTrace();
				}
	        } else if (e.getSource() == button[5]) {
		  		g.setStroke(bs);
            } else if (e.getSource() == button[6]) {
				g.setStroke(bs2);
			}else if (e.getSource() == button[7]) {
				g.setStroke(bs2);
               g.setColor(getBackground());
			}

		}
	}

	// Draw的内部类，
	class PaintCanvas extends Canvas implements MouseListener,
			MouseMotionListener {

		public PaintCanvas() {
		
			g.setStroke(bs);
			g.setColor(Color.red);
			start = end = null;
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}

		public void mouseMoved(MouseEvent arg0) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent me) {
			end = new Point(me.getX(), me.getY());
			try {
				oos.writeObject(end);
				oos.flush();

			} catch (IOException e1) {
	
				e1.printStackTrace();
			}
		}

		public void mouseDragged(MouseEvent me) {
			start = end;
			end = new Point(me.getX(), me.getY());
			try {
				oos.writeObject(end);
				oos.flush();

			} catch (IOException e1) {
	
				e1.printStackTrace();
			}
			g.drawLine(start.x, start.y, end.x, end.y);
		//	px.add(start);
            
			repaint();

		}

		public void paint(Graphics g) {
	g.drawImage(image, 0, 0, this);
	repaint();

		}

		

		public void update(Graphics g) {
			super.update(g);
			paint(g);
		}

	}

	public void DrawConnect() {

		try {
			ds = new Socket("127.0.0.1", 1234);
			oos = new ObjectOutputStream(ds.getOutputStream());
			ois = new ObjectInputStream(ds.getInputStream());
			dbConnected = true;
			System.out.println("Draw connected!");

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// 下面是chat类的方法们
	public void ChatConnect() {
		try {
			cs = new Socket("127.0.0.1", 8888);
			Cdos = new DataOutputStream(cs.getOutputStream());
			Cdis = new DataInputStream(cs.getInputStream());
			System.out.println("Chat connected!");
			Cdos.writeUTF("------------" + uname + "  进入了游戏。------------");
			Cdos.writeUTF("------------" + uname + "  进入了游戏。------------");
			Cdos.flush();
			cbConnected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void disconnect() {
		try {
			Cdos.close();
			Cdis.close();
			
			oos.close();
			ois.close();
			dbConnected = false;
			cbConnected = false;
			cs.close();
			ds.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private class TFListener implements ActionListener {// 向服务器发出聊天内容。

		public void actionPerformed(ActionEvent e) {
			String str = tfTxt.getText().trim();
			// taContent.setText(str);
			tfTxt.setText("");

			try {
				Cdos.writeUTF(str);
				Cdos.writeUTF(uname + ":" + str);
				Cdos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}

	private class RecvThread implements Runnable {// 接收服务器发来的聊天信息

		public void run() {
			try {
				while (cbConnected) {
					String str = Cdis.readUTF();
					if(str.equals("time")){
						t = Cdis.readUTF();
                       timeTa.setText(t);
					}
					else if(str.equals("bingo")) {
						fen=fen+1;
						scoreTa.setText(""+fen);
						taContent.setText(taContent.getText() + cong + '\n');
					 System.out.println("+++++答对了，加一分。+++++");
					 }else if(str.equals("color")){
					String co =Cdis.readUTF();
					switch (co){
					case "white":{
						g.setColor(Color.white);
						repaint();
					}
					case "red":{
						g.setColor(Color.red);
						repaint();
					}
					case"green":{
						g.setColor(Color.green);
						repaint();
					}
					case"yellow":{
						g.setColor(Color.yellow);
						repaint();
					}	
					case"black":{
						g.setColor(Color.black);
	                    repaint();				
					}
					}
					
					
					
					 }
					else{
					taContent.setText(taContent.getText() + str + '\n');
					}
				}
			} catch (SocketException e) {
				System.out.println("退出了，bye!");
			} catch (EOFException e) {
				System.out.println("推出了，bye - bye!");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private class DrawThread implements Runnable {
		//客户端接收服务器那边穿过来的画图，接收后强转成point，然后画出来
		
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) {
				try {
		
                        if(p1==null){
                                p1 = (Point) ois.readObject();
                       }
                        else{
				            
				            	p2=(Point)ois.readObject();
				
					        	repaint();
					        	g.drawLine(p1.x, p1.y,
								        p2.x,p2.y);
				            	
				            	 p1=p2;
					            repaint();
                        }
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
				
				}

			}
		}
		

	}
	

		
}

