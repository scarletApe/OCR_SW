/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchword_fxml.gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jmmc.swocr.searchword.CrossWord;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author manuelmartinez
 */
public class FXML_SearchWord_Controller implements Initializable {

	@FXML
	private WebView editorPane;
	@FXML
	private TextField textField;
	@FXML
	private Button buttonSearch;

	private CrossWord cw;

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
			cw = new CrossWord();
			cw.loadCrossWord(file.toString());

			final WebEngine webEngine = editorPane.getEngine();
			webEngine.loadContent(cw.toString());
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
				cw.saveToHTML(file.toString());
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

	@FXML
	protected void handleClose(ActionEvent e) {
		System.exit(0);
	}

	@FXML
	protected void handleClear(ActionEvent e) {
		cw = null;

		final WebEngine webEngine = editorPane.getEngine();
		webEngine.loadContent("");

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
			final WebEngine webEngine = editorPane.getEngine();
			webEngine.loadContent(cw.toString());
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

	protected void setCrossWord(char[][] matrix) {
		cw = new CrossWord();
		cw.setCrossWord(matrix);

		final WebEngine webEngine = editorPane.getEngine();
		webEngine.loadContent(cw.toString());
		// System.out.println(cw);
		SearchWord_FXML.stagee.setTitle("Imported Image");

		textField.setDisable(false);
		buttonSearch.setDisable(false);
	}
}
