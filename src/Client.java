import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class Client {
    static Path file = null;

    public static void main(String[] args) {
        try(Socket socket = new Socket("127.0.0.1", 7777);
            DataInputStream sentData = new DataInputStream(socket.getInputStream())
        ) {
            receiveFile(sentData);
            System.out.println("File downloaded!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void receiveFile(DataInputStream dataInputStream) {
        try {
            System.out.println("Downloading your file...");
            file = Path.of( "tcpdownloaded-" + dataInputStream.readUTF());

            FileOutputStream fileOutputStream = new FileOutputStream(file.toFile());
            long size = dataInputStream.readLong();

            byte[] buffer = new byte[4096];
            int data = 0;

            while(size > 0 && (data = dataInputStream.read(buffer, 0, (int) Math.min(size, buffer.length))) != -1) {
                fileOutputStream.write(buffer, 0, data);
                size -= data;
            }

            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
