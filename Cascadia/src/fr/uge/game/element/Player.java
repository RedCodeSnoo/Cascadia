package fr.uge.game.element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The Player class represents a player in the game, managing the player's name,
 * habitats, nature tokens, points, and biome points.
 */
public class Player {
	private final String name;
	private final HashMap<Coordinate, Habitat> habitats = new HashMap<Coordinate, Habitat>();
	private int natureToken;
	private int point;
	private final HashMap<String, Integer> biomePoint;

	/**
	 * Constructor for the Player class.
	 * 
	 * @param name The name of the player.
	 * @param natureToken The number of nature tokens the player has.
	 * @param point The current points of the player.
	 * @param biomePoint A map containing the player's biome points.
	 * @throws IllegalArgumentException if natureToken is negative.
	 */
	public Player(String name, int natureToken, int point, HashMap<String, Integer> biomePoint) {
		if (natureToken < 0) {
			throw new IllegalArgumentException("natureToken < 0");
		}
		this.name = Objects.requireNonNull(name, "Name cannot be null");
		this.natureToken = natureToken;
		this.biomePoint = Objects.requireNonNull(biomePoint, "biomePoint cannot be null");
		this.point = point;
	}

	/**
	 * Returns the current points of the player.
	 * 
	 * @return The player's points.
	 */
	public int point() {
		return point;
	}

	/**
	 * Returns the habitat at the specified coordinates (x, y).
	 * 
	 * @param x The x-coordinate of the habitat.
	 * @param y The y-coordinate of the habitat.
	 * @return The habitat at the specified coordinates.
	 */
	public Habitat getHabitat(int x, int y) {
		return habitats.get(new Coordinate(x, y));
	}

	/**
	 * Checks if the provided coordinates are within bounds.
	 * 
	 * @param x The x-coordinate to check.
	 * @param y The y-coordinate to check.
	 * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
	 */
	public void checkIndex(int x, int y) {
		if (x < 0 || x >= 50 || y < 0 || y >= 50) {
			throw new IndexOutOfBoundsException("Coordinates out of bounds");
		}
	}

	/**
	 * Adds a tile to the player's habitat at the specified coordinates.
	 * 
	 * @param tile The tile to add.
	 * @param x The x-coordinate where to add the tile.
	 * @param y The y-coordinate where to add the tile.
	 * @param animalPresent The animal present in the habitat.
	 * @param rotation The rotation of the tile.
	 * @throws NullPointerException if tile or animalPresent is null.
	 * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
	 */
	public void add(Tile tile, int x, int y, Animals animalPresent, int rotation) {
		Objects.requireNonNull(tile, "tile is null");
		Objects.requireNonNull(animalPresent, "animalPresent is null");
		checkIndex(x, y);
		Coordinate coord = new Coordinate(x, y);
		Habitat habitat = new Habitat(tile, animalPresent, rotation);
		habitats.put(coord, habitat);
	}

	/**
	 * Checks if there is any neighboring habitat in a square grid around the given coordinates.
	 * 
	 * @param x The x-coordinate to check.
	 * @param y The y-coordinate to check.
	 * @return True if there is a neighboring habitat, false otherwise.
	 * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
	 */
	public boolean neighbourSquare(int x, int y) {
		checkIndex(x, y);

		return habitats.containsKey(new Coordinate(x + 1, y))
				&& !habitats.get(new Coordinate(x + 1, y)).equals(Habitat.defaultHabitat())
				|| habitats.containsKey(new Coordinate(x - 1, y))
						&& !habitats.get(new Coordinate(x - 1, y)).equals(Habitat.defaultHabitat())
				|| habitats.containsKey(new Coordinate(x, y + 1))
						&& !habitats.get(new Coordinate(x, y + 1)).equals(Habitat.defaultHabitat())
				|| habitats.containsKey(new Coordinate(x, y - 1))
						&& !habitats.get(new Coordinate(x, y - 1)).equals(Habitat.defaultHabitat());
	}

