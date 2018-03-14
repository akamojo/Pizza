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
 * Nadklasa dla klas dostawcy i klienta
 * @author akamojo
 */
public abstract class Czlowiek extends Thread implements Serializable {

    private int id;
    private String imie;
    private String nazwisko;

    /**
     * Generowanie człowieka o losowym imieniu i nazwisku oraz ustalonym (kolejnym), unikatowym ID
     */
    public Czlowiek() {
        this.id = Pizza.at.getAndIncrement();
        StringGenerator randomS = new StringGenerator();
        this.imie = randomS.generate(7);
        this.nazwisko = randomS.generate(14);
    }

    /**
     * Tworzenie człowieka o zadanym imieniu i nazwisku
     * @param imie
     * @param nazwisko 
     */
    public Czlowiek(String imie, String nazwisko) {
        this.id = Pizza.at.getAndIncrement();
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    /**
     * Przesłonięcie metody hashCode
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    /**
     * Przesłonięcie metody equals
     * (Obiekty są inne jeśli mają inne ID)
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
        final Czlowiek other = (Czlowiek) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    /**
     * Czlowiek jest wątkiem.
     * Przesłonięcie metody run - człowiek śpi.
     */
    @Override
    public void run(){
        Random random = new Random();
        while(true){
            try {
                sleep(random.nextInt(10)*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Czlowiek.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Zraca imię
     * @return 
     */
    public String getImie() {
        return imie;
    }

    /**
     * Zwraca nazwisko
     * @return 
     */
    public String getNazwisko() {
        return nazwisko;
    }

    /**
     * Ustawia imię
     * @param imie 
     */
    public void setImie(String imie) {
        this.imie = imie;
    }

    /**
     * Ustawia nazwisko
     * @param nazwisko 
     */
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
    
    /**
     * Wyspisuje podstawowe dane człowieka, czyli imię i nazwisko
     */
    public void wypiszDane(){
        System.out.println(this.getImie() + " " + this.getNazwisko());
    }

    /**
     * Zwraca unikatowe ID
     * @return 
     */
    public int getID() {
        return id;
    }

}
