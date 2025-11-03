package fr.uge.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import fr.uge.game.element.Biome;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;

/**
 * This class is responsible for calculating points based on different game elements
 * such as biomes, habitats, and wildlife cards.
 */
public class CountPoint {

	public CountPoint() {
	}

	/**
   * Recursively calculates the number of connected tiles of a specific biome in a square-shaped grid.
   *
   * @param player The player whose habitat is being evaluated.
   * @param coordinate The current coordinate being evaluated.
   * @param visitedHabitat A map tracking visited coordinates.
   * @param biome The biome to count.
   * @return The total number of connected tiles for the given biome.
   */
	private static int biomePointCountForSquare(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat, String biome) {
		if (visitedHabitat.getOrDefault(coordinate, false)) {
			return 0;
		}
		Habitat habitat = player.getHabitat(coordinate);
		if (habitat != null && habitat.biomeToString(0).equals(biome)) {
			visitedHabitat.put(coordinate, true);
			return 1
					+ biomePointCountForSquare(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, biome)
					+ biomePointCountForSquare(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, biome)
					+ biomePointCountForSquare(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, biome)
					+ biomePointCountForSquare(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, biome);
		}
		return 0;
	}

	/**
   * Compares a coordinate against three neighbors and an old coordinate to check for overlap.
   *
   * @param neighbour1 The first neighboring coordinate.
   * @param neighbour2 The second neighboring coordinate.
   * @param neighbour3 The third neighboring coordinate.
   * @param oldCoordinate The previous coordinate.
   * @return true if the coordinates match, false otherwise.
   */
	private static boolean CompareCoordinate(Coordinate neighbour1, Coordinate neighbour2, Coordinate neighbour3, Coordinate oldCoordinate) {
		return (oldCoordinate.x() == neighbour1.x() && oldCoordinate.y() == neighbour1.y()
				|| oldCoordinate.x() == neighbour2.x() && oldCoordinate.y() == neighbour2.y()
				|| oldCoordinate.x() == neighbour3.x() && oldCoordinate.y() == neighbour3.y())
				|| (oldCoordinate.x() == -1 && oldCoordinate.y() == -1);
	}

