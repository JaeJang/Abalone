package abalone;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

import boardFrame.GameFrame;


@SuppressWarnings("serial")
public class Main extends JFrame{
    
    public static final Toolkit TOOLKIT;
    public static final float HUNDRED = 100.0f;
    public static final float ZERO_EIGHTY = 0.80f;
    
    private static Board board;
    
    static {
        TOOLKIT = Toolkit.getDefaultToolkit();
    }

    public Main() {
        // TODO Auto-generated constructor stub
    }
    
    public static void main(String[] args) {
        final GameFrame frame;
        
        board = new Board();
        frame = new GameFrame(board);
        position(frame);
        frame.init();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public void paint(Graphics g) {
        int radius = 50;
        for(Cell cell : board.getCells()) {
            int x = cell.getX() * 100;
            int y = cell.getY() * 100;
            g.fillOval(x, y, radius, radius);
        }
    }
    
    private static void position(final GameFrame frame) {
        final Dimension size;

        size = calculateScreenArea(10f, 1.2f);
        frame.setSize(1750, 900);
        frame.setLocation(0, 0);;

        
        
        /*size = calculateScreenArea(ZERO_EIGHTY, 0.85f);
        frame.setSize(size);
        frame.setLocation(centreOnScreen(size));*/
        
        
    }
    
    public static Point centreOnScreen(final Dimension size) {
        final Dimension screenSize;

        if (size == null) {
            throw new IllegalArgumentException("Size cannot be null");
        }

        screenSize = TOOLKIT.getScreenSize();

        return (new Point((screenSize.width - size.width) 
                / 2, (screenSize.height - size.height) / 2));
    }
    
    public static Dimension calculateScreenArea(
            final float widthPercent, final float heightPercent) {
        final Dimension screenSize;
        final Dimension area;
        final int width;
        final int height;
        final int size;

        if ((widthPercent <= 0.0f) || (widthPercent > HUNDRED)) {
            throw new IllegalArgumentException("widthPercent cannot be " 
        + "<= 0 or > 100 - got: " + widthPercent);
        }

        if ((heightPercent <= 0.0f) || (heightPercent > HUNDRED)) {
            throw new IllegalArgumentException("heightPercent cannot be "
        + "<= 0 or > 100 - got: " + heightPercent);
        }

        screenSize = TOOLKIT.getScreenSize();
        width = (int) (screenSize.width * widthPercent);
        height = (int) (screenSize.height * heightPercent);
        size = Math.min(width, height);
        area = new Dimension(size, size);

        return (area);
    }
    
}
