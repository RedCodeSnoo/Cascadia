package fr.uge.game.wildlifeCount;

import java.util.HashMap;
import java.util.Objects;

import fr.uge.game.WildlifeCount;
import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;

/**
 * Class representing the "Bear" animal in the WildlifeCount game. Implements
 * the WildlifeCount interface for calculating points based on specific
 * conditions for the bear.
 */
public record Bear(String name, int pattern, int shapeOfTile) implements WildlifeCount {

	/**
   * Constructs a Bear object with the specified name, pattern, and shape of tile.
   * 
   * @param name The name of the bear object (cannot be null).
   * @param pattern The pattern to be used (must be between 1 and 4).
   * @param shapeOfTile The shape of the tile (must be between 0 and 2).
   * @throws IllegalArgumentException if the pattern is not between 1 and 4 or if the shapeOfTile is out of range.
   */
	public Bear {
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
	 * @param counter : The number of counted cells.
	 * @return The score based on pattern 1.
	 */
	private int resultOfCount1(int counter) {
		return switch (counter) {
		case 1 -> 4;
		case 2 -> 11;
		case 3 -> 19;
		default -> (counter >= 4) ? 20 : 0;
		};
	}

	/**
	 * Calculates the counting result for pattern 2.
	 * 
	 * @param counter : The number of counted cells.
	 * @return The score based on pattern 2.
	 */
	private int resultOfCount2(int counter) {
		return counter * 10;
	}

	/**
	 * Calculates the counting result for pattern 3, using an animal counter map.
	 * 
	 * @param mapCounter : A HashMap containing the number of animals counted for
	 *                   each type of form.
	 * @return The score based on the counted animals.
	 */
	private int resultOfCount3(HashMap<Integer, Integer> mapCounter) {
		var result = 0;
		for (var counter : mapCounter.values()) {
			if (counter != 0) {
				result++;
			}
		}
		if (result != 3) {
			result = 0;
		}
		return result + mapCounter.get(1) * 2 + mapCounter.get(2) * 5 + mapCounter.get(3) * 8;
	}

	/**
	 * Calculates the counting result for pattern 4, using an animal counter map.
	 * 
	 * @param mapCounter : A HashMap containing the number of animals counted for
	 *                   each type of form.
	 * @return The score based on the counted animals.
	 */
	private int resultOfCount4(HashMap<Integer, Integer> mapCounter) {
		return mapCounter.get(1) * 5 + mapCounter.get(2) * 8 + mapCounter.get(3) * 13;
	}

	/**
	 * A recursive function to count the bears in a given area by exploring adjacent
	 * cells.
	 * 
	 * @param player         : The player whose score is being calculated.
	 * @param coordinate     : The position from where the counting begins.
	 * @param visitedHabitat : A HashMap to prevent counting the same cell multiple
	 *                       times.
	 * @return The number of bears found.
	 */
	private int countBear(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat) {
		if (visitedHabitat.getOrDefault(coordinate, false)) {
			return 0;
		}

		Habitat habitat = player.getHabitat(coordinate);
		if (habitat != null && habitat.animal().equals(Animals.BEAR)) {
			visitedHabitat.put(coordinate, true);
			if (shapeOfTile == 2) {
				if (coordinate.y() % 2 != 0) {
					return 1 + countBear(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x() - 1, coordinate.y() + 1), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x() - 1, coordinate.y() - 1), visitedHabitat);
				} else {
					return 1 + countBear(player, new Coordinate(coordinate.x() + 1, coordinate.y() - 1), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
							+ countBear(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat);
				}
			} else {
				return 1 + countBear(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
						+ countBear(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
						+ countBear(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
						+ countBear(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat);
			}
		}
		return 0;
	}

	/**
	 * Function that counts the number of animals in a specific area according to
	 * the rules of the pattern and shapeOfTile.
	 * 
	 * @param player                  : The player whose score is being calculated.
	 * @param coordinate              : The position where the counting begins.
	 * @param visitedHabitatForAnimal : A HashMap to avoid revisiting cells.
	 * @param mapCounter              : A HashMap containing the number of animals
	 *                                counted for each type of form.
	 * @return The number of animals found in this area.
	 */
	private int counter(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitatForAnimal,
			HashMap<Integer, Integer> mapCounter) {
		Habitat habitat = player.getHabitat(coordinate);
		int visitedAnimal = 0;
		int counter = 0;
		if (habitat != null && !habitat.animal().equals(Habitat.defaultHabitat().animal())
				&& !visitedHabitatForAnimal.getOrDefault(coordinate, false)) {
			visitedAnimal = countBear(player, coordinate, visitedHabitatForAnimal);
			if (visitedAnimal == 2 && pattern == 1) {
				counter++;
			} else if (visitedAnimal == 3 && pattern == 2) {
				counter++;
			} else if (pattern == 3 && visitedAnimal >= 1 && visitedAnimal <= 3) {
				mapCounter.put(visitedAnimal, mapCounter.get(visitedAnimal) + 1);
			} else if (pattern == 4 && visitedAnimal >= 2 && visitedAnimal <= 4) {
				mapCounter.put(visitedAnimal - 1, mapCounter.get(visitedAnimal - 1) + 1);
			}
		}
		return counter;
	}

	/**
	 * Initializes a map to keep track of the animal counts by type of form.
	 * 
	 * @return A HashMap initialized with counts for each type of form.
	 */
	HashMap<Integer, Integer> initializeMap() {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(1, 0);
		map.put(2, 0);
		map.put(3, 0);
		return map;
	}

	/**
	 * Calculates the final result based on the pattern.
	 * 
	 * @param mapCounter : A HashMap containing the number of animals counted for
	 *                   each type of form.
	 * @param result     : The intermediate result.
	 * @return The final result based on the pattern.
	 */
	private int finalResult(HashMap<Integer, Integer> mapCounter, int result) {
		return switch (pattern) {
		case 3 -> resultOfCount3(mapCounter);
		case 4 -> resultOfCount4(mapCounter);
		default -> result;
		};
	}

	/**
	 * Calculates the points for the player based on the game logic and the specific
	 * rules for bear.
	 * 
	 * @param player : The player whose score is being calculated.
	 * @return The total points the player earned for this round.
	 */
	@Override
	public int pointCount(Player player) {
		Objects.requireNonNull(player, "player is null");

		HashMap<Coordinate, Boolean> visitedHabitatForAnimal = new HashMap<>();
		var result = 0;
		var mapCounter = initializeMap();
		for (Coordinate coordinate : player.habitats().keySet()) {
			Habitat habitat = player.getHabitat(coordinate);
			if (habitat != null && !visitedHabitatForAnimal.getOrDefault(coordinate, false)) {
				var counter = 0;
				counter = counter(player, coordinate, visitedHabitatForAnimal, mapCounter);
				result += switch (pattern) {
				case 1 -> resultOfCount1(counter);
				case 2 -> resultOfCount2(counter);
				default -> 0;
				};
				if (counter != 0) {
					System.out.println("counter : " + counter + ", result : " + result);
				}
			}
		}
		return finalResult(mapCounter, result);
	}
}