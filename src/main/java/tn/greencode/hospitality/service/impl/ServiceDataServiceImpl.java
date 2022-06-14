package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.ServiceData;
import tn.greencode.hospitality.repository.ServiceDataRepository;
import tn.greencode.hospitality.service.ServiceDataService;

/**
 * Service Implementation for managing {@link ServiceData}.
 */
@Service
@Transactional
public class ServiceDataServiceImpl implements ServiceDataService {

    private final Logger log = LoggerFactory.getLogger(ServiceDataServiceImpl.class);

    private final ServiceDataRepository serviceDataRepository;

    public ServiceDataServiceImpl(ServiceDataRepository serviceDataRepository) {
        this.serviceDataRepository = serviceDataRepository;
    }

    @Override
    public ServiceData save(ServiceData serviceData) {
        log.debug("Request to save ServiceData : {}", serviceData);
        return serviceDataRepository.save(serviceData);
    }

    @Override
    public ServiceData update(ServiceData serviceData) {
        log.debug("Request to save ServiceData : {}", serviceData);
        return serviceDataRepository.save(serviceData);
    }

    @Override
    public Optional<ServiceData> partialUpdate(ServiceData serviceData) {
        log.debug("Request to partially update ServiceData : {}", serviceData);

        return serviceDataRepository
            .findById(serviceData.getId())
            .map(existingServiceData -> {
                if (serviceData.getLang() != null) {
                    existingServiceData.setLang(serviceData.getLang());
                }
                if (serviceData.getTitle() != null) {
                    existingServiceData.setTitle(serviceData.getTitle());
                }
                if (serviceData.getContent() != null) {
                    existingServiceData.setContent(serviceData.getContent());
                }
                if (serviceData.getImage() != null) {
                    existingServiceData.setImage(serviceData.getImage());
                }
                if (serviceData.getImageContentType() != null) {
                    existingServiceData.setImageContentType(serviceData.getImageContentType());
                }

                return existingServiceData;
            })
            .map(serviceDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceData> findAll() {
        log.debug("Request to get all ServiceData");
        return serviceDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceData> findOne(Long id) {
        log.debug("Request to get ServiceData : {}", id);
        return serviceDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceData : {}", id);
        serviceDataRepository.deleteById(id);
    }
}
