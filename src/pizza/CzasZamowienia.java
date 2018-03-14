/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.Random;

/**
 * Klasa opisująca możliwy czas zamówienia
 * @author akamojo
 */
public class CzasZamowienia implements Serializable {
    private DniTygodnia dzien;
    private int czas;

    /**
     * Generowanie losowego czasu zamówienia
     */
    public CzasZamowienia() {
        Random random = new Random();
        this.dzien = DniTygodnia.values()[random.nextInt(7)];
        this.czas = random.nextInt(24);
    }

    /**
     * Tworzenie zadanego czasu zamówienia
     * @param dzien
     * @param czas 
     */
    public CzasZamowienia(DniTygodnia dzien, int czas) {
        this.dzien = dzien;
        this.czas = czas;
    }

    /**
     * Zwraca dzień tygodnia, w którym zostało złożone zamówienie
     * @return 
     */
    public DniTygodnia getDzien() {
        return dzien;
    }

    /**
     * Ustawia dzień tygodnia
     * @param dzien 
     */
    public void setDzien(DniTygodnia dzien) {
        this.dzien = dzien;
    }

    /**
     * Zwraca godzinę zamówienia
     * @return 
     */
    public int getCzas() {
        return czas;
    }

    /**
     * Ustawia czas zamówienia
     * @param czas 
     */
    public void setCzas(int czas) {
        this.czas = czas;
    }
    
}
