
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Money Heist");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);


            JPanel startScreen = new JPanel() {

                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    Graphics2D g2d = (Graphics2D) g;
                    Color color1 = new Color(10, 10, 30); // Dark blue-black
                    Color color2 = new Color(40, 0, 50); // Purple shadow
                    g2d.setPaint(new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2));
                    g2d.fillRect(0, 0, getWidth(), getHeight());


                    g2d.setColor(new Color(80, 0, 0, 30)); // Transparent red bricks
                    for (int i = 0; i < getWidth(); i += 40) {
                        for (int j = 0; j < getHeight(); j += 20) {
                            g2d.fillRect(i, j, 35, 15);
                        }
                    }
                }
            };
            startScreen.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(10, 0, 30, 0);


            JLabel titleLabel = new JLabel("Money Heist");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
            titleLabel.setForeground(new Color(255, 20, 150)); // Neon pink
            titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 100, 255, 100), 4),
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
            ));
            gbc.anchor = GridBagConstraints.CENTER;
            startScreen.add(titleLabel, gbc);


            JLabel subtitleLabel = new JLabel("EVADE • STEAL • ESCAPE");
            subtitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            subtitleLabel.setForeground(new Color(255, 255, 100)); // Yellow
            startScreen.add(subtitleLabel, gbc);


            JLabel coinIcon = new JLabel(new ImageIcon("coin.png")); // optional image
            startScreen.add(coinIcon, gbc);


            JButton startButton = new JButton("BEGIN HEIST") {

                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(60, 60, 60));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(120, 120, 120, 100));
                    for (int i = 0; i < getWidth(); i += 3) {
                        g2.drawLine(i, 0, i, getHeight());
                    }
                    super.paintComponent(g);
                }
            };
            startButton.setFont(new Font("Arial", Font.BOLD, 24));
            startButton.setForeground(Color.WHITE);
            startButton.setBorder(BorderFactory.createLineBorder(new Color(255, 50, 50), 2));
            startButton.setContentAreaFilled(false);
            startButton.setOpaque(false);
            startButton.setFocusable(false);
            startButton.setPreferredSize(new Dimension(250, 50));
            startButton.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    startButton.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 3));
                }

                public void mouseExited(MouseEvent e) {
                    startButton.setBorder(BorderFactory.createLineBorder(new Color(255, 50, 50), 2));
                }
            });


            Robber robberGame = new Robber();
            robberGame.setFocusable(true);


            startButton.addActionListener(e -> {
                cardLayout.show(mainPanel, "game");
                robberGame.requestFocusInWindow();
            });

            gbc.insets = new Insets(40, 0, 0, 0);
            startScreen.add(startButton, gbc);


            mainPanel.add(startScreen, "start");
            mainPanel.add(robberGame, "game");


            frame.add(mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            cardLayout.show(mainPanel, "start");
        });
    }
}

