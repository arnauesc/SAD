import java.beans.PropertyChangeSupport;
import java.util.Scanner;
import java.beans.PropertyChangeListener;

public class Line { 
    private PropertyChangeSupport support;
    int position;
    boolean insert=false;
    StringBuffer sb;
    public static final int BUFFERCAPACITY=32; 
    public Line(){
        this.sb = new StringBuffer(BUFFERCAPACITY); 
        this.position=0;
        this.insert = false;
        this.support = new PropertyChangeSupport(this);
    }
    public int getCursorRow(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\033[6n");
        sc.skip("\033\\[8;(\\d+);(\\d+)t");
        return Integer.parseInt(sc.match().group(0));
    }
    //Aquesta funció ens permet afegir un oient a la llista per alertarlos si hi ha un canvi
    public void addPropertyChangeListener(PropertyChangeListener listener){
        this.support.addPropertyChangeListener(listener);
    }
    //Permet retir de la llista d'oients 
    public void removePropertyChangeListener(PropertyChangeListener listener){
        support.removePropertyChangeListener(listener);
    }
    //Retorna la posició del cursor
    public int getPosition(){
        return this.position;
    }
    //Retorna la longitud del StringBuffer
    public int getBufferLenght(){
        return sb.length();
    }
    
    public void moveCursorR(){
        if(this.position<sb.length()){
            this.support.firePropertyChange("moveRight", null,Shortcuts.R);
            //Shortcuts.MOVE_R
            this.position++;
        } 
    }

    public void moveCursorL(){
        if(this.position!=0){
            this.support.firePropertyChange("moveLeft", null,Shortcuts.L);
            //Shortcuts.MOVE_L
            this.position--;
        }   

    }

    public void add(char c){
        if(insert && this.position<sb.length()){
                this.support.firePropertyChange("overwrite", null,Shortcuts.INS);
                //Shortcuts.OVERWRITE
                sb.setCharAt(this.position, c);
                System.out.print(c);
                this.position++;
        }else{
               this.support.firePropertyChange("write", null,Shortcuts.NOINS);
               //Shortcuts.WRITE
               System.out.print(Shortcuts.WRITE);
               sb.insert(this.position, c);
               System.out.print(c);
               this.position++;
        }
            
    }

    public void delete(){
        if(this.position !=0){
            sb.deleteCharAt(position-1);
            this.moveCursorL();
            this.support.firePropertyChange("suprimir", null,Shortcuts.DEL);
            //Shortcuts.SUPRIMIR
        }
        
    }
    
    public void ini(){
        //Move the cursor to the left to where we get back to the initial position
        this.support.firePropertyChange("inicio", null,Shortcuts.HOME);
        //Shortcuts.ESC+"["+this.position+"D"
        this.position = 0; //Reset position to 0

    }
    
    public void fin(){
        // We need to move moveRight positions to get to the end of the StringBuffer
        this.support.firePropertyChange("inicio", null,Shortcuts.END);
        this.position= sb.length();
    }

    public void insertMode(){
        this.insert=!this.insert;

    }

    public void supr(){
        if(this.position<sb.length()){
            sb.deleteCharAt(position);
            this.support.firePropertyChange("inicio", null,Shortcuts.SUPR);
        }
    }

    public String toString(){
        this.ini();
        return sb.toString();
    }

    public void mouseClick(int Cx, int Cy) {
        if(Cy == this.getCursorRow()){
            if(Cx > this.position) {
                while(Cx > this.position) {
                    this.moveCursorR();
                }
            }else {
                    while(this.position> Cx) {
                    this.moveCursorL();
                }
            }
        }
    }

}