	/**
   * Calculates points for a single-biome habitat in a hexagonal grid.
   *
   * @param player The player whose habitat is being evaluated.
   * @param coordinate The starting coordinate for evaluation.
   * @param visitedHabitat A map tracking visited coordinates.
   * @param biome The biome to count.
   * @return The total points for the given biome.
   */
	private static int returnFunctionForOneBiomeHabitat(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat, String biome) {
		visitedHabitat.put(coordinate, true);
		if (coordinate.y() % 2 != 0) {
			return 1
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, biome,
							coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, biome,
							coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, biome,
							coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x() - 1, coordinate.y() + 1), visitedHabitat,
							biome, coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, biome,
							coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x() - 1, coordinate.y() - 1), visitedHabitat,
							biome, coordinate);
		} else {
			return 1
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x() + 1, coordinate.y() - 1), visitedHabitat,
							biome, coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, biome,
							coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1), visitedHabitat,
							biome, coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, biome,
							coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, biome,
							coordinate)
					+ biomePointCountForHexagon(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, biome,
							coordinate);
		}
	}

	/**
   * Calculates points for a two-biome habitat in a hexagonal grid.
   *
   * @param player The player whose habitat is being evaluated.
   * @param coordinate The starting coordinate for evaluation.
   * @param visitedHabitat A map tracking visited coordinates.
   * @param biome The biome to count.
   * @param oldCoordinate The previous coordinate used for comparison.
   * @param habitat The habitat to evaluate.
   * @return The total points for the given biome.
   */
	private static int returnFunctionForTwoBiomeHabitat(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat, String biome, Coordinate oldCoordinate, Habitat habitat) {
		int index = habitat.biomeToString(0).equals(biome) ? 0 : 1;
		Coordinate neighbour1 = initializeFirstNeighbour(habitat, coordinate, index),
				neighbour2 = initializeSecondNeighbour(habitat, coordinate, index),
				neighbour3 = initializeThirdNeighbour(habitat, coordinate, index);
		if (!CompareCoordinate(neighbour1, neighbour2, neighbour3, oldCoordinate)) {
			return 0;
		}
		visitedHabitat.put(coordinate, true);
		return 1 + biomePointCountForHexagon(player, neighbour1, visitedHabitat, biome, coordinate)
				+ biomePointCountForHexagon(player, neighbour2, visitedHabitat, biome, coordinate)
				+ biomePointCountForHexagon(player, neighbour3, visitedHabitat, biome, coordinate);
	}

	/**
   * Initializes the first neighboring coordinate for a habitat based on its rotation.
   *
   * @param habitat The habitat being evaluated.
   * @param coordinate The current coordinate.
   * @param index The biome index.
   * @return The first neighboring coordinate.
   */
	private static Coordinate initializeFirstNeighbour(Habitat habitat, Coordinate coordinate, int index) {
		if (habitat.rotation() == 1 || habitat.rotation() == 4) {
			if ((index == 0 && habitat.rotation() == 1) || (index == 1 && habitat.rotation() == 4)) {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x() - 1, coordinate.y() + 1)
						: new Coordinate(coordinate.x(), coordinate.y() + 1);
			} else {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x(), coordinate.y() - 1)
						: new Coordinate(coordinate.x() + 1, coordinate.y() - 1);
			}
		} else if (habitat.rotation() == 2 || habitat.rotation() == 5) {
			if ((index == 0 && habitat.rotation() == 2) || (index == 1 && habitat.rotation() == 5)) {
				return new Coordinate(coordinate.x() - 1, coordinate.y());
			} else {
				return new Coordinate(coordinate.x() + 1, coordinate.y());
			}
		} else {
			if ((index == 0 && habitat.rotation() == 3) || (index == 1 && habitat.rotation() == 6)) {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x() - 1, coordinate.y() - 1)
						: new Coordinate(coordinate.x(), coordinate.y() - 1);
			} else {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x(), coordinate.y() + 1)
						: new Coordinate(coordinate.x() + 1, coordinate.y() + 1);
			}
		}
	}

	/**
   * Initializes the second neighboring coordinate for a habitat based on its rotation.
   *
   * @param habitat The habitat being evaluated.
   * @param coordinate The current coordinate.
   * @param index The biome index.
   * @return The second neighboring coordinate.
   */
	private static Coordinate initializeSecondNeighbour(Habitat habitat, Coordinate coordinate, int index) {
		if (habitat.rotation() == 1 || habitat.rotation() == 4) {
			if ((index == 0 && habitat.rotation() == 1) || (index == 1 && habitat.rotation() == 4)) {
				return new Coordinate(coordinate.x() - 1, coordinate.y());
			} else {
				return new Coordinate(coordinate.x() + 1, coordinate.y());
			}
		} else if (habitat.rotation() == 2 || habitat.rotation() == 5) {
			if ((index == 0 && habitat.rotation() == 2) || (index == 1 && habitat.rotation() == 5)) {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x() - 1, coordinate.y() - 1)
						: new Coordinate(coordinate.x(), coordinate.y() - 1);
			} else {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x(), coordinate.y() + 1)
						: new Coordinate(coordinate.x() + 1, coordinate.y() + 1);
			}
		} else {
			if ((index == 0 && habitat.rotation() == 3) || (index == 1 && habitat.rotation() == 6)) {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x(), coordinate.y() - 1)
						: new Coordinate(coordinate.x() + 1, coordinate.y() - 1);
			} else {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x() - 1, coordinate.y() + 1)
						: new Coordinate(coordinate.x(), coordinate.y() + 1);
			}
		}
	}

	/**
   * Initializes the third neighboring coordinate for a habitat based on its rotation.
   *
   * @param habitat The habitat being evaluated.
   * @param coordinate The current coordinate.
   * @param index The biome index.
   * @return The third neighboring coordinate.
   */
	private static Coordinate initializeThirdNeighbour(Habitat habitat, Coordinate coordinate, int index) {
		if (habitat.rotation() == 1 || habitat.rotation() == 4) {
			if ((index == 0 && habitat.rotation() == 1) || (index == 1 && habitat.rotation() == 4)) {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x() - 1, coordinate.y() - 1)
						: new Coordinate(coordinate.x(), coordinate.y() - 1);
			} else {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x(), coordinate.y() + 1)
						: new Coordinate(coordinate.x() + 1, coordinate.y() + 1);
			}
		} else if (habitat.rotation() == 2 || habitat.rotation() == 5) {
			if ((index == 0 && habitat.rotation() == 2) || (index == 1 && habitat.rotation() == 5)) {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x(), coordinate.y() - 1)
						: new Coordinate(coordinate.x() + 1, coordinate.y() - 1);
			} else {
				return coordinate.y() % 2 != 0 ? new Coordinate(coordinate.x() - 1, coordinate.y() + 1)
						: new Coordinate(coordinate.x(), coordinate.y() + 1);
			}
		} else {
			if ((index == 0 && habitat.rotation() == 3) || (index == 1 && habitat.rotation() == 6)) {
				return new Coordinate(coordinate.x() + 1, coordinate.y());
			} else {
				return new Coordinate(coordinate.x() - 1, coordinate.y());
			}
		}
	}

	/**
   * Recursively calculates the number of connected tiles of a specific biome in a hexagonal grid.
   *
   * @param player The player whose habitat is being evaluated.
   * @param coordinate The current coordinate being evaluated.
   * @param visitedHabitat A map tracking visited coordinates.
   * @param biome The biome to count.
   * @param oldCoordinate The previous coordinate for comparison.
   * @return The total number of connected tiles for the given biome.
   */
	private static int biomePointCountForHexagon(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat, String biome, Coordinate oldCoordinate) {
		if (visitedHabitat.getOrDefault(coordinate, false)) {
			return 0;
		}
		Habitat habitat = player.getHabitat(coordinate);
		if (player.habitats().containsKey(coordinate) && habitat.tile().biome().size() == 1) {
			if (habitat.biomeToString(0).equals(biome)) {
				return returnFunctionForOneBiomeHabitat(player, coordinate, visitedHabitat, biome);
			}
		} else {
			if (player.habitats().containsKey(coordinate) && (habitat.biomeToString(0).equals(biome) || habitat.biomeToString(1).equals(biome))) {
				return returnFunctionForTwoBiomeHabitat(player, coordinate, visitedHabitat, biome, oldCoordinate, habitat);
			}
		}
		return 0;
	}

	/**
   * Adds biome points for a given player based on their habitat configuration.
   *
   * @param player The player whose habitat is being evaluated.
   * @param biome The biome being evaluated.
   * @param coordinate The starting coordinate.
   * @param visitedHabitatForBiome A map tracking visited coordinates for the biome.
   * @param shapeOfTile The shape of the tiles (square or hexagon).
   */
	private static void addPointForBiomeCount(Player player, Biome biome, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitatForBiome, int shapeOfTile) {
		int visitedBiome = 0;
		Habitat habitat = player.getHabitat(coordinate);
		if (habitat.tile().biome().size() == 2 && !visitedHabitatForBiome.getOrDefault(coordinate, false)) {
			visitedBiome = biomePointCountForHexagon(player, coordinate, visitedHabitatForBiome, biome.name(), new Coordinate(-1, -1));
			if (habitat.biomeToString(0).equals(biome.name()) && visitedBiome >= player.getHashMapBiomePoint().getOrDefault(habitat.biomeToString(0), 0)) {
				player.putHashMapBiomePoint(habitat.biomeToString(0), visitedBiome);
			} else if (habitat.biomeToString(1).equals(biome.name()) && visitedBiome >= player.getHashMapBiomePoint().getOrDefault(habitat.biomeToString(1), 0)) {
				player.putHashMapBiomePoint(habitat.biomeToString(1), visitedBiome);
			}
		} else {
			if (habitat.biomeToString(0).equals(biome.name()) && !visitedHabitatForBiome.getOrDefault(coordinate, false)) {
				visitedBiome = shapeOfTile == 2
						? biomePointCountForHexagon(player, coordinate, visitedHabitatForBiome, biome.name(), new Coordinate(-1, -1))
						: biomePointCountForSquare(player, coordinate, visitedHabitatForBiome, biome.name());
				if (visitedBiome >= player.getHashMapBiomePoint().getOrDefault(habitat.biomeToString(0), 0)) {
					player.putHashMapBiomePoint(habitat.biomeToString(0), visitedBiome);
				}
			}
		}
	}

	/**
	 * Calculates the total points for a player based on their biomes and wildlife cards.
	 *
	 * @param players The list of players whose points are being calculated.
	 * @param wildlifeCards The list of wildlife cards to evaluate.
	 * @param shapeOfTile The shape of the tiles (square or hexagon).
	 */
	private static void totalPointCount(ArrayList<Player> players, ArrayList<WildlifeCount> wildlifeCards, int shapeOfTile) {
		for(var player : players) {
			List<Biome> listOfBiome = List.of(Biome.FOREST, Biome.MEADOW, Biome.MOUNTAIN, Biome.RIVER, Biome.SWAMP);
			for (Biome biome : listOfBiome) {
				HashMap<Coordinate, Boolean> visitedHabitatForBiome = new HashMap<>();
				for (Coordinate coordinate : player.habitats().keySet()) {
					addPointForBiomeCount(player, biome, coordinate, visitedHabitatForBiome, shapeOfTile);
				}
			}
			System.out.println(player.getHashMapBiomePoint());
			for (var entry : player.getHashMapBiomePoint().entrySet()) {
				player.pointAdd(entry.getValue());
			}
			for (var card : wildlifeCards) {
				player.pointAdd(card.pointCount(player));
			}
		}
	}

	/**
	 * Sorts a list of players based on their points for a specific biome.
	 *
	 * @param players The list of players to sort.
	 * @param biome The biome key to sort by.
	 * @return The sorted list of players.
	 */
	private static ArrayList<Player> sortedList(ArrayList<Player> players, String biome) {
		ArrayList<Player> playerBis = new ArrayList<>(players);
		return (ArrayList<Player>) playerBis.stream().sorted((p1, p2) -> Integer
				.compare(p2.getHashMapBiomePoint().getOrDefault(biome, 0), p1.getHashMapBiomePoint().getOrDefault(biome, 0)))
				.collect(Collectors.toList());
	}

	/**
	 * Adds bonus points to players based on their biome rankings.
	 *
	 * @param players The list of players to evaluate.
	 */
	private static void addBonusPoints(ArrayList<Player> players) {
		for (var key : players.get(0).getHashMapBiomePoint().keySet()) {
			ArrayList<Player> sortedPlayer = sortedList(players, key);
			int maxBiomeSize = sortedPlayer.get(0).getHashMapBiomePoint().getOrDefault(key, 0);
			int countMaxPlayers = 0;
			for (Player player : sortedPlayer) {
				if (player.getHashMapBiomePoint().getOrDefault(key, 0) == maxBiomeSize) {
					countMaxPlayers++;
				}
			}
			if (countMaxPlayers == 1) {
				sortedPlayer.get(0).pointAdd(2);
			} else {
				for (int i = 0; i < countMaxPlayers; i++) {
					sortedPlayer.get(i).pointAdd(1);
				}
			}
		}
	}
	
	/**
	 * Awards bonus points to players based on their rankings and the number of players.
	 *
	 * @param players The list of players to evaluate.
	 * @param numberOfPlayers The number of players in the game.
	 */
	private static void bonusPoint(ArrayList<Player> players, int numberOfPlayers) {
		Objects.requireNonNull(players, "players is null");
		if (numberOfPlayers < 0 || numberOfPlayers > 4) {
			throw new IllegalArgumentException("numberOfPlayers < 0 or numberOfPlayers > 4");
		}
		addBonusPoints(players);
	}

	/**
	 * Counts and awards nature token points to players.
	 *
	 * @param players The list of players to evaluate.
	 */
	private static void countNatureToken(ArrayList<Player> players) {
		Objects.requireNonNull(players, "players is null");
		for (var player : players) {
			int token = player.natureToken();
			player.pointAdd(token);
		}
	}
	
	/**
	 * Calculates total points for players, including biome, wildlife cards, 
	 * bonus points, and nature tokens.
	 *
	 * @param players The list of players to evaluate.
	 * @param wildlifeCards The list of wildlife cards to consider.
	 * @param shapeOfTile The shape of the tiles (square or hexagon).
	 * @param numberOfPlayer The number of players in the game.
	 */
	public static void pointCount(ArrayList<Player> players, ArrayList<WildlifeCount> wildlifeCards, int shapeOfTile, int numberOfPlayer) {
		Objects.requireNonNull(players, "players is null");
		Objects.requireNonNull(wildlifeCards, "wildlifeCards is null");
		totalPointCount(players, wildlifeCards, shapeOfTile);
    bonusPoint(players, numberOfPlayer);
    countNatureToken(players);
	}
}