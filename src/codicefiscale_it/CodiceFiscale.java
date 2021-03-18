/*
 * Copyright (C) 2021 Pietro P.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package codicefiscale_it;

import java.io.File;
import java.text.Normalizer;
import java.time.Year; 
import java.util.Scanner;

/**
 * Gestisce un'istanza di CodiceFiscale.
 * Il suo uso principale è salvare i dati di una persona per poi generarne il codice fiscale.
 * Alcuni degli algoritmi possono essere difficili da capire senza sapere come il codice fiscale viene costruito, leggere la <a href="https://it.wikipedia.org/wiki/Codice_fiscale">pagina Wikipedia</a> per più informazioni.
 * @author Pietro P.
 */
public class CodiceFiscale {
    
    private final String CONSONANTI = "BCDFGHJKLMNPQRSTVWXYZ";
    private final String VOCALI = "AEIOU";
    /**
     * 125 anni prima dell'introduzione del codice fiscale nel 1973. Prima di questa data non è sicuramente nata alcuna persona che abbia vissuto abbastanza per avere un codice fiscale
     */
    public static final int ANNOSOGLIA = 1848;
    /**
     * File che contiene i comuni italiani
     */
    public static final File ELENCOCOMUNI = new File("Comuni Italiani.csv");
    
    private String nome;
    private String cognome;
    private int anno;
    private int mese;
    private int giorno;
    private char sesso;
    private String comune;
    private String provincia;
    
    /**
     * Costrusce un oggetto Codice Fiscale con gli attributi inizializzati ai valori di default
     */
    public CodiceFiscale() {
        
        this.nome = "Mario";
        this.cognome = "Rossi";
        this.anno = 1848;
        this.mese = 1;
        this.giorno = 1;
        this.sesso = 'm';
        this.comune = "Roma";
        this.provincia = "RM";
    }
    
    /**
     * Costruisce un oggetto Codice Fiscale con i dati inseriti
     * @param cognome cognome della persona
     * @param nome nome della persona
     * @param anno anno di nascita
     * @param mese mese di nascita
     * @param giorno giorno di nascita
     * @param sesso sesso della persona
     * @param comune comune di appartenenza
     * @param provincia sesso di appartenenza
     * @throws Exception se uno dei dati dati in input non è valido (se uno dei setter fallisce)
     */
    public CodiceFiscale(String cognome, String nome, int anno, int mese, int giorno, char sesso, String comune, String provincia) throws Exception {
        
        setCognome(cognome);
        setNome(nome);
        setAnno(anno);
        setMese(mese);
        setGiorno(giorno);
        setSesso(sesso);
        setComune(comune);
        setProvincia(provincia);
    }

    /**
     * Memorizza il nome della persona dato in input
     * @param nome nome della persona
     * @throws Exception se il nome non è stato inserito o se è composto da spazi e basta 
     */
    public void setNome(String nome) throws Exception {
        
        nome = nome.trim();
        
        if(nome.length() < 1 || nome.isBlank()) {
            throw new Exception("Il nome non può essere vuoto o composto da soli spazi.");
        }
        this.nome = nome;
    }

    /**
     * Memorizza il cognome della persona dato in input
     * @param cognome cognome della persona
     * @throws Exception se il cognome non è stato inserito o se è composto da spazi e basta
     */
    public void setCognome(String cognome) throws Exception {
        
        cognome = cognome.trim();
        
        if(cognome.length() < 1 || cognome.isBlank()) {
            throw new Exception("Il cognome non può essere vuoto o composto da soli spazi.");
        }
        this.cognome = cognome;
    }

    /**
     * Memorizza l'anno di nascita della persona dato in input
     * @param anno anno di nascita
     * @throws Exception se l'anno inserito è maggiore dell'anno odierno o se è minore del 1848 (125 anni dall'introduzione del codice fiscale nel 1973,
     * così include tutte le persone che erano vive quando è stato introdotto)
     */
    public void setAnno(int anno) throws Exception {
        
        int annoCorrente = Year.now().getValue(); //contiene l'anno odierno
        
        if(anno < ANNOSOGLIA || anno > annoCorrente) {
            throw new Exception("L'anno deve essere compreso tra 1848 e l'anno odierno.");
        }
        this.anno = anno;
    }

    /**
     * Memorizza il mese di nascita della persona dato in input
     * @param mese mese di nascita
     * @throws Exception se il mese inserito non è un mese esistente
     */
    public void setMese(int mese) throws Exception {
        
        if(mese < 1 || mese > 12) {
            throw new Exception("Il mese inserito non esiste.");
        }
        this.mese = mese;
    }

