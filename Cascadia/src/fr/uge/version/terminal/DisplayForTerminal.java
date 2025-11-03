package fr.uge.version.terminal;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Objects;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Draw;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;
import fr.uge.game.element.Tile;
import fr.uge.version.Display;

/**
 * This class represents the display for the game in a terminal environment. It
 * handles the rendering of the player's game board, the game state, and manages
 * the interactions between the player and the game interface.
 * 
 * It implements the {@link Display} interface for terminal-based visual output.
 */
public class DisplayForTerminal implements Display {
	private ArrayList<Player> players;
	private final Draw draw;
	private final int index;

	/**
	 * Constructor for the DisplayForTerminal class.
	 * 
	 * @param players The list of players in the game.
	 * @param draw The object representing the deck of the game.
	 * @param index The index of the current player.
	 * @throws IllegalArgumentException If the index is less than 0.
	 * @throws NullPointerException If either players or draw is null.
	 */
	public DisplayForTerminal(ArrayList<Player> players, Draw draw, int index) {
		this.players = Objects.requireNonNull(players, "player is null");
		this.draw = Objects.requireNonNull(draw, "draw is null");
		if (index < 0) {
			throw new IllegalArgumentException("index < 0");
		}
		this.index = index;
	}

	/**
	 * Initializes the counter array used for rendering the game board.
	 * 
	 * @param counter The array to be initialized.
	 * @throws NullPointerException If the counter is null.
	 */
	private void initializeCounterArray(ArrayList<Integer> counter) {
		Objects.requireNonNull(counter, "counter is null");
		for (int i = 0; i < 50; i++) {
			counter.add(0);
		}
	}

	/**
	 * Initializes the space counter for each coordinate on the game board.
	 * 
	 * @param counter The counter array to be initialized.
	 */
	private void initializeCounterSpace(ArrayList<Integer> counter) {
		initializeCounterArray(counter);
		var min = Integer.MAX_VALUE;
		for (int y = 0; y < 50; y++) {
			for (int x = 0; x < 50; x++) {
				Coordinate coord = new Coordinate(x, y);
				Habitat habitat = players.get(index).habitats().getOrDefault(coord, Habitat.defaultHabitat());
				if (!habitat.equals(Habitat.defaultHabitat())) {
					counter.set(y, x);
					min = Math.min(min, x);
					break;
				}
			}
		}
		for (int i = 0; i < counter.size(); i++) {
			counter.set(i, (counter.get(i) - min + 1) * 26);
		}
	}

	/**
   * Appends spaces to the builder based on the counter value for the given row.
   * 
   * @param builder The StringBuilder to append to.
   * @param counter The counter array.
   * @param y The current row.
   */
	private void stringBuilderSpace(StringBuilder builder, ArrayList<Integer> counter, int y) {
		for (var i = 0; i < counter.get(y); i++) {
			builder.append(" ");
		}
	}

	 /**
   * Adds either spaces or a habitat string to the builder based on the habitat at the given coordinates.
   * 
   * @param builder The StringBuilder to append to.
   * @param habitat The habitat at the given coordinates.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   * @param condition The current state of the builder.
   * @return The updated condition value.
   */
	private int stringBuilderSpaceOrHabitat(StringBuilder builder, Habitat habitat, int x, int y, int condition) {
		players.get(index).checkIndex(x, y);
		if (!(habitat.equals(Habitat.defaultHabitat()))) {
			builder.append(habitat).append("/ (").append(x).append(", ").append(y).append(")|");
			return 1;
		} else if (condition == 1) {
			builder.append("                          ");
		}
		return condition;
	}

	/**
   * Renders the game board by displaying the habitats and their coordinates.
   */
	@Override
	public void gameBoard() {
		var builder = new StringBuilder();
		builder.append("Game Board:\n\n");
		ArrayList<Integer> counter = new ArrayList<>(50);
		initializeCounterSpace(counter);
		int condition = 0;
		for (int y = 0; y < 50; y++) {
			stringBuilderSpace(builder, counter, y);
			condition = 0;
			for (int x = 0; x < 50; x++) {
				Coordinate coord = new Coordinate(x, y);
				Habitat habitat = players.get(index).habitats().getOrDefault(coord, Habitat.defaultHabitat());
				condition = stringBuilderSpaceOrHabitat(builder, habitat, x, y, condition);
			}
			if (condition == 1) {
				builder.append("\n");
			}
		}
		System.out.println(builder.toString());
	}

	/**
   * Displays the current player's board, including the game board and nature tokens.
   */
	@Override
	public void playerBoard() {
		System.out.println(players.get(index).name() + " à votre tours de jouer ! Voici votre plateaux:\n");
		gameBoard();
		System.out.println(draw);
		System.out.println("Jetons nature : " + players.get(index).natureToken());
	}

	 /**
   * Displays the results of the game, including each player's points and the winner(s).
   * 
   * @param winnersName The name(s) of the winner(s).
   */
	@Override
	public void resultOfGame(String winnersName) {
		Objects.requireNonNull(winnersName, "winnersName is null");
		for (var player : players) {
			System.out.println("Point de  " + player.name() + " : " + player.point());
		}
		System.out.println("Le ou les gagnant sont : " + winnersName);
	}

	 /**
   * Displays an error message when the player doesn't have enough nature tokens.
   */
	@Override
	public void errorMessageForToken() {
		System.out.println("Vous n'avez pas assez de jeton nature");
	}

	/**
   * Displays the current number of nature tokens for the player.
   */
	@Override
	public void messageForToken() {
		System.out.println("Jetons nature : " + players.get(index).natureToken());
	}

	/**
   * Displays the current state of the draw (deck of animals).
   */
	@Override
	public void drawToDisplay() {
		System.out.println("Jetons nature : " + players.get(index).natureToken());
		System.out.println(draw);
	}

	
	@Override
	public void drawPrompt(Graphics2D graphics) {
	}

	@Override
	public int height() {
		return 0;
	}

	@Override
	public int width() {
		return 0;
	}

	@Override
	public Coordinate cursor() {
		return new Coordinate(25, 25);
	}

	@Override
	public void drawChoiceOf(Graphics2D graphics) {
	}

	@Override
	public void drawChoiceOfNatureToken(Graphics2D graphics) {
	}

	@Override
	public void drawChooseTokenToRemove(Graphics2D graphics) {
	}

	@Override
	public void drawHandleTokenRemovalChoice(Graphics2D graphics) {
	}

	@Override
	public void drawChoiceOfBatch(Graphics2D graphics, int choice) {
	}

	@Override
	public void drawAddTileOnGameBoard(Graphics2D graphics, Tile tile, int[] rotation) {
	}

	@Override
	public void drawAddAnimalOnGameBoard(Graphics2D graphics, Animals animals) {
	}

	@Override
	public void drawAddAnimal(Graphics2D graphics, Animals animal) {
	}

	@Override
	public void changeCursor(int direction) {
	}
}
