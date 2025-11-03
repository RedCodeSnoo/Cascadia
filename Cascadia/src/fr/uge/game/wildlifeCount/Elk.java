package fr.uge.game.wildlifeCount;

import java.util.HashMap;
import java.util.Objects;

import fr.uge.game.WildlifeCount;
import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;

public record Elk(String name, int pattern, int shapeOfTile) implements WildlifeCount {

	/**
   * Constructs a Elk object with the specified name, pattern, and shape of tile.
   * 
   * @param name The name of the elk object (cannot be null).
   * @param pattern The pattern to be used (must be between 1 and 4).
   * @param shapeOfTile The shape of the tile (must be between 0 and 2).
   * @throws IllegalArgumentException if the pattern is not between 1 and 4 or if the shapeOfTile is out of range.
   */
	public Elk {
		Objects.requireNonNull(name, "name is null");
		if (pattern < 1 || pattern > 4) {
			throw new IllegalArgumentException("pattern s different to 5");
		}
		if (shapeOfTile < 0 || shapeOfTile > 2) {
			throw new IllegalArgumentException("shapeOfTile < 0 or shapeOfTile > 2");
		}
	}

	/**
   * Calculates the result based on pattern 1.
   * 
   * @param mapCounter A map containing the count of each pattern group.
   * @return The result based on the counts for pattern 1.
   */
	public int resultOfCount1(HashMap<Integer, Integer> mapCounter) {
		return mapCounter.get(1) * 2 + mapCounter.get(2) * 5 + mapCounter.get(3) * 9 + mapCounter.get(4) * 13;
	}

	/**
   * Calculates the result based on pattern 3.
   * 
   * @param mapCounter A map containing the count of each pattern group.
   * @return The result based on the counts for pattern 3.
   */
	public int resultOfCount3(HashMap<Integer, Integer> mapCounter) {
		return mapCounter.get(1) * 2 + mapCounter.get(2) * 4 + mapCounter.get(3) * 7 + mapCounter.get(4) * 10
				+ mapCounter.get(5) * 14 + mapCounter.get(6) * 18 + mapCounter.get(7) * 23 + mapCounter.get(8) * 28;
	}

	/**
   * Calculates the result based on pattern 4.
   * 
   * @param mapCounter A map containing the count of each pattern group.
   * @return The result based on the counts for pattern 4.
   */
	public int resultOfCount4(HashMap<Integer, Integer> mapCounter) {
		return mapCounter.get(1) * 2 + mapCounter.get(2) * 5 + mapCounter.get(3) * 8 + mapCounter.get(4) * 12
				+ mapCounter.get(5) * 16 + mapCounter.get(6) * 21;
	}

