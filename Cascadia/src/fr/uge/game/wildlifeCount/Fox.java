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
 * Represents a Fox pattern in the WildlifeCount game, which counts the number of specific animal formations in the habitats.
 * Implements the WildlifeCount interface.
 */
public record Fox(String name, int pattern, int shapeOfTile) implements WildlifeCount{
	
	/**
   * Constructs a Fox object. Throws exceptions if any argument is invalid.
   * 
   * @param name The name of the Fox.
   * @param pattern The pattern type (between 1 and 4).
   * @param shapeOfTile The shape of the tile (should be 0, 1, or 2).
   */
	public Fox {
		Objects.requireNonNull(name, "name is null");
		if (pattern < 1 || pattern > 4) {
			throw new IllegalArgumentException("pattern s different to 5");
		}
		if(shapeOfTile < 0 || shapeOfTile > 2) {
			throw new IllegalArgumentException("shapeOfTile < 0 or shapeOfTile > 2");
		}
	}
	
	/**
   * Calculates the points for a given family card (type 1) based on the counter.
   * 
   * @param counter The number of animals in the formation.
   * @return The points for the family card of type 1.
   */
	private int resultOfCount1(int counter) {
		return switch(counter) {
						case 1 -> 1;
						case 2 -> 2;
						case 3 -> 3;
						case 4 -> 4;
						case 5 -> 5;
						default -> 0;
					};
	}
	
	 /**
   * Calculates the points for a given family card (type 2) based on the counter.
   * 
   * @param counter The number of animals in the formation.
   * @return The points for the family card of type 2.
   */
	private int resultOfCount2(int counter) {
		return switch(counter) {
						case 1 -> 3;
						case 2 -> 5;
						case 3 -> 7;
						default -> 0;
					};
	}
	
	/**
   * Calculates the points for a given family card (type 3) based on the counter.
   * 
   * @param counter The number of animals in the formation.
   * @return The points for the family card of type 3.
   */
	private int resultOfCount3(int counter) {
		return switch(counter) {
						case 1 -> 1;
						case 2 -> 2;
						case 3 -> 3;
						case 4 -> 4;
						case 5 -> 5;
						case 6 -> 6;
						default -> 0;
					};
	}
	
	/**
   * Calculates the points for a given family card (type 4) based on the counter.
   * 
   * @param counter The number of animals in the formation.
   * @return The points for the family card of type 4.
   */
	private int resultOfCount4(int counter) {
		return switch(counter) {
						case 1 -> 5;
						case 2 -> 7;
						case 3 -> 9;
						case 4 -> 11;
						default -> 0;
					};
	}
	
	 /**
   * Records the neighbouring animals in a given habitat to a map of animals.
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate to check.
   * @param visitedHabitat A map to track which habitats have been visited.
   * @param mapOfAnimals A map of animals to store the coordinates of animals found in the habitats.
   */
	private void neighbourAnimal(Player player,Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat, HashMap<Animals, ArrayList<Coordinate>> mapOfAnimals) {
		if(player.habitats().containsKey(coordinate)) {
			var animal = player.habitats().get(coordinate).animal();
			if(!animal.equals(Animals.FOX)) {
				visitedHabitat.put(coordinate, true);
			}
			if (!animal.equals(Animals.NOTHING)) {
				System.out.println(animal);
				System.out.println("AVANT : " + mapOfAnimals);
				mapOfAnimals.get(animal).add(coordinate);
				System.out.println("APRES : " + mapOfAnimals);
			}
		}
	}
	
