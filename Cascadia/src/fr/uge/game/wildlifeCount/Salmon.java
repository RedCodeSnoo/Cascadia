package fr.uge.game.wildlifeCount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.uge.game.WildlifeCount;
import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;

/**
 * Represents a salmon wildlife count pattern used for calculating points.
 * The class calculates the points based on the specified pattern and tile shape.
 */
public record Salmon(String name, int pattern, int shapeOfTile) implements WildlifeCount{
	
	/**
   * Constructs a Salmon object with the specified name, pattern, and shape of tile.
   * 
   * @param name The name of the salmon object (cannot be null).
   * @param pattern The pattern to be used (must be between 1 and 4).
   * @param shapeOfTile The shape of the tile (must be between 0 and 2).
   * @throws IllegalArgumentException if the pattern is not between 1 and 4 or if the shapeOfTile is out of range.
   */
	public Salmon {
		Objects.requireNonNull(name, "name is null");
		if (pattern < 1 || pattern > 4) {
			throw new IllegalArgumentException("pattern s different to 5");
		}
		if(shapeOfTile < 0 || shapeOfTile > 2) {
			throw new IllegalArgumentException("shapeOfTile < 0 or shapeOfTile > 2");
		} 
	}
	
	/**
   * Calculates the result based on the count of salmon for pattern 1.
   * 
   * @param counter The count of salmon found in the player's habitats.
   * @return The points based on the counter value for pattern 1.
   */
	private int resultOfCount1(int counter) {
		return switch(counter) {
						case 1 -> 2;
						case 2 -> 5;
						case 3 -> 8;
						case 4 -> 12;
						case 5 -> 16;
						case 6 -> 20;
						default -> (counter >= 7) ? 25 : 0;
					};
	}
	
	/**
   * Calculates the result based on the count of salmon for pattern 2.
   * 
   * @param counter The count of salmon found in the player's habitats.
   * @return The points based on the counter value for pattern 2.
   */
	private int resultOfCount2(int counter) {
		return switch(counter) {
						case 1 -> 2;
						case 2 -> 4;
						case 3 -> 9;
						case 4 -> 11;
						default -> (counter >= 5) ? 17 : 0;
					};
	}
	
	/**
   * Calculates the result based on the count of salmon for pattern 3.
   * 
   * @param counter The count of salmon found in the player's habitats.
   * @return The points based on the counter value for pattern 3.
   */
	private int resultOfCount3(int counter) {
		return switch(counter) {
							case 3 -> 10;
							case 4 -> 12;
							default -> (counter >= 5) ? 15 : 0;
						};
	}
	
	/**
   * Returns the number of points for pattern 4 based on the counter value.
   * 
   * @param counter The count of salmon found in the player's habitats.
   * @return The points for pattern 4 based on the counter value.
   */
	private int resultOfCount4(int counter) {
		return counter;
	}
	
	/**
   * Adds adjacent coordinates to a list based on the tile shape.
   * 
   * @param coordinate The current coordinate to calculate adjacent ones.
   * @param directions The list to store the calculated directions.
   */
	private void addDirection(Coordinate coordinate, List<Coordinate> directions) {
		if (shapeOfTile == 2) {
    	if (coordinate.y() % 2 != 0) {
        directions.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
        directions.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
        directions.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
        directions.add(new Coordinate(coordinate.x() - 1, coordinate.y() + 1));
        directions.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
        directions.add(new Coordinate(coordinate.x() - 1, coordinate.y() - 1));
    	} else {
    		directions.add(new Coordinate(coordinate.x() + 1, coordinate.y() - 1));
        directions.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
        directions.add(new Coordinate(coordinate.x() + 1, coordinate.y() + 1));
        directions.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
        directions.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
        directions.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
    	}
    } else {
        directions.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
        directions.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
        directions.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
        directions.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
    }
	}
	
	/**
   * Counts the number of salmon in the player's habitats starting from a given coordinate.
   * It checks adjacent coordinates and recursively counts all connected salmon.
   * 
   * @param player The player whose habitats are being checked.
   * @param coordinate The starting coordinate for the search.
   * @param visitedHabitat A map that tracks visited habitats.
   * @param otherAnimals A counter for other animals near the salmon.
   * @return The total count of salmon found.
   */
	private int countSalmon(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat, int otherAnimals) {
    if (visitedHabitat.getOrDefault(coordinate, false)) {
    	return 0;
    }
    Habitat habitat = player.getHabitat(coordinate);
    if (!habitat.animal().equals(Animals.SALMON)) {
    	return 0;
    }
    visitedHabitat.put(coordinate, true);
    List<Coordinate> directions = new ArrayList<>();
    addDirection(coordinate, directions);

    int neighboursCount = 0;
    for (var direction : directions) {
      if (player.habitats().containsKey(direction)) {
        if (player.getHabitat(direction).animal().equals(Animals.SALMON)) {
            neighboursCount++;
        }
        if (!player.getHabitat(direction).animal().equals(Animals.SALMON) && !player.getHabitat(direction).animal().equals(Animals.NOTHING) && !visitedHabitat.containsKey(direction)) {
        	visitedHabitat.put(direction, true);
        	otherAnimals++;
        }
      }
    }
    if (neighboursCount > 2) {
    	return 0;
    }
    int total = 1;
    for (Coordinate direction : directions) {
    	if(player.habitats().containsKey(direction)) {
    		total = countSalmon(player, direction, visitedHabitat, otherAnimals);
      }
    }
    if(pattern == 4) {
    	return total + otherAnimals;
    }
    return total;
	}

	/**
   * Calculates the total points for the player based on the selected pattern and shape of tile.
   * It checks each habitat for salmon and calculates points accordingly.
   * 
   * @param player The player whose habitats and animals are being checked.
   * @return The total points for the player based on the current pattern.
   */
	@Override
	public int pointCount(Player player) {
    Objects.requireNonNull(player, "player is null");

    HashMap<Coordinate, Boolean> visitedHabitatForAnimal = new HashMap<>();

    var result = 0;
    for (Coordinate coordinate : player.habitats().keySet()) {
    	Habitat habitat = player.getHabitat(coordinate);
    	if (habitat != null && !visitedHabitatForAnimal.getOrDefault(coordinate, false)) {
    		var counter = 0;
        int otherAnimal = 0;
        counter = countSalmon(player,coordinate, visitedHabitatForAnimal, otherAnimal);
        result += switch(pattern) {
						  	    case 1 -> resultOfCount1(counter);
						  	    case 2 ->resultOfCount2(counter);
						  	    case 3 -> resultOfCount3(counter);
						  	    case 4 -> resultOfCount4(counter) + 1;
						  	    default-> 0;
						      };
    	}
    }
    return result;
  }

}