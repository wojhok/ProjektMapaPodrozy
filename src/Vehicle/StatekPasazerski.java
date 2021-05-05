package Vehicle;

import Mapa.GrafTrasStatek;
import Mapa.MiastoPortowe;
import Mapa.TrasaStatek;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * Klasa Statku Pasażerskiego
 */
public class StatekPasazerski extends Statek {
    private int maks_pojemnosc;
    private int licz_pasazer;
    private String firma;

    /***
     * konstruktor Statku Pasażerskiego
     * @param maks_predkosc
     * @param nazwa
     * @param cel
     * @param start
     * @param grafTrasStatek
     * @param dostepneTrasy
     * @param maks_pojemnosc
     * @param miastoPortowe
     * @param licz_pasazer
     * @param firma
     */
    public StatekPasazerski(int maks_predkosc, String nazwa, String cel, String start, GrafTrasStatek grafTrasStatek,
                            ArrayList<TrasaStatek> dostepneTrasy, int maks_pojemnosc, HashMap<String, MiastoPortowe> miastoPortowe,
                            int licz_pasazer, String firma) {
        super(maks_predkosc,nazwa,cel,start,grafTrasStatek,dostepneTrasy,miastoPortowe);
        this.maks_pojemnosc = maks_pojemnosc;
        this.licz_pasazer = licz_pasazer;
        this.firma = firma;
    }

    /***
     * getter liczby pasazerów
     * @return
     */
    public int getLicz_pasazer() {
        return licz_pasazer;
    }

    /***
     * getter nazwy firmy statku
     * @return
     */
    public String getFirma() {
        return firma;
    }

}
