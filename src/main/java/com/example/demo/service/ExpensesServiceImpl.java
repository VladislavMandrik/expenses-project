package com.example.demo.service;

import com.example.demo.repository.ExpensesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpensesServiceImpl implements ExpensesService {
    private final ExpensesRepository expensesRepository;

}
