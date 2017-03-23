package com.jmmc.swocr.ocr;

import java.io.File;

import com.jmmc.swocr.image_loaders.ManejadorImagen;
import com.jmmc.swocr.image_loaders.PngManager;
import com.jmmc.swocr.mlp.FuncionSigmoidea;
import com.jmmc.swocr.mlp.RNSerializer;
import com.jmmc.swocr.mlp.RedNeuronal;

public class TrainMany {

	public static void main(String args[]) {
		new TrainMany().run();
	}

	private static final double[][] PATRONES_SALIDA = new double[][] {
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 } };

	public void run() {
		int num_nets = 10;
		int epochs = 1800;
		
		int num_in = 48;
		int num_hidden = 36;
		int num_out = 26;

		File saveFolder = createDirectory(
				System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "saveFolder");

		// load the images into memory+++++++++++++++++++++++++++
		System.out.println("Cargando imagenes a memoria++++++++++++++++");
		File inputDir = new File("/Users/juanmartinez/Desktop/letter_samples3/");
		File[] listOfFiles = inputDir.listFiles();
		// inizializar los arreglos de datos de entrada y salida
		byte[][] entrada = new byte[listOfFiles.length][];
		double[][] salida = new double[listOfFiles.length][];

		ManejadorImagen manejador = new PngManager();
		try {
			char caracter;
			int index;
			for (int i = 0; i < listOfFiles.length; i++) {

				File file = listOfFiles[i];
				System.out.println("\tFile:" + file);

				// check if it's not a hidden file
				if (file.getName().charAt(0) == '.') {
					System.out.println(file.getName());
					System.exit(0);
					return;
				}

				entrada[i] = manejador.cargarArregloFlatDeImagen(file.toURI());
				// System.out.println("\tArreglo Tamaño:" + entrada[i].length);

				caracter = file.getName().charAt(0);
//				System.out.println("\nCaracter=" + caracter);

				index = caracter - 65;
				salida[i] = PATRONES_SALIDA[index];

				System.out.println("");
			}
		} catch (Exception e) {
			System.out.println("Error en Task=" + e);
		}

		// Start training the networks+++++++++++++++++++++++++
		System.out.println("Entrenando las Redes");
		RNSerializer ser = new RNSerializer();
		RedNeuronal red;
		File filename;

		for (int i = 0; i < num_nets; i++) {
			red = new RedNeuronal("Red " + i);
			red.crearRed(num_in, new int[] { num_hidden }, num_out, new FuncionSigmoidea());

			// entrenar la red
			red.entrenarRed(entrada, salida, 1d, epochs);

			// probar la red
			int num_correct = 0;
            for (int j = 0; j < entrada.length; j++) {

                char expected = check(binarizeArray(salida[j]));

                byte[] binarizedResult = binarizeArray(red.clasificar(entrada[j]));

                char charResult = check(binarizedResult);

                if (expected == charResult) {
                    num_correct++;
                }
            }
            double precision = (double) num_correct / (double) entrada.length;
            precision = precision * 100;

			
			// almacenar la red
			filename = new File(saveFolder + File.separator + "red_" + i + "_"+precision+".ser");
			ser.serializeRed(red, filename.toString());
		}
	}

	private char check(byte[] muestra) {
		char is = '?';
		for (int i = 0; i < PATRONES_SALIDA.length; i++) {
			boolean es = true;
			for (int j = 0; j < PATRONES_SALIDA[i].length; j++) {
				es = es && (muestra[j] == PATRONES_SALIDA[i][j]);
			}
			if (es) {
				is = (char) (i + 65);
				break;
			}
		}
		return is;
	}

	private byte[] binarizeArray(double[] arr) {
		byte[] bi = new byte[arr.length];

		for (int i = 0; i < bi.length; i++) {
			bi[i] = (arr[i] > 0.5) ? (byte) 1 : (byte) 0;
		}
		return bi;
	}
	
	private File createDirectory(String dir) {
		File f = new File(dir);
		if (!f.exists()) {
			if (f.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		return f;
	}
}
