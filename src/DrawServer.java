import java.awt.Point;
import java.io.*;
import java.net.*;
import java.util.*;

public class DrawServer {
	boolean started = false;
	ServerSocket ss = null;
	String uname;
	List<Client1> clients = new ArrayList<Client1>();
	
	
	public static void main(String[] args) {
		new DrawServer().start();
	}
	
	public void start() {
		try {
			ss = new ServerSocket(1234);
			started = true;
		} catch (BindException e) {
			System.out.println("�˿�ʹ����....");
			System.out.println("��ص���س����������з�������");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			while(started) {
				Socket s = ss.accept();
				Client1 c = new Client1(s);
System.out.println("Draw��a client connected!");

				new Thread(c).start();
				clients.add(c);
				//dis.close();
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
	
	class Client1 implements Runnable {
		private Socket s;
		private boolean bConnected = false;
		private ObjectInputStream ois = null;
		private ObjectOutputStream oos = null;
		public Client1(Socket s) {
			this.s = s;
			try {
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void drawAll(Object point){
			try {
				oos.writeObject(point);
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("��û����ȥ����");
			}
		}
		
		public void run() {//ѭ��ʵ�������пͻ��˷���ͼƬ
			try {
				
				while(bConnected) {
					
						
					
						try {
							
							Object points =ois.readObject();
							for(int i=0; i<clients.size(); i++) {
								
								Client1 c = clients.get(i);
								System.out.println("�������Ѿ�������"+i+"���ͻ���");
								c.drawAll(points);
								System.out.println("����һ���ɹ�");
						
							
					}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();}
						
				}
			} catch (EOFException e) {
				System.out.println("Client closed!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(ois != null) ois.close();
					if(oos != null) oos.close();
					if(s != null)  {
						s.close();
					}
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
			}
		}
		
	}
}

