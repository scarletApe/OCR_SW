/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchword_fxml.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.jmmc.swocr.filtros_imagenes.Binarization;
import com.jmmc.swocr.filtros_imagenes.GrayScale;
import com.jmmc.swocr.filtros_imagenes.ProcesadorBoundingBox;
import com.jmmc.swocr.filtros_imagenes.ProcesadorEscala;
import com.jmmc.swocr.filtros_imagenes.ProcesadorImagen;
import com.jmmc.swocr.filtros_imagenes.ProcesadorInclinacion;
import com.jmmc.swocr.filtros_imagenes.ProcesadorZhangThinning;
import com.jmmc.swocr.ocr.EnsembleClassifier;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sourceforge.javaocr.ocrPlugins.CharacterExtractor;
import net.sourceforge.javaocr.ocrPlugins.CharacterTracer;
import net.sourceforge.javaocr.ocrPlugins.LineExtractor;

/**
 * FXML Controller class
 *
 * @author juanmartinez
 */
public class FXMLImageImportController implements Initializable {

	@FXML
	private BorderPane bpArea;
	@FXML
	private Button btnOpenFile;
	@FXML
	private Button btnImport;
	@FXML
	private ProgressBar pbar;

	@FXML
	private CheckBox cbCleanUp;

	private BufferedImage imageToProcess;
	private File inputFile;
	private FXML_SearchWord_Controller papa;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	
	}

	@FXML
	private void handleOpenImageFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");

		// Set extension filter
		List<String> l = new ArrayList<>();
		l.add("*.png");
		l.add("*.jpg");
		l.add("*.gif");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (png, jpg, gif)", l);
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(((Node) (event.getSource())).getScene().getWindow());
		if (file != null) {
			try {

				final javafx.scene.image.Image image = new javafx.scene.image.Image(
						new FileInputStream(file.getAbsoluteFile()));

				ImageView imageView = new ImageView(image);
				inputFile = file;
				bpArea.setCenter(imageView);
				// bpArea.getChildren().addAll(imageView);
				imageToProcess = ImageIO.read(file.getAbsoluteFile());
				btnImport.setDisable(false);

			} catch (java.lang.IllegalArgumentException e) {
				System.out.println("Error en cargar imagen " + e);
			} catch (IOException ex) {
				Logger.getLogger(FXMLImageImportController.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	}

	@FXML
	private void handleImport(ActionEvent event) throws IOException {
		if (imageToProcess != null && inputFile != null) {

			System.out.println("Staring Service");

			ImportarService service = new ImportarService();
			service.init(inputFile, cbCleanUp.isSelected());

			service.setOnSucceeded((WorkerStateEvent t) -> {
				Foo foo = service.getValue();

				// we now have the filled matrix, send it off
				char[][] matrix = foo.getMatrix();
				papa.setCrossWord(matrix);

				// set the image in the border pane
				bpArea.setCenter(foo.getImageView());

				// the the message dialog
				try {
					FXMLLoader loader = new FXMLLoader(
							getClass().getResource("/searchword_fxml/gui/MessageDialog.fxml"));
					Stage stage = new Stage(StageStyle.DECORATED);
					stage.setTitle("");
					Parent p = (Parent) loader.load();
					p.getStylesheets()
							.add(getClass().getResource("/searchword_fxml/gui/Metro-UI.css").toExternalForm());
					stage.setScene(new Scene(p));
					FXML_MessageDialog_Controller controller = loader.<FXML_MessageDialog_Controller> getController();
					stage.show();
					stage.setResizable(false);
					controller.initData(stage, 1, "The Image was successfully imported with the OCR neural net.");
				} catch (Exception ex) {
					System.out.println("Error " + ex);
				}
			});

			service.setOnFailed(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {
					Throwable ouch = service.getException();
					System.out.println(ouch.getClass().getName() + " -> " + ouch.getMessage());

					// the the message dialog
					try {
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("/searchword_fxml/gui/MessageDialog.fxml"));
						Stage stage = new Stage(StageStyle.DECORATED);
						stage.setTitle("");
						Parent p = (Parent) loader.load();
						p.getStylesheets()
								.add(getClass().getResource("/searchword_fxml/gui/Metro-UI.css").toExternalForm());
						stage.setScene(new Scene(p));
						FXML_MessageDialog_Controller controller = loader
								.<FXML_MessageDialog_Controller> getController();
						stage.show();
						stage.setResizable(false);
						controller.initData(stage, 2, "The image import failed.\n Error: " + ouch.toString());
					} catch (Exception ex) {
						System.out.println("Error " + ex.getMessage());
					}
				}
			});
			pbar.progressProperty().bind(service.progressProperty());
			service.start();

		}
	}

	protected void initData(FXML_SearchWord_Controller papa) {
		this.papa = papa;
		btnImport.setDisable(true);
	}
}

class ImportarService extends Service<Foo> {

	private File inputFile;
	private boolean cleanUp;

	public void init(File inputFile, boolean cleanUp) {
		this.inputFile = inputFile;
		this.cleanUp = cleanUp;
	}

