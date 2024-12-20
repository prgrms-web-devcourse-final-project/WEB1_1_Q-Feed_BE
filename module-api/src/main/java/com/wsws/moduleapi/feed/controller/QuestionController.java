package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.question.QuestionApiResponse;
import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;
import com.wsws.moduleapplication.feed.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "질문 조회", description = "해당 카테고리의 오늘 생성된 질문을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 조회 성공"),
            @ApiResponse(responseCode = "404", description = "없는 질문일 때", content = @Content)
    })
    public ResponseEntity<QuestionApiResponse> getDailyQuestions(
            @Parameter(description = "질문을 조회할 카테고리 ID") @RequestParam("category-id")Long categoryId) {
        QuestionFindServiceResponse questionFindServiceResponse = questionService.findQuestionByCategoryId(categoryId);

        return ResponseEntity.ok().body(new QuestionApiResponse(questionFindServiceResponse));
    }
}