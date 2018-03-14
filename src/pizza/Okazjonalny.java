/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.Random;

/**
 * Rodzaj klienta.
 * @author akamojo
 */
public class Okazjonalny extends Klient implements Serializable {

    private int kod;
    
    /**
     * Wypisuje dane o kliencie okazjonalnym.
     */
    @Override
    public void wypiszDane(){
        super.wypiszDane();
        System.out.println("Kod: " + getKod());
    }

    /**
     * Tworzenie losowego klienta okazjonalnego.
     */
    public Okazjonalny() {
        super();
        super.setRodzaj(RodzajeKlientow.OKAZJONALNY);
        Random random = new Random();
        this.kod = random.nextInt(100);
    }

    /**
     * Tworzenie klienta okazjonalnego o zadanych parametrach.
     * @param imie
     * @param nazwisko
     * @param numerTelefonu
     * @param adres
     * @param kod
     * @param email 
     */
    public Okazjonalny(String imie, String nazwisko, int numerTelefonu, Adres adres, int kod, String email) {
        super(imie, nazwisko, RodzajeKlientow.OKAZJONALNY, numerTelefonu, adres, email);
        this.kod = kod;
    }

    /**
     * Zwraca kod klienta.
     * @return 
     */
    public int getKod() {
        return kod;
    }

    /**
     * Ustala kod klienta.
     * @param kod 
     */
    public void setKod(int kod) {
        this.kod = kod;
    }

}