	/**
	 * Checks if there is any neighboring habitat in a hexagonal grid around the given coordinates.
	 * 
	 * @param x The x-coordinate to check.
	 * @param y The y-coordinate to check.
	 * @return True if there is a neighboring habitat, false otherwise.
	 * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
	 */
	public boolean neighbourHexagonal(int x, int y) {
		checkIndex(x, y);
		if (y % 2 != 0) {
			return habitats.containsKey(new Coordinate(x, y - 1))
					&& !habitats.get(new Coordinate(x, y - 1)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x + 1, y))
							&& !habitats.get(new Coordinate(x + 1, y)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x, y + 1))
							&& !habitats.get(new Coordinate(x, y + 1)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x - 1, y + 1))
							&& !habitats.get(new Coordinate(x - 1, y + 1)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x - 1, y))
							&& !habitats.get(new Coordinate(x - 1, y)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x - 1, y - 1))
							&& !habitats.get(new Coordinate(x - 1, y - 1)).equals(Habitat.defaultHabitat());
		} else {
			return habitats.containsKey(new Coordinate(x + 1, y - 1))
					&& !habitats.get(new Coordinate(x + 1, y - 1)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x + 1, y))
							&& !habitats.get(new Coordinate(x + 1, y)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x + 1, y + 1))
							&& !habitats.get(new Coordinate(x + 1, y + 1)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x, y + 1))
							&& !habitats.get(new Coordinate(x, y + 1)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x - 1, y))
							&& !habitats.get(new Coordinate(x - 1, y)).equals(Habitat.defaultHabitat())
					|| habitats.containsKey(new Coordinate(x, y - 1))
							&& !habitats.get(new Coordinate(x, y - 1)).equals(Habitat.defaultHabitat());
		}
	}

	/**
	 * Returns the map containing the player's biome points.
	 * 
	 * @return A map containing the player's biome points.
	 */
	public HashMap<String, Integer> getHashMapBiomePoint() {
		return this.biomePoint;
	}

	/**
	 * Adds or updates a biome point for the player.
	 * 
	 * @param key The biome name.
	 * @param value The point value to associate with the biome.
	 * @throws NullPointerException if the key is null.
	 */
	public void putHashMapBiomePoint(String key, int value) {
		Objects.requireNonNull(key, "key is null");
		this.biomePoint.put(key, value);
	}

	/**
	 * Returns the habitat at the given coordinate.
	 * 
	 * @param coordinate The coordinate to get the habitat at.
	 * @return The habitat at the given coordinate.
	 * @throws NullPointerException if the coordinate is null.
	 */
	public Habitat getHabitat(Coordinate coordinate) {
		Objects.requireNonNull(coordinate, "coordinate is null");
		return habitats.get(coordinate);
	}

	/**
	 * Returns the name of the player.
	 * 
	 * @return The player's name.
	 */
	public String name() {
		return name;
	}

	/**
	 * Returns the current number of nature tokens the player has.
	 * 
	 * @return The number of nature tokens the player has.
	 */
	public int natureToken() {
		return natureToken;
	}

	/**
	 * Returns the map of habitats the player has.
	 * 
	 * @return A map of habitats with coordinates as keys.
	 */
	public HashMap<Coordinate, Habitat> habitats() {
		return habitats;
	}

	/**
	 * Compares this player to another object for equality.
	 * 
	 * @param o The object to compare to.
	 * @return True if the object is a Player and has the same name as this one, false otherwise.
	 * @throws NullPointerException if the provided object is null.
	 */
	@Override
	public boolean equals(Object o) {
		Objects.requireNonNull(o, "o is null");
		return o instanceof Player player && this.name.equals(player.name);
	}

	/**
	 * Adds points to the player's current score.
	 * 
	 * @param value The number of points to add.
	 */
	public void pointAdd(int value) {
		this.point = this.point + value;
	}

	/**
	 * Increases the player's nature tokens based on the tile's biome.
	 * 
	 * @param tile The tile to check for biome size.
	 */
	public void addNatureToken(Tile tile) {
		Objects.requireNonNull(tile, "tile is null");
		if (tile.biome().size() == 1) {
			natureToken++;
		}
	}

	/**
	 * Decreases the player's nature token count by 1.
	 */
	public void subNatureToken() {
		natureToken--;
	}

	/**
	 * Returns a string representation of the player, including their name, nature tokens,
	 * and habitats.
	 * 
	 * @return A string representing the player's information.
	 */
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("Player: ").append(name).append(", Nature Tokens: ").append(natureToken).append("\n");

		for (Map.Entry<Coordinate, Habitat> entry : habitats.entrySet()) {
			if (!entry.getValue().equals(Habitat.defaultHabitat())) {
				builder.append("Coordinate ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
			}
		}
		return builder.toString();
	}
}
