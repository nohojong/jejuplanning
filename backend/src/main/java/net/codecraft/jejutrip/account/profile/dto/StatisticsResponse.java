package net.codecraft.jejutrip.account.profile.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatisticsResponse {

    private long totalPost;
    private long totalComment;
    private long totalPostView;
    private String joinData;
}