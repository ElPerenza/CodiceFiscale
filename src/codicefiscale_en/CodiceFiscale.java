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
package codicefiscale_en;

import java.io.File;
import java.text.Normalizer;
import java.time.Year; 
import java.util.Scanner;

/**
 * Manages a Codice Fiscale (fiscal code) instance.
 * It's main purpose is to store data about a person to then generate that person's fiscal code.
 * Some of the algoriths can be confusing to understand without knowing how the fiscal code is built, check the <a href="https://it.wikipedia.org/wiki/Codice_fiscale">Wikipedia page</a> on it for more info.
 * @author Pietro P.
 */
public class CodiceFiscale {
    
    private final String CONSONANTI = "BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz";
    private final String VOCALI = "AEIOUaeiou";
    /**
     * 125 years before the introduction of the fiscal code in 1973. No one that got a fiscal code was born before this year
     */
    public static final int ANNOSOGLIA = 1848;
    /**
     * File that contains all italian municipalities
     */
    public static final File ELENCOCOMUNI = new File("Comuni Italiani.csv");
    
    private String nome; //name
    private String cognome; //surname
    private int anno; //birth year
    private int mese; //birth month
    private int giorno; //birth day
    private char sesso; //sex
    private String comune; //municipality
    private String provincia; //province
    
    /**
     * Constructs a CoficeFiscale instance with all of the attributes initialized at default values
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
     * Constructs a CodiceFiscale instance with the data given in input
     * @param cognome surname
     * @param nome name
     * @param anno birth year
     * @param mese birth month
     * @param giorno birth day
     * @param sesso sex
     * @param comune municipality
     * @param provincia province code
     * @throws Exception if one of the data is invalid (so, if one of the setters fails)
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
     * Saves the person's name given in input
     * @param nome name
     * @throws Exception if the name is blank
     */
    public void setNome(String nome) throws Exception {
        
        nome = nome.trim();
        
        if(nome.length() < 1 || nome.isBlank()) {
            throw new Exception("Il nome non può essere vuoto o composto da soli spazi.");
        }
        this.nome = nome;
    }

    /**
     * Saves the person's surname given in input
     * @param cognome surname
     * @throws Exception if the surname is blank
     */
    public void setCognome(String cognome) throws Exception {
        
        cognome = cognome.trim();
        
        if(cognome.length() < 1 || cognome.isBlank()) {
            throw new Exception("Il cognome non può essere vuoto o composto da soli spazi.");
        }
        this.cognome = cognome;
    }

    /**
     * Saves the person's birth year given in input
     * @param anno birth year
     * @throws Exception if the year comes before 1848 or after the current year
     */
    public void setAnno(int anno) throws Exception {
        
        int annoCorrente = Year.now().getValue(); //current year
        
        if(anno < ANNOSOGLIA || anno > annoCorrente) {
            throw new Exception("L'anno deve essere compreso tra 1848 e l'anno odierno.");
        }
        this.anno = anno;
    }

    /**
     * Saves the person's birth month given in input
     * @param mese birth month
     * @throws Exception if the month does not exist
     */
    public void setMese(int mese) throws Exception {
        
        if(mese < 1 || mese > 12) {
            throw new Exception("Il mese inserito non esiste.");
        }
        this.mese = mese;
    }

    /**
     * Saves the person's birth day given in input
     * @param giorno birth day
     * @throws Exception if the day does not exist (using the proviously inserted year and month)
     */
    public void setGiorno(int giorno) throws Exception {
        
        if(giorno < 1 || giorno > 31) {
            throw new Exception("Il giorno inserito non esiste.");
        }
        this.giorno = giorno;
    }

    /**
     * Saves the person's sex given in input
     * @param sesso sex
     * @throws Exception if the sex char does not correspond to the usual 4 characters used to identify sex (m, M, f , F)
     */
    public void setSesso(char sesso) throws Exception {
        
        if(sesso != 'm' && sesso != 'f' && sesso != 'M' && sesso != 'F') {
            throw new Exception("Sesso inserito non valido.");
        }
        this.sesso = sesso;
    }

    /**
     * Saves the person's municipality given in input
     * @param comune municipality
     * @throws Exception if the municipality is blank
     */
    public void setComune(String comune) throws Exception {
        
        comune = comune.trim();
        
        if(comune.length() < 1 || comune.isBlank()) {
            throw new Exception("Il comune non può essere vuoto o composto da soli spazi.");
        }
        this.comune = comune;
    }

    /**
     * Saves the person's province code given in input
     * @param provincia priovince code, must be formed of only 2 characters
     * @throws Exception if the province code is blank or is not formed by 2 characters
     */
    public void setProvincia(String provincia) throws Exception {
        
        provincia = provincia.trim();
        
        if(provincia.length() < 2 || provincia.length() > 2 || provincia.isBlank()) {
            throw new Exception("La provincia non può essere vuota (o più lunga di 2 caratteri) o composta da soli spazi.");
        }
        this.provincia = provincia.toUpperCase(); //all of the province 2 character codes are written all uppercase
    }

