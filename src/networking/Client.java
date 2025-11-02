package networking;

import java.io.*;
import java.net.Socket;

public class Client {

    private static final int SERVER_PORT = 9999;

    public void sendFile(File fileToSend, String serverIpAddress) throws IOException {


        try (

            Socket socket = new Socket(serverIpAddress, SERVER_PORT);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            FileInputStream fis = new FileInputStream(fileToSend);
            BufferedInputStream bis = new BufferedInputStream(fis);
        ) {
            
           
            dos.writeUTF(fileToSend.getName());

           
            dos.writeLong(fileToSend.length());

            
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
             
                dos.write(buffer, 0, bytesRead);
            }
            
            dos.flush();
            
            

        }
        
    }
}