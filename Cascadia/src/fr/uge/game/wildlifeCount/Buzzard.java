package fr.uge.game.wildlifeCount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import fr.uge.game.WildlifeCount;
import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;

/**
 * Class representing the "Buzzard" animal in the WildlifeCount game. Implements
 * the WildlifeCount interface for calculating points based on specific
 * conditions for the buzzard.
 */
public record Buzzard(String name, int pattern, int shapeOfTile) implements WildlifeCount {

	/**
   * Constructs a Buzzard object with the specified name, pattern, and shape of tile.
   * 
   * @param name The name of the buzzard object (cannot be null).
   * @param pattern The pattern to be used (must be between 1 and 4).
   * @param shapeOfTile The shape of the tile (must be between 0 and 2).
   * @throws IllegalArgumentException if the pattern is not between 1 and 4 or if the shapeOfTile is out of range.
   */
	public Buzzard {
		Objects.requireNonNull(name, "name is null");
		if (pattern < 1 || pattern > 4) {
			throw new IllegalArgumentException("pattern s different to 5");
		}
		if (shapeOfTile < 0 || shapeOfTile > 2) {
			throw new IllegalArgumentException("shapeOfTile < 0 or shapeOfTile > 2");
		}
	}

	/**
	 * Calculates the counting result for pattern 1.
	 * 
	 * @param finalCount : A map containing the number of buzzards counted.
	 * @return The score based on pattern 1.
	 */
	private int resultOfCount1(HashMap<Integer, Integer> finalCount) {
		Integer counter = finalCount.get(1);
		return switch (counter) {
		case 1 -> 2;
		case 2 -> 5;
		case 3 -> 8;
		case 4 -> 11;
		case 5 -> 14;
		case 6 -> 18;
		case 7 -> 22;
		default -> (counter >= 8) ? 26 : 0;
		};
	}

	/**
	 * Calculates the counting result for pattern 2.
	 * 
	 * @param finalCount : A map containing the number of buzzards counted.
	 * @return The score based on pattern 2.
	 */
	private int resultOfCount2(HashMap<Integer, Integer> finalCount) {
		Integer counter = finalCount.get(2);
		return switch (counter) {
		case 1 -> 2;
		case 2 -> 5;
		case 3 -> 8;
		case 4 -> 11;
		case 5 -> 14;
		case 6 -> 18;
		case 7 -> 22;
		default -> (counter >= 8) ? 26 : 0;
		};
	}

	/**
	 * Calculates the counting result for pattern 3.
	 * 
	 * @param finalCount : A map containing the number of buzzards counted.
	 * @return The score based on pattern 3.
	 */
	private int resultOfCount3(HashMap<Integer, Integer> finalCount) {
		return 3 * finalCount.get(3);
	}

