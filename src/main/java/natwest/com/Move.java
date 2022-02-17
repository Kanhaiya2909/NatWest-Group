package natwest.com;

public enum Move {

    ROCK {
        @Override
        public boolean beats(Move other) {
            return other == SCISSOR;
        }
    },
    PAPER {
        @Override
        public boolean beats(Move other) {
            return other == ROCK;
        }
    },
    SCISSOR {
        @Override
        public boolean beats(Move other) {
            return other == PAPER;
        }
    };

    public static Move parseMove(String value) {
        String move = value.trim().toLowerCase();
        switch (move) {
            case "rock":
                return ROCK;
            case "paper":
                return PAPER;
            case "scissor":
                return SCISSOR;
            default:
                return null;
        }
    }

    public abstract boolean beats(Move other);

    public static Boolean validMove(String move) {
        return move != null && Move.parseMove(move) != null;
    }
}
