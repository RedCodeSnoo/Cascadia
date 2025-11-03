package fr.uge.version.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.github.forax.zen.ApplicationContext;

import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Draw;
import fr.uge.game.Menu;
import fr.uge.game.Structure;
import fr.uge.game.element.Animals;
import fr.uge.game.element.Player;
import fr.uge.game.element.Tile;
import fr.uge.version.Display;
import fr.uge.version.graphic.actionForGraphic.GraphicAction;

/**
 * This class represents a graphical display for the game, handling the drawing of various game elements such as the menu, players, animals, and other graphical elements.
 */
public class DisplayForGraphic implements Display {

	private final ArrayList<Player> players;
	private final int index;
	private final Draw draw;
	private final Structure structure;
	private final ApplicationContext context;
	private final int width;
	private final int height;
	private final int choiceOfGame;
	private final int gameTurn;
	private Coordinate cursor = new Coordinate(25, 25);

	/**
   * Constructs a DisplayForGraphic object with the specified parameters.
   *
   * @param players the list of players in the game.
   * @param draw the Draw instance representing the draw of the game
   * @param structure the structure object containing game data.
   * @param index the index of the current player.
   * @param context the application context.
   * @param choiceOfGame the choice of the game mode.
   * @param width the width of the display.
   * @param height the height of the display.
   * @param gameTurn the current turn in the game.
   * @throws IllegalArgumentException if the index is less than 0.
   * @throws NullPointerException if any of the parameters are null.
   */
	public DisplayForGraphic(ArrayList<Player> players, Draw draw, Structure structure, int index, ApplicationContext context, int choiceOfGame, int width, int height, int gameTurn) {
		this.players = Objects.requireNonNull(players, "player is null");
		this.draw = Objects.requireNonNull(draw, "draw is null");
		this.structure = Objects.requireNonNull(structure, "structure is null");
		if (index < 0) {
			throw new IllegalArgumentException("index < 0");
		}
		this.index = index;
		this.context = context;
		this.width = width;
		this.height = height;
		this.choiceOfGame = choiceOfGame;
		this.gameTurn = gameTurn;
	}

	/**
   * Draws the choice of the number of players on the screen based on the selected number.
   *
   * @param graphics the graphics object to draw the elements.
   * @param width the width of the screen.
   * @param height the height of the screen.
   * @param numberOfPlayers the number of players selected.
   */
	private static void choiceOfNumberOfPlayers(Graphics2D graphics, int width, int height, int numberOfPlayers) {
		switch (numberOfPlayers) {
		case 1 -> {
			graphics.fill(new Rectangle2D.Float(width / 11 + width / 192, (height / 3) - height / 108, width / 13 + width / 96, height / 11 + height / 54));
		}
		case 2 -> {
			graphics.fill(new Rectangle2D.Float(width / 5 - width / 192, (height / 3) - height / 108, width / 13 + width / 96, height / 11 + height / 54));
		}
		case 3 -> {
			graphics.fill(new Rectangle2D.Float(width / 11 + width / 192, (height / 2) - height / 108, width / 13 + width / 96, height / 11 + height / 54));
		}
		case 4 -> {
			graphics.fill(new Rectangle2D.Float(width / 5 - width / 192, (height / 2) - height / 108, width / 13 + width / 96, height / 11 + height / 54));
		}
		default -> {  }
		}
	}

	/**
   * Draws the choice of point mode on the screen based on the selected mode.
   *
   * @param graphics the graphics object to draw the elements.
   * @param width the width of the screen.
   * @param height the height of the screen.
   * @param pointMode the selected point mode (e.g., family, normal, intermediate).
   */
	private static void choiceOfPointMode(Graphics2D graphics, int width, int height, int pointMode) {
		switch (pointMode) {
		case 1 -> {
			graphics.fill(new Rectangle2D.Float(width - width / 3 - width / 192, (height / 3) - height / 108, width / 5 + width / 96, height / 14 + height / 54));
		}
		case 2 -> {
			graphics.fill(new Rectangle2D.Float(width - width / 3 - width / 192, (height / 3) + height / 7 - height / 108, width / 5 + width / 96, height / 14 + height / 54));
		}
		case 3 -> {
			graphics.fill(new Rectangle2D.Float(width - width / 3 - width / 192, (height / 3) + (height / 7) * 2 - height / 108, width / 5 + width / 96, height / 14 + height / 54));
		}
		default -> {  }
		}
	}

