package net.codecraft.jejutrip.admin.controller;

import net.codecraft.jejutrip.admin.dto.BestRestaurantInfoDto;
import net.codecraft.jejutrip.admin.dto.FacilitesInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelAreaInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelInfoDto2;
import net.codecraft.jejutrip.admin.entity.TravelInfo;
import net.codecraft.jejutrip.admin.service.JsonToTableService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@RestController
@ResponseBody
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class JsonToTableController {

    @Autowired
    private JsonToTableService service;

    public JsonToTableController(JsonToTableService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public String saveToDb(TravelInfo dto) throws URISyntaxException, IOException, InterruptedException, DateTimeParseException {
        service.saveDto(dto);

        return "저장 완료";
    }

    public String [] getValue(JSONObject jsonObject, String strName) {
        String [] arrStr = new String[3];

        JSONObject region = jsonObject.optJSONObject(strName);
        if (region != null) {
            arrStr[0] = region.optString("label");
            arrStr[1] = region.optString("refId");
            arrStr[2] = region.optString("value");
        } else {
            System.out.println(strName + " 정보가 없습니다.");
        }

        return arrStr;
    }

    public String [] getValue2(JSONObject jsonObject, String strName) {
        String [] arrStr = new String[3];

        JSONObject region = jsonObject.optJSONObject(strName);

        if (region != null) {
            JSONObject photoid = region.optJSONObject("photoid");

            if(photoid != null) {
                arrStr[0] = photoid.optString("photoid");
                arrStr[1] = photoid.optString("imgpath");
                arrStr[2] = photoid.optString("thumbnailpath");
            }
        } else {
            System.out.println(strName + " 정보가 없습니다.");
        }

        return arrStr;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/save2")
    public String saveToDb2(@RequestBody TravelAreaInfoDto dto) throws URISyntaxException, IOException, InterruptedException, DateTimeParseException {
        int i = 0;
        String [] arrStr = new String[3];
        String [] imgStr = new String[3];

        HttpClient client = HttpClient.newHttpClient();

        String apiKey = "8c92bf2dc6cb4faf8bc336a3811a3944";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.visitjeju.net/vsjApi/contents/searchList?apiKey=" + apiKey + "&locale=kr&page=1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("response.statusCode() " + response.statusCode());

        if (response.statusCode() == 200) {
            // JSON 가져오기
            String jsonStr = "["+response.body()+"]";

            System.out.println(jsonStr);

            try {
                // 문자열 → JSONArray 변환
                JSONArray jsonArray = new JSONArray(jsonStr);

                JSONObject items = jsonArray.getJSONObject(0);
                JSONArray arrJson = items.getJSONArray("items");

                if (jsonArray == null) {
                    return "자료 변환시 오류 발생 !!! [ null ] ";
                }

                for (Object obj : arrJson) {
                    if(obj == null) obj = new JSONObject();
                    JSONObject areaObj = (JSONObject) obj;


                    TravelInfoDto2 travelDto = new TravelInfoDto2();

                    travelDto.setALL_TAG((String) (( areaObj.optString("alltag") == null) ? "":areaObj.optString("alltag")));
                    travelDto.setCONTENTS_ID((String) areaObj.optString("contentsid"));
                    arrStr = getValue(areaObj, "contentscd");
                    travelDto.setCONTENTS_CD_LABEL(arrStr[0]);
                    travelDto.setTITLE((String) (( areaObj.optString("title") == null) ? "no data":areaObj.optString("title")));
                    arrStr = getValue(areaObj, "region1cd");
                    travelDto.setREGION1_CD(arrStr[0]);
                    arrStr = getValue(areaObj, "region2cd");
                    travelDto.setREGION2_CD(arrStr[0]);
                    travelDto.setADDRESS((String) (( areaObj.optString("address") == null) ? "":areaObj.optString("address")));
                    travelDto.setROAD_ADDRESS((String) (( areaObj.optString("roadaddress") == null) ? "":areaObj.optString("roadaddress")));
                    travelDto.setTAG((String) (( areaObj.optString("roadaddress") == null) ? "":areaObj.optString("tag")));

                    travelDto.setINTRODUCTION((String) (( areaObj.optString("introduction") == null) ? "":areaObj.optString("introduction")));
                    travelDto.setINTRODUCTION(travelDto.getINTRODUCTION());

                    if (areaObj.has("latitude") && !areaObj.isNull("latitude")) {
                        String latitude = areaObj.getString("latitude");
                        travelDto.setLATITUDE((latitude == null) ? "0":latitude);
                    }

                    if (areaObj.has("longitude") && !areaObj.isNull("longitude")) {
                        String longitude = areaObj.getString("longitude");
                        travelDto.setLONGITUDE(( longitude == null) ? "0":longitude);
                    }

                    travelDto.setPOST_CODE((String) (( areaObj.optString("postcode") == null) ? "":areaObj.optString("postcode")));
                    travelDto.setPHONE_NO((String) ((areaObj.optString("phoneno") == null) ? "":areaObj.optString("phoneno")));
                    imgStr = getValue2(areaObj, "repPhoto");
                    travelDto.setIMGPATH(imgStr[1]);
                    travelDto.setTHUMBNAILPATH(imgStr[2]);

                    LocalDateTime today = LocalDateTime.now();
                    travelDto.setREG_DT(today);
                    travelDto.setMOD_DT(today);

                    travelDto.setTITLE(areaObj.getString("title"));

                    i++;

                    service.saveDto2(travelDto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Request failed: " + response.statusCode());
        }

        return "저장 완료";
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/save3")
    public String saveTravelAreaInfo(TravelInfoDto2 dto) throws Exception {

        service.saveTravelAreaInfo(dto);

        return "여행지 마스터 저장 완료";
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/save4")
    public String saveFacilitesInfo(FacilitesInfoDto dto) throws Exception {

        service.saveFacilitesInfo(dto);

        return "편의시설 저장 완료";
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/save5")
    public String saveBestRestaurantInfo(BestRestaurantInfoDto dto) throws Exception {

        service.saveBestRestaurantInfo(dto);

        return "맛집 저장 완료";
    }
}