package fr.uge.version.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Biome;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;
import fr.uge.game.element.Tile;


/**
 * A class that implements the `DrawTiles` interface and provides methods for drawing square-shaped tiles
 * and related game elements such as habitats, players, and animals on the game board.
 * This class handles the specific drawing logic for square tiles, including player habitats and tile animals.
 * 
 * @param width The width of the game board.
 * @param height The height of the game board.
 */
public record DrawSquareTiles(int width, int height) implements DrawTiles {
	
	/**
   * Validates that the provided width and height are non-negative.
   *
   * @param width The width of the game board.
   * @param height The height of the game board.
   * @throws IllegalArgumentException If the width or height is negative.
   */
	public DrawSquareTiles {
		if (width < 0) {
			throw new IllegalArgumentException("width < 0");
		}
		if (height < 0) {
			throw new IllegalArgumentException("height < 0");
		}
	}

	/**
   * Retrieves the color corresponding to the provided biome type.
   *
   * @param biome The biome type to get the color for.
   * @return The color associated with the biome type.
   */
	private static Color getBiomeColor(Biome biome) {
		switch (biome) {
			case MOUNTAIN:
				return new Color(139, 137, 112);
			case MEADOW:
				return new Color(245, 216, 15);
			case SWAMP:
				return new Color(101, 241, 20);
			case RIVER:
				return new Color(18, 199, 253);
			case FOREST:
				return new Color(7, 137, 8);
			default:
				return Color.BLACK;
		}
	}

	/**
   * Draws the habitat on the game board. This includes drawing the biome and the animals present in the habitat.
   *
   * @param graphics The graphics context to use for drawing.
   * @param habitatWidth The width of the habitat to be drawn.
   * @param habitatHeight The height of the habitat to be drawn.
   * @param spaceWidth The horizontal offset for positioning the habitat.
   * @param spaceHeight The vertical offset for positioning the habitat.
   * @param habitat The habitat to be drawn.
   */
	private void graphicHabitat(Graphics2D graphics, int habitatWidth, int habitatHeight, int spaceWidth, int spaceHeight, Habitat habitat) {
		graphics.setColor(getBiomeColor(habitat.tile().biome().get(0)));
		graphics.fill(new Rectangle2D.Float(width / 4 + spaceWidth, height / 10 + spaceHeight, habitatWidth, habitatHeight));
		graphics.setColor(Color.BLACK);
		graphics.drawRect(width / 4 + spaceWidth, height / 10 + spaceHeight, habitatWidth, habitatHeight);
		if (habitat.animal().equals(Animals.NOTHING)) {
			for (int i = 0; i < 2; i++) {
				graphics.setColor(DisplayForGraphic.getAnimalColor(habitat.tile().animals().get(i)));
				graphics.fill(new Ellipse2D.Float(
						width / 4 + spaceWidth + habitatWidth / 2 - Math.min(habitatWidth, habitatHeight) / 4
								+ (i * Math.min(habitatWidth, habitatHeight) / 4),
						height / 10 + spaceHeight + habitatHeight / 2 - Math.min(habitatWidth, habitatHeight) / 4,
						Math.min(habitatWidth, habitatHeight) / 2, Math.min(habitatWidth, habitatHeight) / 2));
			}
		}else {
			graphics.setColor(DisplayForGraphic.getAnimalColor(habitat.animal()));
			graphics.fill(
					new Ellipse2D.Float(width / 4 + spaceWidth + habitatWidth / 2 - Math.min(habitatWidth, habitatHeight) / 4,
							height / 10 + spaceHeight + habitatHeight / 2 - Math.min(habitatWidth, habitatHeight) / 4,
							Math.min(habitatWidth, habitatHeight) / 2, Math.min(habitatWidth, habitatHeight) / 2));
		}
	}

