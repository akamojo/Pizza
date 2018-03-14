/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Klasa reprezentująca posiłek.
 * @author akamojo
 */
public class Posilek implements Serializable {

    private String nazwa;
    private HashSet<Skladniki> listaSkladnikow;
    private float cena;
    private Kategoria kategoria;
    private Rozmiar rozmiar;
    
    /**
     * Wypisuje składniki składające się na posiłek (z listy składników).
     */
    public void wypiszSkladniki(){
        for (Skladniki i : listaSkladnikow){
            System.out.println(i.toString());
        }
    }
    
    /**
     * Wypisuje informacje o posiłku.
     */
    public void wypiszPosilek(){
        System.out.println(getNazwa() + " " + getCena() + " zl, kategoria: " + getKategoria().toString() + " rozmiar: " + getRozmiar().toString());
        System.out.println("Składniki:");
        wypiszSkladniki();
    }

    /**
     * @return the nazwa
     */
    public String getNazwa() {
        return nazwa;
    }

    /**
     * @param nazwa the nazwa to set
     */
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Set<Skladniki> getSetSkladnikow() {
        return listaSkladnikow;
    }

    /**
     * @return the cena
     */
    public float getCena() {
        return cena;
    }

    /**
     * @param cena the cena to set
     */
    public void setCena(float cena) {
        this.cena = cena;
    }

    /**
     * @return the kategoria
     */
    public Kategoria getKategoria() {
        return kategoria;
    }

    /**
     * @param kategoria the kategoria to set
     */
    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    /**
     * @return the rozmiar
     */
    public Rozmiar getRozmiar() {
        return rozmiar;
    }

    /**
     * @param rozmiar the rozmiar to set
     */
    public void setRozmiar(Rozmiar rozmiar) {
        this.rozmiar = rozmiar;
    }

    /**
     * Tworzenie losowego posiłku.
     * (Losowa jest również ilość składników.
     */
    public Posilek() {
        Random random = new Random();
        StringGenerator randomS = new StringGenerator();
        this.nazwa = randomS.generate(10);
        this.cena = Pizza.zaokraglenie(random.nextFloat() + 10);
        this.rozmiar = Rozmiar.values()[random.nextInt(Rozmiar.values().length)];
        this.kategoria = Kategoria.values()[random.nextInt(Kategoria.values().length - 1)];
        listaSkladnikow = new HashSet<Skladniki>();
        int n = random.nextInt(Skladniki.values().length - 1) + 2;
        for (int i = 0; i < n; i++) {
            listaSkladnikow.add(Skladniki.values()[random.nextInt(Skladniki.values().length)]);
        }
    }

    /**
     * Tworzenie posiłku o zadanych parametrach.
     * Lista składników generowana jest losowo.
     * @param nazwa
     * @param cena
     * @param kategoria
     * @param rozmiar 
     */
    public Posilek(String nazwa, float cena, Kategoria kategoria, Rozmiar rozmiar) {
        Random random = new Random();
        this.nazwa = nazwa;
        this.cena = cena;
        this.kategoria = kategoria;
        this.rozmiar = rozmiar;
        listaSkladnikow = new HashSet<Skladniki>();
        int n = random.nextInt(Skladniki.values().length - 1) + 2;
        for (int i = 0; i < n; i++) {
            listaSkladnikow.add(Skladniki.values()[random.nextInt(Skladniki.values().length)]);
        }
    }

}
