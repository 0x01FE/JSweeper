package xfe.grid;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GridMap {
    public int[][] map;
    public boolean[][] mines;

    int height;
    int width;

    int cell_size;
    public boolean is_holding_right_mouse;

    boolean has_won;
    boolean has_lost;

    boolean draw_grids;
    boolean debug;
    boolean in_flood_fill;

    public GridMap(int w, int h, int size) {
        this.width = w;
        this.height = h;
        this.cell_size = size;
        this.draw_grids = true;
        this.is_holding_right_mouse = false;
        this.has_lost = false;
        this.has_won = false;

        this.in_flood_fill = false;

        this.debug = false;

        this.map = new int[h][w];

        for (int[] row : this.map) {
            Arrays.fill(row, -1);
        }

        this.mines = new boolean[h][w];
    }

    public void generate_map(int w, int h, int mine_count) {

        Random random = new Random();

        this.map = new int[h][w];
        this.mines = new boolean[h][w];
        this.clear_map();

        double mine_chance = (double) mine_count / (h * w);
        int mines_placed = 0;
        while (mines_placed < mine_count) {
            int y = 0;
            for (boolean[] row : this.mines) {
                int x = 0;
                for (boolean mine : row) {
                    double place_mine = random.nextDouble();

                    if (place_mine <= mine_chance) {
                        this.mines[y][x] = true;
                        mines_placed++;

                        if (mines_placed >= mine_count)
                            break;
                    }

                    x++;
                }
                if (mines_placed >= mine_count)
                    break;

                y++;
            }
        }
    }

    public void reveal_cell(int x, int y) {
        if (this.has_lost || this.has_won)
            return;

        this.has_lost = this.mines[y][x];

        if (this.has_lost) {
            this.map[y][x] = -3;
            return;
        }

        this.map[y][x] = this.get_mine_neighbors(x, y);

        if (this.map[y][x] == 0 && !this.in_flood_fill)
            this.flood_fill(new Point(x, y));
    }

    private void flood_fill(Point start) {
        ArrayList<Point> filled = new ArrayList<>();
        LinkedList<Point> to_fill = new LinkedList<>();

        to_fill.add(start);

        this.in_flood_fill = true;

        while (!to_fill.isEmpty()) {
            Point c = to_fill.pop();

            if (filled.contains(c)) continue;

            this.reveal_cell(c.x, c.y);
            filled.add(c);

            if (this.map[c.y][c.x] != 0)
                continue;

            int[][] directions = { {0,1}, {1,0}, {0,-1}, {-1,0}, {-1,-1}, {1,-1}, {-1,1}, {1,1} };
            for (int[] dir : directions) {
                int nx = c.x + dir[0];
                int ny = c.y + dir[1];
                Point neighbor = new Point(nx, ny);

                if (is_valid(nx, ny) && !filled.contains(neighbor) && should_fill(nx, ny)) {
                    to_fill.add(neighbor);
                }
            }
        }

        this.in_flood_fill = false;
    }

    private boolean is_valid(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.height;
    }

    private boolean should_fill(int x, int y) {
        return this.map[y][x] == -1;
    }

    public void flag_cell(int x, int y) {
        if (this.has_lost || this.has_won)
            return;

        if (this.map[y][x] == -1)
            this.map[y][x] = -2;
        else if (this.map[y][x] == -2)
            this.map[y][x] = -1;

        this.check_if_won();
    }

    public void check_if_won() {
        int y = 0;
        for (boolean[] row : this.mines) {
            int x = 0;
            for (boolean m : row) {
                if (m && this.map[y][x] != -2)
                    return;
                x++;
            }
            y++;
        }
        this.has_won = true;
    }

    public void clear_map() {
        for (int[] row : this.map) {
            Arrays.fill(row, -1);
        }
    }

    public void setCell(int x, int y, int v) {
        this.map[y][x] = v;
    }

    public void reset() {
        this.has_won = false;
        this.has_lost = false;

        this.generate_map(this.width, this.height, 11);
    }

    public boolean getDraw_grids() {
        return this.draw_grids;
    }

    public void setDraw_grids(boolean v) {
        this.draw_grids = v;
    }

    public int getCell(int x, int y) {
        return this.map[y][x];
    }

    public void setCell_size(int cell_size) {
        this.cell_size = cell_size;
    }

    public int getCell_size() {
        return this.cell_size;
    }

    public int get_mine_neighbors(int x, int y) {
        boolean negative_y = y - 1 >= 0;
        boolean positive_y = y + 1 < this.height;

        int alive_neighbors = 0;

        if (negative_y)
            if (this.mines[y - 1][x])
                alive_neighbors++;

        if (positive_y)
            if (this.mines[y + 1][x])
                alive_neighbors++;

        if (x - 1 >= 0) {
            if (negative_y)
                if (this.mines[y - 1][x - 1])
                    alive_neighbors++;

            if (this.mines[y][x - 1])
                alive_neighbors++;

            if (positive_y)
                if (this.mines[y + 1][x - 1])
                    alive_neighbors++;
        }

        if (x + 1 < this.width) {
            if (negative_y)
                if (this.mines[y - 1][x + 1])
                    alive_neighbors++;

            if (this.mines[y][x + 1])
                alive_neighbors++;

            if (positive_y)
                if (this.mines[y + 1][x + 1])
                    alive_neighbors++;
        }

        return alive_neighbors;
    }

    public int get_width() {
        return this.width;
    }

    public int get_height() {
        return this.height;
    }

    public void draw(Graphics2D g, JPanel p) {

        // Draw cells

        Font font = new Font("Arial", Font.PLAIN, this.cell_size);
        g.setFont(font);

        if (debug) {
            int y = 0;
            for (boolean[] row : this.mines) {
                int x = 0;
                for (boolean cell : row) {

                    if (cell) {
                        g.setColor(Color.RED);
                        g.fillRect(
                                x * this.cell_size,
                                y * this.cell_size,
                                cell_size,
                                cell_size
                        );
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(
                                x * this.cell_size,
                                y * this.cell_size,
                                cell_size,
                                cell_size
                        );
                    }

                    x++;
                }
                y++;
            }
        } else {
            int y = 0;
            for (int[] row : this.map) {
                int x = 0;
                for (int cell : row) {

                    int real_x = x * this.cell_size;
                    int real_y = y * this.cell_size;
                    switch (cell) {
                        case 0:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            break;
                        case 1:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.BLUE);
                            g.drawString(String.valueOf(cell), real_x, real_y + this.cell_size);
                            break;
                        case 2:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.GREEN);
                            g.drawString(String.valueOf(cell), real_x, real_y + this.cell_size);
                            break;
                        case 3:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.RED);
                            g.drawString(String.valueOf(cell), real_x, real_y + this.cell_size);
                            break;
                        case 4:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.YELLOW);
                            g.drawString(String.valueOf(cell), real_x, real_y + this.cell_size);
                            break;
                        case 5:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.ORANGE);
                            g.drawString(String.valueOf(cell), real_x, real_y + this.cell_size);
                            break;
                        case 6:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.PINK);
                            g.drawString(String.valueOf(cell), real_x, real_y + this.cell_size);
                            break;
                        case 7:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.CYAN);
                            g.drawString(String.valueOf(cell), real_x, real_y + this.cell_size);
                            break;
                        case 8:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.MAGENTA);
                            g.drawString(String.valueOf(cell), real_x, real_y + this.cell_size);
                            break;
                        case -1:
                            this.draw_cell(g, real_x, real_y, Color.WHITE);
                            break;
                        // Flagged
                        case -2:
                            this.draw_cell(g, real_x, real_y, Color.GRAY);
                            g.setColor(Color.RED);
                            g.drawString("F", real_x, real_y + this.cell_size);
                            break;
                        // Mine
                        case -3:
                            this.draw_cell(g, real_x, real_y, Color.RED);
                            break;
                    }

                    x++;
                }
                y++;
            }
        }

        // Draw grids
        if (this.draw_grids) {
            g.setColor(Color.GRAY);

            int y_offset = 0;
            int x_offset = 0;
            for (int x = 0; x <= this.width; x++) {
                g.drawLine(x_offset, 0, x_offset, this.height * cell_size);
                x_offset += this.cell_size;
            }

            for (int y = 0; y <= this.height; y++) {
                g.drawLine(0, y_offset, this.width * cell_size, y_offset);
                y_offset += this.cell_size;
            }
        }

        // Print game over
        if (this.has_lost) {
            g.setColor(new Color(117, 0, 4));
            g.drawString("GAME OVER", (p.getWidth() / 2) - (3 * this.cell_size), p.getHeight() / 2);

        }

        // Print is won
        if (this.has_won) {
            g.setColor(new Color(255, 207, 74));
            g.drawString("YOU WIN!", (p.getWidth() / 2) - (2 * this.cell_size), p.getHeight() / 2);
        }
    }

    private void draw_cell(Graphics2D g, int x, int y, Color c) {
        g.setColor(c);
        g.fillRect(
                x, y, this.cell_size, this.cell_size
        );
    }
}
