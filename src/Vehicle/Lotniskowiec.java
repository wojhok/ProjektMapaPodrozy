package Vehicle;

import Lotnisko.Lotnisko;
import Mapa.GrafTrasStatek;
import Mapa.MiastoPortowe;
import Mapa.TrasaStatek;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * Klasa Lotniskowca
 */
public class Lotniskowiec extends Statek{
    private String uzbrojenie;
    private HashMap<String,Lotnisko> dostepneLotniska;

    /***
     * Bezargumentowy Konstruktor
     */
    public Lotniskowiec()
    {
        super();
        dostepneLotniska = new HashMap<>();

    }

    /***
     * Konstruktor Lotniskowca
     * @param maks_predkosc
     * @param nazwa
     * @param cel
     * @param start
     * @param grafTrasStatek
     * @param dostepneTrasy Trasy dostępne na mapie
     * @param miastoPortowe Miasta portowe na mapie
     * @param uzbrojenie
     * @param dostepneLotniska dostępne Lotniska na mapie
     */
    public Lotniskowiec(int maks_predkosc, String nazwa, String cel, String start, GrafTrasStatek grafTrasStatek,
                        ArrayList<TrasaStatek> dostepneTrasy, HashMap<String, MiastoPortowe>miastoPortowe,
                        String uzbrojenie,HashMap<String,Lotnisko> dostepneLotniska)
    {
        super(maks_predkosc,nazwa,cel,start,grafTrasStatek,dostepneTrasy,miastoPortowe);
        this.uzbrojenie = uzbrojenie;
        this.dostepneLotniska = dostepneLotniska;
    }

    /***
     * znajdywanie nabliższego miasta z lotniskiem służy do przekierowania tam Samolotu wojskowego który
     * obecnie startuje z lotniskowca
     * @return
     */
    public String znajdzNajblizeszeMiasto() {
        String miejsceStartu = new String();
        int StatekX = getX();
        int StatekY = getY();
        int odlegloscMin = Integer.MAX_VALUE;
        for(Lotnisko lotnisko: dostepneLotniska.values())
        {
            int odlegloscLotnisko = (int)Math.sqrt(Math.pow((lotnisko.getX() - StatekX),2) +Math.pow((lotnisko.getY() - StatekY),2));
            if (odlegloscLotnisko<odlegloscMin)
            {
                odlegloscMin = odlegloscLotnisko;
                miejsceStartu = lotnisko.getNazwa();
            }
        }
        return miejsceStartu;
    }

    /**
     * getter typu uzbrojenia lotniskoca
     * @return
     */
    public String getUzbrojenie() {
        return uzbrojenie;
    }
}
