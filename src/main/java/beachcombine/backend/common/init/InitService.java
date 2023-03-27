package beachcombine.backend.common.init;

import beachcombine.backend.common.exception.CustomException;
import beachcombine.backend.common.exception.ErrorCode;
import beachcombine.backend.domain.*;
import beachcombine.backend.repository.TrashcanRepository;
import beachcombine.backend.util.GeocodingUtil;

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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InitService {

    private final EntityManager em;
    private final GeocodingUtil geocodingUtil;
    private final TrashcanRepository trashcanRepository;
    @Value("${spring.datasource.databaseAPI}")
    private String serviceKey;

    @Transactional
    public void initDatabase() {

        // 해변 마커용 위치 정보
        Beach beach1 = Beach.builder()
                .name("Haeundae Beach")
                .lat(BigDecimal.valueOf(35.158645))
                .lng(BigDecimal.valueOf(129.160920))
                .badgeImage("imageUrl")
                .beachRange("{35.1595, 129.1623}, {35.1597, 129.1657}, {35.1593, 129.1698}, {35.1585, 129.1708}, "+
                        "{35.1541, 129.1547}, {35.1567, 129.1539}, {35.1585, 129.1578}")
                .build();
        Beach beach2 = Beach.builder()
                .name("Gwangali Beach")
                .lat(BigDecimal.valueOf(35.1531696))
                .lng(BigDecimal.valueOf(129.118666))
                .badgeImage("imageUrl")
                .beachRange("{35.1558, 129.1236}, {35.1527, 129.1248}, {35.1459, 129.11791}, {35.146, 129.1147}, "+
                        "{35.1471, 129.1141}, {35.1491, 129.1149}, {35.1535, 129.1182}, {35.1546,129.1199}")
                .build();
        Beach beach3 = Beach.builder()
                .name("Gwangan Beach Park")
                .lat(BigDecimal.valueOf(35.1554))
                .lng(BigDecimal.valueOf(129.1234))
                .badgeImage("imageUrl")
                .beachRange("{35.1556, 129.1235}, {35.1531, 129.1244}, {35.1534, 129.1223}, {35.1551, 129.1214}")
                .build();
        Beach beach4 = Beach.builder()
                .name("Namcheon Beach Park")
                .lat(BigDecimal.valueOf(35.1465704))
                .lng(BigDecimal.valueOf(129.1147768))
                .badgeImage("imageUrl")
                .beachRange("{35.1477, 129.1144}, {35.1474, 129.117}, {35.1459, 129.1174}, {35.146, 129.1147}")
                .build();
        Beach beach5 = Beach.builder()
                .name("Millak Waterside Park")
                .lat(BigDecimal.valueOf(35.1545716))
                .lng(BigDecimal.valueOf(129.1329907))
                .badgeImage("imageUrl")
                .beachRange("{35.1551, 129.1214}, {35.1534, 129.1223}, {35.1474, 129.117}, {35.1477, 129.1144}")
                .build();
        Beach beach6 = Beach.builder()
                .name("Songjeong Beach")
                .lat(BigDecimal.valueOf(35.1786125))
                .lng(BigDecimal.valueOf(129.1997133))
                .badgeImage("imageUrl")
                .beachRange("{35.1789, 129.199}, {35.1805, 129.2022}, {35.1809, 129.2049}, {35.1795, 129.2052},"+
                        "{35.1746, 129.1975}, {35.1759, 129.1971}")
                .build();

        Store store1 = Store.builder()
                .name("zero-waste store")
                .location("Gangnam-gu")
                .image("storeimageUrl")
                .build();

        Giftcard giftcard1 = Giftcard.builder()
                .cost(1000)
                .store(store1)
                .build();

        for(int i=0; i<25; i++) {
            Member member = Member.builder()
                    .loginId(i+"st_"+"user")
                    .password("")
                    .email(i+"st_"+"user@gmail.com")
                    .nickname(i+"st_"+"user")
                    .role("ROLE_USER")
                    .totalPoint(i*100)
                    .monthPoint(i*100)
                    .build();
            em.merge(member);
        }

        em.merge(beach1);
        em.merge(beach2);
        em.merge(beach3);
        em.merge(beach4);
        em.merge(beach5);
        em.merge(beach6);
        em.persist(store1);
        em.merge(giftcard1);

        // 쓰레기통 좌표 수정
        Trashcan trashcan1 = trashcanRepository.findById(77L)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRASHCAN));
        trashcan1.updateCoords(new BigDecimal( 35.1527), new BigDecimal(129.1182));
        Trashcan trashcan2 = trashcanRepository.findById(78L)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRASHCAN));
        trashcan2.updateCoords(new BigDecimal(35.1471), new BigDecimal(129.1145));
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
                Map<String, String> coords = geocodingUtil.getCoordsByAddress(address);

                Trashcan trashcan = Trashcan.builder()
                        .lat(new BigDecimal(coords.get("lat")))
                        .lng(new BigDecimal(coords.get("lng")))
                        .isCertified(true)
                        .isAddedByUser(false)
                        .address(address)
                        .build();
                trashcanRepository.save(trashcan);

            }
            return new ResponseEntity(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
