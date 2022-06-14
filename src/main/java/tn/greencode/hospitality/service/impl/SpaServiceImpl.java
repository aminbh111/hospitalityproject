package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.Spa;
import tn.greencode.hospitality.repository.SpaRepository;
import tn.greencode.hospitality.service.SpaService;

/**
 * Service Implementation for managing {@link Spa}.
 */
@Service
@Transactional
public class SpaServiceImpl implements SpaService {

    private final Logger log = LoggerFactory.getLogger(SpaServiceImpl.class);

    private final SpaRepository spaRepository;

    public SpaServiceImpl(SpaRepository spaRepository) {
        this.spaRepository = spaRepository;
    }

    @Override
    public Spa save(Spa spa) {
        log.debug("Request to save Spa : {}", spa);
        return spaRepository.save(spa);
    }

    @Override
    public Spa update(Spa spa) {
        log.debug("Request to save Spa : {}", spa);
        return spaRepository.save(spa);
    }

    @Override
    public Optional<Spa> partialUpdate(Spa spa) {
        log.debug("Request to partially update Spa : {}", spa);

        return spaRepository
            .findById(spa.getId())
            .map(existingSpa -> {
                if (spa.getDate() != null) {
                    existingSpa.setDate(spa.getDate());
                }
                if (spa.getPublish() != null) {
                    existingSpa.setPublish(spa.getPublish());
                }
                if (spa.getContentPosition() != null) {
                    existingSpa.setContentPosition(spa.getContentPosition());
                }
                if (spa.getImagePosition() != null) {
                    existingSpa.setImagePosition(spa.getImagePosition());
                }

                return existingSpa;
            })
            .map(spaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Spa> findAll() {
        log.debug("Request to get all Spas");
        return spaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Spa> findOne(Long id) {
        log.debug("Request to get Spa : {}", id);
        return spaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Spa : {}", id);
        spaRepository.deleteById(id);
    }
}
