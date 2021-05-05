package GUI;
import Vehicle.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Klasa okna informacyjnego Statku
 */
public class InfoStatek extends JFrame implements ActionListener {
    private Statek statek;
    private JButton usun;
    private MapaPanel mapaPanel;
    public InfoStatek(Statek statek, MapaPanel mapaPanel){
        super("INFO");
        this.mapaPanel = mapaPanel;
        this.statek = statek;
        this.setResizable(false);
        this.setSize(550,400);

        JPanel nazwaPanel =new JPanel();
        JLabel nazwa = new JLabel("Nazwa Statku: "+ statek.getNazwa());
        nazwaPanel.add(nazwa);

        JPanel info = new JPanel(new GridBagLayout());
        JLabel predkosc = new JLabel("Predkosc Statku: "+Integer.toString(statek.getMaks_predkosc()));
        JLabel cel = new JLabel("Cel Statku: " + statek.getCel());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        info.add(predkosc,gbc);
        gbc.gridy =1;
        info.add(cel,gbc);

        if(statek instanceof StatekPasazerski)
        {
            StatekPasazerski statekPasazerski = (StatekPasazerski) statek;
            JLabel firma = new JLabel("Firma:" + statekPasazerski.getFirma());
            JLabel liczbaPasazerow = new JLabel("Liczba pasazerów: " + statekPasazerski.getLicz_pasazer());
            gbc.gridy = 2;
            info.add(firma,gbc);
            gbc.gridy = 3;
            info.add(liczbaPasazerow,gbc);
        }
        else if(statek instanceof Lotniskowiec)
        {
            Lotniskowiec lotniskowiec = (Lotniskowiec) statek;
            JLabel uzbrojenie = new JLabel("Uzbrojenie: " + lotniskowiec.getUzbrojenie());
            gbc.gridy = 2;
            info.add(uzbrojenie,gbc);
        }

        JPanel przyciski = new JPanel();
        usun = new JButton("usuń");
        usun.addActionListener(this);
        przyciski.add(usun);


        this.setResizable(false);
        this.setLayout(new BorderLayout(0,0));
        this.add(BorderLayout.NORTH,nazwaPanel);
        this.add(BorderLayout.CENTER,info);
        this.add(BorderLayout.SOUTH,przyciski);
    }

    /***
     * Obsułga przycisków okna informayjnego Statku
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == usun)
        {
            statek.stop();
            for(Statek innyStatek: mapaPanel.getStatki())
            {
                innyStatek.getPozostaleStatki().remove(statek);
            }
            mapaPanel.getStatki().remove(statek);
        }
    }
}
