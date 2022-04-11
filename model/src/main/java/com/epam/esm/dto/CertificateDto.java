package com.epam.esm.dto;

import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class CertificateDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;
    private Set<Tag> tagSet;

    public CertificateDto(){}

    public CertificateDto(long id, String name, BigDecimal price,
                          Set<Tag> tagSet) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.tagSet = tagSet;
    }

    public CertificateDto(long id, String name, String description,
                          BigDecimal price, int duration, LocalDateTime createDate,
                          LocalDateTime lastUpdateDate, Set<Tag> tagSet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tagSet = tagSet;
    }
    public CertificateDto(String name, String description,
                          BigDecimal price, int duration, LocalDateTime createDate,
                          LocalDateTime lastUpdateDate, Set<Tag> tagSet) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tagSet = tagSet;
    }

    public CertificateDto(String name, String description, BigDecimal price, int duration,
                          Set<Tag> tagSet) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tagSet = tagSet;
    }

    public CertificateDto(long id, String name, String description, BigDecimal price, int duration, Set<Tag> tagSet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tagSet = tagSet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Set<Tag> getTagSet() {
        return Set.copyOf(tagSet);
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
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
        return tagSet != null ? tagSet.equals(that.tagSet) : that.tagSet == null;
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
        result = 31 * result + (tagSet != null ? tagSet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CertificateDto{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", duration=").append(duration);
        sb.append(", createDate=").append(createDate);
        sb.append(", lastUpdateDate=").append(lastUpdateDate);
        sb.append(", tagSet=").append(tagSet);
        sb.append('}');
        return sb.toString();
    }
}