/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.Random;

/**
 * Rodzaj klienta
 * @author akamojo
 */
public class Firmowy extends Klient implements Serializable {

    private Adres adresKorespond;
    private int numerKonta;
    private int regon;
    
    /**
     * Wypisanie danych o kliencie, wraz z tymi szczegółowymi.
     */
    @Override
    public void wypiszDane(){
        super.wypiszDane();
        System.out.println("Adres korespondencyjny:");
        adresKorespond.wypiszAdres();
        System.out.println("Nr konta: " + getNumerKonta() + " REGON: " + getRegon());
    }

    /**
     * Tworzenie losowego klienta firmowego.
     */
    public Firmowy() {
        super();
        super.setRodzaj(RodzajeKlientow.FIRMOWY);
        Random random = new Random();
        this.numerKonta = random.nextInt(100000000);
        this.regon = random.nextInt(50);
        this.adresKorespond = new Adres();
    }

    /**
     * Tworzenie klienta firmowego o zadanych parametrach.
     * @param imie
     * @param nazwisko
     * @param numerTelefonu
     * @param adres
     * @param email
     * @param numerKonta 
     */
    public Firmowy(String imie, String nazwisko, int numerTelefonu, Adres adres, String email, int numerKonta) {
        super(imie, nazwisko, RodzajeKlientow.FIRMOWY, numerTelefonu, adres, email);
        this.numerKonta = numerKonta;
    }

    /**
     * @return the adresKorespond
     */
    public Adres getAdresKorespond() {
        return adresKorespond;
    }

    /**
     * @param adresKorespond the adresKorespond to set
     */
    public void setAdresKorespond(Adres adresKorespond) {
        this.adresKorespond = adresKorespond;
    }

    /**
     * @return the numerKonta
     */
    public int getNumerKonta() {
        return numerKonta;
    }

    /**
     * @param numerKonta the numerKonta to set
     */
    public void setNumerKonta(int numerKonta) {
        this.numerKonta = numerKonta;
    }

    /**
     * @return the regon
     */
    public int getRegon() {
        return regon;
    }

    /**
     * @param regon the regon to set
     */
    public void setRegon(int regon) {
        this.regon = regon;
    }
}
