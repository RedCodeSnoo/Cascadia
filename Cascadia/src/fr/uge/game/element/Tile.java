package fr.uge.game.element;

import java.util.List;
import java.util.Objects;


/**
 * Represents a Tile containing a list of animals and a list of biomes.
 * A Tile must have at least one animal and one biome.
 */
public record Tile(List<Animals> animals, List<Biome> biome) {
	
	 /**
   * Constructs a Tile with a list of animals and a list of biomes.
   * 
   * @param animals the list of animals on the tile
   * @param biome the list of biomes on the tile
   * @throws IllegalArgumentException if the list of animals or biomes is empty
   * @throws NullPointerException if the list of animals or biomes is null
   */
	public Tile {
		if(animals.isEmpty()) {
			throw new IllegalArgumentException("List animals is empty");
		}
		if(biome.isEmpty()) {
			throw new IllegalArgumentException("List biome is empty");
		}
		animals = List.copyOf(animals);
    biome = List.copyOf(biome);
	}
	
	/**
   * Returns the name of a biome at the specified index as a string.
   * 
   * @param index the index of the biome
   * @return the name of the biome at the specified index
   * @throws IndexOutOfBoundsException if the index is out of range
   */
	public String biomeToString(int index) {
		return biome.get(index).name();
	}
	
	/**
   * Returns an unmodifiable list of animals on the tile.
   * 
   * @return the list of animals
   */
	public List<Animals> getAnimals() {
		return animals;
	}
	
	/**
   * Returns an unmodifiable list of biomes on the tile.
   * 
   * @return the list of biomes
   */
	public List<Biome> biome() {
		return biome;
	}
	
	/**
   * Compares this Tile to the specified object. Two tiles are equal
   * if their lists of animals and biomes are equal.
   * 
   * @param o the object to compare to
   * @return {@code true} if the specified object is equal to this Tile, {@code false} otherwise
   * @throws NullPointerException if the object is null
   */
	@Override
  public boolean equals(Object o) {
      Objects.requireNonNull(o, "o is null");
    	return o instanceof Tile tile && this.animals.equals(tile.animals) && this.biome.equals(tile.biome);
  }
	
	/**
   * Returns a string representation of the Tile.
   * The string includes a shorthand representation of each biome and animal.
   * 
   * @return a string representation of the Tile
   */
	@Override
	public String toString() {
    var builder = new StringBuilder();
    if(!biome.isEmpty()) {
      builder.append("|");
      for (int i = 0; i < biome.size(); i++) {
      	builder.append(biome.get(i).name().substring(0, 2).toLowerCase()).append(" ");
      }
      builder.append("/ ");
    }
    for(var i = 0; i < animals.size(); i++) {
      builder.append(animals.get(i).name().substring(0, 2).toLowerCase());
      if(i < animals.size() - 1) {
        builder.append(" ");
      }
    }
    builder.append("/");
    return builder.toString();
  }
}