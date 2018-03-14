/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa reprezentująca dostawcę (dziedziczy z człowieka)
 * @author akamojo
 */
public class Dostawca extends Czlowiek implements Serializable {

    private List<CzasPracy> listaCzasuPracy;
    private Set<PrawoJazdy> listaPrawaJazdy;
    private List<Zamowienie> listaZamowien;
    private Pojazd pojazd;
    private int pesel;
    private int x;
    private int y;
    private Status status;
    private Adres destination = null;
    private boolean powrot;

    /**
     * Metoda go definiuje sposób poruszania się dostawcy po mapie.
     * Wcześniej musi zostać ustalony cel podróży (dom klienta).
     * W trakcie drogi pojazd traci paliwo oraz sprawdzany jest warunek czy obiekt został zmuszony do powrotu, jeśli tak to wychodzi z funkcji.
     */
    public void go() {
        while (x != getDestination().getPozycjaX()) {

            if (x < getDestination().getPozycjaX())
                x++;
            else
                x--;

            pojazd.setStanBaku(pojazd.getStanBaku() - 1);
            if (isPowrot())
                return;

            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                Logger.getLogger(Dostawca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while (y != getDestination().getPozycjaY()) {

            if (y < getDestination().getPozycjaY())
                y++;
            else
                y--;

            pojazd.setStanBaku(pojazd.getStanBaku() - 1);
            if (isPowrot())
                return;

            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                Logger.getLogger(Dostawca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Metoda definiuje sposób powrotu obiektu dostawcy do restauracji.
     * Poruszanie się analogiczne jak dla metody go, jednak nie ma tutaj możliwości przerwania metody, z powodu ustawionego na wartość true powrotu.
     */
    public void goToRestaurant() {
        while (x != getDestination().getPozycjaX()) {

            if (x < getDestination().getPozycjaX())
                x++;
            else
                x--;

            pojazd.setStanBaku(pojazd.getStanBaku() - 1);

            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                Logger.getLogger(Dostawca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while (y != getDestination().getPozycjaY()) {

            if (y < getDestination().getPozycjaY())
                y++;
            else
                y--;

            pojazd.setStanBaku(pojazd.getStanBaku() - 1);

            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                Logger.getLogger(Dostawca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda opisująca sposób dostarczania przez dostawcę konkretnego zamówienia.
     * Cel podróży ustawiany jest na dom klienta. Kiedy już jest dostarczone, jego status zmienia się na "zrealizowane", a klient jest powiadamiany (może znowu zamiawiać).
     * Aktualne zamówienie usuwane jest z listy zamówień przydzielonych do dostawcy.
     * @param zamowienie 
     */
    public void dostarczZamowienie(Zamowienie zamowienie) {
        destination = zamowienie.getDestination();
        System.out.println("Dostarczam zamowienie pod adres: ");
        zamowienie.getDestination().wypiszAdres();
        go();
        if (isPowrot())
            return;
        zatankuj();
        zamowienie.setStatus(StatusZamowienia.ZREALIZOWANE);
        Restauracja.getInstance().powiadom(zamowienie);
        listaZamowien.remove(zamowienie);
    }

    /**
     * Powrót do restauracji
     */
    public void wroc() {
        setDestination(Restauracja.getInstance().getAdres());
        goToRestaurant();
    }

    /**
     * Tankowanie pojazdu do pełna (jeśli bak nie był pełen)
     */
    public void zatankuj() {
        if (pojazd.getStanBaku() != pojazd.getPojemnoscBaku()) {
            pojazd.zatankuj();
        }
    }

    /**
     * Sortowanie listy zamówień według odległości od aktualnego położenia dostawcy.
     * (najbliższe cele zostaną obsłużone najszybciej)
     */
    public void sortListaZamowien() {
        listaZamowien.sort(new KomparatorZamowien());
    }

    /**
     * Przesłonięcie metody run dla wątku dostawcy.
     * Obiekt śpi, a później jeśli jego status został ustawiony na "trasę" i jego lista zamówień nie jest pusta, przechodzi do działania.
     * Najpierw jeśli nie ma pojazdu, to prosi restaurację o przydzielenie go, a potem dopóki nie wyczerpie zamówień mu przydzielonych lub ładowności pojazdu, rozwozi zamówienia.
     * Po powrocie do restauracji ustawia wartość pola "powrót" na false, ustawia swój status oraz status pojazdu na dostępny i zwalnia go.
     */
    @Override
    public void run() {
        Random random = new Random();
        int it;
        while (true) {
            try {
                Thread.sleep(random.nextInt(10) * 1000);
                if (status == Status.TRASA) {
                    if (!listaZamowien.isEmpty()) {
                        if (pojazd == null)
                            Restauracja.getInstance().przydzielPojazd(this);
                        else {
                            it = 0;
                            while (!listaZamowien.isEmpty() && it < pojazd.getLadownosc()) {
                                sortListaZamowien();
                                dostarczZamowienie(listaZamowien.get(0));
                                if (isPowrot())
                                    break;
                                System.out.println("Pojazd: " + pojazd.getNrRejestracyjny() + " stan paliwa: " + pojazd.getStanBaku());
                                it++;
                            }
                            wroc();
                            setPowrot(false);
                            setStatus(Status.DOSTEPNY);
                            System.out.println("Pojazd: " + pojazd.getNrRejestracyjny() + " stan paliwa: " + pojazd.getStanBaku());
                            pojazd.setStatus(Status.DOSTEPNY);
                            pojazd = null;
                        }
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Czlowiek.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Tworzenie losowego dostawcy, z losowymi danymi, losowymi licencjami na pojazdy (i losową ich ilością), pustą listą zamówień i czasu pracy.
     * Jego status ustawiany jest na dostępny, a powrót na false.
     */
    public Dostawca() {
        super();
        Random random = new Random();
        int n = random.nextInt(PrawoJazdy.values().length) + 1;
        listaPrawaJazdy = new HashSet<PrawoJazdy>();
        for (int i = 0; i < n; i++) {
            listaPrawaJazdy.add(PrawoJazdy.values()[random.nextInt(PrawoJazdy.values().length)]);
        }
        this.pesel = random.nextInt(100000);
        listaCzasuPracy = new ArrayList<CzasPracy>();
        listaZamowien = new ArrayList<Zamowienie>();
        this.pojazd = null;
        this.status = Status.DOSTEPNY;
        this.powrot = false;
    }

    /**
     * Ustawia listę czasu pracy.
     * @param listaCzasuPracy 
     */
    public void setListaCzasuPracy(List<CzasPracy> listaCzasuPracy) {
        this.listaCzasuPracy = listaCzasuPracy;
    }

    /**
     * Ustawia listę licencji.
     * @param listaPrawaJazdy 
     */
    public void setListaPrawaJazdy(Set<PrawoJazdy> listaPrawaJazdy) {
        this.listaPrawaJazdy = listaPrawaJazdy;
    }

    /**
     * Ustawia listę zamówień do rozwiezienia.
     * @param listaZamowien 
     */
    public void setListaZamowien(List<Zamowienie> listaZamowien) {
        this.listaZamowien = listaZamowien;
    }

    /**
     * Tworzy dostawcę o zadanych parametrach.
     * @param listaPrawaJazdy
     * @param pesel
     * @param imie
     * @param nazwisko 
     */
    public Dostawca(Set<PrawoJazdy> listaPrawaJazdy, int pesel, String imie, String nazwisko) {
        super(imie, nazwisko);
        this.listaPrawaJazdy = listaPrawaJazdy;
        this.pesel = pesel;
        listaCzasuPracy = new ArrayList<CzasPracy>();
        listaZamowien = new ArrayList<Zamowienie>();
    }

    /**
     * Dodaje czas pracy do listy czasu pracy.
     * @param dzien
     * @param godzinaRozpoczecia
     * @param czasTrwania 
     */
    public void dodajCzasPracy(DniTygodnia dzien, int godzinaRozpoczecia, int czasTrwania) {
        CzasPracy czasPracy = new CzasPracy(dzien, godzinaRozpoczecia, czasTrwania);
        getListaCzasuPracy().add(czasPracy);
    }

    /**
     * Dodaje losowy czas pracy do listy czasu pracy.
     */
    public void dodajLosowyCzasPracy() {
        CzasPracy czasPracy = new CzasPracy();
        getListaCzasuPracy().add(czasPracy);
    }

    /**
     * @return the listaCzasuPracy
     */
    public List<CzasPracy> getListaCzasuPracy() {
        return listaCzasuPracy;
    }

    /**
     * @return the listaZamowien
     */
    public List<Zamowienie> getListaZamowien() {
        return listaZamowien;
    }

    /**
     * @return the pesel
     */
    public int getPesel() {
        return pesel;
    }

    /**
     * @param pesel the pesel to set
     */
    public void setPesel(int pesel) {
        this.pesel = pesel;
    }

    /**
     * Ustala listę licencji na pojazdy.
     * @return 
     */
    public Set<PrawoJazdy> getListaPrawaJazdy() {
        return listaPrawaJazdy;
    }

    /**
     * Wypisuje przydzielone zamówienia.
     */
    public void wypiszZamowienia() {
        for (Zamowienie i : this.getListaZamowien()) {
            i.wypiszZamowienie();
        }
    }

    /**
     * Wypisuje licencje na prowadzenie pojazdów.
     */
    public void wypiszPrawaJazdy() {
        for (PrawoJazdy i : this.getListaPrawaJazdy()) {
            System.out.println(i.toString());
        }
    }

    /**
     * Wypisuje grafik, czyli dni i godziny pracy.
     */
    public void wypiszGrafik() {
        for (CzasPracy i : this.listaCzasuPracy) {
            i.wypiszCzasPracy();
        }
    }

    /**
     * Wypisuje kolejno wszystkie dane o obiekcie.
     */
    @Override
    public void wypiszDane() {
        super.wypiszDane();
        System.out.println("Pesel: " + getPesel());
        System.out.println("Grafik:");
        wypiszGrafik();
        System.out.println("Prawo jazdy:");
        wypiszPrawaJazdy();
        System.out.println("Lista zamowien:");
        wypiszZamowienia();
    }

    /**
     * Zwraca posiadany przez dostawcę pojazd.
     * @return 
     */
    public Pojazd getPojazd() {
        return pojazd;
    }

    /**
     * Ustala dostawcy pojazd.
     * @param pojazd 
     */
    public void setPojazd(Pojazd pojazd) {
        this.pojazd = pojazd;
    }

    /**
     * Zwraca cel podróży.
     * @return 
     */
    public Adres getDestination() {
        return destination;
    }

    /**
     * Ustala cel podóży.
     * @param destination 
     */
    public void setDestination(Adres destination) {
        this.destination = destination;
    }

    /**
     * Zwraca współrzędną X położenia dostawcy na mapie.
     * @return 
     */
    public int getX() {
        return x;
    }

    /**
     * Ustala współrzędną X
     * @param x 
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Zwraca współrzędną Y położenia dostawcy na mapie.
     * @return 
     */
    public int getY() {
        return y;
    }

    /**
     * Ustala współrzędną Y
     * @param y 
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Zwraca status dostawcy.
     * @return 
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Ustala status dostawcy.
     * @param status 
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Sprawdza czy warunek powrót jest spełniony.
     * @return 
     */
    public boolean isPowrot() {
        return powrot;
    }

    /**
     * Ustala czy dostawca ma wrócić do restauracji.
     * @param powrot 
     */
    public void setPowrot(boolean powrot) {
        this.powrot = powrot;
    }

    /**
     * Metoda porównująca zamówienia pod względem odległości od aktualnego położenia dostawcy.
     * Wykorzystywana do sortowania listy zamówień.
     */
    private class KomparatorZamowien implements Comparator<Zamowienie> {

        @Override
        public int compare(Zamowienie o1, Zamowienie o2) {
            return (int) (Point.distance(x, y, o1.getDestination().getPozycjaX(), o1.getDestination().getPozycjaY()) - Point.distance(x, y, o2.getDestination().getPozycjaX(), o2.getDestination().getPozycjaY()));
        }

    }

}
