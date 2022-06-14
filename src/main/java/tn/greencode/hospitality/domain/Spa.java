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
 * A Spa.
 */
@Entity
@Table(name = "spa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Spa implements Serializable {

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

    @OneToMany(mappedBy = "spa")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "spa" }, allowSetters = true)
    private Set<SpaData> spaData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Spa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Spa date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean getPublish() {
        return this.publish;
    }

    public Spa publish(Boolean publish) {
        this.setPublish(publish);
        return this;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public Position getContentPosition() {
        return this.contentPosition;
    }

    public Spa contentPosition(Position contentPosition) {
        this.setContentPosition(contentPosition);
        return this;
    }

    public void setContentPosition(Position contentPosition) {
        this.contentPosition = contentPosition;
    }

    public Position getImagePosition() {
        return this.imagePosition;
    }

    public Spa imagePosition(Position imagePosition) {
        this.setImagePosition(imagePosition);
        return this;
    }

    public void setImagePosition(Position imagePosition) {
        this.imagePosition = imagePosition;
    }

    public Set<SpaData> getSpaData() {
        return this.spaData;
    }

    public void setSpaData(Set<SpaData> spaData) {
        if (this.spaData != null) {
            this.spaData.forEach(i -> i.setSpa(null));
        }
        if (spaData != null) {
            spaData.forEach(i -> i.setSpa(this));
        }
        this.spaData = spaData;
    }

    public Spa spaData(Set<SpaData> spaData) {
        this.setSpaData(spaData);
        return this;
    }

    public Spa addSpaData(SpaData spaData) {
        this.spaData.add(spaData);
        spaData.setSpa(this);
        return this;
    }

    public Spa removeSpaData(SpaData spaData) {
        this.spaData.remove(spaData);
        spaData.setSpa(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spa)) {
            return false;
        }
        return id != null && id.equals(((Spa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Spa{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", publish='" + getPublish() + "'" +
            ", contentPosition='" + getContentPosition() + "'" +
            ", imagePosition='" + getImagePosition() + "'" +
            "}";
    }
}
