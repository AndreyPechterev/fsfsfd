package com.example.demo.Controller;

import com.example.demo.Entity.Reminder;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ReminderRepository;
import com.example.demo.Service.ReminderService;
import com.example.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/domain/api/v1/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final UserService userService;

    @PostMapping("/create")
    public void create(@RequestBody Reminder reminder, Principal principal) {
        reminderService.createReminder(reminder, principal.getName());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        reminderService.deleteReminder(id);
    }

    @GetMapping("/search")
    public Page<Reminder> searchReminder(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) LocalDate day,
                                         @RequestParam(required = false) Integer pageNumber,
                                         @RequestParam(required = false) Integer pageSize,
                                         @RequestParam(required = false) String sortBy,
                                         @RequestParam(required = false) String orderBy,
                                         Principal principal) {
        return reminderService.searchReminder(title, description, day, pageNumber, pageSize,sortBy,orderBy, principal.getName());
    }

    @GetMapping("/sort")
    public Page<Reminder> sortReminder(@RequestParam(required = false) Integer pageNumber,
                                       @RequestParam(required = false) Integer pageSize,
                                       @RequestParam(required = false) String sortBy,
                                       @RequestParam(required = false) String orderBy,
                                       Principal principal) {
        return reminderService.sortReminders(pageNumber, pageSize, sortBy, orderBy, principal.getName());
    }

    @GetMapping("/list")
    public Page<Reminder> listReminder(@RequestParam(required = false) Integer pageNumber,
                                       @RequestParam(required = false) Integer pageSize,
                                       Principal principal) {
        return reminderService.listReminders(pageNumber, pageSize, principal.getName());
    }

    @GetMapping("/filter")
    public Page<Reminder> filterReminder(@RequestParam(required = false) LocalDate before,
                                         @RequestParam(required = false) LocalDate after,
                                         @RequestParam(required = false) Integer pageNumber,
                                         @RequestParam(required = false) Integer pageSize,
                                         @RequestParam(required = false) String sortBy,
                                         @RequestParam(required = false) String orderBy,
                                         Principal principal) {
        return reminderService.filterReminder(before,after,pageNumber,pageSize,sortBy,orderBy,principal.getName());
    }
}
