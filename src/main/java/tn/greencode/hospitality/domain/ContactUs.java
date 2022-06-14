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
 * A ContactUs.
 */
@Entity
@Table(name = "contact_us")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactUs implements Serializable {

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

    @OneToMany(mappedBy = "contactUs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contactUs" }, allowSetters = true)
    private Set<ContactUsData> contactUsData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactUs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public ContactUs date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean getPublish() {
        return this.publish;
    }

    public ContactUs publish(Boolean publish) {
        this.setPublish(publish);
        return this;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public Position getContentPosition() {
        return this.contentPosition;
    }

    public ContactUs contentPosition(Position contentPosition) {
        this.setContentPosition(contentPosition);
        return this;
    }

    public void setContentPosition(Position contentPosition) {
        this.contentPosition = contentPosition;
    }

    public Position getImagePosition() {
        return this.imagePosition;
    }

    public ContactUs imagePosition(Position imagePosition) {
        this.setImagePosition(imagePosition);
        return this;
    }

    public void setImagePosition(Position imagePosition) {
        this.imagePosition = imagePosition;
    }

    public Set<ContactUsData> getContactUsData() {
        return this.contactUsData;
    }

    public void setContactUsData(Set<ContactUsData> contactUsData) {
        if (this.contactUsData != null) {
            this.contactUsData.forEach(i -> i.setContactUs(null));
        }
        if (contactUsData != null) {
            contactUsData.forEach(i -> i.setContactUs(this));
        }
        this.contactUsData = contactUsData;
    }

    public ContactUs contactUsData(Set<ContactUsData> contactUsData) {
        this.setContactUsData(contactUsData);
        return this;
    }

    public ContactUs addContactUsData(ContactUsData contactUsData) {
        this.contactUsData.add(contactUsData);
        contactUsData.setContactUs(this);
        return this;
    }

    public ContactUs removeContactUsData(ContactUsData contactUsData) {
        this.contactUsData.remove(contactUsData);
        contactUsData.setContactUs(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactUs)) {
            return false;
        }
        return id != null && id.equals(((ContactUs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactUs{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", publish='" + getPublish() + "'" +
            ", contentPosition='" + getContentPosition() + "'" +
            ", imagePosition='" + getImagePosition() + "'" +
            "}";
    }
}
