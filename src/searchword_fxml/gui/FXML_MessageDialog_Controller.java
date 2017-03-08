package searchword_fxml.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Esta classe es una ventanita de dialogo. Solo muestra un texto, una imagen, y
 * un boton okay.
 * 
 * @author juanmartinez
 *
 */
public class FXML_MessageDialog_Controller implements Initializable {

	@FXML
	private ImageView ivIcon;

	@FXML
	private Label labelMessage;

	@FXML
	private TextArea taMessage;

	BufferedImage warningImg;
	BufferedImage successImg;
	BufferedImage javaImg;
	Stage stage;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			warningImg = ImageIO.read(getClass().getResourceAsStream("/searchword_fxml/gui/warning.png"));
			successImg = ImageIO.read(getClass().getResourceAsStream("/searchword_fxml/gui/success.png"));
			javaImg = ImageIO.read(getClass().getResourceAsStream("/searchword_fxml/gui/java.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void handleOkay(ActionEvent event) {
		if (stage != null)
			stage.close();
	}

	protected void initData(Stage s, int messageType, String message) {
		// get the refrence to this stage
		this.stage = s;

		// message types
		// 1 = okay
		// 2 = error
		// 3 = info with java icon
		switch (messageType) {
		case 3:
			labelMessage.setText("WordSearch with OCR");
			if (warningImg != null) {
				ivIcon.setImage(SwingFXUtils.toFXImage(javaImg, null));
			}
			break;
		case 2:
			labelMessage.setText("Oops, There was an error...");
			if (warningImg != null) {
				ivIcon.setImage(SwingFXUtils.toFXImage(warningImg, null));
			}
			break;
		case 1:
		default:
			labelMessage.setText("Success!!");
			if (successImg != null) {
				ivIcon.setImage(SwingFXUtils.toFXImage(successImg, null));
			}
			break;
		}

		// set the message on the text area
		taMessage.setText(message);
	}

}