	/**
   * Examines the surrounding area of a fox and marks visited habitats. It also records neighbouring animals.
   * 
   * @param player The player whose habitats are being examined.
   * @param coordinate The current coordinate to check.
   * @param visitedHabitat A map to track which habitats have been visited.
   * @param mapOfAnimals A map of animals to store the coordinates of animals found in the habitats.
   */
	private void surroundByFox(Player player, Coordinate coordinate, HashMap<Coordinate, Boolean> visitedHabitat, HashMap<Animals, ArrayList<Coordinate>> mapOfAnimals) {
		if (visitedHabitat.getOrDefault(coordinate, false)) {
        return;
    }

    Habitat habitat = player.getHabitat(coordinate);
    if (habitat != null && habitat.animal().equals(Animals.FOX)) {
        visitedHabitat.put(coordinate, true);
        if (shapeOfTile == 2) {
        	if (coordinate.y() % 2 != 0) {
        		neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y() + 1), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y() - 1), visitedHabitat, mapOfAnimals);
        	}
        	else {
    				neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y() - 1), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y() + 1), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, mapOfAnimals);
      			neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, mapOfAnimals);
        	}
        }
        else {
					neighbourAnimal(player, new Coordinate(coordinate.x() + 1, coordinate.y()), visitedHabitat, mapOfAnimals);
					neighbourAnimal(player, new Coordinate(coordinate.x() - 1, coordinate.y()), visitedHabitat, mapOfAnimals);
					neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() + 1), visitedHabitat, mapOfAnimals);
					neighbourAnimal(player, new Coordinate(coordinate.x(), coordinate.y() - 1), visitedHabitat, mapOfAnimals);
        }
    }
    return;
  }
	
	/**
   * Counts the number of unique animals in the map of animals.
   * 
   * @param mapOfAnimals The map of animals to check.
   * @return The count of different animals found in the map.
   */
	private int card1(HashMap<Animals, ArrayList<Coordinate>> mapOfAnimals) {
		var counter = 0;
		for(var animal : mapOfAnimals.keySet()) {
			System.out.println("ARRAY: " + mapOfAnimals.get(animal) + "SIZE : " + mapOfAnimals.get(animal).size() + "ANIMAL : " + animal);
			if(mapOfAnimals.get(animal).size() != 0) {
				counter++;
			}
		}
		return counter;
	}
	
	/**
   * Counts the number of pairs of animals in the map of animals, excluding foxes.
   * 
   * @param mapOfAnimals The map of animals to check.
   * @return The count of pairs of animals found in the map.
   */
	private int card2(HashMap<Animals, ArrayList<Coordinate>> mapOfAnimals) {
		var counter = 0;
		for(var animal : mapOfAnimals.keySet()) {
			if(mapOfAnimals.get(animal).size() % 2 == 0 && !mapOfAnimals.get(animal).isEmpty() && !animal.equals(Animals.FOX)) {
				counter += mapOfAnimals.get(animal).size() / 2;
			}
		}
		return counter;
	}
	
	/**
   * Returns the maximum count of any single type of animal in the map.
   * 
   * @param mapOfAnimals The map of animals to check.
   * @return The maximum count of a single type of animal.
   */
	private int card3(HashMap<Animals, ArrayList<Coordinate>> mapOfAnimals) {
		var max = 0;
		for(var animal : mapOfAnimals.keySet()) {
			if(mapOfAnimals.get(animal).size() > max && !animal.equals(Animals.FOX)) {
				max = mapOfAnimals.get(animal).size();
			}
		}
		return max;
	}
	
	/**
	 * Counts the points for card type 4 (Fox) based on surrounding animals.
	 * If there is only one fox, it will examine its surroundings and adjust the count.
	 * 
	 * @param mapOfAnimals A map that contains the coordinates of different animals.
	 * @param player The player whose habitats are being examined.
	 * @param visitedHabitat A map to keep track of visited habitats.
	 * @return The adjusted count of animals based on the rules for card type 4.
	 */
	private int card4(HashMap<Animals, ArrayList<Coordinate>> mapOfAnimals, Player player, HashMap<Coordinate, Boolean> visitedHabitat) {
		var arrayFox = mapOfAnimals.get(Animals.FOX);
		if(arrayFox.size() == 1) {
			surroundByFox(player, arrayFox.get(0), visitedHabitat, mapOfAnimals);
		}
		var counter = card2(mapOfAnimals);
		return counter - 1;
	}
	
	/**
	 * Initializes the map that will track animals' positions in the habitats.
	 * This method initializes an empty list for each type of animal.
	 * 
	 * @return A HashMap where each key is an animal type and each value is an empty list of coordinates.
	 */
	private HashMap<Animals, ArrayList<Coordinate>> initializeMap() {
		HashMap<Animals, ArrayList<Coordinate>> map = new HashMap<>();
		map.put(Animals.BEAR, new ArrayList<>());
		map.put(Animals.SALMON, new ArrayList<>());
		map.put(Animals.BUZZARD, new ArrayList<>());
		map.put(Animals.ELK, new ArrayList<>());
		map.put(Animals.FOX, new ArrayList<>());
		return map;
	}
	
	/**
	 * Calculates the total points based on the player's habitats and the specific pattern rules.
	 * It checks each habitat for animals and calculates points according to the selected pattern (1 to 4).
	 * 
	 * @param player The player whose habitats are being examined for point calculation.
	 * @return The total points for the player based on the selected pattern.
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
    		var mapOfAnimals = initializeMap();
    		surroundByFox(player, coordinate, visitedHabitatForAnimal, mapOfAnimals);
    		counter = switch(pattern) {
						  	    case 1 -> card1(mapOfAnimals);
						  	    case 2 -> card2(mapOfAnimals);
						  	    case 3 -> card3(mapOfAnimals);
						  	    case 4 -> card4(mapOfAnimals, player, visitedHabitatForAnimal);
						  	    default-> 0;
						      };
						  
				result +=	switch(pattern) {
								    case 1 -> resultOfCount1(counter);
								    case 2 ->resultOfCount2(counter);
								    case 3 -> resultOfCount3(counter);
								    case 4 -> resultOfCount4(counter);
								    default-> 0;
							    };
    		}
    	}
    	return result;
  	}
}