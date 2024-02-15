package com.example.demo.Service;

import com.example.demo.Controller.NotFoundException;
import com.example.demo.Entity.Reminder;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ReminderRepository;
import com.example.demo.Repository.UserRepository;
import com.mchange.v2.lang.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReminderService {

    private final UserService userService;
    private final ReminderRepository reminderRepository;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 3;


    @Transactional
    public void createReminder(Reminder reminder, String name) {
        User user = userService.findByUsername(name).orElseThrow(NotFoundException::new);
        reminder.setUser(user);
        reminderRepository.save(reminder);
    }

    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }

    public Page<Reminder> searchReminder(String title, String description, LocalDate day, Integer pageNumber, Integer pageSize,String sortBy,String orderBy, String name) {
        PageRequest pageRequest = buildPageRequestSorted(pageNumber, pageSize,sortBy,orderBy);
        Long id = userService.findByUsername(name).orElseThrow().getId();

        Page<Reminder> reminders;
        if (StringUtils.nonEmptyString(title)) {
            reminders = reminderRepository.findReminderByTitleContainingIgnoreCaseAndUserId(title, id, pageRequest);
        } else if (StringUtils.nonEmptyString(description)) {
            reminders = reminderRepository.findReminderByDescriptionContainingIgnoreCaseAndUserId(description, id, pageRequest);
        } else if (day != null) {
            LocalDateTime startOfDay = day.atStartOfDay();
            LocalDateTime endOfDay = day.atTime(23, 59, 59);
            Timestamp startOfDayTimestamp = Timestamp.valueOf(startOfDay);
            Timestamp endOfDayTimestamp = Timestamp.valueOf(endOfDay);
            reminders = reminderRepository.findReminderByRemindBetweenAndUserId(startOfDayTimestamp, endOfDayTimestamp, id, pageRequest);
        } else {
            reminders = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        }
        return reminders;
    }

    public Page<Reminder> sortReminders(Integer pageNumber, Integer pageSize, String sortBy, String orderBy, String name) {
        PageRequest pageRequest = buildPageRequestSorted(pageNumber, pageSize, sortBy, orderBy);
        Long id = userService.findByUsername(name).orElseThrow().getId();
        return reminderRepository.findAllByUserId(id, pageRequest);
    }

    public Page<Reminder> filterReminder(LocalDate before,LocalDate after, Integer pageNumber,Integer pageSize, String sortBy, String orderBy, String name){
        PageRequest pageRequest = buildPageRequestSorted(pageNumber,pageSize,sortBy,orderBy);
        Long id = userService.findByUsername(name).orElseThrow().getId();
        Page<Reminder> reminders;
        if (before != null){
            LocalDateTime startOfDay = before.atStartOfDay();
            Timestamp startOfDayT = Timestamp.valueOf(startOfDay);
            reminders = reminderRepository.findReminderByRemindBeforeAndUserId(startOfDayT, id, pageRequest);
        } else if (after != null) {
            LocalDateTime endOfDay = after.atTime(23,59,59);
            Timestamp startOfDayT = Timestamp.valueOf(endOfDay);
            reminders = reminderRepository.findReminderByRemindAfterAndUserId(startOfDayT, id, pageRequest);
        } else {
            reminders = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        }
        return reminders;
    }

    public PageRequest buildPageRequestSorted(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        queryPageSize = Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE);
        Sort sort = buildSort(sortBy, orderBy);
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    public Sort buildSort(String sortBy, String orderBy) {
        if (!isValidOrderBy(orderBy) && !isValidSortBy(sortBy)) {
            return Sort.unsorted();
        } else if (isValidSortBy(sortBy) && isValidOrderBy(orderBy)) {
            Sort.Direction direction = orderBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            return Sort.by(direction, sortBy.toLowerCase());
        } else if (isValidSortBy(sortBy)) {
            return Sort.by(sortBy.toLowerCase());
        } else {
            Sort.Direction direction = orderBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            return Sort.by(direction);
        }
    }

    private boolean isValidSortBy(String sortBy) {
        if (sortBy == null) {
            return false;
        }
        return sortBy.equalsIgnoreCase("remind") || sortBy.equalsIgnoreCase("title") || sortBy.equalsIgnoreCase("description");
    }

    private boolean isValidOrderBy(String orderBy) {
        if (orderBy == null) {
            return false;
        }
        return orderBy.equalsIgnoreCase("asc") || orderBy.equalsIgnoreCase("desc");
    }

}
