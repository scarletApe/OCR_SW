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
package com.jmmc.swocr.image_loaders;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 * @author juanmartinez
 */
public class PngManager implements ManejadorImagen {

	/**
	 * Al momento no se puede guardar una imagen a este formato.
	 */
	@Override
	public void guardarArregloEnImagen(String archivo, byte[][] arreglo) {
	}

	@Override
	public byte[][] cargarArregloDeImagen(URI archivo) {
		return null;
	}

	@Override
	public byte[] flattenArray(byte[][] arreglo) {
		return null;
	}

	/**
	 * Este metodo toma un uri que apunte a una imagen png y la carga en un
	 * arreglo de bytes, donde un pixel blanco es cero y un pixel negro es uno.
	 * Este metodo regresa un null si la imagen es de color.
	 *
	 * @param archivo
	 * @return
	 */
	@Override
	public byte[] cargarArregloFlatDeImagen(URI archivo) {
		byte[] data = null;

		try {
			BufferedImage img = ImageIO.read(archivo.toURL());

			PixelGrabber grabber = new PixelGrabber(img, 0, 0, -1, -1, false);

			if (grabber.grabPixels()) {

				if (isGreyscaleImage(grabber)) {
//					System.out.println("is grey");
					data = (byte[]) grabber.getPixels();

					for (int i = 0; i < data.length; i++) {
						data[i] = (data[i] == 0) ? (byte) 1 : (byte) 0;
					}

				} else {
					System.out.println("its rgb");
				}
			}
		} catch (InterruptedException e1) {
			Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, e1);
		} catch (MalformedURLException ex) {
			Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		return data;
	}

	public static final boolean isGreyscaleImage(PixelGrabber pg) {
		return pg.getPixels() instanceof byte[];
	}

}
