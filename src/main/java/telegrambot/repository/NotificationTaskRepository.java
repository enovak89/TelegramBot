package telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Integer> {

    Collection<NotificationTask> findAllByDateTime(LocalDateTime dateTime);
}
