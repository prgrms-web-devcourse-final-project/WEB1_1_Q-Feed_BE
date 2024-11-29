package com.wsws.moduleexternalapi.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
public record AIQuestionResponse(
        @JsonProperty(required = true, value = "travel") String travel,
        @JsonProperty(required = true, value = "deliciousRestaurant") String deliciousRestaurant,
        @JsonProperty(required = true, value = "movie") String movie,
        @JsonProperty(required = true, value = "music") String music,
        @JsonProperty(required = true, value = "reading") String reading,
        @JsonProperty(required = true, value = "sports") String sports) {
}

