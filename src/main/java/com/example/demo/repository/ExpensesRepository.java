package com.example.demo.repository;

import com.example.demo.model.Expenses;
import com.example.demo.model.Sum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    List<Expenses> findAllByNameAndCategoryNotOrderByDateDesc(String s, String n);

    @Query(value = "select :zp - SUM(sum) from expenses where deleted = false and name = 'V' and category != 'ЗП'", nativeQuery = true)
    Integer getV(Integer zp);
    @Query(value = "select SUM(sum) from expenses where deleted = false and name = 'V' and category = 'ЗП'", nativeQuery = true)
    Integer getVZP();

    @Query(value = "select :zp - SUM(sum) from expenses where deleted = false and name = 'J' and category != 'ЗП'", nativeQuery = true)
    Integer getJ(Integer zp);
    @Query(value = "select SUM(sum) from expenses where deleted = false and name = 'J' and category = 'ЗП'", nativeQuery = true)
    Integer getJZP();
    @Query(value = "select SUM(sum) from expenses where deleted = false and name = 'J' and category = 'ЗП' and comments = 'волосы'", nativeQuery = true)
    Integer getJZPHair();
    @Query(value = "select SUM(sum) from expenses where deleted = false and name = 'J' and category = 'ЗП' and comments != 'волосы'", nativeQuery = true)
    Integer getJZPWithoutHair();

    @Query(value = "select * from expenses where date between :from and :to and deleted = false and category != 'ЗП' and name = 'V' and category = :category", nativeQuery = true)
    List<Expenses> getFilteredV(Timestamp from, Timestamp to, String category);
    @Query(value = "select * from expenses where date between :from and :to and deleted = false and category != 'ЗП' and name = 'J' and category = :category", nativeQuery = true)
    List<Expenses> getFilteredJ(Timestamp from, Timestamp to, String category);

    @Query(value = "select * from expenses where date between :from and :to and deleted = false and category != 'ЗП' and name = 'V'", nativeQuery = true)
    List<Expenses> getFilteredWithoutCategoryV(Timestamp from, Timestamp to);
    @Query(value = "select * from expenses where date between :from and :to and deleted = false and category != 'ЗП' and name = 'J'", nativeQuery = true)
    List<Expenses> getFilteredWithoutCategoryJ(Timestamp from, Timestamp to);

    @Query(value = "select SUM(sum) from expenses where date between :from and :to and deleted = false and name = 'V' and category = 'ЗП'", nativeQuery = true)
    Integer getFilteredSalaryV(Timestamp from, Timestamp to);
    @Query(value = "select SUM(sum) from expenses where date between :from and :to and deleted = false and name = 'J' and category = 'ЗП' and comments = 'волосы'", nativeQuery = true)
    Integer getFilteredSalaryHairJ(Timestamp from, Timestamp to);
    @Query(value = "select SUM(sum) from expenses where date between :from and :to and deleted = false and name = 'J' and category = 'ЗП' and comments != 'волосы'", nativeQuery = true)
    Integer getFilteredSalaryWithoutHairJ(Timestamp from, Timestamp to);

    @Query(value = "select SUM(sum) from expenses where date between :from and :to and deleted = false and name = 'V' and category != 'ЗП' group by category", nativeQuery = true)
    List<Integer> getSumByCategoryV(Timestamp from, Timestamp to);
    @Query(value = "select category from expenses where date between :from and :to and deleted = false and name = 'V' and category != 'ЗП' group by category", nativeQuery = true)
    List<String> getCategoryByCategoryV(Timestamp from, Timestamp to);

    @Query(value = "select SUM(sum) from expenses where date between :from and :to and deleted = false and name = 'J' and category != 'ЗП' group by category", nativeQuery = true)
    List<Integer> getSumByCategoryJ(Timestamp from, Timestamp to);
    @Query(value = "select category from expenses where date between :from and :to and deleted = false and name = 'J' and category != 'ЗП' group by category", nativeQuery = true)
    List<String> getCategoryByCategoryJ(Timestamp from, Timestamp to);
//    List<Expenses> findAllByMasterName(String s);
}
