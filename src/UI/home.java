package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import networking.Client; 
import java.io.IOException;
import networking.Server;
import javax.swing.SwingWorker;
import java.util.List;

import ipAddress.ipv4;

public class home extends JFrame {

   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel fileStatusLabel;
    private JButton sendButton;
    private JButton saveButton;

  
    private JLabel ipLabel;
    private JButton getIpButton;

    public home() {
        setTitle("File Transfer Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        sendButton = new JButton("Send File (Upload)");
        saveButton = new JButton("Save File As (Get)");
        fileStatusLabel = new JLabel("Hey There! Choose an action.");
        fileStatusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fileStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        getIpButton = new JButton("GET IP ADDRESS");
        ipLabel = new JLabel("Click 'GET IP' to see your address");
        ipLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);

        buttonPanel.add(sendButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(getIpButton);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(home.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

           
                    String serverIp = JOptionPane.showInputDialog(
                        home.this,
                        "Enter the Server's IP Address:",
                        "192.168.1.10"
                    );

                    if (serverIp != null && !serverIp.trim().isEmpty()) {
                        try {
               
                            Client client = new Client();
                            client.sendFile(selectedFile, serverIp.trim());

     
                            fileStatusLabel.setText("File sent successfully!");
                            JOptionPane.showMessageDialog(home.this, 
                                "File sent successfully!", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                        } catch (IOException ex) {

                            fileStatusLabel.setText("Error sending file: " + ex.getMessage());
                            JOptionPane.showMessageDialog(home.this,
                                "Error sending file:\n" + ex.getMessage(),
                                "Connection Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

       
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(home.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    fileStatusLabel.setText("Waiting for a client to connect...");
                    saveButton.setEnabled(false);
                    sendButton.setEnabled(false);

             
                    SwingWorker<Void, String> serverWorker = new SwingWorker<Void, String>() {
                        
                        private String errorMessage = null;

                        @Override
                        protected Void doInBackground() throws Exception {
                            try {
                                Server server = new Server();
                                
                          
                                server.receiveFile(fileToSave, (message) -> {
                                    publish(message); // Sends the message to the process() method
                                });

                            } catch (IOException ex) {
                                this.errorMessage = ex.getMessage();
                            }
                            return null;
                        }


                        @Override
                        protected void process(List<String> chunks) {
                           
                            String latestMessage = chunks.get(chunks.size() - 1);
                            fileStatusLabel.setText(latestMessage); // Update the label
                        }

                        @Override
                        protected void done() {
                     
                            saveButton.setEnabled(true);
                            sendButton.setEnabled(true);

                            if (errorMessage == null) {
                              
                                fileStatusLabel.setText("File received successfully!");
                                JOptionPane.showMessageDialog(home.this,
                                    "File received and saved to:\n" + fileToSave.getAbsolutePath(),
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                       
                                fileStatusLabel.setText("Error receiving file: " + errorMessage);
                                JOptionPane.showMessageDialog(home.this,
                                    "Error receiving file:\n" + errorMessage,
                                    "Connection Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    };
                    
                    serverWorker.execute();
                }
            }
        });


        getIpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
                String ip = ipv4.getLocalIPv4();
            
                ipLabel.setText("Your IPv4 Address is: " + ip);
            }
        });

       
        add(buttonPanel, BorderLayout.NORTH);     
        add(fileStatusLabel, BorderLayout.CENTER); 
        add(ipLabel, BorderLayout.SOUTH);       
    }

    public static void main(String[] args) {
      
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new home().setVisible(true);
            }
        });
    }
}