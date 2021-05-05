package Vehicle;

import Lotnisko.Lotnisko;
import Mapa.GrafTrasSamolot;
import Mapa.Skrzyzowanie;
import Mapa.TrasaSamolot;

import java.util.*;

/***
 * Klasa Samolotu
 */
public abstract class Samolot extends Thread
{
    private ArrayList<TrasaSamolot> trasa;
    private TrasaSamolot trasaAktualna;
    private String obecneLotnisko;
    private String nastepneLotnisko;
    private int stan_paliwa;
    private int bak;
    private int liczba_personelu;
    private int x;
    private int y;
    private int predkosc;
    private String cel;
    private String stanPodrozy;
    private String nazwa;
    private HashMap<String,Lotnisko> dostepneLotniska;
    private GrafTrasSamolot grafTrasSamolot;
    private ArrayList<String> trasaLotniska;
    private HashMap<HashSet<String>,TrasaSamolot> dostepneTrasySamolot;
    private ArrayList<Samolot> pozostaleSamoloty;
    private Vector<int []> kolejneWspolrzedne;

    /***
     * Kontruktor Samolotu
     * @param liczba_peronelu
     * @param nazwa
     * @param cel
     * @param dostepneLotniska wszystkie dostepne lotniska na mapie
     * @param grafTrasSamolot
     * @param dostepneTrasySamolot wszystkie dostepne trasy na mapie
     * @param predkosc
     */
    public Samolot( int liczba_peronelu,String nazwa,
                   String cel,HashMap<String,Lotnisko> dostepneLotniska,GrafTrasSamolot grafTrasSamolot,
                   HashMap<HashSet<String>,TrasaSamolot> dostepneTrasySamolot,int predkosc)
    {
        this.bak =2000;
        this.grafTrasSamolot= grafTrasSamolot;
        this.stan_paliwa = bak;
        this.liczba_personelu = liczba_peronelu;
        this.nazwa = nazwa;
        this.cel = cel;
        this.dostepneLotniska = dostepneLotniska;
        this.trasaLotniska = new ArrayList<>();
        this.dostepneTrasySamolot = dostepneTrasySamolot;
        this.trasa = new ArrayList<>();
        this.kolejneWspolrzedne = new Vector<>();
        this.predkosc = predkosc;

    }

