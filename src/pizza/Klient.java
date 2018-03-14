/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa dziedzicząca z człowieka, określająca jeden z podstawowych obiektów występujących w programie.
 * @author akamojo
 */
public abstract class Klient extends Czlowiek implements Serializable {

    private int numerTelefonu;
    private Adres adres;
    private Zamowienie zamowienie;
    private String email;
    private RodzajeKlientow rodzaj;

    /**
     * Wypisanie danych klienta.
     */
    @Override
    public void wypiszDane() {
        super.wypiszDane();
        System.out.println("Rodzaj: " + getRodzaj().toString());
        System.out.println("Adres:");
        adres.wypiszAdres();
        zamowienie.wypiszZamowienie();
        System.out.println("Email: " + getEmail());
    }

    /**
     * Składanie zamówienia przez klienta, oraz zapamiętywanie go w swoim polu.
     * @return 
     */
    public Zamowienie zamow() {
        this.zamowienie = new Zamowienie(this);
        return zamowienie;
    }

    /**
     * Ustalanie numeru telefonu.
     * @param numerTelefonu 
     */
    public void setNumerTelefonu(int numerTelefonu) {
        this.numerTelefonu = numerTelefonu;
    }

    /**
     * Ustalanie adresu.
     * @param adres 
     */
    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    /**
     * Generowanie losowego klienta.
     * Jego rodzaj jest nieokreślony gdyż Klient jest klasą abstrakcyjną.
     * W programie są tylko wystąpienia klientów o określonym rodzaju.
     */
    public Klient() {
        super();
        this.rodzaj = RodzajeKlientow.NIEOKRESLONY;
        this.zamowienie = null;
        Random random = new Random();
        StringGenerator randomS = new StringGenerator();
        this.adres = new Adres();
        this.numerTelefonu = random.nextInt(1000000);
        String email = randomS.generate(7);
        email += "@gmail.com";
        this.email = email;
    }

    /**
     * Ustalanie maila klienta.
     * @param email 
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Tworzenie klienta o zadanych parametrach.
     * @param imie
     * @param nazwisko
     * @param rodzaj
     * @param numerTelefonu
     * @param adres
     * @param email 
     */
    public Klient(String imie, String nazwisko, RodzajeKlientow rodzaj, int numerTelefonu, Adres adres, String email) {
        super(imie, nazwisko);
        this.rodzaj = rodzaj;
        this.zamowienie = null;
        this.adres = adres;
        this.numerTelefonu = numerTelefonu;
        this.email = email;
    }

    /**
     * Zwraca numer telefonu.
     * @return 
     */
    public int getNumerTelefonu() {
        return numerTelefonu;
    }

    /**
     * Zwraca adres zamieszkania klienta.
     * @return 
     */
    public Adres getAdres() {
        return adres;
    }

    /**
     * Zwraca zamówienie przypisane do klienta.
     * @return 
     */
    public Zamowienie getZamowienie() {
        return zamowienie;
    }

    /**
     * Zwraca email klienta.
     * @return 
     */
    public String getEmail() {
        return email;
    }

    /**
     * Zwraca rodzaj klienta.
     * @return 
     */
    public RodzajeKlientow getRodzaj() {
        return rodzaj;
    }

    /**
     * Ustala rodzaj klienta.
     * @param rodzaj 
     */
    public void setRodzaj(RodzajeKlientow rodzaj) {
        this.rodzaj = rodzaj;
    }

    /**
     * Metoda opisująca sposób generowania przez klienta zamówienia.
     * Jeśli żadne zamówienie nie jest do niego aktualnie przypisane lub jest, ale ma status zrealizowany, to zamawia.
     * Następnie prosi restaurację o obsłużenie. Jeśli jest już przypisane do niego zamówienie to klient czeka.
     */
    private void generate() {
        if (zamowienie == null || zamowienie.getStatus() == StatusZamowienia.ZREALIZOWANE) {
            zamow();
            Restauracja.getInstance().obsluzZamowienie(zamowienie);
            System.out.println("Jestem: " + getID() + " i zamowilem!");
        } else {
            System.out.println("" + +getID() + " oczekuje na wygenerowanie zamowienia");
        }
    }

    /**
     * Przesłonięcie metody run.
     * Klient śpi, a później generuje zamówienie w sposób zaimplementowany w metodzie generate.
     */
    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(random.nextInt(20) * 1000);
                generate();
            } catch (InterruptedException ex) {
                Logger.getLogger(Czlowiek.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
