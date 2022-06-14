package tn.greencode.hospitality.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, tn.greencode.hospitality.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, tn.greencode.hospitality.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, tn.greencode.hospitality.domain.User.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Authority.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.User.class.getName() + ".authorities");
            createCache(cm, tn.greencode.hospitality.domain.Service.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Service.class.getName() + ".serviceData");
            createCache(cm, tn.greencode.hospitality.domain.ServiceData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.RestaurantMenu.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.RestaurantMenu.class.getName() + ".restaurantMenuData");
            createCache(cm, tn.greencode.hospitality.domain.RestaurantMenuData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Event.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Event.class.getName() + ".eventData");
            createCache(cm, tn.greencode.hospitality.domain.EventData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.AboutUs.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.AboutUs.class.getName() + ".aboutUsData");
            createCache(cm, tn.greencode.hospitality.domain.AboutUsData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.ContactUs.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.ContactUs.class.getName() + ".contactUsData");
            createCache(cm, tn.greencode.hospitality.domain.ContactUsData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Spa.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Spa.class.getName() + ".spaData");
            createCache(cm, tn.greencode.hospitality.domain.SpaData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.RoomService.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.RoomService.class.getName() + ".roomServiceData");
            createCache(cm, tn.greencode.hospitality.domain.RoomServiceData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Bars.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Bars.class.getName() + ".barsData");
            createCache(cm, tn.greencode.hospitality.domain.BarsData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Meeting.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Meeting.class.getName() + ".meetingData");
            createCache(cm, tn.greencode.hospitality.domain.MeetingData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Offer.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Offer.class.getName() + ".offerData");
            createCache(cm, tn.greencode.hospitality.domain.OfferData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Concierge.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Concierge.class.getName() + ".conciergeData");
            createCache(cm, tn.greencode.hospitality.domain.ConciergeData.class.getName());
            createCache(cm, tn.greencode.hospitality.domain.Playlist.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
