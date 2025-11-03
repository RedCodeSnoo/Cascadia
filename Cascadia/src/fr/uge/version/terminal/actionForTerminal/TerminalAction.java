package fr.uge.version.terminal.actionForTerminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.uge.game.element.Animals;
import fr.uge.game.element.Coordinate;
import fr.uge.game.element.Draw;
import fr.uge.game.element.Player;
import fr.uge.game.element.Tile;
import fr.uge.version.Action;

/**
 * The TerminalAction class implements the Action interface and provides the logic for 
 * handling user interactions related to tiles, animals, and nature tokens in the game.
 */
public class TerminalAction implements Action {
	private Draw draw;
	private final Player player;

	/**
   * Constructor to initialize the TerminalAction with the given Draw and Player.
   *
   * @param draw the Draw object representing the game draw
   * @param player the Player object representing the player
   */
	public TerminalAction(Draw draw, Player player) {
		this.draw = Objects.requireNonNull(draw, "draw is null");
		this.player = Objects.requireNonNull(player, "player is null");
	}

	/**
   * Allows the player to add a tile to the game board by providing coordinates.
   * Ensures the tile is placed correctly based on the player's allowed neighboring squares.
   *
   * @param tile the tile to be added to the board
   * @throws IOException if there is an issue reading the input
   */
	public void addTileOnGameBoard(Tile tile) throws IOException {
  	Objects.requireNonNull(tile, "tile is null");
  	var reader = new BufferedReader(new InputStreamReader(System.in));
  	System.out.println("Veuillez entrer la coordonnée X et Y pour la tuile (exemple : 4 10):");
  	var coordonnees = reader.readLine().split(" ");
	  var x = Integer.parseInt(coordonnees[0]);
    var y = Integer.parseInt(coordonnees[1]);
    while(player.neighbourSquare(x, y) == false || (player.habitats().containsKey(new Coordinate(x, y)))) {
  		System.out.println("Coordonnées Incorrect, Veuillez de nouveau entrer la coordonnée X et Y pour la tuile (exemple : 4 10):");
  		reader = new BufferedReader(new InputStreamReader(System.in));
  		coordonnees = reader.readLine().split(" ");
  	  x = Integer.parseInt(coordonnees[0]);
      y = Integer.parseInt(coordonnees[1]);
    }
  	player.add(tile, x, y, Animals.NOTHING, -1);
  }
  
	/**
   * Adds an animal to a specific habitat on the player's game board.
   * Ensures that the animal is placed correctly and updates the animal count.
   *
   * @param animal the animal to be added
   * @param hashMap the map of animals with their counts
   * @throws IOException if there is an issue reading the input
   */
  private void addAnimal(Animals animal, HashMap<Animals, Integer> hashMap) throws IOException {
  	var reader = new BufferedReader(new InputStreamReader(System.in));
  	System.out.println("Veuillez entrer la coordonnée X et Y pour placer l'animal (exemple : 4 10):");
  	var coordonnees = reader.readLine().split(" ");
	  var x = Integer.parseInt(coordonnees[0]);
    var y = Integer.parseInt(coordonnees[1]);
    while(!(player.habitats().containsKey(new Coordinate(x, y))) || (!(animal.equals(player.getHabitat(new Coordinate(x, y)).getAnimals().get(0))) && !(animal.equals(player.getHabitat(new Coordinate(x, y)).getAnimals().get(1))))){
    	System.out.println("Coordonnées Incorrect, Veuillez de nouveau entrer la coordonnée X et Y pour placer l'animal (exemple : 4 10):");
    	reader = new BufferedReader(new InputStreamReader(System.in));
    	coordonnees = reader.readLine().split(" ");
    	x = Integer.parseInt(coordonnees[0]);
      y = Integer.parseInt(coordonnees[1]);
    }
    player.addNatureToken(player.habitats().get(new Coordinate(x, y)).tile());
    player.habitats().get(new Coordinate(x, y)).changeAnimal(animal);
    for(var key : hashMap.keySet()) {
    	if(key.equals(animal)) {
    		hashMap.put(key, hashMap.get(key) - 1);
    		break;
    	}
    }
  }
	