    /***
     * Metoda obsługująca działanie wątku w zależności od stanu w akim znajduje się Samolot
     * Samolot może podróżować, lądować, startować lub też zgłosić usterkę aby nastepnie awayjnie
     * lądować na najbliższym lotnisku i pozostać w stanie zepsutym.
     * Metoda nadzoruje ustawianie kolejnych współrzędnych samolotu oraz podążanie nimi do następnego lotniska, celu
     */
    public void run()
    {
        while(true) {
            int predkosc = getPredkosc();
            long czasOczekiwania = ustawCzasOczekiwania(predkosc);
            if(!(getStanPodrozy().equals("wyladowal")) && !(czyMozeLeciec()))
            {
                continue;
            }
            try {
                Thread.sleep(czasOczekiwania);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getStanPodrozy().equals("startuje") && getKolejneWspolrzedne().isEmpty()) {
                setStanPodrozy("podrozuje");
                setKolejneWspolrzedne(podrozoj(getTrasaAktualna()));
            }
            else if (getStanPodrozy().equals("podrozuje") && !(getKolejneWspolrzedne().isEmpty())) {
                if(!(getTrasaAktualna().getSkrzyzowania().isEmpty()))
                {
                    for (Skrzyzowanie skrzyzowanie: getTrasaAktualna().getSkrzyzowania())
                    {
                        int iloscIteracji = 8;
                        if(getKolejneWspolrzedne().size()<iloscIteracji)
                        {
                            iloscIteracji = getKolejneWspolrzedne().size();
                        }
                        for(int i =0;i<iloscIteracji;i++)
                        {
                            if(Arrays.equals(skrzyzowanie.getWspolrzedneSkrzyzowania(),getKolejneWspolrzedne().get(i)))
                            {
                                skrzyzowanie.uzytkujSkrzyzowanie(this);
                                break;
                            }
                        }
                        continue;
                    }
                }
                int[] wspolrzedne = getKolejneWspolrzedne().remove(0);
                stan_paliwa -= 1;
                setX(wspolrzedne[0]);
                setY(wspolrzedne[1]);
            }
            else if (getStanPodrozy().equals("podrozuje") && getKolejneWspolrzedne().isEmpty()) {
                Lotnisko lotnisko = new Lotnisko();
                if(this instanceof SamolotPasazerski) {
                    lotnisko = getDostepneLotniska().get(getNastepneLotnisko() + "Cywilne");
                }
                else
                {
                    lotnisko = getDostepneLotniska().get(getNastepneLotnisko() + "Wojskowe");
                }
                if(lotnisko.getPojemnosc() >= lotnisko.getAktualnaLiczbaSamolotow()) {
                    lotnisko.setAktualnaLiczbaSamolotow(lotnisko.getAktualnaLiczbaSamolotow() + 1);
                    if (getTrasa().indexOf(getTrasaAktualna()) == getTrasa().size() - 1) {
                        ArrayList<TrasaSamolot> trasa = getTrasa();
                        Collections.reverse(trasa);
                        setTrasa(trasa);
                    }
                    else if (getTrasa().size() > 1) {

                        setTrasaAktualna(getTrasa().get(getTrasa().indexOf(getTrasaAktualna()) + 1));
                    }
                    setStanPodrozy("laduje");
                    setKolejneWspolrzedne(laduj(getNastepneLotnisko()));

                }
                else
                {
                    continue;
                }
            }
            else if (getStanPodrozy().equals("laduje") && !(getKolejneWspolrzedne().isEmpty())) {
                int[] wspolrzedne = getKolejneWspolrzedne().remove(0);
                stan_paliwa-= 1;
                setX(wspolrzedne[0]);
                setY(wspolrzedne[1]);
            }
            else if (getStanPodrozy().equals("laduje") && getKolejneWspolrzedne().isEmpty()) {
                Lotnisko lotnisko = new Lotnisko();
                if(this instanceof SamolotPasazerski) {
                    lotnisko = getDostepneLotniska().get(getNastepneLotnisko() + "Cywilne");
                }
                else
                {
                    lotnisko = getDostepneLotniska().get(getNastepneLotnisko() + "Wojskowe");
                }
                if (getTrasaLotniska().indexOf(getNastepneLotnisko()) == getTrasaLotniska().size() - 1) {
                    ArrayList<String> trasaLotnisko = getTrasaLotniska();
                    Collections.reverse(trasaLotnisko);
                    setTrasaLotniska(trasaLotnisko);
                }
                setObecneLotnisko(getNastepneLotnisko());
                setNastepneLotnisko(getTrasaLotniska().get(getTrasaLotniska().indexOf(getNastepneLotnisko()) + 1));
                setStanPodrozy("wyladowal");
            }
            else if(getStanPodrozy().equals("wyladowal"))
            {
                Lotnisko lotnisko = new Lotnisko();
                if(this instanceof SamolotPasazerski) {
                    lotnisko = getDostepneLotniska().get(getObecneLotnisko() + "Cywilne");
                }
                else
                {
                    lotnisko = getDostepneLotniska().get(getObecneLotnisko() + "Wojskowe");
                }
                lotnisko.Wylot(this);
            }
            else if (getStanPodrozy().equals("usterka"))
            {

                setKolejneWspolrzedne(laduj(znajdzNajblizeszeMiasto()));
                setStanPodrozy("awaria");
            }
            else if(getStanPodrozy().equals("awaria") && !(getKolejneWspolrzedne().isEmpty()))
            {
                int[] wspolrzedne = getKolejneWspolrzedne().remove(0);
                stan_paliwa -=1;
                setX(wspolrzedne[0]);
                setY(wspolrzedne[1]);
            }
            else if(getStanPodrozy().equals("awaria") && getKolejneWspolrzedne().isEmpty())
            {
                setStanPodrozy("zepsuty");
            }
        }
    }