	/**
   * Draws the buttons for selecting the point mode (family, normal, intermediate).
   *
   * @param graphics the graphics object to draw the buttons.
   * @param width the width of the screen.
   * @param height the height of the screen.
   */
	private static void buttonForPointMode(Graphics2D graphics, int width, int height) {
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle2D.Float(width - width / 3, (height / 3), width / 5, height / 14));
		graphics.fill(new Rectangle2D.Float(width - width / 3, (height / 3) + height / 7, width / 5, height / 14));
		graphics.fill(new Rectangle2D.Float(width - width / 3, (height / 3) + (height / 7) * 2, width / 5, height / 14));

		graphics.setFont(new Font("Arial", Font.BOLD, width / 60));
		graphics.setColor(Color.WHITE);
		graphics.drawString("Variante Famille", width - width / 3 + width / 35, (height / 3) + height / 19);
		graphics.drawString("Variante Normale", width - width / 3 + width / 35, (height / 3) + height / 3);
		graphics.setFont(new Font("Arial", Font.BOLD, width / 70));
		graphics.drawString("Variante Intermediare", width - width / 3 + width / 130, (height / 3) + height / 6 + height / 48);
	}

	/**
   * Draws the buttons for selecting the number of players.
   *
   * @param graphics the graphics object to draw the buttons.
   * @param width the width of the screen.
   * @param height the height of the screen.
   */
	private static void buttonForNumberOfPlayer(Graphics2D graphics, int width, int height) {
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle2D.Float(width / 11 + width / 96, (height / 3), width / 13, height / 11));
		graphics.fill(new Rectangle2D.Float(width / 5, (height / 3), width / 13, height / 11));
		graphics.fill(new Rectangle2D.Float(width / 11 + width / 96, (height / 2), width / 13, height / 11));
		graphics.fill(new Rectangle2D.Float(width / 5, (height / 2), width / 13, height / 11));

		graphics.setFont(new Font("Arial", Font.BOLD, width / 27));
		graphics.setColor(Color.WHITE);
		graphics.drawString("1", width / 11 + width / 25, (height / 3) + height / 14);
		graphics.drawString("2", width / 5 + width / 35, (height / 3) + height / 14);
		graphics.drawString("3", width / 11 + width / 25, (height / 2) + height / 14);
		graphics.drawString("4", width / 5 + width / 35, (height / 2) + height / 14);
	}

	/**
   * Draws the next or play button on the screen based on the selected point mode.
   *
   * @param graphics the graphics object to draw the button.
   * @param width the width of the screen.
   * @param height the height of the screen.
   * @param pointMode the selected point mode (e.g., family, normal, intermediate).
   */
	private static void drawNextOrPlay(Graphics2D graphics, int width, int height, int pointMode) {
		if (pointMode == 3) {
			graphics.drawString("Suivant", (width / 2 - (width / 5) / 2) + (width / 192) * 6 + (width / 192) / 2, (height / 2 + (height / 11) * 2) + height / 15);
		} else {
			graphics.drawString("Jouer", (width / 2 - (width / 5) / 2) + (width / 192) * 9, (height / 2 + (height / 11) * 2) + height / 15);
		}
	}

	/**
   * Draws the fourth menu on the screen, allowing the user to select the number of players and the game mode.
   *
   * @param graphics the graphics object to draw the menu.
   * @param width the width of the screen.
   * @param height the height of the screen.
   * @param numberOfPlayers the selected number of players.
   * @param pointMode the selected point mode (e.g., family, normal, intermediate).
   */
	public static void drawFourthMenu(Graphics2D graphics, int width, int height, int numberOfPlayers, int pointMode) {
		Objects.requireNonNull(graphics, "graphics is null");
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle2D.Float(width / 2 - (width / 5) / 2, height / 2 + (height / 11) * 2, width / 5, height / 11));
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, (width / 192) * 7));
		drawNextOrPlay(graphics, width, height, pointMode);
		Menu.drawQuitButton(graphics, width, height);
		graphics.setColor(Color.GREEN);
		graphics.drawString("Cascadia", (width / 2 - (width / 5) / 2) + width / 48, (height / 6));
		graphics.setFont(new Font("Arial", Font.BOLD, height / 24));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Nombre de Joueur", width / 11, (height / 4));
		graphics.drawString("Variante du Jeu", width - width / 3 + width / 80, (height / 4));
		graphics.setColor(Color.RED);
		choiceOfNumberOfPlayers(graphics, width, height, numberOfPlayers);
		choiceOfPointMode(graphics, width, height, pointMode);
		buttonForPointMode(graphics, width, height);
		buttonForNumberOfPlayer(graphics, width, height);
	}

	/**
   * Returns the color associated with a specific animal.
   *
   * @param animal the animal for which to return the color.
   * @return the color associated with the animal.
   * @throws NullPointerException if the animal is null.
   */
	public static Color getAnimalColor(Animals animal) {
		Objects.requireNonNull(animal, "animal is null");
		switch (animal) {
		case BEAR:
			return new Color(129, 81, 49);
		case SALMON:
			return new Color(249, 79, 79);
		case FOX:
			return new Color(249, 147, 79);
		case ELK:
			return new Color(225, 176, 144);
		case BUZZARD:
			return new Color(79, 238, 249);
		default:
			return Color.BLACK;
		}
	}

	/**
   * Draws an image from a specified file path on the screen.
   *
   * @param graphics the graphics object to draw the image.
   * @param imagePath the file path to the image.
   * @param x the x-coordinate of the image position.
   * @param y the y-coordinate of the image position.
   * @param width the width of the image.
   * @param height the height of the image.
   */
	private static void drawImage(Graphics2D graphics, String imagePath, int x, int y, int width, int height) {
		try {
			Path path = Paths.get(imagePath);
			if (Files.exists(path)) {
				BufferedImage image = ImageIO.read(path.toFile());
				if (image != null) {
					graphics.drawImage(image, x, y, width, height, null);
				}
			} else {
				System.err.println("Le fichier spécifié n'existe pas : " + imagePath);
			}
		} catch (IOException e) {
			System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
		}
	}

	/**
   * Draws the animal cards on the game interface based on the current wildlife cards.
   *
   * @param graphics the graphics object used for rendering
   */
	private void drawCardsForAnimal(Graphics2D graphics) {
		if (structure.wildlifeCards().size() == 1) {
			drawImage(graphics,
					"image/" + structure.wildlifeCards().get(0).name() + "/" + structure.wildlifeCards().get(0).name() + ".jpg",
					(width / 2) + width / 3 - width / 30, height / 2 - (height / 108) * 20, (width / 192) * 30,
					(height / 108) * 40);
		} else {
			drawImage(graphics,
					"image/" + structure.wildlifeCards().get(0).name() + "/" + structure.wildlifeCards().get(0).name() + "_"
							+ structure.wildlifeCards().get(0).pattern() + ".jpg",
					(width / 2) + width / 4 + width / 100, height / 2 - (height / 108) * 40, (width / 192) * 20,
					(height / 108) * 27);
			drawImage(graphics,
					"image/" + structure.wildlifeCards().get(1).name() + "/" + structure.wildlifeCards().get(1).name() + "_"
							+ structure.wildlifeCards().get(1).pattern() + ".jpg",
					(width / 2) + width / 4 + width / 100 + (width / 192) * 20 + width / 80, height / 2 - (height / 108) * 40,
					(width / 192) * 20, (height / 108) * 27);
			drawImage(graphics,
					"image/" + structure.wildlifeCards().get(2).name() + "/" + structure.wildlifeCards().get(2).name() + "_"
							+ structure.wildlifeCards().get(2).pattern() + ".jpg",
					(width / 2) + width / 4 + width / 100, height / 2 - (height / 108) * 10, (width / 192) * 20,
					(height / 108) * 27);
			drawImage(graphics,
					"image/" + structure.wildlifeCards().get(3).name() + "/" + structure.wildlifeCards().get(3).name() + "_"
							+ structure.wildlifeCards().get(3).pattern() + ".jpg",
					(width / 2) + width / 4 + width / 100 + (width / 192) * 20 + width / 80, height / 2 - (height / 108) * 10,
					(width / 192) * 20, (height / 108) * 27);
			drawImage(graphics,
					"image/" + structure.wildlifeCards().get(4).name() + "/" + structure.wildlifeCards().get(4).name() + "_"
							+ structure.wildlifeCards().get(4).pattern() + ".jpg",
					(width / 2) + width / 4 + width / 100 + (width / 192) * 12, height / 2 + (height / 108) * 20,
					(width / 192) * 20, (height / 108) * 27);
		}
	}

	/**
   * Draws the player's board with tiles representing the habitat and player actions.
   *
   * @param graphics the graphics object used for rendering
   */
	private void drawPlayerBoard(Graphics2D graphics) {
		int habitatWidth = (width / 2) / 11;
		int habitatHeight = (height - 2 * (height / 10)) / 11;
		graphics.setColor(new Color(240, 240, 240));
		graphics.fill(new Rectangle2D.Float(width / 4, height / 10, width / 2, height - 2 * (height / 10)));
		graphics.setColor(Color.BLACK);
		graphics.drawRect(width / 4, height / 10, width / 2, height - 2 * (height / 10));
		DrawTiles PlayerBoard = choiceOfGame == 2 ? new DrawHexagonTiles(width, height) : new DrawSquareTiles(width, height);
		PlayerBoard.drawPlayerBoard(graphics, habitatWidth, habitatHeight, players, index, cursor);
	}

	/**
   * Draws the player's information such as name, current turn, and nature tokens.
   *
   * @param graphics the graphics object used for rendering
   */
	private void drawPlayerInformation(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial", Font.BOLD, height / 24));
		graphics.drawString(players.get(index).name(), (width / 2 - (width / 8) / 2) + width / 48, (height / 15));
		graphics.drawString("Tour : " + (gameTurn + 1), (width / 2 + width / 7), (height / 15));
		graphics.setFont(new Font("Arial", Font.BOLD, height / 36));
		graphics.drawString("Points nature : " + players.get(index).natureToken(), (width / 2 - (width / 8) / 2) + width / 48, height - (height / 15));

	}

	/**
   * Draws the fifth menu, including the player board, player information, and wildlife cards.
   *
   * @param graphics the graphics object used for rendering
   */
	private void drawFifthMenu(Graphics2D graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		drawPlayerBoard(graphics);
		drawPlayerInformation(graphics);
		drawCardsForAnimal(graphics);
	}

	 /**
   * Renders the game board, including both the fifth menu and tile selections.
   */
	@Override
	public void gameBoard() {
		context.renderFrame(graphics -> drawFifthMenu(graphics));
		context.renderFrame(graphics -> drawTile(graphics));
	}

	 /**
   * Renders the player's board along with the game board.
   */
	@Override
	public void playerBoard() {
		gameBoard();
	}

	/**
   * Draws the tile selection area on the GUI, including the available tiles for the current game.
   *
   * @param graphics the graphics object used for rendering
   */
	private void drawTile(Graphics2D graphics) {
		graphics.setColor(new Color(240, 240, 240));
		graphics.fill(new Rectangle2D.Float(width / 100, height / 7, width / 5, height / 4));
		graphics.setColor(Color.BLACK);
		graphics.drawRect(width / 100, height / 7, width / 5, height / 4);
		int spaceWidth = width / 100;
		int spaceHeight = height / 50;
		int tileWidth = (width / 2) / 11;
		int tileHeight = (height - 2 * (height / 10)) / 11;
		for (int i = 0; i < draw.tilesForChoice().size(); i++) {
			if (i % 2 == 0 && i != 0) {
				spaceHeight += tileHeight + height / 50;
				spaceWidth = width / 100;
			}
			DrawTiles drawTile = choiceOfGame == 2 ? new DrawHexagonTiles(width, height) : new DrawSquareTiles(width, height);
			drawTile.graphicTile(graphics, draw.tilesForChoice().get(i), draw.animalsForChoice().get(i), spaceWidth, spaceHeight, tileWidth, tileHeight, i);
			spaceWidth += tileWidth + width / 100;
		}
	}

	/**
	 * Draws the clickable zone on the screen.
	 * This zone is visually represented by a rectangle and is used to show
	 * areas where interaction with the game is possible.
	 *
	 * @param graphics The graphics object used to draw the clickable zone.
	 */
	private void drawClickableZone(Graphics2D graphics) {
		graphics.setColor(new Color(240, 240, 240));
		graphics.fill(new Rectangle2D.Float(width / 100, height / 2, width / 5, height / 4));
		graphics.setColor(Color.BLACK);
		graphics.drawRect(width / 100, height / 2, width / 5, height / 4);
	}

	/**
	 * Draws the button on the screen.
	 *
	 * @param graphics The graphics object used to draw the button.
	 */
	private void drawButton(Graphics2D graphics) {
		graphics.setColor(new Color(200, 255, 200));
		graphics.fill(new Rectangle2D.Float(width / 100 + width / 190, height / 2 + height / 8, width / 12, height / 15));
		graphics.setColor(Color.BLACK);
		graphics.drawRect(width / 100 + width / 190, height / 2 + height / 8, width / 12, height / 15);
		graphics.setColor(new Color(255, 200, 200));
		graphics.fill(new Rectangle2D.Float(width / 100 + width / 190 + width / 12 + width / 50, height / 2 + height / 8, width / 12, height / 15));
		graphics.setColor(Color.BLACK);
		graphics.drawRect(width / 100 + width / 190 + width / 12 + width / 50, height / 2 + height / 8, width / 12, height / 15);
	}

	/**
	 * Draws the prompt asking the player if they want to redraw a card.
	 *
	 * @param graphics The graphics object used to draw the prompt and buttons.
	 */
	@Override
	public void drawPrompt(Graphics2D graphics) {
		Objects.requireNonNull(graphics, "graphics is null");
		drawClickableZone(graphics);
		drawButton(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 70));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Voulez-vous repiocher ?", width / 30, height / 2 + height / 22);
		graphics.drawString("Oui", width / 100 + width / 190 + width / 35, height / 2 + height / 8 + height / 25);
		graphics.drawString("Non", width / 100 + width / 190 + width / 12 + width / 50 + width / 35, height / 2 + height / 8 + height / 25);

	}

	/**
	 * Draws the prompt asking the player if they want to spend a nature token.
	 *
	 * @param graphics The graphics object used to draw the prompt and buttons.
	 */
	@Override
	public void drawChoiceOf(Graphics2D graphics) {
		Objects.requireNonNull(graphics, "graphics is null");
		drawClickableZone(graphics);
		drawButton(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 100));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Voulez-vous dépenser un jeton nature ?", width / 40, height / 2 + height / 22);
		graphics.drawString("Oui", width / 100 + width / 190 + width / 35, height / 2 + height / 8 + height / 25);
		graphics.drawString("Non", width / 100 + width / 190 + width / 12 + width / 50 + width / 35, height / 2 + height / 8 + height / 25);
	}

	/**
	 * Draws the prompt for using a nature token.
	 *
	 * @param graphics The graphics object used to draw the prompt and instructions.
	 */
	@Override
	public void drawChoiceOfNatureToken(Graphics2D graphics) {
		Objects.requireNonNull(graphics, "graphics is null");
		drawClickableZone(graphics);
		drawButton(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 100));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Utilisation du Jeton Nature : ", width / 20, height / 2 + height / 22);
		graphics.drawString("prendre n'importe", width / 100 + width / 200 + width / 180, height / 2 + height / 8 + height / 50);
		graphics.drawString("quel tuiles et", width / 100 + width / 200 + width / 80,	height / 2 + height / 8 + height / 27);
		graphics.drawString("jetons présent", width / 100 + width / 200 + width / 80, height / 2 + height / 8 + height / 18);
		graphics.drawString("enlever autant de", width / 100 + width / 190 + width / 12 + width / 50 + width / 120, height / 2 + height / 8 + height / 30);
		graphics.drawString("jetons souhaiter", width / 100 + width / 190 + width / 12 + width / 50 + width / 120, height / 2 + height / 8 + height / 20);
	}

	/**
	 * Draws the prompt for selecting a batch of tokens to remove.
	 *
	 * @param graphics The graphics object used to draw the prompt.
	 */
	@Override
	public void drawChooseTokenToRemove(Graphics2D graphics) {
		Objects.requireNonNull(graphics, "graphics is null");
		drawClickableZone(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 100));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Selectionner le lot du jeton à supprimer", width / 37, height / 2 + height / 22);
	}

	/**
	 * Draws the prompt asking the player if they want to remove another token.
	 *
	 * @param graphics The graphics object used to draw the prompt and buttons.
	 */
	@Override
	public void drawHandleTokenRemovalChoice(Graphics2D graphics) {
		Objects.requireNonNull(graphics, "graphics is null");
		drawClickableZone(graphics);
		drawButton(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 100));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Voulez-vous enlever un autre jeton faune ?", width / 40, height / 2 + height / 22);
		graphics.drawString("Oui", width / 100 + width / 190 + width / 35, height / 2 + height / 8 + height / 25);
		graphics.drawString("Non", width / 100 + width / 190 + width / 12 + width / 50 + width / 35, height / 2 + height / 8 + height / 25);
	}

	/**
	 * Draws a prompt based on the player's choice for which item they need to select.
	 *
	 * @param graphics The graphics object used to draw the prompt.
	 * @param choice The choice made by the player, which will determine the prompt message.
	 */
	@Override
	public void drawChoiceOfBatch(Graphics2D graphics, int choice) {
		Objects.requireNonNull(graphics, "graphics is null");
		drawClickableZone(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 70));
		graphics.setColor(Color.BLACK);
		switch (choice) {
			case 0 -> {
				graphics.drawString("Choisissez une tuile", width / 20, height / 2 + height / 22);
			}
			case 1 -> {
				graphics.drawString("Choisissez un animal", width / 20, height / 2 + height / 22);
			}
			case 2 -> {
				graphics.drawString("Choisissez un lot", width / 20, height / 2 + height / 22);
			}
			default -> {  }
		}
	}

	/**
	 * Draws a tile on the game board with a prompt.
	 * Depending on the game choice, it uses either hexagonal or square tiles.
	 * 
	 * @param graphics the Graphics2D object to draw on
	 * @param tile the tile to be placed on the game board
	 * @param rotation the rotation of the tile
	 */
	@Override
	public void drawAddTileOnGameBoard(Graphics2D graphics, Tile tile, int[] rotation) {
		Objects.requireNonNull(graphics, "graphics is null");
		drawClickableZone(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 70));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Placer la tuile", width / 16, height / 2 + height / 22);
		DrawTiles drawAddTile = choiceOfGame == 2 ? new DrawHexagonTiles(width, height) : new DrawSquareTiles(width, height);
		drawAddTile.drawAddTileOnGameBoard(graphics, tile, rotation[0]);
	}

	/**
	 * Draws an animal on the game board with a prompt asking the player if they want to place it.
	 * 
	 * @param graphics the Graphics2D object to draw on
	 * @param animal the animal to be placed on the game board
	 */
	@Override
	public void drawAddAnimalOnGameBoard(Graphics2D graphics, Animals animal) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(animal, "animal is null");
		int animalWidth = (width / 2) / 11;
		int animalHeight = (height - 2 * (height / 10)) / 11;
		drawClickableZone(graphics);
		drawButton(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 105));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Voulez-vous placer l'animal sur votre plateau ?", width / 60, height / 2 + height / 60);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 100));
		graphics.drawString("Etes vous sûre de pouvoir le placer ?", width / 30, height / 2 + height / 26);
		graphics.drawString("Oui", width / 100 + width / 190 + width / 35, height / 2 + height / 8 + height / 25);
		graphics.drawString("Non", width / 100 + width / 190 + width / 12 + width / 50 + width / 35, height / 2 + height / 8 + height / 25);
		graphics.setColor(getAnimalColor(animal));
		graphics.fill(new Ellipse2D.Float(width / 12 + animalWidth / 2 - Math.min(animalWidth, animalHeight) / 4,
				height / 2 + height / 20 + animalHeight / 2 - Math.min(animalWidth, animalHeight) / 4,
				Math.min(animalWidth, animalHeight) / 2, Math.min(animalWidth, animalHeight) / 2));
	}

	/**
	 * Draws an animal on the game board without the placement prompt.
	 * 
	 * @param graphics the Graphics2D object to draw on
	 * @param animal the animal to be placed on the game board
	 */
	@Override
	public void drawAddAnimal(Graphics2D graphics, Animals animal) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(animal, "animal is null");
		int animalWidth = (width / 2) / 11;
		int animalHeight = (height - 2 * (height / 10)) / 11;
		drawClickableZone(graphics);
		graphics.setFont(new Font("Arial", Font.PLAIN, width / 70));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Placer l'animal", width / 16, height / 2 + height / 22);
		graphics.setColor(getAnimalColor(animal));
		graphics.fill(new Ellipse2D.Float(width / 12 + animalWidth / 2 - Math.min(animalWidth, animalHeight) / 4,
				height / 2 + height / 20 + animalHeight / 2 - Math.min(animalWidth, animalHeight) / 4,
				Math.min(animalWidth, animalHeight) / 2, Math.min(animalWidth, animalHeight) / 2));
	}

	/**
	 * Changes the position of the cursor based on the given direction.
	 * 
	 * @param caseForCursor the direction in which to move the cursor:
	 */
	@Override
	public void changeCursor(int direction) {
		switch (direction) {
			case 1 -> {
				cursor = new Coordinate(cursor.x(), cursor.y() + 1);
			}
			case 2 -> {
				cursor = new Coordinate(cursor.x() + 1, cursor.y());
			}
			case 3 -> {
				cursor = new Coordinate(cursor.x(), cursor.y() - 1);
			}
			case 4 -> {
				cursor = new Coordinate(cursor.x() - 1, cursor.y());
			}
			default -> { }
		}
	}

	/**
	 * Displays the victory menu after the game ends, showing the winner and the list of players and their points.
	 * 
	 * @param graphics the Graphics2D object to draw on
	 * @param winnersName the name of the winning player
	 */
	private void victoryMenu(Graphics2D graphics, String winnersName) {
		graphics.setFont(new Font("Arial", Font.BOLD, (width / 192) * 7));
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(Color.GREEN);
		graphics.drawString("Cascadia", (width / 2 - (width / 5) / 2) + width / 48, (height / 6));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Le gagnant est le " + winnersName, (width / 2) - width / 5, (height / 2));
		graphics.setFont(new Font("Arial", Font.PLAIN, (width / 192) * 4));
		int lineHeight = (height / 30);
		int x = width / 48;
		int y = height / 2;
		for (Player player : players) {
			String playerInfo = "Joueur : " + player.name() + " - Points : " + player.point();
			graphics.drawString(playerInfo, x, y);
			y += lineHeight;
		}
	}

	/**
	 * Displays the results of the game and shows the winner.
	 * Waits for a click before continuing.
	 * 
	 * @param winnersName the name of the winning player
	 */
	@Override
	public void resultOfGame(String winnersName) {
		Objects.requireNonNull(winnersName, "winnersName is null");
		context.renderFrame(graphics -> victoryMenu(graphics, winnersName));
		GraphicAction.waitClick(context);
	}

	/**
	 * Draws cards of a specific animal on the screen.
	 * 
	 * @param graphics the Graphics2D object to draw on
	 * @param width the width of the screen
	 * @param height the height of the screen
	 * @param animalName the name of the animal whose cards will be drawn
	 */
	private static void drawCards(Graphics2D graphics, int width, int height, String animalName) {
		drawImage(graphics, "image/" + animalName + "/" + animalName + "_1.jpg", (width / 2) - (width / 192) * 65,
				height / 2 - (height / 108) * 20, (width / 192) * 30, (height / 108) * 40);
		drawImage(graphics, "image/" + animalName + "/" + animalName + "_2.jpg", (width / 2) - (width / 192) * 30,
				height / 2 - (height / 108) * 20, (width / 192) * 30, (height / 108) * 40);
		drawImage(graphics, "image/" + animalName + "/" + animalName + "_3.jpg", (width / 2) + (width / 192) * 5,
				height / 2 - (height / 108) * 20, (width / 192) * 30, (height / 108) * 40);
		drawImage(graphics, "image/" + animalName + "/" + animalName + "_4.jpg", (width / 2) + (width / 192) * 40,
				height / 2 - (height / 108) * 20, (width / 192) * 30, (height / 108) * 40);
	}

	/**
	 * Displays a set of cards that the player can choose from.
	 * 
	 * @param graphics the Graphics2D object to draw on
	 * @param width the width of the screen
	 * @param height the height of the screen
	 * @param animalName the name of the animal whose cards will be displayed
	 */
	public static void drawCardsToChoose(Graphics2D graphics, int width, int height, String animalName) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(animalName, "animalName is null");
		graphics.setFont(new Font("Arial", Font.BOLD, (width / 192) * 7));
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		drawCards(graphics, width, height, animalName);
		graphics.setColor(Color.GREEN);
		graphics.drawString("Cascadia", (width / 2 - (width / 5) / 2) + width / 48, (height / 6));
		graphics.setColor(Color.BLACK);
		graphics.drawString("Choisissez une carte decompte", (width / 2) - width / 4, (height / 2) + height / 3);
	}

	
	@Override
	public void messageForToken() {
	}

	@Override
	public void drawToDisplay() {
	}

	@Override
	public void errorMessageForToken() {
	}

	/**
	 * Returns the draw object used for rendering the game board.
	 * 
	 * @return the draw object
	 */
	public Draw draw() {
		return draw;
	}

	/**
	 * Returns the height of the game window or screen.
	 * 
	 * @return the height of the screen
	 */
	@Override
	public int height() {
		return height;
	}

	/**
	 * Returns the width of the game window or screen.
	 * 
	 * @return the width of the screen
	 */
	@Override
	public int width() {
		return width;
	}

	/**
	 * Returns the current cursor position on the game board.
	 * 
	 * @return the current cursor position
	 */
	@Override
	public Coordinate cursor() {
		return cursor;
	}
}