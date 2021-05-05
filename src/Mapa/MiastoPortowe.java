package Mapa;

/***
 * Klasa miasta portowego
 */
public class MiastoPortowe {
    private int x;
    private int y;
    private String nazwa;

    /***
     * Konstruktor Miasta Portowego
     * @param x wspolrzedna
     * @param y wspolrzedna
     * @param nazwa
     */
    public MiastoPortowe(int x, int y, String nazwa) {
        this.x = x;
        this.y = y;
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
     * getter współrzędnej y
     * @return
     */
    public int getY() {
        return y;
    }

    /***
     * getter Nazwy
     * @return
     */
    public String getNazwa() {
        return nazwa;
    }

    /***
     * setter Nazwy
     * @param nazwa
     */
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
}
