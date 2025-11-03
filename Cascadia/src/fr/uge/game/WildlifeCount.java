package fr.uge.game;

import fr.uge.game.element.Player;


/**
 * Represents the count and scoring logic for different types of wildlife in the game.
 * This sealed interface is implemented by specific wildlife classes such as Fox, Bear, etc.
 */
public interface WildlifeCount {
	
	/**
   * Returns the name of the wildlife.
   * 
   * @return the name of the wildlife as a String.
   */
	String name();
	
	/**
   * Returns the unique pattern associated with the wildlife.
   * This pattern is used for game mechanics such as tile placement or scoring.
   * 
   * @return the pattern as an integer.
   */
	int pattern();
	
	/**
   * Calculates and returns the total points for a player based on this wildlife type.
   * 
   * @param player the player for whom the points are being calculated.
   * @return the total points as an integer.
   */
	int pointCount(Player player);
}