	/**
	 * Calculates the counting result for pattern 4.
	 * 
	 * @param finalCount : A map containing the number of each animals counted.
	 * @return The score based on pattern 4.
	 */
	private int resultOfCount4(HashMap<Animals, Integer> mapOfAnimals) {
		var counter = 0;
		for (var animal : mapOfAnimals.keySet()) {
			if (!animal.equals(Animals.BUZZARD) && mapOfAnimals.get(animal) != 0) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Finds all the buzzards present in the player's habitat.
	 * 
	 * @param player : The player whose buzzards are being counted.
	 * @return A list of coordinates where the buzzards are located.
	 */
	private ArrayList<Coordinate> findBuzzard(Player player) {
		ArrayList<Coordinate> listOfBuzzard = new ArrayList<>();
		for (var coordinate : player.habitats().keySet()) {
			if (player.getHabitat(coordinate).animal().equals(Animals.BUZZARD)) {
				listOfBuzzard.add(coordinate);
			}
		}
		return listOfBuzzard;
	}

	/**
	 * Checks if a given coordinate is occupied by a buzzard's habitat.
	 * 
	 * @param player     : The player whose habitat is being checked.
	 * @param coordinate : The coordinate to check.
	 * @return True if the coordinate is occupied by something other than a buzzard,
	 *         false if it is occupied by a buzzard.
	 */
	private boolean neighbourAnimal(Player player, Coordinate coordinate) {
		if (player.habitats().containsKey(coordinate)) {
			if (player.getHabitat(coordinate).animal().equals(Animals.BUZZARD)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Initializes a map to count the number of buzzards encountered during the
	 * counting process.
	 * 
	 * @return A HashMap initialized with counts for different categories of buzzard
	 *         occurrences.
	 */
	private HashMap<Integer, Integer> initializeMap() {
		HashMap<Integer, Integer> mapOfCounter = new HashMap<>();
		mapOfCounter.put(1, 0);
		mapOfCounter.put(2, 0);
		mapOfCounter.put(3, 0);
		mapOfCounter.put(4, 0);
		return mapOfCounter;
	}

	/**
	 * Initializes a map to count the number of different animals encountered.
	 * 
	 * @return A HashMap initialized with counts for different animals.
	 */
	private HashMap<Animals, Integer> initializeMap2() {
		HashMap<Animals, Integer> mapOfAnimals = new HashMap<>();
		mapOfAnimals.put(Animals.BEAR, 0);
		mapOfAnimals.put(Animals.SALMON, 0);
		mapOfAnimals.put(Animals.FOX, 0);
		mapOfAnimals.put(Animals.BUZZARD, 0);
		mapOfAnimals.put(Animals.ELK, 0);
		return mapOfAnimals;
	}

	/**
	 * Checks if the given coordinate is surrounded by other animals.
	 * 
	 * @param player     : The player whose habitat is being checked.
	 * @param coordinate : The coordinate to check.
	 * @return True if the coordinate is surrounded by animals, false otherwise.
	 */
	private boolean surroundByOtherAnimal(Player player, Coordinate coordinate) {
		Objects.requireNonNull(player, "player is null");

		if (shapeOfTile == 2) {
			if (coordinate.y() % 2 != 0) {
				return neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1))
						& neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()))
						& neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1))
						& neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y() + 1))
						& neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()))
						& neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y() - 1));
			} else {
				return neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y() - 1))
						& neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()))
						& neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1))
						& neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1))
						& neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()))
						& neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1));
			}
		}
		return neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()))
				&& neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()))
				&& neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1))
				&& neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1));

	}

	/**
	 * Checks if the path between two coordinates forms a valid line and processes
	 * the animals along the path.
	 * 
	 * @param coordinate1   : The first coordinate of the path.
	 * @param coordinate2   : The second coordinate of the path.
	 * @param animals       : A map to count the animals encountered.
	 * @param player        : The player whose habitat is being checked.
	 * @param finalCount    : A map to track final counts.
	 * @param visitedAnimal : A map to track the animals that have already been
	 *                      visited.
	 * @param min           : The minimum value of the coordinate (x or y) to check.
	 * @param max           : The maximum value of the coordinate (x or y) to check.
	 * @return True if the path is valid and processed, false otherwise.
	 */
	private boolean checkColumRow(Coordinate coordinate1, Coordinate coordinate2, HashMap<Animals, Integer> animals,
			Player player, HashMap<Integer, Integer> finalCount, HashMap<Coordinate, Boolean> visitedAnimal, int min,
			int max) {
		if (shapeOfTile != 2) {
			for (int i = min + 1; i < max; i++) {
				Coordinate currentCoord = new Coordinate(coordinate1.x(), i);
				if (player.habitats().containsKey(currentCoord)) {
					Habitat habitat = player.getHabitat(currentCoord);
					if (!habitat.animal().equals(Animals.BUZZARD) && !habitat.animal().equals(Animals.NOTHING)) {
						Animals animal = habitat.animal();
						animals.put(animal, animals.getOrDefault(animal, 0) + 1);
					}
					if (habitat.animal().equals(Animals.BUZZARD) && !visitedAnimal.containsKey(currentCoord)) {
						visitedAnimal.put(currentCoord, true);
						finalCount.put(3, finalCount.get(3) + 1);
						if (pattern != 3) {
							finalCount.put(2, finalCount.get(2) + 1);
							return true;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the given coordinates form a valid line and processes the animals
	 * in the line.
	 * 
	 * @param player        : The player whose habitat is being checked.
	 * @param coordinate1   : The starting coordinate of the line.
	 * @param coordinate2   : The ending coordinate of the line.
	 * @param finalCount    : A map tracking the number of animals encountered.
	 * @param visitedAnimal : A map tracking which coordinates have been visited.
	 * @param animals       : A map to count the animals encountered.
	 * @return True if the line is valid and processed, false otherwise.
	 */
	private boolean checkLine(Player player, Coordinate coordinate1, Coordinate coordinate2,
			HashMap<Integer, Integer> finalCount, HashMap<Coordinate, Boolean> visitedAnimal,
			HashMap<Animals, Integer> animals) {
		if (coordinate1.x() == coordinate2.x()) {
			int min = Math.min(coordinate1.y(), coordinate2.y());
			int max = Math.max(coordinate1.y(), coordinate2.y());
			if (min + 1 == max) {
				return false;
			}
			return checkColumRow(coordinate1, coordinate2, animals, player, finalCount, visitedAnimal, min, max);
		} else if (coordinate1.y() == coordinate2.y()) {
			int min = Math.min(coordinate1.x(), coordinate2.x());
			int max = Math.max(coordinate1.x(), coordinate2.x());
			if (min + 1 == max) {
				return false;
			}
			return checkColumRow(coordinate1, coordinate2, animals, player, finalCount, visitedAnimal, min, max);
		}
		return true;
	}

	/**
	 * Checks if a given coordinate is valid (within the bounds of the game board).
	 * 
	 * @param coord : The coordinate to check.
	 * @return True if the coordinate is valid, false otherwise.
	 */
	private boolean isValidCoord(Coordinate coord) {
		return coord.x() >= 0 && coord.y() >= 0 && coord.x() < 50 && coord.y() < 50;
	}

	/**
	 * Determines the direction to take based on movement on a hexagonal grid.
	 * 
	 * @param direction : The direction to move (0 or 1).
	 * @param y         : The current y-coordinate.
	 * @param x         : The current x-coordinate.
	 * @return The next coordinate to move to.
	 */
	private Coordinate directionToUse(int direction, int y, int x) {
		if (y % 2 != 0) {
			return (direction == 1) ? new Coordinate(x, y - 1) : new Coordinate(x - 1, y - 1);
		}
		return (direction == 1) ? new Coordinate(x + 1, y - 1) : new Coordinate(x, y - 1);
	}

	/**
	 * Adds an animal to the animals map if the habitat at the given coordinate
	 * contains an animal.
	 * 
	 * @param habitat : The habitat to check.
	 * @param animals : The map to count the animals.
	 */
	private void addOtherAnimal(Habitat habitat, HashMap<Animals, Integer> animals) {
		if (habitat != null) {
			if (!habitat.animal().equals(Animals.NOTHING)) {
				Animals animal = habitat.animal();
				animals.put(animal, animals.getOrDefault(animal, 0) + 1);
				System.out.println("PREMIER : " + animals);
			}
		}
	}

	/**
	 * Traverses a diagonal path from the starting point to the target point and
	 * processes the animals along the way.
	 * 
	 * @param player        : The player whose habitat is being checked.
	 * @param startPoint    : The starting coordinate of the diagonal.
	 * @param targetPoint   : The target coordinate to reach.
	 * @param animals       : A map to count the animals encountered.
	 * @param visitedAnimal : A map to track the animals that have already been
	 *                      visited.
	 * @param direction     : The direction to move in (0 or 1).
	 * @return True if the diagonal is traversed successfully, false otherwise.
	 */
	private boolean travelDiagonal(Player player, Coordinate startPoint, Coordinate targetPoint,
			HashMap<Animals, Integer> animals, HashMap<Coordinate, Boolean> visitedAnimal, int direction) {
		int x = startPoint.x();
		int y = startPoint.y();
		while (true) {
			Coordinate nextCoordinate = directionToUse(direction, y, x);
			if (!isValidCoord(nextCoordinate)) {
				break;
			}
			Habitat habitat = player.getHabitat(nextCoordinate);
			addOtherAnimal(habitat, animals);
			if (nextCoordinate.x() == targetPoint.x() && nextCoordinate.y() == targetPoint.y()) {
				if (pattern == 4) {
					visitedAnimal.put(nextCoordinate, true);
				}
				return true;
			}
			x = nextCoordinate.x();
			y = nextCoordinate.y();
		}
		return false;
	}

	/**
	 * Checks if a diagonal path between two coordinates on a hexagonal grid is
	 * valid and processes the animals encountered.
	 * 
	 * @param player        : The player whose habitat is being checked.
	 * @param coordinate1   : The first coordinate of the diagonal.
	 * @param coordinate2   : The second coordinate of the diagonal.
	 * @param finalCount    : A map to track final counts.
	 * @param visitedAnimal : A map to track the animals that have already been
	 *                      visited.
	 * @param animals       : A map to count the animals encountered.
	 * @return True if the diagonal is valid and processed, false otherwise.
	 */
	private boolean checkDiagonalHexagonalUsingNeighbours(Player player, Coordinate coordinate1, Coordinate coordinate2,
			HashMap<Integer, Integer> finalCount, HashMap<Coordinate, Boolean> visitedAnimal,
			HashMap<Animals, Integer> animals) {
		HashMap<Animals, Integer> temporaryAnimals = initializeMap2();
		int y = Math.max(coordinate1.y(), coordinate2.y());
		var coordinateMaxy = (y == coordinate1.y()) ? coordinate1 : coordinate2;
		var coordinateMiny = (y == coordinate2.y()) ? coordinate1 : coordinate2;
		System.out.println(coordinate1 + " " + coordinate2);
		if (travelDiagonal(player, coordinateMaxy, coordinateMiny, temporaryAnimals, visitedAnimal, 0)
				|| travelDiagonal(player, coordinateMaxy, coordinateMiny, temporaryAnimals, visitedAnimal, 1)) {
			for (var key : temporaryAnimals.keySet()) {
				if (temporaryAnimals.get(key) != 0) {
					animals.put(key, animals.getOrDefault(key, 0) + 1);
					System.out.println("DEUXIEME : " + animals);
				}
			}
			finalCount.put(3, finalCount.get(3) + 1);
			return true;
		}
		return false;
	}

	/**
	 * Performs final score calculation based on the selected pattern.
	 * 
	 * @param mapOfCounter : A map containing the counts for different categories.
	 * @param animals      : A map containing the counts for different animals.
	 * @return The final score based on the selected pattern.
	 */
	private int finalResult(HashMap<Integer, Integer> mapOfCounter, HashMap<Animals, Integer> animals) {
		return switch (pattern) {
		case 1 -> resultOfCount1(mapOfCounter);
		case 2 -> resultOfCount2(mapOfCounter);
		case 3 -> resultOfCount3(mapOfCounter);
		case 4 -> resultOfCount4(animals);
		default -> 0;
		};
	}

	/**
	 * Calculates the points for the player based on the game logic and the specific
	 * rules for the buzzard.
	 * 
	 * @param player : The player whose score is being calculated.
	 * @return The total points the player earned for this round.
	 */
	@Override
	public int pointCount(Player player) {
		Objects.requireNonNull(player, "player is null");
		ArrayList<Coordinate> buzzardOnGameBoard = findBuzzard(player);
		HashMap<Integer, Integer> mapOfCounter = initializeMap();
		HashMap<Animals, Integer> animals = initializeMap2();
		HashMap<Coordinate, Boolean> visitedAnimal = new HashMap<>();
		for (int i = 0; i < buzzardOnGameBoard.size(); i++) {
			if (pattern == 1) {
				if (surroundByOtherAnimal(player, buzzardOnGameBoard.get(i))) {
					mapOfCounter.put(1, mapOfCounter.get(1) + 1);
				}
			} else {
				for (int j = i + 1; j < buzzardOnGameBoard.size(); j++) {
					if (shapeOfTile() == 2) {
						checkLine(player, buzzardOnGameBoard.get(i), buzzardOnGameBoard.get(j), mapOfCounter, visitedAnimal,
								animals);
						checkDiagonalHexagonalUsingNeighbours(player, buzzardOnGameBoard.get(i), buzzardOnGameBoard.get(j),
								mapOfCounter, visitedAnimal, animals);
					} else {
						checkLine(player, buzzardOnGameBoard.get(i), buzzardOnGameBoard.get(j), mapOfCounter, visitedAnimal,
								animals);
					}
				}
			}
		}
		return finalResult(mapOfCounter, animals);
	}
}