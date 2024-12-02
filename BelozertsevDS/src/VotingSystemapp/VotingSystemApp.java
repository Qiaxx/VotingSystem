package VotingSystemapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Программа для демонстрации системы электронного голосования с разграничением по ролям.
 * 
 * @author BelozertsevDS
 */
public class VotingSystemApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

/**
 * Окно авторизации
 */
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    // Список пользователей с паролями (в реальном проекте хранилось бы в БД)
    private HashMap<String, String> users;

    public LoginFrame() {
        setTitle("Авторизация");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));
        
        // Создаем пользователей
        users = new HashMap<>();
        users.put("admin", "admin123"); // Администратор
        users.put("user", "user123");  // Обычный пользователь

        // Поля для ввода
        add(new JLabel("Имя пользователя:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(new LoginButtonListener());
        add(loginButton);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (users.containsKey(username) && users.get(username).equals(password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                        "Добро пожаловать, " + username + "!", 
                        "Успешный вход", JOptionPane.INFORMATION_MESSAGE);
                
                // Открываем соответствующее окно
                if (username.equals("admin")) {
                    new AdminFrame().setVisible(true);
                } else {
                    new UserFrame().setVisible(true);
                }
                dispose(); // Закрываем окно авторизации
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                        "Неверное имя пользователя или пароль!", 
                        "Ошибка входа", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

/**
 * Окно для администратора
 */
class AdminFrame extends JFrame {
    private JTextArea logArea;

    public AdminFrame() {
        setTitle("Администратор");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Добавляем фон
        getContentPane().setBackground(new Color(30, 30, 30)); // Тёмно-серый фон
        
        // Панель заголовка
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(50, 50, 50)); // Светло-серый фон
        JLabel headerLabel = new JLabel("Список голосовавших");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Панель с логами голосования
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(60, 60, 60));
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        logArea.append("Список голосовавших:\n\n");

        // Добавляем примеры (в будущем - данные из БД или файла)
        logArea.append("1. Иван Иванов\n");
        logArea.append("2. Анна Смирнова\n");
        logArea.append("3. Сергей Петров\n");

        add(new JScrollPane(logArea), BorderLayout.CENTER);
    }
}

/**
 * Окно для пользователя
 */
class UserFrame extends JFrame {
    private ArrayList<Snowflake> snowflakes = new ArrayList<>();
    private JLabel statusLabel;
    private JButton voteButton;
    private JTextField nameField;

    public UserFrame() {
        setTitle("Система голосования");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Добавляем фон
        getContentPane().setBackground(new Color(30, 30, 30)); // Тёмно-серый фон
        
        // Панель логотипа
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(50, 50, 50)); // Светло-серый фон
        try {
            ImageIcon originalLogo = new ImageIcon(getClass().getResource("/resources/voting-system.png"));
            Image scaledLogo = originalLogo.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoPanel.add(logoLabel);
        } catch (NullPointerException e) {
            System.out.println("Логотип не найден. Убедитесь, что путь к логотипу указан правильно.");
            JLabel placeholderLogo = new JLabel("Логотип");
            placeholderLogo.setForeground(Color.WHITE);
            logoPanel.add(placeholderLogo);
        }
        add(logoPanel, BorderLayout.NORTH);

        // Панель голосования
        JPanel votePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        votePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
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
        Timer timer = new Timer(30, e -> {
            for (Snowflake snowflake : snowflakes) {
                snowflake.updatePosition();
            }
            repaint();
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
        for (Snowflake snowflake : snowflakes) {
            snowflake.draw(g);
        }
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        return label;
    }

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

    private class VoteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(UserFrame.this, 
                        "Пожалуйста, введите ваше имя перед голосованием.", 
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(UserFrame.this, 
                        "Спасибо за ваш голос, " + name + "!", 
                        "Голос принят", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("Спасибо за ваш голос, " + name + "!");
                
                // Третье уведомление (например, поздравление)
                if (new Random().nextBoolean()) {
                    JOptionPane.showMessageDialog(UserFrame.this, 
                            "Поздравляем! Вы только что внесли вклад в демократию.", 
                            "Поздравление", JOptionPane.INFORMATION_MESSAGE);
                }
                statusLabel.setText("Спасибо за ваш голос, " + name + "!");
            }
        }
    }
}