    /***
     * znajdywanie najbliżeszego miasta z lotniskiem przez samolot
     * Metoda wykorzystywana przy awaryjnym lądowaniu
     * @return
     */
    public String znajdzNajblizeszeMiasto() {
        String miejsceAwarii = new String();
        int SamolotX = getX();
        int SamolotY = getY();
        int odlegloscMin = Integer.MAX_VALUE;
        for(Lotnisko lotnisko: dostepneLotniska.values())
        {
            int odlegloscLotnisko = (int)Math.sqrt(Math.pow((lotnisko.getX() - SamolotX),2) +Math.pow((lotnisko.getY() - SamolotY),2));
            if (odlegloscLotnisko<odlegloscMin)
            {
                odlegloscMin = odlegloscLotnisko;
                miejsceAwarii = lotnisko.getNazwa();
            }
        }
        return miejsceAwarii;
    }

    /***
     * planwanie trasy pomiędzy startem, a celem za pomocą grafu tras lotniczych
     * zwraca ArrayListe kolejnych nazw miast.
     * @param start
     * @return
     */
    public ArrayList<String> planujTrasaLotniska(String start)
    {
        ArrayList<String> trasaMiasta = grafTrasSamolot.wyznaczLosowaTrase(start,cel);
        return trasaMiasta;
    }

    /***
     * Planowanie trasy zwraca ArrayListe kolejnych tras lotniczych prowadzących do celu
     * @return
     */
    public ArrayList<TrasaSamolot> planujTrase(){

        ArrayList<TrasaSamolot> trasyTrasaSamolot = new ArrayList<>();
        for(int i = 0;i<trasaLotniska.size()-1;i++)
        {
            HashSet<String> miastaTrasaSamolotID = new HashSet<>(2);
            miastaTrasaSamolotID.add(trasaLotniska.get(i));
            miastaTrasaSamolotID.add(trasaLotniska.get(i+1));
            TrasaSamolot trasaSkladowa = dostepneTrasySamolot.get(miastaTrasaSamolotID);
            trasyTrasaSamolot.add(trasaSkladowa);
            miastaTrasaSamolotID.clear();
        }
        return trasyTrasaSamolot;
    }

    /***
     * metoda sprawdzająca czy w najlbiższym otoczeniu samolotu nie znajduję się inny, jeżeli tak samolot zwalnia do prędkości
     * porprzedzającego
     * @return
     */
    public boolean czyMozeLeciec() {
        boolean mozeLeciec = true;
        int liczbaIteracji = 8;
        if(getKolejneWspolrzedne().size() < 8)
        {
            liczbaIteracji = getKolejneWspolrzedne().size();
        }
        for(int i = 0;i<liczbaIteracji;i++)
        {
            int [] wspolrzedne = getKolejneWspolrzedne().get(i);
            try{
            for(Samolot samolot: getPozostaleSamoloty())
            {
                try {
                    if (!(samolot.getStanPodrozy().equals("wyladowal") || samolot.getStanPodrozy().equals("zepsuty")) && !(samolot.getStanPodrozy().equals("laduje"))) {
                        if (samolot.getX() == wspolrzedne[0] && samolot.getY() == wspolrzedne[1]) {
                            mozeLeciec = false;
                        }
                    }
                }
                catch (NullPointerException|NoSuchElementException e)
                {
                    continue;
                }
            }
        }catch (ConcurrentModificationException e)
            {
                czyMozeLeciec();
            }
        }

        return  mozeLeciec;
    }


