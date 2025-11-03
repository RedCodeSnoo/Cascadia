package fr.uge.version;

import java.awt.Graphics2D;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Tile;

/**
 * Interface representing the display of a game. It contains methods to draw various
 * elements on the screen such as the game board, player boards, tiles, animals,
 * victory screens, and other UI components.
 */
public interface Display {

	/**
   * Displays the victory screen with the winner's name and the list of players
   * along with their scores.
   *
   * @param graphics    The graphics context used to render the victory screen.
   * @param winnersName The name of the player who won the game.
   */
	void resultOfGame(String winnersName);

	/**
   * Displays the player's board, including the current state of the game such as
   * the player's progress, score, actions, and available cards.
   */
	void playerBoard();

	 /**
   * Displays the game board. This can include the display of game elements such as
   * tiles, animals, and available actions.
   */
	void gameBoard();

	/**
   * Displays a message related to token actions, prompting the player to select or
   * interact with a specific token.
   */
	void messageForToken();

	 /**
   * Displays additional content on the screen, such as specific graphical elements
   * or UI updates.
   */
	void drawToDisplay();

	/**
   * Displays an error message related to token actions, such as when the placement
   * of a token is invalid or not allowed.
   */
	void errorMessageForToken();

	/**
   * Displays a prompt asking the player if they want to draw new tokens or cards.
   *
   * @param graphics The graphics context used to draw the prompt.
   */
	void drawPrompt(Graphics2D graphics);

	/**
   * Returns the height of the display.
   *
   * @return The height of the display.
   */
	int height();

	/**
   * Returns the width of the display.
   *
   * @return The width of the display.
   */
	int width();

	/**
   * Returns the current position of the cursor on the display.
   *
   * @return The current position of the cursor as a Coordinate object.
   */
	Coordinate cursor();

	/**
   * Draws the possible choices of an element (token or tile) that the player can select or interact with.
   *
   * @param graphics The graphics context used to draw the choices.
   */
	void drawChoiceOf(Graphics2D graphics);

	/**
   * Draws the choices related to the selection of a nature token, allowing the player
   * to choose the type of token to use.
   *
   * @param graphics The graphics context used to draw the nature token choices.
   */
	void drawChoiceOfNatureToken(Graphics2D graphics);

	/**
   * Draws the options for selecting a token to remove from the game.
   *
   * @param graphics The graphics context used to draw the token removal options.
   */
	void drawChooseTokenToRemove(Graphics2D graphics);

	 /**
   * Draws the options for handling token removal.
   *
   * @param graphics The graphics context used to display the token removal options.
   */
	void drawHandleTokenRemovalChoice(Graphics2D graphics);

	/**
   * Draws the options for selecting a batch of tile and animal in the game, allowing the player to choose a group or set of items.
   *
   * @param graphics The graphics context used to draw the batch selection options.
   * @param choice   The index or type of the batch to display.
   */
	void drawChoiceOfBatch(Graphics2D graphics, int choice);

	 /**
   * Draws a tile on the game board at a specific position, with the option to rotate it.
   *
   * @param graphics The graphics context used to draw the tile on the game board.
   * @param tile     The tile to draw.
   * @param rotation An array containing the rotation angle for the tile.
   */
	void drawAddTileOnGameBoard(Graphics2D graphics, Tile tile, int[] rotation);

	/**
   * Draws an animal on the game board, with the option to place it at a specific position
   * based on the player's actions.
   *
   * @param graphics The graphics context used to draw the animal on the game board.
   * @param animals  The animal to draw on the game board.
   */
	void drawAddAnimalOnGameBoard(Graphics2D graphics, Animals animals);

	/**
   * Draws an animal where the player can choose its placement on the board.
   *
   * @param graphics The graphics context used to draw the animal.
   * @param animal   The animal to draw.
   */
	void drawAddAnimal(Graphics2D graphics, Animals animal);

	/**
   * Changes the position of the cursor on the display, typically in response to player input
   * or game actions.
   *
   * @param direction The direction or distance for the cursor change:
   *
   */
	void changeCursor(int direction);
}
