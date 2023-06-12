import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;

public class Xat {
    static JTextPane input;
    static JButton button_send;
    static JTextField userText;
    static JFrame iniciXat;
    static JFrame pantallaPrincipal;
    static JTextArea messages;
    ArrayList<String> users;
    JTextField textField;
    JList<String> users_list;
    DefaultListModel<String> listModel;

    static MySocket socket;
    String username;
    String message_user;

    public Xat() {
        super();
        socket = new MySocket("127.0.0.1", 2345);
        users = new ArrayList<>();
        listModel = new DefaultListModel<String>();
        users_list = new JList<String>(listModel);


    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Xat xat = new Xat();
                xat.iniciXat();
            }
        });
    }

    public void iniciXat() {

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        iniciXat = new JFrame("Xat");
        iniciXat.getContentPane().setLayout(new BoxLayout(iniciXat.getContentPane(), BoxLayout.PAGE_AXIS));
        iniciXat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an output JPanel and add a JList with the messages inside a
        // JScrollPane

        JPanel enterUsername = new JPanel();
        enterUsername.setLayout(new BoxLayout(enterUsername, BoxLayout.PAGE_AXIS));
        JLabel labelNewUser = new JLabel("Enter your username");
        userText = new JTextField(25);
        JButton button = new JButton("Create");
        button.addActionListener(new EnterServerButtonListener());


        enterUsername.add(labelNewUser);
        enterUsername.add(userText);
        enterUsername.add(button);
        enterUsername.setMaximumSize(
                new Dimension(enterUsername.getMaximumSize().width, enterUsername.getMinimumSize().height));

        // add panels to main frame
        iniciXat.add(enterUsername, BorderLayout.PAGE_END);

        // Display the window centered.
        // iniciXat.pack();
        iniciXat.setSize(400, 500);
        iniciXat.setLocationRelativeTo(null);
        iniciXat.setVisible(true);

    }

    public void pantallaPrincipal() {

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        pantallaPrincipal = new JFrame("Xat");
        pantallaPrincipal.getContentPane()
                .setLayout(new BoxLayout(pantallaPrincipal.getContentPane(), BoxLayout.PAGE_AXIS));
        pantallaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pantallaPrincipal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                socket.println("Exit " +username );
                llistaUsuaris("Exit " + username);
            }
        }); 

            
        
        // Create an output JPanel and add a JTextArea(20, 30) inside a JScrollPane
        messages = new JTextArea(20,30);
        JPanel output = new JPanel();
        output.setLayout(new BoxLayout(output, BoxLayout.PAGE_AXIS));
        messages.setEditable(false);
        output.add(new JScrollPane(messages));


         // Create the list and put it in a scroll pane.
         JLabel usersLabel= new JLabel("Users available");
         output.add(usersLabel);
 
         // Create a list available users 

         users_list.setBackground(new Color(0, 255, 0));
         JScrollPane scrollPane = new JScrollPane(users_list);
         scrollPane.setMaximumSize(new Dimension(scrollPane.getMaximumSize().width, scrollPane.getMinimumSize().height));
         output.add(scrollPane);
         output.add(new JScrollPane(messages));
        
        // Create an input JPanel and add a JTextField(25) and a JButton
        input = new JTextPane();
        input.setLayout(new BoxLayout(input, BoxLayout.LINE_AXIS));
        textField = new JTextField(25);

        button_send = new JButton("Send");
        button_send.addActionListener(new sendMessageButtonListener());

        input.add(textField);
        input.add(button_send);
        input.setMaximumSize(new Dimension(input.getMaximumSize().width, input.getMinimumSize().height));

        // add panels to main frame
        pantallaPrincipal.add(output);
        pantallaPrincipal.add(input);

        // Display the window centered.
        // frame.pack();
        pantallaPrincipal.setSize(400, 500);
        pantallaPrincipal.setLocationRelativeTo(null);
        pantallaPrincipal.setVisible(true);

    }

    public class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            message_user = textField.getText();
            if (message_user.length() < 1) {
                // do nothing
            } else {
                System.out.println("Entra boto");
                textField.setText("");
                socket.println(message_user);
                messages.append(username + ": " + message_user + "\n");
            }
            textField.requestFocusInWindow();

        }

    }

    void popUpErrorMessage(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                JOptionPane.ERROR_MESSAGE);
    }

    void enterServer() {
        username = userText.getText();

        if (username.length() < 1) {
            popUpErrorMessage("Please fill in the required fields");
        } else {
            try {
                socket.println(username);
                System.out.println("User " + username);
                String connected = socket.readLine();
                if (connected.equals("Exist")) {
                    System.out.println("Nom d'usuari existent");
                    popUpErrorMessage("The user already exist");
                }
                else{
                    llistaUsuaris(username);
                    iniciXat.setVisible(false);
                    pantallaPrincipal();
                    startListenning();
                }
                
            } catch (Exception e) {
                popUpErrorMessage(
                        "Looks like the data you entered is incorrect, make sure the fields are in the correct format and that a server is listenning to the specified port");
            }

        }
    }


    private void startListenning() {
        new Thread() {
            public void run() {
                String line;
                try {
                    while ((line = socket.readLine()) != null) {
                        messages.append(line + " \n");
                        
                        if(line.contains("Exit")){
                            llistaUsuaris(line);
                        }
                        else{
                            String[] parts = line.split(":");
                            llistaUsuaris(parts[0]);
                        }
                    }
                } catch (Exception ex) {
                    socket.close();
                    System.exit(0);
                }
            }
        }.start();
    }

    public class EnterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            enterServer();
        }
    }

    public void llistaUsuaris(String username) {
        if (username.contains("Exit")) {
            String name = username.substring(5);
            if (users.contains(name)) {
                users.remove(name);
                listModel.removeElement(name);
                System.out.println("Eliminem NOM" + name);
            }
        } else {
            String name = username.substring(0);
            if (!users.contains(name)) {
                users.add(name);
                listModel.addElement(name);
                
                for(int i = 0; i< users.size(); i++ ){
                    System.out.println(users_list.getModel().getElementAt(i));
                }
                /*socket.println(name);*/
                System.out.println("Afegim NOM " + name);
            }
        }
    }

}