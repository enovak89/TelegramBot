package telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import telegrambot.entity.NotificationTask;
import telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
public class NotificationTaskService {

    private Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);

    private final TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(TelegramBot telegramBot, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void NotificationSend() {
        logger.info("NotificationSend method was invoked");
        Collection<NotificationTask> notificationsList = notificationTaskRepository.findAllByDateTime(
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        System.out.println(notificationsList);
        notificationsList
                .forEach(notificationTask -> {
                    SendMessage message = new SendMessage(notificationTask.getChatId(),
                            notificationTask.getFirstName() + ", не забудь " + notificationTask.getNotification());
                    telegramBot.execute(message);
                });
    }
}
