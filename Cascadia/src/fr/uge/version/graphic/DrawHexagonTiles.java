package fr.uge.version.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Objects;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Biome;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;
import fr.uge.game.element.Tile;

/**
 * Class for drawing hexagonal tiles on the graphical user interface.
 * Implements the {@link DrawTiles} interface to provide a way to visualize game elements.
 */
public record DrawHexagonTiles(int width, int height) implements DrawTiles {
	
	/**
   * Constructor to create an instance of DrawHexagonTiles with specified width and height.
   *
   * @param width  the width of the area to be drawn.
   * @param height the height of the area to be drawn.
   * @throws IllegalArgumentException if width or height is less than zero.
   */
	public DrawHexagonTiles {
		if (width < 0) {
			throw new IllegalArgumentException("width < 0");
		}
		if (height < 0) {
			throw new IllegalArgumentException("height < 0");
		}
	}

	/**
   * Returns the color associated with a given biome.
   *
   * @param biome the biome to determine the color for.
   * @return the color representing the given biome.
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
   * Draws a biome for a habitat using the hexagonal shape on the graphics object.
   *
   * @param graphics  the graphics object to draw on.
   * @param xPoints   the x-coordinates of the hexagon vertices.
   * @param yPoints   the y-coordinates of the hexagon vertices.
   * @param tile      the tile associated with the habitat.
   * @param rotation  the rotation of the tile (for determining the biome).
   * @param xyIndex   the indices of the points to be used for drawing the polygons.
   */
	private void drawBiomeForHabitat(Graphics2D graphics, int[] xPoints, int[] yPoints, Tile tile, int rotation, int[] xyIndex) {
		int[] xFirstFour = new int[4];
		int[] yFirstFour = new int[4];
		int[] xLastFour = new int[4];
		int[] yLastFour = new int[4];
		for (int i = 0; i < xyIndex.length; i++) {
			if (i < 4) {
				xFirstFour[i] = xPoints[xyIndex[i]];
				yFirstFour[i] = yPoints[xyIndex[i]];
			} else {
				xLastFour[i - 4] = xPoints[xyIndex[i]];
				yLastFour[i - 4] = yPoints[xyIndex[i]];
			}
		}
		graphics.setColor(getBiomeColor(tile.biome().get(rotation < 4 ? 0 : 1)));
		graphics.fillPolygon(xFirstFour, yFirstFour, 4);
		graphics.setColor(getBiomeColor(tile.biome().get(rotation < 4 ? 1 : 0)));
		graphics.fillPolygon(xLastFour, yLastFour, 4);
		graphics.setColor(Color.BLACK);
		graphics.drawPolygon(xFirstFour, yFirstFour, 4);
		graphics.drawPolygon(xLastFour, yLastFour, 4);
	}

	/**
   * Draws the biome for a habitat with proper rotation of the hexagonal tile.
   *
   * @param graphics the graphics object to draw on.
   * @param xPoints  the x-coordinates of the hexagon vertices.
   * @param yPoints  the y-coordinates of the hexagon vertices.
   * @param tile     the tile associated with the habitat.
   * @param rotation the rotation of the tile (for determining the biome).
   */
	private void graphicBiomeForHabitat(Graphics2D graphics, int[] xPoints, int[] yPoints, Tile tile, int rotation) {
		if (tile.biome().size() == 2) {
			if (rotation == 1 || rotation == 4) {
				int[] xyIndex = { 2, 3, 4, 5, 5, 0, 1, 2 };
				drawBiomeForHabitat(graphics, xPoints, yPoints, tile, rotation, xyIndex);
			} else if (rotation == 2 || rotation == 5) {
				int[] xyIndex = { 3, 4, 5, 0, 0, 1, 2, 3 };
				drawBiomeForHabitat(graphics, xPoints, yPoints, tile, rotation, xyIndex);
			} else if (rotation == 3 || rotation == 6) {
				int[] xyIndex = { 4, 5, 0, 1, 1, 2, 3, 4 };
				drawBiomeForHabitat(graphics, xPoints, yPoints, tile, rotation, xyIndex);
			}
		} else {
			graphics.setColor(getBiomeColor(tile.biome().get(0)));
			graphics.fillPolygon(xPoints, yPoints, 6);
			graphics.setColor(Color.BLACK);
			graphics.drawPolygon(xPoints, yPoints, 6);
		}
	}

