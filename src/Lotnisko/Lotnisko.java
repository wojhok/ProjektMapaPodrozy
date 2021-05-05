package Lotnisko;
import Vehicle.Samolot;
import Vehicle.SamolotPasazerski;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

/***
 * Klasa Lotniska
 */
public class Lotnisko {
    private String nazwa;
    private int pojemnosc;
    private int aktualnaLiczbaSamolotow;
    private int x;
    private int y;
    private String typ;
    private Image ikona;
    private Object lock = new Object();
    private Object lock1 = new Object();

    /***
     * Konstruktor bezargmentowy
     */
    public Lotnisko()
    {

    }

    /***
     * Konstruktor Lotniska
     * @param pojemnosc
     * @param x
     * @param y
     * @param typ
     * @param nazwa
     */
    public Lotnisko(int pojemnosc, int x,int y, String typ, String nazwa) {
        this.nazwa = nazwa;
        this.pojemnosc = pojemnosc;
        this.typ = typ;
        if (typ == "Cywilne") {
            ImageIcon ikonaIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Obrazy/airport-building.png")));
            ikona = ikonaIcon.getImage();
        }
        else if(typ == "Wojskowe")
        {
            ImageIcon ikonaIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Obrazy/army-tower.png")));
            ikona = ikonaIcon.getImage();
        }
        this.x = x;
        this.y = y;
        this.aktualnaLiczbaSamolotow = 0;
    }

    /***
     * getter ikony Lotniska
     * @return
     */
    public Image getIkona() {
        return ikona;
    }

    /***
     *  getter pojemnosci Lotniska
     * @return
     */
    public int getPojemnosc() {
        return pojemnosc;
    }

    /***
     * getter typu Lotniska
     * @return
     */
    public String getTyp() {
        return typ;
    }

    /***
     *  getter nazwy Lotniska
     * @return
     */
    public String getNazwa() {
        return nazwa;
    }

    /***
     * setter nazwy
     * @param nazwa
     */
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    /***
     * getter wspolrzednej x
     * @return
     */
    public int getX() {
        return x;
    }

    /***
     * getter wspolrzednej y
     * @return
     */
    public int getY() {
        return y;
    }

    /***
     * getter Aktualnej Liczby Samolotów na Lotnisku
     * @return
     */
    public int getAktualnaLiczbaSamolotow() {
        return aktualnaLiczbaSamolotow;
    }

    /***
     * setter Aktualnej liczby samolotów na Lotnisku
     * @param aktualnaLiczbaSamolotow
     */
    public void setAktualnaLiczbaSamolotow(int aktualnaLiczbaSamolotow) {
        this.aktualnaLiczbaSamolotow = aktualnaLiczbaSamolotow;
    }

    /***
     * funkcja synchronizująca wylot samolotu z lotniska
     * @param samolot
     */
    public void Wylot(Samolot samolot)
    {
        synchronized (lock) {
                samolot.setStanPodrozy("startuje");
                boolean zgodaStart = true;
                int wyborWspolrzednych = 0;
                if(samolot.getTrasaAktualna().getLiczbaKierunkow() == 1)
                {
                    zgodaStart = samolot.czyTrasaWolna();
                }
                else if(samolot.getTrasaAktualna().getLiczbaKierunkow() ==2)
                {
                    wyborWspolrzednych = samolot.wybierzWspolrzedne();
                }
                if(zgodaStart) {
                    samolot.setKolejneWspolrzedne(samolot.startuje(samolot.getTrasaAktualna().getWspolrzedne().get(wyborWspolrzednych)));
                }
                else{
                    samolot.setStanPodrozy("wyladowal");
                }

                if (samolot.getStanPodrozy().equals("startuje")) {
                    if (samolot instanceof SamolotPasazerski) {
                        SamolotPasazerski samolotPasazerski = (SamolotPasazerski) samolot;
                        Random losowa = new Random();
                        int liczbaPasazer = losowa.nextInt(samolotPasazerski.getMaks_pojemnosc());
                        samolotPasazerski.setLicz_pasazer(liczbaPasazer);
                }
                    samolot.setStan_paliwa(samolot.getBak());
                }
                while (!(samolot.getKolejneWspolrzedne().isEmpty())){
                    try {
                        samolot.sleep(samolot.ustawCzasOczekiwania(samolot.getPredkosc()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(samolot.czyMozeLeciec()) {
                        int[] wspolrzedne = samolot.getKolejneWspolrzedne().remove(0);
                        samolot.setStan_paliwa(samolot.getStan_paliwa()-1);
                        samolot.setX(wspolrzedne[0]);
                        samolot.setY(wspolrzedne[1]);
                    }
                }
                if(samolot.getStanPodrozy().equals("startuje")) {
                    this.aktualnaLiczbaSamolotow -= 1;
                }
        }
    }
}
