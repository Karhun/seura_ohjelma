package kanta;

import javax.swing.SwingConstants;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Anette Karhu
 * @version 28.7.2016
 */
public class IntKentta extends PerusKentta{
	private int arvo;
	
	/**
	 * Alustetaan kenttä kysymyksellä
	 * @param kysymys joka näytetään kenttää kysyessä
	 */
	public IntKentta(String kysymys) {
		super(kysymys);
	}
	
	/**
	 * @return kentän arvo kokonaislukuna
	 */
	public int getValue() {
		return arvo;
	}

	/**
	 * Asetetaan kentän arvo kokonaislukuna
	 * @param value asetettava kokonaislukuarvo
	 */
	public void setValue(int value) {
		arvo = value;
	}
	
	/**
	 * @return merkkijonona kokonaisluvun
	 */
	@Override
	public String toString(){
		return "" + arvo;
	}
	
	/**
	 * Palauttaa kentän tiedot vertailtavana merkkijonona
	 * @return vertailtava merkkijono kentästä
	 */
	@Override
	public String getAvain() {
		return Mjonot.fmt(arvo, 10);
	}
	

	/**
	 * Asetetaan kentän arvo merkkijonosta. Jos
	 * jono kunnollinen, palautetaan null. Jos ei ole
	 * kunnollinen int-syöte, palautetaan virheilmoitus.
	 * Tällöin kentän alkuperäinen arvo jää voimaan
	 */
	@Override
	public String aseta(String jono) {
		StringBuffer sb = new StringBuffer(jono);
		try {
			this.arvo = Mjonot.erota(sb, ' ', 0);
			return null;
		} catch (NumberFormatException e) {
			return "Ei kokonaisluku (" + jono + ")";
		}
	}
	
	/**
	 * @return vaakasuuntainen sijainti kentälle
	 */
	@Override
	public int getSijainti() {
		return SwingConstants.RIGHT;
	}
	
	/**
	 * @return syväkopio oliosta
	 */
	@Override
	public IntKentta clone() throws CloneNotSupportedException {
		return (IntKentta) super.clone();
	}

	@Override
	public int getSijainti(int k) {
		return 0;
	}
	
}
