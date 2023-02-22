package beachcombine.backend.controller;

import beachcombine.backend.common.auth.PrincipalDetails;
import beachcombine.backend.domain.Trashcan;
import beachcombine.backend.dto.request.TrashcanSaveRequest;
import beachcombine.backend.dto.response.IdResponse;
import beachcombine.backend.dto.response.TrashcanResponse;
import beachcombine.backend.service.GeocodingService;
import beachcombine.backend.service.TrashcanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("trashcans")
public class TrashcanController {

    private final TrashcanService trashcanService;

    // (지도) 쓰레기통 위치 조회
    @GetMapping("map")
    public ResponseEntity<List<TrashcanResponse>> findTrashcanLocation(){

        List<TrashcanResponse> trashcanResponse = trashcanService.findCertifiedTrashcanCoords();

        return ResponseEntity.status(HttpStatus.OK).body(trashcanResponse);
    }

    // 쓰레기통 신고하기
    @PostMapping("")
    public ResponseEntity<IdResponse> saveTrashcan (@AuthenticationPrincipal PrincipalDetails userDetails,
                                                    TrashcanSaveRequest request) throws IOException {

        Long trashcanId = trashcanService.saveTrashcan(userDetails.getMember().getId(), request);

        IdResponse response = IdResponse.builder()
                .id(trashcanId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // test
    @Value("${spring.datasource.databaseAPI}")
    private String serviceKey;
    private final GeocodingService geocodingService;
    @GetMapping("/api")
    public ResponseEntity<Void> loadTrashcanDataFromApiAndSave() {

        String page = "1";
        String perPage = "8";

        String urlStr = "https://api.odcloud.kr/api/15089095/v1/uddi:c03ea19d-e9b0-4cfe-9714-96a9bb363db6?" +
                "page=" + page +
                "&perPage=" + perPage;
        StringBuffer result = new StringBuffer();
        System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty ("Authorization", "Infuser "+serviceKey);

            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result.append(bf.readLine());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
            for(int i=0; i<jsonArray.size(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String address = (String) object.get("설치장소");
                // int trashcanCount = (int) object.get("개수");
                Map<String, String> coords = geocodingService.getGeoDataByAddress(address);

                Trashcan trashcan = Trashcan.builder()
                        .lat(new BigDecimal(coords.get("lat")))
                        .lng(new BigDecimal(coords.get("lng")))
                        .isCertified(true)
                        .isAddedByUser(false)
                        .build();
                //trashcanRepository.save(trashcan);
                //em.merge(trashcan);
                System.out.println("\n>>>>> 설치장소: " +address);
                System.out.println(">>>>> lat: " +coords.get("lat"));
            }

            return new ResponseEntity(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
