public class Line {
    int position;
    StringBuffer sb;
    public Line(){
        sb = new StringBuffer(); // initial capacity of 16 characters
        position=0;
    }

    public String moveCursorR(){
        position++;
        //System.out.println(Shortcuts.MOVE_R);
        return Shortcuts.MOVE_R;
    }

    public String moveCursorL(){
        position--;
        //System.out.println(Shortcuts.MOVE_L);
        return Shortcuts.MOVE_L;
    }

    public String insert(char c){
        String s="";
        sb.insert(position, c);
        return s;
    }

}
