import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MySocket {
    Socket socket;
    BufferedReader kbd;
    PrintWriter pw;
    public MySocket(Socket socket) {
        this.socket = socket;
        try {
            this.kbd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public MySocket(String host, int port){
        try {
            this.socket = new Socket(host, port);
            this.kbd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(),true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine(){
        try {
            return this.kbd.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void println(String line){
       try{
        pw.println(line);
        pw.flush();
       } catch (Exception e){
        e.printStackTrace();
       }
    }

    public void close(){
        try {
            this.socket.close();
            this.kbd.close();
            this.pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    
}
