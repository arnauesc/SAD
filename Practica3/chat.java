/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 

 // IMPORTS
 import MySocket;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.KeyEvent;
 import java.awt.event.KeyListener;
 import javax.swing.*;
 import javax.swing.text.SimpleAttributeSet;
 import javax.swing.text.StyleConstants;
 import javax.swing.text.StyledDocument;
 
 
 import java.util.*;
 import java.net.*;
 
 
 
 /**
  *
  * @author lsadusr11
  */
 public class ChatRoom {
     
     static String      appName     = "SwingRoom v0.1";
     ChatRoom           chatRoom;
     static JFrame      newFrame    = new JFrame();
     static JButton     sendMessageButton;
     static JTextField  messageBox;
     static JTextPane   chatBox;
     static JLabel      colorLabel;
     static JTextField  portChooser;
     static JTextField  hostChooser;
     static JTextField  usernameChooser;
     static JFrame      preFrame;
     
     HashMap<String, Color> clientColors = new HashMap<String, Color>();
     
     static MySocket    socket;
     String             username;
     String             host;
     String             port;
     
 
     public static void main(String[] args) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                         try {
                             UIManager.setLookAndFeel(UIManager
                                     .getSystemLookAndFeelClassName());
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                         ChatRoom mainGUI = new ChatRoom();
                         mainGUI.preDisplay();
                     }
                 });
             }
         });
     }
 
     public void preDisplay() {
         newFrame.setVisible(false);
         preFrame = new JFrame(appName);
         JPanel prePanel = new JPanel(new GridBagLayout());
 
         // form
         usernameChooser = new JTextField(15);
         if (username != null) usernameChooser.setText(username);
         JLabel chooseUsernameLabel = new JLabel("Pick a username:");
         hostChooser = new JTextField(30);
         if (host != null) hostChooser.setText(host);
         JLabel chooseHostLabel = new JLabel("Host:");
         portChooser = new JTextField(5);
         if (port != null) portChooser.setText(port);
         JLabel choosePortLabel = new JLabel("Port:");
         JButton enterServer = new JButton("Enter Chat Server");
         enterServer.addActionListener(new EnterServerButtonListener());
 
         usernameChooser.addKeyListener(new EnterServerKeyListener());
         hostChooser.addKeyListener(new EnterServerKeyListener());
         portChooser.addKeyListener(new EnterServerKeyListener());
 
         GridBagConstraints preRightConstraints = new GridBagConstraints();
         preRightConstraints.insets = new Insets(0, 0, 0, 10);
         preRightConstraints.anchor = GridBagConstraints.EAST;
         GridBagConstraints preLeftConstraints = new GridBagConstraints();
         preLeftConstraints.anchor = GridBagConstraints.WEST;
         preLeftConstraints.insets = new Insets(0, 10, 0, 10);
         preRightConstraints.weightx = 2.0;
         preRightConstraints.fill = GridBagConstraints.HORIZONTAL;
         preRightConstraints.gridwidth = GridBagConstraints.REMAINDER;
 
         prePanel.add(chooseUsernameLabel, preLeftConstraints);
         prePanel.add(usernameChooser, preRightConstraints);
         prePanel.add(chooseHostLabel, preLeftConstraints);
         prePanel.add(hostChooser, preRightConstraints);
         prePanel.add(choosePortLabel, preLeftConstraints);
         prePanel.add(portChooser, preRightConstraints);
         preFrame.add(BorderLayout.CENTER, prePanel);
         preFrame.add(BorderLayout.SOUTH, enterServer);
         preFrame.setSize(400, 400);
         preFrame.setVisible(true);
         preFrame.setLocationRelativeTo(null);
         
     }
 
     public void display() {
         newFrame = new JFrame(username);
         
         JPanel mainPanel = new JPanel();
         mainPanel.setLayout(new BorderLayout());
 
         JPanel southPanel = new JPanel();
         southPanel.setBackground(Color.BLUE);
         southPanel.setLayout(new GridBagLayout());
 
         messageBox = new JTextField(30);
         messageBox.requestFocusInWindow();
         messageBox.addKeyListener(new SendMessageKeyListener());
 
         sendMessageButton = new JButton("Send Message");
         sendMessageButton.addActionListener(new SendMessageButtonListener());
         
         chatBox = new JTextPane();
         chatBox.setEditable(false);
         chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
         // chatBox.setLineWrap(true);
 
         mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);
 
         GridBagConstraints left = new GridBagConstraints();
         left.anchor = GridBagConstraints.LINE_START;
         left.fill = GridBagConstraints.HORIZONTAL;
         left.weightx = 512.0D;
         left.weighty = 1.0D;
 
         GridBagConstraints right = new GridBagConstraints();
         right.insets = new Insets(0, 10, 0, 0);
         right.anchor = GridBagConstraints.LINE_END;
         right.fill = GridBagConstraints.NONE;
         right.weightx = 1.0D;
         right.weighty = 1.0D;
 
         southPanel.add(messageBox, left);
         southPanel.add(sendMessageButton, right);
 
         mainPanel.add(BorderLayout.SOUTH, southPanel);
 
         newFrame.add(mainPanel);
         newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         newFrame.setSize(470, 300);
         newFrame.setVisible(true);
         newFrame.setLocationRelativeTo(null);
     }
 
     private void appendToChatBox(String line, SimpleAttributeSet keyWord) {
         StyledDocument doc = chatBox.getStyledDocument();
         try
         {
             doc.insertString(doc.getLength(), line, keyWord );
         }
         catch(Exception e) { System.out.println(e); }
     }
     
     private void sendMessage() {
         if (messageBox.getText().length() < 1) {
             // do nothing
         } else if (messageBox.getText().equals(".clear")) {
             chatBox.setText("Cleared all messages\n");
             messageBox.setText("");
         } else {
             String newChatLine = "<" + username + ">:  " + messageBox.getText(); 
             appendNewMessage(newChatLine);
             socket.printString(newChatLine);
             messageBox.setText("");
         }
         messageBox.requestFocusInWindow();
     }    
 
     class SendMessageButtonListener implements ActionListener {
         public void actionPerformed(ActionEvent event) {
             sendMessage();
         }
     }
 
     void enterServer() {
         username = usernameChooser.getText();
         host = hostChooser.getText();
         port = portChooser.getText();
         
         if (username.length() < 1 || host.length() < 1 || port.length() < 1) {
             popUpErrorMessage("Please fill in the required fields");
         } else {
             try {
                 createClientSocket(host, port);
                 preFrame.setVisible(false);
                 display();
                 startListenning();
             } catch (Exception e) {
                 popUpErrorMessage("Looks like the data you entered is incorrect, make sure the fields are in the correct format and that a server is listenning to the specified port");
             }
         }
     }
 
     class EnterServerButtonListener implements ActionListener {
         public void actionPerformed(ActionEvent event) {
             enterServer();            
         }
     }
 
     class EnterServerKeyListener implements KeyListener {
         @Override
         public void keyPressed(KeyEvent e) {
             if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                 enterServer();
             }
         }
         @Override
         public void keyReleased(KeyEvent e) {
             // do nothing
         }
         @Override
         public void keyTyped(KeyEvent e) {
             // do nothing
         }   
     }
 
     void nickAlreadyUsed() {
         // go back and close the connection 
         preDisplay();
         usernameChooser.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
         popUpErrorMessage("Nick already used");
         socket.closeWriter();
         socket.closeReader();
         socket.closeSocket();
     }
 
     class SendMessageKeyListener implements KeyListener {
         @Override
         public void keyPressed(KeyEvent e) {
             if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                 sendMessage();
             }
         }
         @Override
         public void keyReleased(KeyEvent e) {
             // do nothing
         }
         @Override
         public void keyTyped(KeyEvent e) {
             // do nothing
         }   
     }
     
     private void createClientSocket(String host, String port) throws Exception {
         int portInt = Integer.parseInt(port);
         socket = new MySocket(host, portInt);
         socket.printString(username);
     }
     
     private void startListenning() {
         new Thread(){
             public void run() {
                 String line;
                 try {
                     while((line = socket.readString()) != null){
                         appendNewMessage(line);
                     }
                     // if the server unexpectedly closes connection is that nick is already used
                     nickAlreadyUsed();
                 } catch (Exception ex) {
                     socket.closeReader();
                     socket.closeSocket();
                     System.exit(0);
                 }
             }
         }.start();
     }
 
     void popUpErrorMessage(String message) {
         JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
         JOptionPane.ERROR_MESSAGE);
     }
 
     private void appendNewMessage(String line) {
         String nick = getNick(line);
         if (!nick.isEmpty() && nick != null) {
             Color clientColor = clientColors.get(nick);
             if (clientColor != null) {
                 // existing client messages
                 printMessageWithColor(line, clientColor);
             } else if (clientColor == null && nick != username) {
                 // new client messages
                 clientColor = getRandomColor();
                 clientColors.put(nick, clientColor);
                 printMessageWithColor(line, clientColor);
             } else {
                 // own messages
                 appendToChatBox( line + "\n", null);        
             }
         } else {
             // server messages
             appendToChatBox( line + "\n", null);        
         }
     }
     
     private String getNick(String message) {
         String nick = "";
         if (message != null && message.startsWith("<")) {
             nick = message.substring(1);
             nick = nick.split(">")[0];
         }
         return nick;
     }
 
     private void printMessageWithColor(String message, Color color) {
         SimpleAttributeSet keyWord = new SimpleAttributeSet();
         StyleConstants.setForeground(keyWord, color);
         appendToChatBox(message + "\n", keyWord);
     }
 
     Random rand = new Random();
     private Color getRandomColor() {
         float r = rand.nextFloat();
         float g = rand.nextFloat() / 2f;
         float b = rand.nextFloat() / 2f;
         Color randomColor = new Color(r, g, b);
         return randomColor;
     }
 }