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

import java.time.LocalDate;
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
    @Operation(
            summary = "질문 저장",
            description = "질문 생성 오류시 관리자가 새로 생성된 질문을 저장합니다",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "요청 예시",
                                    value = """
                {
                    "questions": {
                        "SPORTS": "스포츠를 통해 배운 가장 중요한 삶의 교훈은 무엇인가요? 그 교훈이 당신에게 어떻게 도움이 되었나요?",
                        "TRAVEL": "장기 여행을 떠날 수 있다면, 어느 나라를 선택하고 싶은가요? 그곳에서 어떤 경험을 해보고 싶나요?",
                        "ETC": "가장 기억에 남는 친구와의 특별한 순간이나 활동은 무엇인가요? 그 순간이 왜 특별했나요?",
                        "DELICIOUS_RESTAURANT": "가장 좋아하는 길거리 음식은 무엇이며, 그 음식을 먹을 때의 추억이나 특별한 경험이 있다면 공유해 줄 수 있나요?",
                        "FASHION": "올 가을에 가장 끌리는 패션 트렌드는 무엇인가요? 그 스타일을 어떻게 표현하고 싶나요?",
                        "CULTURE": "요즘 스트레스를 해소하기 위해 즐겨하는 취미나 활동은 무엇인가요? 그 활동이 주는 즐거움은 어떤 건가요?"
                    }
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "질문 저장 성공"),
            @ApiResponse(responseCode = "404", description = "없는 카테고리일 때", content = @Content)
    })
    public ResponseEntity<MessageResponse> postQuestions(@RequestBody QuestionApiRequest apiRequest) {
        questionService.saveQuestions(apiRequest.questions(), LocalDate.now());
        questionService.updateQuestionStatus();
        return ResponseEntity.status(201).body(new MessageResponse("저장되었습니다."));
    }


    @PutMapping
    @Operation(
            summary = "질문 수정",
            description = "관리자가 질문을 수정합니다",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "요청 예시",
                                    value = """
                {
                    "questions": {
                        "SPORTS": "스포츠를 통해 배운 가장 중요한 삶의 교훈은 무엇인가요? 그 교훈이 당신에게 어떻게 도움이 되었나요?",
                        "TRAVEL": "장기 여행을 떠날 수 있다면, 어느 나라를 선택하고 싶은가요? 그곳에서 어떤 경험을 해보고 싶나요?",
                        "ETC": "가장 기억에 남는 친구와의 특별한 순간이나 활동은 무엇인가요? 그 순간이 왜 특별했나요?",
                        "DELICIOUS_RESTAURANT": "가장 좋아하는 길거리 음식은 무엇이며, 그 음식을 먹을 때의 추억이나 특별한 경험이 있다면 공유해 줄 수 있나요?",
                        "FASHION": "올 가을에 가장 끌리는 패션 트렌드는 무엇인가요? 그 스타일을 어떻게 표현하고 싶나요?",
                        "CULTURE": "요즘 스트레스를 해소하기 위해 즐겨하는 취미나 활동은 무엇인가요? 그 활동이 주는 즐거움은 어떤 건가요?"
                    }
                }
                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 수정 성공"),
            @ApiResponse(responseCode = "404", description = "없는 카테고리일 때", content = @Content)
    })
    public ResponseEntity<MessageResponse> putQuestions(@RequestBody QuestionApiRequest apiRequest) {
        questionService.updateQuestions(apiRequest.questions());
        return ResponseEntity.ok(new MessageResponse("수정되었습니다."));
    }

}