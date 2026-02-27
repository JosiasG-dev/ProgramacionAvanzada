package Parte1.Vista;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class practica_01_01_b extends JFrame {
	 private JTabbedPane contentPane;

	    public static void main(String[] args) {
	        EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                	practica_01_01_b frame = new practica_01_01_b();
	                    frame.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	    }

	    public practica_01_01_b() {
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setBounds(100, 100, 450, 300);
	        setTitle("Frame Practica01_01 b");
	        contentPane = new JTabbedPane();
	        contentPane.add("Pestaña 1", new JPanel());
	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	        contentPane.add("Pestaña 2", scrollPane);
	        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	        setContentPane(contentPane);
	        JDesktopPane desktopPane = new JDesktopPane();
	        contentPane.addTab("Pestaña 3", null, desktopPane, null);
	        JInternalFrame internalFrame = new JInternalFrame("New InternalFrame");
	        contentPane.addTab("Pestaña 4", null, internalFrame, null);
	        internalFrame.setVisible(true);
	    }
}
