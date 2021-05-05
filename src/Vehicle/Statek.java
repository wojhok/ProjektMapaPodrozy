package Vehicle;
import Mapa.*;
import java.util.*;

/***
 * Klasa statku
 */
public class Statek extends Thread {
    private int maks_predkosc;
    private HashMap<String,MiastoPortowe> punktyPrzesiadkowe;
    private ArrayList<TrasaStatek> dostepneTrasy;
    private ArrayList<TrasaStatek> trasy;
    private TrasaStatek trasaAktualna;
    private int x;
    private int y;
    private String nazwa;
    private String cel;
    private String start;
    private GrafTrasStatek grafTrasStatek;
    private ArrayList<String> podrozPunkty;
    private ArrayList<Statek> pozostaleStatki;
    private Vector<int []> kolejneWspolrzedne;
    private String obecnyPunkt;
    private String nastepnyPunkt;
    private String stan;

    /***
     * Bezargumentowy kostruktor statku
     */
    public Statek()
    {}

    /***
     * Konstruktor Statku
     * @param maks_predkosc
     * @param nazwa
     * @param cel
     * @param start
     * @param grafTrasStatek
     * @param dostepneTrasy
     * @param punktyPrzesiadkowe miejsca w pobliżu ktorych nastąpi zmiana trasy statku
     */
    public Statek(int maks_predkosc,String nazwa,String cel,String start,GrafTrasStatek grafTrasStatek,
                  ArrayList<TrasaStatek> dostepneTrasy,HashMap<String,MiastoPortowe> punktyPrzesiadkowe)
    {
        this.maks_predkosc = maks_predkosc;
        this.nazwa = nazwa;
        this.cel = cel;
        this.start = start;
        this.punktyPrzesiadkowe = punktyPrzesiadkowe;
        this.grafTrasStatek  =grafTrasStatek;
        this.podrozPunkty = planujPodrozPunkty();
        this.pozostaleStatki = new ArrayList<>();
        this.dostepneTrasy = dostepneTrasy;
        this.trasy = planujTrase();
        this.kolejneWspolrzedne = new Vector<>();
        tworz();
    }

