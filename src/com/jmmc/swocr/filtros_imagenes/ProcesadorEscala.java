
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author juanmartinez
 */
public class ProcesadorEscala extends ProcesadorImagen {

	private final Dimension dimension;

	public ProcesadorEscala(Dimension di) {
		dimension = di;
	}

	@Override
	public BufferedImage procesar(BufferedImage imagen) {
		BufferedImage imagenProcesada = new BufferedImage(dimension.width, dimension.height,
				BufferedImage.TYPE_BYTE_BINARY);
		pintar(imagenProcesada, Color.white);

		// tamaños en x de cada celda a escalar
		int celdasX[] = new int[(int) dimension.getWidth()];
		int celdaX = (int) (imagen.getWidth() / dimension.getWidth());
		int extraX = (int) (imagen.getWidth() % dimension.getWidth());
		for (int i = 0; i < celdasX.length; i++) {
			celdasX[i] = celdaX;

		}
		if (celdaX == 0) {
			// se asigna el tamaño extra completo al centro
			celdasX[celdasX.length / 2] = extraX;
		} else {
			// se distribuye el tamaño extra
			for (int i = 0, e = extraX; e > 0; i++, e--) {
				celdasX[i] += 1;
			}
		}

		// tamano en y de cada celda a escalar
		int celdasY[] = new int[(int) dimension.getHeight()];
		int celdaY = (int) (imagen.getHeight() / dimension.getHeight());
		int extraY = (int) (imagen.getHeight() % dimension.getHeight());

		for (int i = 0; i < celdasY.length; i++) {
			celdasY[i] = celdaY;

		}
		if (celdaY == 0) {
			// se asigna el tamañño extra completo al centro
			celdasY[celdasY.length / 2] = extraY;
		} else {
			// se distribuye el tamaño extra
			for (int i = 0, extra = extraY; extra > 0; i++, extra--) {
				celdasY[i] += 1;

			}
		}
		for (int x = 0, xi = 0; x < celdasX.length; xi += celdasX[x++]) {
			for (int y = 0, yi = 0; y < celdasY.length; yi += celdasY[y++]) {
				if (hayPixel(imagen, xi, yi, celdasX[x], celdasY[y])) {
					imagenProcesada.setRGB(x, y, NEGRO);
				}
			}
		}
		return imagenProcesada;
	}

	private boolean hayPixel(BufferedImage imagen, int x, int y, int celdaX, int celdaY) {
		for (int xc = 0; xc < celdaX; xc++) {
			for (int yc = 0; yc < celdaY; yc++) {
				if (imagen.getRGB(xc + x, yc + y) != BLANCO) {
					return true;
				}
			}
		}
		return false;
	}

}
