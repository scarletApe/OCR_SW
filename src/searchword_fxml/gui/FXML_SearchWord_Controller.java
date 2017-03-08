/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchword_fxml.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.jmmc.swocr.searchword.CrossWord_Image;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author manuelmartinez
 */
public class FXML_SearchWord_Controller implements Initializable {

	// @FXML
	// private WebView editorPane;
	@FXML
	private BorderPane bpCenter;
	@FXML
	private TextField textField;
	@FXML
	private Button buttonSearch;

	private CrossWord_Image cw;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		this.cw = null;

		textField.setDisable(true);
		buttonSearch.setDisable(true);
	}

	@FXML
	protected void handleOpenFile(ActionEvent e) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(SearchWord_FXML.stagee);
		if (file != null) {
			cw = new CrossWord_Image();
			cw.loadCrossWord(file.toString());

			// final WebEngine webEngine = editorPane.getEngine();
			// webEngine.loadContent(cw.toString());

			BufferedImage img = cw.getImage();
			ImageView imageView = new ImageView(SwingFXUtils.toFXImage(img, null));
			bpCenter.setCenter(imageView);

			SearchWord_FXML.stagee.setHeight(img.getHeight() + 100);

			// System.out.println(cw);
			SearchWord_FXML.stagee.setTitle(file.getName());

			textField.setDisable(false);
			buttonSearch.setDisable(false);
		}
	}

	@FXML
	protected void handleExportHTML(ActionEvent e) {
		if (cw != null) {
			FileChooser fileChooser = new FileChooser();

			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML file ", "*.html");
			fileChooser.getExtensionFilters().add(extFilter);

			fileChooser.setTitle("Export to HTML");
			File file = fileChooser.showSaveDialog(SearchWord_FXML.stagee);
			if (file != null) {
				cw.saveToHTML(file.toString(), file.getName());
			}
		}
	}

	@FXML
	protected void handleExportText(ActionEvent e) {
		if (cw != null) {
			FileChooser fileChooser = new FileChooser();

			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Txt file ", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);

			fileChooser.setTitle("Export to Txt");
			File file = fileChooser.showSaveDialog(SearchWord_FXML.stagee);
			if (file != null) {
				cw.saveToText(file.toString());
			}
		}
	}

	// handleExportImage
	@FXML
	protected void handleExportImage(ActionEvent e) throws IOException {
		if (cw != null) {
			FileChooser fileChooser = new FileChooser();

			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG file ", "*.png");
			fileChooser.getExtensionFilters().add(extFilter);

			fileChooser.setTitle("Export Image to PNG");
			File file = fileChooser.showSaveDialog(SearchWord_FXML.stagee);
			if (file != null) {
				// cw.saveToText(file.toString());
				BufferedImage img = cw.getImage();
				ImageIO.write(img, "png", file);
			}
		}
	}

	@FXML
	protected void handleClose(ActionEvent e) {
		System.exit(0);
	}

	@FXML
	protected void handleClear(ActionEvent e) {
		cw = null;

		// final WebEngine webEngine = editorPane.getEngine();
		// webEngine.loadContent("");

		BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
		ImageView imageView = new ImageView(SwingFXUtils.toFXImage(img, null));
		bpCenter.setCenter(imageView);

		textField.setText("");
		textField.setDisable(true);
		buttonSearch.setDisable(true);
		// this.setTitle("");
		SearchWord_FXML.stagee.setTitle("");
	}

	@FXML
	protected void handleButtonSearch(ActionEvent e) {
		String s = textField.getText();
		if (s.isEmpty()) {
		} else if (cw.findWord(s)) {
			// editorPane.setText(cw.toString());
			// final WebEngine webEngine = editorPane.getEngine();
			// webEngine.loadContent(cw.toString());

			BufferedImage img = cw.getImage();
			ImageView imageView = new ImageView(SwingFXUtils.toFXImage(img, null));
			bpCenter.setCenter(imageView);

			textField.setText("");
		} else {
			textField.setText("");
			// JOptionPane.showMessageDialog(null, "Sorry " + s + " was not
			// found...");
			System.out.println("Sorry " + s + " was not found...");
		}
	}

	// handleImportImage
	@FXML
	protected void handleImportImage(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchword_fxml/gui/FXMLImageImport.fxml"));

			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Import Image");

			Parent p = (Parent) loader.load();
			p.getStylesheets().add(getClass().getResource("/searchword_fxml/gui/Metro-UI.css").toExternalForm());

			stage.setScene(new Scene(p));

			FXMLImageImportController controller = loader.<FXMLImageImportController> getController();

			stage.show();
			controller.initData(this);
		} catch (Exception ex) {
			System.out.println("Error Importing Image");
		}
	}

	// handleAbout
	@FXML
	protected void handleAbout(ActionEvent e) {
		// the the message dialog
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchword_fxml/gui/MessageDialog.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("");
			Parent p = (Parent) loader.load();
			p.getStylesheets().add(getClass().getResource("/searchword_fxml/gui/Metro-UI.css").toExternalForm());
			stage.setScene(new Scene(p));
			FXML_MessageDialog_Controller controller = loader.<FXML_MessageDialog_Controller> getController();
			stage.show();
			stage.setResizable(false);
			controller.initData(stage, 3, "This program solves WordSearch puzzles. You can import an image with OCR. "
					+ "The OCR in this system was implemented with a Neural Network. \nThis "
					+ "software uses the Backward Oracle Matching Algorithm." + "\nCopyleft 2017 Juan Manuel Martinez");
		} catch (Exception ex) {
			System.out.println("Error " + ex);
		}
	}

	protected void setCrossWord(char[][] matrix) {
		cw = new CrossWord_Image();
		cw.setCrossWord(matrix);

		// final WebEngine webEngine = editorPane.getEngine();
		// webEngine.loadContent(cw.toString());

		BufferedImage img = cw.getImage();
		ImageView imageView = new ImageView(SwingFXUtils.toFXImage(img, null));
		bpCenter.setCenter(imageView);

		SearchWord_FXML.stagee.setHeight(img.getHeight() + 100);

		// System.out.println(cw);
		SearchWord_FXML.stagee.setTitle("Imported Image");

		textField.setDisable(false);
		buttonSearch.setDisable(false);
	}
}
