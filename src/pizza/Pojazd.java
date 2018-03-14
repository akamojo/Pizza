/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.Random;

/**
 * Klasa definiująca pojazd.
 * @author akamojo
 */
public abstract class Pojazd implements Serializable {

    private String nazwa;
    private int ladownosc;
    private float predkosc;
    private String nrRejestracyjny;
    private float pojemnoscBaku;
    private float stanBaku = 0;
    private Status status;

    /**
     * Wypisanie podstawowych informacji o pojeździe.
     */
    public void wypiszPojazd() {
        System.out.println(this.getNazwa() + " nr: " + this.getNrRejestracyjny() + "ladownosc: " + this.getLadownosc() + " prędkość : " + this.getPredkosc() + " km/h, pojemność baku: " + this.getPojemnoscBaku());
        System.out.println("Stan baku: " + this.getStanBaku());
    }

    /**
     * Tworzenie losowego pojazdu.
     * Jego początkowy status to "dostępny".
     */
    public Pojazd() {
        Random random = new Random();
        StringGenerator randomS = new StringGenerator();
        this.nazwa = randomS.generate(8);
        this.ladownosc = random.nextInt(10) + 1;
        this.predkosc = random.nextInt(300);
        this.nrRejestracyjny = randomS.generate(5) + Integer.toString(random.nextInt(500));
        this.pojemnoscBaku = random.nextInt(300) + 500;
        this.status = Status.DOSTEPNY;
    }

    /**
     * Tworzenie pojazdu o zadanych parametrach
     * @param nazwa
     * @param ladownosc
     * @param predkosc
     * @param nrRejestracyjny
     * @param pojemnoscBaku 
     */
    public Pojazd(String nazwa, int ladownosc, float predkosc, String nrRejestracyjny, float pojemnoscBaku) {
        this.nazwa = nazwa;
        this.ladownosc = ladownosc;
        this.predkosc = predkosc;
        this.nrRejestracyjny = nrRejestracyjny;
        this.pojemnoscBaku = pojemnoscBaku;
    }

    /**
     * Tankowanie samochodu.
     */
    public void zatankuj() {
        stanBaku = pojemnoscBaku;
    }

    /**
     * @return the ladownosc
     */
    public int getLadownosc() {
        return ladownosc;
    }

    /**
     * @param ladownosc the ladownosc to set
     */
    public void setLadownosc(int ladownosc) {
        this.ladownosc = ladownosc;
    }

    /**
     * @return the predkosc
     */
    public float getPredkosc() {
        return predkosc;
    }

    /**
     * @param predkosc the predkosc to set
     */
    public void setPredkosc(float predkosc) {
        this.predkosc = predkosc;
    }

    /**
     * @return the nrRejestracyjny
     */
    public String getNrRejestracyjny() {
        return nrRejestracyjny;
    }

    /**
     * @param nrRejestracyjny the nrRejestracyjny to set
     */
    public void setNrRejestracyjny(String nrRejestracyjny) {
        this.nrRejestracyjny = nrRejestracyjny;
    }

    /**
     * @return the pojemnoscBaku
     */
    public float getPojemnoscBaku() {
        return pojemnoscBaku;
    }

    /**
     * @param pojemnoscBaku the pojemnoscBaku to set
     */
    public void setPojemnoscBaku(float pojemnoscBaku) {
        this.pojemnoscBaku = pojemnoscBaku;
    }

    /**
     * @return the stanBaku
     */
    public float getStanBaku() {
        return stanBaku;
    }

    /**
     * @param stanBaku the stanBaku to set
     */
    public void setStanBaku(float stanBaku) {
        this.stanBaku = stanBaku;
    }

    /**
     * Zwraca nazwę pojazdu.
     * @return 
     */
    public String getNazwa() {
        return nazwa;
    }

    /**
     * Zwraca status pojazdu.
     * @return 
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Ustala status pojazdu.
     * @param status 
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
