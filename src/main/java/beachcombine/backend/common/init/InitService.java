package beachcombine.backend.common.init;

import beachcombine.backend.domain.Beach;
import beachcombine.backend.domain.Trashcan;
import beachcombine.backend.repository.TrashcanRepository;
import beachcombine.backend.service.GeocodingService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InitService {

    private final EntityManager em;
    private final GeocodingService geocodingService;
    private final TrashcanRepository trashcanRepository;
    @Value("${spring.datasource.databaseAPI}")
    private String serviceKey;

    @Transactional
    public void initDatabase() {

        // 해변 마커용 위치 정보
        Beach beach1 = Beach.builder()
                .id(1L)
                .name("Haeundae Beach")
                .lat(BigDecimal.valueOf(35.158645))
                .lng(BigDecimal.valueOf(129.160920))
                .badgeImage("imageUrl")
                .build();
        Beach beach2 = Beach.builder()
                .id(2L)
                .name("Gwangali Beach")
                .lat(BigDecimal.valueOf(35.1531696))
                .lng(BigDecimal.valueOf(129.118666))
                .badgeImage("imageUrl")
                .build();
        Beach beach3 = Beach.builder()
                .id(3L)
                .name("Gwangan Beach Park")
                .lat(BigDecimal.valueOf(35.1554))
                .lng(BigDecimal.valueOf(129.1234))
                .badgeImage("imageUrl")
                .build();
        Beach beach4 = Beach.builder()
                .id(4L)
                .name("Namcheon Beach Park")
                .lat(BigDecimal.valueOf(35.1465704))
                .lng(BigDecimal.valueOf(129.1147768))
                .badgeImage("imageUrl")
                .build();
        Beach beach5 = Beach.builder()
                .id(5L)
                .name("Millak Waterside Park")
                .lat(BigDecimal.valueOf(35.1545716))
                .lng(BigDecimal.valueOf(129.1329907))
                .badgeImage("imageUrl")
                .build();

        em.merge(beach1);
        em.merge(beach2);
        em.merge(beach3);
        em.merge(beach4);
        em.merge(beach5);
    }


    // 공공데이터포털 - 부산 수영구 쓰레기통 정보 API
    public ResponseEntity<Void> loadTrashcanDataFromApiAndSave() {

        String page = "1";
        String perPage = "80";

        String urlStr = "https://api.odcloud.kr/api/15089095/v1/uddi:c03ea19d-e9b0-4cfe-9714-96a9bb363db6?" +
                "page=" + page +
                "&perPage=" + perPage+
                "&serviceKey="+serviceKey;
        StringBuffer result = new StringBuffer();

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "application/json");
            //urlConnection.setRequestProperty ("Authorization", "Infuser "+serviceKey); // 헤더로 인증

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
                trashcanRepository.save(trashcan);

            }

            return new ResponseEntity(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