    /**
     * Returns the saved person's name
     * @return name
     */
    public String getNome() {
        return nome;
    }

    /**
     * Returns the saved person's surname
     * @return surname
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Returns the saved person's birth year
     * @return birth year
     */
    public int getAnno() {
        return anno;
    }

    /**
     * Returns the saved person's birth month
     * @return birth month
     */
    public int getMese() {
        return mese;
    }

    /**
     * Returns the saved person's birth day
     * @return birth day
     */
    public int getGiorno() {
        return giorno;
    }

    /**
     * Returns the saved person's sex
     * @return sex
     */
    public char getSesso() {
        return sesso;
    }

    /**
     * Returns the saved person's municipality
     * @return municipality
     */
    public String getComune() {
        return comune;
    }

    /**
     * Returns the saved person's province code
     * @return province code
     */
    public String getProvincia() {
        return provincia;
    }
    
    /**
     * Reduces a string to only a specific set of characters (the characters to use are passed in input with the string to reduce)
     * @param caratteri the only characters that can be in the reduced string: all of the others will be removed
     * @param stringa string to reduce
     * @return reduced string, with all of the characters not containted in "caratteri" deleted
     */
    private String stringaRidotta(String caratteri, String stringa) {
        
        stringa = rimouviAccentate(stringa); //removes all accents
        
        //creates a regular expression that negates a character array formed by "caratteri"
        //that's then used to remove every character of the input string that doesn't appear in the regex
        String regex = "[^(?!"+caratteri+")]";
        String ridotta = stringa.replaceAll(regex, "");
        
        return ridotta;
    }
    
    /**
     * Generates the Fiscal Code 3-character code for the surname
     * @param cognome surname
     * @return the surname 3-character code
     */
    private String stringaCognome(String cognome) {
        
        String output;
        
        cognome = cognome.toUpperCase(); //everything in the fiscal code is uppercase
        
        //Generates a string with all of the consonants present in the surname + the same but for vowels + 2 "X"
        //Taking the first 3 characters of this string gives us the 3-char code of the surname.
        output = stringaRidotta(CONSONANTI, cognome) + stringaRidotta(VOCALI, cognome) + "XX";
        
        return output.substring(0,3); //only the first 3 letters are taken (that form the 3-char code)
    }
    
    /**
     * Generates the Fiscal Code 3-character code for the name
     * @param gnome name
     * @return the name 3-character code
     */
    private String stringaNome(String nome) {
        
        String output;
        String conspresenti; //contains only the consonants present in the name
        
        nome = nome.toUpperCase();
        
        conspresenti = stringaRidotta(CONSONANTI, nome); //Generates a string with all of the consonants present in the name
        
        //Generates a string with the previously created consonant string + the same but for vowels + 2 "X"
        output = conspresenti + stringaRidotta(VOCALI, nome) + "XX";
        
        //since the name's 3-char code is generated slightly differently from the surname's one, another operation is needed
        if(conspresenti.length()>=4) {
            return output.substring(0,1)+output.substring(2,4); //If the surname has 4+ consonants, the first, thrid and fouth letters of the string get taken
        }
        else {
            return output.substring(0,3); //If the surname has less than 4 consonants, the same rule as the surname applies
        }
    }
    
    /**
     * Generates the 2 numbers corresponding to the year in the fiscal code
     * @param anno birth year
     * @return the 3 numbers of the year
     */
    private String stringaAnnoNascita(int anno) {
        
        String annostring = String.valueOf(anno); //transform int to String
        
        return annostring.substring(2,4); //the 2 numbers of the year in the fiscal code are the last two numbers of the year (tens and units)
    }
    
    /**
     * Generates the letter used to refer to the birth month in the fiscal code
     * @param mese birth month
     * @return letter corresponding to the birth month 
     */
    private char charMeseNascita(int mese) {
        
        String codicemesi ="ABCDEHLMPRST"; //the list of characters corresponding to each month (janauary: A, february: B, ...)
        
        return codicemesi.charAt(mese-1); //returns the corresponding character of the month
    }
    
    /**
     * Generates the 2 numbers that identify day of birth and sex in the fiscal code
     * @param giorno birth day
     * @param sesso sex
     * @return two numbers corresponding to birth day and sex
     */
    private String stringaGiornoNascitaSesso(int giorno, char sesso) {
        
        String output = String.valueOf(giorno);
        
        if(sesso=='f' || sesso=='F') { //if sex is female, 40 is added to the value of the day
            giorno=giorno+40;
            output = String.valueOf(giorno);
        }
        else if(giorno<10) {
            output = "0"+giorno; //if the day is smaller than 10, a 0 is added at the font to keep it 2 characters long
        }
        
        return output;
    }
    
