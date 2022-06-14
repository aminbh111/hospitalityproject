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
 * A Concierge.
 */
@Entity
@Table(name = "concierge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Concierge implements Serializable {

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

    @OneToMany(mappedBy = "concierge")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "concierge" }, allowSetters = true)
    private Set<ConciergeData> conciergeData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Concierge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Concierge date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean getPublish() {
        return this.publish;
    }

    public Concierge publish(Boolean publish) {
        this.setPublish(publish);
        return this;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public Position getContentPosition() {
        return this.contentPosition;
    }

    public Concierge contentPosition(Position contentPosition) {
        this.setContentPosition(contentPosition);
        return this;
    }

    public void setContentPosition(Position contentPosition) {
        this.contentPosition = contentPosition;
    }

    public Position getImagePosition() {
        return this.imagePosition;
    }

    public Concierge imagePosition(Position imagePosition) {
        this.setImagePosition(imagePosition);
        return this;
    }

    public void setImagePosition(Position imagePosition) {
        this.imagePosition = imagePosition;
    }

    public Set<ConciergeData> getConciergeData() {
        return this.conciergeData;
    }

    public void setConciergeData(Set<ConciergeData> conciergeData) {
        if (this.conciergeData != null) {
            this.conciergeData.forEach(i -> i.setConcierge(null));
        }
        if (conciergeData != null) {
            conciergeData.forEach(i -> i.setConcierge(this));
        }
        this.conciergeData = conciergeData;
    }

    public Concierge conciergeData(Set<ConciergeData> conciergeData) {
        this.setConciergeData(conciergeData);
        return this;
    }

    public Concierge addConciergeData(ConciergeData conciergeData) {
        this.conciergeData.add(conciergeData);
        conciergeData.setConcierge(this);
        return this;
    }

    public Concierge removeConciergeData(ConciergeData conciergeData) {
        this.conciergeData.remove(conciergeData);
        conciergeData.setConcierge(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Concierge)) {
            return false;
        }
        return id != null && id.equals(((Concierge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Concierge{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", publish='" + getPublish() + "'" +
            ", contentPosition='" + getContentPosition() + "'" +
            ", imagePosition='" + getImagePosition() + "'" +
            "}";
    }
}
