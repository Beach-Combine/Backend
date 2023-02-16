package beachcombine.backend.controller;

import beachcombine.backend.service.RecordService;
import beachcombine.backend.service.TrashcanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TrashcanController {

    private final TrashcanService trashcanService;
}
