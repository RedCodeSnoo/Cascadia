package fr.uge.main;

import java.io.IOException;
import java.util.List;
import java.awt.Color;
import com.github.forax.zen.Application;

import fr.uge.game.CountPoint;
import fr.uge.game.Menu;
import fr.uge.game.GameRound;
import fr.uge.game.Structure;
import fr.uge.game.element.Draw;
import fr.uge.version.Choice;
import fr.uge.version.Display;
import fr.uge.version.graphic.DisplayForGraphic;
import fr.uge.version.graphic.actionForGraphic.GraphicChoice;
import fr.uge.version.terminal.DisplayForTerminal;
import fr.uge.version.terminal.actionForTerminal.TerminalChoice;

public class Main {

	
	public static void main(String[] args) {
    Application.run(Color.WHITE, context -> {
        try {
            var screenInfo = context.getScreenInfo();
            Menu menu = new Menu(context, screenInfo.width(), screenInfo.height());
            int choiceOfGame = menu.handleMenus();
            Choice choice = choiceOfGame == 0 ? new TerminalChoice() : new GraphicChoice();
            if(choiceOfGame == 1 || choiceOfGame == 2) choice.handleFourthMenu(context, screenInfo.width(), screenInfo.height());
            var numberOfPlayers = choice.numberOfPlayer();
            var game = choice.choiceOfGame();
            List<Integer> wildlifeCards = game == 3 ? choice.choiceOfCards(context, screenInfo.width(), screenInfo.height()) : List.of();
            Structure structure = new Structure(numberOfPlayers, game, choiceOfGame);
            structure.initialize(choiceOfGame, wildlifeCards);
            Draw draw = new Draw(structure);
            draw.initialize();
            Display display = choiceOfGame == 0 ? new DisplayForTerminal(structure.players(), draw, 0) : new DisplayForGraphic(structure.players(), draw, structure, 0, context, choiceOfGame, screenInfo.width(), screenInfo.height(), 0);
            GameRound gameRound = new GameRound(structure, draw, choiceOfGame);
            for (int i = 0; i < 15; i++) {
                gameRound.roundOfGame(context, screenInfo.width(), screenInfo.height(), i);
            }
            CountPoint.pointCount(structure.players(), structure.wildlifeCards(), numberOfPlayers, choiceOfGame);
            display.resultOfGame(gameRound.winner());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            context.dispose();
        }
    });
	}
}
