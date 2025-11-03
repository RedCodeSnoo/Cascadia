package fr.uge.version.graphic.actionForGraphic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Draw;
import fr.uge.game.element.Player;
import fr.uge.game.element.Tile;
import fr.uge.version.Action;
import fr.uge.version.Display;

/**
 * Handles the graphical actions in the game, including tile selection,
 * overpopulation resolution, and user input processing through pointer and
 * keyboard events.
 */
public class GraphicAction implements Action {
	private Draw draw;
	private final Player player;
	private Display display;
	private final ApplicationContext context;
	private final int shapeOfTile;

	/**
	 * Constructs a GraphicAction with the specified parameters.
	 * 
	 * @param draw the Draw instance representing the draw of the game
	 * @param player the Player instance representing the player interacting with the game
	 * @param context the ApplicationContext used for rendering frames and handling events
	 * @param display the Display instance that handles the graphical display of the game
	 * @param shapeOfTile the integer representing the shape of the tile (square or hexagon)
	 * @throws NullPointerException if any of the parameters are null
	 */
	public GraphicAction(Draw draw, Player player, ApplicationContext context, Display display, int shapeOfTile) {
		this.draw = Objects.requireNonNull(draw, "draw is null");
		this.player = Objects.requireNonNull(player, "player is null");
		this.display = Objects.requireNonNull(display, "display is null");
		this.context = Objects.requireNonNull(context, "context is null");
		this.shapeOfTile = shapeOfTile;
	}

	/**
	 * Manages the logic for choosing available tiles based on animal counts in the
	 * draw deck, handling overpopulation and updating the game board.
	 *
	 * @throws NumberFormatException if there is an issue with number formatting during tile choice
	 * @throws IOException if there is an issue with input or output during tile choice
	 */
	@Override
	public void availableTile() throws NumberFormatException, IOException {
		var counter1 = draw.numberOfSameAnimal(0);
		var counter2 = draw.numberOfSameAnimal(1);
		if (counter1 == 3) {
			overpopulation(draw.animalsForChoice().get(0));
		}
		if (counter2 == 3 && counter1 != 4) {
			overpopulation(draw.animalsForChoice().get(1));
		}
		if (counter1 == 4) {
			draw.changeAnimalToChoice();
			availableTile();
		}
		context.renderFrame(graphics -> display.gameBoard());
	}

