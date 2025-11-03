package fr.uge.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import fr.uge.game.element.Player;
import fr.uge.game.element.Animals;
import fr.uge.game.element.Biome;
import fr.uge.game.element.Tile;
import fr.uge.game.wildlifeCount.Bear;
import fr.uge.game.wildlifeCount.Buzzard;
import fr.uge.game.wildlifeCount.Elk;
import fr.uge.game.wildlifeCount.Family;
import fr.uge.game.wildlifeCount.Fox;
import fr.uge.game.wildlifeCount.Intermediate;
import fr.uge.game.wildlifeCount.Salmon;

/**
 * Core class managing the game's setup, tiles, players, and rules.
 */
public class Structure {
	private final ArrayList<Tile> tiles = new ArrayList<>();
	private final ArrayList<Player> players = new ArrayList<>();
	private final ArrayList<ArrayList<Tile>> startHabitat = new ArrayList<>();
	private final HashMap<Animals, Integer> animalToken = new HashMap<>();
	private final ArrayList<WildlifeCount> wildlifeCards = new ArrayList<>();
	private final int game;
	private final int numberOfPlayers;
	private final int shapeOfTile;

	/**
   * Constructor for the Structure class.
   *
   * @param numberOfPlayers The number of players in the game (between 2 and 4).
   * @param game The game mode (0 to 3).
   * @param shapeOfTile The shape of the tiles (0 = square, 2 = hexagonal).
   * @throws IllegalArgumentException if any parameter is out of its valid range.
   */
	public Structure(int numberOfPlayers, int game, int shapeOfTile) {
		if (game < 0 && game > 3) {
			throw new IllegalArgumentException("game < 0 and game > 3");
		}
		if (numberOfPlayers < 2 && numberOfPlayers > 4) {
			throw new IllegalArgumentException("numberOfPlayers < 2 and numberOfPlayers > 4");
		}
		if (shapeOfTile < 0 && shapeOfTile > 2) {
			throw new IllegalArgumentException("shapeOfTile < 0 and shapeOfTile > 2");
		}
		this.numberOfPlayers = numberOfPlayers;
		this.game = game;
		this.shapeOfTile = shapeOfTile;
	}

	/**
   * Gets the list of tiles in the game.
   *
   * @return An ArrayList of Tile objects.
   */
	public ArrayList<Tile> tiles() {
		return tiles;
	}

	/**
   * Gets the map of animal tokens.
   *
   * @return A HashMap where the key is an Animals enum and the value is the token count.
   */
	public HashMap<Animals, Integer> animalToken() {
		return animalToken;
	}

	/**
   * Adjusts the token count for a specific animal.
   *
   * @param animal The animal whose token count will be adjusted.
   * @param value The value to subtract from the current token count.
   */
	public void addOrSubtractAnimalToken(Animals animal, int value) {
		Objects.requireNonNull(animal, "animal is null");
		if (animalToken.containsKey(animal)) {
			int currentValue = animalToken.get(animal);
			animalToken.put(animal, currentValue - value);
		}
	}

	 /**
   * Gets the list of players in the game.
   *
   * @return An ArrayList of Player objects.
   */
	public ArrayList<Player> players() {
		return players;
	}

	/**
   * Gets the game mode.
   *
   * @return The game mode as an integer.
   */
	public int game() {
		return game;
	}

	/**
   * Gets the number of players in the game.
   *
   * @return The number of players as an integer.
   */
	public int numberOfPlayers() {
		return numberOfPlayers;
	}

	/**
   * Gets the list of wildlife cards.
   *
   * @return An ArrayList of WildlifeCount objects.
   */
	public ArrayList<WildlifeCount> wildlifeCards() {
		return wildlifeCards;
	}

	/**
   * Initializes the animal tokens with their default values.
   * Bear, Salmon, Fox, Elk, and Buzzard start with 20 tokens each.
   */
	private void initializeAnimalToken() {
		animalToken.put(Animals.BEAR, 20);
		animalToken.put(Animals.SALMON, 20);
		animalToken.put(Animals.FOX, 20);
		animalToken.put(Animals.ELK, 20);
		animalToken.put(Animals.BUZZARD, 20);
		animalToken.put(Animals.NOTHING, 0);
	}

