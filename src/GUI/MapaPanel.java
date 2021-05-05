package GUI;
import Lotnisko.Lotnisko;
import Mapa.*;
import Vehicle.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Klasa panelu mapy
 */
public class MapaPanel extends JPanel {
    private ImageIcon image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Obrazy/bg_map.png")));
    private javax.swing.Timer timer;
    private HashMap<String, Lotnisko> lotniska;
    private HashMap<HashSet<String>,TrasaSamolot> trasySamolot;
    private GrafTrasStatek grafTrasStatek;
    private ArrayList<TrasaStatek> trasyStatek;
    private GrafTrasSamolot grafTrasSamolot;
    private ArrayList<Samolot> samoloty;
    private ArrayList<Statek> statki;
    private HashMap<String,MiastoPortowe> miastaPortowe;
    private ArrayList<Lotniskowiec> lotniskowce;
    private MapaPanel self;

    /***
     * Konstruktor Panelu Mapy
     */
    public MapaPanel() {
        this.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
        this.self = this;
        this.timer = new Timer(50, new petla(this));
        this.timer.start();
        this.lotniskowce = new ArrayList<>();
        this.lotniska = dodajLotniska();
        this.miastaPortowe = dodajMiastaportowe();
        this.trasyStatek = dodajTrasyStatek();
        this.grafTrasStatek = new GrafTrasStatek(trasyStatek);
        this.statki = dodajStatki();
        this.trasySamolot = dodajTrasySamolot();
        this.grafTrasSamolot = new GrafTrasSamolot(trasySamolot);
        this.samoloty = dodajSamoloty();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();
//                System.out.println(e.getX() + "," + e.getY());
                for(Samolot samolot: samoloty)
                {
                    int xSamolot = samolot.getX();
                    int ySamolot = samolot.getY();
                    if((x > xSamolot-8 && x<xSamolot+8) && (y>ySamolot-8 && y<ySamolot+8))
                    {
                        InfoSamolot info =new InfoSamolot(samolot,self);
                        info.setVisible(true);
                    }
                }
                for(Statek statek: statki)
                {
                    int xStatek = statek.getX();
                    int yStatek = statek.getY();
                    if((x>xStatek-8 && x< xStatek+8) && (y>yStatek-8 && y<yStatek+8))
                    {
                        InfoStatek info  = new InfoStatek(statek,self);
                        info.setVisible(true);
                    }
                }
            }
        });
        for(Statek statek: statki)
        {
            statek.start();
        }
        for(Samolot samolot: samoloty)
            samolot.start();
    }

    /***
     * Metoda dodająca początkowe statki do Panelu
     * @return
     */
    private ArrayList<Statek> dodajStatki() {
        ArrayList<Statek> statki = new ArrayList<>();
        Lotniskowiec sta1 = new Lotniskowiec(2,"Lotniskowiec1","Jacksonville","Reykjavik",grafTrasStatek,trasyStatek,miastaPortowe,"torpedy",lotniska);
        Statek sta2 = new StatekPasazerski(2,"titanic","Lizbona","Jacksonville",grafTrasStatek,trasyStatek,50,miastaPortowe,20,"Aba");
        Lotniskowiec sta3 = new Lotniskowiec(1,"Lotniskowiec2","Freetown","Jacksonville",grafTrasStatek,trasyStatek,miastaPortowe,"miny",lotniska);
        Statek sta4 = new StatekPasazerski(3,"Cruiser","Jacksonville","Luanda",grafTrasStatek,trasyStatek,50,miastaPortowe,20,"Aba");

        lotniskowce.add(sta1);
        lotniskowce.add(sta3);
        statki.add(sta1);
        statki.add(sta2);
        statki.add(sta3);
        statki.add(sta4);
        for(Statek statek: statki)
        {
            ArrayList<Statek> pozostaleStatki = (ArrayList<Statek>) statki.clone();
            pozostaleStatki.remove(statek);
            statek.setPozostaleStatki(pozostaleStatki);
        }
        return statki;
    }

    /***
     * Metoda dodająca Trasy Morskie do panelu
     * @return
     */
    private ArrayList<TrasaStatek> dodajTrasyStatek() {
        trasyStatek = new ArrayList<>();
        TrasaStatek jckLiz = new TrasaStatek(new int[]{289,270}, new int[]{509,236},"Jacksonville","Lizbona");
        TrasaStatek jckLua = new TrasaStatek(new int[]{291,276}, new int[]{575,405},"Jacksonville","Luanda");
        TrasaStatek lizRek = new TrasaStatek(new int[]{511,227}, new int[]{474,134},"Lizbona","Reykjavik");
        TrasaStatek rekFrt = new TrasaStatek(new int[]{467,132}, new int[]{417,368},"Reykjavik","Fortaleza");
        TrasaStatek frtFre = new TrasaStatek(new int[]{423,373}, new int[]{503,352},"Fortaleza","Freetown");
        TrasaStatek freLua = new TrasaStatek(new int[]{506,357}, new int[]{573,395},"Freetown","Luanda");


        ArrayList<Skrzyzowanie> skrzyzowaniaJckLiz= new ArrayList<>();
        ArrayList<Skrzyzowanie> skrzyzowaniaRekFrt= new ArrayList<>();
        ArrayList<Skrzyzowanie> skrzyzowaniaFrtFre= new ArrayList<>();
        ArrayList<Skrzyzowanie> skrzyzowaniaJckLua= new ArrayList<>();

        Skrzyzowanie jckLizRekFrt00 = new Skrzyzowanie(new int[]{442,246});
        Skrzyzowanie jckLizRekFrt10 = new Skrzyzowanie(new int[]{444,238});
        Skrzyzowanie jckLizRekFrt11 = new Skrzyzowanie(new int[]{452,236});
        Skrzyzowanie jckLizRekFrt01 = new Skrzyzowanie(new int[]{451,244});

        Skrzyzowanie jckLuaRekFrt00 = new Skrzyzowanie(new int[]{423,335});
        Skrzyzowanie jckLuaRekFrt10 = new Skrzyzowanie(new int[]{425,328});
        Skrzyzowanie jckLuaRekFrt11 = new Skrzyzowanie(new int[]{432,333});
        Skrzyzowanie jckLuaRekFrt01 = new Skrzyzowanie(new int[]{431,339});

        Skrzyzowanie jckLuaFrtFre00 = new Skrzyzowanie(new int[]{475,359});
        Skrzyzowanie jckLuaFrtFre10 = new Skrzyzowanie(new int[]{485,356});
        Skrzyzowanie jckLuaFrtFre11 = new Skrzyzowanie(new int[]{475,351});
        Skrzyzowanie jckLuaFrtFre01 = new Skrzyzowanie(new int[]{464,354});

        skrzyzowaniaFrtFre.add(jckLuaFrtFre00);
        skrzyzowaniaFrtFre.add(jckLuaFrtFre10);
        skrzyzowaniaFrtFre.add(jckLuaFrtFre11);
        skrzyzowaniaFrtFre.add(jckLuaFrtFre01);

        skrzyzowaniaJckLua.add(jckLuaFrtFre00);
        skrzyzowaniaJckLua.add(jckLuaFrtFre10);
        skrzyzowaniaJckLua.add(jckLuaFrtFre11);
        skrzyzowaniaJckLua.add(jckLuaFrtFre01);
        skrzyzowaniaJckLua.add(jckLuaRekFrt00);
        skrzyzowaniaJckLua.add(jckLuaRekFrt10);
        skrzyzowaniaJckLua.add(jckLuaRekFrt11);
        skrzyzowaniaJckLua.add(jckLuaRekFrt01);

        skrzyzowaniaRekFrt.add(jckLuaRekFrt00);
        skrzyzowaniaRekFrt.add(jckLuaRekFrt10);
        skrzyzowaniaRekFrt.add(jckLuaRekFrt11);
        skrzyzowaniaRekFrt.add(jckLuaRekFrt01);
        skrzyzowaniaRekFrt.add(jckLizRekFrt00);
        skrzyzowaniaRekFrt.add(jckLizRekFrt01);
        skrzyzowaniaRekFrt.add(jckLizRekFrt11);
        skrzyzowaniaRekFrt.add(jckLizRekFrt10);

        skrzyzowaniaJckLiz.add(jckLizRekFrt00);
        skrzyzowaniaJckLiz.add(jckLizRekFrt01);
        skrzyzowaniaJckLiz.add(jckLizRekFrt11);
        skrzyzowaniaJckLiz.add(jckLizRekFrt01);

        jckLiz.setSkrzyzowania(skrzyzowaniaJckLiz);
        rekFrt.setSkrzyzowania(skrzyzowaniaRekFrt);
        jckLua.setSkrzyzowania(skrzyzowaniaJckLua);
        frtFre.setSkrzyzowania(skrzyzowaniaFrtFre);


        trasyStatek.add(jckLiz);
        trasyStatek.add(jckLua);
        trasyStatek.add(lizRek);
        trasyStatek.add(rekFrt);
        trasyStatek.add(frtFre);
        trasyStatek.add(freLua);

        return trasyStatek;
    }

    /**
     * Metoda dodająca Miasta Portowe do Panelu
     * Z miast statki rozpoczynają podróż lecz podczas niej nigdy w nich nie stają
     * @return
     */
    private HashMap<String,MiastoPortowe> dodajMiastaportowe() {
        HashMap<String,MiastoPortowe> miastaPortowe = new HashMap<>();
        MiastoPortowe jacksonville = new MiastoPortowe(287,275,"Jacksonville");
        MiastoPortowe lizbona = new MiastoPortowe(512,240,"Lizbona");
        MiastoPortowe reykjavik = new MiastoPortowe(467,127,"Reykjavik");
        MiastoPortowe fortaleza = new MiastoPortowe(419,375,"Fortaleza");
        MiastoPortowe freetown = new MiastoPortowe(501,345,"Freetown");
        MiastoPortowe luanda = new MiastoPortowe(582,413,"Luanda");

        miastaPortowe.put(jacksonville.getNazwa(),jacksonville);
        miastaPortowe.put(lizbona.getNazwa(),lizbona);
        miastaPortowe.put(reykjavik.getNazwa(),reykjavik);
        miastaPortowe.put(fortaleza.getNazwa(),fortaleza);
        miastaPortowe.put(freetown.getNazwa(), freetown);
        miastaPortowe.put(luanda.getNazwa(),luanda);
        return miastaPortowe;
    }

    /***
     * Metoda dodająca Samoloty do Panelu
     * @return
     */
    private ArrayList<Samolot> dodajSamoloty() {
        ArrayList<Samolot> samoloty = new ArrayList<>();
        Samolot sam1 = new SamolotPasazerski(15,"Dreamliner",50,37,"LOT","Warszawa","Sydney",lotniska,grafTrasSamolot,trasySamolot,1);
        Samolot sam2 = new SamolotPasazerski(15,"Boeing 737",50,37,"Ryanair","Warszawa","Nowy Jork",lotniska,grafTrasSamolot,trasySamolot,2);
        Samolot sam3 = new SamolotPasazerski(15,"Boeing 883",50,37,"Lufhansa","Warszawa","Pekin",lotniska,grafTrasSamolot,trasySamolot,3);
        Samolot sam4 = new SamolotPasazerski(15,"Airbus 535",50,37,"PanAm","Pekin","Nowy Jork",lotniska,grafTrasSamolot,trasySamolot,2);
        Samolot sam5 = new SamolotPasazerski(15,"AirBus 845",50,37,"SAS","Kapsztad","Warszawa",lotniska,grafTrasSamolot,trasySamolot,2);
        Samolot sam6 = new SamolotPasazerski(15,"Cessna",50,37,"Quatar Airlines","Pekin","Warszawa",lotniska,grafTrasSamolot,trasySamolot,2);
        Samolot sam9 = new SamolotWojskowy(15,"Gunfire","Pekin","Bombowiec",lotniska,grafTrasSamolot,trasySamolot,2,"Lotniskowiec1",statki);

        samoloty.add(sam1);
        samoloty.add(sam2);
        samoloty.add(sam3);
        samoloty.add(sam4);
        samoloty.add(sam5);
        samoloty.add(sam6);
        samoloty.add(sam9);
        for(Samolot samolot: samoloty)
        {
            ArrayList<Samolot> pozostaleSamoloty = (ArrayList<Samolot>) samoloty.clone();
            pozostaleSamoloty.remove(samolot);
            samolot.setPozostaleSamoloty(pozostaleSamoloty);
        }
        return samoloty;
    }


    /***
     * Metoda dodająca trasy Lotnicze do Panelu
     * @return
     */
    public HashMap<HashSet<String>,TrasaSamolot> dodajTrasySamolot() {
        trasySamolot = new HashMap<>(6);
        TrasaSamolot wwaNy = new TrasaSamolot(new int[]{317,220}, new int[]{617,158},2,"Nowy Jork","Warszawa");
        TrasaSamolot cptWwa = new TrasaSamolot(new int[]{604,456}, new int[]{619,194},1,"Kapsztad","Warszawa");
        TrasaSamolot sydWwa = new TrasaSamolot(new int[]{1007,460}, new int[]{642,189},2,"Sydney","Warszawa");
        TrasaSamolot sydPek = new TrasaSamolot(new int[]{1017,460}, new int[]{906,236},2,"Sydney","Pekin");
        TrasaSamolot pekNy = new TrasaSamolot(new int[]{886,222}, new int[]{318,226},1,"Pekin","Nowy Jork");
        TrasaSamolot cptSyd = new TrasaSamolot(new int[]{631,476}, new int[]{1002,477},1,"Kapsztad","Sydney");

        ArrayList<Skrzyzowanie> skrzyzowaniaPekNy= new ArrayList<>();
        ArrayList<Skrzyzowanie> skrzyzowaniaCptWwa= new ArrayList<>();
        ArrayList<Skrzyzowanie> skrzyzowaniaWwaSyd= new ArrayList<>();
        Skrzyzowanie pekNySydWwa1 = new Skrzyzowanie(new int[]{699,223});
        Skrzyzowanie pekNySydWwa2 = new Skrzyzowanie(new int[]{688,223});
        Skrzyzowanie cptWwaPekNy  = new Skrzyzowanie(new int[]{617,223});

        skrzyzowaniaPekNy.add(pekNySydWwa1);
        skrzyzowaniaPekNy.add(pekNySydWwa2);
        skrzyzowaniaPekNy.add(cptWwaPekNy);

        skrzyzowaniaWwaSyd.add(pekNySydWwa1);
        skrzyzowaniaWwaSyd.add(pekNySydWwa2);

        skrzyzowaniaCptWwa.add(cptWwaPekNy);


        pekNy.setSkrzyzowania((ArrayList<Skrzyzowanie>) skrzyzowaniaPekNy.clone());
        sydWwa.setSkrzyzowania((ArrayList<Skrzyzowanie>) skrzyzowaniaWwaSyd.clone());
        cptWwa.setSkrzyzowania((ArrayList<Skrzyzowanie>) skrzyzowaniaCptWwa.clone());

        trasySamolot.put(wwaNy.getKonceMiasta(),wwaNy);
        trasySamolot.put(cptWwa.getKonceMiasta(),cptWwa);
        trasySamolot.put(sydWwa.getKonceMiasta(),sydWwa);
        trasySamolot.put(sydPek.getKonceMiasta(), sydPek);
        trasySamolot.put(pekNy.getKonceMiasta(), pekNy);
        trasySamolot.put(cptSyd.getKonceMiasta(), cptSyd);

        return trasySamolot;
    }

    /***
     * Metoda dodająca Lotniska do Panelu
     * @return
     */
    public HashMap<String, Lotnisko> dodajLotniska() {
        lotniska = new HashMap<String, Lotnisko>(12);
        Lotnisko nyc = new Lotnisko(10, 299, 234, "Cywilne", "Nowy Jork");
        Lotnisko nyw = new Lotnisko(10, 305, 224, "Wojskowe", "Nowy Jork");
        Lotnisko wwac = new Lotnisko(10, 615, 184, "Cywilne", "Warszawa");
        Lotnisko wwaw = new Lotnisko(10, 630, 184, "Wojskowe", "Warszawa");
        Lotnisko cptc = new Lotnisko(10, 608, 481, "Cywilne", "Kapsztad");
        Lotnisko cptw = new Lotnisko(10, 623, 481, "Wojskowe", "Kapsztad");
        Lotnisko sydc = new Lotnisko(10, 1030, 481, "Cywilne", "Sydney");
        Lotnisko sydw = new Lotnisko(10, 1015, 481, "Wojskowe", "Sydney");
        Lotnisko pekc = new Lotnisko(10, 911, 229, "Cywilne", "Pekin");
        Lotnisko pekw = new Lotnisko(10, 896, 229, "Wojskowe", "Pekin");
        lotniska.put(nyc.getNazwa()+nyc.getTyp(), nyc);
        lotniska.put(nyw.getNazwa()+nyw.getTyp(), nyw);
        lotniska.put(wwac.getNazwa()+wwac.getTyp(), wwac);
        lotniska.put(wwaw.getNazwa()+wwaw.getTyp(), wwaw);
        lotniska.put(cptc.getNazwa()+cptc.getTyp(), cptc);
        lotniska.put(cptw.getNazwa()+cptw.getTyp(), cptw);
        lotniska.put(sydc.getNazwa()+sydc.getTyp(), sydc);
        lotniska.put(sydw.getNazwa()+sydw.getTyp(), sydw);
        lotniska.put(pekc.getNazwa()+pekc.getTyp(), pekc);
        lotniska.put(pekw.getNazwa()+pekw.getTyp(), pekw);
        return lotniska;
    }

    /***
     * obsługa rysowania poszczególnych komponentów
     * @param g
     */
    public void rysuj(Graphics g) {
        rysujLotnisko(g);
        rysujStatek(g);
        rysujSamolot(g);
    }

    /***
     * Rysowanie statków w Panelu
     * @param g
     */
    private void rysujStatek(Graphics g) {
        for(Statek statek : statki)
        {
            if(statek instanceof StatekPasazerski)
            g.drawRect(statek.getX(),statek.getY(),4,4);
            else if(statek instanceof Lotniskowiec)
            {
                g.fillRect(statek.getX(),statek.getY(),5,5);
            }
        }
    }

    /***
     * rysowanie Samolotów w Panelu
     * @param g
     */
    private void rysujSamolot(Graphics g) {
        for(Samolot samolot : samoloty) {
            if (samolot.getStanPodrozy().equals("wyladowal") || samolot.getStanPodrozy().equals("zepsuty")) {
                continue;
            } else {
                if(samolot instanceof SamolotPasazerski) {
                    g.drawOval(samolot.getX(), samolot.getY(), 4, 4);
                }
                else if(samolot instanceof SamolotWojskowy)
                {
                    g.fillOval(samolot.getX(),samolot.getY(),5,5 );
                }
            }
        }
    }

    /***
     * Rysowanie Lotnisk w Panelu
     * @param g
     */
    public void rysujLotnisko(Graphics g) {
        for (Map.Entry lotnisko : lotniska.entrySet()) {
            g.drawImage(lotniska.get(lotnisko.getKey()).getIkona(), lotniska.get(lotnisko.getKey()).getX() - lotniska.get(lotnisko.getKey()).getIkona().getWidth(null) / 2,
                    lotniska.get(lotnisko.getKey()).getY() - lotniska.get(lotnisko.getKey()).getIkona().getHeight(null), this);
        }
    }

    /***
     * przeciążona metoda rysowania komponentów
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image.getImage(), 0, 0, null);
        rysuj(g);
    }

    /***
     * Odświeżanie
     */
    public void zapetl() {
        this.repaint();
    }

    /***
     * Dodawanie Lotniskowca zadeklarowanego przez użytkownika
     * @param cel
     * @param start
     * @param uzbrojenie
     * @param predkosc
     * @param nazwa
     */
    public void dodajLotniskowiec(String cel,String start,String uzbrojenie,int predkosc,String nazwa)
    {
        Lotniskowiec nowyLotniskowiec = new Lotniskowiec(predkosc,nazwa,cel,start,grafTrasStatek,trasyStatek,miastaPortowe,uzbrojenie,lotniska);
        lotniskowce.add(nowyLotniskowiec);
        dodajNowyStatek(nowyLotniskowiec);
        nowyLotniskowiec.start();

    }

    /***
     * Dodawanie Statku Pasażerskiego zadeklarowanego przez użytkownika
     * @param cel
     * @param start
     * @param nazwa
     * @param predkosc
     * @param liczbaPasazer
     * @param maksPojemnosc
     * @param firma
     */
    public void dodajStatekPasazerski(String cel,String start,String nazwa,int predkosc,int liczbaPasazer,int maksPojemnosc,String firma)
    {
        Statek nowyStatekPasazerski = new StatekPasazerski(predkosc,nazwa,cel,start,grafTrasStatek,trasyStatek,maksPojemnosc,miastaPortowe,liczbaPasazer,firma);
        dodajNowyStatek(nowyStatekPasazerski);
        nowyStatekPasazerski.start();
    }

    /***
     * Dodawanie Statku zadeklarowanego przez użytkownika do listy statków oraz powiadomienie inncyh statków o jego istnieniu
     * poprzez wprowadzenie go do ich ArrayListy pozostałych Statków
     * @param nowyStatek
     */
    public void dodajNowyStatek(Statek nowyStatek)
    {
        statki.add(nowyStatek);
        for(Statek statek: statki)
        {
            ArrayList<Statek> pozostaleStatki = (ArrayList<Statek>) statki.clone();
            pozostaleStatki.remove(statek);
            statek.setPozostaleStatki(pozostaleStatki);
        }
    }

    /***
     * Dodawanie Samolotu wojskowego Zadeklarowanego przez Użytkownika oraz rozpoczęcie przez niego podróży
     * @param nazwa
     * @param liczbaPersonelu
     * @param cel
     * @param typUzbrojenia
     * @param predkosc
     * @param nazwaStatkuTworzacego
     */
    public void dodajSamolotWojskowy(String nazwa,int liczbaPersonelu,String cel,String typUzbrojenia,int predkosc,String nazwaStatkuTworzacego)
    {
        Samolot nowySamolotWojskowy = new SamolotWojskowy(liczbaPersonelu,nazwa,cel,typUzbrojenia,lotniska,grafTrasSamolot,trasySamolot,predkosc,nazwaStatkuTworzacego,statki);
        dodajNowySamolot(nowySamolotWojskowy);
        nowySamolotWojskowy.start();
    }

    /***
     * Dodawanie nowego Samolotu Pasażerskiego oraz rozpoczęcie jego podróży
     * @param nazwa
     * @param liczbaPersonelu
     * @param liczbaPasazerow
     * @param pojemnosc
     * @param firma
     * @param cel
     * @param start
     * @param predkosc
     */
    public void dodajSamolotPasazerski(String nazwa,int liczbaPersonelu,int liczbaPasazerow,int pojemnosc,String firma,String cel,String start,int predkosc)
    {
        Samolot nowySamolotPasazerski = new SamolotPasazerski(liczbaPersonelu,nazwa,pojemnosc,liczbaPasazerow,firma,start,cel,lotniska,grafTrasSamolot,trasySamolot,predkosc);
        dodajNowySamolot(nowySamolotPasazerski);
        nowySamolotPasazerski.start();
    }

    /***
     * Dodanie nowego Samolotu do rejestru samolotów w panelu oraz powiadomienie innych statków o jego istnieniu
     * @param nowySamolot
     */
    private void dodajNowySamolot(Samolot nowySamolot) {
        samoloty.add(nowySamolot);
        for(Samolot samolot: samoloty)
        {
            ArrayList<Samolot> pozostaleSamoloty = (ArrayList<Samolot>) samoloty.clone();
            pozostaleSamoloty.remove(samolot);
            samolot.setPozostaleSamoloty(pozostaleSamoloty);
        }
    }

    /***
     * getter Miast Portowych
     * @return
     */
    public HashMap<String, MiastoPortowe> getMiastaPortowe() {
        return miastaPortowe;
    }

    /***
     * getter Lotnisk
     * @return
     */
    public HashMap<String, Lotnisko> getLotniska() {
        return lotniska;
    }

    /***
     * getter Lotniskowców
     * @return
     */
    public ArrayList<Lotniskowiec> getLotniskowce() {
        return lotniskowce;
    }

    /***
     * getter Samolotów
     * @return
     */
    public ArrayList<Samolot> getSamoloty() {
        return samoloty;
    }

    /***
     * getter statków
     * @return
     */
    public ArrayList<Statek> getStatki() {
        return statki;
    }
}
