package gr.aueb.mscis.tracker;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;




public class ChordTracker {
        public static Set<TrackerRecord> setOfNodes = new HashSet<TrackerRecord>();
        private static int serverPort = 8081;

        public static void main(String[] args) throws InterruptedException, IOException {
        	ServerSocket serverSocket = null;
            boolean listening = true;
        	
        	try {
                serverSocket = new ServerSocket(serverPort);
                System.out.println("Tracker Started");
            } catch (IOException e) {
                System.err.println("Could not listen on port: "+serverPort+".");
                System.exit(-1);
            }

            while (listening)
    	    new ChordTrackerThread(serverSocket.accept()).start();
            
            serverSocket.close(); 
            System.out.println("closed");
        }
}