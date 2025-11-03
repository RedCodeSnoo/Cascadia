package fr.uge.game.wildlifeCount;

import java.util.HashMap;
import java.util.Objects;

import fr.uge.game.WildlifeCount;
import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;

/**
 * Represents a Family card in the WildlifeCount game, which counts the number of specific animal formations in the habitats.
 * Implements the WildlifeCount interface.
 */
public record Family(String name, int pattern, int shapeOfTile) implements WildlifeCount {

	/**
   * Constructs a Family object. Throws exceptions if any argument is invalid.
   * 
   * @param name The name of the family.
   * @param pattern The pattern type (must be 5).
   * @param shapeOfTile The shape of the tile (should be 0, 1, or 2).
   */
	public Family {
		Objects.requireNonNull(name, "name is null");
		if (pattern != 5) {
			throw new IllegalArgumentException("pattern s different to 5");
		}
		if (shapeOfTile < 0 || shapeOfTile > 2) {
			throw new IllegalArgumentException("shapeOfTile < 0 or shapeOfTile > 2");
		}
	}

	/**
   * Calculates the points for a given family card based on the counter (number of animals in a formation).
   * 
   * @param counter The number of animals in a formation.
   * @return The points for the family card based on the counter.
   */
	private int cardOfFamilyPoint(int counter) {
		return switch (counter) {
			case 1 -> 2;
			case 2 -> 5;
			default -> (counter >= 3) ? 9 : 0;
		};
	}

	 /**
   * Recursively counts the number of animals of a specific type in a player's habitats,
   * and ensures that all animals in a specific shape or pattern are accounted for.
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate to check.
   * @param visitedHabitat A map that tracks the habitats that have already been visited.
   * @param animal The animal type to count in the habitats.
   * @return The number of animals in the formation.
   */
	private int animalsPointCount(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat,
			Animals animal) {

		if (visitedHabitat.getOrDefault(coordinate, false)) {
			return 0;
		}

		Habitat habitat = player.getHabitat(coordinate);
		if (habitat != null && habitat.animal().equals(animal)) {
			visitedHabitat.put(coordinate, true);
			if (shapeOfTile == 2) {
				if (coordinate.y() % 2 != 0) {
					return 1
							+ animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y() + 1), visitedHabitat,
									animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y() - 1), visitedHabitat,
									animal);
				} else {
					return 1
							+ animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y() - 1), visitedHabitat,
									animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1), visitedHabitat,
									animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, animal)
							+ animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, animal);
				}
			} else {
				return 1 + animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, animal)
						+ animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, animal)
						+ animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, animal)
						+ animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, animal);
			}
		}
		return 0;
	}

	/**
   * Calculates the total points for the player based on the family's pattern and the animals in their habitats.
   * 
   * @param player The player whose total points are being calculated.
   * @return The total points based on the family's pattern and animal formations.
   */
	@Override
	public int pointCount(Player player) {
		Objects.requireNonNull(player, "player is null");

		HashMap<Coordinate, Boolean> visitedHabitatForAnimal = new HashMap<>();
		var result = 0;
		for (Coordinate coordinate : player.habitats().keySet()) {
			Habitat habitat = player.getHabitat(coordinate);
			if (habitat != null && !habitat.animal().equals(Habitat.defaultHabitat().animal())
					&& !visitedHabitatForAnimal.getOrDefault(coordinate, false)) {
				var counter = 0;
				counter = animalsPointCount(player, coordinate, visitedHabitatForAnimal, habitat.animal());

				result += cardOfFamilyPoint(counter);
				if (counter != 0) {
					System.out.println("counter : " + counter);
					System.out.println("result : " + result);
				}
			}
		}
		return result;

	}
}