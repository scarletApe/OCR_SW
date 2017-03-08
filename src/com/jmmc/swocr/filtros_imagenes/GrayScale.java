
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

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

public class GrayScale {

	static final int WHITE = 0x00FFFFFF;
	static final int BLACK = 0x00000000;

	static final int BWHITE = 0xFF;
	static final int BBLACK = 0x00;

	private BufferedImage grayscale(BufferedImage image) {
		/*
		 * Grayscale: the java way
		 */
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
			return image;
		}
		int imageType = BufferedImage.TYPE_BYTE_GRAY;

		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage newImg = new BufferedImage(w, h, imageType);
		ColorSpace srcSpace = image.getColorModel().getColorSpace();
		ColorSpace newSpace = newImg.getColorModel().getColorSpace();
		ColorConvertOp convert = new ColorConvertOp(srcSpace, newSpace, null);
		convert.filter(image, newImg);
		return newImg;
	}

	public BufferedImage grayscale2(BufferedImage src) {
		/*
		 * Grayscale: by hand
		 */
		int w = src.getWidth();
		int h = src.getHeight();
		int size = w * h;

		WritableRaster swr = src.getRaster();
		DataBuffer sdb = swr.getDataBuffer();

		BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster dwr = dest.getRaster();
		DataBuffer ddb = dwr.getDataBuffer();

		int rgb, r, g, b, gr;

		for (int i = 0; i < size; i++) {
			rgb = sdb.getElem(i);
			r = (rgb >>> 16) & 0xFF;
			g = (rgb >>> 8) & 0xFF;
			b = rgb & 0xFF;
			gr = ((r * 77 + g * 151 + b * 28) >>> 8) & 0xFF; // 0.3 x red + 0.59
																// x green +
																// 0.11 x blue
			ddb.setElem(i, gr);
		}
		return dest;
	}

	public BufferedImage filter(BufferedImage src) {
		return grayscale(src);
	}
}
// </pre></body></html>
