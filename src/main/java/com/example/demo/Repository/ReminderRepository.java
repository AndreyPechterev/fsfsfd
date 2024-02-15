package com.example.demo.Repository;

import com.example.demo.Entity.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Page<Reminder> findReminderByTitleContainingIgnoreCaseAndUserId(String title,Long userId, Pageable pageable);

    Page<Reminder> findReminderByDescriptionContainingIgnoreCaseAndUserId(String description,Long userId, Pageable pageable);

    Page<Reminder> findReminderByRemindBetweenAndUserId(Timestamp startOfDay,Timestamp endOfDay,Long userId, Pageable pageable);

    Page<Reminder> findAllByUserId(Long userId, Pageable pageable);

    Page<Reminder> findReminderByRemindBeforeAndUserId(Timestamp day,Long id, Pageable pageable);

    Page<Reminder> findReminderByRemindAfterAndUserId(Timestamp day,Long id, Pageable pageable);
}
