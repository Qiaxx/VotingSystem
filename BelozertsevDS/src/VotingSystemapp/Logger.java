package VotingSystemapp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для логирования событий приложения.
 * Записывает сообщения с временной меткой в текстовый файл.
 */
public class Logger {
    private static final String LOG_FILE = "voting_system_log.txt";
    private static Iterable<String> logEvents;

    /**
     * Метод для записи события в лог.
     *
     * @param message Текст сообщения.
     */
    public static void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка записи в лог-файл: " + e.getMessage());
        }
    }
    
    public static void saveLog() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
            for (String event : logEvents) {
                writer.println(event);
            }
            Logger.log("Сохранение лога завершено успешно.");
        } catch (IOException e) {
            Logger.log("Ошибка сохранения лога: " + e.getMessage());
        }
    }
}
