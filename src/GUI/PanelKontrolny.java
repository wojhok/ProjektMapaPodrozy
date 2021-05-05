package GUI;

import Lotnisko.Lotnisko;
import Mapa.MiastoPortowe;
import Vehicle.Lotniskowiec;
import Vehicle.Statek;

import javax.swing.*;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/***
 * Klasa Panelu Kontrolnego
 */
public class PanelKontrolny extends JFrame implements ActionListener {
    private JRadioButton buttonStatekPasazerski;
    private JRadioButton buttonSamolotWojskowy;
    private JRadioButton buttonSamolotCywilny;
    private JRadioButton buttonLotniskowiec;
    private JPanel radioPanel;
    private JPanel tekst;
    private JPanel przycisk;
    private JButton tworz;
    private JLabel typPojazdu;
    private MapaPanel mapaPanel;
    private JTextField nazwaIn,firmaIn,pojemnoscIn,liczbaPasazerIn,liczbaPersoneluIn,liczPasazerIn;
    private ButtonGroup pojazd;
    private JComboBox celIn,startIn,predkoscIn,uzbrojenieIn;

    /***
     * Konstruktor Panelu Kontrolnego
     * @param mapa
     */
    public PanelKontrolny(MapaPanel mapa)
    {
        super("PanelSterowania");
        this.mapaPanel = mapa;
        radioPanel = new JPanel();

        buttonLotniskowiec = new JRadioButton("Lotniskowiec");
        buttonSamolotCywilny = new JRadioButton("Samolot Cywilny");
        buttonSamolotWojskowy = new JRadioButton("Samolot Wojskowy");
        buttonStatekPasazerski = new JRadioButton("Statek Pasażerski");

        pojazd = new ButtonGroup();
        pojazd.add(buttonLotniskowiec);
        buttonLotniskowiec.addActionListener(this);
        buttonLotniskowiec.setActionCommand("Lotniskowiec");
        pojazd.add(buttonSamolotCywilny);
        buttonSamolotCywilny.addActionListener(this);
        buttonSamolotCywilny.setActionCommand("Samolot Pasażerski");
        pojazd.add(buttonSamolotWojskowy);
        buttonSamolotWojskowy.addActionListener(this);
        buttonSamolotWojskowy.setActionCommand("Samolot Wojskowy");
        pojazd.add(buttonStatekPasazerski);
        buttonStatekPasazerski.addActionListener(this);
        buttonStatekPasazerski.setActionCommand("Statek Pasażerski");

        radioPanel.add(buttonLotniskowiec);
        radioPanel.add(buttonSamolotCywilny);
        radioPanel.add(buttonSamolotWojskowy);
        radioPanel.add(buttonStatekPasazerski);

        tekst = new JPanel(new GridBagLayout());

        przycisk = new JPanel(new BorderLayout());
        tworz =  new JButton("Twórz");
        przycisk.add(tworz,BorderLayout.CENTER);
        tworz.addActionListener(this);


        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(550,600);
        this.setResizable(false);
        this.setLayout(new BorderLayout(0,0));

        this.add(BorderLayout.NORTH,radioPanel);
        this.add(BorderLayout.CENTER,tekst);
        this.add(BorderLayout.SOUTH,przycisk);

    }
    @Override
    /***
     * Obsługa Przcysików w Panelu Kontrolnym
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == tworz) {
            String wybranyPojazd = getSelectedButtonText();
            if (wybranyPojazd.equals("Statek Pasażerski")) {
                int pojemnoscStatku = 0;
                int liczPasazer = 0;
                boolean sprawdzWartosci = true;
                String nazwaStatkuPasazeskiego = nazwaIn.getText();
                String cel = (String) celIn.getSelectedItem();
                String start = (String) startIn.getSelectedItem();
                int predkosc = (Integer) predkoscIn.getSelectedItem();
                String firma = firmaIn.getText();
                try {
                    pojemnoscStatku = Integer.parseInt(pojemnoscIn.getText());
                } catch (NumberFormatException nfe) {
                    sprawdzWartosci = false;
                }
                try {
                    liczPasazer = Integer.parseInt(liczbaPasazerIn.getText());
                } catch (NumberFormatException nfe) {
                    sprawdzWartosci = false;
                }
                boolean prawidlowaLiczbaPasazer = true;
                if (pojemnoscStatku< liczPasazer)
                {
                    prawidlowaLiczbaPasazer =false;
                }
                if (!(cel.equals(start)) && sprawdzWartosci && prawidlowaLiczbaPasazer) {
                    mapaPanel.dodajStatekPasazerski(cel, start, nazwaStatkuPasazeskiego, predkosc, liczPasazer, pojemnoscStatku, firma);
                }
                else
                {
                    if(!(sprawdzWartosci))
                    JOptionPane.showMessageDialog(null, "Podano nieodpowiednie wartości w polach przeznaczonych na wartości liczbowe",
                            "Error",JOptionPane.ERROR_MESSAGE);
                    if(cel.equals(start)) {
                        JOptionPane.showMessageDialog(null, "Cel oraz start nie może być ten sam",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                    if(!(prawidlowaLiczbaPasazer))
                    {
                        JOptionPane.showMessageDialog(null, "Liczba pasażerów nie może być większa od pojemności",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (wybranyPojazd.equals("Lotniskowiec")) {
                String nazwaLotniskowca = nazwaIn.getText();
                String cel = (String) celIn.getSelectedItem();
                String start = (String) startIn.getSelectedItem();
                int predkosc = (Integer) predkoscIn.getSelectedItem();
                String uzbrojenie = (String) uzbrojenieIn.getSelectedItem();
                if (!(cel.equals(start))) {
                    mapaPanel.dodajLotniskowiec(cel, start, uzbrojenie, predkosc, nazwaLotniskowca);
                }
                else
                {
                    if(cel.equals(start)) {
                        JOptionPane.showMessageDialog(null, "Pola cel oraz start nie moga zawierać tych samych wartości",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }

                }
            } else if (wybranyPojazd.equals("Samolot Pasażerski")) {
                int pojemnoscSamolotu = 0;
                int liczPasazer = 0;
                int liczbaPersonelu = 0;
                boolean sprawdzWartosci =true;
                boolean czyLotniskoWolne = true;
                String nazwaSamolotuPasazerskiego =nazwaIn.getText();
                String cel = (String) celIn.getSelectedItem();
                String start = (String) startIn.getSelectedItem();
                String firma = firmaIn.getText();
                int predkosc = (Integer) predkoscIn.getSelectedItem();
                try {
                    pojemnoscSamolotu = Integer.parseInt(pojemnoscIn.getText());
                } catch (NumberFormatException nfe) {
                    sprawdzWartosci = false;
                }
                try {
                    liczPasazer = Integer.parseInt(liczPasazerIn.getText());
                } catch (NumberFormatException nfe) {
                    sprawdzWartosci = false;
                }
                try {
                    liczbaPersonelu = Integer.parseInt(liczbaPersoneluIn.getText());
                } catch (NumberFormatException nfe) {
                    sprawdzWartosci = false;
                }
                boolean prawidlowaLiczbaPasazer = true;
                if (pojemnoscSamolotu< liczPasazer)
                {
                    prawidlowaLiczbaPasazer =false;
                }
                if(mapaPanel.getLotniska().get(start+"Cywilne").getPojemnosc()<= mapaPanel.getLotniska().get(start+"Cywilne").getAktualnaLiczbaSamolotow())
                {
                    czyLotniskoWolne = false;
                }
                if(!(cel.equals(start)) && sprawdzWartosci && prawidlowaLiczbaPasazer && czyLotniskoWolne)
                {
                    mapaPanel.dodajSamolotPasazerski(nazwaSamolotuPasazerskiego,liczbaPersonelu,liczPasazer,pojemnoscSamolotu,firma,cel,start,predkosc);
                }
                else
                {
                    if(!(sprawdzWartosci))
                        JOptionPane.showMessageDialog(null, "Podano nieodpowiednie wartości w polach przeznaczonych na wartości liczbowe",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    if(cel.equals(start)) {
                        JOptionPane.showMessageDialog(null, "Cel oraz start nie może być ten sam",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                    if(!(prawidlowaLiczbaPasazer))
                    {
                        JOptionPane.showMessageDialog(null, "Liczba pasażerów nie może być większa od pojemności",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                    if(!(czyLotniskoWolne))
                    {
                        JOptionPane.showMessageDialog(null, "Lotnisko startowe jest zapełnione",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else if (wybranyPojazd.equals("Samolot Wojskowy")) {
                boolean sprawdzWartosci = true;
                boolean typStatku = true;
                boolean czyStartPoprawny = true;
                boolean czyMiejsceNaLotnisku = true;
                int liczbaPersonelu = 0;
                String nazwaSamolotuWojskowego = nazwaIn.getText();
                String cel = (String) celIn.getSelectedItem();
                String start = (String) startIn.getSelectedItem();
                int predkosc = (Integer) predkoscIn.getSelectedItem();
                String uzbrojenie = (String) uzbrojenieIn.getSelectedItem();
                try {
                    liczbaPersonelu = Integer.parseInt(liczbaPersoneluIn.getText());
                } catch (NumberFormatException nfe) {
                    sprawdzWartosci = false;
                }
                if (uzbrojenie.equals("myśliwiec")) {
                    for (Lotniskowiec lotniskowiec : mapaPanel.getLotniskowce()) {
                        if (lotniskowiec.getNazwa().equals(start) && !(lotniskowiec.getUzbrojenie().equals("torpedy")))
                        {
                            typStatku = false;
                        }
                    }
                }
                else if (uzbrojenie.equals("bombowiec")) {
                    for (Lotniskowiec lotniskowiec : mapaPanel.getLotniskowce()) {
                        if (lotniskowiec.getNazwa().equals(start) && !(lotniskowiec.getUzbrojenie().equals("miny")))
                        {
                            typStatku = false;
                        }
                    }
                }
                for (Lotniskowiec lotniskowiec : mapaPanel.getLotniskowce()) {
                    if (lotniskowiec.getNazwa().equals(start) && lotniskowiec.znajdzNajblizeszeMiasto().equals(cel))
                    {
                        czyStartPoprawny = false;
                    }
                }
                if(sprawdzWartosci && typStatku && czyStartPoprawny)
                {
                    mapaPanel.dodajSamolotWojskowy(nazwaSamolotuWojskowego,liczbaPersonelu,cel,uzbrojenie,predkosc,start);
                }
                else
                {
                    if(!(sprawdzWartosci))
                        JOptionPane.showMessageDialog(null, "Podano nieodpowiednie wartości w polach przeznaczonych na wartości liczbowe",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    if(!(czyStartPoprawny)) {
                        JOptionPane.showMessageDialog(null, "Najblizsze lotnisko czyli start jest takie samo jak cel. Popraw wartości lub poczekaj na zmianę położenia wybranego lotniskowca.",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                    if(!(typStatku))
                    {
                        JOptionPane.showMessageDialog(null, "Podany typ lotniskowca nie może wytworzyć danego typu samolotu.",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        }
        if(e.getSource() == buttonStatekPasazerski)
        {
            tekst.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx =0;
            gbc.gridy = 0;
            typPojazdu = new JLabel("Statek Pasażerski:");
            tekst.add(typPojazdu,gbc);

            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel nazwa = new JLabel("Nazwa:");
            tekst.add(nazwa,gbc);

            gbc.gridx = 4;
            gbc.gridy=2;
            nazwaIn = new JTextField("");
            tekst.add(nazwaIn,gbc);

            gbc.gridx=0;
            gbc.gridy =3;
            JLabel predkosc = new JLabel("Prędkość:");
            tekst.add(predkosc,gbc);

            gbc.gridx=4;
            gbc.gridy=3;
            Integer[] predkoscWybory = new Integer[3];
            int inc=1;
            for(int i=0;i<3;i++){
                predkoscWybory[i]= inc;
                inc++;
            }
            predkoscIn = new JComboBox<>(predkoscWybory);
            tekst.add(predkoscIn,gbc);

            gbc.gridx = 0;
            gbc.gridy= 4;
            JLabel pojemnosc = new JLabel("Pojemność Statku");
            tekst.add(pojemnosc,gbc);

            gbc.gridx = 4;
            gbc.gridy = 4;
            pojemnoscIn =  new JTextField("");
            tekst.add(pojemnoscIn,gbc);

            gbc.gridx= 0;
            gbc.gridy = 5 ;
            JLabel liczbaPasazer = new JLabel("Liczba Pasażerów:");
            tekst.add(liczbaPasazer,gbc);

            gbc.gridx = 4;
            gbc.gridy = 5;
            liczbaPasazerIn =  new JTextField("");
            tekst.add(liczbaPasazerIn,gbc);

            gbc.gridx =0;
            gbc.gridy =6;
            JLabel firma = new JLabel("Firma:");
            tekst.add(firma,gbc);

            gbc.gridx = 4;
            gbc.gridy = 6;
            firmaIn =  new JTextField("");
            tekst.add(firmaIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 7;
            JLabel cel = new JLabel("Cel:");
            tekst.add(cel,gbc);

            gbc.gridx = 4;
            gbc.gridy = 7;
            HashMap<String, MiastoPortowe> miasta =  mapaPanel.getMiastaPortowe();
            String[] nazwyMiast = miasta.keySet().toArray(new String[miasta.size()]);
            celIn = new JComboBox(nazwyMiast);
            tekst.add(celIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 8;
            JLabel start = new JLabel("Start:");
            tekst.add(start,gbc);

            gbc.gridx = 4;
            gbc.gridy = 8;
            startIn = new JComboBox(nazwyMiast);
            tekst.add(startIn,gbc);

            tekst.updateUI();

        }
        if(e.getSource() == buttonSamolotWojskowy)
        {
            tekst.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx=0;
            gbc.gridy=0;
            typPojazdu = new JLabel("Samolot Wojskowy:");
            tekst.add(typPojazdu,gbc);

            gbc.gridx =0;
            gbc.gridy= 2;
            JLabel nazwa = new JLabel("Nazwa:");
            tekst.add(nazwa,gbc);

            gbc.gridx=4;
            gbc.gridy=2;
            nazwaIn = new JTextField("");
            tekst.add(nazwaIn,gbc);

            gbc.gridx=0;
            gbc.gridy=3;
            JLabel predkosc = new JLabel("Prędkość:");
            tekst.add(predkosc,gbc);

            gbc.gridx = 4;
            gbc.gridy = 3;
            Integer[] predkoscWybory = new Integer[3];
            int inc=1;
            for(int i=0;i<3;i++){
                predkoscWybory[i]= inc;
                inc++;
            }
            predkoscIn = new JComboBox(predkoscWybory);
            tekst.add(predkoscIn, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            JLabel liczbaPersonelu = new JLabel("Liczba Personelu:");
            tekst.add(liczbaPersonelu,gbc);

            gbc.gridx = 4;
            gbc.gridy = 4;
            liczbaPersoneluIn =  new JTextField("");
            tekst.add(liczbaPersoneluIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            JLabel uzbrojenie = new JLabel("Typ Uzbrojenia:");
            tekst.add(uzbrojenie,gbc);

            gbc.gridx = 4;
            gbc.gridy = 6;
            String[] typyUzbrojenia = new String[]{"bombowiec","myśliwiec"};
            uzbrojenieIn =  new JComboBox(typyUzbrojenia);
            tekst.add(uzbrojenieIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 7;
            JLabel cel = new JLabel("Cel:");
            tekst.add(cel,gbc);

            gbc.gridx = 4;
            gbc.gridy = 7;
            HashMap<String, Lotnisko> lotniskaMap =  mapaPanel.getLotniska();
            Collection<Lotnisko> wartosci = lotniskaMap.values();
            ArrayList<Lotnisko> lotniska = new ArrayList<Lotnisko>(wartosci);
            ArrayList<String> nazwyMiastArray = new ArrayList<>();
            for(Lotnisko lotnisko:lotniska)
            {
                if(lotnisko.getTyp().equals("Wojskowe"))
                {
                    nazwyMiastArray.add(lotnisko.getNazwa());
                }
            }
            String[] nazwyMiast = nazwyMiastArray.toArray(new String[nazwyMiastArray.size()]);
            celIn = new JComboBox(nazwyMiast);
            tekst.add(celIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 8;
            JLabel start = new JLabel("Start:");
            tekst.add(start,gbc);

            gbc.gridx = 4;
            gbc.gridy = 8;
            ArrayList<Lotniskowiec> lotniskowce = mapaPanel.getLotniskowce();
            ArrayList<String> nazwyLotniskowcowArray = new ArrayList<>();
            for(Lotniskowiec lotniskowiec :lotniskowce)
            {
                    nazwyLotniskowcowArray.add(lotniskowiec.getNazwa());

            }
            String[] nazwyLotniskowcow = nazwyLotniskowcowArray.toArray(new String[nazwyLotniskowcowArray.size()]);
            startIn = new JComboBox(nazwyLotniskowcow);
            tekst.add(startIn,gbc);

            tekst.updateUI();
        }
        if(e.getSource() == buttonSamolotCywilny)
        {
            tekst.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx= 0;
            gbc.gridy = 0;
            typPojazdu = new JLabel("Samolot Cywilny:");
            tekst.add(typPojazdu,gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel nazwa = new JLabel("Nazwa:");
            tekst.add(nazwa,gbc);

            gbc.gridx=4;
            gbc.gridy = 2;
            nazwaIn = new JTextField("");
            tekst.add(nazwaIn,gbc);

            gbc.gridx=0;
            gbc.gridy=3;
            JLabel predkosc = new JLabel("Prędkość:");
            tekst.add(predkosc,gbc);

            gbc.gridx=4;
            gbc.gridy=3;
            Integer[] predkoscWybory = new Integer[3];
            int inc=1;
            for(int i=0;i<3;i++){
                predkoscWybory[i]= inc;
                inc++;
            }
            predkoscIn = new JComboBox(predkoscWybory);
            tekst.add(predkoscIn,gbc);

            gbc.gridx= 0;
            gbc.gridy= 4;
            JLabel liczbaPersonelu = new JLabel("Liczba Personelu:");
            tekst.add(liczbaPersonelu,gbc);

            gbc.gridx= 4;
            gbc.gridy =4;
            liczbaPersoneluIn =  new JTextField("");
            tekst.add(liczbaPersoneluIn,gbc);

            gbc.gridx= 0;
            gbc.gridy=5;
            JLabel pojemnosc = new JLabel("Pojemnosc:");
            tekst.add(pojemnosc,gbc);

            gbc.gridx = 4;
            gbc.gridy = 5;
            pojemnoscIn =  new JTextField("");
            tekst.add(pojemnoscIn,gbc);

            gbc.gridx=0;
            gbc.gridy=6;
            JLabel liczPasazer = new JLabel("Aktualna liczba pasażerów:");
            tekst.add(liczPasazer,gbc);

            gbc.gridx=4;
            gbc.gridy=6;
            liczPasazerIn = new JTextField("");
            tekst.add(liczPasazerIn,gbc);

            gbc.gridx=0;
            gbc.gridy=7;
            JLabel firma = new JLabel("Firma:");
            tekst.add(firma,gbc);

            gbc.gridx=4;
            gbc.gridy=7;
            firmaIn =  new JTextField("");
            tekst.add(firmaIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 8;
            JLabel cel = new JLabel("Cel:");
            tekst.add(cel,gbc);

            gbc.gridx = 4;
            gbc.gridy = 8;
            HashMap<String, Lotnisko> lotniskaMap =  mapaPanel.getLotniska();
            Collection<Lotnisko> wartosci = lotniskaMap.values();
            ArrayList<Lotnisko> lotniska = new ArrayList<Lotnisko>(wartosci);
            ArrayList<String> nazwyMiastArray = new ArrayList<>();
            for(Lotnisko lotnisko:lotniska)
            {
                if(lotnisko.getTyp().equals("Cywilne"))
                {
                    nazwyMiastArray.add(lotnisko.getNazwa());
                }
            }
            String[] nazwyMiast = nazwyMiastArray.toArray(new String[nazwyMiastArray.size()]);
            celIn = new JComboBox(nazwyMiast);
            tekst.add(celIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 9;
            JLabel start = new JLabel("Start:");
            tekst.add(start,gbc);

            gbc.gridx = 4;
            gbc.gridy = 9;
            startIn = new JComboBox(nazwyMiast);
            tekst.add(startIn,gbc);



            tekst.updateUI();
        }
        if(e.getSource() == buttonLotniskowiec)
        {
            tekst.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx= 0;
            gbc.gridy = 0;
            typPojazdu = new JLabel("Lotniskowiec:");
            tekst.add(typPojazdu,gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel nazwa = new JLabel("Nazwa:");
            tekst.add(nazwa,gbc);

            gbc.gridx =4;
            gbc.gridy=2;
            nazwaIn = new JTextField("");
            tekst.add(nazwaIn,gbc);

            gbc.gridx=0;
            gbc.gridy= 3;
            JLabel uzbrojenie = new JLabel("Uzbrojenie:");
            tekst.add(uzbrojenie,gbc);

            gbc.gridx = 4;
            gbc.gridy = 3;
            String[] uzbrojnieWybory = new String[]{"miny","torpedy"};
            uzbrojenieIn = new JComboBox(uzbrojnieWybory);
            tekst.add(uzbrojenieIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            JLabel predkosc = new JLabel("Prędkość:");
            tekst.add(predkosc,gbc);

            gbc.gridx =4;
            gbc.gridy = 4;
            Integer[] predkoscWybory = new Integer[3];
            int inc=1;
            for(int i=0;i<3;i++){
                predkoscWybory[i]= inc;
                inc++;
            }
            predkoscIn = new JComboBox<>(predkoscWybory);
            tekst.add(predkoscIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            JLabel cel = new JLabel("Cel:");
            tekst.add(cel,gbc);

            gbc.gridx = 4;
            gbc.gridy = 5;
            HashMap<String, MiastoPortowe> miasta =  mapaPanel.getMiastaPortowe();
            String[] nazwyMiast = miasta.keySet().toArray(new String[miasta.size()]);
            celIn = new JComboBox(nazwyMiast);
            tekst.add(celIn,gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            JLabel start = new JLabel("Start:");
            tekst.add(start,gbc);

            gbc.gridx = 4;
            gbc.gridy = 6;
            startIn = new JComboBox(nazwyMiast);
            tekst.add(startIn,gbc);


            tekst.updateUI();
        }
        }

    /***
     * metoda umożliwiająca sprawdzenie który przycisk z RadioPanel jest obecnie wciśniety
     * @return
     */
    public String getSelectedButtonText() {
        ButtonModel model = pojazd.getSelection();
        if (model == null) {
            return "";
        } else {
            return model.getActionCommand();
        }
    }

}
