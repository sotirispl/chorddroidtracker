package gr.aueb.mscis.tracker;

import gr.aueb.mscis.protocol.NodeIdentifier;
import gr.aueb.mscis.protocol.ProtocolType;
import gr.aueb.mscis.protocol.RemoteMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;


public class ChordTrackerThread extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private Socket socket = null;

	public ChordTrackerThread(Socket socket) {
		super("ChordTrackerThread");
		this.socket = socket;
	}

	/*mazi me tin ip, lambanetai kai to port pou 8a akouei o komvos*/
	private RemoteMessage checkForNodes(Socket client, NodeIdentifier nodeId) throws UnknownHostException {
		
		RemoteMessage response = null;

		TrackerRecord tr = new TrackerRecord(client.getInetAddress(), nodeId.getPort());

		/*komvos uparxei idi sto ring*/
		if (ChordTracker.setOfNodes.contains(tr)) {
			
			System.out.println("Node "+client.getInetAddress()+ ":"+nodeId.getPort() +"already in ring");
			/*dimiourgia minimatos oti uparxei idi*/
			response = new RemoteMessage(ProtocolType.ALREADY_IN_RING,null,null);
			return response;
			
		}

		/*o komvos den uparxei sto ring kai uparxoun komvoi katagegramenoi ston tracker*/
		if (!ChordTracker.setOfNodes.isEmpty()) {
			TrackerRecord returnRecord = null;
			
			Random rand = new Random();
		    int num = rand.nextInt(ChordTracker.setOfNodes.size());
		    System.out.println("size is "+ChordTracker.setOfNodes.size() + " num is "+num);
		    returnRecord = (TrackerRecord) ChordTracker.setOfNodes.toArray()[num];
			ChordTracker.setOfNodes.add(tr);
			response = new RemoteMessage(ProtocolType.JOIN_RING, null, new NodeIdentifier(returnRecord.getAddr(), returnRecord.getPort()));
			//response = new RemoteMessage(ProtocolType.JOIN_RING,null ,new NodeIdentifier(InetAddress.getByName("192.168.1.2"), 8985));
	
			return response;			
		}
			
			
			
			
			
//			TrackerRecord returnRecord = null;
//
//			int randomNodeId = (int) Math.round(Math.random() * ChordTracker.setOfNodes.size());
//			System.out.println("id: " + randomNodeId);
//
//			TrackerRecord randomNode = ChordTracker.setOfNodes.iterator().next();
//	
//			returnRecord = randomNode;
//			/*pros8iiki tou komvou ston pinaka tou tracker*/
//			ChordTracker.setOfNodes.add(tr);
//			/*apostoli epilegmenou komvou gia na kanei join mesw autou*/
//			response = new RemoteMessage(ProtocolType.JOIN_RING, new NodeIdentifier(returnRecord.getAddr(),
//							returnRecord.getPort()), new NodeIdentifier(
//							tr.getAddr(), tr.getPort()));
//			return response;
//		}
//		/*alliws den uparxei allos komvos opote 8a kanei create autos*/
		else {
			ChordTracker.setOfNodes.add(tr);
			response = new RemoteMessage(ProtocolType.CREATE_RING, null,
					new NodeIdentifier(tr.getAddr(), tr.getPort()));
			return response;
		}
	}


	private void sendReply(Socket connectionSocket, RemoteMessage replyMsg) {
		try {
			oos = new ObjectOutputStream(connectionSocket.getOutputStream());
			oos.writeObject(replyMsg);
			oos.flush();
			System.out.println("Reply-type-sent: "+ replyMsg.getProtocolType().toString());
			connectionSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
				Socket inFromClient = socket;
				System.out.println("Node "+socket.getInetAddress() +":"+socket.getPort()+" connected");
				ois = new ObjectInputStream(inFromClient.getInputStream());
				RemoteMessage clientMsg = (RemoteMessage) ois.readObject();
				System.out.println("client msg: " + clientMsg.getProtocolType().toString());
				if (clientMsg.getProtocolType().equals(ProtocolType.REGISTER)) {
					RemoteMessage msg = checkForNodes(inFromClient,clientMsg.getNodeId());
					sendReply(inFromClient, msg);
				}
		} catch (SocketException e) {
			System.out.println("Node "+socket.getInetAddress() +":"+socket.getPort()+" disconnected");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();		
		}
	}

}
