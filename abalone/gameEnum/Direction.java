package abalone.gameEnum;

public enum Direction {

    LEFT("LEFT"),

    RIGHT("RIGHT"),

    TOP_LEFT("TOP LEFT"),

    TOP_RIGHT("TOP RIGHT"),

    BOT_LEFT("BOTTOM LEFT"),

    BOT_RIGHT("BOTTOM RIGHT");

    private final String text;
    
    Direction(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }
}