	/**
   * Reads tile data from a file and adds it to the tiles list.
   *
   * @param path The path to the file containing tile data.
   * @param tiles The list to populate with tile data.
   * @param reader The BufferedReader used to read the file.
   * @throws IOException If an I/O error occurs while reading the file.
   */
	private void readFile(Path path, List<Tile> tiles, BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			List<Biome> biome = new ArrayList<>();
			List<Animals> animals = new ArrayList<>();
			String[] words = line.split("\\s+");
			var change = false;
			for (String word : words) {
				if (word.equals("|")) {
					change = true;
				} else if (!change) {
					biome.add(Biome.valueOf(word));
				} else {
					animals.add(Animals.valueOf(word));
				}
			}
			var tile = new Tile(animals, biome);
			tiles.add(tile);
		}
	}

	/**
   * Reads all tiles from a given file.
   *
   * @param path The path to the file containing tile data.
   * @return An ArrayList of Tile objects read from the file.
   */
	private ArrayList<Tile> totalOfTiles(Path path) {
		ArrayList<Tile> totalTile = new ArrayList<>();
		try (var reader = Files.newBufferedReader(path)) {
			readFile(path, totalTile, reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return totalTile;
	}

	/**
   * Initializes habitat tiles for hexagonal tiles.
   * Reads tiles from a file and randomly selects a subset based on the number of players.
   */
	private void initializeHabitatHexagonal() {
		Path path = Paths.get("HabitatCards.txt");
		var index = numberOfPlayers * 20 + 3;
		Random random = new Random();
		var totalTile = totalOfTiles(path);
		while (index != 0) {
			var listIndex = random.nextInt(totalTile.size());
			tiles.add(totalTile.get(listIndex));
			totalTile.remove(listIndex);
			index--;
		}
	}

	/**
   * Generates a random number that is different from a given number.
   *
   * @param limit The upper bound (exclusive) for the random number.
   * @param number The number to avoid.
   * @return A random integer between 0 (inclusive) and limit (exclusive), excluding the given number.
   * @throws IllegalArgumentException If the limit is less than the number.
   */
	private static int randomNumber(int limit, int number) {
		if (limit < number) {
			throw new IllegalArgumentException("limit < number");
		}
		Random random = new Random();
		int random1;
		do {
			random1 = random.nextInt(limit);
		} while (random1 == number);
		return random1;
	}

	/**
   * Adds a randomly generated tile to the tiles list based on a biome index.
   *
   * @param index The biome index used to determine the tile's biome.
   * @throws IllegalArgumentException If the index is negative.
   */
	private void tileToAdd(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index < 0");
		}
		List<Animals> animals = List.of(Animals.BEAR, Animals.SALMON, Animals.FOX, Animals.ELK, Animals.BUZZARD);
		List<Biome> biome = List.of(Biome.MOUNTAIN, Biome.FOREST, Biome.MEADOW, Biome.SWAMP, Biome.RIVER);
		Random random = new Random();
		var random1 = random.nextInt(5);
		var random2 = randomNumber(5, random1);
		tiles.add(new Tile(List.of(animals.get(random1), animals.get(random2)), List.of(biome.get(index))));
	}

	/**
   * Creates a list of tiles with random animals and biomes.
   * 
   * @return a list of randomly generated tiles.
   */
	private ArrayList<Tile> createTiles() {
		int randomNumber;
		List<Animals> animals = List.of(Animals.BEAR, Animals.SALMON, Animals.FOX, Animals.ELK, Animals.BUZZARD);
		List<Biome> biome = List.of(Biome.MOUNTAIN, Biome.FOREST, Biome.MEADOW, Biome.SWAMP, Biome.RIVER);
		ArrayList<Tile> tiles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 5; k++) {
					randomNumber = randomNumber(5, k);
					tiles.add(new Tile(List.of(animals.get(k), animals.get(randomNumber)), List.of(biome.get(i))));
				}
			}
			tileToAdd(i);
			tileToAdd(i);
		}
		return tiles;
	}

	/**
   * Initializes habitat tiles for a square-shaped game board.
   * Uses randomly generated tiles to populate the habitat.
   */
	private void initializeHabitatSquare() {
		Random random = new Random();
		var totalTiles = createTiles();
		int numberOfTile = 20 * numberOfPlayers + 3;
		for (int i = 0; i < numberOfTile; i++) {
			int index = random.nextInt(totalTiles.size());
			Tile randomTile = totalTiles.get(index);
			tiles.add(randomTile);
		}
	}

	/**
   * Creates starting habitat tiles for a hexagonal game board from a file.
   * 
   * @throws IOException if there is an error reading the file.
   */
	private void createStartTilesHexagonal() {
		Path path = Paths.get("StartHabitatCards.txt");
		var totalTile = totalOfTiles(path);
		ArrayList<Tile> startHabitToAdd = new ArrayList<>();
		for (int i = 0; i < totalTile.size(); i++) {
			startHabitToAdd.add(totalTile.get(i));
			if ((i + 1) % 3 == 0 || i == totalTile.size() - 1) {
				startHabitat.add(startHabitToAdd);
				startHabitToAdd = new ArrayList<>();
			}
		}
	}

	/**
   * Creates starting habitat tiles for a square game board.
   */
	private void createStartTilesSquare() {
		Random random = new Random();
		int random1;
		int random2;
		List<Animals> animals = List.of(Animals.BEAR, Animals.SALMON, Animals.FOX, Animals.ELK, Animals.BUZZARD);
		List<Biome> biome = List.of(Biome.MOUNTAIN, Biome.FOREST, Biome.MEADOW, Biome.SWAMP, Biome.RIVER);
		for (int i = 0; i < 5; i++) {
			ArrayList<Tile> tileList = new ArrayList<>();
			for (int j = 0; j < 3; j++) {
				random1 = random.nextInt(5);
				random2 = randomNumber(5, random1);
				Tile tile = new Tile(List.of(animals.get(random1), animals.get(random2)), List.of(biome.get(random.nextInt(5))));
				tileList.add(tile);
			}
			startHabitat.add(tileList);
		}
	}

	/**
   * Initializes a HashMap for player scoring based on biomes.
   * 
   * @return a HashMap where keys are biome names, and values are scores (initially 0).
   */
	private HashMap<String, Integer> initializePlayerHashMap() {
		var map = new HashMap<String, Integer>();
		map.put("FOREST", 0);
		map.put("RIVER", 0);
		map.put("SWAMP", 0);
		map.put("MEADOW", 0);
		map.put("MOUNTAIN", 0);
		return map;
	}

	/**
   * Creates player objects and initializes their scores.
   * 
   * @param choice an integer to determine how player names are assigned:
   *               0 = prompt user for names, 
   *               1 = assign default names (Player 1, Player 2, etc.),
   *               2 = assign default names with preset scores.
   * @throws IOException if an error occurs while reading input.
   */
	private void createPlayers(int choice) throws IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		Player player;
		for (var i = 1; i <= numberOfPlayers; i++) {
			if (choice == 0) {
				System.out.println("Entrer un nom de Joueur pour le joueur " + i + " :");
				String name = reader.readLine();
				player = new Player(name, 0, 0, initializePlayerHashMap());
			} else if (choice == 1) {
				player = new Player("Player " + i, 0, 0, initializePlayerHashMap());
			} else {
				player = new Player("Player " + i, 5, 0, initializePlayerHashMap());
			}
			players.add(player);
		}
	}

	/**
   * Assigns starting tiles to players randomly from the available habitat tiles.
   */
	private void choiceOfStartTiles() {
		Random rand = new Random();
		for (var i = 0; i < numberOfPlayers; i++) {
			
			ArrayList<Tile> listofChoice = startHabitat.get(rand.nextInt(startHabitat.size()));
			players.get(i).add(listofChoice.get(0), 25, 24, Animals.NOTHING, 1);
			players.get(i).add(listofChoice.get(1), 25, 25, Animals.NOTHING, 2);
			players.get(i).add(listofChoice.get(2), 26, 25, Animals.NOTHING, 3);
			startHabitat.remove(listofChoice);
		}
	}

	/**
   * Initializes wildlife cards based on the game mode.
   * 
   * @param cards a list of integers representing card configurations.
   */
	private void initializeWildlifeCards(List<Integer> cards) {
		if (game == 3) {
			wildlifeCards.add(new Fox("renard", cards.get(0), shapeOfTile));
			wildlifeCards.add(new Salmon("saumon", cards.get(1), shapeOfTile));
			wildlifeCards.add(new Bear("ours", cards.get(2), shapeOfTile));
			wildlifeCards.add(new Elk("wapiti", cards.get(3), shapeOfTile));
			wildlifeCards.add(new Buzzard("buse", cards.get(4), shapeOfTile));
		} else if (game == 2) {
			wildlifeCards.add(new Intermediate("intermediaire", 6, shapeOfTile));
		} else {
			wildlifeCards.add(new Family("famille", 5, shapeOfTile));
		}
	}

	/**
   * Initializes the players and assigns starting tiles.
   * 
   * @param choice an integer to determine player name assignment (see `createPlayers` for details).
   * @throws IOException if an error occurs while reading input.
   */
	private void initializePlayers(int choice) throws IOException {
		if (shapeOfTile == 2) {
			createStartTilesHexagonal();
		} else {
			createStartTilesSquare();
		}
		createPlayers(choice);
		choiceOfStartTiles();
	}

	/**
   * Main initialization method for the game.
   * Sets up the board, players, tokens, and cards based on the game configuration.
   * 
   * @param choice an integer to determine player name assignment.
   * @param cards  a list of integers representing card configurations.
   * @throws IOException if an error occurs while reading input.
   */
	public void initialize(int choice, List<Integer> cards) throws NumberFormatException, IOException {
		Objects.requireNonNull(cards, "cards is null");
		if (shapeOfTile == 2) {
			initializeHabitatHexagonal();
		} else {
			initializeHabitatSquare();
		}
		initializeAnimalToken();
		initializePlayers(choice);
		initializeWildlifeCards(cards);
	}

}