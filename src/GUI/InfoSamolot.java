package GUI;

import Lotnisko.Lotnisko;
import Mapa.TrasaSamolot;
import Vehicle.Samolot;
import Vehicle.SamolotPasazerski;
import Vehicle.SamolotWojskowy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/***
 * Klasa Okna informacyjnego Samolotu
 */
public class InfoSamolot extends JFrame implements ActionListener {

    JButton usun;
    JButton awaria;
    JButton zmienTrase;
    JComboBox nowyCelIn;
    Samolot samolot;
    MapaPanel mapaPanel;

    /***
     * Konstruktor Klasy okna informacyjnego Samolotu
     * @param samolot
     * @param mapaPanel
     */
    public InfoSamolot(Samolot samolot,MapaPanel mapaPanel){
        super("INFO");
        this.samolot =samolot;
        this.mapaPanel = mapaPanel;
        this.setResizable(false);
        this.setSize(550,400);

        JPanel nazwaPanel =new JPanel();
        JLabel nazwa = new JLabel("Nazwa Samolotu: "+ samolot.getNazwa());
        nazwaPanel.add(nazwa);

        JPanel info = new JPanel(new GridBagLayout());
        JLabel predkosc = new JLabel("Predkosc Samolotu: "+Integer.toString(samolot.getPredkosc()));
        JLabel cel = new JLabel("Cel Samolotu: " + samolot.getCel());
        JLabel nastepneLotnisko = new JLabel("Następne Lotnisko: " + samolot.getNastepneLotnisko());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        info.add(predkosc,gbc);
        gbc.gridy =1;
        info.add(cel,gbc);
        gbc.gridy = 2;
        info.add(nastepneLotnisko,gbc);
        if(samolot instanceof SamolotPasazerski)
        {
            SamolotPasazerski samolotPasazerski = (SamolotPasazerski) samolot;
            JLabel firma = new JLabel("Firma:" + samolotPasazerski.getFirma());
            JLabel liczbaPasazerow = new JLabel("Liczba pasazerów: " + samolotPasazerski.getLicz_pasazer());
            gbc.gridy = 3;
            info.add(firma,gbc);
            gbc.gridy = 4;
            info.add(liczbaPasazerow,gbc);
        }
        else if(samolot instanceof SamolotWojskowy)
        {
            SamolotWojskowy samolotWojskowy = (SamolotWojskowy) samolot;
            JLabel uzbrojenie = new JLabel("Uzbrojenie: " + samolotWojskowy.getTyp_uzbrojenia());
            gbc.gridy = 3;
            info.add(uzbrojenie,gbc);
        }
        gbc.gridy = 7;
        JLabel nowyCel = new JLabel("Nowy cel:");
        info.add(nowyCel,gbc);
        gbc.gridx = 4;
        HashMap<String, Lotnisko> lotniskaMap =  samolot.getDostepneLotniska();
        Collection<Lotnisko> wartosci = lotniskaMap.values();
        ArrayList<Lotnisko> lotniska = new ArrayList<>(wartosci);
        ArrayList<String> nazwyMiastArray = new ArrayList<>();
        for(Lotnisko lotnisko:lotniska)
        {
            if(lotnisko.getTyp().equals("Wojskowe"))
            {
                nazwyMiastArray.add(lotnisko.getNazwa());
            }
        }
        String[] nazwyMiast = nazwyMiastArray.toArray(new String[nazwyMiastArray.size()]);
        nowyCelIn = new JComboBox(nazwyMiast);
        info.add(nowyCelIn,gbc);

        JPanel przyciski = new JPanel();
        usun = new JButton("usuń");
        usun.addActionListener(this);
        zmienTrase = new JButton("zmień trase");
        zmienTrase.addActionListener(this);
        awaria = new JButton("awaria");
        awaria.addActionListener(this);
        przyciski.add(usun);
        przyciski.add(zmienTrase);
        przyciski.add(awaria);


        this.setResizable(false);
        this.setLayout(new BorderLayout(0,0));
        this.add(BorderLayout.NORTH,nazwaPanel);
        this.add(BorderLayout.CENTER,info);
        this.add(BorderLayout.SOUTH,przyciski);
    }

    /***
     * Obsługa przycisków w oknie Informacyjnym Samolotu
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == awaria)
        {
            samolot.setStanPodrozy("usterka");
        }
        else if(e.getSource() == zmienTrase)
        {
            if(!(samolot.getNastepneLotnisko().equals(nowyCelIn.getSelectedItem()))) {
                samolot.setCel((String) nowyCelIn.getSelectedItem());
                ArrayList<String> trasaLotniska = samolot.planujTrasaLotniska(samolot.getNastepneLotnisko());
                samolot.setTrasaLotniska(trasaLotniska);
                ArrayList<TrasaSamolot> trasy = samolot.planujTrase();
                if (trasy.indexOf(samolot.getTrasaAktualna())==0)
                {
                    Collections.reverse(trasy);
                }
                samolot.setTrasa(trasy);
                if (trasy.size() == 1)
                {
                    samolot.setTrasaAktualna(trasy.get(0));
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Podaj inny nowy cel, podany to następne lotnisko","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getSource() == usun)
        {
            for(Samolot innySamolot: mapaPanel.getSamoloty())
            {
                innySamolot.getPozostaleSamoloty().remove(samolot);
            }
            samolot.stop();
            mapaPanel.getSamoloty().remove(samolot);
        }
    }
}