    /***
     * Metoda wybierająca tor trasy samolotu w  przypadku gdy następująca trasa jest dwukierunkowa
     * @return
     */
    public int wybierzWspolrzedne() {
        int wyborWspolrzednych = 0;
        int[] celInnegoSamolotu = new int[2];
        ArrayList<int []> kierunekTrasyPierwszy = (ArrayList<int[]>) getTrasaAktualna().getWspolrzedne().get(0).clone();
        ArrayList<int []> kierunekTrasyDrugi = (ArrayList<int[]>) getTrasaAktualna().getWspolrzedne().get(1).clone();
        for(Samolot samolot : getPozostaleSamoloty())
        {
            if(samolot.getTrasaAktualna().equals(getTrasaAktualna()))
            {
                if (samolot.getNastepneLotnisko().equals(this.getNastepneLotnisko()) && samolot.getStanPodrozy().equals("podrozuje"))
                {
                    if(!(samolot.getKolejneWspolrzedne().isEmpty())) {
                        celInnegoSamolotu = samolot.getKolejneWspolrzedne().lastElement();
                        wyborWspolrzednych = podajNumerTrasy(celInnegoSamolotu,kierunekTrasyPierwszy,kierunekTrasyDrugi);
                        return wyborWspolrzednych;
                    }
                }

            }
        }
        for(Samolot samolot : getPozostaleSamoloty())
        {
            if(samolot.getTrasaAktualna().equals(getTrasaAktualna()))
            {
                if (samolot.getNastepneLotnisko().equals(this.getObecneLotnisko()) &&
                        (samolot.getStanPodrozy().equals("startuje") || samolot.getStanPodrozy().equals("podrozuje")))
                {
                    if(samolot.getStanPodrozy().equals("podrozuje"))
                    {
                        if(!(samolot.getKolejneWspolrzedne().isEmpty()))
                        {
                            celInnegoSamolotu = samolot.getKolejneWspolrzedne().lastElement();
                            wyborWspolrzednych = podajNumerTrasyPrzeciwnej(celInnegoSamolotu,kierunekTrasyPierwszy,kierunekTrasyDrugi);
                            return wyborWspolrzednych;
                        }
                        else {
                            continue;
                        }
                    }
                    if(samolot.getStanPodrozy().equals("startuje")) {
                        if (!(samolot.getKolejneWspolrzedne().isEmpty())) {
                            celInnegoSamolotu = samolot.getKolejneWspolrzedne().lastElement();
                            wyborWspolrzednych = podajNumerTrasyPrzeciwnej(celInnegoSamolotu, kierunekTrasyPierwszy, kierunekTrasyDrugi);
                            return wyborWspolrzednych;
                        }
                        else{
//                            try {
//                                Thread.sleep(20);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            celInnegoSamolotu = samolot.getKolejneWspolrzedne().lastElement();
//                            wyborWspolrzednych = podajNumerTrasyPrzeciwnej(celInnegoSamolotu,kierunekTrasyPierwszy,kierunekTrasyDrugi);
//                            return wyborWspolrzednych;
                        }
                    }

                }
            }
        }

        return wyborWspolrzednych;
    }

    private int podajNumerTrasyPrzeciwnej(int [] celInnegoSamolotu,ArrayList<int []> kierunekTrasyPierwszy,ArrayList<int []> kierunekTrasyDrugi) {
        int wyborWspolrzednych = 0;
        if(Arrays.equals(celInnegoSamolotu,kierunekTrasyPierwszy.get(0)) ||
                Arrays.equals(celInnegoSamolotu,kierunekTrasyPierwszy.get(kierunekTrasyPierwszy.size()-1)))
        {
            wyborWspolrzednych = 1;
            return wyborWspolrzednych;
        }
        else if(Arrays.equals(celInnegoSamolotu,kierunekTrasyDrugi.get(0)) ||
                Arrays.equals(celInnegoSamolotu,kierunekTrasyDrugi.get(kierunekTrasyDrugi.size()-1)))
        {
            wyborWspolrzednych = 0;
            return wyborWspolrzednych;
        }
        return wyborWspolrzednych;
    }


