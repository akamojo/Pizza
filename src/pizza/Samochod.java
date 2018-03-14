/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;

/**
 * Klasa reprezentująca samochód.
 * Dziedziczy z pojazdu.
 * @author akamojo
 */
public class Samochod extends Pojazd implements Serializable {
    
    /**
     * Tworzy losowy samochód.
     */
    public Samochod(){
        super();
    }
    
    /**
     * Tworzy samochód o zadanych parametrach.
     * @param nazwa
     * @param ladownosc
     * @param predkosc
     * @param nrRejestracyjny
     * @param pojemnoscBaku 
     */
    public Samochod(String nazwa, int ladownosc, float predkosc, String nrRejestracyjny, float pojemnoscBaku) {
        super(nazwa, ladownosc, predkosc, nrRejestracyjny, pojemnoscBaku);
    }
    
}
