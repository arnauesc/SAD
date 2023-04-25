import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class EchoClient {
    public static void main(String[] args){
        MySocket sc = new MySocket("127.0.0.1", 2345);
        BufferedReader kbd = new BufferedReader(new InputStreamReader(System.in));
        sc.println(args[0]);

        //Input thread
        new Thread(){
            public void run(){
                String line;
                try{
                    while((line=kbd.readLine())!=null){
                        sc.println(line);
                        if(line.matches("exit")){
                            sc.close();
                            break;
                        }
                     }
                     sc.println("exit");
                     sc.close();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.start();

        //Output thread
        new Thread(){
            public void run(){
                String line;
                while((line=sc.readLine())!=null){
                    if(line.matches("exit")){
                        break;
                    }
                    System.out.println(line);
                }
                System.out.println("Client Disconnected...");
                sc.close();
                System.exit(0);
            } 
        }.start();
    }
}
