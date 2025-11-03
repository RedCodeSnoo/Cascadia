package fr.uge.version.terminal.actionForTerminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import fr.uge.version.Choice;

/**
 * The TerminalChoice class implements the Choice interface and provides the logic for
 * handling player choices in the terminal, such as selecting the number of players,
 * game mode, and card choices.
 */
public class TerminalChoice implements Choice {

	/**
   * Prompts the user to enter the number of players and ensures it is between 1 and 4.
   *
   * @return the number of players selected by the user
   * @throws NumberFormatException if the input is not a valid integer
   * @throws IOException if there is an issue reading the input
   */
	public int numberOfPlayer() throws NumberFormatException, IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Entrer le nombre de joueur. Vous pouvez jouer de 1 à 4 joueurs.");
		var numberOfPlayers = Integer.parseInt(reader.readLine());
		while (numberOfPlayers < 1 || numberOfPlayers > 4) {
			System.out.println("Erreur ! Entrer le nombre de joueur. Vous pouvez jouer de 1 à 4 joueurs.");
			numberOfPlayers = Integer.parseInt(reader.readLine());
		}
		return numberOfPlayers;
	}

	/**
   * Prompts the user to choose the game mode and ensures a valid selection (1, 2, or 3).
   *
   * @return the chosen game mode (1, 2, or 3)
   * @throws NumberFormatException if the input is not a valid integer
   * @throws IOException if there is an issue reading the input
   */
	public int choiceOfGame() throws NumberFormatException, IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		int choice;
		do {
			System.out.println("Voulez-vous jouer au mode famille, intermédiare ou normal(Selectionner 1, 2, 3)");
			choice = Integer.parseInt(reader.readLine());
		} while (choice != 1 && choice != 2 && choice != 3);
		return choice;
	}

	 /**
   * Validates the player's card choices by ensuring all choices are integers between 0 and 4.
   *
   * @param choice the array of card choices to validate
   * @return true if all choices are valid, false otherwise
   */
	private boolean isValidChoice(String[] choice) {
		try {
			for (String c : choice) {
				int number = Integer.parseInt(c);
				if (number < 0 || number > 4) {
					return false;
				}
			}
		}catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	 /**
   * Prompts the user to choose cards for different animals (fox, salmon, bear, elk, buzzard),
   * ensuring the input is valid.
   *
   * @param context the application context, used for managing the display and input events
   * @param width the width of the terminal screen
   * @param height the height of the terminal screen
   * @return a list of integers representing the card choices
   * @throws NumberFormatException if the input is not a valid integer
   * @throws IOException if there is an issue reading the input
   */
	@Override
	public List<Integer> choiceOfCards(ApplicationContext context, int width, int height) throws NumberFormatException, IOException {
		Objects.requireNonNull(context, "context is null");
		var reader = new BufferedReader(new InputStreamReader(System.in));
		String[] choice;
		do {
			System.out.println("Choisissez une carte de décompte pour le renard, le saumon, l'ours, le wapiti et le buse (ex : 1 2 3 4 4)");
			choice = reader.readLine().split(" ");
		} while (choice.length != 5 || !isValidChoice(choice));
		List<Integer> cardsMap = List.of(Integer.parseInt(choice[0]), Integer.parseInt(choice[0]), Integer.parseInt(choice[0]), Integer.parseInt(choice[0]), Integer.parseInt(choice[0]));
		return cardsMap;
	}

  /**
   * Handles the actions for the fourth menu. This implementation is a placeholder and does not do anything.
   *
   * @param context the application context, used for managing the display and input events
   * @param width the width of the terminal screen
   * @param height the height of the terminal screen
   */
	@Override
	public void handleFourthMenu(ApplicationContext context, int width, int height) {
	}

}
