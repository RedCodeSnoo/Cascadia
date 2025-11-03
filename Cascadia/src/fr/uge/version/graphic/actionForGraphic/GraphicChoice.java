package fr.uge.version.graphic.actionForGraphic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.PointerEvent.Location;

import fr.uge.game.Menu;
import fr.uge.version.Choice;
import fr.uge.version.graphic.DisplayForGraphic;

/**
 * This class handles user interactions and choice events in the graphical
 * interface of the game. It allows the user to select the number of players,
 * point mode, and other game-related options. It uses the Zen framework to
 * handle pointer events and render frames.
 */
public class GraphicChoice implements Choice {

	private int numberOfPlayers = 0;
	private int pointMode = 0;

	/**
	 * Processes the player's selection for the number of players and point mode.
	 * 
	 * @param context The application context that manages the rendering and event loop.
	 * @param location The location of the pointer (mouse click or touch).
	 * @param width The width of the display window.
	 * @param height The height of the display window.
	 */
	private void selectedPlayerAndPointMode(ApplicationContext context, Location location, int width, int height) {
		int selectedPlayer = numberOfPlayersChosen(location, width, height);
		if (selectedPlayer != 0) {
			this.numberOfPlayers = selectedPlayer;
			context.renderFrame(graphics -> DisplayForGraphic.drawFourthMenu(graphics, width, height, this.numberOfPlayers, this.pointMode));
		}
		int selectedPointMode = pointModeChosen(location, width, height);
		if (selectedPointMode != 0) {
			this.pointMode = selectedPointMode;
			context.renderFrame(graphics -> DisplayForGraphic.drawFourthMenu(graphics, width, height, this.numberOfPlayers, this.pointMode));
		}
	}

