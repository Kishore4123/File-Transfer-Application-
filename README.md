📁 Java Socket File Transfer
A simple and lightweight desktop application for transferring files between two computers on a local network. Built entirely in Java using Swing for the GUI and Java Sockets for the core networking.

🌟 Features
Peer-to-Peer Transfer: Send files directly from one computer (Client) to another (Server).

Simple GUI: An intuitive and minimal interface built with Java Swing.

IP Utility: A "GET IP ADDRESS" button to quickly find the server's local IPv4 address.

Cross-Platform: Runs on any machine with the Java Runtime Environment (JRE) installed.

📸 Demo
Here is the application's simple user interface.

(I recommend you record a short GIF of your video and add it here! For now, a screenshot is great.)

Application Workflow
Server (Receiver) starts the app and clicks "GET IP ADDRESS".

Client (Sender) starts the app and clicks "Send File (Upload)".

Client selects a file and enters the Server's IP address.

The file is sent, and success messages appear on both machines.

🛠️ How It Works: Architecture
This application uses a basic Client-Server architecture based on Java's Socket and ServerSocket.

Server (Receiver):

The application starts and immediately creates a ServerSocket on a specific port.

It then waits (listens) for a client to connect using serverSocket.accept(). This is done on a separate thread so the GUI doesn't freeze.

When a client connects, it establishes a Socket connection and prepares a FileOutputStream to save the incoming file.

It reads the file data (as a stream of bytes) from the socket and writes it to the local disk.

Client (Sender):

The user selects a file, which opens a FileInputStream.

The user provides the Server's IP address.

The Client creates a Socket and connects to the Server's IP and port.

It writes the file data (as a stream of bytes) into the socket's OutputStream.

Once finished, it closes the connection.

Diagram: Connection & Transfer Flow
Here is a sequence diagram showing exactly what happens when you send a file, just like in your video:

Code snippet

sequenceDiagram
    participant UserA as (Server User)
    participant Server
    participant UserB as (Client User)
    participant Client

    UserA->>Server: 1. Clicks "GET IP ADDRESS"
    Server-->>UserA: 2. Displays "Your IP is 10.x.x.x"
    Note over Server: ServerSocket is waiting for connection...

    UserA-->>UserB: 3. Shares IP (Manually)

    UserB->>Client: 4. Clicks "Send File (Upload)"
    Client->>UserB: 5. Shows File Chooser
    UserB-->>Client: 6. Selects file
    Client->>UserB: 7. Asks for Server IP
    UserB-->>Client: 8. Enters Server IP

    Client->>Server: 9. Initiates Socket Connection
    Server-->>Client: 10. Accepts Connection
    
    Note over Client,Server: Connection established!

    Client->>Server: 11. Sends File Data (Header + Bytes)
    Server->>Server: 12. Reads byte stream and saves to file
    
    Server-->>Client: 13. (Optional) Sends Success Acknowledgment
    Client-->>UserB: 14. Shows "File sent successfully!"
    Server-->>UserA: 15. Shows "File received and saved!"
(This diagram is written in Mermaid and will render automatically on GitHub.)

🚀 How to Use
To run this application, you need two computers on the same network (e.g., connected to the same Wi-Fi).

Prerequisites
Java Runtime Environment (JRE) or Java Development Kit (JDK) 8 or higher.

Steps
Download: Clone this repository or download the YourApp.jar file from the Releases page.

Run the Server (Receiver):

On the computer that will receive the file, run the application (e.g., double-click the .jar or run java -jar YourApp.jar).

The app will show "Waiting for a client to connect...".

Click the "GET IP ADDRESS" button.

Note down this IPv4 address (e.g., 192.168.1.10).

Run the Client (Sender):

On the computer that will send the file, run the same application.

Click the "Send File (Upload)" button.

A file chooser will open. Select the file you want to send.

An input box will appear. Type the Server's IP address you noted in Step 2.

Click "OK".

Done!

The Client will show a "File sent successfully!" message.

The Server will show a "File received and saved to: [File Path]" message.

💻 Technologies Used
Java: Core programming language.

Java Swing: For building the graphical user interface (GUI).

Java Sockets (java.net.Socket, java.net.ServerSocket): For the low-level network communication.

Java IO Streams (FileInputStream, FileOutputStream): For reading and writing the file data.

📈 Future Improvements
This project is a great foundation. Here are some ideas for taking it to the next level:

[ ] "Save As" Dialog: Allow the server (receiver) to choose where to save the incoming file.

[ ] "Get File" Functionality: Implement the "Save File As (Get)" button to allow the client to download a file from the server.

[ ] Transfer Progress Bar: Show a visual progress bar for large file transfers.

[ ] Network Discovery (Zero-config): Use UDP broadcasting so the Client can automatically find the Server on the network without needing to type an IP.

[ ] Send Folders: Add the ability to zip and send an entire directory.
