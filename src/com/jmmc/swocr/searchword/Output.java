/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.searchword;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * Esta clase permite exportar la sopa de letras a un archivo de texto plano o
 * un archivo html, tambien permite mostrarlo a la consola.
 * 
 * @author juanmartinez
 */
public class Output {

	/**
	 * Mostrar la matriz a la consola.
	 * 
	 * @param c
	 */
	public void showCrossWord(char c[][]) {
		System.out.println();
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[0].length; j++) {
				System.out.print(" " + c[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Almacenar el contenido de la matriz a un archivo de texto plano.
	 * 
	 * @param c
	 * @param direccion
	 */
	public void saveToText(char c[][], String direccion) {
		FileWriter fichero = null;
		PrintWriter pw;
		try {
			fichero = new FileWriter(direccion, false);
			pw = new PrintWriter(fichero);
			for (int i = 0; i < c.length; i++) {
				for (int j = 0; j < c[0].length; j++) {
					pw.print(c[i][j] + " ");
				}
				pw.print("\n");
			}

		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			try {

				if (null == fichero) {
				} else {
					fichero.close();
				}
			} catch (Exception e2) {
				// e2.printStackTrace();
			}
		}
	}

	/**
	 * Este metodo permite almacenar el contenido de la sopa de letras a un
	 * archivo html. Utiliza css y bootstrap.
	 * 
	 * @param c
	 * @param direccion
	 * @param filename
	 * @param wordsFound
	 * @param wordsNotFound
	 */
	public void saveToHTML2(char c[][], String direccion, String filename, List<String> wordsFound,
			List<String> wordsNotFound) {

		System.out.println("direccion=" + direccion + " filename=" + filename);

		StringBuilder sb = new StringBuilder();

		sb.append("<!DOCTYPE html><html lang=\"en\"><head> \n");
		sb.append("<title>").append(filename).append("</title> \n");
		sb.append("<meta charset=\"utf-8\"> \n");
		sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> \n");

		// sb.append("<meta name="author" content="JMMC">");

		sb.append(
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\"> \n");
		sb.append("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js\"></script> \n");
		sb.append("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script> \n");
		sb.append("<style> body{line-height: 1.25;} body > .container { padding: 60px 15px 0;} \n");
		sb.append(
				".navbar {font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif; background-color: #369ab7; border: 1px solid transparent;} \n");
		sb.append(".navbar-inverse .navbar-brand{ color: #ffffff; font-size: 16px;} \n");
		sb.append(
				"#customers { font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif; border-collapse: collapse;} \n");
		sb.append(
				"#customers td, #customers th {font-size: 12px; padding: 3px 7px 2px 7px; border-radius: 25px;} </style> </head> <body> \n");
		sb.append(
				"<nav class=\"navbar navbar-inverse navbar-fixed-top\"> <div class=\"container\"> <div class=\"navbar-header\"> \n");
		sb.append("<a class=\"navbar-brand\" href=\"#\">").append(filename)
				.append(" Word Search</a></div></div></nav> \n");
		sb.append("<div class=\"container\"> <section class=\"main row well\"> <article class=\"container-fluid\"> \n");
		sb.append("<center><table id='customers'> \n");
		sb.append("<tr> <th><center>Word Search Solved</center></th> \n");
		sb.append("<th><center>Words Found</center></th> \n");
		sb.append("<th><center>Words Not Found</center></th></tr> \n");
		sb.append(
				"<tr> <td> <table id='customers' cellspacing=1 cellpadding=6 style=\"border: 1px solid #369ab7\"> \n");

		// Fill the table
		for (int i = 0; i < c.length; i++) {
			sb.append("<tr>");
			for (int j = 0; j < c[0].length; j++) {
				if (Character.isUpperCase(c[i][j])) {
					sb.append("<td style=\"background-color:lightblue\">" + c[i][j] + "</td>");
				} else {
					sb.append("<td>" + c[i][j] + "</td>");
				}
			}
			sb.append("</tr>\n");
		}

		sb.append("</table> </td> \n");
		sb.append("<td> <ul style=\"list-style-type:circle\"> \n");

		// fill the found word list
		for (String s : wordsFound) {
			sb.append("<li>").append(s).append("</li> \n");
		}

		sb.append("</ul> </td> \n");
		sb.append("<td> <ul style=\"list-style-type:circle\"> \n");

		// fill the not found list
		for (String s : wordsNotFound) {
			sb.append("<li>").append(s).append("</li> \n");
		}

		sb.append("</ul> </td> </tr> </table> </center> </article> </section> </div> </body> </html> \n");

		// escribirlo a un archivo
		FileWriter fichero = null;
		PrintWriter pw;
		try {
			fichero = new FileWriter(direccion, false);
			pw = new PrintWriter(fichero);

			pw.print(sb.toString());
		} catch (Exception e) {
			System.out.println("Error escribiendo archivo: " + e.getMessage());
		} finally {
			try {
				if (null != fichero) {
					fichero.close();
				}
			} catch (Exception e2) {
				// e2.printStackTrace();
			}
		}
	}
}
