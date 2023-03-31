package beachcombine.backend.common.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    // 초기 환경설정에서만 주석 해제해서 사용
//    @PostConstruct
//    public void init() {
//
//        initService.loadTrashcanDataFromApiAndSave();
//        initService.initDatabase();
//    }

}