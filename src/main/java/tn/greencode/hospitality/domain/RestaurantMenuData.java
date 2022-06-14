package tn.greencode.hospitality.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.greencode.hospitality.domain.enumeration.Language;

/**
 * A RestaurantMenuData.
 */
@Entity
@Table(name = "restaurant_menu_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RestaurantMenuData implements Serializable {

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
    @JsonIgnoreProperties(value = { "restaurantMenuData" }, allowSetters = true)
    private RestaurantMenu restaurantMenu;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RestaurantMenuData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLang() {
        return this.lang;
    }

    public RestaurantMenuData lang(Language lang) {
        this.setLang(lang);
        return this;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return this.title;
    }

    public RestaurantMenuData title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public RestaurantMenuData content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return this.image;
    }

    public RestaurantMenuData image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public RestaurantMenuData imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public RestaurantMenu getRestaurantMenu() {
        return this.restaurantMenu;
    }

    public void setRestaurantMenu(RestaurantMenu restaurantMenu) {
        this.restaurantMenu = restaurantMenu;
    }

    public RestaurantMenuData restaurantMenu(RestaurantMenu restaurantMenu) {
        this.setRestaurantMenu(restaurantMenu);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantMenuData)) {
            return false;
        }
        return id != null && id.equals(((RestaurantMenuData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantMenuData{" +
            "id=" + getId() +
            ", lang='" + getLang() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
