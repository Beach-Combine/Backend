package beachcombine.backend.controller;

import beachcombine.backend.service.BeachService;
import beachcombine.backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BeachController {

    private final BeachService beachService;
}
