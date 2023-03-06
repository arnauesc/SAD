public class Line{ 
    int position;
    boolean insert=false;
    StringBuffer sb;
    public static final int BUFFERCAPACITY=32; // initial capacity of 16 characters by default
    //comentaris: 1- Si s'ompla el buffer, s'ha de crear un nou o es limita?
    //2- Al modificarse la capacitat del stringbuffer, té sentit la condició de limitació per capacitat?
    public Line(){
        sb = new StringBuffer(BUFFERCAPACITY); 
        this.position=0;
    }

    public void moveCursorR(){
        if(this.position<sb.length()){
            System.out.print(Shortcuts.MOVE_R);
            this.position++;
        } 
    }

    public void moveCursorL(){
        if(this.position!=0){
            System.out.print(Shortcuts.MOVE_L);
            this.position--;
        }   

    }

    public void add(char c){
        if(insert){
                System.out.print(Shortcuts.OVERWRITE);
                sb.setCharAt(this.position, c);
                System.out.print(c);
                this.position++;
        }else{
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
            System.out.print(Shortcuts.SUPRIMIR);
            
        }
        
    }
    
    public void ini(){
        //Move the cursor to the left to where we get back to the initial position
        System.out.print(Shortcuts.ESC+"["+this.position+"D"); 
        this.position = 0; //Reset position to 0

    }
    
    public void fin(){
        // We need to move moveRight positions to get to the end of the StringBuffer
        int moveRight= sb.length()-this.position;
        System.out.print(Shortcuts.ESC+"["+moveRight+"C"); 
        this.position= sb.length();
    }

    public void insertMode(){
        this.insert=!this.insert;
    }

    public void supr(){
        if(this.position<sb.length()){
            sb.deleteCharAt(position);
            System.out.print(Shortcuts.SUPRIMIR);
        }
    }

}

