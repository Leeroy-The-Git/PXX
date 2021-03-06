package csc2a.px.model.ui;

import java.util.Random;

import csc2a.px.model.game.EGAME_STATE;
import csc2a.px.model.game.GameController;
import csc2a.px.model.game.GameLoop;
import csc2a.px.model.game.Map;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 * Handles ui transitions
 */
public class PaneHandler extends BorderPane {
	private static final Color DEF_C = Color.WHITE;
	private static final Color DEF_LAND_C = Color.MEDIUMSEAGREEN;
	private static final Color DEF_RIVER_C = Color.CORNFLOWERBLUE;
	private static final Color[] DEF_ROUTE_C = {Color.RED, Color.BLUE, Color.PURPLE, Color.YELLOW, Color.DEEPSKYBLUE, Color.HOTPINK};

	private GameMenuBar gameMenu;
	private GameInfoPane gameInfoPane;
	private HowToPlayPane howToPlayePane;
	private AboutPane aboutPane;
	private GameController controller;
	private GameLoop gameLoop;
	private GameCanvas canvas;
	private StartPane startPane;
	private WinPane winPane;
	private LosePane losePane;
	public PaneHandler(Image carriageImage, Image startImage) {
		winPane = new WinPane();
		losePane = new LosePane();
		aboutPane = new AboutPane();		
		howToPlayePane = new HowToPlayPane();
		startPane = new StartPane(startImage, 800, 600);
		
		setMinSize(800, 600);
		setPrefSize(800, 600);
		setMaxSize(800, 600);
		setWidth(800);
		setHeight(600);
		this.setCenter(startPane);
		canvas = new GameCanvas();

		gameInfoPane = new GameInfoPane();
		controller = new GameController(DEF_C, DEF_ROUTE_C, carriageImage);
		canvas.setController(controller);
		
		gameLoop = new GameLoop() {

			@Override
			public void tick(float deltaTime) {
				if (controller.getGameState() != EGAME_STATE.CONTINUE) {
					gameLoop.stop();
					outputGameState(controller.getGameState());
				}
				controller.update(deltaTime);
				gameInfoPane.updateInfo(controller);
				canvas.redrawCanvas();
			}
		};

		gameMenu = new GameMenuBar() {

			@Override
			public void stop() {
				canvas.pause();
				gameLoop.stop();
			}

			@Override
			public void showHowToPlay() {
				setRight(null);
				setCenter(howToPlayePane);
			}

			@Override
			public void showAbout() {
				setRight(null);
				setCenter(aboutPane);
			}

			@Override
			public void start() {
				canvas.clear();
				if (controller.getMap() == null) {
					Alert errorAlert = new Alert(AlertType.WARNING);
					errorAlert.setHeaderText("No map specified");
					errorAlert.setContentText("Please generate or open a map file to continue");
					errorAlert.showAndWait();
				} else {
					setRight(gameInfoPane);
					canvas.setWidth(PaneHandler.this.getWidth() - gameInfoPane.getWidth());
					canvas.setHeight(PaneHandler.this.getHeight() - gameMenu.getHeight());
					setCenter(canvas);;
					canvas.play();
					gameLoop.start();
				}
			}

			@Override
			public void randomRiver() {
				setRight(gameInfoPane);
				canvas.clear();
				canvas.setWidth(PaneHandler.this.getWidth() - gameInfoPane.getWidth());
				canvas.setHeight(PaneHandler.this.getHeight() - gameMenu.getHeight());
				setCenter(canvas);
				Random random = new Random();
				Map map = new Map(DEF_LAND_C, DEF_RIVER_C, canvas.getWidth(), 
						canvas.getHeight());
				map.generateRandomRivers(random.nextInt(2) + 1, random.nextInt(100 - 45 + 1) + 15, 30);
				controller.setMap(map);
				canvas.redrawCanvas();
			}

			@Override
			public void addMap(Map map) {
				canvas.setWidth(PaneHandler.this.getWidth() - gameInfoPane.getWidth());
				canvas.setHeight(PaneHandler.this.getHeight() - gameMenu.getHeight());
				canvas.clear();
				controller.setMap(map);
				setRight(gameInfoPane);
				setCenter(canvas);
				canvas.redrawCanvas();
			}

			@Override
			public Map getMap() {
				return controller.getMap();
			}
		};

		this.setTop(gameMenu);
	}

	private void outputGameState(EGAME_STATE gameState) {
		switch (gameState) {
		case CONTINUE:
			gameLoop.start();
			setCenter(canvas);
			setRight(gameInfoPane);
			break;
		case LOSE:
			setRight(null);
			setCenter(losePane);
			break;
		case WIN:
			setRight(null);
			setCenter(winPane);
			break;
		default:
			break;
		
		}
	}
	
	public void setKeyHandler(Scene scene) {
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.SPACE) {
				if (gameLoop.isPaused()) {
					canvas.play();
					gameLoop.play();
				}
				else {
					canvas.pause();
					gameLoop.pause();
				}
			}
			if (!gameLoop.isPaused()) {
				switch (event.getCode()) {
				case Q:
					controller.purchaseRoute();
					break;
				case W:
					controller.addWagon();
					break;
				case E:
					controller.addCarriage();
					break;
				case DIGIT1:
					controller.setCurrentRoute(1);
					break;
				case DIGIT2:
					controller.setCurrentRoute(2);
					break;
				case DIGIT3:
					controller.setCurrentRoute(3);
					break;
				case DIGIT4:
					controller.setCurrentRoute(4);
					break;
				case DIGIT5:
					controller.setCurrentRoute(5);
					break;
				case DIGIT6:
					controller.setCurrentRoute(6);
					break;
				default:
					break;
				}
			}
		});
	}
}
