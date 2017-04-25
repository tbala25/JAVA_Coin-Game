package cs3500.hw04;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Used to test the methods in CoinGameModel
 */
public class StrictCoinGameModelTest {

    ArrayList<String> a1 = new ArrayList<String>();
    ArrayList<String> a2 = new ArrayList<String>() {{
        add("A");
        add("B");
        add("C");
        add("D");
    }};
    ArrayList<String> a3 = new ArrayList<String>() {{
        add("A");
        add("B");
    }};

    StrictCoinGameModel ex1 = new StrictCoinGameModel.Builder()
            .board("--O-O-OO-").players(a3).build();
    StrictCoinGameModel ex2 = new StrictCoinGameModel.Builder()
            .board("--O-O-OO---").players(a2).build();
    StrictCoinGameModel ex3 = new StrictCoinGameModel.Builder()
            .board("-").players(a2).build();
    StrictCoinGameModel ex4 = new StrictCoinGameModel.Builder()
            .board("OOOO").players(a2).build();
    StrictCoinGameModel ex5 = new StrictCoinGameModel.Builder()
            .board("OO-O").players(a2).build();
    StrictCoinGameModel ex6 = new StrictCoinGameModel.Builder()
            .board("OOO----").players(a2).build();
    StrictCoinGameModel ex7 = new StrictCoinGameModel.Builder()
            .board("O--OO--O-").players(a2).build();


    @Test
    public void testAddPlayer() {
        assertEquals(ex1.getPlayers().size(), 2);
        ex1.addPlayer("C", 1);
        assertEquals(ex1.getPlayers().size(), 3);

        assertEquals(ex2.getPlayers().size(), 4);
        ex2.addPlayer("E", 1);
        assertEquals(ex2.getPlayers().size(), 5);
    }

    @Test
    public void testBoardSize() {
        assertEquals(ex1.boardSize(), 9);
        assertEquals(ex2.boardSize(), 11);
        assertEquals(ex3.boardSize(), 1);
    }

    @Test
    public void testCoinCount() {
        assertEquals(ex1.coinCount(), 4);
        assertEquals(ex2.coinCount(), 4);
        assertEquals(ex3.coinCount(), 0);
    }

    @Test
    public void testIsGameOver() {
        assertEquals(ex4.isGameOver(), true);
        assertEquals(ex5.isGameOver(), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoinPositionBad() {
        // no coin on the board
        ex1.getCoinPosition(0);
        //no such coin
        ex2.getCoinPosition(10);
        // no negative coins
        ex3.getCoinPosition(-1);
    }

    @Test
    public void testCoinPosition() {
        assertEquals(ex6.getCoinPosition(0), 0);
        assertEquals(ex6.getCoinPosition(1), 1);
        assertEquals(ex6.getCoinPosition(2), 2);
        assertEquals(ex7.getCoinPosition(0), 0);
        assertEquals(ex7.getCoinPosition(1), 3);
        assertEquals(ex7.getCoinPosition(2), 4);
    }

    @Test(expected = CoinGameModel.IllegalMoveException.class)
    public void testBadMove() {
        ex7.move(2, -1);
        ex7.move(3, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadArg() {
        // no such coin
        ex6.move(4, 0);
        // neg argument
        ex3.move(-1, 3);
        // no such board space
        ex2.move(1, 15);
        // no such coin
        ex7.move(5, 2);
    }


    @Test(expected = CoinGameModel.IllegalMoveException.class)
    public void testStrictBadMove() {
        // can't game over case
        ex6.move(1, 0);
        // can't pass a coin
        ex7.move(2, 2);
        // coin already there
        ex7.move(1, 5);
    }

    @Test
    public void testMove() {
        assertEquals(ex1.toString(), "--O-O-OO-");
        ex1.move(0, 0);
        assertEquals(ex1.toString(), "O---O-OO-");
        ex1.move(2, 5);
        assertEquals(ex1.toString(), "O---OO-O-");
        ex1.move(3, 6);
        assertEquals(ex1.toString(), "O---OOO--");
    }

    @Test
    public void testGetPlayers() {
        assertEquals(ex2.getPlayers(), a2);
        assertEquals(ex1.getPlayers(), a3);
    }

    @Test
    public void testWhosTurn() {
        /*
        start a game, move the coins a few times and see if the index is incremented
         */
        assertEquals(ex1.whosTurn(), "A");
        ex1.move(0, 0);
        assertEquals(ex1.whosTurn(), "B");
        ex1.move(1, 1);
        assertEquals(ex1.whosTurn(), "A");
        assertEquals(ex2.whosTurn(), "A");
        ex2.move(0, 0);
        assertEquals(ex2.whosTurn(), "B");
        ex2.move(1, 1);
        assertEquals(ex2.whosTurn(), "C");
        ex2.move(2, 2);
        assertEquals(ex2.whosTurn(), "D");
    }

    @Test
    public void testWinner() {
        assertEquals(ex6.winner(), "A");
        ex5.move(2, 2);
        assertEquals(ex5.winner(), "B");
        assertEquals(ex3.winner(), "A");
    }

    @Test(expected = IllegalStateException.class)
    public void testBadWinner() {
        // an example where there is no winner because the game isn't over yet
        ex1.winner();
        ex2.winner();
    }

}