	@Override
	protected Task<Foo> createTask() {
		return new Task<Foo>() {

			@Override
			protected Foo call() throws IOException, MalformedURLException {

				System.out.println("Running Service");
				Foo foo = new Foo();

				// apply grey scale and thresholding on the image
				GrayScale grey = new GrayScale();
				Binarization bin = new Binarization();

				File greyDir = createDirectory(
						System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "greyfolder");
				BufferedImage img = ImageIO.read(inputFile);
				img = grey.filter(img);
				img = bin.filter(img);
				File greyImage = new File(greyDir + File.separator + "image.png");
				ImageIO.write(img, "png", greyImage);
				updateProgress(1, 10); 

				// apply character tracing+++++++++++++++++++++++++
				// create a copy of the grey scale image in color
				BufferedImage coloredImage = new BufferedImage(img.getWidth(), img.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = coloredImage.getGraphics();
				g.drawImage(img, 0, 0, null);
				g.dispose();
				File colorImageFile = new File(greyDir + File.separator + "image_color.png");
				ImageIO.write(coloredImage, "png", colorImageFile);
				updateProgress(2, 10); 

				CharacterTracer ct = new CharacterTracer();
				coloredImage = ct.getTracedImage(colorImageFile);
				ImageView imageView = new ImageView(SwingFXUtils.toFXImage(coloredImage, null));
				foo.setImageView(imageView);
				updateProgress(3, 10); 

				// seperate image into lines+++++++++++++++++++++++++
				File lineFolder = createDirectory(
						System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "linefolder");
				LineExtractor le = new LineExtractor();
				le.slice(greyImage, lineFolder);
				updateProgress(4, 10); 

				// separate each line into individual characters++++++++
				File charFolder = createDirectory(
						System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "charFolder");
				CharacterExtractor ce = new CharacterExtractor();
				File[] listOfFiles = lineFolder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					ce.slice(listOfFiles[i], charFolder, 20, 20);
				}
				updateProgress(5, 10);

				// Apply image filter on the individual character images+++++++
				ArrayList<ProcesadorImagen> procesadores = new ArrayList<>();
				procesadores.add(new ProcesadorInclinacion());
				procesadores.add(new ProcesadorZhangThinning());
				procesadores.add(new ProcesadorBoundingBox());
				procesadores.add(new ProcesadorEscala(new Dimension(6, 8)));

				File filteredFolder = createDirectory(System.getProperty("user.home") + File.separator + "Desktop"
						+ File.separator + "filteredFolder");
				File[] listOfCharFiles = charFolder.listFiles();

				for (int i = 0; i < listOfCharFiles.length; i++) {
					String name = listOfCharFiles[i].getName();
					name = removeFileSuffix(name);
					System.out.println(name);
					BufferedImage imagen = ImageIO.read(listOfCharFiles[i].getAbsoluteFile());

					for (ProcesadorImagen procesador : procesadores) {
						imagen = procesador.procesar(imagen);
					}
					ImageIO.write(imagen, "png", new File(filteredFolder + File.separator + name + ".png"));
				}
				updateProgress(6, 10);

				// Recognize each processed character and build the matrix++++++
				File[] listOfFilteredFiles = filteredFolder.listFiles();

				// create a biarray to hold the parsed line/char info
				int[][] parsed = new int[listOfFilteredFiles.length][2];

				// iterate over the array of list of files
				String name;
				String[] split;
				for (int i = 0; i < listOfFilteredFiles.length; i++) {
					name = listOfFilteredFiles[i].getName();
					name = removeFileSuffix(name);
					split = name.split("_");
					parsed[i][0] = Integer.parseInt(split[1]);
					parsed[i][1] = Integer.parseInt(split[3]);
				}
				updateProgress(7, 10); 
				// find the max number of lines
				int max_lines = 0;
				for (int i = 0; i < parsed.length; i++) {
					if (parsed[i][0] > max_lines) {
						max_lines = parsed[i][0];
					}
				}
				System.out.println("max_lines=" + max_lines);

				// find the max number of chars
				int max_chars = 0;
				for (int i = 0; i < parsed.length; i++) {
					if (parsed[i][1] > max_chars) {
						max_chars = parsed[i][1];
					}
				}
				System.out.println("max_chars=" + max_chars);
				updateProgress(8, 10); 

				// create the array to hold the OCRed info
				char[][] matrix = new char[max_lines + 1][max_chars + 1];

				// classify each filtered image
//				OCR_ANN ocr = new OCR_ANN(); //TODO
				EnsembleClassifier ensemble = new EnsembleClassifier();
				for (int i = 0; i < listOfFilteredFiles.length; i++) {
//					matrix[parsed[i][0]][parsed[i][1]] = ocr.clasificar(listOfFilteredFiles[i]);
					matrix[parsed[i][0]][parsed[i][1]] = ensemble.clasificar(listOfFilteredFiles[i]);
				}
				updateProgress(9, 10); 

				// make the matrix lower case
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[i].length; j++) {
						matrix[i][j] = Character.toLowerCase(matrix[i][j]);
					}
				}

				foo.setMatrix(matrix);

				// clean up the created directories and files
				if (cleanUp) {
					deleteFolder(greyDir);
					deleteFolder(lineFolder);
					deleteFolder(charFolder);
					deleteFolder(filteredFolder);
				}
				updateProgress(10, 10);

				return foo;
			}

		};
	}

	public void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	private String removeFileSuffix(String s) {
		int i2 = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '.') {
				i2 = i;
				break;
			}
		}
		return s.substring(0, i2);
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

class Foo {
	private ImageView imageView;
	private char[][] matrix;

	public Foo() {

	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public char[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(char[][] matrix) {
		this.matrix = matrix;
	}

}
