/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.searchword;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanmartinez
 * @version 2.0
 */
public class CrossWord {

    private char[][] crossword;
    private char[][] crosswordShower;
    private List<String> wordsFound;
    private List<String> wordsNotFound;
    private int position = 0;
    private static final Input in;
    private static final Output out;
    private static final BackwardOracleMatching bom;

    static {
        in = new Input();
        out = new Output();
        bom = new BackwardOracleMatching();
    }

    public CrossWord() {
        wordsFound = new ArrayList<String>();
        wordsNotFound = new ArrayList<String>();
    }

    public void showCrossWord() {
        out.showCrossWord(crosswordShower);
    }

    public void saveToText(String direccion) {
        out.saveToText(crosswordShower, direccion);
    }

    public void saveToHTML(String direccion) {
        out.saveToHTML(crosswordShower, direccion, direccion, wordsFound, wordsNotFound);
    }

    public void loadCrossWord(String filename) {
        crossword = in.readFileCrossWord(filename);
        crosswordShower = new char[crossword.length][crossword[0].length];
        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[0].length; j++) {
                crosswordShower[i][j] = crossword[i][j];
            }
        }
    }

    public boolean findWord(String word) {
        if (searchWord(word)) {
            wordsFound.add(word);
            return true;
        } else {
            wordsNotFound.add(word);
            return false;
        }
    }

    public boolean searchWord(String word) {

        if (findHorizontal(word, false)) {
            return true;
        }
        if (findHorizontal(word, true)) {
            return true;
        }
        if (findVertical(word, false)) {
            return true;
        }
        if (findVertical(word, true)) {
            return true;
        }
        if (findDiagonal(word, false)) {
            return true;
        }
        if (findDiagonal(word, true)) {
            return true;
        }
        if (findDiagonalInverse(word, false)) {
            return true;
        }
        if (findDiagonalInverse(word, true)) {
            return true;
        }
        return false;
    }

    public boolean findHorizontal(String patron, boolean reverse) {
        StringBuilder s;
        if (reverse) {
            patron = reverseAString(patron);
        }
        for (int i = 0; i < crossword.length; i++) {
            s = new StringBuilder();
            for (int j = 0; j < crossword[0].length; j++) {
                s.append(crossword[i][j]);
            }
            position = bom.findWord(s.toString(), patron);
            if (position > -1) {
                for (int j = position; j < patron.length() + position; j++) {
                    crosswordShower[i][j] = Character.toUpperCase(crossword[i][j]);
                }
                System.out.println("Found at position " + position + " on String line " + s.toString() + ", size of word " + patron.length() + ", word found " + patron);
                return true;
            }
        }
        return false;
    }

    public boolean findVertical(String patron, boolean reverse) {
        StringBuilder s;
        if (reverse) {
            patron = reverseAString(patron);
        }
        for (int j = 0; j < crossword[0].length; j++) {
            s = new StringBuilder();
            for (int i = 0; i < crossword.length; i++) {
                s.append(crossword[i][j]);
            }
            position = bom.findWord(s.toString(), patron);
            if (position > -1) {
                for (int i = position; i < patron.length() + position; i++) {
                    crosswordShower[i][j] = Character.toUpperCase(crossword[i][j]);
                }
                System.out.println("Found at position " + position + " on String line " + s.toString() + ", size of word " + patron.length() + ", word found " + patron);
                return true;
            }
        }
        return false;
    }

    public boolean findDiagonal(String patron, boolean reverse) {
        StringBuilder s;
        int index1 = 0, index2 = 0;
        boolean inicio;
        if (reverse) {
            patron = reverseAString(patron);
        }
        for (int i = 0; i < crossword.length; i++) {
            inicio = true;
            s = new StringBuilder();
            for (int r = i, c = 0; r < crossword.length && c < crossword[0].length; r++, c++) {
                if (inicio) {
                    index1 = r;
                    index2 = c;
                    inicio = false;
                }
                s.append(crossword[r][c]);
            }
            position = bom.findWord(s.toString(), patron);
            if (position > -1) {
                System.out.println("Found at position " + position + " on String line " + s.toString()
                        + ", size of word " + patron.length() + ",\n word found " + patron + " 1=" + index1 + " 2=" + index2);
                int size = patron.length() + position;
                for (int k = position; k < size; k++) {
                    crosswordShower[index1 + k][index2 + k] = Character.toUpperCase(crossword[index1 + k][index2 + k]);
                }
                return true;
            }
        }
        for (int j = 1; j < crossword[0].length; j++) {
            inicio = true;
            s = new StringBuilder();
            for (int r = 0, c = j; r < crossword.length && c < crossword[0].length; r++, c++) {
                if (inicio) {
                    index1 = r;
                    index2 = c;
                    inicio = false;
                }
                s.append(crossword[r][c]);
            }
            position = bom.findWord(s.toString(), patron);
            if (position > -1) {
                System.out.println("Found at position " + position + " on String line " + s.toString()
                        + ", size of word " + patron.length() + ",\n word found " + patron + " 1=" + index1 + " 2=" + index2);
                int size = patron.length() + position;
                for (int k = position; k < size; k++) {
                    crosswordShower[index1 + k][index2 + k] = Character.toUpperCase(crossword[index1 + k][index2 + k]);
                }
                return true;
            }
        }
        return false;
    }

    public boolean findDiagonalInverse(String patron, boolean reverse) {
        StringBuilder s;
        int index1 = 0, index2 = 0;
        boolean inicio;
        if (reverse) {
            patron = reverseAString(patron);
        }
        for (int i = crossword.length - 1; i >= 0; i--) {
            inicio = true;
            s = new StringBuilder();
            for (int r = i, c = 0; r >= 0 && c < crossword[0].length; r--, c++) {
                if (inicio) {
                    index1 = r;
                    index2 = c;
                    inicio = false;
                }
                s.append(crossword[r][c]);
            }
            position = bom.findWord(s.toString(), patron);
            if (position > -1) {
                System.out.println("Found at position " + position + " on String line " + s.toString()
                        + ", size of word " + patron.length() + ",\n word found " + patron + " 1=" + index1 + " 2=" + index2);
                int size = patron.length() + position;
                for (int k = position; k < size; k++) {
                    crosswordShower[index1 - k][index2 + k] = Character.toUpperCase(crossword[index1 - k][index2 + k]);
                }
                return true;
            }
        }
        for (int j = 1; j < crossword[0].length; j++) {
            inicio = true;
            s = new StringBuilder();
            for (int r = crossword.length - 1, c = j; r >= 0 && c < crossword[0].length; r--, c++) {
                if (inicio) {
                    index1 = r;
                    index2 = c;
                    inicio = false;
                }
                s.append(crossword[r][c]);
            }
            position = bom.findWord(s.toString(), patron);
            if (position > -1) {
                System.out.println("Found at position " + position + " on String line " + s.toString()
                        + ", size of word " + patron.length() + ",\n word found " + patron + " 1=" + index1 + " 2=" + index2);
                int size = patron.length() + position;
                for (int k = position; k < size; k++) {
                    crosswordShower[index1 - k][index2 + k] = Character.toUpperCase(crossword[index1 - k][index2 + k]);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head><style>\n");
        sb.append("#customers {\n"
                + "    font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\n"
//                + "    width: 100%;\n"
//                + "    radius: 5px;\n"
                + "    border-collapse: collapse;\n"
                + "}\n"
                + "\n"
                + "#customers td, #customers th {\n"
                + "    font-size: 1em;\n"
                + "    border: 1px solid darkblue;\n"
                + "    padding: 3px 7px 2px 7px;\n"
                + "}");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>").append("<center><table id='customers' border=1 cellspacing=1 cellpadding=6>\n");
        for (int i = 0; i < crossword.length; i++) {
            sb.append("<tr>");
            for (int j = 0; j < crossword[0].length; j++) {
                if (Character.isUpperCase(crosswordShower[i][j])) {
//                    sb.append("<td><font color=\"red\">").append(crosswordShower[i][j]).append("</font></td>");
                    sb.append("<td style=\"background-color:lightblue\">").append(crosswordShower[i][j]).append("</font></td>");
                } else {
                    sb.append("<td>").append(crosswordShower[i][j]).append("</td>");
                }
            }
            sb.append("</tr>\n");
        }
        sb.append("</table></center><br/></body></html>");
        return sb.toString();
    }

    public void setCrossWord(char[][] matrix) {
        this.crossword = matrix;
        crosswordShower = new char[crossword.length][crossword[0].length];
        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[0].length; j++) {
                crosswordShower[i][j] = crossword[i][j];
            }
        }
    }
    
    public String reverseAString(String word) {
        int numwrds = word.length();
        char[] wordArray = new char[numwrds];

        for (int i = 0; i < word.length(); i++) {
            wordArray[numwrds - 1] = word.charAt(i);
            numwrds--;
        }
        String wordRv = new String(wordArray);
        return wordRv;
    }
}

// 