    private int podajNumerTrasy(int [] celInnegoSamolotu ,ArrayList<int []> kierunekTrasyPierwszy,ArrayList<int []> kierunekTrasyDrugi) {
        int wyborWspolrzednych = 0;
        if(Arrays.equals(celInnegoSamolotu,kierunekTrasyPierwszy.get(0)) ||
                Arrays.equals(celInnegoSamolotu,kierunekTrasyPierwszy.get(kierunekTrasyPierwszy.size()-1)))
        {
            wyborWspolrzednych = 0;
            return wyborWspolrzednych;
        }
        else if(Arrays.equals(celInnegoSamolotu,kierunekTrasyDrugi.get(0)) ||
                Arrays.equals(celInnegoSamolotu,kierunekTrasyDrugi.get(kierunekTrasyDrugi.size()-1)))
        {
            wyborWspolrzednych = 1;
            return wyborWspolrzednych;
        }
        return wyborWspolrzednych;
    }

    /***
     * Metoda określająca czy następna trasa jednokierunkowa umożliwia przelot bez kolizyjny
     * @return
     */
    public boolean czyTrasaWolna() {
        boolean zgodaStart = true;
        for(Samolot samolot : getPozostaleSamoloty())
        {
            if(samolot.getTrasaAktualna().equals(getTrasaAktualna()))
            {
                if(samolot.getNastepneLotnisko().equals(this.getObecneLotnisko()) &&
                        (samolot.getStanPodrozy().equals("startuje") || samolot.getStanPodrozy().equals("podrozuje")))
                {
                    zgodaStart = false;
                    break;
                }
            }
        }
        return zgodaStart;
    }

    /***
     * Metoda wyznaczająca liste kolejnych współrzędnych samolotu na bazie wspołrzędnych następnej trasy
     * @param trasaAktualna
     * @return
     */
    public  Vector<int[]> podrozoj(TrasaSamolot trasaAktualna) {
        ArrayList<int[]> trasaPrzelotu = wybierzKierunek(trasaAktualna);
        Vector<int[]> wspolrzedne = new Vector<>();
        wspolrzedne.addAll(trasaPrzelotu);
        return wspolrzedne;
    }


    private  ArrayList<int[]> wybierzKierunek(TrasaSamolot trasa) {
        ArrayList<int[]> trasaPunkty = new ArrayList<>();
        int [] wspolrzedneAktualneSamolotu = new int[]{getX(),getY()};
        if(trasa.getLiczbaKierunkow() == 1) {
            trasaPunkty = (ArrayList<int[]>) trasa.getWspolrzedne().get(0).clone();
            if(Arrays.equals(trasaPunkty.get(trasaPunkty.size()-1),wspolrzedneAktualneSamolotu))
            {
                Collections.reverse(trasaPunkty);
            }
        }
        else if(trasa.getLiczbaKierunkow() == 2)
        {
            ArrayList<int[]> wspolrzedneKierunku1 = (ArrayList<int[]>) trasa.getWspolrzedne().get(0).clone();
            ArrayList<int[]> wspolrzedneKierunku2 = (ArrayList<int[]>) trasa.getWspolrzedne().get(1).clone();
            if(Arrays.equals(wspolrzedneKierunku1.get(0),wspolrzedneAktualneSamolotu) ||
                    Arrays.equals(wspolrzedneKierunku1.get(wspolrzedneKierunku1.size()-1),wspolrzedneAktualneSamolotu) )
            {
                trasaPunkty = (ArrayList<int[]>) trasa.getWspolrzedne().get(0).clone();
                if(Arrays.equals(wspolrzedneKierunku1.get(wspolrzedneKierunku1.size()-1),wspolrzedneAktualneSamolotu))
                {
                    Collections.reverse(trasaPunkty);
                }
            }
            else if(Arrays.equals(wspolrzedneKierunku2.get(0),wspolrzedneAktualneSamolotu) ||
                    Arrays.equals(wspolrzedneKierunku2.get(wspolrzedneKierunku2.size()-1),wspolrzedneAktualneSamolotu))
            {
                trasaPunkty = (ArrayList<int[]>) trasa.getWspolrzedne().get(1).clone();
                if(Arrays.equals(wspolrzedneKierunku2.get(wspolrzedneKierunku2.size()-1),wspolrzedneAktualneSamolotu))
                {
                    Collections.reverse(trasaPunkty);
                }
            }
        }
        return trasaPunkty;
    }

