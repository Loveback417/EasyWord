package com.example.demo.controller;

import com.example.demo.entity.TotalWord;
import com.example.demo.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/words")
public class WordController {

    @Autowired
    private WordRepository wordRepository;

    // 查询所有单词（带搜索功能）
    @GetMapping
    public String listWords(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("words", wordRepository.findByWordContainingOrMeaningContaining(keyword, keyword));
        } else {
            model.addAttribute("words", wordRepository.findAll());
        }
        return "word";
    }

    // 删除单词
    @GetMapping("/delete/{id}")
    public String deleteWord(@PathVariable Long id) {
        wordRepository.deleteById(id);
        return "redirect:/words";
    }
}