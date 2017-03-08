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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * {@link ProcesadorImagen} para obtener el esqueleto de una imagen
 *
 * @author Gabriel Roberto Constenla
 */
public class ProcesadorEsqueleto extends ProcesadorImagen {

	@Override
	public BufferedImage procesar(BufferedImage imagen) {
		BufferedImage imagenProcesada = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
		imagenProcesada.setData(imagen.getData());

		boolean paso1ConCambios;
		boolean paso2ConCambios;

		ArrayList<Point> puntosAeliminar;

		do {
			// paso 1
			puntosAeliminar = new ArrayList<>();
			for (int x = 0; x < imagenProcesada.getWidth(); x++) {
				for (int y = 0; y < imagenProcesada.getHeight(); y++) {
					if (getP1(imagenProcesada, x, y) == 1 && vecinos(imagenProcesada, x, y) >= 2
							&& vecinos(imagenProcesada, x, y) <= 6 && transiciones(imagenProcesada, x, y) == 1
							&& (getP2(imagenProcesada, x, y) * getP4(imagenProcesada, x, y)
									* getP6(imagenProcesada, x, y)) == 0
							&& (getP4(imagenProcesada, x, y) * getP6(imagenProcesada, x, y)
									* getP8(imagenProcesada, x, y)) == 0) {
						puntosAeliminar.add(new Point(x, y));

					}
				}
			}
			paso1ConCambios = !puntosAeliminar.isEmpty();

			eliminarPuntos(imagenProcesada, puntosAeliminar);

			// paso 2
			puntosAeliminar = new ArrayList<>();
			for (int x = 0; x < imagenProcesada.getWidth(); x++) {
				for (int y = 0; y < imagenProcesada.getHeight(); y++) {
					if (getP1(imagenProcesada, x, y) == 1 && vecinos(imagenProcesada, x, y) >= 2
							&& vecinos(imagenProcesada, x, y) <= 6 && transiciones(imagenProcesada, x, y) == 1
							&& (getP2(imagenProcesada, x, y) * getP4(imagenProcesada, x, y)
									* getP8(imagenProcesada, x, y)) == 0
							&& (getP2(imagenProcesada, x, y) * getP6(imagenProcesada, x, y)
									* getP8(imagenProcesada, x, y)) == 0) {
						puntosAeliminar.add(new Point(x, y));

					}
				}
			}

			paso2ConCambios = !puntosAeliminar.isEmpty();
			eliminarPuntos(imagenProcesada, puntosAeliminar);
		} while (paso1ConCambios || paso2ConCambios);

		return imagenProcesada;
	}

	private int transiciones(BufferedImage esqueleto, int x, int y) {
		int transiciones = 0;
		int vecinos[] = { getP2(esqueleto, x, y), getP3(esqueleto, x, y), getP4(esqueleto, x, y),
				getP5(esqueleto, x, y), getP6(esqueleto, x, y), getP7(esqueleto, x, y), getP8(esqueleto, x, y),
				getP9(esqueleto, x, y), getP2(esqueleto, x, y)

		};

		for (int i = 0; i < vecinos.length - 1; i++) {
			if (vecinos[i] == 0 && vecinos[i + 1] == 1) {
				transiciones++;
			}
		}
		return transiciones;
	}

	private int vecinos(BufferedImage imagen, int x, int y) {
		return getP2(imagen, x, y) + getP3(imagen, x, y) + getP4(imagen, x, y) + getP5(imagen, x, y)
				+ getP6(imagen, x, y) + getP7(imagen, x, y) + getP8(imagen, x, y) + getP8(imagen, x, y);
	}

	private int getP1(BufferedImage imagen, int x, int y) {
		return imagen.getRGB(x, y) == BLANCO ? 0 : 1;
	}

	private int getP2(BufferedImage imagen, int x, int y) {
		return y + 1 > imagen.getHeight() - 1 ? 0 : (imagen.getRGB(x, y + 1) == BLANCO ? 0 : 1);
	}

	private int getP3(BufferedImage imagen, int x, int y) {
		return x + 1 > imagen.getWidth() - 1 || y + 1 > imagen.getHeight() - 1 ? 0
				: (imagen.getRGB(x + 1, y + 1) == BLANCO ? 0 : 1);
	}

	private int getP4(BufferedImage imagen, int x, int y) {
		return x + 1 > imagen.getWidth() - 1 ? 0 : (imagen.getRGB(x + 1, y) == BLANCO ? 0 : 1);
	}

	private int getP5(BufferedImage imagen, int x, int y) {
		return x + 1 > imagen.getWidth() - 1 || y - 1 < 0 ? 0 : (imagen.getRGB(x + 1, y - 1) == BLANCO ? 0 : 1);
	}

	private int getP6(BufferedImage imagen, int x, int y) {
		return y - 1 < 0 ? 0 : (imagen.getRGB(x, y - 1) == BLANCO ? 0 : 1);
	}

	private int getP7(BufferedImage imagen, int x, int y) {
		return x - 1 < 0 || y - 1 < 0 ? 0 : (imagen.getRGB(x - 1, y - 1) == BLANCO ? 0 : 1);
	}

	private int getP8(BufferedImage imagen, int x, int y) {
		return x - 1 < 0 ? 0 : (imagen.getRGB(x - 1, y) == BLANCO ? 0 : 1);
	}

	private int getP9(BufferedImage imagen, int x, int y) {
		return x - 1 < 0 || y + 1 > imagen.getHeight() - 1 ? 0 : (imagen.getRGB(x - 1, y + 1) == BLANCO ? 0 : 1);
	}

	private void eliminarPuntos(BufferedImage imagen, ArrayList<Point> puntosAeliminar) {
		for (Point p : puntosAeliminar) {
			imagen.setRGB(p.x, p.y, BLANCO);
		}
	}
}
