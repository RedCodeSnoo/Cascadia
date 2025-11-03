package fr.uge.version;

import java.io.IOException;
import java.util.List;

import com.github.forax.zen.ApplicationContext;


/**
 * Interface representing the choices that can be made by the player during the game.
 * This includes choosing the number of players, selecting a game, choosing cards,
 * and handling different menus.
 */
public interface Choice {
	
	/**
   * Prompts the player to choose the number of players for the game.
   *
   * @return the number of players chosen.
   * @throws NumberFormatException if the input is not a valid number.
   * @throws IOException if an I/O error occurs during input/output.
   */
	int numberOfPlayer() throws NumberFormatException, IOException;
	
	/**
   * Prompts the player to choose a game to play.
   *
   * @return an integer representing the chosen game option.
   * @throws NumberFormatException if the input is not a valid number.
   * @throws IOException if an I/O error occurs during input/output.
   */
	int choiceOfGame() throws NumberFormatException, IOException;
	
	/**
   * Prompts the player to choose all the DebitCards.
   * @param context the application context in which the cards are chosen.
   * @param width the width of the current game setup.
   * @param height the height of the current game setup.
   * @return a list of integers representing the chosen cards.
   * @throws NumberFormatException if the input is not a valid number.
   * @throws IOException if an I/O error occurs during input/output.
   */
	List<Integer> choiceOfCards(ApplicationContext context, int width, int height) throws NumberFormatException, IOException;
	
	/**
   * Handles the menu options for the fourth menu in the game.
   * @param context the application context that holds the current state of the game.
   * @param width the width of the current game setup.
   * @param height the height of the current game setup.
   */
	void handleFourthMenu(ApplicationContext context, int width, int height);

}
