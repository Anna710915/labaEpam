package com.epam.esm.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * The type Certificate duration dto.
 */
public class CertificateDurationDto {

    @Min(value = 1, message = "{min_certificate_duration}")
    @Max(value = 365, message = "{max_certificate_duration}")
    private int duration;

    /**
     * Instantiates a new Certificate duration dto.
     */
    public CertificateDurationDto(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificateDurationDto that = (CertificateDurationDto) o;

        return duration == that.duration;
    }

    @Override
    public int hashCode() {
        return duration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CertificateDurationDto{");
        sb.append("duration=").append(duration);
        sb.append('}');
        return sb.toString();
    }
}
