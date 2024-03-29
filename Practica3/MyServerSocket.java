import java.io.IOException;
import java.net.ServerSocket;


public class MyServerSocket {
    ServerSocket ss;
    public MyServerSocket(int port) {
        try {
            this.ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
     
    }
    public MySocket accept(){
        try {
            return new MySocket(this.ss.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void close(){
        try{
            this.ss.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    
    }
}
