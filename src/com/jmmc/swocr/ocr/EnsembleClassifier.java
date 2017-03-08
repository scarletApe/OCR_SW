package com.jmmc.swocr.ocr;

import java.io.File;
import java.util.HashSet;

/**
 * This class is an ensemble classifier, it combines many artificial neural
 * networks in a horizontal structure to increase the accuracy of the ocr
 * classification. As its strategie it uses majority voting.
 * 
 * @author juanmartinez
 *
 */
public class EnsembleClassifier {

	private OCR_ANN[] classifiers;

	public EnsembleClassifier() {
		classifiers = new OCR_ANN[5];

		classifiers[0] = new OCR_ANN("com/jmmc/swocr/ocr/red_2_96.0773803722834.ser"); // the
																						// best
																						// one
		classifiers[1] = new OCR_ANN("com/jmmc/swocr/ocr/red_1_97.01783451905273.ser");
		classifiers[2] = new OCR_ANN("com/jmmc/swocr/ocr/red_14_96.79855764545367.ser");
		classifiers[3] = new OCR_ANN("com/jmmc/swocr/ocr/red_17_96.69135561836079.ser");
		classifiers[4] = new OCR_ANN("com/jmmc/swocr/ocr/red_38_97.12503654614561.ser");
	}

	/**
	 * This method classifies an input image file to a char. It uses majority
	 * voting to see which is the most likely class for it. First each network
	 * classifies the input sample, then we see which are the candidates, then
	 * the votes are tallied. The class that recieves the most votes is the one
	 * that gets returned.
	 * 
	 * @param f
	 * @return
	 */
	public char clasificar(File f) {
		char[] inputs = new char[classifiers.length];

		// we obtain the clasification of each neural net
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = classifiers[i].clasificar(f);
		}

		for (int i = 0; i < inputs.length; i++) {
			System.out.println(inputs[i]);
		}

		// next we see which are the candidates
		HashSet<Character> candidates = new HashSet<>();
		for (int i = 0; i < inputs.length; i++) {

			// we are going to ignore the ? as a candidate
			if (inputs[i] != '?') {
				candidates.add(inputs[i]);
			}
		}

		System.out.println("The candidates:");
		// just printing out what it has
		Character[] c = candidates.toArray(new Character[0]);
		for (int i = 0; i < c.length; i++) {
			System.out.println(c[i]);
		}

		// voting time
		int[] votes = new int[c.length];
		for (int i = 0; i < votes.length; i++) {
			votes[i] = 0;
			for (int j = 0; j < inputs.length; j++) {
				if (inputs[j] == c[i]) {
					votes[i]++;
				}
			}
		}

		// lets see the results of the voting
		System.out.println("the results of the voting:");
		for (int i = 0; i < c.length; i++) {
			System.out.println(c[i] + " votes=" + votes[i]);
		}

		// we are going to see which has the most votes
		int max = 0;
		int index = 0;
		// System.out.println("votes.length=" + votes.length);
		for (int i = 0; i < votes.length; i++) {
			if (votes[i] > max) {
				max = votes[i];
				index = i;
				// System.out.println("votes=" + votes[i] + " max=" + max + "
				// index=" + index);
			}
		}

		char votingResult = c[index];

		System.out.println("The winner is " + votingResult + "\n\n");

		return votingResult;
	}
}
