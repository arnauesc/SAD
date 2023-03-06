
public class Shortcuts{

   //Mapping
     static final int SUPR = 0; //3 Delete on the right
     static final int L = 1; //D
     static final int R = 2; //C
     static final int DEL = 3; 
     static final int HOME = 4; //H
     static final int END = 5; //F
     static final int INS = 6;

     //Escape codes
     static final String MOVE_R= "\033[1C";
     static final String MOVE_L= "\033[1D";
     static final String ESC="\033";
     static final String WRITE="\033[4h";
     static final String OVERWRITE="\033[4l";
     static final String SUPRIMIR="\033[P";


}