package net.codecraft.jejutrip.tour.batch.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.tour.batch.dto.VisitJejuApiResponse;
import net.codecraft.jejutrip.tour.batch.dto.VisitJejuItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VisitJejuApiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiUrl;
    private final String apiKey;

    public VisitJejuApiClient(RestTemplate restTemplate,
                              ObjectMapper objectMapper,
                              @Value("${visitjeju.api.url}") String apiUrl,
                              @Value("${visitjeju.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public List<VisitJejuItem> fetchAllItems() {
        List<VisitJejuItem> allItems = new ArrayList<>();
        int page = 1;
        int totalPages = 1;

        while (page <= totalPages) {
            log.info("Fetching page {} from Visit Jeju API...", page);

            URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("apiKey", apiKey)
                    .queryParam("locale", "kr")
                    .queryParam("page", String.valueOf(page))
                    .build()
                    .toUri();

            log.info("Request URI: {}", uri.toString());

            try {
                String responseString = restTemplate.getForObject(uri, String.class);
                VisitJejuApiResponse responseDto = objectMapper.readValue(responseString, VisitJejuApiResponse.class);

                if (page == 1) {
                    totalPages = responseDto.getPageCount();
                    log.info("Total pages to fetch set to: {}", totalPages);
                }

                // API가 데이터를 반환했고, 그 데이터가 비어있지 않을 경우에만 리스트에 추가
                if (responseDto != null && responseDto.getItems() != null && !responseDto.getItems().isEmpty()) {
                    allItems.addAll(responseDto.getItems());
                    log.info("Page {} fetched successfully. Total items accumulated: {}", page, allItems.size());
                } else {
                    // API가 빈 items 배열을 보내주면, 그것이 마지막 페이지라는 신호
                    log.warn("No items found on page {}, assuming this is the end. Stopping fetch process.", page);
                    break;
                }

                page++;

                if (page <= totalPages) {
                    Thread.sleep(1000);
                }


            } catch (Exception e) {
                log.error("An unexpected error occurred while fetching data from Visit Jeju API", e);
                break;
            }
        }

        log.info("Total {} items fetched from Visit Jeju API.", allItems.size());

        List<VisitJejuItem> distinctItems = allItems.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(VisitJejuItem::getContentsid))),
                        ArrayList::new
                ));

        log.info("Removed duplicates. Final item count: {}", distinctItems.size());

        return distinctItems;
    }
}