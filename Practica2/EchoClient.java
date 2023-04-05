import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class EchoClient {
    public static void main(String[] args){
        MySocket sc = new MySocket(args[0], Integer.parseInt(args[1]));

        //Input thread
        new Thread(){
            public void run(){
                String line;
                BufferedReader kbd = new BufferedReader(new InputStreamReader(System.in));
                
                try{
                    while((line=kbd.readLine())!=null){
                        sc.println(line);
                     }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                sc.close();
            }
        }.start();

        //Output thread
        new Thread(){
            public void run(){
                String line;
                while((line=sc.readLine())!=null){
                    sc.println(line);
                }
                System.out.println("Client Disconnected...");
                sc.close();
                System.exit(0);
            } 
        }.start();
    }
}
