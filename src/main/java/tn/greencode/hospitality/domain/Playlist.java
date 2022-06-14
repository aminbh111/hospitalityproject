package tn.greencode.hospitality.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import tn.greencode.hospitality.domain.enumeration.Language;

/**
 * A Playlist.
 */
@Entity
@Table(name = "playlist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    private Language lang;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "file")
    private String file;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Playlist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Playlist date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Language getLang() {
        return this.lang;
    }

    public Playlist lang(Language lang) {
        this.setLang(lang);
        return this;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public String getFile() {
        return this.file;
    }

    public Playlist file(String file) {
        this.setFile(file);
        return this;
    }

    public void setFile(String file) {
        this.file = file;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Playlist)) {
            return false;
        }
        return id != null && id.equals(((Playlist) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Playlist{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", lang='" + getLang() + "'" +
            ", file='" + getFile() + "'" +
            "}";
    }
}
