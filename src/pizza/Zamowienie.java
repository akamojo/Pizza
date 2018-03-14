/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Klasa definiująca obiekt zamówienia.
 *
 * @author akamojo
 */
public class Zamowienie implements Comparable<Zamowienie>, Serializable {

    private int numer;
    private List<Posilek> listaPosilkow;
    private float koszt;
    private float cenaDowozu;
    private float znizka;
    private StatusZamowienia status;
    private CzasZamowienia czasZamowienia;
    private int idKlienta;
    private int idDostawcy;
    private Adres destination;

    /**
     * Wypisanie wszystkich posiłków składających się na zamówienie.
     */
    public void wypiszPosilki() {
        for (Posilek i : listaPosilkow) {
            i.wypiszPosilek();
        }
    }

    /**
     * Wypisanie wszystkich informacji o zamówieniu.
     */
    public void wypiszZamowienie() {
        System.out.println("Zamówienie nr " + getNumer() + ": " + getKoszt() + " zl, status zamowienia: " + getStatus().toString() + " Czas zamówienia: " + getCzasZamowienia().getDzien().toString() + " " + getCzasZamowienia().getCzas());
        System.out.println("Cena dowozu: " + getCenaDowozu() + " zl, znizka: " + getZnizka() + " zl");
        System.out.println("Posiłki:");
        wypiszPosilki();
    }

    /**
     * Zwraca status zamówienia.
     *
     * @return
     */
    public StatusZamowienia getStatus() {
        return status;
    }

    /**
     * Ustala listę posiłków.
     *
     * @param listaPosilkow
     */
    public void setListaPosilkow(List<Posilek> listaPosilkow) {
        this.listaPosilkow = listaPosilkow;
    }

    /**
     * Ustala zniżkę.
     *
     * @param znizka
     */
    public void setZnizka(float znizka) {
        this.znizka = znizka;
    }

    /**
     * Ustala status zamówienia.
     *
     * @param status
     */
    public void setStatus(StatusZamowienia status) {
        this.status = status;
    }

    /**
     * Przesłonięcie metody hashCode.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.numer;
        return hash;
    }

    /**
     * Przesłonięcie metody equals dla obiektu klasy Zamówienie. Zamówienia są
     * różne jeśli mają różne numery.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Zamowienie other = (Zamowienie) obj;
        if (this.numer != other.numer) {
            return false;
        }
        return true;
    }

    /**
     * Generowanie losowego zamówienia dla danego klienta.
     *
     * @param klient
     */
    public Zamowienie(Klient klient) {
        Random random = new Random();
        this.status = StatusZamowienia.NOWE;
        this.numer = Pizza.at.getAndIncrement();
        this.czasZamowienia = new CzasZamowienia();
        listaPosilkow = new LinkedList<Posilek>();
        int n = random.nextInt(3) + 1;
        for (int i = 0; i < n; i++) {
            listaPosilkow.add(Restauracja.getInstance().getListaPosilkow().get(random.nextInt(Restauracja.getInstance().getListaPosilkow().size())));
        }
        this.koszt = obliczanieKosztu();
        this.idKlienta = klient.getID();
        this.destination = klient.getAdres();
    }

    /**
     * Tworzenie zamówienia o zadanych parametrach.
     *
     * @param numer
     * @param listaPosilkow
     * @param klient
     * @param czasZamowienia
     */
    public Zamowienie(int numer, List<Posilek> listaPosilkow, Klient klient, CzasZamowienia czasZamowienia) {
        this.numer = numer;
        this.czasZamowienia = czasZamowienia;
        this.status = StatusZamowienia.NOWE;
        this.listaPosilkow = listaPosilkow;
        this.koszt = obliczanieKosztu();
    }

    /**
     * @return the numer
     */
    public int getNumer() {
        return numer;
    }

    /**
     * @param numer the numer to set
     */
    public void setNumer(int numer) {
        this.numer = numer;
    }

    /**
     * Ustalanie kosztu zamówienia.
     *
     * @param koszt
     */
    public void setKoszt(float koszt) {
        this.koszt = koszt;
    }

    /**
     * Ustalanie ceny dowozu.
     *
     * @param cenaDowozu
     */
    public void setCenaDowozu(float cenaDowozu) {
        this.cenaDowozu = cenaDowozu;
    }

    /**
     * @return the listaPosilkow
     */
    public List<Posilek> getListaPosilkow() {
        return listaPosilkow;
    }

    /**
     * @return the koszt
     */
    public float getKoszt() {
        return koszt;
    }

    /**
     * @param koszt the koszt to set
     */
    public float obliczanieKosztu() {
        float cena = 0;
        for (Posilek i : listaPosilkow) {
            cena += i.getCena();
        }
        cena -= obliczanieZnizki();
        cena += obliczanieCenyDowozu();
        Pizza.zaokraglenie(cena);
        return cena;
    }

    /**
     * @return the cenaDowozu
     */
    public float getCenaDowozu() {
        return cenaDowozu;
    }

    /**
     * @param cenaDowozu the cenaDowozu to set
     */
    public float obliczanieCenyDowozu() {
        return cenaDowozu;
    }

    /**
     * @return the znizka
     */
    public float getZnizka() {
        return znizka;
    }

    /**
     * @param znizka the znizka to set
     */
    public float obliczanieZnizki() {
        return znizka;
    }

    /**
     * Dodanie posiłku do zamówienia.
     *
     * @param posilek
     */
    public void dodajPosilek(Posilek posilek) {
        listaPosilkow.add(posilek);
    }

    /**
     * Zwraca czas zamówienia.
     *
     * @return
     */
    public CzasZamowienia getCzasZamowienia() {
        return czasZamowienia;
    }

    /**
     * Ustala czas zamówienia.
     *
     * @param czasZamowienia
     */
    public void setCzasZamowienia(CzasZamowienia czasZamowienia) {
        this.czasZamowienia = czasZamowienia;
    }

    /**
     * Funkcja służąca do porównywania zamówień
     * @param t
     * @return 
     */
    @Override
    public int compareTo(Zamowienie t) {
        return (this).compareTo(t);
    }

    /**
     * Zwraca ID klienta do którego przypisane jest zamówienie.
     * @return 
     */
    public int getIdKlienta() {
        return idKlienta;
    }

    /**
     * Ustala ID klienta do którego ma być przypisane zamówienie.
     * @param idKlienta 
     */
    public void setIdKlienta(int idKlienta) {
        this.idKlienta = idKlienta;
    }

    /**
     * Zwraca ID dostawcy przydzielonego do zamówienia.
     * @return 
     */
    public int getIdDostawcy() {
        return idDostawcy;
    }

    /**
     * Ustala ID dostawcy który ma być przydzielony do zamówienia.
     * @param idDostawcy 
     */
    public void setIdDostawcy(int idDostawcy) {
        this.idDostawcy = idDostawcy;
    }

    /**
     * Zwraca adres pod który ma zostać dostarczone zamówienie.
     * @return 
     */
    public Adres getDestination() {
        return destination;
    }

    /**
     * Ustala adres pod który ma zostać dostarczone zamówienie.
     * @param destination 
     */
    public void setDestination(Adres destination) {
        this.destination = destination;
    }
}
