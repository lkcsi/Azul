import com.project.azul.api.Code;
import com.project.azul.controllers.GameController;
import com.project.azul.models.Game;
import com.project.azul.models.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

public class GameTest {
    GameController controller;

    @BeforeEach
    void setUp(){
        controller = new GameController();
    }
    @Test
    @DisplayName("Start game")
    void testNewGame(){
        var code = controller.newGame(2);
        Assertions.assertEquals(Code.SUCCESS, code);

        code = controller.newGame(3);
        Assertions.assertEquals(Code.SUCCESS, code);

        code = controller.newGame(4);
        Assertions.assertEquals(Code.SUCCESS, code);
    }

    @Test
    @DisplayName("Register players")
    void testRegistration(){
        controller.newGame(4);

        var status = controller.getGameDto().getState();
        Assertions.assertEquals(State.WAITING_FOR_PLAYERS ,status);

        Code code = controller.addPlayer("Jose");
        Assertions.assertEquals(Code.SUCCESS, code);
        status = controller.getGameDto().getState();
        Assertions.assertEquals(State.WAITING_FOR_PLAYERS ,status);

        code = controller.addPlayer("Casper");
        Assertions.assertEquals(Code.SUCCESS, code);
        status = controller.getGameDto().getState();
        Assertions.assertEquals(State.WAITING_FOR_PLAYERS ,status);

        code = controller.addPlayer("Julia");
        Assertions.assertEquals(Code.SUCCESS, code);
        status = controller.getGameDto().getState();
        Assertions.assertEquals(State.WAITING_FOR_PLAYERS ,status);

        code = controller.addPlayer("Kate");
        Assertions.assertEquals(Code.SUCCESS, code);

        status = controller.getGameDto().getState();
        Assertions.assertEquals(State.READY ,status);
    }

    @Test
    @DisplayName("Check number of factories")
    void numberOfFactories(){
        controller.newGame(2);
        Assertions.assertEquals(5,controller.getFactories().size());

        controller.newGame(3);
        Assertions.assertEquals(7,controller.getFactories().size());

        controller.newGame(4);
        Assertions.assertEquals(9,controller.getFactories().size());
    }

    @Test
    @DisplayName("Check picking")
    void checkPicking(){
        var game = new Game(4);
        game.registerPlayer("Joe"); game.registerPlayer("Bill");
        game.registerPlayer("Evan"); game.registerPlayer("Carl");

        var randomFactory = game.getFactories().get(0);
        var randomTile = randomFactory.getTiles().get(0);
        var sameTiles = randomFactory.getTiles().stream()
                .filter(t->t.getColor() == randomTile.getColor())
                .collect(Collectors.toList());

        game.pickFromFactory("Joe", randomFactory.getId(), 0, randomTile.getColor().toString(), false);
        Assertions.assertEquals(0, randomFactory.size());
    }


}
