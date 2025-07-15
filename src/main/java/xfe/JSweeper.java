package xfe;

import xfe.grid.GridMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JSweeper {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DifficultyFrame::new);
    }
}

class JSweeperPanel extends JPanel {
    GridMap game;

    public JSweeperPanel(int difficulty) {
        int grid_width = 9;
        int grid_height = 9;
        int mine_count = 11;
        int cell_size = 32;

        switch (difficulty) {
            case 2 -> {
                grid_width = 16;
                grid_height = 16;
                mine_count = 40;
            }
            case 3 -> {
                grid_width = 30;
                grid_height = 16;
                mine_count = 99;
            }
        }

        this.game = new GridMap(grid_width, grid_height, cell_size);
        this.game.generate_map(grid_width, grid_height, mine_count);

        int hres = grid_height * cell_size;
        int wres = grid_width * cell_size;
        this.setPreferredSize(new Dimension(wres, hres));

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R -> game.reset();
                    case KeyEvent.VK_ESCAPE -> {
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(JSweeperPanel.this);
                        topFrame.dispose();
                        new DifficultyFrame();
                    }
                }

                JSweeperPanel.this.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        this.addMouseListener(new MouseListener() {
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

                    JSweeperPanel.this.repaint();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    int x = e.getX();
                    int y = e.getY();

                    Point p = new Point(x, y);
                    float x_translate = game.getCell_size();
                    float y_translate = game.getCell_size();

                    Point cell_space = new Point((int) Math.floor(p.x / x_translate), (int) Math.floor(p.y / y_translate));

                    game.flag_cell(cell_space.x, cell_space.y);

                    JSweeperPanel.this.repaint();
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

        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        this.game.draw(g2d, this);
    }
}

class JSweeperFrame extends JFrame {
    public JSweeperFrame(int difficulty) {
        this.setTitle("JSweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSweeperPanel panel = new JSweeperPanel(difficulty);
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}

