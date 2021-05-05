package Vehicle;
import Lotnisko.Lotnisko;
import Mapa.GrafTrasSamolot;
import Mapa.Skrzyzowanie;
import Mapa.TrasaSamolot;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.lang.Math;
import java.util.Timer;

public class SamolotPasazerski extends Samolot {

    private int maks_pojemnosc;
    private int licz_pasazer;
    private String miejsceStartu;
    private String firma;





    public SamolotPasazerski( int liczba_peronelu, String nazwa,
                             int maks_pojemnosc, int licz_pasazer, String firma,
                             String miejsceStartu, String cel, HashMap<String,Lotnisko> dostepneLotniska,
                             GrafTrasSamolot grafTrasSamolot, HashMap<HashSet<String>,TrasaSamolot> dostepneTrasySamolot,
                             int predkosc)
    {
        super(liczba_peronelu,nazwa,cel,dostepneLotniska,grafTrasSamolot,dostepneTrasySamolot,predkosc);
        this.maks_pojemnosc = maks_pojemnosc;
        this.licz_pasazer = licz_pasazer;
        this.firma = firma;
        this.miejsceStartu = miejsceStartu;
        ArrayList<String> trasaLotniska = planujTrasaLotniska(miejsceStartu);
        setTrasaLotniska(trasaLotniska);
        ArrayList<TrasaSamolot> trasy = planujTrase();
        setTrasa(trasy);
        tworz(miejsceStartu,dostepneLotniska);
    }

    public void tworz(String miejsceStartu,HashMap<String,Lotnisko> lotniska)
    {
        Lotnisko lotniskoStart = lotniska.get(miejsceStartu+"Cywilne");
        lotniskoStart.setAktualnaLiczbaSamolotow(lotniskoStart.getAktualnaLiczbaSamolotow()+1);
        setX(lotniskoStart.getX());
        setY(lotniskoStart.getY());
        setObecneLotnisko(getTrasaLotniska().get(0));
        setNastepneLotnisko(getTrasaLotniska().get(1));
        setTrasaAktualna(super.getTrasa().get(0));
        setStanPodrozy("wyladowal");
    }


    public int getLicz_pasazer() {
        return licz_pasazer;
    }

    public String getFirma() {
        return firma;
    }

    public void setLicz_pasazer(int licz_pasazer) {
        this.licz_pasazer = licz_pasazer;
    }

    public int getMaks_pojemnosc() {
        return maks_pojemnosc;
    }
}