    /***
     * metoda wyznaczająca trasę startu z lotniska do wspołrzędnych toru następnej trasy Lotniczej
     * @param wspolrzedneNastepnejTrasy
     * @return
     */
    public Vector<int[]> startuje(ArrayList<int []> wspolrzedneNastepnejTrasy)
    {
        ArrayList<int []> wspolrzedne = new ArrayList<>();
        int [] kraniecPierwszy = wspolrzedneNastepnejTrasy.get(0);
        int [] kraniecDrugi = wspolrzedneNastepnejTrasy.get(wspolrzedneNastepnejTrasy.size()-1);
        int odlegloscPierwszy = (int)Math.sqrt(Math.pow((kraniecPierwszy[0] - getX()),2) +Math.pow((kraniecPierwszy[1] - getY()),2));
        int odlegloscDrugi = (int)Math.sqrt(Math.pow((kraniecDrugi[0] - getX()),2) +Math.pow((kraniecDrugi[1] - getY()),2));
        if (odlegloscDrugi>odlegloscPierwszy)
        {
            wspolrzedne = wyznaczTraseChwilowa(kraniecPierwszy);
        }
        else if(odlegloscPierwszy>odlegloscDrugi)
        {
            wspolrzedne = wyznaczTraseChwilowa(kraniecDrugi);
        }
        Vector<int [] > stosWspolrzedne = new Vector<>();
        stosWspolrzedne.addAll(wspolrzedne);
        return stosWspolrzedne;
    }

    /***
     * Wyznaczeni współrzędnych trasy chwilowej w razie startu, lądwoania, awariii prowadzących do współrzednych celu za pomocą funckji liniowej
     * @param cel
     * @return
     */
    public ArrayList<int []> wyznaczTraseChwilowa(int[] cel)
    {
        ArrayList<int[]> wspolrzedne = new ArrayList<>();
        int xWspolrzednaStartu = getX();
        int yWspolrzednaStartu = getY();
        int xCel =cel[0];
        int yCel = cel[1];
        int dlugoscX = Math.abs(xCel - xWspolrzednaStartu) +1;
        int dlugoscY = Math.abs(yCel - yWspolrzednaStartu)+1;
        boolean czyObrocic = false;
        int x1=0,y1=0,x2=0,y2=0;
        if(dlugoscX>=dlugoscY) {
            if(xCel>xWspolrzednaStartu)
            {
                x1 = xWspolrzednaStartu;
                x2 = xCel;
                y1 = yWspolrzednaStartu;
                y2 = yCel;
                czyObrocic = false;
            }
            else if(xCel<xWspolrzednaStartu)
            {
                x2 = xWspolrzednaStartu;
                x1 = xCel;
                y2 = yWspolrzednaStartu;
                y1 = yCel;
                czyObrocic = true;
            }
            if (xCel != xWspolrzednaStartu) {
                double a = ((double) y2 - (double) y1) / ((double) x2 - (double) x1);
                double b = (double) y1 - a * (double) x1;
                for (int i = 0; i < dlugoscX; i++) {
                    int x = x1 + i;
                    int y = (int) (a * x + b);
                    wspolrzedne.add(new int[]{x, y});
                }
            }
        }
        else if(dlugoscY>dlugoscX)
        {
            if(yCel>yWspolrzednaStartu)
            {
                x1 = xWspolrzednaStartu;
                x2 = xCel;
                y1 = yWspolrzednaStartu;
                y2 = yCel;
                czyObrocic = false;
            }
            else if(yCel<yWspolrzednaStartu)
            {
                x2 = xWspolrzednaStartu;
                x1 = xCel;
                y2 = yWspolrzednaStartu;
                y1 = yCel;
                czyObrocic = true;
            }
            if(y1 != y2) {
                double a = ((double) x2 - (double) x1) / ((double) y2 - (double) y1);
                double b = (double)x1 - a*(double)y1;
                for(int i =0;i<dlugoscY;i++) {
                    int y = y1 +i;
                    int x =(int)(a*y +b);
                    wspolrzedne.add(new int[]{x,y});
                }
            }
        }
        if(czyObrocic)
        {
            Collections.reverse(wspolrzedne);
        }
        if(!(Arrays.equals(wspolrzedne.get(0),new int[]{xWspolrzednaStartu,yWspolrzednaStartu})))
        {
            wspolrzedne.add(0,new int[]{xWspolrzednaStartu,yWspolrzednaStartu});
        }
        if(!(Arrays.equals(wspolrzedne.get(0),new int[]{xCel,yCel})))
        {
            wspolrzedne.add(new int[]{xCel,yCel});
        }
        return wspolrzedne;
    }

