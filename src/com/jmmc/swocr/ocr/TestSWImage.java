package com.jmmc.swocr.ocr;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jmmc.swocr.searchword.CrossWord_Image;

public class TestSWImage {

	public static void main(String args[]) {
		new TestSWImage().run();
	}

	public void run() {
		char[][] array = { { 'a', 'b', 'c', 'd', 'e' }, { 'f', 'g', 'h', 'i', 'j' }, { 'k', 'l', 'm', 'n', 'o' },
				{ 'p', 'q', 'r', 's', 't' }, { 'u', 'v', 'w', 'x', 'y' } };
		CrossWord_Image cw = new CrossWord_Image();
		cw.setCrossWord(array);

		// horizontals
		// cw.findWord("bcd");
		// cw.findWord("srqp");

		// verticals
		// cw.findWord("joty");
		// cw.findWord("pkfa");

		// diagonal
		// cw.findWord("bhn");
		// cw.findWord("jd");

		cw.findWord("agmsy");
		// cw.findWord("vp");

		BufferedImage img = cw.getImage();

		JFrame f = new JFrame();
		JPanel p = new JPanel() {

			private static final long serialVersionUID = -5743119812429043516L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(img, 10, 10, null);
			}
		};
		f.add(p);
		f.pack();
		f.setSize(180, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
