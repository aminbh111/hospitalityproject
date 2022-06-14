package tn.greencode.hospitality.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.greencode.hospitality.domain.enumeration.Position;

/**
 * A RestaurantMenu.
 */
@Entity
@Table(name = "restaurant_menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RestaurantMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "publish")
    private Boolean publish;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_position")
    private Position contentPosition;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_position")
    private Position imagePosition;

    @OneToMany(mappedBy = "restaurantMenu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurantMenu" }, allowSetters = true)
    private Set<RestaurantMenuData> restaurantMenuData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RestaurantMenu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public RestaurantMenu date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean getPublish() {
        return this.publish;
    }

    public RestaurantMenu publish(Boolean publish) {
        this.setPublish(publish);
        return this;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public Position getContentPosition() {
        return this.contentPosition;
    }

    public RestaurantMenu contentPosition(Position contentPosition) {
        this.setContentPosition(contentPosition);
        return this;
    }

    public void setContentPosition(Position contentPosition) {
        this.contentPosition = contentPosition;
    }

    public Position getImagePosition() {
        return this.imagePosition;
    }

    public RestaurantMenu imagePosition(Position imagePosition) {
        this.setImagePosition(imagePosition);
        return this;
    }

    public void setImagePosition(Position imagePosition) {
        this.imagePosition = imagePosition;
    }

    public Set<RestaurantMenuData> getRestaurantMenuData() {
        return this.restaurantMenuData;
    }

    public void setRestaurantMenuData(Set<RestaurantMenuData> restaurantMenuData) {
        if (this.restaurantMenuData != null) {
            this.restaurantMenuData.forEach(i -> i.setRestaurantMenu(null));
        }
        if (restaurantMenuData != null) {
            restaurantMenuData.forEach(i -> i.setRestaurantMenu(this));
        }
        this.restaurantMenuData = restaurantMenuData;
    }

    public RestaurantMenu restaurantMenuData(Set<RestaurantMenuData> restaurantMenuData) {
        this.setRestaurantMenuData(restaurantMenuData);
        return this;
    }

    public RestaurantMenu addRestaurantMenuData(RestaurantMenuData restaurantMenuData) {
        this.restaurantMenuData.add(restaurantMenuData);
        restaurantMenuData.setRestaurantMenu(this);
        return this;
    }

    public RestaurantMenu removeRestaurantMenuData(RestaurantMenuData restaurantMenuData) {
        this.restaurantMenuData.remove(restaurantMenuData);
        restaurantMenuData.setRestaurantMenu(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantMenu)) {
            return false;
        }
        return id != null && id.equals(((RestaurantMenu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantMenu{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", publish='" + getPublish() + "'" +
            ", contentPosition='" + getContentPosition() + "'" +
            ", imagePosition='" + getImagePosition() + "'" +
            "}";
    }
}