    /***
     * funkcja wyznaczająca współrzędne od lotniska na którym samolot ma zaraz wylądować w zależności od typu samolotu
     * @param nazwa
     * @return
     */
    public Vector<int []> laduj(String nazwa)
    {
        HashMap<String, Lotnisko> lotniska = getDostepneLotniska();
        Lotnisko lotniskoCel = new Lotnisko();
        if(this instanceof SamolotPasazerski) {
            lotniskoCel = lotniska.get(nazwa + "Cywilne");
        }
        else
        {
            lotniskoCel = lotniska.get(nazwa + "Wojskowe");
        }
        int celX = lotniskoCel.getX();
        int celY = lotniskoCel.getY();
        ArrayList<int [] > trasaLadowania = wyznaczTraseChwilowa(new int[]{celX,celY});
        Vector<int[]> wspolrzedne = new Vector<>();
        wspolrzedne.addAll(trasaLadowania);
        return  wspolrzedne;

    }

    /***
     * funkcja wyznaczająca prędkość samolotu
     * @param predkosc
     * @return
     */
    public long ustawCzasOczekiwania(int predkosc) {
        long czasOczekiwania = 0;
        if (predkosc == 1)
        {
            czasOczekiwania = 30;
        }
        else if (predkosc == 2 )
        {
            czasOczekiwania = 20;
        }
        else if (predkosc == 3)
        {
            czasOczekiwania = 10;
        }
        return czasOczekiwania;
    }

    /***
     * setter tras samolotu
     * @param trasa
     */
    public void setTrasa(ArrayList<TrasaSamolot> trasa) {
        this.trasa = trasa;
    }

    /***
     * setter stanu paliwa
     * @param stan_paliwa
     */
    public void setStan_paliwa(int stan_paliwa) {
        this.stan_paliwa = stan_paliwa;
    }

    /***
     * setter wspołrzednej x samolotu
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /***
     * setter współrzędnej y samolotu
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /***
     * getter tras samolotu
     * @return
     */
    public ArrayList<TrasaSamolot> getTrasa() {
        return trasa;
    }

    /***
     * getter stanu Paliwa
     * @return
     */
    public int getStan_paliwa() {
        return stan_paliwa;
    }

    /***
     * getter współrzednej x samolotu
     * @return
     */
    public int getX() {
        return x;
    }

    /***
     * getter współrzędnej y samolotu
     * @return
     */
    public int getY() {
        return y;
    }

