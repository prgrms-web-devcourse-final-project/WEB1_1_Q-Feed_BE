package com.wsws.moduleexternalapi.feed.util;

public class TokenCalculateUtil {
    private static Long PROMPT_TOKEN_COUNT = 0L;
    private static Long GENERATION_TOKEN_COUNT = 0L;
    private static Long TOTAL_TOKEN_COUNT = 0L;

    public static void addPromptToken(Long promptToken) {
        PROMPT_TOKEN_COUNT += promptToken;
    }

    public static Long getPromptToken(){
        return PROMPT_TOKEN_COUNT;
    }

    public static void addGenerationToken(Long generationToken) {
        GENERATION_TOKEN_COUNT += generationToken;
    }

    public static Long getGenerationToken(){
        return GENERATION_TOKEN_COUNT;
    }

    public static void addTotalToken(Long totalToken) {
        TOTAL_TOKEN_COUNT += totalToken;
    }

    public static Long getTotalToken(){
        return TOTAL_TOKEN_COUNT;
    }
}
