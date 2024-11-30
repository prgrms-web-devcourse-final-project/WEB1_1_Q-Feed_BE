package com.wsws.moduleexternalapi.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
public record AIQuestionResponse(
        @JsonProperty(required = true, value = "TRAVEL") String travel,
        @JsonProperty(required = true, value = "DELICIOUS_RESTAURANT") String deliciousRestaurant,
        @JsonProperty(required = true, value = "MOVIE") String movie,
        @JsonProperty(required = true, value = "MUSIC") String music,
        @JsonProperty(required = true, value = "READING") String reading,
        @JsonProperty(required = true, value = "SPORTS") String sports) {
}

