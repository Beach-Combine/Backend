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

}
