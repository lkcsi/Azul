import com.project.azul.api.Code;
import com.project.azul.controllers.GameController;
import com.project.azul.models.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTestNegative {
    GameController controller;

    @BeforeEach
    void setUp(){
        controller = new GameController();
    }


    @Test
    @DisplayName("Start game")
    void testNewGame(){
        Code code = controller.newGame(1);
        Assertions.assertEquals(Code.INVALID_NUMBER_OF_PLAYERS, code);

        code = controller.newGame(5);
        Assertions.assertEquals(Code.INVALID_NUMBER_OF_PLAYERS, code);
    }

    @Test
    @DisplayName("Register players")
    void testRegistration(){
        controller.newGame(4);

        controller.addPlayer("Jose");
        controller.addPlayer("Kate");
        controller.addPlayer("Moore");
        controller.addPlayer("Jasmin");

        Code code = controller.addPlayer("Peter");
        Assertions.assertEquals(Code.GAME_IS_FULL, code);
    }
}
