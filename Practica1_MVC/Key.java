
public class Key{

    //Keys
    /*
 Right:	ESC [ C
 Left:	ESC [ D
 Home:	ESC 0 H, ESC [ 1 ~ (keypad)
 End:	ESC O F, ESC [ 4 ~ (keypad)
 Insert:	ESC [ 2 ~
 Delete:	ESC [ 3 ~
    */

     static final int INS = 50; //
     static final int SUPR = 51; //3 Delete on the right
     static final int ESC = 27; //^[
     static final int L = 68; //D
     static final int R = 67; //C
     static final int DEL = 127; 
     static final int BRACKET = 91; //[
     static final int HOME = 72; //H
     static final int END = 70; //F
     static final int MOUSE = 'M';

}