	/**
	 * Handles the events for the fourth menu where the player can choose the number of players and point mode.
	 * 
	 * @param context The application context.
	 * @param width The width of the window.
	 * @param height The height of the window.
	 * @param pe The pointer event triggered by the user interaction.
	 * @return Returns 0 when the next step can be taken, otherwise returns 1 to continue.
	 */
	private int actionForFourthMenu(ApplicationContext context, int width, int height, PointerEvent pe) {
		if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
			var location = pe.location();
			if (Menu.isQuitButtonClicked(location, width, height)) {
				context.dispose();
				System.exit(0);
			}
			selectedPlayerAndPointMode(context, location, width, height);
			if (isNextOrPlayButtonClicked(location, width, height) && numberOfPlayers != 0 && pointMode != 0) {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * Handles the rendering and user interaction for the fourth menu.
	 * 
	 * @param context The application context that manages the rendering and event loop.
	 * @param width The width of the display window.
	 * @param height The height of the display window.
	 */
	@Override
	public void handleFourthMenu(ApplicationContext context, int width, int height) {
		Objects.requireNonNull(context, "context is null");
		context.renderFrame(graphics -> DisplayForGraphic.drawFourthMenu(graphics, width, height, this.numberOfPlayers, this.pointMode));
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			switch (event) {
			case PointerEvent pe -> {
				if (actionForFourthMenu(context, width, height, pe) == 0) {
					return;
				}
			}
			default -> {
			}
			}
		}
	}

	/**
   * Checks if the "Next" or "Play" button was clicked based on the pointer location.
   * 
   * @param location The location of the pointer event.
   * @param width The width of the window.
   * @param height The height of the window.
   * @return Returns true if the "Next" or "Play" button is clicked, otherwise false.
   */
	private boolean isNextOrPlayButtonClicked(Location location, int width, int height) {
		return location.x() >= (width / 2 - (width / 5) / 2) && location.x() <= (width / 2 + (width / 5) / 2)
				&& location.y() >= (height / 2 + (height / 11) * 2)
				&& location.y() <= (height / 2 + (height / 11) * 2 + height / 11);
	}

	/**
   * Determines which point mode was selected based on the pointer location.
   * 
   * @param location The location of the pointer event.
   * @param width The width of the display window.
   * @param height The height of the display window.
   * @return Returns an integer representing the selected point mode (1, 2, or 3), or 0 if none selected.
   */
	private int pointModeChosen(Location location, int width, int height) {
		if (location.x() >= width - width / 3 && location.x() <= width - width / 3 + width / 5 && location.y() >= height / 3
				&& location.y() <= height / 3 + height / 14) {
			return 1;
		} else if (location.x() >= width - width / 3 && location.x() <= width - width / 3 + width / 5
				&& location.y() >= height / 3 + height / 7 && location.y() <= height / 3 + height / 7 + height / 14) {
			return 2;
		} else if (location.x() >= width - width / 3 && location.x() <= width - width / 3 + width / 5
				&& location.y() >= height / 3 + (height / 7) * 2
				&& location.y() <= height / 3 + (height / 7) * 2 + height / 14) {
			return 3;
		} else {
			return 0;
		}
	}

	/**
   * Determines which number of players was chosen based on the pointer location.
   * 
   * @param location The location of the pointer event.
   * @param width The width of the display window.
   * @param height The height of the display window.
   * @return Returns an integer representing the number of players selected (1 to 4), or 0 if none selected.
   */
	private int numberOfPlayersChosen(Location location, int width, int height) {
		if (location.x() >= width / 11 + width / 96 && location.x() <= width / 11 + width / 96 + width / 13
				&& location.y() >= height / 3 && location.y() <= height / 3 + height / 11) {
			return 1;
		} else if (location.x() >= width / 5 && location.x() <= width / 5 + width / 13 && location.y() >= height / 3
				&& location.y() <= height / 3 + height / 11) {
			return 2;
		} else if (location.x() >= width / 11 + width / 96 && location.x() <= width / 11 + width / 96 + width / 13
				&& location.y() >= height / 2 && location.y() <= height / 2 + height / 11) {
			return 3;
		} else if (location.x() >= width / 5 && location.x() <= width / 5 + width / 13 && location.y() >= height / 2
				&& location.y() <= height / 2 + height / 11) {
			return 4;
		} else {
			return 0;
		}
	}

	/**
   * Returns the selected number of players.
   * 
   * @return The number of players.
   */
	@Override
	public int numberOfPlayer() {
		return numberOfPlayers;
	}

	/**
   * Returns the selected point mode.
   * 
   * @return The point mode.
   */
	@Override
	public int choiceOfGame() {
		return pointMode;
	}

	/**
   * Handles the user's selection of cards in the graphical interface.
   * 
   * @param context The application context that manages the rendering and event loop.
   * @param width The width of the display window.
   * @param height The height of the display window.
   * @param cardsMap A list to store the chosen card IDs.
   */
	private void actionForChoiceOfCards(ApplicationContext context, int width, int height, ArrayList<Integer> cardsMap) {
		boolean end = true;
		while (end) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			switch (event) {
				case PointerEvent pe -> {
					if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
						int result = cardChosen(pe.location(), width, height);
						if (result != 0) {
							cardsMap.add(result);
							end = false;
						}
					}
				}
				default -> {  }
			}
		}
	}

	/**
   * Allows the user to choose cards during the game.
   * 
   * @param context The application context.
   * @param width The width of the display window.
   * @param height The height of the display window.
   * @return A list of chosen cards (represented by their IDs).
   * @throws NumberFormatException If a non-numeric value is encountered during card selection.
   * @throws IOException If an I/O error occurs.
   */
	@Override
	public List<Integer> choiceOfCards(ApplicationContext context, int width, int height) throws NumberFormatException, IOException {
		Objects.requireNonNull(context, "context is null");
		ArrayList<Integer> cardsMap = new ArrayList<Integer>();
		List<String> animalList = List.of("renard", "saumon", "ours", "wapiti", "buse");
		for (String animal : animalList) {
			context.renderFrame(graphics -> DisplayForGraphic.drawCardsToChoose(graphics, width, height, animal));
			actionForChoiceOfCards(context, width, height, cardsMap);
		}
		return List.copyOf(cardsMap);
	}

	/**
   * Determines which card was chosen based on the pointer location.
   * 
   * @param location The location of the pointer event.
   * @param width The width of the window.
   * @param height The height of the window.
   * @return The ID of the selected card (1 to 4), or 0 if none selected.
   */
	private int cardChosen(Location location, int width, int height) {
		if (location.x() >= (width / 2) - (width / 192) * 65
				&& location.x() <= (width / 2) - (width / 192) * 65 + (width / 192) * 30
				&& location.y() >= height / 2 - (height / 108) * 20
				&& location.y() <= height / 2 - (height / 108) * 20 + (height / 108) * 40) {
			return 1;
		}
		if (location.x() >= (width / 2) - (width / 192) * 30
				&& location.x() <= (width / 2) - (width / 192) * 30 + (width / 192) * 30
				&& location.y() >= height / 2 - (height / 108) * 20
				&& location.y() <= height / 2 - (height / 108) * 20 + (height / 108) * 40) {
			return 2;
		}
		if (location.x() >= (width / 2) + (width / 192) * 5
				&& location.x() <= (width / 2) + (width / 192) * 5 + (width / 192) * 30
				&& location.y() >= height / 2 - (height / 108) * 20
				&& location.y() <= height / 2 - (height / 108) * 20 + (height / 108) * 40) {
			return 3;
		}
		if (location.x() >= (width / 2) + (width / 192) * 40
				&& location.x() <= (width / 2) + (width / 192) * 40 + (width / 192) * 30
				&& location.y() >= height / 2 - (height / 108) * 20
				&& location.y() <= height / 2 - (height / 108) * 20 + (height / 108) * 40) {
			return 4;
		}
		return 0;
	}

}
