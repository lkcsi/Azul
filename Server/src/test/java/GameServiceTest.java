import com.project.azul.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    GameService gameService;

    @BeforeEach void setUp(){
        gameService = new GameService();
    }

    @Test
    @DisplayName("Create game")
    void createGame(){
        assertThrows(Exception.class, () -> gameService.newGame(1));
        assertDoesNotThrow(() -> gameService.newGame(2));
        assertDoesNotThrow(() -> gameService.newGame(3));
        assertDoesNotThrow(() -> gameService.newGame(4));
        assertThrows(Exception.class, () -> gameService.newGame(5));
    }
    @Test
    @DisplayName("Add players")
    void addPlayers(){
        var game = gameService.newGame(4);
        assertDoesNotThrow(() -> gameService.addPlayer(game.getId(), "Roger"));
        assertDoesNotThrow(() -> gameService.addPlayer(game.getId(), "Peter"));
        assertDoesNotThrow(() -> gameService.addPlayer(game.getId(), "Jane"));
        assertDoesNotThrow(() -> gameService.addPlayer(game.getId(), "Maggie"));
        assertThrows(Exception.class, () -> gameService.addPlayer(game.getId(), "Louis"));
    }
}
