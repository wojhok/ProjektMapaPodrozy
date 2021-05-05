package Mapa;

import Vehicle.Samolot;
import Vehicle.Statek;

import java.util.Arrays;

/***
 * Klasa Skrzyżowania na trasach
 */
public class Skrzyzowanie {
    private int [] wspolrzedneSkrzyzowania;
    private Object lock = new Object();

    /***
     * Konstruktor Skrzyżowania
     * @param wspolrzedneSkrzyzowania
     */
    public Skrzyzowanie(int []  wspolrzedneSkrzyzowania)
    {
        this.wspolrzedneSkrzyzowania = wspolrzedneSkrzyzowania;
    }

    /***
     * metoda umożliwiająca synchronizacje użytkowanego skrzyżowania tylko przez jeden samolot
     * zrealizowana mechanizmem Monitorów
     * @param samolot
     */
    public void uzytkujSkrzyzowanie(Samolot samolot)
    {
        synchronized (lock)
        {
            while(!(samolot.getX() == wspolrzedneSkrzyzowania[0] && samolot.getY() == wspolrzedneSkrzyzowania[1]))
            {
               int[] wspolrzedne = samolot.getKolejneWspolrzedne().remove(0);
                try {
                    samolot.sleep(samolot.ustawCzasOczekiwania(samolot.getPredkosc()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                samolot.setStan_paliwa(samolot.getStan_paliwa()-1);
                samolot.setX(wspolrzedne[0]);
                samolot.setY(wspolrzedne[1]);
            }
            int iloscIteracji =5;
            if(samolot.getKolejneWspolrzedne().size()<iloscIteracji)
            {
                iloscIteracji = samolot.getKolejneWspolrzedne().size();
            }
            for(int i = 0; i<iloscIteracji; i++)
            {
                boolean wykrytoKolejneSkrzyzowanie = false;
                for (Skrzyzowanie skrzyzowanie: samolot.getTrasaAktualna().getSkrzyzowania())
                {
                    int iloscIteracji2 = 8;
                    if(samolot.getKolejneWspolrzedne().size()<iloscIteracji2)
                    {
                        iloscIteracji2 = samolot.getKolejneWspolrzedne().size();
                    }
                    for(int j =0;j<iloscIteracji2;j++)
                    {
                        if(Arrays.equals(skrzyzowanie.getWspolrzedneSkrzyzowania(),samolot.getKolejneWspolrzedne().get(j)))
                        {
                            skrzyzowanie.uzytkujSkrzyzowanie(samolot);
                            wykrytoKolejneSkrzyzowanie = true;
                            break;
                        }
                    }
                }
                if(wykrytoKolejneSkrzyzowanie)
                    break;
                int[] wspolrzedne = samolot.getKolejneWspolrzedne().remove(0);
                try {
                    samolot.sleep(samolot.ustawCzasOczekiwania(samolot.getPredkosc()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                samolot.setStan_paliwa(samolot.getStan_paliwa()-1);
                samolot.setX(wspolrzedne[0]);
                samolot.setY(wspolrzedne[1]);
            }
        }
    }

    /***
     * Metoda umożliwiająca użytowanie skrzyżwania tylko przez jeden statek w danym momencie
     * Zrealizowana z pomocą mechanimu monitorów
     * @param statek
     */
    public void uzytkujSkrzyzowanieStatek(Statek statek)
    {
        synchronized (lock)
        {
            while(!(statek.getX() == wspolrzedneSkrzyzowania[0] && statek.getY() == wspolrzedneSkrzyzowania[1]))
            {
                int[] wspolrzedne = statek.getKolejneWspolrzedne().remove(0);
                try {
                    statek.sleep(statek.ustawCzasOczekiwania(statek.getMaks_predkosc()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                statek.setX(wspolrzedne[0]);
                statek.setY(wspolrzedne[1]);
            }
            int iloscIteracji =5;
            if(statek.getKolejneWspolrzedne().size()<iloscIteracji)
            {
                iloscIteracji = statek.getKolejneWspolrzedne().size();
            }
            for(int i = 0; i<iloscIteracji; i++)
            {
                boolean wykrytoKolejneSkrzyzowanie = false;
                for (Skrzyzowanie skrzyzowanie: statek.getTrasaAktualna().getSkrzyzowania())
                {
                    int iloscIteracji2 = 8;
                    if(statek.getKolejneWspolrzedne().size()<iloscIteracji2)
                    {
                        iloscIteracji2 = statek.getKolejneWspolrzedne().size();
                    }
                    for(int j =0;j<iloscIteracji2;j++)
                    {
                        if(Arrays.equals(skrzyzowanie.getWspolrzedneSkrzyzowania(),statek.getKolejneWspolrzedne().get(j)))
                        {
                            skrzyzowanie.uzytkujSkrzyzowanieStatek(statek);
                            wykrytoKolejneSkrzyzowanie = true;
                            break;
                        }
                    }
                }
                if(wykrytoKolejneSkrzyzowanie)
                    break;
                int[] wspolrzedne = statek.getKolejneWspolrzedne().remove(0);
                try {
                    statek.sleep(statek.ustawCzasOczekiwania(statek.getMaks_predkosc()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                statek.setX(wspolrzedne[0]);
                statek.setY(wspolrzedne[1]);
            }
        }
    }

    /***
     * getter współrzędnych skrzyżowania
     * @return
     */
    public int[] getWspolrzedneSkrzyzowania() {
        return wspolrzedneSkrzyzowania;
    }
}
