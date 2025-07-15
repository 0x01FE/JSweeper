package xfe;

import javax.swing.*;
import java.awt.*;

public class DifficultyFrame extends JFrame {
    public DifficultyFrame() {
        this.setTitle("Difficulty Select");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DifficultyPanel panel = new DifficultyPanel();

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}

class DifficultyPanel extends JPanel {

    int selected_difficulty;

    public DifficultyPanel() {
        this.selected_difficulty = 1;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");

        easy.setAlignmentX(Component.CENTER_ALIGNMENT);
        medium.setAlignmentX(Component.CENTER_ALIGNMENT);
        hard.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension buttonSize = new Dimension(150, 40);

        easy.setMaximumSize(buttonSize);
        easy.setPreferredSize(buttonSize);
        medium.setMaximumSize(buttonSize);
        medium.setPreferredSize(buttonSize);
        hard.setMaximumSize(buttonSize);
        hard.setPreferredSize(buttonSize);

        Font font = new Font("Arial", Font.PLAIN, 20);

        easy.setFont(font);
        medium.setFont(font);
        hard.setFont(font);

        easy.setForeground(new Color(0, 148, 25));
        medium.setForeground(new Color (189, 116, 0));
        hard.setForeground(new Color(135, 0, 0));

        easy.addActionListener(e -> {
            this.selected_difficulty = 1;
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new JSweeperFrame(this.selected_difficulty);
        });

        medium.addActionListener(e -> {
            this.selected_difficulty = 2;
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new JSweeperFrame(this.selected_difficulty);
        });

        hard.addActionListener(e -> {
            this.selected_difficulty = 3;
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new JSweeperFrame(this.selected_difficulty);
        });

        this.add(easy);
        this.add(Box.createVerticalStrut(10));
        this.add(medium);
        this.add(Box.createVerticalStrut(10));
        this.add(hard);

        this.setPreferredSize(new Dimension(300, 150));
    }
}
