package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.OfferData;
import tn.greencode.hospitality.repository.OfferDataRepository;
import tn.greencode.hospitality.service.OfferDataService;

/**
 * Service Implementation for managing {@link OfferData}.
 */
@Service
@Transactional
public class OfferDataServiceImpl implements OfferDataService {

    private final Logger log = LoggerFactory.getLogger(OfferDataServiceImpl.class);

    private final OfferDataRepository offerDataRepository;

    public OfferDataServiceImpl(OfferDataRepository offerDataRepository) {
        this.offerDataRepository = offerDataRepository;
    }

    @Override
    public OfferData save(OfferData offerData) {
        log.debug("Request to save OfferData : {}", offerData);
        return offerDataRepository.save(offerData);
    }

    @Override
    public OfferData update(OfferData offerData) {
        log.debug("Request to save OfferData : {}", offerData);
        return offerDataRepository.save(offerData);
    }

    @Override
    public Optional<OfferData> partialUpdate(OfferData offerData) {
        log.debug("Request to partially update OfferData : {}", offerData);

        return offerDataRepository
            .findById(offerData.getId())
            .map(existingOfferData -> {
                if (offerData.getLang() != null) {
                    existingOfferData.setLang(offerData.getLang());
                }
                if (offerData.getTitle() != null) {
                    existingOfferData.setTitle(offerData.getTitle());
                }
                if (offerData.getContent() != null) {
                    existingOfferData.setContent(offerData.getContent());
                }
                if (offerData.getImage() != null) {
                    existingOfferData.setImage(offerData.getImage());
                }
                if (offerData.getImageContentType() != null) {
                    existingOfferData.setImageContentType(offerData.getImageContentType());
                }

                return existingOfferData;
            })
            .map(offerDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferData> findAll() {
        log.debug("Request to get all OfferData");
        return offerDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OfferData> findOne(Long id) {
        log.debug("Request to get OfferData : {}", id);
        return offerDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OfferData : {}", id);
        offerDataRepository.deleteById(id);
    }
}
