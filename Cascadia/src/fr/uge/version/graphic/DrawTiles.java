package fr.uge.version.graphic;

import java.awt.Graphics2D;
import java.util.ArrayList;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Player;
import fr.uge.game.element.Tile;

/**
 * This interface represents the general behavior for drawing tiles in a game. It is used for drawing game elements 
 * such as tiles and animals on the game board. It is implemented by specific classes that handle the drawing 
 * of square and hexagonal tiles.
 * 
 * @see DrawSquareTiles
 * @see DrawHexagonTiles
 */
public sealed interface DrawTiles permits DrawSquareTiles, DrawHexagonTiles {

	 /**
   * Draws the player's board, which may include the habitat and a cursor indicating the selected position.
   * The drawing is performed within the defined area based on the habitat's width and height.
   * 
   * @param graphics The graphical context used for drawing on the screen.
   * @param habitatWidth The width of the player's habitat.
   * @param habitatHeight The height of the player's habitat.
   * @param players The list of players playing in the game.
   * @param index The index of the current player in the players list.
   * @param cursor The cursor's coordinates that indicate the selected position on the board.
   */
	public void drawPlayerBoard(Graphics2D graphics, int habitatWidth, int habitatHeight, ArrayList<Player> players, int index, Coordinate cursor);
	
	/**
   * Draws a tile on the game board, which can also include an animal on that tile. The drawing is performed
   * according to the specified space and tile dimensions.
   * 
   * @param graphics The graphical context used for drawing on the screen.
   * @param tile The tile to be drawn.
   * @param animal The animal associated with the tile, if any.
   * @param spaceWidth The width of the space where the tile is placed.
   * @param spaceHeight The height of the space where the tile is placed.
   * @param tileWidth The width of the tile itself.
   * @param tileHeight The height of the tile itself.
   * @param indice The index for the tile to indicate its position or state.
   */
	public void graphicTile(Graphics2D graphics, Tile tile, Animals animal, int spaceWidth, int spaceHeight, int tileWidth, int tileHeight, int indice);
	
	/**
   * Draws a tile being added to the game board, with a specified rotation applied to the tile.
   * 
   * @param graphics The graphical context used for drawing on the screen.
   * @param tile The tile to be added to the game board.
   * @param rotation The rotation angle to be applied to the tile.
   */
	public void drawAddTileOnGameBoard(Graphics2D graphics, Tile tile, int rotation);
}
