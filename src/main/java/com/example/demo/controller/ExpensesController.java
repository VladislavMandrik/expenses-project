package com.example.demo.controller;

import com.example.demo.model.Expenses;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Map;

public interface ExpensesController {
    @GetMapping("/get")
    String getExpenses(Model model);

    @GetMapping("/new")
    String newExpenses(Map<String, Object> model);

    @GetMapping("edit/{id}")
    String showUpdateForm(@PathVariable("id") long id, Model model);

    @PostMapping("update/{id}")
    String updateExpenses(@PathVariable("id") long id, @Valid Expenses expenses, BindingResult result,
                          Model model);

    @PostMapping("/save")
    String saveExpenses(@ModelAttribute("expenses") Expenses expenses);

    @GetMapping("delete/{id}")
    String showDeleteForm(@PathVariable("id") long id);

    @GetMapping("/filter")
    String filter(Model model);

    @PostMapping("/new-filter")
    String newFilter(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("category") String category);

    @GetMapping("/salary")
    String getSalary(Model model);

    @PostMapping("/salary-filter")
    String salaryFilter(@RequestParam("from") Timestamp from, @RequestParam("to") Timestamp to);
}
