package Libreria;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
public class UtilDesktop {
	 public static void centrarInternal(JDesktopPane desktop, JInternalFrame frame) {

	        int x = (desktop.getWidth() - frame.getWidth()) / 2;
	        int y = (desktop.getHeight() - frame.getHeight()) / 2;

	        frame.setLocation(x, y);
	    }
}
