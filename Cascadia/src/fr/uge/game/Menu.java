package fr.uge.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

/**
 * The `Menu` class handles the display and interaction for the game's menus,
 * allowing the player to choose options like starting a new game, selecting
 * tile shapes, and game versions. It manages events and button clicks to
 * navigate through different menu screens.
 */
public class Menu {
	private final ApplicationContext context;
	private final int width;
	private final int height;

	/**
	 * Constructs a Menu object with the provided context and display dimensions.
	 * 
	 * @param context The application context
	 * @param width The width of the menu
	 * @param height The height of the menu
	 */
	public Menu(ApplicationContext context, int width, int height) {
		this.context = context;
		this.width = width;
		this.height = height;
	}

	/**
	 * Draws the first menu with a "Play" button and a "Quit" button.
	 * 
	 * @param graphics The graphics context to render the menu
	 */
	private void drawFirstMenu(Graphics2D graphics) {
		int buttonWidth = width / 5;
		int buttonHeight = height / 11;
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle2D.Float(width / 2 - buttonWidth / 2, height / 2 - buttonHeight / 2, buttonWidth, buttonHeight));
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, width / 27));
		graphics.drawString("Jouer", (width / 2 - buttonWidth / 2) + width / 21, (height / 2 - buttonHeight / 2) + height / 15);
		drawQuitButton(graphics, width, height);
		graphics.setColor(Color.GREEN);
		graphics.drawString("Cascadia", (width / 2 - buttonWidth / 2) + width / 48, (height / 6));
	}

	/**
	 * Draws the second menu with options to choose between square and hexagonal tiles.
	 * 
	 * @param graphics The graphics context to render the menu
	 */
	private void drawSecondMenu(Graphics2D graphics) {
		int buttonWidth = width / 5;
		int buttonHeight = height / 11;
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle2D.Float(width / 6, height / 2 - buttonHeight / 2, buttonWidth, buttonHeight));
		graphics.fill(new Rectangle2D.Float(width - (width / 6) - buttonWidth, height / 2 - buttonHeight / 2, buttonWidth, buttonHeight));
		graphics.setFont(new Font("Arial", Font.BOLD, width / 48));
		graphics.setColor(Color.WHITE);
		graphics.drawString("Tuiles Carrés", (width / 6) + width / 32, (height / 2 - buttonHeight / 2) + height / 16);
		graphics.setFont(new Font("Arial", Font.BOLD, width / 60));
		graphics.drawString("Tuiles Hexagonales", width - (width / 6) - buttonWidth + width / 192, (height / 2 - buttonHeight / 2) + height / 16);
		graphics.setFont(new Font("Arial", Font.BOLD, width / 27));
		drawQuitButton(graphics, width, height);
		graphics.setColor(Color.GREEN);
		graphics.drawString("Cascadia", (width / 2 - buttonWidth / 2) + width / 48, (height / 6));
	}

	/**
	 * Draws the third menu with options to choose between terminal and graphical
	 * versions of the game.
	 * 
	 * @param graphics The graphics context to render the menu
	 */
	private void drawThirdMenu(Graphics2D graphics) {
		int buttonWidth = width / 5;
		int buttonHeight = height / 11;
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle2D.Float(width / 6, height / 2 - buttonHeight / 2, buttonWidth, buttonHeight));
		graphics.setFont(new Font("Arial", Font.BOLD, width / 60));
		graphics.setColor(Color.WHITE);
		graphics.drawString("Version Terminal", (width / 6) + width / 64, (height / 2 - buttonHeight / 2) + height / 16);
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle2D.Float(width - (width / 6) - buttonWidth, height / 2 - buttonHeight / 2, buttonWidth, buttonHeight));
		graphics.setColor(Color.WHITE);
		graphics.drawString("Version Graphique", width - (width / 6) - buttonWidth + width / 192, (height / 2 - buttonHeight / 2) + height / 16);
		graphics.setFont(new Font("Arial", Font.BOLD, width / 27));
		drawQuitButton(graphics, width, height);
		graphics.setColor(Color.GREEN);
		graphics.drawString("Cascadia", (width / 2 - buttonWidth / 2) + width / 48, (height / 6));
	}

	/**
	 * Handles the pointer events for the first menu (Play and Quit).
	 * 
	 * @param pe The pointer event
	 * @return Returns 0 if the "Play" button is clicked, otherwise returns 1
	 */
	private int actionForFirstMenu(PointerEvent pe) {
		if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
			var location = pe.location();
			if (isPlayButtonClicked(location)) {
				return 0;
			} else if (isQuitButtonClicked(location, width, height)) {
				context.dispose();
				System.exit(0);
			}
		}
		return 1;
	}

	/**
	 * Handles rendering the first menu and processing events.
	 */
	private void handleFirstMenu() {
		context.renderFrame(this::drawFirstMenu);
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			switch (event) {
				case PointerEvent pe -> {
					if (actionForFirstMenu(pe) == 0) {
						return;
					}
				}
				default -> {  }
			}
		}
	}

	/**
	 * Handles the pointer events for the second menu (Tile types).
	 * 
	 * @param pe The pointer event
	 * @return Returns 0 if "Square Tiles" is selected, 1 if "Hexagonal Tiles" is selected, otherwise 2
	 */
	private int actionForSecondMenu(PointerEvent pe) {
		if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
			var location = pe.location();
			if (isTileSquareButtonClicked(location)) {
				return 0;
			} else if (isTileHexagonalButtonClicked(location)) {
				return 1;
			} else if (isQuitButtonClicked(location, width, height)) {
				context.dispose();
				System.exit(0);
			}
		}
		return 2;
	}

	/**
	 * Handles rendering the second menu and processing events.
	 * 
	 * @return Returns true if "Square Tiles" is selected, false if "Hexagonal Tiles" is selected
	 */
	private boolean handleSecondMenu() {
		context.renderFrame(this::drawSecondMenu);
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			switch (event) {
				case PointerEvent pe -> {
					if (actionForSecondMenu(pe) == 0) {
						return true;
					} else if (actionForSecondMenu(pe) == 1) {
						return false;
					}
				}
				default -> {  }
			}
		}
	}

	/**
	 * Handles the pointer events for the third menu (Version selection).
	 * 
	 * @param pe The pointer event
	 * @return Returns 0 if "Terminal Version" is selected, 1 if "Graphical Version" is selected, otherwise 2
	 */
	private int actionForThirdMenu(PointerEvent pe) {
		if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
			var location = pe.location();
			if (isOption1ButtonClicked(location)) {
				return 0;
			} else if (isOption2ButtonClicked(location)) {
				return 1;
			} else if (isQuitButtonClicked(location, width, height)) {
				context.dispose();
				System.exit(0);
			}
		}
		return 2;
	}

	/**
	 * Handles rendering the third menu and processing events.
	 * 
	 * @return Returns true if "Terminal Version" is selected, false if "Graphical Version" is selected
	 */
	private boolean handleThirdMenu() {
		context.renderFrame(this::drawThirdMenu);
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			switch (event) {
				case PointerEvent pe -> {
					if (actionForThirdMenu(pe) == 0) {
						return true;
					} else if (actionForThirdMenu(pe) == 1) {
						return false;
					}
				}
				default -> {  }
			}
		}
	}

	/**
	 * Handles the overall menu navigation process, calling each menu handler in sequence.
	 * 
	 * @return Returns 0 if "Terminal Version" is selected, 1 if "Graphical Version" is selected, 2 if "Hexagonal Tiles" is selected
	 */
	public int handleMenus() {
		handleFirstMenu();
		if (handleSecondMenu()) {
			if (handleThirdMenu()) {
				context.dispose();
				return 0;
			} else {
				return 1;
			}
		} else {
			return 2;
		}
	}

	/**
	 * Draws the "Quit" button on the screen.
	 * 
	 * @param graphics The graphics context
	 * @param width The width of the menu
	 * @param height The height of the menu
	 */
	public static void drawQuitButton(Graphics2D graphics, int width, int height) {
		Objects.requireNonNull(graphics, "graphics is null");
		graphics.setFont(new Font("Arial", Font.BOLD, width / 27));
		graphics.setColor(Color.RED);
		graphics.fill(new Rectangle2D.Float(0, (height / 5) * 2 + height / 2, width / 6, height / 11));
		graphics.setColor(Color.WHITE);
		graphics.drawString("Quitter", width / 64, height - height / 36);
	}

	/**
	 * Checks if the "Terminal Version" button is clicked.
	 * 
	 * @param location The location of the pointer event
	 * @return Returns true if the "Terminal Version" button is clicked
	 */
	private boolean isOption1ButtonClicked(PointerEvent.Location location) {
		return location.x() >= width / 6 && location.x() <= width / 6 + width / 5
				&& location.y() >= height / 2 - height / 22 && location.y() <= height / 2 + height / 22;
	}

	/**
	 * Checks if the "Graphical Version" button is clicked.
	 * 
	 * @param location The location of the pointer event
	 * @return Returns true if the "Graphical Version" button is clicked
	 */
	private boolean isOption2ButtonClicked(PointerEvent.Location location) {
		return location.x() >= width - (width / 6) - width / 5 && location.x() <= width - (width / 6)
				&& location.y() >= height / 2 - height / 22 && location.y() <= height / 2 + height / 22;
	}

	/**
	 * Checks if the "Play" button is clicked.
	 * 
	 * @param location The location of the pointer event
	 * @return Returns true if the "Play" button is clicked
	 */
	private boolean isPlayButtonClicked(PointerEvent.Location location) {
		return location.x() >= width / 2 - width / 10 && location.x() <= width / 2 + width / 10
				&& location.y() >= height / 2 - height / 22 && location.y() <= height / 2 + height / 22;
	}

	/**
	 * Checks if the "Quit" button is clicked.
	 * 
	 * @param location The location of the pointer event
	 * @param width The width of the menu
	 * @param height The height of the menu
	 * @return Returns true if the "Quit" button is clicked
	 */
	public static boolean isQuitButtonClicked(PointerEvent.Location location, int width, int height) {
		Objects.requireNonNull(location, "location is null");
		return location.x() >= 0 && location.x() <= width / 6 && location.y() >= (height / 5) * 2 + height / 2
				&& location.y() <= (height / 5) * 2 + height / 2 + height / 11;
	}

	/**
	 * Checks if the "Square Tiles" button is clicked.
	 * 
	 * @param location The location of the pointer event
	 * @return Returns true if the "Square Tiles" button is clicked
	 */
	private boolean isTileSquareButtonClicked(PointerEvent.Location location) {
		return location.x() >= width / 6 && location.x() <= width / 6 + width / 5
				&& location.y() >= height / 2 - height / 22 && location.y() <= height / 2 + height / 22;
	}

	/**
	 * Checks if the "Hexagonal Tiles" button is clicked.
	 * 
	 * @param location The location of the pointer event
	 * @return Returns true if the "Hexagonal Tiles" button is clicked
	 */
	private boolean isTileHexagonalButtonClicked(PointerEvent.Location location) {
		return location.x() >= width - (width / 6) - width / 5 && location.x() <= width - (width / 6)
				&& location.y() >= height / 2 - height / 22 && location.y() <= height / 2 + height / 22;
	}
}
