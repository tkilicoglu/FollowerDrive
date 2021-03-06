import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MasterConnection {

	//Default Values
	public static final String DEFAULT_SERVER_ADDRESS = "localhost";
	public static final int DEFAULT_SERVER_PORT = 9999;
	public static final int DEFAULT_DATAGRAM_PORT = 6666;
	
	//Variables
	private String masterAddress;
	private int masterPort;
	private Socket socket;
	private DatagramSocket dataSocket;
	private BufferedReader br; //Input Stream
	private PrintWriter pw; //Output Stream
	public static MasterConnection master_connection;
	
	public static MasterConnection getInstance() {
		if(master_connection == null) {
			master_connection = new MasterConnection(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT);
			return master_connection;
		} else {
			return master_connection;
		}
	}
	
	
	public MasterConnection(String address, int port) {
		masterAddress= address;
		masterPort = port;
	}
	
	
	public boolean Connect() {
		
		try {
			socket = new Socket(masterAddress, masterPort);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
			
			System.out.println("Connection Successful, address:"+masterAddress+", port :"+masterPort);
			return true;
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean Disconnect() {
		
		try {
			br.close();
			pw.close();
			socket.close();
			System.out.println("Connection Closed. Address: "+masterAddress+", port:"+masterPort);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}
	
	
	public boolean uploadFile(File f) throws IOException {
		
		
		try {
			System.out.println("File uploading... File name:"+f.getName());
			dataSocket = new DatagramSocket();
			FileInputStream fileInputStream = new FileInputStream(f);
			
			int fileSize = (int) f.length();
			
			byte[] data = new byte[fileSize];
			
			for(int i = 0; i<fileSize;i++) {
				data[i] = (byte)fileInputStream.read();
			}
			
			DatagramPacket datagramPacket = new DatagramPacket(data, data.length, Inet4Address.getLocalHost(), DEFAULT_DATAGRAM_PORT);
			dataSocket.send(datagramPacket);
			
			fileInputStream.close();
			dataSocket.close();
			System.out.println("File upload complete. File sent:"+f.getName());
			return true;
						
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Error occured during the file upload!");
		return false;
	}
	
	public boolean downloadFile(String filename, int fileSize) throws IOException {
		
		try {
			System.out.println("File downloading... File name:"+filename);
			dataSocket = new DatagramSocket();
			
			byte[] data = new byte[fileSize];
			String path = System.getProperty("user.home") + "/Desktop/GoogleDrive/"+filename;
			
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			
			DatagramPacket datagramPacket = new DatagramPacket(data, fileSize);
			
			dataSocket.receive(datagramPacket);
			
			
			
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return false;
	}
	
	
}
