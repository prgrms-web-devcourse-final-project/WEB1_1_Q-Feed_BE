package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.question.QuestionApiResponse;
import com.wsws.moduleapplication.feed.service.QuestionService;
import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/daily")
    public ResponseEntity<QuestionApiResponse> getDailyQuestions(@RequestParam("category-id")Long categoryId) {
        QuestionFindServiceResponse questionFindServiceResponse = questionService.findQuestionByCategoryId(categoryId);

        return ResponseEntity.ok().body(new QuestionApiResponse(questionFindServiceResponse));
    }
}
