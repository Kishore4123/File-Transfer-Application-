package networking;



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer; // <-- 1. Import Consumer

/**
 * Server class to handle file receiving logic.
 * This is designed to be run on a background thread by the GUI.
 */
public class Server {

    private static final int SERVER_PORT = 9999;

    public void receiveFile(File fileToSave, Consumer<String> statusConsumer) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            
            // This is a "blocking" call.
            Socket clientSocket = serverSocket.accept();
            
            // <-- 3. Send a status update
            statusConsumer.accept("Client connected. Receiving data...");

            try (
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                FileOutputStream fos = new FileOutputStream(fileToSave);
                BufferedOutputStream bos = new BufferedOutputStream(fos)
            ) {

                String fileName = dis.readUTF();
                
                // <-- 4. Send a status update
                statusConsumer.accept("Receiving file: " + fileName);

                long fileSize = dis.readLong();
                long totalBytesRead = 0;

                byte[] buffer = new byte[4096];
                int bytesRead;

                while (totalBytesRead < fileSize && (bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize - totalBytesRead))) != -1) {
                    bos.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }

                bos.flush();
                
                // We no longer print here. The home.java's "done()" method
                // will handle the "Success" message.
            }
        }
    }
}