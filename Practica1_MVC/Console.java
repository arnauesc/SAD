import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Console implements PropertyChangeListener {
    private Line line;

    public Console(Line line){
        this.line =line;
        this.line.addPropertyChangeListener(this);
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int seq = (int)evt.getNewValue();
            switch (seq) {
                case Shortcuts.R:
                    System.out.print(Shortcuts.MOVE_R);
                    break;
                case Shortcuts.L:
                    System.out.print(Shortcuts.MOVE_L);
                    break;
                case Shortcuts.DEL:
                    System.out.print(Shortcuts.SUPRIMIR);
                    break;
                case Shortcuts.HOME:
                    System.out.print(Shortcuts.ESC+"["+line.getPosition()+"D"); 
                    break;
                case Shortcuts.END:
                    int moveRight= line.getBufferLenght()-line.getPosition();
                    System.out.print(Shortcuts.ESC+"["+moveRight+"C");
                    break;
                case Shortcuts.SUPR:
                    System.out.print(Shortcuts.SUPRIMIR);
                    break;
                case Shortcuts.INS:
                    System.out.print(Shortcuts.OVERWRITE);
                    break;
                case Shortcuts.NOINS:
                    System.out.print(Shortcuts.WRITE);
                default:
                    break;
            }
    }

    
}
