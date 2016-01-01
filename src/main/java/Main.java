import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {

  private static final int EOF = -1;

  public static void main(String[] args) throws IOException {


    ServerSocket server = new ServerSocket(8080);

    while (true) {
      System.out.println("loop");
      Socket socket = server.accept();
      InputStream inputStream = socket.getInputStream();

       String str = convertInputStream(inputStream);
       System.out.println(str);
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write("accepted".getBytes());
      System.out.println("wrote to output stream");
      socket.close();

    }

  }

  public static String convertInputStream(InputStream inputStream) throws IOException {
    char[] buffer = new char[1024 * 4];
    InputStreamReader input = new InputStreamReader(inputStream);
    BufferedReader br = new BufferedReader(input);
    StringWriter writer = new StringWriter();
    long count = 0;
    int n = 0;
//    while (EOF != (n = input.read(buffer))) {
//      writer.write(buffer, 0, n);
//      count += n;
//    }
//    return writer.toString();
    
    return br.readLine()+"\n"+br.readLine()+"\n"+br.readLine()+"\n"+br.readLine();
  }
}