    /**
     * Finds the code of the municipality given in input by searching in a .csv file containging all italian municipalities 
     * @param comune municipality to compare
     * @param provincia province to compare
     * @param file file with all of the italian municipalities
     * @return the correct municipality code
     * @throws Exception if the file wasn't found, or if the municipality and province weren't found in the file
     */
    private String codiceComune(String comune, String provincia, File file) throws Exception {
        
        comune = comune.toUpperCase();
        comune = rimouviAccentate(comune);
        
        final String DELIMITATORE = ","; //value delimitator in .csv files
        Scanner input = new Scanner(file);
        String[] linea = null; //contains the current line of the .csv file, split in parts
        boolean trovato=false; //verifies that the municipality code was found
        
        while(input.hasNextLine()) { //reads all of the lines of the file
            
            linea = input.nextLine().split(DELIMITATORE); //splits the line in Municipality, Province and Municipality code
            
            //if municipality and province of the current line coincide with the ones passed in input, the right line has been found and the program can exit the cycle
            if(linea[0].equals(comune) && linea[1].equals(provincia)) {
                trovato=true;
                break;
            }
        }
        
        if(!trovato) { //if the municipality and province weren't found, an error is thrown
            throw new Exception("Comune/Provincia non trovati nell'elenco.");
        }
        
        return linea[2]; //returns the municipality code
    }
    
    /**
     * Generates a control character using a very specific algorithm to validate the fiscal code: if this character is incorrect, then the code is a false
     * @param codiceFiscale the rest of the fiscal code string from which the control character will be generated
     * @return the control character
     */
    private char carattereControllo(String codiceFiscale) {
        
        codiceFiscale = codiceFiscale.toUpperCase();
        
        //array that contains the numerical values to use for characters in an odd position --> A/0 = 1, B/1 = 0, .... (even position is just 0 to 25)
        int[] charDispari = {1,0,5,7,9,13,15,17,19,21,2,4,18,20,11,3,6,8,12,14,16,10,22,25,24,23};
        char charControllo;
        int somma=0; //total of all the character values
        
        for (int i=1; i<=codiceFiscale.length(); i++) { //it starts from 1 because in the algorithm (since it was first done by humans and not machines), first char is 1, not 0
            
            if(i%2==0) { //characters in even position
                if(Character.isDigit(codiceFiscale.charAt(i-1))) { //i-1 --> because it started from 1 instead of 0
                    somma = somma + Character.digit(codiceFiscale.charAt(i-1), 10); //if the character is a number, his value is used
                }
                else {
                    somma = somma + codiceFiscale.charAt(i-1)-'A'; //subtraction between current char and "A" returns the value to add to the total (A-A = 0, B-A = 1, ...)
                }
            }
            else { //characters in odd position
                if(Character.isDigit(codiceFiscale.charAt(i-1))) {
                    //if the character is a number, the value of the number corresponds to the index of the array where the value to add to the total is
                    somma = somma + charDispari[Character.digit(codiceFiscale.charAt(i-1), 10)]; 
                }
                else {
                    //subtraction between current char and "A" corresponds to the index of the array where the value to add to the total is
                    somma = somma + charDispari[codiceFiscale.charAt(i-1)-'A'];
                }
            }
        }
        
        //the remainder of the division of the total by 26 + "A" gives the Unicode number for the control character (remainder 0 --> A, remainder 1 --> B, ...)
        charControllo = (char)((somma % 26)+'A');
        
        return charControllo;
    }
    
    /**
     * Generates the fiscal code using the input data
     * @return String with fiscal code
     * @throws Exception if the generator for the municipality code fails (codiceComune)
     */
    public String costruisciCodiceFiscale() throws Exception {
        
        String output;
        
        //checks if the inserted date is actually exists
        if(!isGiornoEsistente(giorno, mese, anno)) {
            throw new Exception("Data inserita non esistente.");
        }
        
        output = stringaCognome(cognome) + stringaNome(nome) + stringaAnnoNascita(anno) + charMeseNascita(mese) + stringaGiornoNascitaSesso(giorno, sesso) + codiceComune(comune, provincia, ELENCOCOMUNI);
        output = output + carattereControllo(output); //adds the control char
        
        return output;
    }
    
    /**
     * Using month and year as reference, checks if the day given in input exisits
     * @param giorno the day to check
     * @param mese month for reference
     * @param anno year for reference
     * @return true if the day exists, otherwise false
     */
    private boolean isGiornoEsistente(int giorno, int mese, int anno) {
        
        boolean output=true;
        boolean isBisestile = Year.isLeap(anno); //checks if the year is a leap year
        
        switch(mese) {
            //checks that for 30-day months the day is smaller (or equal, of course) than 30
            case 4:
            case 6:
            case 9:
            case 11:
                if(giorno > 30) {
                    output=false;
                }
                break;
            //for february, first checks if it's a leap year or not: if it's a leap year, the day must be smaller than 29, if it's not the day must be smaller than 28
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
     * Substitutes accented letters with their non-accented counterpart
     * @param stringa the string from which accents have to be removed
     * @return the string without accents
     */
    private String rimouviAccentate(String stringa) {
        
        stringa = Normalizer.normalize(stringa, Normalizer.Form.NFD); //normalize string
        stringa = stringa.replaceAll("\\p{M}", ""); //remove the accents
        
        return stringa;
    }
}