	/**
   * Draws an animal on the hexagonal tile.
   *
   * @param graphics   the graphics object to draw on.
   * @param tile       the tile associated with the animal.
   * @param animalOfTile the animal to be drawn.
   * @param tileWidth  the width of the tile.
   * @param tileHeight the height of the tile.
   * @param centerX    the x-coordinate of the tile's center.
   * @param centerY    the y-coordinate of the tile's center.
   * @param i          the index of the animal to be drawn.
   */
	private void drawAnimal(Graphics2D graphics, Tile tile, Animals animalOfTile, int tileWidth, int tileHeight, int centerX, int centerY, int i) {
		graphics.setColor(DisplayForGraphic.getAnimalColor(animalOfTile));
		float circleDiameter = Math.min(tileWidth, tileHeight) / 3;
		float totalWidth = tile.animals().size() * circleDiameter;
		float startX = centerX - totalWidth / 2;
		float animalX = startX + i * circleDiameter;
		float animalY = centerY - circleDiameter / 2;
		graphics.fill(new Ellipse2D.Float(animalX, animalY, circleDiameter, circleDiameter));
	}

	/**
   * Draws a habitat using hexagonal geometry for the player board.
   *
   * @param graphics    the graphics object to draw on.
   * @param habitatWidth the width of the habitat.
   * @param habitatHeight the height of the habitat.
   * @param spaceWidth   the horizontal spacing.
   * @param spaceHeight  the vertical spacing.
   * @param habitat      the habitat to be drawn.
   */
	private void graphicHexagonHabitat(Graphics2D graphics, int habitatWidth, int habitatHeight, int spaceWidth, int spaceHeight, Habitat habitat) {
		int centerX = width / 4 + spaceWidth + habitatWidth / 2;
		int centerY = height / 10 + spaceHeight + habitatHeight / 2;
		int[] xPoints = new int[6];
		int[] yPoints = new int[6];
		for (int i = 0; i < 6; i++) {
			xPoints[i] = (int) (centerX + habitatWidth / 2 * Math.cos(Math.toRadians(60 * i - 30)));
			yPoints[i] = (int) (centerY + habitatHeight / 2 * Math.sin(Math.toRadians(60 * i - 30)));
		}
		graphicBiomeForHabitat(graphics, xPoints, yPoints, habitat.tile(), habitat.rotation());
		if (habitat.animal().equals(Animals.NOTHING)) {
			for (int i = 0; i < habitat.tile().animals().size(); i++) {
				drawAnimal(graphics, habitat.tile(), habitat.tile().animals().get(i), habitatWidth, habitatHeight, centerX, centerY, i);
			}
		} else {
			graphics.setColor(DisplayForGraphic.getAnimalColor(habitat.animal()));
			float circleDiameter = Math.min(habitatWidth, habitatHeight) / 3 * 1.5f;
			graphics.fill(new Ellipse2D.Float(centerX - circleDiameter / 2, centerY - circleDiameter / 2, circleDiameter,
					circleDiameter));
		}
	}

