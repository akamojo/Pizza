/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import static pizza.Kategoria.ZESTAW;

/**
 * Szczególny przypadek Posiłku.
 * @author akamojo
 */
public class ZestawObiadowy extends Posilek implements Serializable {
    
    private List<Posilek> listaPosilkow;
    
    /**
     * Wypisuje posiłki składające się na zestaw.
     */
    public void wypiszPosilki(){
        for (Posilek i : this.getListaPosilkow()){
            i.wypiszPosilek();
        }
    }
    
    /**
     * Wypisuje informacje o zestawie.
     */
    public void wypiszZestaw(){
        super.wypiszPosilek();
        System.out.println("Posiłki:");
        this.wypiszPosilki();
    }
    
    /**
     * Generowanie losowego zestawu obiadowego.
     * Narazie jego lista posiłków jest pusta.
     */
    public ZestawObiadowy(){
        super();
        super.setKategoria(ZESTAW);
        listaPosilkow = new LinkedList<Posilek>();
    }
    
    /**
     * Tworzenie zestawu obiadowego o zadanych parametrach.
     * @param nazwa
     * @param cena
     * @param rozmiar 
     */
    public ZestawObiadowy(String nazwa, float cena, Rozmiar rozmiar) {
        super(nazwa, cena, ZESTAW, rozmiar);
        listaPosilkow = new LinkedList<Posilek>();
    }
    
    /**
     * Dodanie posiłku do zestawu.
     * @param posilek 
     */
    public void dodajPosilek(Posilek posilek){
        getListaPosilkow().add(posilek);
    }

    /**
     * @return the listaPosilkow
     */
    public List<Posilek> getListaPosilkow() {
        return listaPosilkow;
    }
    
}