    /**
     * Memorizza il giorno di nascita della persona dato in input
     * @param giorno giorno di nascita
     * @throws Exception se il giorno non è esistente basandosi sul mese e anno inseriti prima. Tiene conto degli anni bisestili
     */
    public void setGiorno(int giorno) throws Exception {
        
        boolean esistente = isGiornoEsistente(giorno, mese, anno); //controlla che il giorno sia effettivamente esistente
        
        if(giorno < 1 || giorno > 31 || !esistente) {
            throw new Exception("Il giorno inserito non esiste.");
        }
        this.giorno = giorno;
    }

    /**
     * Memorizza il sesso della persona dato in input
     * @param sesso sesso della persona
     * @throws Exception se il sesso dato in input non corrisponde ai 4 caratteri normalmente utilizzati per indicare i 2 sessi (f,F,m,M)
     */
    public void setSesso(char sesso) throws Exception {
        
        if(sesso != 'm' && sesso != 'f' && sesso != 'M' && sesso != 'F') {
            throw new Exception("Sesso inserito non valido.");
        }
        this.sesso = sesso;
    }

    /**
     * Memorizza il comune di appartenenza della persona dato in input
     * @param comune comune di appartenenza
     * @throws Exception se il comune non è stato inserito o se è composto da uno spazio e basta
     */
    public void setComune(String comune) throws Exception {
        
        comune = comune.trim();
        
        if(comune.length() < 1 || comune.isBlank()) {
            throw new Exception("Il comune non può essere vuoto o composto da soli spazi.");
        }
        this.comune = comune;
    }

    /**
     * Memorizza la provincia di appartenenza della persona data in input
     * @param provincia provincia di appartenenza. Deve essere composta da solo 2 caratteri
     * @throws Exception se la provincia non è stata inserita (o se è più lunga di 2 caratteri) o se è composta da uno spazio e basta
     */
    public void setProvincia(String provincia) throws Exception {
        
        provincia = provincia.trim();
        
        if(provincia.length() < 2 || provincia.length() > 2 || provincia.isBlank()) {
            throw new Exception("La provincia non può essere vuota (o più lunga di 2 caratteri) o composta da soli spazi.");
        }
        this.provincia = provincia.toUpperCase();
    }

    /**
     * Restituisce il nome della persona inserito in precedenza
     * @return il nome della persona 
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome della persona inserito in precedenza
     * @return il cognome della persona 
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce l'anno di nascita della persona inserito in precedenza
     * @return l'anno di nascita della persona 
     */
    public int getAnno() {
        return anno;
    }

    /**
     * Restituisce il mese di nascita della persona inserito in precedenza
     * @return il mese di nascita della persona 
     */
    public int getMese() {
        return mese;
    }

    /**
     * Restituisce il giorno di nascita della persona inserito in precedenza
     * @return il giorno di nascita della persona 
     */
    public int getGiorno() {
        return giorno;
    }

    /**
     * Restituisce il sesso della persona inserito in precedenza
     * @return il sesso della persona 
     */
    public char getSesso() {
        return sesso;
    }

    /**
     * Restituisce il il comune di appartenenza inserito in precedenza
     * @return il comune di appartenenza della persona 
     */
    public String getComune() {
        return comune;
    }

    /**
     * Restituisce la provincia di appartenenza inserita in precedenza
     * @return la provincia di appartenenza della persona
     */
    public String getProvincia() {
        return provincia;
    }
    
    /**
     * Riduce una stringa data in input così che contenga solo degli specifici caratteri, che vengono passati insieme alla stringa da ridurre
     * @param caratteri i caratteri su cui basarsi per costruire la stringa ridotta
     * @param stringa la stringa da ridurre
     * @return la stringa ridotta, con solo i caratteri contenuti in "caratteri"
     */
    private String stringaRidotta(String caratteri, String stringa) {
        
        stringa = rimouviAccentate(stringa); //sostituisce le lettere accentate con quelle non accentate
        
        String ridotta = ""; //parto da stringa vuota
        
        for (int i = 0; i < stringa.length(); i++) {
            if (caratteri.indexOf(stringa.charAt(i)) != -1)
                //aggiungo la consonante alla stringa ridotta
                ridotta = ridotta + stringa.charAt(i);
        }
        return ridotta;
    }
    
    /**
     * Genera i tre caratteri del cognome presnti nel codice fiscale
     * @param cognome cognome della persona
     * @return i tre caratteri del cognome presenti nel codice fiscale 
     */
    private String stringaCognome(String cognome) {
        
        String output;
        
        cognome = cognome.toUpperCase(); //assicura che la stringa sia tutta maiuscola (il codice fiscale è tutto in maiuscolo)
        
        //Genera una stringa basata sul cognome che ha tutte le consonanti del cognome, poi tutte le vocali del cognome e infine 2 "X"
        //Questo fa si che, prendendo i primi 3 caratteri di questa stringa si abbia in risultato i tre caratteri da usare nel codice fiscale
        output = stringaRidotta(CONSONANTI, cognome) + stringaRidotta(VOCALI, cognome) + "XX";
        
        return output.substring(0,3); //si recuperano le 3 letter del cognome da usare
    }
    
