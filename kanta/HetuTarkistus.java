package kanta;


/**
 * Luokka henkil�tunnuksen tarkistamiseksi
 * @author Anette Karhu
 * @version 2.7.2016
 *
 */
public class HetuTarkistus  {
    /** Hetuun kelpaavat tarkistusmerkit j�rjestyksess� */
    //                                            0123456789012345678901234567890
    public static final String TARKISTUSMERKIT = "0123456789ABCDEFHJKLMNPRSTUVWXY";

    
    /**
     * Palauttaa mik� olisi hetun tarkistumerkki. Tuotava parametrin�
     * laillista muotoa oleva hetu, josta mahdollisesti tarkistumerkki 
     * puuttuu.
     * @param hetu tutkittava hetu
     * @return hetun tarkistusmerkki
     * @example
     * <pre name="test">
     *    hetunTarkistusMerkki("121212-222")    === 'N';
     *    hetunTarkistusMerkki("121212-222S")   === 'N';
     *    hetunTarkistusMerkki("121212-222N")   === 'N';
     *    hetunTarkistusMerkki("121212-231Y")   === 'Y';
     *    hetunTarkistusMerkki("311212-2317")   === '7';
     *    hetunTarkistusMerkki("311212-2317XY") === '7'; // vaikka on liikaa merkkej�
     *    hetunTarkistusMerkki("999999-9999XY") === 'F'; // vaikka on pvm v��rin
     *    hetunTarkistusMerkki("12121A-222S")   === 'N'; #THROWS NumberFormatException
     *    hetunTarkistusMerkki("12121A-22")     === 'N'; #THROWS StringIndexOutOfBoundsException
     *    hetunTarkistusMerkki("121")           === 'N'; #THROWS StringIndexOutOfBoundsException
     * </pre>
     */
    public static char hetunTarkistusMerkki(String hetu) {
        String pvm = hetu.substring(0,6);
        String yksilo = hetu.substring(7,10);
        long luku = Long.parseLong(pvm+yksilo);
        int jakojaannos = (int)(luku % 31L);
        return TARKISTUSMERKIT.charAt(jakojaannos);
    }
    
   
    /**
     * Arvotaan satunnainen kokonaisluku v�lille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yl�raja
     * @return satunnainen luku v�lilt� [ala,yla]
     */
    public static int rand(int ala, int yla) {
      double n = (yla-ala)*Math.random() + ala;
      return (int)Math.round(n);
    }

    
    /**
     * Arvotaan satunnainen henkil�tunnus, joka t�ytt�� hetun ehdot    
     * @return satunnainen laillinen henkil�tunnus
     */ // 010203-123
    public static String arvoHetu() {
        String apuhetu = String.format("%02d",rand(1,28))   +
        String.format("%02d",rand(1,12))   +
        String.format("%02d",rand(1,99))   + "-" +
        String.format("%03d",rand(1,1000));
        return apuhetu + hetunTarkistusMerkki(apuhetu);        
    }
    
}