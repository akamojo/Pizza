/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;

/**
 * Klient stały, dziedziący z klasy klient.
 * @author akamojo
 */
public class Staly extends Klient implements Serializable {

    private int punktyLojalnosciowe = 0;
    private int znizka = 0;
    
    /**
     * Wypisanie informacji o kliencie stałym.
     */
    @Override
    public void wypiszDane(){
        super.wypiszDane();
        System.out.println("Punkty lojalnościowe: " + this.getPunktyLojalnosciowe() + " zniżka: " + this.getZnizka());
    }

    /**
     * Generowanie losowego klienta stałego.
     */
    public Staly() {
        super();
        super.setRodzaj(RodzajeKlientow.STALY);
    }

    /**
     * Tworzenie klienta stałego o zadanych parametrach.
     * @param imie
     * @param nazwisko
     * @param numerTelefonu
     * @param adres
     * @param email 
     */
    public Staly(String imie, String nazwisko, int numerTelefonu, Adres adres, String email) {
        super(imie, nazwisko, RodzajeKlientow.STALY, numerTelefonu, adres, email);
    }
    

    /**
     * @return the punktyLojalnosciowe
     */
    public int getPunktyLojalnosciowe() {
        return punktyLojalnosciowe;
    }

    /**
     * @param punktyLojalnosciowe the punktyLojalnosciowe to set
     */
    public void setPunktyLojalnosciowe(int punktyLojalnosciowe) {
        this.punktyLojalnosciowe = punktyLojalnosciowe;
    }

    /**
     * @return the znizka
     */
    public int getZnizka() {
        return znizka;
    }

    /**
     * @param znizka the znizka to set
     */
    public void setZnizka(int znizka) {
        this.znizka = znizka;
    }

}
