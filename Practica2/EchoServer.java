import java.util.HashMap;
import java.util.Map;

public class EchoServer {
    public static void main(String[] args){
        MyServerSocket ss = new MyServerSocket(2345);
        Map<String,MySocket> users = new HashMap<String,MySocket>();

        while(true){
            MySocket s= ss.accept();
            s.println("Connected");
            new Thread(){
                public void run(){
                    String username= s.readLine();
                    users.put(username, s);
                    System.out.println(username + " joined the chat");
                    String line;
                    while((line=s.readLine())!=null){
                        

                        if(line.matches("exit")){
                            System.out.println(username+" left the chat");
                            users.remove(username);
                        }

                        for(String oneUser: users.keySet()){
                            if(!oneUser.matches(username)){
                                users.get(oneUser).println(username + ": "+ line);
                            }
                        }
                    }
                    s.close();
                }
            }.start();
        }
    }
}
