package abalone.gameEnum;

public enum MarbleType {
    WHITE("WHITE"),
    BLACK("BLACK");
    
    private final String text;
    
    MarbleType(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }
}
