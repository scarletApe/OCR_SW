//<editor-fold defaultstate="collapsed" desc="licence">
/*
 * Copyright (C) 2016 juanmartinez
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
//</editor-fold>
package com.jmmc.swocr.filtros_imagenes;

import java.awt.image.BufferedImage;

public class Binarization {

	static final int WHITE = 0x00FFFFFF;
	static final int BLACK = 0x00000000;

	static final int BWHITE = 0xFF;
	static final int BBLACK = 0x00;
	private int rshift = 0, gshift = 0, bshift = 0;

	public Binarization() {
	}

	public Binarization(int threshold) {
		rshift += 64 - (threshold * 128) / 100;
		gshift += 64 - (threshold * 128) / 100;
		bshift += 64 - (threshold * 128) / 100;
	}

	public BufferedImage filter(BufferedImage image) {
		switch (image.getType()) {
		case BufferedImage.TYPE_BYTE_BINARY:
			return image;
		case BufferedImage.TYPE_BYTE_GRAY:
			return filterGray(image);
		default:
			return filterRGB(image);
		}
	}

	public BufferedImage filterGray(BufferedImage image) {
		int c;
		int w = image.getWidth();
		int h = image.getHeight();

		Histogram hs = new Histogram(256);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				c = image.getRGB(x, y) & 0x000000FF;
				hs.write(c);
			}
		}
		int t = hs.getThreshold();

		System.out.println("Threshold=[" + t + "]");

		BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				c = image.getRGB(x, y) & 0x000000FF;
				if (c > t) {
					dest.setRGB(x, y, -1);
				} else {
					dest.setRGB(x, y, 0);
				}
			}
		}
		return dest;
	}

	public BufferedImage filterRGB(BufferedImage image) {
		int c;
		int w = image.getWidth();
		int h = image.getHeight();

		Histogram rhs = new Histogram(256);
		Histogram ghs = new Histogram(256);
		Histogram bhs = new Histogram(256);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				c = image.getRGB(x, y);
				rhs.write((c >> 16) & 0x000000FF);
				ghs.write((c >> 8) & 0x000000FF);
				bhs.write(c & 0x000000FF);
			}
		}
		int rt = rhs.getThreshold() + rshift;
		int gt = ghs.getThreshold() + gshift;
		int bt = bhs.getThreshold() + bshift;

		System.out.println("Threshold[r,g,b]=[" + rt + "," + gt + "," + bt + "]");

		BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

		int r, g, b;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				c = image.getRGB(x, y);
				r = (c >> 16) & 0x000000FF;
				g = (c >> 8) & 0x000000FF;
				b = c & 0x000000FF;

				if ((r > rt) || (g > gt) || (b > bt)) {
					dest.setRGB(x, y, -1);
				} else {
					dest.setRGB(x, y, 0);
				}
			}
		}
		return dest;
	}
}

/*
 * [1] Shunji Mori; Optical Character Recognition (1999); John Wiley & Sons 4.2
 * Thresholding selection based on global discriminant analysis; pp.112
 * 
 * [2] Ioannis Pavlidis, Vassilios Morellas, Pete Roeber; Programming Cameras
 * and Pan-Tilts with DirectX and Java Morgan-Kaufmann Publishers; ISBN
 * 1-55860-756-0 pp.69
 * 
 * Original Paper:
 * 
 * [3] Otsu,N.A Threshold Selection Method from Gray-Level Histograms. IEEE
 * Transactions on Systems,Man, and Cybernetics,1979,vol.9,pp.62-66
 */
// </pre></body></html>
