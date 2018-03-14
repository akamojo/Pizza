/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.Random;

/**
 * Klasa opisująca jednostkę pracy dostawcy.
 * @author akamojo
 */
public class CzasPracy implements Serializable {
    private DniTygodnia dzien;
    private int czasRozpoczecia;
    private int czasTrwania;
    
    /**
     * Losowe generowanie czasu pracy
     */
    public CzasPracy() {
        Random random = new Random();
        this.dzien = DniTygodnia.values()[random.nextInt(7)];
        this.czasRozpoczecia = random.nextInt(24);
        this.czasTrwania = random.nextInt(10) + 1;
    }

    /**
     * Tworzenie zadanego czasu pracy
     * @param dzien
     * @param czasRozpoczecia
     * @param czasTrwania 
     */
    public CzasPracy(DniTygodnia dzien, int czasRozpoczecia, int czasTrwania) {
        this.dzien = dzien;
        this.czasRozpoczecia = czasRozpoczecia;
        this.czasTrwania = czasTrwania;
    }

    /**
     * Zwraca dzień tygodnia
     * @return 
     */
    public DniTygodnia getDzien() {
        return dzien;
    }

    /**
     * Zwraca godzinę rozpoczęcia pracy
     * @return 
     */
    public int getCzasRozpoczecia() {
        return czasRozpoczecia;
    }

    /**
     * Zwraca czas trwania pracy w godzinach
     * @return 
     */
    public int getCzasTrwania() {
        return czasTrwania;
    }

    /**
     * Ustawia zadany dzień tygodnia
     * @param dzien 
     */
    public void setDzien(DniTygodnia dzien) {
        this.dzien = dzien;
    }

    /**
     * Ustawia godzinę rozpoczęcia
     * @param czasRozpoczecia 
     */
    public void setCzasRozpoczecia(int czasRozpoczecia) {
        this.czasRozpoczecia = czasRozpoczecia;
    }

    /**
     * Ustawia czas trwania w godzinach
     * @param czasTrwania 
     */
    public void setCzasTrwania(int czasTrwania) {
        this.czasTrwania = czasTrwania;
    }
    
    /**
     * Wypisuje pojedynczy czas pracy
     */
    public void wypiszCzasPracy(){
        System.out.println(this.getDzien().toString() + " Start: " + this.getCzasRozpoczecia() + " Czas: " + this.getCzasTrwania() + "h");
    }
    
}
