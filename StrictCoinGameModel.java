package cs3500.hw04;

import java.util.ArrayList;

// INVARIANT: External board representation must only contain "-" and "O"
// INVARIANT: There must be at least one player playing
// INVARIANT: Number of coins must be non-negative
// INVARIANT: Board size must be non-negative
// INVARIANT: playerIndex must be non-negative

/**
 * StrictCoinGameModel implements CoinGameModel interface
 */

public final class StrictCoinGameModel implements CoinGameModel {
    private ArrayList<String> players = new ArrayList<>();
    private Boolean[] board;
    private int playerIndex;

    /**
     * Constructor takes in a Builder
     * @param builder
     */

    public StrictCoinGameModel(Builder builder) {
        this.players = builder.players;
        this.board = builder.board;
        this.playerIndex = builder.playerIndex;
    }



    /**
     * Builder class sets default values for StrictCoinGameModel
     */

    public static class Builder {
        private ArrayList<String> players;
        private Boolean[] board;
        private int playerIndex;


        /**
         * Checks if given ArrayList has duplicates and removes them
         * If given ArrayList is empty, initializes a default player "A"
         * @param players
         * @return Builder
         */
        public Builder players(ArrayList<String> players) {

            boolean duplicates = false;
            for (int x=0; x< players.size(); x++)
                for (int y=x+1; y<players.size(); y++)
                    if (y!=x && players.get(y) == players.get(x))
                        duplicates=true;
            if (duplicates) {
                throw new IllegalArgumentException("This list of" +
                        " players has two or more players that are identical");
            }
            else {
                this.players = players;
                }
            return this;
        }

        /**
         * Checks if given String board is valid
         * Converts to internal board representation of an array of Booleans
         * @param board
         * @return Builder
         */
        public Builder board(String board) {
            Boolean[] b = new Boolean[board.length()];

            for(int x = 0; x < board.length(); x++) {
                if(board.charAt(x) == 'O') {
                    b[x] = true;
                    this.board = b;
                }
                else if(board.charAt(x) == '-') {
                    b[x] = false;
                    this.board = b;
                }
                else {
                    throw new IllegalArgumentException("Illegal board");
                }
            }
            return this;
        }

        /**
         * @return StrictCoingGameModel
         */
        public StrictCoinGameModel build() {
            return new StrictCoinGameModel(this);
        }
    }


    /**
     * Not a valid constructor anymore
     * @param board
     * @param players
     * @throws UnsupportedOperationException
     */
    protected StrictCoinGameModel(String board, ArrayList<String> players) {
        // You don't need to implement this constructor.
        throw new UnsupportedOperationException("no need to implement this");
    }

    /**
     * Returns the size of the board
     * @return length of board
     */
    public int boardSize() {
        return this.board.length;
    }

    /**
     * Returns the number of coins on the board
     * @return number of coins
     */
    public int coinCount() {
        int coins = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == true) {
                coins = coins + 1;
            }
        }
        return coins;
    }

    /**
     * Given a coinIndex, gets the position of that coin on the board
     * @param coinIndex
     * @return position of coin
     */
    public int getCoinPosition(int coinIndex) {
        int coin = 0;
        int position = 0;
        // if there are no coins on the board or no such coin with given index
        if (this.coinCount() < 1 || coinIndex > this.coinCount()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < board.length; i++) {
            if (board[i] == true) {
                if (coin == coinIndex) {
                    return i;
                } else {
                    coin = coin + 1;
                }
            }
            position = i;
        }
        return position;
    }

    /**
     * Returns true if all of the coins are in their leftmost valid position
     * @return if the game is over
     */
    public boolean isGameOver() {
        for (int i = 0; i < this.coinCount(); i++) {
            if (this.board[i]) {
            } else {
                return false;
            }
        }
        return true;
    }


    /**
     * Moves a coin if it is a valid move
     * Cannot move if:
     * You are trying to 'jump' a coin
     * You are trying to move right
     * You are not moving
     * There is a coin at the new position
     *
     * Checks if playerIndex is valid, if not it resets it to zero
     *
     * @param coinIndex
     * @param newPosition
     * @throws IllegalMoveException
     */
    public void move(int coinIndex, int newPosition) {

        int oldPosition = this.getCoinPosition(coinIndex);
                /*
        check to make sure that coin doesn't pass the other coin, if there is one
        >= 0
        new Position < current position
        can't be in same place as coin

        if any are true, then throw IllegalMoveException
        if they all pass:
        1. move coin (change new index and old index)
         */

        if (coinIndex > this.coinCount() - 1) {
            throw new IllegalArgumentException();
        } else if (coinIndex < 0 || newPosition < 0 || board[newPosition] == true
                || newPosition > this.getCoinPosition(coinIndex) ||
                (newPosition == this.getCoinPosition(coinIndex)) ||
                (coinIndex > 0 && this.getCoinPosition(coinIndex - 1) > newPosition)) {
            throw new IllegalMoveException();
        } else {
            board[oldPosition] = false;
            board[newPosition] = true;
        }

        if(this.playerIndex < this.players.size()-1) {
            this.playerIndex += 1;
        }
        else {
            this.playerIndex = 0;
        }
    }

    /*
    The game allows players to take their turn in chronological order in the ArrayList
    the int PlayerIndex keeps track of who's turn it is currently
    You can add players right after the int playerIndex is
    incremented and before the current player plays, but
    will not affect the current player's turn
     */
    /**
     * Returns the name of the current player
     * @return the string name of the player whose turn it currently is
     */
    public String whosTurn() {
        return this.players.get(this.playerIndex);
    }

    /**
     * Gives the list of all the players in the game
     * @return the ArrayList players
     */
    public ArrayList<String> getPlayers() {
        return this.players;
    }

    /**
     * Adds a player of the given name at the given index
     * If the given index is before the current index we increment playerIndex
     *
     * @param playerName
     * @param index
     */
    public void addPlayer(String playerName, int index) {
        this.players.add(index, playerName);
        if (index <= this.playerIndex) {
            this.playerIndex += 1;
        }
    }

    /**
     * Returns the name of the winner IF the game is over
     * if the game isn't over throws an exception
     * @return the name of the winner
     * @throws IllegalStateException
     */
    public String winner() {
        if (this.isGameOver()) {
            return this.players.get(this.playerIndex);
        }
        else {
            throw new IllegalStateException();
        }
    }


    /**
     * Gives you the board as a string
     * @return the external representation of the board as a string
     */
    public String toString() {
        String external="";
        for(int x = 0; x < boardSize(); x++) {
            if(board[x] == true) {
                external += "O";
            }
            else {
                external += "-";
            }
        }
        return external;
    }
}


