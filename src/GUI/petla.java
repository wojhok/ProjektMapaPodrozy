package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Klasa Petli
 * służąca odświeżaniu obrazu
 */
public class petla implements ActionListener {
    private MapaPanel panel;
    public petla(MapaPanel panel)
    {
        this.panel = panel;
    }

    /***
     * Odświeżanie obrazu
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.panel.zapetl();
    }
}