	/**
	 * Handles user actions for overpopulation, allowing the player to choose whether to redraw an animal or skip.
	 *
	 * @param animal the animal involved in the overpopulation
	 * @param event the event triggered by the player (pointer or keyboard)
	 * @return true if the action was completed, false otherwise
	 * @throws NumberFormatException if there is an issue with number formatting
	 * @throws IOException if there is an issue with input or output during the action
	 */
	private boolean actionForOverPopulation(Animals animal, Event event) throws NumberFormatException, IOException {
		switch (event) {
		case PointerEvent pe -> {
			if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
				if (isYesButtonClicked(pe.location())) {
					redrawAnimal(animal);
					return true;
				} else if (isNoButtonClicked(pe.location())) {
					return true;
				}
			}
		}
		case KeyboardEvent ke -> {
			handleKeyboardEvent(ke);
			context.renderFrame(graphics -> display.drawPrompt(graphics));
		}
		default -> {  }
		}
		return false;
	}

	/**
	 * Handles the overpopulation scenario by prompting the player to decide whether to redraw an animal or not.
	 *
	 * @param animal the animal involved in the overpopulation situation
	 * @throws NumberFormatException if there is an issue with number formatting
	 * @throws IOException if there is an issue with input or output during the action
	 */
	private void overpopulation(Animals animal) throws NumberFormatException, IOException {
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawPrompt(graphics));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			if (actionForOverPopulation(animal, event)) {
				return;
			}
		}
	}

	/**
	 * Redraws the specified animal by replacing it with a new random animal.
	 *
	 * @param animal the animal to be replaced
	 * @throws NumberFormatException if there is an issue with number formatting
	 * @throws IOException if there is an issue with input or output during the action
	 */
	private void redrawAnimal(Animals animal) throws NumberFormatException, IOException {
		for (int i = 0; i < draw.animalsForChoice.size(); i++) {
			if (draw.animalsForChoice.get(i).equals(animal)) {
				draw.animalsForChoice.set(i, draw.randomAnimal());
			}
		}
		availableTile();
	}

	/**
	 * Handles the player's choice action, where they can either confirm or cancel the current action.
	 *
	 * @param event the event triggered by the player (pointer or keyboard)
	 * @return 1 if the player chooses to confirm, 0 if the player chooses to cancel, -1 if no action is taken
	 */
	private int actionForChoiceOf(Event event) {
		switch (event) {
		case PointerEvent pe -> {
			if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
				if (isYesButtonClicked(pe.location())) {
					return 1;
				} else if (isNoButtonClicked(pe.location())) {
					return 0;
				}
			}
		}
		case KeyboardEvent ke -> {
			if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
				handleKeyboardEvent(ke);
				context.renderFrame(graphics -> display.drawChoiceOf(graphics));
			}
		}
		default -> {  }
		}
		return -1;
	}

	/**
	 * Handles the player's choice action, where they choose a specific action from available options.
	 * It continuously waits for an event and processes it accordingly.
	 * 
	 * @return the player's action choice, as an integer (1 for confirm, 0 for cancel, etc.)
	 * @throws IOException if there is an issue with input or output during the action
	 */
	public int choiceOf() throws IOException {
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawChoiceOf(graphics));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			int actionForChoiceOfReturn = actionForChoiceOf(event);
			System.out.println(actionForChoiceOfReturn);
			if (actionForChoiceOfReturn != -1) {
				return actionForChoiceOfReturn;
			}
		}
	}

	/**
	 * Handles the player's choice action for selecting a nature token.
	 * It continuously waits for an event and processes it accordingly, specifically handling pointer and keyboard events.
	 *
	 * @return 1 if the player confirms their choice, 0 if they cancel the action
	 * @throws IOException if there is an issue with input or output during the action
	 */
	public int choiceOfNatureToken() throws IOException {
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawChoiceOfNatureToken(graphics));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			switch (event) {
				case PointerEvent pe -> {
					if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
						var location = pe.location();
						if (isYesButtonClicked(location)) {
							return 1;
						} else if (isNoButtonClicked(location)) {
							return 0;
						}
					}
				}
				case KeyboardEvent ke -> {
					handleKeyboardEvent(ke);
					context.renderFrame(graphics -> display.drawChoiceOfNatureToken(graphics));
				}
				default -> {  }
			}
		}
	}

	/**
	 * Handles the keyboard events for navigating choices (up, down, left, right) and updates the display accordingly.
	 *
	 * @param ke the keyboard event triggered by the player
	 */
	private void handleKeyboardEvent(KeyboardEvent ke) {
		switch (ke.key()) {
			case KeyboardEvent.Key.UP -> {
				display.changeCursor(1);
			}
			case KeyboardEvent.Key.DOWN -> {
				display.changeCursor(3);
			}
			case KeyboardEvent.Key.LEFT -> {
				display.changeCursor(2);
			}
			case KeyboardEvent.Key.RIGHT -> {
				display.changeCursor(4);
			}
			default -> {  }
		}
		context.renderFrame(graphics -> display.gameBoard());
	}

	/**
	 * Handles the player's choice action for selecting a batch from available options.
	 * It waits for a pointer or keyboard event and processes it accordingly, returning the selected tile index.
	 *
	 * @param event the event triggered by the player (pointer or keyboard)
	 * @param i the index of the current batch being processed
	 * @return the index of the selected tile, or -1 if no valid selection was made
	 */
	private int actionForChoiceOfBatch(Event event, int i) {
		switch (event) {
			case PointerEvent pe -> {
				if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
					var location = pe.location();
					int animal = getClickedTileIndex(location);
					if (animal != -1) {
						return animal;
					}
				}
			}
			case KeyboardEvent ke -> {
				if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
					handleKeyboardEvent(ke);
					context.renderFrame(graphics -> display.drawChoiceOfBatch(graphics, i));
				}
			}
			default -> {  }
		}
		return -1;
	}

	/**
	 * Handles the player's choice for selecting a batch, continuously waiting for an event and returning the selected batch.
	 *
	 * @return the index of the selected batch
	 * @throws IOException if there is an issue with input or output during the action
	 */
	public int choiceOfBatch() throws IOException {
		int[] i = {2};
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawChoiceOfBatch(graphics, i[0]));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			int actionForChoiceOfBatchReturn = actionForChoiceOfBatch(event, i[0]);
			if (actionForChoiceOfBatchReturn != -1) {
				return actionForChoiceOfBatchReturn;
			}
		}
	}

	/**
	 * Checks if the "Yes" button was clicked based on the pointer event's location.
	 *
	 * @param location the location of the pointer event
	 * @return true if the "Yes" button was clicked, false otherwise
	 */
	private boolean isYesButtonClicked(PointerEvent.Location location) {
		return location.x() >= display.width() / 100 + display.width() / 190
				&& location.x() <= display.width() / 100 + display.width() / 190 + display.width() / 12
				&& location.y() >= display.height() / 2 + display.height() / 8
				&& location.y() <= display.height() / 2 + display.height() / 8 + display.height() / 15;
	}

	/**
	 * Checks if the "No" button was clicked based on the pointer event's location.
	 *
	 * @param location the location of the pointer event
	 * @return true if the "No" button was clicked, false otherwise
	 */
	private boolean isNoButtonClicked(PointerEvent.Location location) {
		return location.x() >= display.width() / 100 + display.width() / 190 + display.width() / 12 + display.width() / 50
				&& location.x() <= display.width() / 100 + display.width() / 190 + display.width() / 12 + display.width() / 50
						+ display.width() / 12
				&& location.y() >= display.height() / 2 + display.height() / 8
				&& location.y() <= display.height() / 2 + display.height() / 8 + display.height() / 15;
	}

	/**
	 * Handles the tile and animal choice process by rendering the game board and batch choices,
	 * and waiting for the player to make selections until two choices are made.
	 *
	 * @return a list of selected tile indices
	 * @throws IOException if there is an issue with input or output during the action
	 */
	@Override
	public List<Integer> handleTileAndAnimalChoice() throws IOException {
		int[] i = { 0 };
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawChoiceOfBatch(graphics, i[0]));
		ArrayList<Integer> result = new ArrayList<>();
		while (i[0] != 2) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			int choice = actionForChoiceOfBatch(event, i[0]);
			if (choice != -1) {
				result.add(choice);
				i[0]++;
				context.renderFrame(graphics -> display.drawChoiceOfBatch(graphics, i[0]));
			}
		}
		return List.copyOf(result);
	}

	/**
	 * Handles the action for choosing a token to remove by checking pointer and keyboard events.
	 * Updates the list of tokens to remove and redraws the game state.
	 *
	 * @param listOfToken a set that keeps track of tokens chosen for removal
	 * @param event the event triggered by the player (pointer or keyboard)
	 * @return true if a valid choice was made, false otherwise
	 */
	private boolean actionForChooseTokenToRemove(Set<Integer> listOfToken, Event event) {
		switch (event) {
			case PointerEvent pe -> {
				if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
					var location = pe.location();
					int animal = getClickedTileIndex(location);
					if (animal != -1 && listOfToken.add(animal)) {
						draw.animalsForChoice.set(animal - 1, draw.randomAnimal());
						return true;
					}
				}
			}
			case KeyboardEvent ke -> {
				if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
					handleKeyboardEvent(ke);
					context.renderFrame(graphics -> display.drawChooseTokenToRemove(graphics));
				}
			}
			default -> {  }
		}
		return false;
	}

	/**
	 * Prompts the player to choose a token to remove, waiting for a valid selection.
	 *
	 * @param listOfToken a set to track the tokens selected for removal
	 * @throws IOException if there is an issue with input or output during the action
	 */
	private void chooseTokenToRemove(Set<Integer> listOfToken) throws NumberFormatException, IOException {
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawChooseTokenToRemove(graphics));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			if (actionForChooseTokenToRemove(listOfToken, event)) {
				return;
			}
		}
	}

	/**
	 * Gets the index of the clicked tile based on the pointer event's location on the screen.
	 *
	 * @param location the location of the pointer event
	 * @return the index of the clicked tile, or -1 if no tile was clicked
	 */
	private int getClickedTileIndex(PointerEvent.Location location) {
		int spaceWidth = display.width() / 100;
		int spaceHeight = display.height() / 50;
		int width1 = (display.width() / 2) / 11;
		int height1 = (display.height() - 2 * (display.height() / 10)) / 11;
		for (int i = 0; i < draw.tilesForChoice().size(); i++) {
			if (i % 2 == 0 && i != 0) {
				spaceHeight += height1 + display.height() / 50;
				spaceWidth = display.width() / 100;
			}
			int tileXStart = display.width() / 20 + spaceWidth;
			int tileYStart = display.height() / 6 + spaceHeight;
			if (location.x() >= tileXStart && location.x() <= tileXStart + width1 && location.y() >= tileYStart && location.y() <= tileYStart + height1) {
				return i + 1;
			}
			spaceWidth += width1 + display.width() / 100;
		}
		return -1;
	}

	/**
	 * Handles the choice action for token removal by checking pointer and keyboard events,
	 * allowing the player to confirm or cancel their decision.
	 *
	 * @param event the event triggered by the player (pointer or keyboard)
	 * @param end the boolean flag indicating the end of the removal process
	 * @return 0 if "Yes" was clicked, 1 if "No" was clicked, or -1 if no valid choice was made
	 */
	private int actionForHandleTokenRemovalChoice(Event event, Boolean end) {
		switch (event) {
			case PointerEvent pe -> {
				if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
					var location = pe.location();
					if (isYesButtonClicked(location)) {
						return 0;
					} else if (isNoButtonClicked(location)) {
						return 1;
					}
				}
			}
			case KeyboardEvent ke -> {
				if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
					handleKeyboardEvent(ke);
					context.renderFrame(graphics -> display.drawHandleTokenRemovalChoice(graphics));
				}
			}
			default -> {  }
		}
		return -1;
	}

	/**
	 * Handles the token removal choice process, allowing the player to select and remove tokens
	 * by rendering the game board and managing pointer and keyboard events.
	 *
	 * @throws NumberFormatException if there is an error in number parsing
	 * @throws IOException if there is an issue with input or output during the action
	 */
	@Override
	public void handleTokenRemovalChoice() throws NumberFormatException, IOException {
		Set<Integer> listOfToken = new HashSet<>();
		for (int i = 0; i < 4 && player.natureToken() != 0; i++) {
			chooseTokenToRemove(listOfToken);
			context.renderFrame(graphics -> display.gameBoard());
			context.renderFrame(graphics -> display.drawHandleTokenRemovalChoice(graphics));
			boolean end = false;
			while (!end) {
				var event = context.pollOrWaitEvent(10);
				if (event == null) {
					continue;
				}
				switch (actionForHandleTokenRemovalChoice(event, end)) {
					case 0 -> {
						end = true;
					}
					case 1 -> {
						return;
					}
					default -> {  }
				}
			}
		}
	}

	/**
	 * Converts a pointer event's location to a hexagonal coordinate system.
	 *
	 * @param location the location of the pointer event
	 * @return the converted coordinate
	 */
	private Coordinate coordinateConversionHexagon(PointerEvent.Location location) {
		int hexWidth = (display.width() / 2) / 11;
		int hexHeight = (display.height() - 2 * (display.height() / 10)) / 11;
		int relativeX = location.x() - (display.width() / 4);
		int relativeY = location.y() - (display.height() / 10);
		int col = (int) (relativeY / (3.0 * hexHeight / 4));
		if ((display.cursor().y() - 7 + col) % 2 == 0) {
			relativeX = relativeX - (display.width() / 2) / 25;
		}
		int row = (int) (relativeX / (9.0 * hexWidth / 10));
		int boardX = display.cursor().x() - 5 + row;
		int boardY = display.cursor().y() - 7 + col - 1;
		return new Coordinate(boardX, boardY + 1);
	}

	/**
	 * Handles the pointer event for adding a tile on the game board, checking if the tile is
	 * placed in a valid position based on the player's neighbor rules and the tile's shape.
	 *
	 * @param pe the pointer event triggered by the player
	 * @param tile the tile to be added
	 * @param event the event triggered by the player
	 * @param rotation an array representing the current tile rotation
	 * @return 0 if the tile was successfully added, 1 otherwise
	 */
	private int actionPointerEventForAddTileOnGameBoard(PointerEvent pe, Tile tile, Event event, int[] rotation) {
		if (pe.action() == PointerEvent.Action.POINTER_DOWN && isGameBoardClicked(pe.location())) {
			Coordinate coordinate = shapeOfTile == 2 ? coordinateConversionHexagon(pe.location()) : coordinateConversion(pe.location());
			if (shapeOfTile == 2) {
				if (player.neighbourHexagonal(coordinate.x(), coordinate.y()) == true && (!player.habitats().containsKey(new Coordinate(coordinate.x(), coordinate.y())))) {
					player.add(tile, coordinate.x(), coordinate.y(), Animals.NOTHING, tile.biome().size() == 1 ? 0 : rotation[0]);
					return 0;
				}
			} else {
				if (player.neighbourSquare(coordinate.x(), coordinate.y()) == true && (!player.habitats().containsKey(new Coordinate(coordinate.x(), coordinate.y())))) {
					player.add(tile, coordinate.x(), coordinate.y(), Animals.NOTHING, 0);
					return 0;
				}
			}
		}
		return 1;
	}

	/**
	 * Handles the action for adding a tile on the game board by processing pointer and keyboard events.
	 * Allows the player to rotate the tile and place it on the board.
	 *
	 * @param tile the tile to be added
	 * @param event the event triggered by the player
	 * @param rotation an array representing the current tile rotation
	 * @return the result of the action (0 for successful placement, 2 for rotation, 1 otherwise)
	 */
	private int actionForAddTileOnGameBoard(Tile tile, Event event, int[] rotation) {
		switch (event) {
			case PointerEvent pe -> {
				return actionPointerEventForAddTileOnGameBoard(pe, tile, event, rotation);
			}
			case KeyboardEvent ke -> {
				if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
					handleKeyboardEvent(ke);
					return ke.key() == KeyboardEvent.Key.R ? 2 : 1;
				}
			}
			default -> {  }
		}
		return 1;
	}

	/**
	 * Adds a tile to the game board, allowing the player to place and rotate the tile.
	 *
	 * @param tile the tile to be added
	 * @throws IOException if there is an issue with input or output during the action
	 */
	@Override
	public void addTileOnGameBoard(Tile tile) throws IOException {
		Objects.requireNonNull(tile, "tile is null");
		int[] rotation = { 1 };
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawAddTileOnGameBoard(graphics, tile, rotation));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			int resForAction = actionForAddTileOnGameBoard(tile, event, rotation);
			if (resForAction == 0) {
				return;
			}else if (resForAction == 2) {
				rotation[0] = rotation[0] == 6 ? 1 : rotation[0] + 1;
			}
			context.renderFrame(graphics -> display.drawAddTileOnGameBoard(graphics, tile, rotation));
		}
	}

	/**
	 * Checks if the game board was clicked based on the pointer event's location.
	 *
	 * @param location the location of the pointer event
	 * @return true if the game board area was clicked, false otherwise
	 */
	private boolean isGameBoardClicked(PointerEvent.Location location) {
		return location.x() >= display.width() / 4 && location.x() <= display.width() / 4 + display.width() / 2
				&& location.y() >= display.height() / 10
				&& location.y() <= display.height() / 10 + display.height() - 2 * (display.height() / 10);
	}

	/**
	 * Converts the pointer event's location to a coordinate in the square grid system.
	 *
	 * @param location the location of the pointer event
	 * @return the converted coordinate
	 */
	private Coordinate coordinateConversion(PointerEvent.Location location) {
		int width1 = (display.width() / 2) / 11;
		int height1 = (display.height() - 2 * (display.height() / 10)) / 11;
		int startX = display.width() / 4;
		int startY = display.height() / 10;
		int x = (location.x() - startX) / width1;
		int y = (location.y() - startY) / height1;
		return new Coordinate(display.cursor().x() - 5 + x, display.cursor().y() - 5 + y);
	}

	/**
	 * Handles the pointer event for adding an animal to the game board, ensuring that the animal
	 * is added to a valid habitat and updates the player's token count.
	 *
	 * @param pe the pointer event triggered by the player
	 * @param event the event triggered by the player
	 * @param animal the animal to be added
	 * @param hashMap a map that tracks the available animals and their count
	 * @return true if the animal was successfully added, false otherwise
	 */
	private boolean actionForAddAnimalPointerDown(PointerEvent pe, Event event, Animals animal, HashMap<Animals, Integer> hashMap) {
		if (pe.action() == PointerEvent.Action.POINTER_DOWN && isGameBoardClicked(pe.location())) {
			Coordinate coordinate = shapeOfTile == 2 ? coordinateConversionHexagon(pe.location()) : coordinateConversion(pe.location());
			if (player.habitats().containsKey(coordinate)) {
				for (var animalToCompare : player.getHabitat(coordinate).getAnimals()) {
					if (animal.equals(animalToCompare)) {
						player.addNatureToken(player.habitats().get(coordinate).tile());
						player.habitats().get(coordinate).changeAnimal(animal);
					}
				}
				for (var key : hashMap.keySet()) {
					if (key.equals(animal)) {
						hashMap.put(key, hashMap.get(key) - 1);
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Handles the action for adding an animal to the game board, processing pointer and keyboard events.
	 *
	 * @param event the event triggered by the player
	 * @param animal the animal to be added
	 * @param hashMap a map that tracks the available animals and their count
	 * @return true if the action was successful, false otherwise
	 */
	private boolean actionForAddAnimal(Event event, Animals animal, HashMap<Animals, Integer> hashMap) {
		switch (event) {
			case PointerEvent pe -> {
				if (actionForAddAnimalPointerDown(pe, event, animal, hashMap)) {
					return true;
				}
			}
			case KeyboardEvent ke -> {
				if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
					handleKeyboardEvent(ke);
					context.renderFrame(graphics -> display.drawAddAnimal(graphics, animal));
				}
			}
			default -> {  }
		}
		return false;
	}

	/**
	 * Adds an animal to the game board, allowing the player to place the animal in a valid habitat.
	 *
	 * @param animal the animal to be added
	 * @param hashMap a map that tracks the available animals and their count
	 * @throws IOException if there is an issue with input or output during the action
	 */
	private void addAnimal(Animals animal, HashMap<Animals, Integer> hashMap) throws IOException {
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawAddAnimal(graphics, animal));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			if (actionForAddAnimal(event, animal, hashMap)) {
				return;
			}
		}
	}

	/**
	 * Handles the action for adding an animal on the game board, ensuring that the animal is
	 * placed in a valid habitat by checking pointer and keyboard events.
	 *
	 * @param event the event triggered by the player
	 * @param animals the animal to be added
	 * @param hashMap a map that tracks the available animals and their count
	 * @return true if the action was successful, false otherwise
	 */
	private boolean actionForAddAnimalOnGameBoard(Event event, Animals animals, HashMap<Animals, Integer> hashMap) throws IOException {
		switch (event) {
			case PointerEvent pe -> {
				if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
					if (isYesButtonClicked(pe.location())) {
						addAnimal(animals, hashMap);
						return true;
					} else if (isNoButtonClicked(pe.location())) {
						return true;
					}
				}
			}
			case KeyboardEvent ke -> {
				if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
					handleKeyboardEvent(ke);
					context.renderFrame(graphics -> display.drawAddAnimalOnGameBoard(graphics, animals));
				}
			}
			default -> {  }
		}
		return false;
	}

	/**
	 * Waits for a click from the user to proceed with the game.
	 *
	 * @param context the application context that holds the events
	 */
	public static void waitClick(ApplicationContext context) {
		Objects.requireNonNull(context, "context is null");
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			switch (event) {
				case PointerEvent pe -> {
					if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
						return;
					}
				}
				default -> {  }
			}
		}
	}

	/**
	 * Adds an animal on the game board based on user interaction.
	 *
	 * @param animals the animal to be added
	 * @param hashMap a map that tracks the available animals and their count
	 * @throws NumberFormatException if an error occurs during number parsing
	 * @throws IOException if there is an issue with input or output during the action
	 */
	public void addAnimalOnGameBoard(Animals animals, HashMap<Animals, Integer> hashMap) throws NumberFormatException, IOException {
		Objects.requireNonNull(animals, "animal is null");
		Objects.requireNonNull(hashMap, "hashMap is null");
		context.renderFrame(graphics -> display.gameBoard());
		context.renderFrame(graphics -> display.drawAddAnimalOnGameBoard(graphics, animals));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			if (actionForAddAnimalOnGameBoard(event, animals, hashMap)) {
				return;
			}
		}
	}
}
