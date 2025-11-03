package fr.uge.game.element;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Habitat, which contains a tile, an animal, and a rotation value.
 */
public class Habitat {
	private final Tile tile;
	private Animals animalPresent;
	private final int rotation;
	
	/**
   * Constructs a Habitat with the specified tile, animal, and rotation value.
   * 
   * @param tile the tile associated with this habitat
   * @param animalPresent the animal currently present in the habitat
   * @param rotation the rotation value for the habitat (must be between -1 and 6)
   * @throws NullPointerException if the tile or animal is null
   * @throws IllegalArgumentException if the rotation value is less than -1 or greater than 6
   */
	public Habitat(Tile tile, Animals animalPresent, int rotation) {
    this.tile = Objects.requireNonNull(tile, "Tile cannot be null");
    this.animalPresent = Objects.requireNonNull(animalPresent, "Animal present cannot be null");
    if (rotation < -1 || rotation > 6) {
    	throw new IllegalArgumentException("rotation < 1 or rotation > 3");
    }
    this.rotation = rotation;
	}
	
	/**
   * Creates a default Habitat with a default tile, no animals, and a rotation of 0.
   * 
   * @return the default habitat
   */
	public static Habitat defaultHabitat() {
    Tile defaultTile = new Tile(List.of(Animals.NOTHING), List.of(Biome.NOTHING));
    return new Habitat(defaultTile, Animals.NOTHING, 0);
  }
	
	/**
   * Checks if this habitat is equal to another object.
   * Two habitats are considered equal if their tiles and animals are equal.
   * 
   * @param o the object to compare to
   * @return {@code true} if the object is equal to this habitat, {@code false} otherwise
   * @throws NullPointerException if the object is null
   */
	@Override
	public boolean equals(Object o) {
    Objects.requireNonNull(o, "o is null");
  	return o instanceof Habitat habitat && this.tile.equals(habitat.tile) && this.animalPresent.equals(habitat.animalPresent);
	}
	
	/**
   * Returns the tile associated with this habitat.
   * 
   * @return the tile
   */
	public Tile tile() {
		return tile;
	}
	
	/**
   * Returns the animal currently present in this habitat.
   * 
   * @return the animal present
   */
	public Animals animal() {
		return animalPresent;
	}
	
	/**
   * Returns the rotation value of this habitat.
   * 
   * @return the rotation value
   */
	public int rotation() {
		return rotation;
	}
	
	/**
   * Changes the animal present in this habitat.
   * 
   * @param animal the new animal to set
   * @throws NullPointerException if the animal is null
   */
	public void changeAnimal(Animals animal) {
		Objects.requireNonNull(animal, "animal is null");
  	this.animalPresent = animal;
  }

	/**
   * Returns the name of a biome at the specified index in the tile's biome list.
   * 
   * @param index the index of the biome
   * @return the name of the biome at the specified index
   * @throws IndexOutOfBoundsException if the index is out of range
   */
	public String biomeToString(int index) {
		return tile.biomeToString(index);
	}
	
	/**
   * Returns the list of animals present in the tile.
   * 
   * @return the list of animals
   */
	public List<Animals> getAnimals() {
		return tile.getAnimals();
	}
	
	/**
   * Returns a string representation of the habitat.
   * The string includes the tile representation and the name of the animal present.
   * 
   * @return a string representation of the habitat
   */
	@Override
	public String toString() {
		return tile.toString() + " " +  animalPresent.name().substring(0, 2).toLowerCase();
	}
}