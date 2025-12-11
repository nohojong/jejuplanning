package net.codecraft.jejutrip.account.profile.ropository;

import net.codecraft.jejutrip.account.profile.dto.StatisticsResponse;

public interface CustomProfileRepository {
    StatisticsResponse getStatisticsOfUser(String userEmail);
}
