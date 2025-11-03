package fr.uge.version;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Tile;

/**
 * Interface representing actions that can be performed within the game.
 * This includes selecting tiles, placing animals on the board, tokens gestion,
 * and handling other gameplay-related actions.
 */
public interface Action {

	/**
   * Displays available tiles to the player.
   * This method is used for tile selection.
   *
   * @throws NumberFormatException if the input is not a valid number.
   * @throws IOException if an I/O error occurs.
   */
	void availableTile() throws NumberFormatException, IOException;

	/**
	 * Handles the selection of tiles and animals by the player.
	 * 
	 * @return A list of integers representing the chosen tiles or animals.
	 * @throws IOException if there is an I/O error during input/output operations.
	 */
	List<Integer> handleTileAndAnimalChoice() throws IOException;

	/**
	 * Handles the removal of a token based on the player's choice.
	 * 
	 * @throws NumberFormatException if there is an error parsing numbers.
	 * @throws IOException if there is an I/O error during input/output operations.
	 */
	void handleTokenRemovalChoice() throws NumberFormatException, IOException;

	/**
	 * Adds a tile to the game board.
	 * 
	 * @param tile The tile to be added to the board.
	 * @throws IOException if there is an I/O error during input/output operations.
	 */
	void addTileOnGameBoard(Tile tile) throws IOException;

	/**
	 * Adds an animal to the game board.
	 * 
	 * @param animals The animal to be added to the board.
	 * @param hashMap A map of animals with their counts.
	 * @throws NumberFormatException if there is an error parsing numbers.
	 * @throws IOException if there is an I/O error during input/output operations.
	 */
	void addAnimalOnGameBoard(Animals animals, HashMap<Animals, Integer> hashMap)
			throws NumberFormatException, IOException;

	/**
	 * Prompts the player to choose a batch between 4.
	 * 
	 * @return The player's choice of batch.
	 * @throws IOException if there is an I/O error during input/output operations.
	 */
	int choiceOfBatch() throws IOException;

	/**
	 * Prompts the player to choose if he use a nature token or not.
	 * 
	 * @return The player's chosen option.
	 * @throws IOException if there is an I/O error during input/output operations.
	 */
	int choiceOf() throws IOException;

	/**
	 * Prompts the player to choose an action.
	 * 
	 * @return The number of nature tokens chosen by the player.
	 * @throws IOException if there is an I/O error during input/output operations.
	 */
	int choiceOfNatureToken() throws IOException;

}
