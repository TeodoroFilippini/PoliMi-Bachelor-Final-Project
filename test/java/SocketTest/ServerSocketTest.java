package SocketTest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import model.Costanti;

public class ServerSocketTest implements Runnable{

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	public ServerSocketTest(){
		try {
			serverSocket = new ServerSocket(Costanti.PORTA_SERVER);
			serverSocket.setReuseAddress(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			clientSocket = serverSocket.accept();
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			input = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public ObjectInputStream getInput() {
		return input;
	}
	 public void chiudiServer(){
		 try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
}