    /**
     * Genera i tre caratteri del nome presnti nel codice fiscale
     * @param nome nome della persona
     * @return i tre caratteri del nome presenti nel codice fiscale
     */
    private String stringaNome(String nome) {
        
        String output;
        String conspresenti; //contiene le consonanti presenti nel nome
        
        nome = nome.toUpperCase(); //assicura che la stringa sia tutta maiuscola (il codice fiscale è tutto in maiuscolo)
        
        conspresenti = stringaRidotta(CONSONANTI, nome); //assegnazione del valore alla variabile
        
        //Genera una stringa basata sul nome che ha tutte le consonanti del nome, poi tutte le vocali del nome e infine 2 "X"
        output = conspresenti + stringaRidotta(VOCALI, nome) + "XX";
        
        //il nome viene definito diversamente dal cognome, quindi bisogna fare queste operazioni in più
        if(conspresenti.length()>=4) {
            return output.substring(0,1)+output.substring(2,4); //se il cognome ha 4+ consonanti, vengono prese la prima, terza e quarta lettera dell'output
        }
        else {
            return output.substring(0,3); //se il cognome ha meno di 4 consonanti, si prensono le prime 3 lettere dell'ouput (come il cognome)
        }
    }
    
    /**
     * Genera i due numeri dell'anno presenti nel codice fiscale
     * @param anno anno di nascita
     * @return i due numeri dell'anno
     */
    private String stringaAnnoNascita(int anno) {
        
        String annostring = String.valueOf(anno); //stringa che contiene i caratteri di "anno"
        
        return annostring.substring(2,4); //nel codice fiscale, per l'anno si prendono gli ultimi due numeri (decine è unità)
    }
    
    /**
     * Genera la lettera usata per identificare il mese dato in input nel codice fiscale
     * @param mese mese di nascita
     * @return la lettera corrispondente al mese 
     */
    private char charMeseNascita(int mese) {
        
        String codicemesi ="ABCDEHLMPRST"; //la codifica del caratteri del mese del codice fiscale (gennaio: A, febbraio: B, ...)
        
        return codicemesi.charAt(mese-1); //si restituisce il carattere che corrisponde al mese
    }
    
    /**
     * Genera i due numeri nel codice fiscale che identificano giorno di nascita e sesso
     * @param giorno giorno di nascita
     * @param sesso sesso della persona
     * @return i due numeri corrispondenti a giorno di nascita e sesso
     */
    private String stringaGiornoNascitaSesso(int giorno, char sesso) {
        
        String output = null;
        
        if(sesso=='f' || sesso=='F') { //se il sesso è femminile, al giorno si aggiunge 40
            giorno=giorno+40;
            output = String.valueOf(giorno);
        }
        else if(giorno<10) {
            output = "0"+giorno; //se il giorno è minore di 10, si aggiunge uno 0, così che sono lo stesso 2 numeri
        }
        
        return output;
    }
    
    /**
     * Recupera da un file .csv il codice catastale del comune dato in input 
     * @param comune il comune passato in input
     * @param provincia la provincia passata in input (serve per distinguere comuni con nomi uguali)
     * @param file l'elenco dei comuni italiani
     * @return il codice catastale del comune
     * @throws Exception se il file da cui leggere non è stato trovato, o se la provincia/comune inseriti non sono trovati nell'elenco
     */
    private String codiceComune(String comune, String provincia, File file) throws Exception {
        
        comune = comune.toUpperCase(); //nel codice fiscale tutto è maiuscolo
        comune = rimouviAccentate(comune);
        
        final String DELIMITATORE = ","; //delimitatore dei valori nel file .csv
        Scanner input = new Scanner(file); //apertura di un file di testo
        String[] linea = null; //vettore dove viene memorizzata la linea che si sta analizzando del file
        boolean trovato=false; //verifica che il codice sia stato trovato
        
        while(input.hasNextLine()) { //il file viene letto fino alla fine
            
            linea = input.nextLine().split(DELIMITATORE); //legge una riga e la spezza nelle 3 parti: comune, provincia e codice catastale
            
            //se il comune e provincia della linea corrispondono a quelli passati in input, si esce dal ciclo
            if(linea[0].equals(comune) && linea[1].equals(provincia)) {
                trovato=true;
                break;
            }
        }
        
        if(!trovato) { //se il comune non è stato trovato si manda un errore
            throw new Exception("Comune/Provincia non trovati nell'elenco.");
        }
        
        return linea[2]; //si restituisce il codice catastale
    }
    
