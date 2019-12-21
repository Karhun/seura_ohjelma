package seura;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
 * @author Anette Karhu
 * @version 2.7.2016
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;


    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
     * k�ytett�v� viesti
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}