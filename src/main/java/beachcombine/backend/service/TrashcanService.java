package beachcombine.backend.service;

import beachcombine.backend.domain.Trashcan;
import beachcombine.backend.repository.TrashcanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;
}
