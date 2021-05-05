package Mapa;
import java.util.ArrayList;
import java.util.HashSet;

/***
 * Klasa trasy Morskiej
 */
public class TrasaStatek {
    private HashSet<int []> konceWspolrzedne;
    private ArrayList<ArrayList<int[]>> wspolrzedne;
    private HashSet<String> konceMiasta;
    private ArrayList<Skrzyzowanie> skrzyzowania;

    /***
     * Konstruktor trasy morskiej
     * @param poczatek
     * @param koniec
     * @param kraniecFirst miasto znajdujące sie na jednym końcu trasy
     * @param kraniecSecond miasto znajdujące się na drugim końcu trasy
     */
    public TrasaStatek(int[] poczatek, int[] koniec,String kraniecFirst,String kraniecSecond) {
        this.konceMiasta = new HashSet<>();
        this.konceWspolrzedne =  new HashSet<>();

        this.skrzyzowania = new ArrayList<>();
        konceMiasta.add(kraniecFirst);
        konceMiasta.add(kraniecSecond);
        konceWspolrzedne.add(poczatek);
        konceWspolrzedne.add(koniec);
        this.wspolrzedne = tworz();
    }

    /***
     * tworzenie trasy oraz wyznaczenie jej przsunięcia w zależności od nachylenia
     * @return
     */
    public ArrayList<ArrayList<int[]>> tworz() {
        ArrayList<ArrayList<int[]>> wspolrzedne = new ArrayList<ArrayList<int[]>>();
        ArrayList<int[]> krance =  new ArrayList<>();
        for(int[] kraniec : konceWspolrzedne)
        {
            krance.add(kraniec);
        }
        int[] poczatek = krance.get(0);
        int[] koniec = krance.get(1);
        int dlugoscX = Math.abs(poczatek[0] - koniec[0]) + 1;
        int dlugoscY = Math.abs(poczatek[1]-koniec[1]) + 1;
        int poczatekDrugiX = poczatek[0];
        int poczatekDrugiY = poczatek[1];
        int koniecDrugiX = koniec[0];
        int koniecDrugiY = koniec[1];
        if(dlugoscY>= dlugoscX)
        {
            poczatekDrugiX+=8;
            koniecDrugiX+=8;
        }
        else if(dlugoscX>dlugoscY)
        {
            poczatekDrugiY -=8;
            koniecDrugiY -=8;
        }
        int [] poczatekDrugi = new int[]{poczatekDrugiX,poczatekDrugiY};
        int[] koniecDrugi = new int[]{koniecDrugiX,koniecDrugiY};
        ArrayList<int []> zbiorPunktowPierwszy = wyznaczWspolrzedne(poczatek,koniec);
        ArrayList<int []> zbiorPunktowDrugi = wyznaczWspolrzedne(poczatekDrugi,koniecDrugi);
        wspolrzedne.add(zbiorPunktowPierwszy);
        wspolrzedne.add(zbiorPunktowDrugi);

        return wspolrzedne;
    }

    /***
     * wyznaczenie wspołrzędnych która zawiera trasa zwraca ArrayListe współrzędnych
     * @param poczatek współrzedne początku
     * @param koniec wpołrzędne końca
     * @return
     */
    public ArrayList<int[]> wyznaczWspolrzedne(int [] poczatek, int [] koniec)
    {
        double a;
        double b;
        int dlugoscX = Math.abs(poczatek[0] - koniec[0]) + 1;
        int dlugoscY = Math.abs(poczatek[1]-koniec[1]) + 1;
        ArrayList<int[]> trasa = new ArrayList<>();
        int x1=0,x2=0,y1=0,y2=0;
        if(dlugoscX >= dlugoscY) {
            if(poczatek[0]>koniec[0])
            {
                x1 = koniec[0];
                x2 = poczatek[0];
                y1 = koniec[1];
                y2 = poczatek[1];
            }
            else if(poczatek[0]<koniec[0])
            {
                x2 = koniec[0];
                x1 = poczatek[0];
                y2 = koniec[1];
                y1 = poczatek[1];
            }
            if (x1 != x2) {
                a = ((double) y2 - (double) y1) / ((double) x2 - (double) x1);
                b = (double) y1 - a * (double) x1;
                for (int i = 0; i < dlugoscX; i++) {
                    int x = x1 + i;
                    int y = (int) (a * x + b);
                    trasa.add(new int[]{x, y});
                }
            }
        }
        else if(dlugoscY>dlugoscX){
            if(poczatek[1]>koniec[1])
            {
                x1 = koniec[0];
                x2 = poczatek[0];
                y1 = koniec[1];
                y2 = poczatek[1];
            }
            else if(poczatek[1]<koniec[1])
            {
                x2 = koniec[0];
                x1 = poczatek[0];
                y2 = koniec[1];
                y1 = poczatek[1];
            }
            if(y1 != y2) {
                a = ((double) x2 - (double) x1) / ((double) y2 - (double) y1);
                b = (double)x1 - a*(double)y1;
                for(int i =0;i<dlugoscY;i++) {
                    int y = y1 +i;
                    int x =(int)(a*y +b);
                    trasa.add(new int[]{x,y});
                }
            }

        }
        return trasa;
    }

    /***
     * getter Współrzędnych trasy
     * @return
     */
    public ArrayList<ArrayList<int[]>> getWspolrzedne() {

        return wspolrzedne;
    }

    /***
     * getter miast znajdujących się na początku i końcu trasy
     * @return
     */
    public HashSet<String> getKonceMiasta() {
        return konceMiasta;
    }

    /***
     * getter skrzyżowań na trasie
     * @return
     */
    public ArrayList<Skrzyzowanie> getSkrzyzowania() {
        return skrzyzowania;
    }

    /***
     * setter skrzyżowań znajdujących się na trasie
     * @param skrzyzowania
     */
    public void setSkrzyzowania(ArrayList<Skrzyzowanie> skrzyzowania) {
        this.skrzyzowania = skrzyzowania;
    }
}
