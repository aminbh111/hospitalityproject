package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.Bars;
import tn.greencode.hospitality.repository.BarsRepository;
import tn.greencode.hospitality.service.BarsService;

/**
 * Service Implementation for managing {@link Bars}.
 */
@Service
@Transactional
public class BarsServiceImpl implements BarsService {

    private final Logger log = LoggerFactory.getLogger(BarsServiceImpl.class);

    private final BarsRepository barsRepository;

    public BarsServiceImpl(BarsRepository barsRepository) {
        this.barsRepository = barsRepository;
    }

    @Override
    public Bars save(Bars bars) {
        log.debug("Request to save Bars : {}", bars);
        return barsRepository.save(bars);
    }

    @Override
    public Bars update(Bars bars) {
        log.debug("Request to save Bars : {}", bars);
        return barsRepository.save(bars);
    }

    @Override
    public Optional<Bars> partialUpdate(Bars bars) {
        log.debug("Request to partially update Bars : {}", bars);

        return barsRepository
            .findById(bars.getId())
            .map(existingBars -> {
                if (bars.getDate() != null) {
                    existingBars.setDate(bars.getDate());
                }
                if (bars.getPublish() != null) {
                    existingBars.setPublish(bars.getPublish());
                }
                if (bars.getContentPosition() != null) {
                    existingBars.setContentPosition(bars.getContentPosition());
                }
                if (bars.getImagePosition() != null) {
                    existingBars.setImagePosition(bars.getImagePosition());
                }

                return existingBars;
            })
            .map(barsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bars> findAll() {
        log.debug("Request to get all Bars");
        return barsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bars> findOne(Long id) {
        log.debug("Request to get Bars : {}", id);
        return barsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bars : {}", id);
        barsRepository.deleteById(id);
    }
}
