package Mapa;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/***
 * Klasa grafu Tras Samolotu
 */
public class GrafTrasSamolot {
    private HashMap<HashSet<String>,TrasaSamolot> trasy;
    private ArrayList<ArrayList<Integer>> macierzSasiedztwa;
    private ArrayList<String> listaMiast;
    private  int liczbaMiast;

    /***
     * Konstruktor Grafu Tras Samolotu
     * @param trasy
     */
    public GrafTrasSamolot(HashMap<HashSet<String>,TrasaSamolot> trasy) {
        this.trasy = trasy;
        this.listaMiast = dodajMiasta();
        this.liczbaMiast = listaMiast.size();
        this.macierzSasiedztwa = tworzMacierzSasiedztwa(listaMiast);
    }

    /***
     * metoda zwaracająca zwracające ArrayListe wszystkich Miast Z Lotniskami
     * @return
     */
    public ArrayList<String> dodajMiasta()
    {
        ArrayList<String> miasta = new ArrayList<String>();
        for(TrasaSamolot trasa : trasy.values() )
        {
            HashSet<String> konceTrasy = trasa.getKonceMiasta();
            for(String miasto : konceTrasy) {
                if (!(miasta.contains(miasto))) {
                    miasta.add(miasto);
                }
            }
        }
        return miasta;
    }

    private ArrayList<ArrayList<Integer>> tworzMacierzSasiedztwa(ArrayList<String> listaMiast) {
            ArrayList<ArrayList<Integer>>grafPolaczen =new ArrayList<>();
            for (int i = 0; i<liczbaMiast;i++)
            {
                grafPolaczen.add(new ArrayList<Integer>(liczbaMiast));
                for (int j = 0; j<liczbaMiast;j++) {
                    grafPolaczen.get(i).add(0);
                }

            }
            for(TrasaSamolot trasa: trasy.values())
            {
                ArrayList<Integer> indeksy = new ArrayList<>();
                for(String miasto: trasa.getKonceMiasta()) {

                    indeksy.add(listaMiast.indexOf(miasto));
                }
                grafPolaczen.get(indeksy.get(0)).set(indeksy.get(1), 1);
                grafPolaczen.get(indeksy.get(1)).set(indeksy.get(0), 1);
                indeksy.clear();
            }
            return grafPolaczen;
    }
    private ArrayList<ArrayList<Integer>> wyznaczWszystkieTrasyAB(int start,int cel)
    {
        boolean [] miastoOdwiedzone = new boolean[liczbaMiast];
        ArrayList<Integer> sciezka = new ArrayList<>();
        ArrayList<ArrayList<Integer>> wszystkieSciezki = new ArrayList<>();
        sciezka.add(start);
        wszystkieSciezki = dfs(start,cel,sciezka,miastoOdwiedzone,wszystkieSciezki);
        return wszystkieSciezki;
    }

    private ArrayList<ArrayList<Integer>> dfs(int aktualneMiasto, int cel, ArrayList<Integer> sciezka, boolean[] miastoOdwiedzone, ArrayList<ArrayList<Integer>> wszystkieSciezki) {
        miastoOdwiedzone[aktualneMiasto] = true;
        if(aktualneMiasto == cel)
        {
            ArrayList<Integer> sciezkaWyznaczona = (ArrayList<Integer>) sciezka.clone();
            wszystkieSciezki.add(sciezkaWyznaczona);
            miastoOdwiedzone[cel] = false;
            return wszystkieSciezki;

        }
        for(int nrMiasta=0;nrMiasta<liczbaMiast;nrMiasta++)
        {
            if(macierzSasiedztwa.get(aktualneMiasto).get(nrMiasta)==1 && miastoOdwiedzone[nrMiasta]==false)
            {
                sciezka.add(nrMiasta);
                wszystkieSciezki = dfs(nrMiasta,cel,sciezka,miastoOdwiedzone,wszystkieSciezki);
                sciezka.remove(Integer.valueOf(nrMiasta));
            }
        }
        miastoOdwiedzone[aktualneMiasto] = false;
        return wszystkieSciezki;
    }

    /***
     * Wyznaczanie losowej trasy pomiędzy dwoma Miastami zwracana jest lista kolejnych miast na trasie
     * @param start
     * @param cel
     * @return
     */
    public ArrayList<String> wyznaczLosowaTrase(String start,String cel)
    {
        int nrWierzcholkaMiastaStart = listaMiast.indexOf(start);
        int nrWierzcholkaMiastaCel = listaMiast.indexOf(cel);
        ArrayList<ArrayList<Integer>> wszystkieTrasyNrWierzcholkow = wyznaczWszystkieTrasyAB(nrWierzcholkaMiastaStart,nrWierzcholkaMiastaCel);
        Random generatorLosowy = new Random();
        int indexLosowejTrasy = generatorLosowy.nextInt(wszystkieTrasyNrWierzcholkow.size());
        ArrayList<Integer> trasaNrWierzcholkow = wszystkieTrasyNrWierzcholkow.get(indexLosowejTrasy);
        ArrayList<String> trasa = transformIntNaStr(trasaNrWierzcholkow);
        return trasa;
    }

    private ArrayList<String> transformIntNaStr(ArrayList<Integer> trasaNrWierzcholkow) {
        ArrayList<String> trasa = new ArrayList<>();
        for(Integer nrWierzcholka: trasaNrWierzcholkow)
        {
            String Miasto = listaMiast.get(nrWierzcholka);
            trasa.add(Miasto);
        }
        return  trasa;
    }
}
