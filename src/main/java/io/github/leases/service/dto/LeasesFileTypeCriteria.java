package io.github.leases.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.leases.domain.enumeration.LeasesFileMediumTypes;
import io.github.leases.domain.enumeration.LeasesFileModelType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.leases.domain.LeasesFileType} entity. This class is used
 * in {@link io.github.leases.web.rest.LeasesFileTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leases-file-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeasesFileTypeCriteria implements Serializable, Criteria {
    /**
     * Class for filtering LeasesFileMediumTypes
     */
    public static class LeasesFileMediumTypesFilter extends Filter<LeasesFileMediumTypes> {

        public LeasesFileMediumTypesFilter() {
        }

        public LeasesFileMediumTypesFilter(LeasesFileMediumTypesFilter filter) {
            super(filter);
        }

        @Override
        public LeasesFileMediumTypesFilter copy() {
            return new LeasesFileMediumTypesFilter(this);
        }

    }
    /**
     * Class for filtering LeasesFileModelType
     */
    public static class LeasesFileModelTypeFilter extends Filter<LeasesFileModelType> {

        public LeasesFileModelTypeFilter() {
        }

        public LeasesFileModelTypeFilter(LeasesFileModelTypeFilter filter) {
            super(filter);
        }

        @Override
        public LeasesFileModelTypeFilter copy() {
            return new LeasesFileModelTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter leasesFileTypeName;

    private LeasesFileMediumTypesFilter leasesFileMediumType;

    private StringFilter description;

    private LeasesFileModelTypeFilter leasesfileType;

    public LeasesFileTypeCriteria() {
    }

    public LeasesFileTypeCriteria(LeasesFileTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.leasesFileTypeName = other.leasesFileTypeName == null ? null : other.leasesFileTypeName.copy();
        this.leasesFileMediumType = other.leasesFileMediumType == null ? null : other.leasesFileMediumType.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.leasesfileType = other.leasesfileType == null ? null : other.leasesfileType.copy();
    }

    @Override
    public LeasesFileTypeCriteria copy() {
        return new LeasesFileTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLeasesFileTypeName() {
        return leasesFileTypeName;
    }

    public void setLeasesFileTypeName(StringFilter leasesFileTypeName) {
        this.leasesFileTypeName = leasesFileTypeName;
    }

    public LeasesFileMediumTypesFilter getLeasesFileMediumType() {
        return leasesFileMediumType;
    }

    public void setLeasesFileMediumType(LeasesFileMediumTypesFilter leasesFileMediumType) {
        this.leasesFileMediumType = leasesFileMediumType;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LeasesFileModelTypeFilter getLeasesfileType() {
        return leasesfileType;
    }

    public void setLeasesfileType(LeasesFileModelTypeFilter leasesfileType) {
        this.leasesfileType = leasesfileType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeasesFileTypeCriteria that = (LeasesFileTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(leasesFileTypeName, that.leasesFileTypeName) &&
            Objects.equals(leasesFileMediumType, that.leasesFileMediumType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(leasesfileType, that.leasesfileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        leasesFileTypeName,
        leasesFileMediumType,
        description,
        leasesfileType
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeasesFileTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (leasesFileTypeName != null ? "leasesFileTypeName=" + leasesFileTypeName + ", " : "") +
                (leasesFileMediumType != null ? "leasesFileMediumType=" + leasesFileMediumType + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (leasesfileType != null ? "leasesfileType=" + leasesfileType + ", " : "") +
            "}";
    }

}