    /***
     * metoda obsługująca działanie wątku Statku zarządza ona zmianą wspórzednych oraz zarządza dobór kolejnych
     */
    @Override
    public void run() {
        while(true)
        {
            int predkosc = getMaks_predkosc();
            long czasOczekiwania = ustawCzasOczekiwania(predkosc);
            if(!(czyMozePlynac()))
            {
                continue;
            }
            try {
                Thread.sleep(czasOczekiwania);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!(getKolejneWspolrzedne().isEmpty())) {
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
                                skrzyzowanie.uzytkujSkrzyzowanieStatek(this);
                                break;
                            }
                        }
                        continue;
                    }
                }
                int[] wspolrzedne = getKolejneWspolrzedne().remove(0);
                setX(wspolrzedne[0]);
                setY(wspolrzedne[1]);
            }
            else if (getKolejneWspolrzedne().isEmpty()) {
                if (getPodrozPunkty().indexOf(getNastepnyPunkt()) == getPodrozPunkty().size() - 1) {
                    Random generator = new Random();
                    ArrayList<String> nazwyPunkty = new ArrayList<>();
                    nazwyPunkty.addAll(punktyPrzesiadkowe.keySet());
                    int index = nazwyPunkty.indexOf(getCel());
                    nazwyPunkty.remove(index);
                    int losowaLiczba = generator.nextInt(nazwyPunkty.size());
                    setStart(getCel());
                    setCel(nazwyPunkty.get(losowaLiczba));
                    podrozPunkty = planujPodrozPunkty();
                    trasy = planujTrase();
                    setTrasaAktualna(getTrasy().get(0));
                    setObecnyPunkt(podrozPunkty.get(0));
                    setNastepnyPunkt(podrozPunkty.get(1));
                }
                else{
                    if(!(getObecnyPunkt().equals(getNastepnyPunkt())))
                    {
                        setTrasaAktualna(getTrasy().get(getTrasy().indexOf(getTrasaAktualna()) + 1));
                    }
                    setObecnyPunkt(getNastepnyPunkt());
                    setNastepnyPunkt(podrozPunkty.get(podrozPunkty.indexOf(getObecnyPunkt())+1));
                }
                int nrKolejnejTrasy = wybierzWspolrzedne();
                setKolejneWspolrzedne(wybierzKierunek(getTrasaAktualna(),nrKolejnejTrasy));
            }
        }
    }
    private boolean czyMozePlynac() {
        boolean mozeLeciec = true;
        int liczbaIteracji = 8;
        if(getKolejneWspolrzedne().size() < 8)
        {
            liczbaIteracji = getKolejneWspolrzedne().size();
        }
        for(int i = 0;i<liczbaIteracji;i++)
        {
            int [] wspolrzedne = getKolejneWspolrzedne().get(i);
            for(Statek statek: getPozostaleStatki())
            {
                try {
                    if (statek.getX() == wspolrzedne[0] && statek.getY() == wspolrzedne[1]) {
                        mozeLeciec = false;
                    }
                }
                catch (NullPointerException e)
                {
                    continue;
                }
            }
        }
        return  mozeLeciec;
    }

    /***
     * Metoda wybierająca tor kolejnej trasy co ma prowadzic do uniknięcia kolizji
     * @return
     */
    public int wybierzWspolrzedne() {
        int wyborWspolrzednych = 0;
        int[] celInnegoStatku = new int[2];
        ArrayList<int []> kierunekTrasyPierwszy = (ArrayList<int[]>) getTrasaAktualna().getWspolrzedne().get(0).clone();
        ArrayList<int []> kierunekTrasyDrugi = (ArrayList<int[]>) getTrasaAktualna().getWspolrzedne().get(1).clone();
        for(Statek statek : getPozostaleStatki())
        {
            if(statek.getTrasaAktualna().equals(getTrasaAktualna()))
            {
                if (statek.getNastepnyPunkt().equals(this.getNastepnyPunkt()))
                {
                    if(!(statek.getKolejneWspolrzedne().isEmpty())) {
                        celInnegoStatku = statek.getKolejneWspolrzedne().lastElement();
                        wyborWspolrzednych = podajNumerTrasy(celInnegoStatku,kierunekTrasyPierwszy,kierunekTrasyDrugi);
                        return wyborWspolrzednych;
                    }
                }

            }
        }
        for(Statek statek : getPozostaleStatki())
        {
            if(statek.getTrasaAktualna().equals(getTrasaAktualna()))
            {
                if (statek.getNastepnyPunkt().equals(this.getObecnyPunkt()))
                {
                        if(!(statek.getKolejneWspolrzedne().isEmpty()))
                        {
                            celInnegoStatku = statek.getKolejneWspolrzedne().lastElement();
                            wyborWspolrzednych = podajNumerTrasyPrzeciwnej(celInnegoStatku,kierunekTrasyPierwszy,kierunekTrasyDrugi);
                            return wyborWspolrzednych;
                        }
                        else {
                            continue;
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

    private  Vector<int[]> wybierzKierunek(TrasaStatek trasa,int nrTrasy) {
        ArrayList<int[]> wspolrzedneKierunku = (ArrayList<int[]>) trasa.getWspolrzedne().get(nrTrasy).clone();
        int [] kraniecPierwszy = wspolrzedneKierunku.get(0);
        int [] kraniecDrugi = wspolrzedneKierunku.get(wspolrzedneKierunku.size()-1);
        int odlegloscPierwszy = (int)Math.sqrt(Math.pow((kraniecPierwszy[0] - getX()),2) +Math.pow((kraniecPierwszy[1] - getY()),2));
        int odlegloscDrugi = (int)Math.sqrt(Math.pow((kraniecDrugi[0] - getX()),2) +Math.pow((kraniecDrugi[1] - getY()),2));
        if(odlegloscDrugi<odlegloscPierwszy)
        {
            Collections.reverse(wspolrzedneKierunku);
        }
        Vector<int[]> kolejneWspolrzedne = new Vector<>(wspolrzedneKierunku);
        return kolejneWspolrzedne;
    }

    /***
     * ustalenie predkosci statku
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

    private ArrayList<TrasaStatek> planujTrase() {
        ArrayList<TrasaStatek> trasyTrasaStatek = new ArrayList<>();
        for(int i = 0;i<podrozPunkty.size()-1;i++)
        {
            HashSet<String> punktyTrasaStatekID = new HashSet<>(2);
            punktyTrasaStatekID.add(podrozPunkty.get(i));
            punktyTrasaStatekID.add(podrozPunkty.get(i+1));
            for (TrasaStatek trasa : dostepneTrasy)
            {
                if(trasa.getKonceMiasta().equals(punktyTrasaStatekID))
                {
                    TrasaStatek trasaSkladowa = trasa;
                    trasyTrasaStatek.add(trasaSkladowa);
                    break;
                }
            }
            punktyTrasaStatekID.clear();
        }
        return trasyTrasaStatek;
    }

    /***
     * Metoda tworządza statek w miejście portowym po stworzeniu statek wypływa w podróż
     */
    private void tworz() {
        setX(punktyPrzesiadkowe.get(start).getX());
        setY(punktyPrzesiadkowe.get(start).getY());
        setObecnyPunkt(podrozPunkty.get(0));
        setNastepnyPunkt(podrozPunkty.get(0));
        setTrasaAktualna(trasy.get(0));
    }

    /***
     * metoda wyznacza trase statku - kolejne miasta a tak własciwie punkty przesiadkowe obok których przepłynie statek
     * @return
     */
    public ArrayList<String> planujPodrozPunkty()
    {
        ArrayList<String> trasaPunkty = grafTrasStatek.wyznaczLosowaTrase(start,cel);
        return trasaPunkty;
    }

    /***
     * getter maksymalnej predkosci statku
     * @return
     */
    public int getMaks_predkosc() {
        return maks_predkosc;
    }

    /***
     * getter tras statku
     * @return
     */
    public ArrayList<TrasaStatek> getTrasy() {
        return trasy;
    }

    /***
     * setter tras statku
     * @param trasy
     */
    public void setTrasy(ArrayList<TrasaStatek> trasy) {
        this.trasy = trasy;
    }

    /***
     * getter aktualnie użytkowanej trasy przez statek
     * @return
     */
    public TrasaStatek getTrasaAktualna() {
        return trasaAktualna;
    }

    /***
     * setter aktualnie użytkowanej trasy przez statek
     * @param trasaAktualna
     */
    public void setTrasaAktualna(TrasaStatek trasaAktualna) {
        this.trasaAktualna = trasaAktualna;
    }

    /***
     * getter współrzędnej x statku
     * @return
     */
        public int getX() {
        return x;
    }

    /***
     * setter współrzednej x  statku
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /***
     * getter współrzędnej y statku
     * @return
     */
    public int getY() {
        return y;
    }

    /***
     * setter współrzędnej y statku
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /***
     * getter nazwy statku
     * @return
     */
    public String getNazwa() {
        return nazwa;
    }

    /***
     * setter nazwy statku
     * @param nazwa
     */
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    /***
     * getter celu staktu
     * @return
     */
    public String getCel() {
        return cel;
    }

    /***
     * setter celu statku
     * @param cel
     */
    public void setCel(String cel) {
        this.cel = cel;
    }

    /***
     * setter miejsca startu statku
     * @param start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /***
     * getter punktów przesiadkowych - nazw kolejnych miast z startu do celu
     * @return
     */
    public ArrayList<String> getPodrozPunkty() {
        return podrozPunkty;
    }

    /***
     * getter pozostałych samolotów znajdujących się na mapie
     * @return
     */
    public ArrayList<Statek> getPozostaleStatki() {
        return pozostaleStatki;
    }

    /***
     * setter pozostałych statków znajdujących się na mapie
     * @param pozostaleStatki
     */
    public void setPozostaleStatki(ArrayList<Statek> pozostaleStatki) {
        this.pozostaleStatki = pozostaleStatki;
    }

    /***
     * getter kolejnych współrzędnych znajdujących się na mapie
     * @return
     */
    public Vector<int[]> getKolejneWspolrzedne() {
        return kolejneWspolrzedne;
    }

    /***
     * setter kolejnych współrzędnych znajdujących się na mapie
     * @param kolejneWspolrzedne
     */
    public void setKolejneWspolrzedne(Vector<int[]> kolejneWspolrzedne) {
        this.kolejneWspolrzedne = kolejneWspolrzedne;
    }

    /***
     * getter obecnego punktu przesiadkowego w którym znajduje się statek
     * @return
     */
    public String getObecnyPunkt() {
        return obecnyPunkt;
    }

    /***
     * setter obecnego punktu przesiadkowego w którym znajduje się statek
     * @param obecnyPunkt
     */
    public void setObecnyPunkt(String obecnyPunkt) {
        this.obecnyPunkt = obecnyPunkt;
    }

    /***
     * getter następnego punktu przesiadkowego
     * @return
     */
    public String getNastepnyPunkt() {
        return nastepnyPunkt;
    }

    /***
     * setter następnego punktu przesiadkowego
     * @param nastepnyPunkt
     */
    public void setNastepnyPunkt(String nastepnyPunkt) {
        this.nastepnyPunkt = nastepnyPunkt;
    }

}
