package com.example.demo.repository;

import com.example.demo.entity.TotalWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<TotalWord, Long> {
    // 按单词或释义搜索
    List<TotalWord> findByWordContainingOrMeaningContaining(String wordKeyword, String meaningKeyword);
}