  /**
   * Asks the player if they want to place an animal on the board and validates the choice.
   *
   * @param animals the animal to be placed
   * @param hashMap the map of animals with their counts
   * @throws IOException if there is an issue reading the input
   */
  public void addAnimalOnGameBoard(Animals animals, HashMap<Animals, Integer> hashMap) throws NumberFormatException, IOException {
  	Objects.requireNonNull(animals, "animal is null");
  	Objects.requireNonNull(hashMap, "hashMap is null");
  	
  	var reader = new BufferedReader(new InputStreamReader(System.in));
		int choice;
		do {
			System.out.println("Voulez-vous placer l'animal sur votre plateau ? Etes vous sûre de pouvoir le placer ? (Si oui entrer 1 sinon entrer 2)");
			choice = Integer.parseInt(reader.readLine());
		} while(choice != 1 && choice != 2);
		if(choice == 1) {
			addAnimal(animals, hashMap);
		}
	}
  
  /**
   * Handles the overpopulation situation by asking the player if they want to redraw an animal.
   *
   * @param animal the animal to be redrawn
   * @throws NumberFormatException if the input is not a valid number
   * @throws IOException if there is an issue reading the input
   */
  private void overpopulation(Animals animal) throws NumberFormatException, IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		int choice;
		do {
			System.out.println("Voulez-vous repiocher(si oui mettez 1 sinon 2)");
			choice = Integer.parseInt(reader.readLine());
		} while (choice != 1 && choice != 2);
		if(choice == 1) {
			redrawAnimal(animal);
		}
	}

  /**
   * Displays available tiles and handles the overpopulation situation if necessary.
   * Calls overpopulation or redrawAnimal depending on the tile counts.
   *
   * @throws NumberFormatException if the input is not a valid number
   * @throws IOException if there is an issue reading the input
   */
	public void availableTile() throws NumberFormatException, IOException {
		var counter1 = draw.numberOfSameAnimal(0);
		var counter2 = draw.numberOfSameAnimal(1);
		if(counter1 == 3) {
			overpopulation(draw.animalsForChoice().get(0));
			System.out.println(draw.toString());
		}
		if (counter2 == 3 && counter1 != 4) {
			overpopulation(draw.animalsForChoice().get(1));
			System.out.println(draw.toString());
		}
		if(counter1 == 4) {
			System.out.println("Veuillez re piocher");
			draw.changeAnimalToChoice();
			availableTile();
			System.out.println(draw);
		}
	}
	
	/**
   * Redraws an animal by replacing it with a random animal from the draw pool.
   *
   * @param animal the animal to be replaced
   * @throws NumberFormatException if the input is not a valid number
   * @throws IOException if there is an issue reading the input
   */
	private void redrawAnimal(Animals animal) throws NumberFormatException, IOException {
		for(int i = 0; i < draw.animalsForChoice.size(); i++) {
			if(draw.animalsForChoice.get(i).equals(animal)) {
				draw.animalsForChoice.set(i, draw.randomAnimal());
			}
		}
		availableTile();
	}
	
	/**
   * Handles the player's choice of tile and animal by prompting the user for input.
   * Validates the input to ensure both the tile and animal are within valid ranges.
   *
   * @return a list containing the tile and animal choices
   * @throws IOException if there is an issue reading the input
   */
	public List<Integer> handleTileAndAnimalChoice() throws IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		int animal, tile;
		List<Integer> numbers = new ArrayList<>();
    do {
    	System.out.println("Veuillez entrer le numéro du lot pour la tuile et le numéro de lot pour l'animal(exemple : 4 1):");
    	var coordonnees = reader.readLine().split(" ");
  	  tile = Integer.parseInt(coordonnees[0]);
      animal = Integer.parseInt(coordonnees[1]);
    } while((tile < 0 || tile > 4) || (animal < 0 || animal > 4));
    numbers.add(tile);
    numbers.add(animal);
    return numbers;
	}
	
	/**
   * Allows the player to choose a token to remove from the game.
   *
   * @param listOfToken the set of tokens available for removal
   * @throws NumberFormatException if the input is not a valid number
   * @throws IOException if there is an issue reading the input
   */
	private  void chooseTokenToRemove(Set<Integer> listOfToken) throws NumberFormatException, IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		int animal;
		do {
    	System.out.println("Veuillez entrer le numéro du lot pour le jeton (exemple : 4):");
      animal = Integer.parseInt(reader.readLine());
    } while(!listOfToken.add(animal) || (animal < 0 || animal > 4));
		draw.animalsForChoice.set(animal - 1, draw.randomAnimal());
	}
	
	/**
   * Handles the player's choice to remove nature tokens, allowing them to remove multiple tokens if desired.
   *
   * @throws NumberFormatException if the input is not a valid number
   * @throws IOException if there is an issue reading the input
   */
	public void handleTokenRemovalChoice() throws NumberFormatException, IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		Set<Integer> listOfToken = new HashSet<>();
		for (int i = 0; i < 4 && player.natureToken() != 0; i++) {
			chooseTokenToRemove(listOfToken);
			int choice1;
			do {
				System.out.println("Voulez-vous enlever un autre jeton faune ? (oui : 1, non : 2)");
				choice1 = Integer.parseInt(reader.readLine());
			} while(choice1 != 1 || choice1 != 2);
			var resultChoice = choice1;
			if (resultChoice == 2) {
				break;
			}
		}
	}
	
	/**
   * Asks the player if they want to spend a nature token for a specific action.
   *
   * @return the player's choice (1 for yes, 2 for no)
   * @throws IOException if there is an issue reading the input
   */
	public int choiceOf() throws IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Voulez-vous dépenser un jeton nature pour (entrer 1 pour oui et 2 pour non):\n-prendre n'importe quel tuiles et jetons pésent\n-enlever autant de jetons souhaiter");
		var choice = reader.readLine();
		while (Integer.parseInt(choice) != 1 && Integer.parseInt(choice) != 2 || choice.equals("")) {
			System.out.println("Ce choix n'existe pas, choississez une nouvelle fois");
			choice = reader.readLine();
		}
		return Integer.parseInt(choice);
	}
	
	/**
   * Asks the player if they want to take any available tile or remove nature tokens.
   *
   * @return the player's choice (1 for taking tiles and tokens, 2 for removing tokens)
   * @throws IOException if there is an issue reading the input
   */
	public int choiceOfNatureToken() throws IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Entrer 1 si vous voulez prendre n'importe quel tuiles et jetons pésent\nEntrer 2 si vous voulez enlever autant de jetons souhaiter");
		var choice = reader.readLine();
		while (Integer.parseInt(choice) != 1 && Integer.parseInt(choice) != 2 || choice.equals("")) {
			System.out.println("Ce choix n'existe pas, choississez une nouvelle fois");
			choice = reader.readLine();
		}
		return Integer.parseInt(choice);
	}
	
	/**
	 * Prompts the user to select a batch by entering a number between 1 and 4.
	 * The method will repeatedly ask the user for input until a valid number (1-4) is entered.
	 * 
	 * @return The selected batch number (between 1 and 4).
	 * @throws IOException If an I/O error occurs while reading the input from the user.
	 */
	public int choiceOfBatch() throws IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choississez un lot (Mettez le nombre):");
		var choice = reader.readLine();
		while (Integer.parseInt(choice) < 1 || Integer.parseInt(choice) > 4 || choice.equals("")) {
			System.out.println("Ce lot n'existe pas, choississez un autre lot (Mettez le nombre):");
			choice = reader.readLine();
		}
		return Integer.parseInt(choice);
	}
}
