/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author akamojo
 */
public class Adres implements Serializable {

    private String ulica;
    private int numerDomu;
    private int pozycjaX;
    private int pozycjaY;

    /**
     * Tworzenie losowego adresu.
     */
    public Adres() {
        Random random = new Random();
        StringGenerator randomS = new StringGenerator();
        this.ulica = randomS.generate(10);
        this.numerDomu = random.nextInt(10) + 1;
        this.pozycjaX = random.nextInt(10) * 30;
        this.pozycjaY = random.nextInt(10) * 30;
    }

    /**
     * Tworzenie adresu o zadanych parametrach
     * @param ulica
     * @param numerDomu
     * @param pozycjaX
     * @param pozycjaY 
     */
    public Adres(String ulica, int numerDomu, int pozycjaX, int pozycjaY) {
        this.ulica = ulica;
        this.numerDomu = numerDomu;
        this.pozycjaX = pozycjaX;
        this.pozycjaY = pozycjaY;
    }

    /**
     * Zwraca nazwę ulicy
     * @return 
     */
    public String getUlica() {
        return ulica;
    }
    
    /**
     * Zwraca numer domu
     * @return 
     */
    public int getNumerDomu() {
        return numerDomu;
    }
    
    /**
     * Zwraca współrzędną X położenia na mapie
     * @return 
     */
    public int getPozycjaX() {
        return pozycjaX;
    }

    /**
     * Zwraca współrzędną Y położenia na mapie
     * @return 
     */
    public int getPozycjaY() {
        return pozycjaY;
    }
    /**
     * Ustawia nazwę ulicy
     * @param ulica 
     */
    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    /**
     * Ustawia numer domu
     * @param numerDomu 
     */
    public void setNumerDomu(int numerDomu) {
        this.numerDomu = numerDomu;
    }

    /**
     * Ustawia współrzędną X
     * @param pozycjaX 
     */
    public void setPozycjaX(int pozycjaX) {
        this.pozycjaX = pozycjaX;
    }

    /**
     * Ustawia współrzędną Y
     * @param pozycjaY 
     */
    public void setPozycjaY(int pozycjaY) {
        this.pozycjaY = pozycjaY;
    }

    /**
     * Wypisuje adres w pełnej formie na konsolę
     */
    public void wypiszAdres() {
        System.out.println("ul. " + this.getUlica() + " " + this.getNumerDomu() + " (" + this.getPozycjaX() + "," + this.getPozycjaY() + ")");
    }

}
