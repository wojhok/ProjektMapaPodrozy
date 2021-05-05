package Vehicle;

import Lotnisko.Lotnisko;
import Mapa.GrafTrasSamolot;
import Mapa.TrasaSamolot;

import java.util.*;

/***
 * Klasa samolotu wojskowego
 */
public class SamolotWojskowy extends Samolot
{

    private String typ_uzbrojenia;
    String nazwaStatkuTworzacego;
    ArrayList<Statek> statki;
    String miejsceStartu;

    /***
     * Kosntruktor samolotu Wojskowego
     * @param liczba_peronelu
     * @param nazwa
     * @param cel
     * @param typ_uzbrojenia
     * @param dostepneLotniska lotniska dostepne na mapie
     * @param grafTrasSamolot
     * @param dostepneTrasySamolot dostepne trasy na mapie
     * @param predkosc
     * @param nazwaStatkuTworzacego
     * @param statki
     */
    public SamolotWojskowy( int liczba_peronelu, String nazwa, String cel,
                            String typ_uzbrojenia, HashMap<String, Lotnisko> dostepneLotniska, GrafTrasSamolot grafTrasSamolot,
                            HashMap<HashSet<String>, TrasaSamolot> dostepneTrasySamolot, int predkosc,String nazwaStatkuTworzacego,
                            ArrayList<Statek> statki)
    {
        super(liczba_peronelu,nazwa,cel,dostepneLotniska,grafTrasSamolot,dostepneTrasySamolot, predkosc);
        this.typ_uzbrojenia = typ_uzbrojenia;
        this.nazwaStatkuTworzacego = nazwaStatkuTworzacego;
        this.statki = statki;
        Lotniskowiec statekTworzacy = znajdzLotniskowiec();
        this.miejsceStartu = statekTworzacy.znajdzNajblizeszeMiasto();
        ArrayList<String> trasaLotniska = planujTrasaLotniska(miejsceStartu);
        setTrasaLotniska(trasaLotniska);
        ArrayList<TrasaSamolot> trasy = planujTrase();
        setTrasa(trasy);
        tworz(statekTworzacy,miejsceStartu,dostepneLotniska);
    }

    private Lotniskowiec znajdzLotniskowiec() {
        Lotniskowiec statekTworzacy = new Lotniskowiec();
        for(Statek statek: statki)
        {
            if(statek instanceof Lotniskowiec && statek.getNazwa().equals(nazwaStatkuTworzacego))
            {
                statekTworzacy = (Lotniskowiec) statek;
            }
        }
        return  statekTworzacy;
    }

    /***
     * Metoda tworząca samolot w miejscu wybranego lotniskowca ustala ona takze współrzędne do dołaczenia do ruchu lotniczego
     * @param statekTworzacy
     * @param miejsceStartu
     * @param lotniska
     */
    public void tworz(Lotniskowiec statekTworzacy,String miejsceStartu,HashMap<String,Lotnisko> lotniska)
    {
        setX(statekTworzacy.getX());
        setY(statekTworzacy.getY());
        setNastepneLotnisko(getTrasaLotniska().get(0));
        setTrasaAktualna(getTrasa().get(0));
        setKolejneWspolrzedne(laduj(getNastepneLotnisko()));
        Lotnisko lotniskoStart = lotniska.get(miejsceStartu+"Wojskowe");
        lotniskoStart.setAktualnaLiczbaSamolotow(lotniskoStart.getAktualnaLiczbaSamolotow()+1);
        setStanPodrozy("laduje");
    }

    /***
     * getter typu uzbrojenia samolotu
     * @return
     */
    public String getTyp_uzbrojenia() {
        return typ_uzbrojenia;
    }

}
