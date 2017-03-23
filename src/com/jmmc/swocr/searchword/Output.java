/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.searchword;

import java.util.*;
import java.io.*;

/**
 *
 * @author juanmartinez
 */
public class Output {

    public void showCrossWord(char c[][]) {
        System.out.println();
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                System.out.print(" " + c[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public void saveToText(char c[][], String direccion) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(direccion , false);
            pw = new PrintWriter(fichero);
            for (int i = 0; i < c.length; i++) {
                for (int j = 0; j < c[0].length; j++) {
                    pw.print(c[i][j] + " ");
                }
                pw.print("\n");
            }

        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            try {

                if (null == fichero) {
                } else {
                    fichero.close();
                }
            } catch (Exception e2) {
//                e2.printStackTrace();
            }
        }
    }

    public void saveToHTML(char c[][], String direccion, String filename, List<String> wordsFound, List<String> wordsNotFound) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(direccion, false);
            pw = new PrintWriter(fichero);

            pw.println("<html>\n<head><title>" + direccion + "</title></head>");
            pw.println("<body><n1><center><font size=100 color=\"black\">" + filename + " Word Search Solved</font></center></n1><br/><hr>");
            pw.println("<center><table border=5 cellspacing=5 cellpadding=10>");

            for (int i = 0; i < c.length; i++) {
                pw.print("<tr>");
                for (int j = 0; j < c[0].length; j++) {
                    if (Character.isUpperCase(c[i][j])) {
                        pw.print("<td><font color=\"red\">" + c[i][j] + "</font></td>");
                    } else {
                        pw.print("<td>" + c[i][j] + "</td>");
                    }
                }
                pw.print("</tr>\n");
            }
            pw.println("</table></center><br/>");
            pw.println("<n1><center><font size=60 color=\"black\">The Following Words Were Found</font></center></n1><br/><hr>");
            for (int i = 0; i < wordsFound.size(); i++) {
                pw.println(wordsFound.get(i) + "<br/>");
            }
            pw.println("<n1><center><font size=60 color=\"black\">The Following Words Were Not Found</font></center></n1><br/><hr>");
            for (int i = 0; i < wordsNotFound.size(); i++) {
                pw.println(wordsNotFound.get(i) + "<br/>");
            }
            pw.println("</body></html>");

        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            try {

                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
//                e2.printStackTrace();
            }
        }
    }
}
