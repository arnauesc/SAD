import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

class EditableBufferedReader extends BufferedReader {

    Line line;

    public EditableBufferedReader(Reader in) {
        super(in);
        this.line= new Line();
    }

    public void setRaw() {
        ArrayList<String> ordres = new ArrayList<String>();
        ordres.addAll(Arrays.asList("/bin/sh", "-c", "stty -echo raw</dev/tty"));
        ProcessBuilder processBuilder = new ProcessBuilder(ordres);
        try {
            processBuilder.start();
        } catch (IOException ex) {
            System.out.println("Error processBuilder cooked to raw mode");
        }

    }

    public void unsetRaw() {
        ArrayList<String> cmd = new ArrayList<String>();
        cmd.addAll(Arrays.asList("/bin/sh", "-c", "stty cooked echo </dev/tty"));
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            processBuilder.start();
        } catch (IOException ex) {
            System.out.println("Error processBuilder raw to cooked mode");
        }

    }
        //Keys
    /*
 Right:	ESC [ C
 Left:	ESC [ D
 Home:	ESC O H, ESC [ 1 ~ (keypad)
 End:	ESC O F, ESC [ 4 ~ (keypad)
 Insert:	ESC [ 2 ~
 Delete:	ESC [ 3 ~
    */ 

    public int read(){
        int i, valor = 0;
        try {
            i = super.read();
            if(i!=Key.ESC){
                if(i== Key.DEL){
                    return Shortcuts.DEL;
                }else{
                    return  i;
                }
            }
            if((i = super.read()) == Key.BRACKET){
                switch (i=super.read()) {
                    case Key.SUPR:
                        if(super.read()=='~'){
                            valor = Shortcuts.SUPR;
                        }
                        break;
                    case Key.L:
                        valor = Shortcuts.L;
                        break;                        
                    case Key.R:
                        valor = Shortcuts.R;
                        break;
                        
                    case Key.INS:
                        if(super.read()=='~'){
                            valor= Shortcuts.INS;
                        }
                        break;
                    case Key.END:
                        valor = Shortcuts.END;
                        break;
                    case Key.HOME:
                        valor = Shortcuts.HOME;
                        break;
                    default:
                       break;
                    }
                }else if(i=='O'){
                    switch (i=super.read()) {
                        case Key.HOME:
                            valor = Shortcuts.HOME;
                            break;
                        case Key.END:
                            valor = Shortcuts.END;
                            break;
                        default:
                            break;
                    }
                    
                }
        }catch(IOException e){
            e.printStackTrace();
            i = 0;
        }
        return valor;

    }

    public String readLine() {
        Consol consol = new Consol(this.line);
        this.setRaw();
        int car;

        do  {
            car =  this.read();
            switch (car) {
                case Shortcuts.DEL:
                    line.delete();
                    break;
            
                case Shortcuts.END:
                    line.fin();
                    break;
                
                case Shortcuts.HOME:
                    line.ini();
                    break;

                case Shortcuts.INS:
                    line.insertMode();
                    break;
                
                case Shortcuts.L:
                    line.moveCursorL();
                    break;

                case Shortcuts.R:
                    line.moveCursorR();
                    break;
                
                case Shortcuts.SUPR:
                    line.supr();
                    break;

                default:
                    line.add((char)car);
                    break;
            }
            

        }while((car != 13));

        System.out.println("\nREAD "+line.toString());
        this.unsetRaw();
        return line.toString();
    }
}

//per poder calcular les columnes i les linies de la consola s'ha d'utlitzar la funció  System.getenv("COLUMNS") Y  System.getenv("LINES")