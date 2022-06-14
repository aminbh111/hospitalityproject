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
 * A RoomService.
 */
@Entity
@Table(name = "room_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RoomService implements Serializable {

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

    @OneToMany(mappedBy = "roomService")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roomService" }, allowSetters = true)
    private Set<RoomServiceData> roomServiceData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RoomService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public RoomService date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean getPublish() {
        return this.publish;
    }

    public RoomService publish(Boolean publish) {
        this.setPublish(publish);
        return this;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public Position getContentPosition() {
        return this.contentPosition;
    }

    public RoomService contentPosition(Position contentPosition) {
        this.setContentPosition(contentPosition);
        return this;
    }

    public void setContentPosition(Position contentPosition) {
        this.contentPosition = contentPosition;
    }

    public Position getImagePosition() {
        return this.imagePosition;
    }

    public RoomService imagePosition(Position imagePosition) {
        this.setImagePosition(imagePosition);
        return this;
    }

    public void setImagePosition(Position imagePosition) {
        this.imagePosition = imagePosition;
    }

    public Set<RoomServiceData> getRoomServiceData() {
        return this.roomServiceData;
    }

    public void setRoomServiceData(Set<RoomServiceData> roomServiceData) {
        if (this.roomServiceData != null) {
            this.roomServiceData.forEach(i -> i.setRoomService(null));
        }
        if (roomServiceData != null) {
            roomServiceData.forEach(i -> i.setRoomService(this));
        }
        this.roomServiceData = roomServiceData;
    }

    public RoomService roomServiceData(Set<RoomServiceData> roomServiceData) {
        this.setRoomServiceData(roomServiceData);
        return this;
    }

    public RoomService addRoomServiceData(RoomServiceData roomServiceData) {
        this.roomServiceData.add(roomServiceData);
        roomServiceData.setRoomService(this);
        return this;
    }

    public RoomService removeRoomServiceData(RoomServiceData roomServiceData) {
        this.roomServiceData.remove(roomServiceData);
        roomServiceData.setRoomService(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomService)) {
            return false;
        }
        return id != null && id.equals(((RoomService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomService{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", publish='" + getPublish() + "'" +
            ", contentPosition='" + getContentPosition() + "'" +
            ", imagePosition='" + getImagePosition() + "'" +
            "}";
    }
}
