package telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import telegrambot.entity.NotificationTask;
import telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            String messageText = update.message().text();
            logger.info("Processing update: {}", update);
            if (messageText.equals("/start")) {
                SendMessage message = new SendMessage(update.message().chat().id(), "Hello! Write your notification " +
                        "by format: dd.mm.yyyy hh:mm notification text");
                SendResponse response = telegramBot.execute(message);
            }
            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W\\w+]+)");
            Matcher matcher = pattern.matcher(messageText);
            if (matcher.matches()) {
                String dateTimeString = messageText.substring(0, 16);
                String notificationText = messageText.substring(17);
                System.out.println(dateTimeString);
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                System.out.println(notificationText);
                NotificationTask notificationTask = new NotificationTask();
                notificationTask.setChatId(update.message().chat().id());
                notificationTask.setDateTime(dateTime);
                notificationTask.setFirstName(update.message().chat().firstName());
                notificationTask.setNotification(notificationText);
                notificationTaskRepository.save(notificationTask);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
