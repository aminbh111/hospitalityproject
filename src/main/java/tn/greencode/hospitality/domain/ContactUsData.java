package tn.greencode.hospitality.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.greencode.hospitality.domain.enumeration.Language;

/**
 * A ContactUsData.
 */
@Entity
@Table(name = "contact_us_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactUsData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    private Language lang;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contactUsData" }, allowSetters = true)
    private ContactUs contactUs;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactUsData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLang() {
        return this.lang;
    }

    public ContactUsData lang(Language lang) {
        this.setLang(lang);
        return this;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return this.title;
    }

    public ContactUsData title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public ContactUsData content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return this.image;
    }

    public ContactUsData image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public ContactUsData imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public ContactUs getContactUs() {
        return this.contactUs;
    }

    public void setContactUs(ContactUs contactUs) {
        this.contactUs = contactUs;
    }

    public ContactUsData contactUs(ContactUs contactUs) {
        this.setContactUs(contactUs);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactUsData)) {
            return false;
        }
        return id != null && id.equals(((ContactUsData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactUsData{" +
            "id=" + getId() +
            ", lang='" + getLang() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
