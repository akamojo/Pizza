/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 *
 * @author akamojo
 */
public class Pizza {
    
    public static AtomicInteger at = new AtomicInteger(1);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InfoJFrame pizza = new InfoJFrame();
                pizza.setLocation(getShowPosition(pizza));
                try {
                    URL url = Pizza.class.getResource("Pizza-icon.png");
                    Toolkit kit = Toolkit.getDefaultToolkit();
                    Image img = kit.createImage(url);
                    pizza.setIconImage(img);
                    pizza.setVisible(true);
                    Timer timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        Thread worker = new Thread() {
                            
                            @Override
                            public void run() {
                                pizza.getMapa().revalidate();
                                pizza.getMapa().repaint();
                            }
                        };
                        
                        worker.start(); // So we don't hold up the dispatch thread.
                    }
                });
                timer.start();
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                SwingUtilities.updateComponentTreeUI(pizza);
            }
        });
    }
    
    public static Point getShowPosition(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
          frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
          frameSize.width = screenSize.width;
        }
        return new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    public static Point getShowPosition(JDialog dialog) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = dialog.getSize();
        if (dialogSize.height > screenSize.height) {
          dialogSize.height = screenSize.height;
        }
        if (dialogSize.width > screenSize.width) {
          dialogSize.width = screenSize.width;
        }
        return new Point((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
    }
    /*
    public static int czyNalezy(int elem, List<Integer> tab) {
        for (int i = 0; i < tab.size(); i++) {
            if (tab.get(i) == elem) {
                return 1;
            }
        }
        return 0;
    }*/
    
    /**
     *
     * @param liczba
     * @return
     */
    public static float zaokraglenie(float liczba){
        liczba *= 100;
        liczba = Math.round(liczba);
        liczba /= 100;
        return liczba;
    }
    
}
