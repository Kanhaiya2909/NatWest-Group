package natwest.com;

class Player {
    private final String playerName;
    private Move playerMove;

    /**
     * @param name Name of player
     * @throws IllegalArgumentException If name of player is null or an empty string
     */
    Player(String name) throws IllegalArgumentException{
        if (name != null && !name.isEmpty())
            this.playerName = name;
        else {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
    }

    String getPlayerName(){
        return playerName;
    }

    Move getPlayerMove(){
        return playerMove;
    }

    void setPlayerMove(String move){
        if(getPlayerMove() == null)
            playerMove = Move.parseMove(move);
    }

    Boolean playerHasMadeMove(){
        return getPlayerMove() != null;
    }

}
