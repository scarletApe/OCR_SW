/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.searchword;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author juanmartinez
 * @version 2.0
 */
public class CrossWord_Image {

	private char[][] crossword;
	private char[][] crosswordShower;
	private List<String> wordsFound;
	private List<String> wordsNotFound;
	private int position = 0;
	private static final Input in;
	private static final Output out;
	private static final BackwardOracleMatching bom;

	private BufferedImage img;
	private int cell_width = 30;
	private int cell_height = 30;
	private int paddingY = 25;
	private int paddingX = 15;
	private int rectPaddY = 3;
	private int rectPaddX = 8;

	private int alpha = 127; // 50% transparent
	private Color myColour;
	private Random random = new Random();

	static {
		in = new Input();
		out = new Output();
		bom = new BackwardOracleMatching();
	}

	public CrossWord_Image() {

	}

	public void showCrossWord() {
		out.showCrossWord(crosswordShower);
	}

	public void saveToText(String direccion) {
		out.saveToText(crosswordShower, direccion);
	}

	public void saveToHTML(String direccion, String filename) {
		out.saveToHTML2(crosswordShower, direccion, filename, wordsFound, wordsNotFound);
	}

	public void loadCrossWord(String filename) {
		setCrossWord(in.readFileCrossWord(filename));
	}

