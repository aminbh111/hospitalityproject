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
 * A Bars.
 */
@Entity
@Table(name = "bars")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bars implements Serializable {

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

    @OneToMany(mappedBy = "bars")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bars" }, allowSetters = true)
    private Set<BarsData> barsData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bars id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Bars date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean getPublish() {
        return this.publish;
    }

    public Bars publish(Boolean publish) {
        this.setPublish(publish);
        return this;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public Position getContentPosition() {
        return this.contentPosition;
    }

    public Bars contentPosition(Position contentPosition) {
        this.setContentPosition(contentPosition);
        return this;
    }

    public void setContentPosition(Position contentPosition) {
        this.contentPosition = contentPosition;
    }

    public Position getImagePosition() {
        return this.imagePosition;
    }

    public Bars imagePosition(Position imagePosition) {
        this.setImagePosition(imagePosition);
        return this;
    }

    public void setImagePosition(Position imagePosition) {
        this.imagePosition = imagePosition;
    }

    public Set<BarsData> getBarsData() {
        return this.barsData;
    }

    public void setBarsData(Set<BarsData> barsData) {
        if (this.barsData != null) {
            this.barsData.forEach(i -> i.setBars(null));
        }
        if (barsData != null) {
            barsData.forEach(i -> i.setBars(this));
        }
        this.barsData = barsData;
    }

    public Bars barsData(Set<BarsData> barsData) {
        this.setBarsData(barsData);
        return this;
    }

    public Bars addBarsData(BarsData barsData) {
        this.barsData.add(barsData);
        barsData.setBars(this);
        return this;
    }

    public Bars removeBarsData(BarsData barsData) {
        this.barsData.remove(barsData);
        barsData.setBars(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bars)) {
            return false;
        }
        return id != null && id.equals(((Bars) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bars{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", publish='" + getPublish() + "'" +
            ", contentPosition='" + getContentPosition() + "'" +
            ", imagePosition='" + getImagePosition() + "'" +
            "}";
    }
}
