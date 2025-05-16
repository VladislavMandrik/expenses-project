package com.example.demo.controller;

import com.example.demo.model.Expenses;
import com.example.demo.model.Sum;
import com.example.demo.repository.ExpensesRepository;
import com.example.demo.service.ExpensesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Controller
public class ExpensesControllerImpl implements ExpensesController {

    private final ExpensesServiceImpl expensesService;
    private final ExpensesRepository expensesRepository;
    private Timestamp date;
    private Timestamp filterFrom;
    private Timestamp filterTo;
    private String filterCategory;
    private Timestamp salaryFilterFrom;
    private Timestamp salaryFilterTo;

    public String getExpenses(Model model) {
        model.addAttribute("expensesV", expensesRepository.findAllByNameAndCategoryNotOrderByDateDesc("V", "ЗП"));
        model.addAttribute("expensesJ", expensesRepository.findAllByNameAndCategoryNotOrderByDateDesc("J", "ЗП"));
        model.addAttribute("V", expensesRepository.getV(expensesRepository.getVZP()));
        model.addAttribute("J", expensesRepository.getJ(expensesRepository.getJZP()));
        return "expenses_page";
    }

    public String newExpenses(Map<String, Object> model) {
        Expenses expenses = new Expenses();
        model.put("expenses", expenses);
        return "create_expenses";
    }

    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Expenses expenses = expensesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expenses Id:" + id));
        model.addAttribute("expenses", expenses);
        date = expenses.getDate();
        return "edit_expenses";
    }

    public String updateExpenses(@PathVariable("id") long id, @Valid Expenses expenses, BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            expenses.setId(id);
            return "edit_expenses";
        }

        expenses.setDate(date);
        date = null;
        expensesRepository.save(expenses);
        model.addAttribute("expenses", expensesRepository.findAll());
        return "redirect:/get";
    }

    public String saveExpenses(@ModelAttribute("expenses") Expenses expenses) {
        expenses.setDate(new Timestamp(System.currentTimeMillis())
//                .now(ZoneId.of("Europe/Moscow")
        );
        expensesRepository.save(expenses);
        return "redirect:/get";
    }

    public String showDeleteForm(@PathVariable("id") long id) {
        Expenses expenses = expensesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expenses Id:" + id));
        expensesRepository.delete(expenses);
        return "redirect:/get";
    }

    public String filter(Model model) {
        if (!Objects.equals(filterCategory, "")) {
            model.addAttribute("filteredV", expensesRepository.getFilteredV(filterFrom, filterTo, filterCategory));
            model.addAttribute("filteredJ", expensesRepository.getFilteredJ(filterFrom, filterTo, filterCategory));

            Integer countV = 0;
            Integer countJ = 0;

            List<Expenses> filteredV = expensesRepository.getFilteredV(filterFrom, filterTo, filterCategory);
            List<Expenses> filteredJ = expensesRepository.getFilteredJ(filterFrom, filterTo, filterCategory);

            for (Expenses e : filteredV) {
                countV += e.getSum();
            }
            for (Expenses e : filteredJ) {
                countJ += e.getSum();
            }

            model.addAttribute("countV", countV);
            model.addAttribute("countJ", countJ);
        } else {
            model.addAttribute("filteredV", expensesRepository.getFilteredWithoutCategoryV(filterFrom, filterTo));
            model.addAttribute("filteredJ", expensesRepository.getFilteredWithoutCategoryJ(filterFrom, filterTo));

            List<Sum> l = new ArrayList<>();
            AtomicInteger count = new AtomicInteger();
            List<Integer> sumByCategoryV = expensesRepository.getSumByCategoryV(filterFrom, filterTo);
            expensesRepository.getCategoryByCategoryV(filterFrom, filterTo).forEach(s -> {
                if (s != null) {
                    l.add(new Sum(s, sumByCategoryV.get(count.get())));
                    count.getAndIncrement();
                }
            });
//            l.sort(Comparator.comparing(Sum::getSum).reversed());
            model.addAttribute("sumByCategoryV", l);

            List<Sum> lJ = new ArrayList<>();
            AtomicInteger cJ = new AtomicInteger();
            List<Integer> sumByCategoryJ = expensesRepository.getSumByCategoryJ(filterFrom, filterTo);
            expensesRepository.getCategoryByCategoryJ(filterFrom, filterTo).forEach(s -> {
                if (s != null && cJ.get() != Integer.MAX_VALUE) {
                    lJ.add(new Sum(s, sumByCategoryJ.get(cJ.get())));
                    cJ.getAndIncrement();
                }
            });
//            lJ.sort(Comparator.comparing(Sum::getSum).reversed());
            model.addAttribute("sumByCategoryJ", lJ);

            Integer countV = 0;
            Integer countJ = 0;

            List<Expenses> filteredV = expensesRepository.getFilteredWithoutCategoryV(filterFrom, filterTo);
            List<Expenses> filteredJ = expensesRepository.getFilteredWithoutCategoryJ(filterFrom, filterTo);

            for (Expenses e : filteredV) {
                countV += e.getSum();
            }
            for (Expenses e : filteredJ) {
                countJ += e.getSum();
            }

            model.addAttribute("countV", countV);
            model.addAttribute("countJ", countJ);
        }
        return "filter_expenses";
    }

    public String newFilter(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("category") String category) {
        filterFrom = Timestamp.valueOf(from + " 00:00:00");
        filterTo = Timestamp.valueOf(to + " 23:59:59");
        filterCategory = category;
        return "redirect:/filter";
    }

    public String getSalary(Model model) {
        model.addAttribute("salaryV", expensesRepository.getFilteredSalaryV(salaryFilterFrom, salaryFilterTo));
        model.addAttribute("salaryJHair", expensesRepository.getFilteredSalaryHairJ(salaryFilterFrom, salaryFilterTo));
        model.addAttribute("salaryJWithoutHair", expensesRepository.getFilteredSalaryWithoutHairJ(salaryFilterFrom, salaryFilterTo));
        return "salary_page";
    }

    public String salaryFilter(@RequestParam("from") Timestamp from, @RequestParam("to") Timestamp to) {
        salaryFilterFrom = from;
        salaryFilterTo = to;
        return "redirect:/salary";
    }
}