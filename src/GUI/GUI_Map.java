package GUI;

import javax.swing.*;

/***
 * Klasa GUI JFrame okna Mapy
 */
public class GUI_Map extends JFrame {
    private MapaPanel panel;

    /***
     * Konstruktor klasy GUI_Map
     */
    public GUI_Map() {
        super("Mapa");
        panel = new MapaPanel();
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
    }

    /***
     * getter Panlu Mapy
     * @return
     */
    public MapaPanel getPanel() {
        return panel;
    }

}