	/**
   * Draws the player’s board, including their habitats. The habitats are drawn in a grid around the cursor position.
   *
   * @param graphics The graphics context to use for drawing.
   * @param habitatWidth The width of each habitat tile.
   * @param habitatHeight The height of each habitat tile.
   * @param players The list of players in the game.
   * @param index The index of the current player.
   * @param cursor The current cursor position.
   */
	public void drawPlayerBoard(Graphics2D graphics, int habitatWidth, int habitatHeight, ArrayList<Player> players, int index, Coordinate cursor) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(players, "players is null");
		Objects.requireNonNull(cursor, "cursor is null");
		int spaceWidth, spaceHeight = 0;
		for (int j = cursor.y() - 5; j <= cursor.y() + 5; j++) {
			spaceWidth = 0;
			for (int i = cursor.x() - 5; i <= cursor.x() + 5; i++) {
				if (players.get(index).habitats().containsKey(new Coordinate(i, j))) {
					graphicHabitat(graphics, habitatWidth, habitatHeight, spaceWidth, spaceHeight,
							players.get(index).habitats().get(new Coordinate(i, j)));
				}
				spaceWidth += habitatWidth;
			}
			spaceHeight += habitatHeight;
		}
	}

	/**
   * Draws a batch of animals on a tile. The position of the animals is determined based on the tile's size and index.
   *
   * @param graphics The graphics context to use for drawing.
   * @param animal The animal to be drawn.
   * @param spaceWidth The horizontal offset for positioning the animal.
   * @param spaceHeight The vertical offset for positioning the animal.
   * @param tileWidth The width of the tile where the animal is placed.
   * @param tileHeight The height of the tile where the animal is placed.
   * @param indice The index of the animal batch, used to determine the position.
   */
	private void drawAnimalBatch(Graphics2D graphics, Animals animal, int spaceWidth, int spaceHeight, int tileWidth, int tileHeight, int indice) {
		int animalBatchX;
		if (indice == 0 || indice == 2) {
			animalBatchX = width / 30 + spaceWidth + (tileWidth - Math.min(tileWidth, tileHeight) / 2) / 2 - Math.min(tileWidth, tileHeight) / 2;
		} else {
			animalBatchX = width / 9 - width / 200 + spaceWidth + (tileWidth - Math.min(tileWidth, tileHeight) / 2) / 2 - Math.min(tileWidth, tileHeight) / 2;
		}
		graphics.setColor(DisplayForGraphic.getAnimalColor(animal));
		graphics.fill(new Ellipse2D.Float(animalBatchX, height / 6 + spaceHeight + (tileHeight - Math.min(tileWidth, tileHeight) / 2) / 2,
				Math.min(tileWidth, tileHeight) / 2, Math.min(tileWidth, tileHeight) / 2));
	}

	/**
   * Draws a tile on the game board with the associated animals. The animals are drawn based on the position 
   * within the tile.
   *
   * @param graphics The graphics context to use for drawing.
   * @param tile The tile to be drawn.
   * @param animal The animal associated with the tile.
   * @param spaceWidth The horizontal offset for positioning the tile.
   * @param spaceHeight The vertical offset for positioning the tile.
   * @param tileWidth The width of the tile.
   * @param tileHeight The height of the tile.
   * @param indice The index used to calculate animal batch positions.
   */
	public void graphicTile(Graphics2D graphics, Tile tile, Animals animal, int spaceWidth, int spaceHeight, int tileWidth, int tileHeight, int indice) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(tile, "tile is null");
		Objects.requireNonNull(animal, "animal is null");
		graphics.setColor(getBiomeColor(tile.biome().get(0)));
		graphics.fill(new Rectangle2D.Float(width / 20 + spaceWidth, height / 6 + spaceHeight, tileWidth, tileHeight));
		graphics.setColor(Color.BLACK);
		graphics.drawRect(width / 20 + spaceWidth, height / 6 + spaceHeight, tileWidth, tileHeight);
		for (int i = 0; i < tile.animals().size(); i++) {
			Animals animalOfTile = tile.animals().get(i);
			graphics.setColor(DisplayForGraphic.getAnimalColor(animalOfTile));
			graphics.fill(new Ellipse2D.Float(
					width / 20 + spaceWidth + (tileWidth - Math.min(tileWidth, tileHeight) / 2) / 2
							+ (i % 2) * Math.min(tileWidth, tileHeight) / 4,
					height / 6 + spaceHeight + tileHeight / 2 - Math.min(tileWidth, tileHeight) / 4
							+ (i / 2) * Math.min(tileWidth, tileHeight) / 4,
					Math.min(tileWidth, tileHeight) / 2, Math.min(tileWidth, tileHeight) / 2));
		}
		drawAnimalBatch(graphics, animal, spaceWidth, spaceHeight, tileWidth, tileHeight, indice);
	}

	 /**
   * Draws a tile being added to the game board with a specified rotation.
   *
   * @param graphics The graphics context to use for drawing.
   * @param tile The tile to be drawn on the game board.
   * @param rotation The rotation angle to be applied to the tile.
   */
	public void drawAddTileOnGameBoard(Graphics2D graphics, Tile tile, int rotation) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(tile, "tile is null");
		int tileWidth = (width / 2) / 11;
		int tileHeight = (height - 2 * (height / 10)) / 11;
		graphics.setColor(getBiomeColor(tile.biome().get(0)));
		graphics.fill(new Rectangle2D.Float(width / 12, height / 2 + height / 11, tileWidth, tileHeight));
		graphics.setColor(Color.BLACK);
		graphics.drawRect(width / 12, height / 2 + height / 11, tileWidth, tileHeight);
		for (int i = 0; i < tile.animals().size(); i++) {
			graphics.setColor(DisplayForGraphic.getAnimalColor(tile.animals().get(i)));
			graphics.fill(new Ellipse2D.Float(
					width / 12 + tileWidth / 2 - Math.min(tileWidth, tileHeight) / 4 + (i * Math.min(tileWidth, tileHeight) / 4),
					height / 2 + height / 11 + tileHeight / 2 - Math.min(tileWidth, tileHeight) / 4,
					Math.min(tileWidth, tileHeight) / 2, Math.min(tileWidth, tileHeight) / 2));
		}
	}

}
