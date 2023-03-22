
public class Shortcuts{

   //Mapping
     static final int SUPR = -1; //3 Delete on the right
     static final int L = -2; //D
     static final int R = -3; //C
     static final int DEL = -4; 
     static final int HOME = -5; //H
     static final int END = -6; //F
     static final int INS = -7;
     static final int NOINS = -8;
     static final int MOUSE = -9;

     //Escape codes
     static final String MOVE_R= "\033[1C";
     static final String MOVE_L= "\033[1D";
     static final String ESC="\033";
     static final String WRITE="\033[4h";
     static final String OVERWRITE="\033[4l";
     static final String SUPRIMIR="\033[P";
     static final String CLEAR = "\033[2J";
     static final String INICI = "\033[0;0H";
     static final String ACTIVATE_MOUSE="\033[?9h";
     static final String DEACTIVATE_MOUSE="\033[?9l";

}