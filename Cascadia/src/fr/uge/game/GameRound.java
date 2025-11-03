package fr.uge.game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.random.RandomGenerator;

import com.github.forax.zen.ApplicationContext;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Draw;
import fr.uge.game.element.Player;
import fr.uge.version.Action;
import fr.uge.version.terminal.DisplayForTerminal;
import fr.uge.version.terminal.actionForTerminal.TerminalAction;
import fr.uge.version.Display;
import fr.uge.version.graphic.DisplayForGraphic;
import fr.uge.version.graphic.actionForGraphic.GraphicAction;

/**
 * The `GameRound` class represents a single round in the board game, handling player actions,
 * tile and animal placements, and token usage. It updates the game state and determines the winner.
 */
public class GameRound {
	private Structure structure;
	private Draw draw;
	private final int shapeOfTile;
	
	/**
   * Constructor to initialize a new game round.
   *
   * @param structure The structure of the game, containing players, tiles, and other components.
   * @param draw The draw object that holds available tiles and animals for the game.
   * @param shapeOfTile The shape of the tiles (0 for square, 1 for hexagon, 2 for other shapes).
   * @throws IllegalArgumentException if shapeOfTile is not between 0 and 2.
   */
	public GameRound(Structure structure, Draw draw, int shapeOfTile) {
		this.structure = Objects.requireNonNull(structure, "structure is null");
		this.draw = Objects.requireNonNull(draw, "draw is null");
		this.shapeOfTile = shapeOfTile;
		if (shapeOfTile < 0 || shapeOfTile > 2) {
			throw new IllegalArgumentException("shapeOfTile < 0 or shapeOfTile > 2"); 
		}
	}
	
	/**
   * Adds a tile and an animal to the game board and updates the display.
   *
   * @param display The display object to show the updated game board.
   * @param actionOfPlayer The action of the current player.
   * @param draw The draw object containing available tiles and animals.
   * @param tile The index of the tile to be added.
   * @param animal The index of the animal to be added.
   * @param animalToken The animal tokens available.
   * @throws NumberFormatException if the tile or animal index is invalid.
   * @throws IOException if there is an issue displaying the updated game board.
   */
	private void addOnGameBoard(Display diplay, Action actionOfPlayer, Draw draw, int tile, int animal, HashMap<Animals, Integer> animalToken) throws NumberFormatException, IOException {
		actionOfPlayer.addTileOnGameBoard(draw.tilesForChoice.get(tile - 1));
		diplay.gameBoard();
		actionOfPlayer.addAnimalOnGameBoard(draw.animalsForChoice.get(animal - 1), animalToken);
  }
	
	 /**
   * Updates the game by adding a new tile and animal to the game board and removing the used ones.
   *
   * @param display The display object to update the game visuals.
   * @param actionOfPlayer The action of the current player.
   * @param player The player performing the action.
   * @param tile The tile index to be added to the game board.
   * @param animal The animal index to be added to the game board.
   * @throws NumberFormatException if the tile or animal index is invalid.
   * @throws IOException if there is an issue updating the game display.
   */
	private void updateGame(Display diplay, Action actionOfPlayer, Player player, int tile, int animal) throws NumberFormatException, IOException{
		int randomNumber;
		addOnGameBoard(diplay, actionOfPlayer, draw, tile, animal, structure.animalToken());
		RandomGenerator rand = RandomGenerator.getDefault();
		draw.animalsForChoice.set(animal - 1, draw.randomAnimal());
		randomNumber = rand.nextInt(structure.tiles().size());
		draw.tilesForChoice.set(tile - 1, structure.tiles().get(randomNumber));
		structure.tiles().remove(randomNumber);
	}
	
	/**
   * Handles the token usage for a player during their turn.
   * 
   * If the player chooses to use a token, the method prompts the player to choose an action
   * (use the token for placing a tile and animal or remove the token). 
   *
   * @param display The display object to show any relevant messages or actions.
   * @param action The action object representing the player's actions during the round.
   * @param play A flag indicating whether the player is playing or not.
   * @param player The player who is making the decision.
   * @return true if the player used a token; false otherwise.
   * @throws NumberFormatException if the token choice or other inputs are invalid.
   * @throws IOException if there is an issue displaying the game messages or handling the token.
   */
	private boolean playerUsedToken(Display display, Action action, boolean play, Player player) throws NumberFormatException, IOException {
		int usedToken;
		do {
			usedToken = action.choiceOf();
			if (usedToken == 1 && player.natureToken() != 0) {
				int choiceOfUse = action.choiceOfNatureToken();
				player.subNatureToken();
				
				if (choiceOfUse == 1) {
					var result = action.handleTileAndAnimalChoice();
					updateGame(display, action, player, result.get(0), result.get(1));
					return true;
				} 
				else {
						action.handleTokenRemovalChoice();
				}
				display.messageForToken();
				display.drawToDisplay();
			} else if (usedToken != 1 && player.natureToken() == 0) {
					display.errorMessageForToken();
					break;
			}
		} while (usedToken == 1);
		return false;
	}
	
	/**
   * Determines the winner(s) of the game based on the highest score.
   *
   * @return A string containing the name(s) of the player(s) with the highest score.
   */
	public String winner() {
		var name = new StringBuilder();
		var maximum = -1;
		for (var player : structure.players()) {
			if (player.point() == maximum) {
				name.append(", ").append(player.name());
			}
			if (player.point() > maximum) {
				name = new StringBuilder();
				name.append(player.name());
				maximum = player.point();
			}
		}
		return name.toString();
	}

	/**
   * Executes a round of the game for all players, prompting each player to take their turn
   * and perform actions such as placing tiles, animals, or using tokens.
   *
   * @param context The application context to manage the game interface.
   * @param width The width of the display window.
   * @param height The height of the display window.
   * @param gameTurn The current game turn number.
   * @throws NumberFormatException if invalid input is provided by the player.
   * @throws IOException if there is an issue with the display or handling the player's actions.
   */
	public void roundOfGame(ApplicationContext context, int width, int height, int gameTurn) throws NumberFormatException, IOException {
		Objects.requireNonNull(context, "context is null");
		for (int i = 0; i < structure.players().size(); i++) {
			var players = structure.players();
			Display display = shapeOfTile == 0 ? new DisplayForTerminal(players, draw, i) : new DisplayForGraphic(players, draw, structure, i, context, shapeOfTile, width, height, gameTurn);
			Action action = shapeOfTile == 0  ? new TerminalAction(draw, players.get(i)) : new GraphicAction(draw, players.get(i), context, display, shapeOfTile);
			display.playerBoard();
			action.availableTile();
			var play = false;
			if(shapeOfTile == 2) {
				play = playerUsedToken(display, action, play, players.get(i));
			}
			if(!play) {
				int bach = action.choiceOfBatch();
				updateGame(display, action, players.get(i), bach, bach);
			}
		
		}
	}
}
