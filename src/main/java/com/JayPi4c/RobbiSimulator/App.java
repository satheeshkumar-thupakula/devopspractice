package com.JayPi4c.RobbiSimulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.controller.examples.DatabaseManager;
import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * 
 * Hauptklasse des Robbi Simulators.<br>
 * Javaversion: 17 <br>
 * JavaFX: 17<br>
 * 
 * @author Jonas Pohl
 *
 */
public class App extends Application {

	private static final Logger logger = LogManager.getLogger(App.class);

	@Override
	public void start(Stage primaryStage) {
		logger.info("Starting application");
		logger.debug("Creating scene");
		ProgramController.createAndShow(ProgramController.DEFAULT_ROBBI_FILE_NAME);
		logger.debug("Scene creation done");
	}

	/**
	 * Application entry point
	 * 
	 * @param args the arguments from the command line
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() {
		logger.info("Initialize application");

		logger.debug("Loading Program Controller");
		if (!ProgramController.initialize()) {
			logger.error("Failed to load Program Controller");
			AlertHelper.showAlertAndWait(AlertType.ERROR, I18nUtils.i18n("Init.error.message"), null, null,
					I18nUtils.i18n("Init.error.title"), I18nUtils.i18n("Init.error.header"));
			Platform.exit();
		}
		logger.debug("loading Program Controller successfully");

		logger.debug("Connecting to Database");
		if (DatabaseManager.getDatabaseManager().initialize())
			logger.debug("Connecting to Database successfully");
		else
			logger.debug("Connecting to Database failed");

		logger.debug("Loading images");
		MainStage.loadImages();
		TerritoryPanel.loadImages();
		logger.debug("Finished loading images");

	}

	@Override
	public void stop() {
		logger.debug("Closing Database Connection");
		DatabaseManager.getDatabaseManager().shutDown();
		logger.debug("Closing Database Connection successfully");

		logger.info("Quitting application");
	}

}