    /**
     * Genera il carattere di controllo seguendo un perciso algoritmo che verifica la validità del codice fiscale
     * @param codiceFiscale il resto del codice fiscale da cui ricavare il carattere
     * @return il carattere di controllo
     */
    private char carattereControllo(String codiceFiscale) {
        
        codiceFiscale = codiceFiscale.toUpperCase();
        
        //l'array da cui prendere il numero da utilizzare per ogni carattere dispari (quello pari è semplicemente numeri crescenti da 0 a 25)
        int[] charDispari = {1,0,5,7,9,13,15,17,19,21,2,4,18,20,11,3,6,8,12,14,16,10,22,25,24,23};
        char charControllo;
        int somma=0; //somma di tutti valori dei caratteri del codice fiscale
        
        for (int i=1; i<=codiceFiscale.length(); i++) { //si parte da 1 perché per l'operazione si prte da 1, non da 0 come normalmente in informatica
            
            if(i%2==0) { //caratteri in posizione pari
                if(Character.isDigit(codiceFiscale.charAt(i-1))) {
                    somma = somma + Character.digit(codiceFiscale.charAt(i-1), 10); //se il carattere è un numero, si prende direttamente il suo valore
                }
                else {
                    somma = somma + codiceFiscale.charAt(i-1)-'A'; //la sottrazione tra il carattere che si sta analizzando del codice fiscale e A restituisce il valore da sommare (A-A = 0, B-A =1, ...)
                }
            }
            else {
                if(Character.isDigit(codiceFiscale.charAt(i-1))) {
                    //se il carattere è un numero, si prende l'elemento dell'array dei caratter dispari all'indice definto dal carattere che si sta analizzando
                    somma = somma + charDispari[Character.digit(codiceFiscale.charAt(i-1), 10)]; 
                }
                else {
                    //la sottrazione tra il carattere che si sta analizzando del codice fiscale e A restituisce il valore dell'indice dell'elemento dell'array dei caratteri dispari da sommare
                    somma = somma + charDispari[codiceFiscale.charAt(i-1)-'A'];
                }
            }
        }
        
        //si prende il resto della divisione per 26 della somma totale e lo si usa per prendere il carattere di controllo sommando il resto ad "A" (il numero risultante è il codice Unicode del char controllo
        charControllo = (char)((somma % 26)+'A');
        
        return charControllo;
    }
    
    /**
     * Costruisce il codice fiscale utilizzando i dati inseriti
     * @return la stringa del codice fiscale
     * @throws Exception se codiceComune fallisce
     */
    public String costruisciCodiceFiscale() throws Exception {
        
        String output;
        
        output = stringaCognome(cognome) + stringaNome(nome) + stringaAnnoNascita(anno) + charMeseNascita(mese) + stringaGiornoNascitaSesso(giorno, sesso) + codiceComune(comune, provincia, ELENCOCOMUNI);
        output = output + carattereControllo(output); //aggiunta del carattere di controllo
        
        return output;
    }
    
    /**
     * Basandosi su mese a anno, verifica che il giorno passato sia effettivamente esistente
     * @param giorno il giorno da verificare
     * @param mese mese su cui basarsi
     * @param anno anno su cui basarsi
     * @return "true" se il giorno esiste, altrimenti "false"
     */
    private boolean isGiornoEsistente(int giorno, int mese, int anno) {
        
        boolean output=true;
        boolean isBisestile = Year.isLeap(anno); //controlla se l'anno è bisestile
        
        switch(mese) {
            //controlla che per i mesi da 30 giorni un numero più grande non sia stato inserito
            case 4:
                if(giorno > 30) {
                    output=false;
                }
                break;
            case 6:
                if(giorno > 30) {
                    output=false;
                }
                break;
            case 9:
                if(giorno > 30) {
                    output=false;
                }
                break;
            case 11:
                if(giorno > 30) {
                    output=false;
                }
                break;
            //per febbraio controlla se è bisestile o not: se è bisestile, allora il giorno deve essere minore di 29, altrimenti minore di 28
            case 2:
                if(isBisestile) {
                    if(giorno > 29) {
                        output=false;
                    }
                }
                else {
                    if(giorno > 28) {
                        output=false;
                    }
                }
                break;
        }
        
        return output;
    }
    
    /**
     * sostituisce le lettere accentate della stringa con le loro controparti non accentate
     * @param stringa la stringa da cui rimuovere gli accenti
     * @return la stringa data in input senza lettere accentate
     */
    private String rimouviAccentate(String stringa) {
        
        stringa = Normalizer.normalize(stringa, Normalizer.Form.NFD); //normalizza stringa
        stringa = stringa.replaceAll("[\\p{InCombiningDiacriticalMarks}]", ""); //rimuove gli accenti
        
        return stringa;
    }
}