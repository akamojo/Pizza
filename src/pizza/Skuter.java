/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;

/**
 * Klasa definiująca skuter.
 * @author akamojo
 */
public class Skuter extends Pojazd implements Serializable {
    
    /**
     * Tworzy losowy skuter.
     */
    public Skuter(){
        super();
    }
    
    /**
     * Tworzy skuter o zadanych parametrach.
     * @param nazwa
     * @param ladownosc
     * @param predkosc
     * @param nrRejestracyjny
     * @param pojemnoscBaku 
     */
    public Skuter(String nazwa, int ladownosc, float predkosc, String nrRejestracyjny, float pojemnoscBaku) {
        super(nazwa, ladownosc, predkosc, nrRejestracyjny, pojemnoscBaku);
    }
    
}