	/**
   * Draws the player board, showing a range of habitats around the current cursor position.
   *
   * @param graphics the graphics object to draw on.
   * @param habitatWidth the width of the habitat.
   * @param habitatHeight the height of the habitat.
   * @param players the list of players.
   * @param index the index of the current player.
   * @param cursor the current cursor position.
   */
	public void drawPlayerBoard(Graphics2D graphics, int habitatWidth, int habitatHeight, ArrayList<Player> players, int index, Coordinate cursor) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(players, "players is null");
		Objects.requireNonNull(cursor, "cursor is null");
		int spaceWidth, spaceHeight = 0;
		for (int j = cursor.y() - 7; j <= cursor.y() + 6; j++) {
			spaceWidth = j % 2 == 0 ? (width / 2) / 25 : 0;
			for (int i = cursor.x() - 5; i <= cursor.x() + 6; i++) {
				if (players.get(index).habitats().containsKey(new Coordinate(i, j))) {
					System.out.println("x : " + i + " y : " + j);
					graphicHexagonHabitat(graphics, habitatWidth, habitatHeight, spaceWidth, spaceHeight, players.get(index).habitats().get(new Coordinate(i, j)));
				}
				spaceWidth += habitatWidth - width / 180;
			}
			spaceHeight += habitatHeight - height / 50;
		}
	}

	/**
	 * Draws a batch of animals
	 *
	 * @param graphics the graphics object used to draw the animals on the game board.
	 * @param animal the type of animal to be drawn.
	 * @param spaceWidth the horizontal offset where the animal batch is drawn.
	 * @param spaceHeight the vertical offset where the animal batch is drawn.
	 * @param tileWidth the width of the tile, used to calculate the animal's size and positioning.
	 * @param tileHeight the height of the tile, used to calculate the animal's size and positioning.
	 * @param indice the index used to adjust the horizontal positioning of the animal batch.
	 */
	private void drawAnimalBatch(Graphics2D graphics, Animals animal, int spaceWidth, int spaceHeight, int tileWidth, int tileHeight, int indice) {
		int animalBatchX;
		if (indice == 0 || indice == 2) {
			animalBatchX = width / 30 + spaceWidth + (tileWidth - Math.min(tileWidth, tileHeight) / 2) / 2 - Math.min(tileWidth, tileHeight) / 2;
		} else {
			animalBatchX = width / 9 - width / 200 + spaceWidth + (tileWidth - Math.min(tileWidth, tileHeight) / 2) / 2 - Math.min(tileWidth, tileHeight) / 2;
		}
		graphics.setColor(DisplayForGraphic.getAnimalColor(animal));
		graphics.fill(new Ellipse2D.Float(animalBatchX,
				height / 6 + spaceHeight + (tileHeight - Math.min(tileWidth, tileHeight) / 2) / 2,
				Math.min(tileWidth, tileHeight) / 2, Math.min(tileWidth, tileHeight) / 2));
	}

	/**
   * Draws a single tile on the graphical user interface, displaying its biome and animals.
   *
   * @param graphics the graphics object to draw on.
   * @param tile the tile to be drawn.
   * @param animal the animal to be drawn in the tile.
   * @param spaceWidth the horizontal space for drawing.
   * @param spaceHeight the vertical space for drawing.
   * @param tileWidth the width of the tile.
   * @param tileHeight the height of the tile.
   * @param indice the index for drawing animal batches.
   */
	public void graphicTile(Graphics2D graphics, Tile tile, Animals animal, int spaceWidth, int spaceHeight, int tileWidth, int tileHeight, int indice) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(tile, "tile is null");
		Objects.requireNonNull(animal, "animal is null");
		int centerX = width / 20 + spaceWidth + tileWidth / 2;
		int centerY = height / 6 + spaceHeight + tileHeight / 2;
		int[] xPoints = new int[6];
		int[] yPoints = new int[6];
		for (int i = 0; i < 6; i++) {
			xPoints[i] = (int) (centerX + tileWidth / 2 * Math.cos(Math.toRadians(60 * i - 30)));
			yPoints[i] = (int) (centerY + tileHeight / 2 * Math.sin(Math.toRadians(60 * i - 30)));
		}
		graphicBiomeForHabitat(graphics, xPoints, yPoints, tile, 1);
		for (int i = 0; i < tile.animals().size(); i++) {
			drawAnimal(graphics, tile, tile.animals().get(i), tileWidth, tileHeight, centerX, centerY, i);
		}
		drawAnimalBatch(graphics, animal, spaceWidth, spaceHeight, tileWidth, tileHeight, indice);
	}

	/**
	 * Draws an additional tile on the game board, including its biome and animals, based on the given rotation.
	 * The tile is drawn in a hexagonal shape, with the appropriate size and position calculated dynamically.
	 *
	 * @param graphics the graphics object used to draw the tile on the game board.
	 * @param tile the tile to be drawn, which includes its biome and animals.
	 * @param rotation the rotation angle of the tile, affecting how the biome is oriented.
	 */
	public void drawAddTileOnGameBoard(Graphics2D graphics, Tile tile, int rotation) {
		Objects.requireNonNull(graphics, "graphics is null");
		Objects.requireNonNull(tile, "tile is null");
		int tileWidth = (width / 2) / 11;
		int tileHeight = (height - 2 * (height / 10)) / 11;
		int centerX = width / 12 + tileWidth / 2;
		int centerY = height / 2 + height / 11 + tileHeight / 2;
		int[] xPoints = new int[6];
		int[] yPoints = new int[6];
		for (int i = 0; i < 6; i++) {
			xPoints[i] = (int) (centerX + tileWidth / 2 * Math.cos(Math.toRadians(60 * i - 30)));
			yPoints[i] = (int) (centerY + tileHeight / 2 * Math.sin(Math.toRadians(60 * i - 30)));
		}
		graphicBiomeForHabitat(graphics, xPoints, yPoints, tile, rotation);
		for (int i = 0; i < tile.animals().size(); i++) {
			drawAnimal(graphics, tile, tile.animals().get(i), tileWidth, tileHeight, centerX, centerY, i);
		}
	}

}
