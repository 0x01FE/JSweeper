package xfe;

import xfe.grid.GridMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    static final int CELL_SIZE = 32;

    static final int GRID_HEIGHT = 9;
    static final int GRID_WIDTH = 9;

    static final int HEIGHT = GRID_HEIGHT * CELL_SIZE;
    static final int WIDTH = GRID_WIDTH * CELL_SIZE;

    public static void main(String[] args) {
        GridMap game = new GridMap(GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);
        game.generate_map(GRID_WIDTH, GRID_HEIGHT, 10);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                game.draw(g2d, this);
            }
        };

        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R -> game.reset();
                }

                panel.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        panel.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = e.getX();
                    int y = e.getY();

                    Point p = new Point(x, y);
                    float x_translate = game.getCell_size();
                    float y_translate = game.getCell_size();

                    Point cell_space = new Point((int) Math.floor(p.x / x_translate), (int) Math.floor(p.y / y_translate));

                    game.reveal_cell(cell_space.x, cell_space.y);

                    panel.repaint();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    int x = e.getX();
                    int y = e.getY();

                    Point p = new Point(x, y);
                    float x_translate = game.getCell_size();
                    float y_translate = game.getCell_size();

                    Point cell_space = new Point((int) Math.floor(p.x / x_translate), (int) Math.floor(p.y / y_translate));

                    game.flag_cell(cell_space.x, cell_space.y);

                    panel.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });


        panel.setFocusable(true);
        contentPane.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}