    /***
     * getter celu samolotu
     * @return
     */
    public String getCel() {
        return cel;
    }

    /***
     * setter celu samolotu
     * @param cel
     */
    public void setCel(String cel) {
        this.cel = cel;
    }

    /***
     * getter nazwy samolotu
     * @return
     */
    public String getNazwa() {
        return nazwa;
    }

    /***
     * setter nazwy samolotu
     * @param nazwa
     */
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    /***
     * getter dostępnych lotnisk na mapie
     * @return
     */
    public HashMap<String, Lotnisko> getDostepneLotniska() {
        return dostepneLotniska;
    }

    /***
     * getter trasy- listy następujących miast do celu
     * @return
     */
    public ArrayList<String> getTrasaLotniska() {
        return trasaLotniska;
    }

    /***
     * setter trsy - listy następujących miast do celu
     * @param trasaLotniska
     */
    public void setTrasaLotniska(ArrayList<String> trasaLotniska) {
        this.trasaLotniska = trasaLotniska;
    }

    /***
     * getter aktualnie użytkowaniej trasy przez samolot
     * @return
     */
    public TrasaSamolot getTrasaAktualna() {
        return trasaAktualna;
    }

    /***
     * setter aktualnie użytkowaniej trasy przez samolot
     * @param trasaAktualna
     */
    public void setTrasaAktualna(TrasaSamolot trasaAktualna) {
        this.trasaAktualna = trasaAktualna;
    }

    /***
     * getter Następnego miast odwiedzanego przez samolot
     * @return
     */
    public String getNastepneLotnisko() {
        return nastepneLotnisko;
    }

    /***
     * setter nastepnego lotniska odwiedzaniego przez samolot
     * @param nastepneLotnisko
     */
    public void setNastepneLotnisko(String nastepneLotnisko) {
        this.nastepneLotnisko = nastepneLotnisko;
    }

    /***
     * gettter stanu w jakim obecnie się  znajduje samolot
     * @return
     */
    public String getStanPodrozy() {
        return stanPodrozy;
    }

    /***
     * setter stanu podrozy w jakim obecnie znajduje sie  samolot
     * @param stanPodrozy
     */
    public void setStanPodrozy(String stanPodrozy) {
        this.stanPodrozy = stanPodrozy;
    }

    /***
     * getter pozostałych samolotów znajdujących sie na mapie
     * @return
     */
    public ArrayList<Samolot> getPozostaleSamoloty() {
        return pozostaleSamoloty;
    }

    /***
     * setter pozostałych samolotów znajdujących się na mapie
     * @param pozostaleSamoloty
     */
    public void setPozostaleSamoloty(ArrayList<Samolot> pozostaleSamoloty) {
        this.pozostaleSamoloty = pozostaleSamoloty;
    }

    /***
     * getter kolejnych współrzednych samolotu
     * @return
     */
    public Vector<int[]> getKolejneWspolrzedne() {
        return kolejneWspolrzedne;
    }

    /***
     * setter kolejnych współrzędnych samolotu
     * @param kolejneWspolrzedne
     */
    public void setKolejneWspolrzedne(Vector<int[]> kolejneWspolrzedne) {
        this.kolejneWspolrzedne = kolejneWspolrzedne;
    }

    /***
     * getter obecnego lotniska sammolotu
     * @return
     */
    public String getObecneLotnisko() {
        return obecneLotnisko;
    }

    /***
     * setter obecnego lotniska samolotu
     * @param obecneLotnisko
     */
    public void setObecneLotnisko(String obecneLotnisko) {
        this.obecneLotnisko = obecneLotnisko;
    }

    /***
     * getter obecnej predkosci samolotu
     * @return
     */
    public int getPredkosc() {
        return predkosc;
    }

    /***
     * getter pojemności baku samolotu
     * @return
     */
    public int getBak() {
        return bak;
    }
}
