/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa reprezentująca obiekt restauracji. Kluczowa, główna klasa,
 * przechowująca dane o wszystkich obiektach w programie.
 *
 * @author akamojo
 */
public class Restauracja extends Thread implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nazwa;
    private Map<Integer, Dostawca> mapaDostawcow;
    private List<Dostawca> listaDostawcow;
    private Map<Integer, Klient> mapaKlientow;
    private List<Klient> listaKlientow;
    private List<Posilek> listaPosilkow;
    private Map<Integer, Zamowienie> mapaZamowien;
    private List<Zamowienie> listaZamowien;
    private List<Pojazd> listaPojazdow;
    private int progPunktow;
    private int progCeny;
    private int progOdleglosci;
    private Adres adres;
    private transient static Restauracja instance = null;
    private transient List<OrdersListener> ordersListenersList = new ArrayList<OrdersListener>();

    /**
     * Tworzenie pojedynczego wystąpienia obiektu klasy Restauracja w programie.
     *
     * @return
     */
    public static Restauracja getInstance() {
        if (instance == null) {
            instance = new Restauracja();
        }
        return instance;
    }

    /**
     * Wczytywanie danych z pliku.
     * Pobranie obiektu restauracji, wraz z wszystkimi zawieranymi przez nią obiektami, a następnie odtworzenie stanu sprzed zamknięcia programu.
     * (wystartowanie wątków klienta i dostawcy)
     */
    public static void initFromFile() {
        FileInputStream streamIn = null;
        try {
            File file = new File("data.txt");
            if (!file.exists()) {
                return;
            }
            streamIn = new FileInputStream("data.txt");
            ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
            instance = (Restauracja) objectinputstream.readObject();
            for (Dostawca i : instance.listaDostawcow) {
                i.start();
            }
            for (Klient i : instance.listaKlientow) {
                i.start();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Restauracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Restauracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Restauracja.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (streamIn != null) {
                    streamIn.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Restauracja.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Dodanie listenera do nasłuchiwania zmian w zamówieniach.
     * @param listener 
     */
    public void addOrdersListener(OrdersListener listener) {
        if (ordersListenersList == null) {
            ordersListenersList = new ArrayList<OrdersListener>();
        }
        ordersListenersList.add(listener);
    }

    /**
     * Wypisanie informacji o wszystkich klientach znajdujących w programie.
     */
    public void wypiszKlientow() {
        System.out.println("Klienci:");
        for (Klient i : this.getListaKlientow()) {
            i.wypiszDane();
        }
    }

    /**
     * Wypisanie informacji o wszystkich dostawcach znajdujących się w programie.
     */
    public void wypiszDostawcow() {
        System.out.println("Dostawcy:");
        for (Dostawca i : this.getMapaDostawcow().values()) {
            i.wypiszDane();
        }
    }

    /**
     * Wypisanie podstawowych informacji o restauracji.
     */
    public void wypiszRestauracje() {
        System.out.println(this.getNazwa() + " próg punktów: " + this.getProgPunktow() + " próg ceny: " + this.getProgCeny() + " próg odległości: " + this.getProgOdleglosci());
        this.wypiszKlientow();
        this.getMapaDostawcow();
    }

    /**
     * Utworzenie obiektu restauracji o częściowo losowych parametrach.
     * Wygenerowanie początkowego menu, listy dostawców oraz pojazdów.
     * Wystartowanie wątku dostawcy.
     */
    public Restauracja() {
        Random random = new Random();
        this.nazwa = "Pizzeria 24h/7";
        this.adres = new Adres("Restauracyjna", 5, 150, 150);
        this.progPunktow = 10;
        this.progCeny = random.nextInt(50);
        this.progOdleglosci = 10;
        mapaDostawcow = new HashMap<Integer, Dostawca>();
        listaDostawcow = new ArrayList<Dostawca>();
        mapaKlientow = new HashMap<Integer, Klient>();
        listaKlientow = new ArrayList<Klient>();
        for (int i = 0; i < 3; i++) {
            Dostawca dostawca = new Dostawca();
            dostawca.setX(adres.getPozycjaX());
            dostawca.setY(adres.getPozycjaY());
            for (int j = 0; j < 5; j++) {
                dostawca.dodajLosowyCzasPracy();
            }
            dodajDostawce(dostawca);
            dostawca.start();
        }
        listaPosilkow = new ArrayList<Posilek>();
        for (int i = 0; i < 10; i++) {
            dodajLosowyPosilek();
        }
        mapaZamowien = new HashMap<Integer, Zamowienie>();
        listaZamowien = new ArrayList<Zamowienie>();
        listaPojazdow = new ArrayList<Pojazd>();
        for (int i = 0; i < 10; i++) {
            dodajLosowyPojazd();
        }
    }

    /**
     * Metoda opisująca sposób przydzielania przez restaurację pojazdu do dostawcy.
     * Najpierw sprawdzany jest status pojazdu, a później określane jest czy dany dostawca posiada licencję na prowadzenie pojazdu.
     * Po przydzieleniu pojazdu jest on tankowany do pełna.
     * @param dostawca 
     */
    public synchronized void przydzielPojazd(Dostawca dostawca) {
        for (Pojazd i : listaPojazdow) {
            if (i.getStatus() == Status.DOSTEPNY) {
                for (PrawoJazdy j : dostawca.getListaPrawaJazdy()) {
                    if (((i instanceof Samochod) && (j == PrawoJazdy.SAMOCHOD)) || ((i instanceof Skuter) && (j == PrawoJazdy.SKUTER))) {
                        dostawca.setPojazd(i);
                        i.setStatus(Status.TRASA);
                        dostawca.zatankuj();
                        return;
                    }
                }
            }
        }
        System.err.println("Nie przydzielilem pojazdu!");
    }

    /**
     * Dany dostawca zwraca swój pojazd.
     * Jego status ustawiany jest na dostępny.
     * @param dostawca 
     */
    public synchronized void zwrocPojazd(Dostawca dostawca) {
        Pojazd pojazd = dostawca.getPojazd();
        pojazd.setStatus(Status.DOSTEPNY);
        dostawca.setPojazd(null);
    }

    /**
     * Dodanie losowego pojazdu do listy pojazdów znajdującej się w restauracji.
     */
    public synchronized void dodajLosowyPojazd() {
        Random random = new Random();
        Pojazd pojazd = null;
        switch (random.nextInt(2)) {
            case 0:
                pojazd = new Samochod();
                break;
            case 1:
                pojazd = new Skuter();
                break;
            default:
                break;
        }
        listaPojazdow.add(pojazd);
    }

    /**
     * Dodanie losowego posiłku do menu.
     */
    public void dodajLosowyPosilek() {
        Random random = new Random();
        Posilek posilek = null;
        switch (random.nextInt(2)) {
            case 0:
                posilek = new Posilek();
                break;
            case 1:
                posilek = new ZestawObiadowy();
                break;
            default:
                break;
        }
        listaPosilkow.add(posilek);
    }

    /**
     * Utworzenie restauracji o zadanych parametrach.
     * @param nazwa
     * @param adres
     * @param progPunktow
     * @param progCeny
     * @param progOdleglosci 
     */
    public Restauracja(String nazwa, Adres adres, int progPunktow, int progCeny, int progOdleglosci) {
        this.nazwa = nazwa;
        this.adres = adres;
        this.progPunktow = progPunktow;
        this.progCeny = progCeny;
        this.progOdleglosci = progOdleglosci;
        mapaDostawcow = new HashMap<Integer, Dostawca>();
        listaDostawcow = new ArrayList<Dostawca>();
        listaKlientow = new ArrayList<Klient>();
        mapaKlientow = new HashMap<Integer, Klient>();
    }

    /**
     * Dodanie zamówienia do ogólnej listy i mapy zamówień.
     * Powiadomienie listenerów o konieczności aktualizacji tabeli.
     * @param zamowienie 
     */
    public synchronized void dodajZamowienie(Zamowienie zamowienie) {
        mapaZamowien.put(zamowienie.getNumer(), zamowienie);
        listaZamowien.add(zamowienie);
        notifyOrderListeners();
    }

    /**
     * Dodanie dostawcy do listy i mapy dostawców.
     * @param dostawca 
     */
    public synchronized void dodajDostawce(Dostawca dostawca) {
        mapaDostawcow.put(dostawca.getID(), dostawca);
        listaDostawcow.add(dostawca);
    }

    /**
     * Dodanie losowego dostawcy oraz wystartowanie jego wątku.
     */
    public synchronized void dodajLosowegoDostawce() {
        Dostawca dostawca = new Dostawca();
        dostawca.setX(adres.getPozycjaX());
        dostawca.setY(adres.getPozycjaY());
        for (int j = 0; j < 5; j++) {
            dostawca.dodajLosowyCzasPracy();
        }
        dodajDostawce(dostawca);
        dostawca.start();
    }

    /*public synchronized void dodajLosowegoDostawceFullTime() {
        Dostawca dostawca = new Dostawca();
        for (int i = 0; i < 7; i++) {
            DniTygodnia dzien = DniTygodnia.values()[i];
            int godzinaRozpoczecia = 0;
            int czasTrwania = 24;
            dostawca.dodajCzasPracy(dzien, godzinaRozpoczecia, czasTrwania);
        }
        mapaDostawcow.put(dostawca.getID(), dostawca);
        listaDostawcow.add(dostawca);
    }*/
    
    /**
     * Dodanie klienta do listy i mapy klientów.
     * @param klient 
     */
    public synchronized void dodajKlienta(Klient klient) {
        listaKlientow.add(klient);
        mapaKlientow.put(klient.getID(), klient);
    }

    /**
     * Dodanie losowego klienta oraz wystartowanie jego wątku.
     */
    public synchronized void dodajLosowegoKlienta() {
        Random random = new Random();
        Klient klient = null;
        switch (random.nextInt(3)) {
            case 0:
                klient = new Okazjonalny();
                break;
            case 1:
                klient = new Firmowy();
                break;
            case 2:
                klient = new Staly();
                break;
            default:
                break;
        }
        listaKlientow.add(klient);
        mapaKlientow.put(klient.getID(), klient);
        klient.start();
    }

    /**
     * Usuwanie dostawcy o zadanym ID.
     * Zanim dostawca zostanie usunięty, statusy przydzielonych do niego zamówień zostają zmienione na oczekujące.
     * Zwolniony zostaje również pojazd dostawcy.
     * Wątek dostawcy zostaje przerwany.
     * @param id 
     */
    public synchronized void usunDostawce(int id) {
        Dostawca dostawca = mapaDostawcow.get(id);
        for(Zamowienie i : dostawca.getListaZamowien()){
            i.setStatus(StatusZamowienia.OCZEKUJACE);
            i.setIdDostawcy(0);
        }
        dostawca.getPojazd().setStatus(Status.DOSTEPNY);
        notifyOrderListeners();
        mapaDostawcow.remove(id);
        listaDostawcow.remove(dostawca);
        dostawca.interrupt();
    }

    /**
     * Usuwanie klienta o zadanym ID.
     * Wcześniej jednak usuwane jest jego zamówienie z listy zamówień restauracji.
     * Jego wątek zostaje przerwany.
     * @param id 
     */
    public synchronized void usunKlienta(int id) {
        Klient klient = mapaKlientow.get(id);
        for (Iterator<Zamowienie> iterator = listaZamowien.iterator(); iterator.hasNext();) {
            Zamowienie zamowienie = iterator.next();
            if (zamowienie.getIdKlienta() == id) {
                iterator.remove();
            }
        }
        notifyOrderListeners();
        mapaKlientow.remove(id);
        listaKlientow.remove(klient);
        klient.interrupt();
    }

    /**
     * @return the mapaDostawcow
     */
    public synchronized Map<Integer, Dostawca> getMapaDostawcow() {
        return mapaDostawcow;
    }

    /**
     * @return the listaKlientow
     */
    public synchronized List<Klient> getListaKlientow() {
        return listaKlientow;
    }

    /**
     * @return the progPunktow
     */
    public synchronized int getProgPunktow() {
        return progPunktow;
    }

    /**
     * @param progPunktow the progPunktow to set
     */
    public synchronized void setProgPunktow(int progPunktow) {
        this.progPunktow = progPunktow;
    }

    /**
     * @return the progCeny
     */
    public synchronized int getProgCeny() {
        return progCeny;
    }

    /**
     * @param progCeny the progCeny to set
     */
    public synchronized void setProgCeny(int progCeny) {
        this.progCeny = progCeny;
    }

    /**
     * @return the progOdleglosci
     */
    public synchronized int getProgOdleglosci() {
        return progOdleglosci;
    }

    /**
     * @param progOdleglosci the progOdleglosci to set
     */
    public synchronized void setProgOdleglosci(int progOdleglosci) {
        this.progOdleglosci = progOdleglosci;
    }

    /**
     * Zwraca nazwę restauracji.
     * @return 
     */
    public synchronized String getNazwa() {
        return nazwa;
    }

    /**
     * Ustala nazwę restauracji.
     * @param nazwa 
     */
    public synchronized void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    /**
     * Zwraca listę dostawców.
     * @return 
     */
    public synchronized List<Dostawca> getListaDostawcow() {
        return listaDostawcow;
    }

    /**
     * Ustala listę dostawców.
     * @param listaDostawcow 
     */
    public synchronized void setListaDostawcow(List<Dostawca> listaDostawcow) {
        this.listaDostawcow = listaDostawcow;
    }

    /**
     * Zwraca adres restauracji.
     * @return 
     */
    public synchronized Adres getAdres() {
        return adres;
    }

    /**
     * Ustala adres restauracji.
     * @param adres 
     */
    public synchronized void setAdres(Adres adres) {
        this.adres = adres;
    }

    /**
     * Zwraca mapę klientów.
     * @return 
     */
    public synchronized Map<Integer, Klient> getMapaKlientow() {
        return mapaKlientow;
    }

    /**
     * Ustala mapę klientów.
     * @param mapaKlientow 
     */
    public synchronized void setMapaKlientow(Map<Integer, Klient> mapaKlientow) {
        this.mapaKlientow = mapaKlientow;
    }

    /**
     * Zwraca listę posiłków, składjącą się na menu.
     * @return 
     */
    public synchronized List<Posilek> getListaPosilkow() {
        return listaPosilkow;
    }

    /**
     * Ustala listę posiłków, składającą się na menu.
     * @param listaPosilkow 
     */
    public synchronized void setListaPosilkow(List<Posilek> listaPosilkow) {
        this.listaPosilkow = listaPosilkow;
    }

    /**
     * Zwraca mapę zamówień restauracji.
     * @return 
     */
    public synchronized Map<Integer, Zamowienie> getMapaZamowien() {
        return mapaZamowien;
    }

    /**
     * Ustala listę zamówień restauracji.
     * @param mapaZamowien 
     */
    public synchronized void setMapaZamowien(Map<Integer, Zamowienie> mapaZamowien) {
        this.mapaZamowien = mapaZamowien;
    }

    /**
     * Zwraca listę zamówień restauracji.
     * @return 
     */
    public synchronized List<Zamowienie> getListaZamowien() {
        return listaZamowien;
    }

    /**
     * Ustala listę zamówień restauracji.
     * @param listaZamowien 
     */
    public synchronized void setListaZamowien(List<Zamowienie> listaZamowien) {
        this.listaZamowien = listaZamowien;
    }

    /**
     * Metoda powiadamiająca klienta o tym, że zostało zrealizowane jego zamówienie.
     * Sprowadza się ona do sprawdzenia czy dany klient jest stały, a jeśli tak to jego punkty lojalnościowe i zniżka są aktualizowane.
     * @param zamowienie 
     */
    public synchronized void powiadom(Zamowienie zamowienie) {
        Klient klient = mapaKlientow.get(zamowienie.getIdKlienta());
        if (klient.getRodzaj() == RodzajeKlientow.STALY) {
            ((Staly) klient).setZnizka((int) (((Staly) klient).getZnizka() - klient.getZamowienie().getKoszt()));
            ((Staly) klient).setPunktyLojalnosciowe(((Staly) klient).getPunktyLojalnosciowe() + 1);
        }
        notifyOrderListeners();
    }

    /**
     * Główna funkcja obsługująca dane zamówienie.
     * Najpierw jest ono przygotowywane, później obliczana jest jego cena.
     * Czynność ta jest wykonywana na podstawie sprawdzania warunków przekroczenia progów, których wartości są cechami restauracji.
     * Obliczana jest również zniżka. Po wykonaniu tych operacji status zamówienia zmienia się na "oczekujące" i dodawane jest do listy.
     * @param zamowienie 
     */
    public synchronized void obsluzZamowienie(Zamowienie zamowienie) {
        System.out.println("Przygotowuje zamowienie");
        zamowienie.setStatus(StatusZamowienia.PRZYGOTOWYWANE);
        float odleglosc = (float) Point.distance(getAdres().getPozycjaX(), getAdres().getPozycjaY(), zamowienie.getDestination().getPozycjaX(), zamowienie.getDestination().getPozycjaY());
        if (odleglosc > getProgOdleglosci() && zamowienie.obliczanieKosztu() < getProgCeny()) {
            zamowienie.setCenaDowozu(10 + odleglosc - getProgOdleglosci());
        }
        for (Posilek i : zamowienie.getListaPosilkow()) {
            if (i.getKategoria() == Kategoria.ZESTAW) {
                zamowienie.setZnizka((float) (zamowienie.obliczanieKosztu() * 0.20));
            }
        }
        Klient klient = mapaKlientow.get(zamowienie.getIdKlienta());
        if (klient.getRodzaj() == RodzajeKlientow.STALY) {
            Staly staly = (Staly) klient;
            if (staly.getPunktyLojalnosciowe() >= getProgPunktow()) {
                staly.setZnizka(100);
                staly.setPunktyLojalnosciowe(staly.getPunktyLojalnosciowe() - getProgPunktow());
            }
        }
        zamowienie.setKoszt(zamowienie.obliczanieKosztu());
        System.out.println("Zamowienie przygotowane");
        zamowienie.setStatus(StatusZamowienia.OCZEKUJACE);
        dodajZamowienie(zamowienie);
    }

    /**
     * Metoda obsługująca oczekujące zamówienia.
     * Najpierw znajdywany jest dostawca, który ma status "dostępny", następnie przydzielany jest mu pojazd.
     * Jeśli wykonanie tej czynności jest niemożliwe to wychodzi z funkcji.
     * Zamówienia pakowane są w "paczki" o wielkości zależnej od pojemności przydzielonego pojazdu.
     * Sprawdzany jest również warunek, czy w zadanym czasie złożenia zamówienia dostawca jest w pracy.
     * Jeśli tak, to zamówienie zostaje do niego przydzielone, i taki też status ono przyjmuje ("przydzielone").
     * Na koniec, jeżeli nie udało się przydzielić jakiegoś zamówienia, to generowany jest dla niego nowy losowy czas, aby następnym razem być może mogło zostać obsłużone.
     */
    public synchronized void sprawdzZamowienia() {
        int liczbaDostawcowObslugujacychZlecenia = 0;
        int liczbaObsluzonychZlecen = 0;
        for (Dostawca i : listaDostawcow) {
            if (i.getStatus() == Status.DOSTEPNY) {
                przydzielPojazd(i);
                if (i.getPojazd() == null) {
                    System.err.println("Nie mozna zrealizowac zamowienia, nie ma pojazdow");
                    return;
                }
                int liczbaZamowienDostawcy = 0;
                int maxLiczbaZamowien = i.getPojazd().getLadownosc();
                List<Zamowienie> listaZamowienDostawcy = new ArrayList<Zamowienie>();
                for (CzasPracy j : i.getListaCzasuPracy()) {
                    for (Zamowienie z : listaZamowien) {
                        if (z.getStatus() == StatusZamowienia.OCZEKUJACE) {
                            if (liczbaZamowienDostawcy <= maxLiczbaZamowien) {
                                if (j.getDzien() == z.getCzasZamowienia().getDzien()) {
                                    if (z.getCzasZamowienia().getCzas() > j.getCzasRozpoczecia() && z.getCzasZamowienia().getCzas() < (j.getCzasRozpoczecia() + j.getCzasTrwania())) {
                                        System.out.println("Dla zamowienia " + z.getNumer() + " znalazlem dostawce: " + i.getImie() + " " + i.getNazwisko());
                                        System.out.println("Przydzielilem pojazd: " + i.getPojazd().getNrRejestracyjny());
                                        System.out.println("Ustalilem cel: " + z.getDestination());
                                        z.setIdDostawcy(i.getID());
                                        z.setStatus(StatusZamowienia.PRZYDZIELONE);
                                        listaZamowienDostawcy.add(z);
                                        liczbaZamowienDostawcy++;
                                        liczbaObsluzonychZlecen++;
                                    }
                                }
                            }
                        }
                    }
                }
                if (liczbaZamowienDostawcy > 0) {
                    liczbaDostawcowObslugujacychZlecenia++;
                    i.setListaZamowien(listaZamowienDostawcy);
                    i.setStatus(Status.TRASA);
                } else {
                    zwrocPojazd(i);
                }
            }
        }
        for (Zamowienie i : listaZamowien) {
            if (i.getStatus() == StatusZamowienia.OCZEKUJACE) {
                i.setCzasZamowienia(new CzasZamowienia());
            }
        }
        System.out.println("Przydzielono " + liczbaObsluzonychZlecen + " zlecen " + liczbaDostawcowObslugujacychZlecenia + " dostawcom");
        notifyOrderListeners();
    }

    /**
     * Przesłonięcie metody run dla wątku restauracji.
     * Restauracja "śpi", a następnie sprawdza zamówienia, w sposób zaimplementowany w metodzie o tej samej nazwie ("sprawdzZamowienia").
     */
    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(random.nextInt(10) * 1000);
                sprawdzZamowienia();
            } catch (InterruptedException ex) {
                Logger.getLogger(Czlowiek.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Zwraca listę pojazdów, należących do restauracji.
     * @return 
     */
    public synchronized List<Pojazd> getListaPojazdow() {
        return listaPojazdow;
    }

    /**
     * Ustala listę pojazdów, należących do restauracji.
     * @param listaPojazdow 
     */
    public synchronized void setListaPojazdow(List<Pojazd> listaPojazdow) {
        this.listaPojazdow = listaPojazdow;
    }

    /**
     * Metoda powiadamiająca listenerów o zmianach w zamówieniach.
     */
    private synchronized void notifyOrderListeners() {
        for (OrdersListener i : ordersListenersList) {
            i.ordersChanged();
        }
    }

}
