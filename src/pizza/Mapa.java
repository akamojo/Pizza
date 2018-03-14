/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Klasa definiująca mapę.
 * Dziedziczy z JPanel.
 * @author akamojo
 */
public class Mapa extends JPanel {

    private BufferedImage house;
    private BufferedImage car;
    private BufferedImage restaurant;
    private BufferedImage tlo;
    private Restauracja restauracja;
    private String info;
    private InfoJFrame parent;
    
    /**
     * Zwraca człowieka znajdującego się w punkcie na mapie, na który kliknął użytkownik.
     * Jeśli nic nie ma w wyznaczonym miejscu to zwraca null.
     * @param punkt
     * @return 
     */
    public synchronized Czlowiek znajdzObiekt(Point punkt) {
        Rectangle area = new Rectangle(punkt.x - 40, punkt.y - 40, 50, 50);
        Point obiekt;
        for (Dostawca i : Restauracja.getInstance().getListaDostawcow()) {
            obiekt = new Point(i.getX(), i.getY());
            if (area.contains(obiekt)) {
                return i;
            }
        }
        for (Klient i : Restauracja.getInstance().getListaKlientow()) {
            obiekt = new Point(i.getAdres().getPozycjaX(), i.getAdres().getPozycjaY());
            if (area.contains(obiekt)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Zwraca podstawowe informacje o dostawcy jako string.
     * @param dostawca
     * @return 
     */
    public synchronized String wypiszDostawce(Dostawca dostawca) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<html>Dostawca: ").append(dostawca.getImie()).append(" ").append(dostawca.getNazwisko()).append("<BR>");
        buffer.append("PESEL: ").append(Integer.toString(dostawca.getPesel())).append("<BR>");
        buffer.append("Aktualny cel: (").append(dostawca.getDestination().getPozycjaX()).append(", ").append(dostawca.getDestination().getPozycjaY()).append(")<BR>");
        buffer.append("Ilość paliwa: ").append(Float.toString(dostawca.getPojazd().getStanBaku())).append(" litropikseli<BR>(Kliknij dwa razy na obiekt, aby zmusić go do powrotu do restauracji)</html>");
        return buffer.toString();
    }

    /**
     * Zwraca podstawowe informacje o kliencie jako string.
     * @param klient
     * @return 
     */
    public synchronized String wypiszKlienta(Klient klient) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<html>Klient: ").append(klient.getImie()).append(" ").append(klient.getNazwisko()).append("<BR>");
        if (klient.getZamowienie() != null) {
            buffer.append("Zamówienie nr: ").append(Integer.toString(klient.getZamowienie().getNumer())).append(" Status: ").append(klient.getZamowienie().getStatus().toString()).append("<BR>");
        }
        buffer.append("Rodzaj klienta: ").append(klient.getRodzaj().toString()).append("</html>");
        return buffer.toString();
    }

    /**
     * Tworzenie mapy, z JFrame'em podanym jako parametr.
     * Obsługa kliku i dwukliku myszy.
     * Jeśli użytkownik kliknie raz na obiekt na mapie to w panelu informacyjnym pojawia się informacja o nim.
     * Jeśli dwa razy, to w przypadku kiedy obiektem jest dostawca to zostanie on zmuszony do powrotu do restauracji.
     * @param parent
     * @throws IOException 
     */
    public Mapa(InfoJFrame parent) throws IOException {
        this.parent = parent;
        this.restauracja = Restauracja.getInstance();
        URL url = Mapa.class.getResource("house-icon3.png");
        house = ImageIO.read(url);
        url = Mapa.class.getResource("car2.png");
        car = ImageIO.read(url);
        url = Mapa.class.getResource("restaurant.png");
        restaurant = ImageIO.read(url);
        info = "";
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point punkt = new Point(e.getX(), e.getY());
                Czlowiek czlowiek = znajdzObiekt(punkt);
                setInfo("");
                if (e.getClickCount() == 2) {
                    if (czlowiek instanceof Dostawca) {
                        ((Dostawca) czlowiek).setPowrot(true);
                        System.out.println("ustawiam powrót");
                    }
                } else if (e.getClickCount() == 1) {
                    if (czlowiek instanceof Dostawca) {
                        setInfo(wypiszDostawce((Dostawca) czlowiek));
                    } else if (czlowiek instanceof Klient) {
                        setInfo(wypiszKlienta((Klient) czlowiek));
                    }
                    parent.setInfoMessage(getInfo());
                }
            }
        });
    }

    /**
     * Przesłoniona metoda rysująca obiekty na mapie zgodnie z ich aktualnym położeniem.
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        //g.drawImage(tlo, 0, 50, null);
        for (Dostawca i : restauracja.getListaDostawcow()) {
            g.drawImage(car, i.getX(), i.getY(), null);
        }
        g.drawImage(restaurant, restauracja.getAdres().getPozycjaX(), restauracja.getAdres().getPozycjaY(), null);
        for (Klient i : restauracja.getListaKlientow()) {
            g.drawImage(house, i.getAdres().getPozycjaX(), i.getAdres().getPozycjaY(), null);
        }
    }

    /**
     * Zwraca informacje o ostatnio wybranym obiekcie.
     * @return 
     */
    public String getInfo() {
        return info;
    }

    /**
     * Ustawia informacje obiekcie, które wyświetlają się w panelu informacyjnym.
     * @param info 
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Zwraca obiekt restauracji przypisany do mapy.
     * @return 
     */
    public Restauracja getRestauracja() {
        return restauracja;
    }

    /**
     * Przypisje restaurację do mapy.
     * @param restauracja 
     */
    public void setRestauracja(Restauracja restauracja) {
        this.restauracja = restauracja;
    }

}