	/**
   * Recursive method to count elks in a specific pattern (Pattern 1).
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate being checked.
   * @param visitedHabitat A map tracking the visited habitats.
   * @return The count of elk based on the specified pattern.
   */
	private int countElk1(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat) {
		if (visitedHabitat.getOrDefault(coordinate, false)) {
			return 0;
		}

		Habitat habitat = player.getHabitat(coordinate);
		if (habitat != null && habitat.animal().equals(Animals.ELK)) {
			visitedHabitat.put(coordinate, true);
			return 1 + countElk1(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1), visitedHabitat)
					+ countElk1(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat);
		}
		return 0;
	}

	/**
   * Recursive method to count elks for pattern 3 (with special tile shapes).
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate being checked.
   * @param visitedHabitat A map tracking the visited habitats.
   * @return The count of elk based on the specified pattern.
   */
	private int countElk3(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat) {
		if (visitedHabitat.getOrDefault(coordinate, false)) {
			return 0;
		}

		Habitat habitat = player.getHabitat(coordinate);
		if (habitat != null && habitat.animal().equals(Animals.ELK)) {
			visitedHabitat.put(coordinate, true);
			if (shapeOfTile == 2) {
				if (coordinate.y() % 2 != 0) {
					return 1 + countElk3(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x() - 1, coordinate.y() + 1), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x() - 1, coordinate.y() - 1), visitedHabitat);
				} else {
					return 1 + countElk3(player, new Coordinate(coordinate.x() + 1, coordinate.y() - 1), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
							+ countElk3(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat);
				}

			} else {
				return 1 + countElk3(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
						+ countElk3(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
						+ countElk3(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
						+ countElk3(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat);
			}
		}
		return 0;
	}

	/**
   * Helper method to check if a specific coordinate has an adjacent elk animal.
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate being checked.
   * @param visitedHabitat A map tracking the visited habitats.
   * @return 1 if the coordinate contains an adjacent elk, otherwise 0.
   */
	private int neighbourAnimal(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat) {
		if (player.habitats().containsKey(coordinate)) {
			if (player.getHabitat(coordinate).animal().equals(Animals.ELK) && !visitedHabitat.containsKey(coordinate)) {
				visitedHabitat.put(coordinate, true);
				return 1;
			}
		}
		return 0;
	}

	/**
   * Recursive method to count how many elk surround a specific habitat.
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate being checked.
   * @param visitedHabitat A map tracking the visited habitats.
   * @return The count of elks surrounding the specified habitat.
   */
	private int surroundByElk(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat) {
		if (visitedHabitat.getOrDefault(coordinate, false)) {
			return 0;
		}

		Habitat habitat = player.getHabitat(coordinate);
		if (habitat != null && !habitat.animal().equals(Animals.ELK)) {
			visitedHabitat.put(coordinate, true);
			if (shapeOfTile == 2) {
				if (coordinate.y() % 2 != 0) {
					return 1 + neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y() + 1), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y() - 1), visitedHabitat);
				} else {
					return 1 + neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y() - 1), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
							+ neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat);
				}
			}
			return 1 + neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat)
					+ neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat)
					+ neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat)
					+ neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat);
		}
		return 0;
	}

	private int specifiqueForm(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat,
			HashMap<Integer, Integer> mapOfCounter) {
		if (visitedHabitat.getOrDefault(coordinate, false)) {
			return 0;
		}

		Habitat habitat = player.getHabitat(coordinate);
		if (habitat != null && !habitat.animal().equals(Animals.ELK) && !visitedHabitat.containsKey(coordinate)) {
			visitedHabitat.put(coordinate, true);
			if (player.habitats().containsKey(new Coordinate(coordinate.x() - 1, coordinate.y()))
					&& player.habitats().containsKey(new Coordinate(coordinate.x() + 1, coordinate.y()))
					&& player.habitats().containsKey(new Coordinate(coordinate.x() + 1, coordinate.y() + 1))) {
				var habitat2 = player.getHabitat(new Coordinate(coordinate.x() - 1, coordinate.y())).animal();
				var habitat3 = player.getHabitat(new Coordinate(coordinate.x() + 1, coordinate.y())).animal();
				var habitat4 = player.getHabitat(new Coordinate(coordinate.x() + 1, coordinate.y() + 1)).animal();
				if (habitat2.equals(Animals.ELK) && habitat3.equals(Animals.ELK) && habitat4.equals(Animals.ELK)
						&& !visitedHabitat.containsKey(new Coordinate(coordinate.x() - 1, coordinate.y()))
						&& !visitedHabitat.containsKey(new Coordinate(coordinate.x() + 1, coordinate.y()))
						&& !visitedHabitat.containsKey(new Coordinate(coordinate.x() + 1, coordinate.y() + 1))) {
					mapOfCounter.put(4, mapOfCounter.get(4) + 1);
					visitedHabitat.put(new Coordinate(coordinate.x() - 1, coordinate.y()), true);
					visitedHabitat.put(new Coordinate(coordinate.x() + 1, coordinate.y()), true);
					visitedHabitat.put((new Coordinate(coordinate.x() + 1, coordinate.y() + 1)), true);
					return 1;
				}
			}
			if (player.habitats().containsKey(new Coordinate(coordinate.x() + 1, coordinate.y()))
					&& player.habitats().containsKey(new Coordinate(coordinate.x(), coordinate.y() - 1))) {
				var habitat2 = player.getHabitat(new Coordinate(coordinate.x(), coordinate.y() - 1)).animal();
				var habitat4 = player.getHabitat(new Coordinate(coordinate.x() + 1, coordinate.y())).animal();
				if (habitat4.equals(Animals.ELK) && habitat2.equals(Animals.ELK)
						&& !visitedHabitat.containsKey(new Coordinate(coordinate.x() + 1, coordinate.y()))
						&& !visitedHabitat.containsKey(new Coordinate(coordinate.x(), coordinate.y() - 1))) {
					mapOfCounter.put(3, mapOfCounter.get(3) + 1);
					visitedHabitat.put(new Coordinate(coordinate.x() + 1, coordinate.y()), true);
					visitedHabitat.put((new Coordinate(coordinate.x(), coordinate.y() - 1)), true);
					return 1;
				}
			}
			if (player.habitats().containsKey(new Coordinate(coordinate.x() + 1, coordinate.y()))) {
				var habitat3 = player.getHabitat(new Coordinate(coordinate.x() + 1, coordinate.y())).animal();
				if (habitat3.equals(Animals.ELK)
						&& !visitedHabitat.containsKey(new Coordinate(coordinate.x() + 1, coordinate.y()))) {
					mapOfCounter.put(2, mapOfCounter.get(2) + 1);
					visitedHabitat.put(new Coordinate(coordinate.x() + 1, coordinate.y()), true);
					return 1;
				}
			}
			mapOfCounter.put(1, mapOfCounter.get(1) + 1);
			return 1;
		}
		return 0;
	}

	/**
   * Initializes the map counter for counting elk in the specific patterns.
   * 
   * @return A map with the initialized counts for each pattern group.
   */
	HashMap<Integer, Integer> initializeMap() {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(1, 0);
		map.put(2, 0);
		map.put(3, 0);
		map.put(4, 0);
		map.put(5, 0);
		map.put(6, 0);
		map.put(7, 0);
		map.put(8, 0);
		return map;
	}

	/**
   * Adds the appropriate count to the pattern 1 map based on the count of elk.
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate being checked.
   * @param visitedHabitatForAnimal A map tracking the visited habitats.
   * @param mapCounter A map tracking the counts of the pattern groups.
   */
	private void addToPattern1(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitatForAnimal,
			HashMap<Integer, Integer> mapCounter) {
		switch (countElk1(player, coordinate, visitedHabitatForAnimal)) {
			case 1 -> mapCounter.put(1, mapCounter.get(1) + 1);
			case 2 -> mapCounter.put(2, mapCounter.get(2) + 1);
			case 3 -> mapCounter.put(3, mapCounter.get(3) + 1);
			case 4 -> mapCounter.put(4, mapCounter.get(4) + 1);
		}
	}

	/**
   * Adds the appropriate count to the pattern 3 map based on the count of elk.
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate being checked.
   * @param visitedHabitatForAnimal A map tracking the visited habitats.
   * @param mapCounter A map tracking the counts of the pattern groups.
   */
	private void addToPattern3(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitatForAnimal,
			HashMap<Integer, Integer> mapCounter) {
		switch (countElk3(player, coordinate, visitedHabitatForAnimal)) {
			case 1 -> mapCounter.put(1, mapCounter.get(1) + 1);
			case 2 -> mapCounter.put(2, mapCounter.get(2) + 1);
			case 3 -> mapCounter.put(3, mapCounter.get(3) + 1);
			case 4 -> mapCounter.put(4, mapCounter.get(4) + 1);
			case 5 -> mapCounter.put(5, mapCounter.get(5) + 1);
			case 6 -> mapCounter.put(6, mapCounter.get(6) + 1);
			case 7 -> mapCounter.put(7, mapCounter.get(7) + 1);
			case 8 -> mapCounter.put(8, mapCounter.get(8) + 1);
		}
	}

	/**
   * Adds the appropriate count to the pattern 4 map based on the count of elk.
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate being checked.
   * @param visitedHabitatForAnimal A map tracking the visited habitats.
   * @param mapCounter A map tracking the counts of the pattern groups.
   */
	private void addToPattern4(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitatForAnimal,
			HashMap<Integer, Integer> mapCounter) {
		switch (surroundByElk(player, coordinate, visitedHabitatForAnimal)) {
			case 1 -> mapCounter.put(1, mapCounter.get(1) + 1);
			case 2 -> mapCounter.put(2, mapCounter.get(2) + 1);
			case 3 -> mapCounter.put(3, mapCounter.get(3) + 1);
			case 4 -> mapCounter.put(4, mapCounter.get(4) + 1);
			case 5 -> mapCounter.put(5, mapCounter.get(5) + 1);
			case 6 -> mapCounter.put(6, mapCounter.get(6) + 1);
		}
	}

	/**
	 * Counts the number of elk in the player's habitats based on the selected pattern.
	 * This method iterates through the player's habitats, visiting each one and updating the mapCounter
	 * based on the selected counting pattern.
	 * 
	 * @param player The player whose habitats are being examined.
	 * @param mapCounter A map that keeps track of the counts for each pattern group.
	 */
	private void counter(Player player, HashMap<Integer, Integer> mapCounter) {
		Objects.requireNonNull(player, "player is null");

		HashMap<Coordinate, Boolean> visitedHabitatForAnimal = new HashMap<>();
		for (Coordinate coordinate : player.habitats().keySet()) {
			Habitat habitat = player.getHabitat(coordinate);
			if (habitat != null && !visitedHabitatForAnimal.getOrDefault(coordinate, false)) {
				if (pattern == 1) {
					addToPattern1(player, coordinate, visitedHabitatForAnimal, mapCounter);
				} else if (pattern == 2) {
					specifiqueForm(player, coordinate, visitedHabitatForAnimal, mapCounter);
				} else if (pattern == 3) {
					addToPattern3(player, coordinate, visitedHabitatForAnimal, mapCounter);
				} else if (pattern == 4) {
					addToPattern4(player, coordinate, visitedHabitatForAnimal, mapCounter);
				}
			}
		}
	}

	/**
	 * Calculates the total points for a player based on the selected pattern and the count of elk 
	 * in their habitats. This method uses the counter method to collect the count of elk for the 
	 * specified pattern and then returns the total score.
	 * 
	 * @param player The player whose total points are being calculated.
	 * @return The total points based on the selected pattern and elk count.
	 */
	@Override
	public int pointCount(Player player) {
		var mapCounter = initializeMap();
		counter(player, mapCounter);
		System.out.println(mapCounter);
		return switch (pattern) {
			case 1 -> resultOfCount1(mapCounter);
			case 2 -> resultOfCount1(mapCounter);
			case 3 -> resultOfCount3(mapCounter);
			case 4 -> resultOfCount1(mapCounter);
			default -> resultOfCount1(mapCounter);
		};
	}
}