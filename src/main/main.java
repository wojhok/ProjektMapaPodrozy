package main;

import GUI.GUI_Map;
import GUI.MapaPanel;
import GUI.PanelKontrolny;


public class main {
    public static void main(String[] args) throws Exception {
        GUI_Map mapa = new GUI_Map();
        mapa.setVisible(true);
        MapaPanel mapaPanel = mapa.getPanel();
       PanelKontrolny panel=new PanelKontrolny(mapaPanel);
        panel.setVisible(true);
    }
}
