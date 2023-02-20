package beachcombine.backend.service;

import beachcombine.backend.domain.Record;
import beachcombine.backend.dto.request.RecordSaveRequest;
import beachcombine.backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;

    public void saveRecord(Long id, RecordSaveRequest request) throws IOException {

        
    }
}
