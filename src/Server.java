import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class Server {
    public static void main(String[] args) {
        Path file = Path.of("dorgival.png");
        while(true) {
            try(ServerSocket serverSocket = new ServerSocket(7777);
                Socket socket = serverSocket.accept();
                DataOutputStream serverData = new DataOutputStream(socket.getOutputStream())
            ) {
                sendFile(file, serverData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void sendFile(Path file, DataOutputStream dataOutputStream) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file.toFile());
            dataOutputStream.writeUTF(String.valueOf(file.getFileName()));
            dataOutputStream.writeLong(file.toFile().length());

            byte[] buffer = new byte[4096];
            int data;

            while((data = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, data);
                System.out.println("sending data: " + data);
            }

            fileInputStream.close();
        } catch (IOException e) {
            System.err.println("File not found");
        }
    }
}