	public void setCrossWord(char[][] matrix) {
		// set the matrix
		this.crossword = matrix;

		// make a copy of it
		crosswordShower = new char[crossword.length][crossword[0].length];
		for (int i = 0; i < crossword.length; i++) {
			for (int j = 0; j < crossword[0].length; j++) {
				crosswordShower[i][j] = crossword[i][j];
			}
		}

		// initialize the lists
		wordsFound = new ArrayList<String>();
		wordsNotFound = new ArrayList<String>();

		// create the image that represents this cross word
		img = new BufferedImage(crossword[0].length * cell_width + 10, crossword.length * cell_height + 10,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());

		String fontFamily = "Menio";
		Font font_large = new Font(fontFamily, Font.PLAIN, 20);

		g.setFont(font_large);
		g.setColor(Color.BLACK);
		for (int i = 0; i < crossword.length; i++) {
			for (int j = 0; j < crossword[i].length; j++) {
				g.drawString("" + Character.toUpperCase(crossword[i][j]), (j * cell_width) + paddingX,
						(i * cell_height) + paddingY);
			}
		}

		// horizontal rectangle
		// myColour = new Color(random.nextInt(255), random.nextInt(255),
		// random.nextInt(255), alpha);
		// g.setColor(myColour);
		// g.fillRect(rectPaddX + (1 * cell_width), rectPaddY + (3 *
		// cell_height), cell_width * 4, cell_height);

		// myColour = new Color(random.nextInt(255), random.nextInt(255),
		// random.nextInt(255), alpha);
		// g.setColor(myColour);
		// g.fillRect(rectPaddX + (2 * cell_width), rectPaddY + (2 *
		// cell_height), cell_width * 1, cell_height);

		// vertical rectangle
		// myColour = new Color(random.nextInt(255), random.nextInt(255),
		// random.nextInt(255), alpha);
		// g.setColor(myColour);
		// g.fillRect((rectPaddX) + (3 * cell_width), (rectPaddY) + (0 *
		// cell_height), cell_width, cell_height * 4);

		// draw diagonal rectangle
		// myColour = new Color(random.nextInt(255), random.nextInt(255),
		// random.nextInt(255), alpha);
		// g.setColor(myColour); // x y
		//// Rectangle rect = new Rectangle(-6 + (2 * cell_width), 34 + (2 *
		// cell_height), cell_width * 6, cell_height);
		// Rectangle2D.Double rect = new Rectangle2D.Double(0 + (0 *
		// cell_width), 0 + (2 * cell_height), cell_width * 5.5, cell_height);
		// AffineTransform transform = new AffineTransform();
		// transform.rotate(Math.toRadians(45), rect.getX() + rect.width / 2,
		// rect.getY() + rect.height / 2);
		// Shape transformed = transform.createTransformedShape(rect);
		// g.fill(transformed);

		// myColour = new Color(random.nextInt(255), random.nextInt(255),
		// random.nextInt(255), alpha);
		// g.setColor(myColour); // x y
		//// Rectangle rect = new Rectangle(-6 + (2 * cell_width), 34 + (2 *
		// cell_height), cell_width * 6, cell_height);
		// Rectangle2D.Double rect = new Rectangle2D.Double( (1 * cell_width) +
		// 0 ,
		// (3 * cell_height)+ -10,
		// cell_width * 4.5, cell_height);
		// AffineTransform transform = new AffineTransform();
		// transform.rotate(Math.toRadians(45), rect.getX() + rect.width / 2,
		// rect.getY() + rect.height / 2);
		// Shape transformed = transform.createTransformedShape(rect);
		// g.fill(transformed);

		// draw inverse diagonal rectangle
		// myColour = new Color(random.nextInt(255), random.nextInt(255),
		// random.nextInt(255), alpha);
		// g.setColor(myColour);
		// rect = new Rectangle(-70 + (2 * cell_width), 34 + (0 * cell_height),
		// cell_width * 4, cell_height);
		// transform = new AffineTransform();
		// transform.rotate(Math.toRadians(315), rect.getX() + rect.width / 2,
		// rect.getY() + rect.height / 2);
		// transformed = transform.createTransformedShape(rect);
		// g.fill(transformed);

		g.dispose();
		showCrossWord();
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
				System.out.println("Found at position " + position + " on String line " + s.toString()
						+ ", size of word " + patron.length() + ", word found " + patron);

				Graphics2D g = (Graphics2D) img.getGraphics();
				myColour = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), alpha);
				g.setColor(myColour);
				g.fillRect(rectPaddX + (position * cell_width), rectPaddY + (i * cell_height),
						cell_width * patron.length(), cell_height);
				g.dispose();

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
				System.out.println("Found at position " + position + " on String line " + s.toString()
						+ ", size of word " + patron.length() + ", word found " + patron);

				Graphics2D g = (Graphics2D) img.getGraphics();
				myColour = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), alpha);
				g.setColor(myColour);
				g.fillRect((rectPaddX) + (j * cell_width), (rectPaddY) + (position * cell_height), cell_width,
						cell_height * patron.length());
				g.dispose();

				return true;
			}
		}
		return false;
	}

	double x;
	double y;
	double ty;

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
						+ ", size of word " + patron.length() + ",\n word found " + patron + " index1=" + index1
						+ " index2=" + index2);
				int size = patron.length() + position;
				for (int k = position; k < size; k++) {
					crosswordShower[index1 + k][index2 + k] = Character.toUpperCase(crossword[index1 + k][index2 + k]);
				}

				Graphics2D g = (Graphics2D) img.getGraphics();
				myColour = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), alpha);
				g.setColor(myColour);
				System.out.println("position= " + position + " line=" + i);
				// Rectangle rect = new Rectangle(-6 + (position * cell_width),
				// 34 + (i * cell_height), cell_width * (patron.length()+1),
				// cell_height);

				x = position;
				y = (index1 + position) + Math.ceil(patron.length() / 2);
				ty = 0;

				if (patron.length() % 2 == 0) {
					ty = (y * cell_height) - (cell_height / 2);
				} else {
					ty = (y * cell_height);
				}

				Rectangle2D.Double rect = new Rectangle2D.Double((x * cell_width), ty,
						cell_width * (patron.length() + .5), cell_height);

				AffineTransform transform = new AffineTransform();
				transform.rotate(Math.toRadians(45), rect.getX() + rect.width / 2, rect.getY() + rect.height / 2);
				Shape transformed = transform.createTransformedShape(rect);
				g.fill(transformed);
				g.dispose();

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
						+ ", size of word " + patron.length() + ",\n word found " + patron + " index1=" + index1
						+ " index2=" + index2);
				int size = patron.length() + position;
				for (int k = position; k < size; k++) {
					crosswordShower[index1 + k][index2 + k] = Character.toUpperCase(crossword[index1 + k][index2 + k]);
				}

				Graphics2D g = (Graphics2D) img.getGraphics();
				myColour = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), alpha);
				g.setColor(myColour);

				x = index2;
				y = (index1 + position) + Math.ceil(patron.length() / 2);
				ty = 0;

				if (patron.length() % 2 == 0) {
					ty = (y * cell_height) - (cell_height / 2);
				} else {
					ty = (y * cell_height);
				}

				Rectangle2D.Double rect = new Rectangle2D.Double((x * cell_width), ty,
						cell_width * (patron.length() + 0.5), cell_height);

				AffineTransform transform = new AffineTransform();
				transform.rotate(Math.toRadians(45), rect.getX() + rect.width / 2, rect.getY() + rect.height / 2);
				Shape transformed = transform.createTransformedShape(rect);
				g.fill(transformed);
				g.dispose();

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
				System.out
						.println("Found at position " + position + " on String line " + s.toString() + ", size of word "
								+ patron.length() + ",\n word found " + patron + " 1=" + index1 + " 2=" + index2);
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
				System.out
						.println("Found at position " + position + " on String line " + s.toString() + ", size of word "
								+ patron.length() + ",\n word found " + patron + " 1=" + index1 + " 2=" + index2);
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
		sb.append("#customers {\n" + "    font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\n"
		// + " width: 100%;\n"
		// + " radius: 5px;\n"
				+ "    border-collapse: collapse;\n" + "}\n" + "\n" + "#customers td, #customers th {\n"
				+ "    font-size: 1em;\n" + "    border: 1px solid darkblue;\n" + "    padding: 3px 7px 2px 7px;\n"
				+ "}");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>").append("<center><table id='customers' border=1 cellspacing=1 cellpadding=6>\n");
		for (int i = 0; i < crossword.length; i++) {
			sb.append("<tr>");
			for (int j = 0; j < crossword[0].length; j++) {
				if (Character.isUpperCase(crosswordShower[i][j])) {
					// sb.append("<td><font
					// color=\"red\">").append(crosswordShower[i][j]).append("</font></td>");
					sb.append("<td style=\"background-color:lightblue\">").append(crosswordShower[i][j])
							.append("</font></td>");
				} else {
					sb.append("<td>").append(crosswordShower[i][j]).append("</td>");
				}
			}
			sb.append("</tr>\n");
		}
		sb.append("</table></center><br/></body></html>");
		return sb.toString();
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

	public BufferedImage getImage() {
		return img;
	}
}

//
