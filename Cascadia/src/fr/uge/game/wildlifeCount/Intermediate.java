package fr.uge.game.wildlifeCount;

import java.util.HashMap;
import java.util.Objects;

import fr.uge.game.WildlifeCount;
import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Habitat;
import fr.uge.game.element.Player;

/**
 * Represents an intermediary wildlife count pattern used for calculating points.
 * The class calculates the points based on a specified pattern and tile shape.
 */
public record Intermediate(String name, int pattern, int shapeOfTile) implements WildlifeCount{
	
	/**
   * Constructs an Intermediate object with the specified name, pattern, and shape of tile.
   * 
   * @param name The name of the intermediate object (cannot be null).
   * @param pattern The pattern to be used (must be 6).
   * @param shapeOfTile The shape of the tile (must be between 0 and 2).
   * @throws IllegalArgumentException if the pattern is not 6 or if the shapeOfTile is out of range.
   */
	public Intermediate{
		Objects.requireNonNull(name, "name is null");
		if (pattern != 6) {
			throw new IllegalArgumentException("pattern s different to 5");
		}
		if(shapeOfTile < 0 || shapeOfTile > 2) {
			throw new IllegalArgumentException("shapeOfTile < 0 or shapeOfTile > 2");
		}
	}
	
	/**
   * Calculates the points for intermediary cards based on the count of animals.
   * The point calculation follows different rules depending on the animal count.
   * 
   * @param counter The number of animals found in the player's habitats.
   * @return The points calculated for the given animal count.
   */
	private int cardOfIntermediaryPoint(int counter) {
		return switch(counter) {
			case 2 -> 5;
			case 3 -> 8;
			default ->  (counter >= 4) ? 12 : 0;
		};
  }
	
	/**
   * Recursively counts the number of animals of a specific type in the player's habitats, 
   * starting from a given coordinate. It checks all adjacent coordinates.
   * 
   * @param player The player whose habitats are being checked.
   * @param coordinate The starting coordinate for the animal search.
   * @param visitedHabitat A map tracking which habitats have been visited.
   * @param animal The type of animal to count.
   * @return The total count of the specified animal in the player's habitats.
   */
	private int animalsPointCount(Player player,Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat, Animals animal) {
    if (visitedHabitat.getOrDefault(coordinate, false)) {
    	return 0;
    }
    Habitat habitat = player.getHabitat(coordinate);
    if (habitat != null && habitat.animal().equals(animal)) {
    	visitedHabitat.put(coordinate, true);
      if(shapeOfTile == 2) {
      	if (coordinate.y() % 2 != 0) {
      		return 1 +
      				animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, animal) +
      				animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y() + 1), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y() - 1), visitedHabitat, animal);
      	}
      	else {
      		return 1 +
      				animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y() - 1), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, animal) +
        			animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, animal);
      	}
      }
      else {
      	 return 1 +
      			 animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, animal) +
      			 animalsPointCount(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, animal) +
      			 animalsPointCount(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, animal) +
      			 animalsPointCount(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, animal);
      }
    }
    return 0;
  }
  
	/**
   * Calculates the total points for the player based on the selected pattern and tile shape.
   * It checks each habitat for animals and calculates points accordingly.
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
    	if (habitat != null && !habitat.animal().equals(Habitat.defaultHabitat().animal()) && !visitedHabitatForAnimal.getOrDefault(coordinate, false)) {
    		var counter = 0;
    		counter = animalsPointCount(player, coordinate, visitedHabitatForAnimal, habitat.animal());
    		result = cardOfIntermediaryPoint(counter);
      }
    }
    return result;
    
 
  }
}