package com.epam.esm.model.dto;

import com.epam.esm.model.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Certificate dto class is used for transferring data between layers.
 *
 * @author Anna Merkul
 */
public class CertificateDto extends RepresentationModel<CertificateDto> {

    private long id;

    @Pattern(regexp = "[\\w\\p{Blank}А-Яа-я]+", message = "{invalid_certificate_name}")
    @Size(min = 2, max = 100, message = "{size_certificate_name}")
    private String name;

    @Pattern(regexp = "[^<>]+", message = "{invalid_description}")
    @Size(max = 120, message = "{size_certificate_description}")
    private String description;

    @Min(value = 5, message = "{min_certificate_price}")
    @Max(value = 100000, message = "{max_certificate_price}")
    private BigDecimal price;

    @Min(value = 1, message = "{min_certificate_duration}")
    @Max(value = 365, message = "{max_certificate_duration}")
    private int duration;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;
    private List<Tag> tags;

    /**
     * Instantiates a new Certificate dto.
     */
    public CertificateDto(){}

    /**
     * Instantiates a new Certificate dto.
     *
     * @param tags the tags
     */
    public CertificateDto(List<Tag> tags){
        this.tags = tags;
    }

    /**
     * Instantiates a new Certificate dto.
     *
     * @param id    the id
     * @param name  the name
     * @param price the price
     * @param tags  the tag list
     */
    public CertificateDto(long id, String name, BigDecimal price,
                          List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.tags = tags;
    }

    /**
     * Instantiates a new Certificate dto.
     *
     * @param id             the id
     * @param name           the name
     * @param description    the description
     * @param price          the price
     * @param duration       the duration
     * @param createDate     the create date
     * @param lastUpdateDate the last update date
     * @param tags           the tag list
     */
    public CertificateDto(long id, String name, String description,
                          BigDecimal price, int duration, LocalDateTime createDate,
                          LocalDateTime lastUpdateDate, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    /**
     * Instantiates a new Certificate dto.
     *
     * @param name           the name
     * @param description    the description
     * @param price          the price
     * @param duration       the duration
     * @param createDate     the create date
     * @param lastUpdateDate the last update date
     * @param tags           the tag list
     */
    public CertificateDto(String name, String description,
                          BigDecimal price, int duration, LocalDateTime createDate,
                          LocalDateTime lastUpdateDate, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    /**
     * Instantiates a new Certificate dto.
     *
     * @param name        the name
     * @param description the description
     * @param price       the price
     * @param duration    the duration
     * @param tags        the tag list
     */
    public CertificateDto(String name, String description, BigDecimal price, int duration,
                          List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tags = tags;
    }

    /**
     * Instantiates a new Certificate dto.
     *
     * @param id          the id
     * @param name        the name
     * @param description the description
     * @param price       the price
     * @param duration    the duration
     * @param tags        the tag list
     */
    public CertificateDto(long id, String name, String description, BigDecimal price,
                          int duration, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tags = tags;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets last update date.
     *
     * @return the last update date
     */
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets last update date.
     *
     * @param lastUpdateDate the last update date
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Gets tag list.
     *
     * @return the tag set
     */
    public List<Tag> getTags() {
        return List.copyOf(tags);
    }

    /**
     * Sets tag set.
     *
     * @param tags the tag set
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificateDto that = (CertificateDto) o;

        if (id != that.id) return false;
        if (duration != that.duration) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (lastUpdateDate != null ? !lastUpdateDate.equals(that.lastUpdateDate) : that.lastUpdateDate != null)
            return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CertificateDto{" + "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tagSet=" + tags +
                '}';
    }
}
