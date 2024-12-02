package VotingSystemapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Программа для демонстрации системы электронного голосования с анимацией
 * снежинок, логотипом, улучшенным внешним видом формы и уведомлениями.
 * 
 * @author BelozertsevDS
 */
public class VotingSystemApp extends JFrame {
    private ArrayList<Snowflake> snowflakes = new ArrayList<>();
    private JLabel logoLabel, statusLabel;
    private JButton voteButton;
    private JTextField nameField;

    public VotingSystemApp() {
        setTitle("Система электронного голосования");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Добавляем фон
        getContentPane().setBackground(new Color(30, 30, 30)); // тёмно-серый фон
        
        // Панель логотипа с фоном
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(50, 50, 50)); // более светлый серый фон
        try {
            ImageIcon originalLogo = new ImageIcon(getClass().getResource("/resources/voting-system.png"));
            Image scaledLogoImage = originalLogo.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledLogoImage));
        } catch (NullPointerException e) {
            System.out.println("Логотип не найден. Убедитесь, что путь к логотипу указан правильно.");
            logoLabel = new JLabel("Логотип");
            logoLabel.setForeground(Color.WHITE);
        }
        logoPanel.add(logoLabel);
        add(logoPanel, BorderLayout.NORTH);

        // Панель голосования с фоном и границами
        JPanel votePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        votePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                "Форма для голосования", 0, 0, null, Color.WHITE));
        votePanel.setBackground(new Color(60, 60, 60));

        votePanel.add(createLabel("Введите ваше имя:", Color.WHITE));
        nameField = new JTextField();
        votePanel.add(nameField);

        votePanel.add(createLabel("Ваш голос:", Color.WHITE));
        voteButton = new JButton("Проголосовать");
        voteButton.setBackground(new Color(100, 149, 237)); // Корнфлавор (синий) цвет
        voteButton.setForeground(Color.WHITE);
        voteButton.addActionListener(new VoteButtonListener());
        votePanel.add(voteButton);

        add(votePanel, BorderLayout.CENTER);

        // Панель статуса
        statusLabel = new JLabel("Добро пожаловать в систему голосования!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel, BorderLayout.SOUTH);

        // Анимация снежинок
        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Snowflake snowflake : snowflakes) {
                    snowflake.updatePosition();
                }
                repaint();
            }
        });
        timer.start();

        // Создание снежинок
        for (int i = 0; i < 50; i++) {
            snowflakes.add(new Snowflake());
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Рисуем снежинки поверх интерфейса
        for (Snowflake snowflake : snowflakes) {
            snowflake.draw(g);
        }
    }

    // Класс для создания цветных меток
    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        return label;
    }

    // Класс снежинки
    private class Snowflake {
        private int x, y, speed;
        private final Random random = new Random();

        public Snowflake() {
            x = random.nextInt(800);
            y = random.nextInt(600);
            speed = 1 + random.nextInt(3);
        }

        public void updatePosition() {
            y += speed;
            if (y > 600) y = 0;
        }

        public void draw(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillOval(x, y, 5, 5);
        }
    }

    // Обработчик нажатия кнопки голосования
    private class VoteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                // Уведомление о пустом имени
                JOptionPane.showMessageDialog(VotingSystemApp.this, 
                        "Пожалуйста, введите ваше имя перед голосованием.", 
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            } else {
                // Уведомление об успешном голосовании
                JOptionPane.showMessageDialog(VotingSystemApp.this, 
                        "Спасибо за ваш голос, " + name + "!", 
                        "Голос принят", JOptionPane.INFORMATION_MESSAGE);

                // Третье уведомление (например, поздравление)
                if (new Random().nextBoolean()) {
                    JOptionPane.showMessageDialog(VotingSystemApp.this, 
                            "Поздравляем! Вы только что внесли вклад в демократию.", 
                            "Поздравление", JOptionPane.INFORMATION_MESSAGE);
                }
                statusLabel.setText("Спасибо за ваш голос, " + name + "!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VotingSystemApp app = new VotingSystemApp();
            app.setVisible(true);
        });
    }
}
