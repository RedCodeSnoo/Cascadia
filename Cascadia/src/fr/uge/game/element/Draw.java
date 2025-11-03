package fr.uge.game.element;

import java.util.ArrayList;
import java.util.Objects;
import java.util.random.RandomGenerator;

import fr.uge.game.Structure;

/**
 * Class responsible for managing the draw phase, including selecting tiles and
 * animals for the player's choice in the game.
 */
public class Draw {
	public ArrayList<Tile> tilesForChoice = new ArrayList<>();
	public ArrayList<Animals> animalsForChoice = new ArrayList<>();
	Structure structure;
	Player player;

	/**
	 * Constructor for the Draw class.
	 * 
	 * @param structure The game structure, containing the available tiles and animal tokens.
	 * @throws NullPointerException if the structure, tilesForChoice, or animalsForChoice are null.
	 */
	public Draw(Structure structure) {
		Objects.requireNonNull(tilesForChoice, "tilesForChoice is null");
		Objects.requireNonNull(animalsForChoice, "animalsForChoice is null");
		Objects.requireNonNull(structure, "structure is null");
		this.structure = structure;
	}

	/**
	 * Initializes the list of tiles for the player's choice by randomly selecting 4 tiles
	 * from the available tiles in the structure and removing them.
	 */
	public void initializeTilesToChoice() {
		RandomGenerator rand = RandomGenerator.getDefault();
		var randomNumber = 0;
		for (var i = 0; i < 4; i++) {
			randomNumber = rand.nextInt(structure.tiles().size());
			tilesForChoice.add(structure.tiles().get(randomNumber));
			structure.tiles().remove(structure.tiles().get(randomNumber));
		}
	}

	/**
	 * Changes the animals available for the player's choice by removing 4 animals from the 
	 * animal tokens and adding new animals to the list of animals for choice.
	 */
	public void changeAnimalToChoice() {
		for (var i = 0; i < 4; i++) {
			structure.addOrSubtractAnimalToken(animalsForChoice.get(0), 1);
			animalsForChoice.remove(0);
		}
		initializeAnimalsToChoice();
	}

	/**
	 * Returns a randomly selected animal from the available animal tokens.
	 * 
	 * @return A random animal from the available tokens.
	 * @throws IllegalStateException if no animals are available.
	 */
	public Animals randomAnimal() {
		RandomGenerator rand = RandomGenerator.getDefault();
		Animals animal;
		do {
			animal = Animals.values()[rand.nextInt(Animals.values().length)];
		} while (structure.animalToken().get(animal) <= 0);
		return animal;
	}

	/**
	 * Initializes the list of animals for the player's choice by randomly selecting 4 animals
	 * from the available animal tokens.
	 */
	public void initializeAnimalsToChoice() {
		for (var i = 0; i < 4; i++) {
			var animal = randomAnimal();
			animalsForChoice.add(animal);
		}
	}

	/**
	 * Counts the number of occurrences of the same animal starting from a given index in the list 
	 * of animals for choice.
	 * 
	 * @param index The starting index to begin counting from.
	 * @return The count of the same animal starting from the given index.
	 */
	public int numberOfSameAnimal(int index) {
		var counter = 1;
		for (var i = index + 1; i < animalsForChoice.size(); i++) {
			if (animalsForChoice.get(index).equals(animalsForChoice.get(i))) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Initializes both the tiles and animals for the player's choice by calling respective 
	 * initialization methods.
	 */
	public void initialize() {
		initializeTilesToChoice();
		initializeAnimalsToChoice();
	}

	/**
	 * Appends the details of the animals for choice to a StringBuilder for a formatted string 
	 * representation.
	 * 
	 * @param builder The StringBuilder to append the animal names to.
	 */
	private void builderAppendLot(StringBuilder builder) {
		for (var i = 0; i < animalsForChoice.size(); i++) {
			builder.append(animalsForChoice.get(i).name().toLowerCase());
			if (animalsForChoice.get(i).name().length() < 7) {
				for (var j = 0; j < (7 - animalsForChoice.get(i).name().length()); j++) {
					builder.append(" ");
				}
			}
			for (var j = 0; j < 20 + i; j++) {
				builder.append(" ");
			}
		}
		builder.append("\n");
	}

	/**
	 * Returns a formatted string representation of the tiles and animals available for the player's 
	 * choice, including their names and layout.
	 * 
	 * @return A string representation of the available tiles and animals for choice.
	 */
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("\n");
		for (var i = 0; i < 4; i++) {
			builder.append("   Lot ").append(i + 1).append("                    ");
		}
		builder.append("\n");
		for (var i = 0; i < tilesForChoice.size(); i++) {
			builder.append(tilesForChoice.get(i));
			for (var j = 0; j < tilesForChoice.size() + 11 + i; j++) {
				builder.append(" ");
			}
		}
		builder.append("\n");
		builderAppendLot(builder);
		return builder.toString();
	}

	/**
	 * Returns the list of tiles available for the player's choice.
	 * 
	 * @return A list of tiles available for the player's choice.
	 */
	public ArrayList<Tile> tilesForChoice() {
		return this.tilesForChoice;
	}

	/**
	 * Returns the list of animals available for the player's choice.
	 * 
	 * @return A list of animals available for the player's choice.
	 */
	public ArrayList<Animals> animalsForChoice() {
		return animalsForChoice;
	}
}