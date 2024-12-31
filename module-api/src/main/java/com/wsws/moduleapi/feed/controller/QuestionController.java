package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.MessageResponse;
import com.wsws.moduleapi.feed.dto.question.QuestionApiResponse;
import com.wsws.moduleapi.feed.dto.question.QuestionApiRequest;
import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;
import com.wsws.moduleapplication.feed.service.QuestionAIService;
import com.wsws.moduleapplication.feed.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/feed/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionAIService questionAIService;

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

    @GetMapping("/generate")
    @Operation(summary = "질문 생성 및 조회", description = "AI가 카테고리별로 질문을 생성하고 해당 질문을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "질문 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "    \"SPORTS\": \"운동을 하면서 가장 기억에 남는 순간이나 성취는 무엇이었나요?\",\n" +
                                    "    \"TRAVEL\": \"당신이 가장 가보고 싶은 국가나 도시가 있다면 어디인가요? 그곳에 가고 싶은 이유는 무엇인가요?\",\n" +
                                    "    \"ETC\": \"최근에 참여한 특별한 이벤트나 축제가 있다면, 그 경험이 어땠는지 이야기해 줄 수 있나요?\",\n" +
                                    "    \"DELICIOUS_RESTAURANT\": \"가족이나 친구와 함께 갔던 기억에 남는 식당이 있다면 어떤 곳이며, 그곳의 분위기는 어땠나요?\",\n" +
                                    "    \"FASHION\": \"최근에 발견한 패션 브랜드나 디자이너가 있다면 누구인지, 그 이유는 무엇인가요?\",\n" +
                                    "    \"CULTURE\": \"최근에 읽은 책 중에서 가장 기억에 남는 것은 무엇이며, 그 책이 당신에게 어떤 영향을 미쳤나요?\"\n" +
                                    "}"
                    )
            )
    )
    public ResponseEntity<Map<String, String>> generateAndGetQuestions() {
        return ResponseEntity.ok(questionAIService.generateAndValidateQuestions());
    }

    @PostMapping
    public ResponseEntity<MessageResponse> postQuestions(QuestionApiRequest apiRequest) {
        return ResponseEntity.ok(new MessageResponse("저장되었습니다."));
    }

    @PutMapping
    public ResponseEntity<MessageResponse> putQuestions(QuestionApiRequest apiRequest) {
        return ResponseEntity.ok(new MessageResponse("수정되었습니다."));
    }

}