package io.github.leases.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import io.github.leases.domain.enumeration.LeasesFileMediumTypes;

import io.github.leases.domain.enumeration.LeasesFileModelType;

/**
 * A LeasesFileType.
 */
@Entity
@Table(name = "leases_file_type")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leasesfiletype")
public class LeasesFileType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "leases_file_type_name", nullable = false, unique = true)
    private String leasesFileTypeName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "leases_file_medium_type", nullable = false)
    private LeasesFileMediumTypes leasesFileMediumType;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "file_template")
    private byte[] fileTemplate;

    @Column(name = "file_template_content_type")
    private String fileTemplateContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "leasesfile_type")
    private LeasesFileModelType leasesfileType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeasesFileTypeName() {
        return leasesFileTypeName;
    }

    public LeasesFileType leasesFileTypeName(String leasesFileTypeName) {
        this.leasesFileTypeName = leasesFileTypeName;
        return this;
    }

    public void setLeasesFileTypeName(String leasesFileTypeName) {
        this.leasesFileTypeName = leasesFileTypeName;
    }

    public LeasesFileMediumTypes getLeasesFileMediumType() {
        return leasesFileMediumType;
    }

    public LeasesFileType leasesFileMediumType(LeasesFileMediumTypes leasesFileMediumType) {
        this.leasesFileMediumType = leasesFileMediumType;
        return this;
    }

    public void setLeasesFileMediumType(LeasesFileMediumTypes leasesFileMediumType) {
        this.leasesFileMediumType = leasesFileMediumType;
    }

    public String getDescription() {
        return description;
    }

    public LeasesFileType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFileTemplate() {
        return fileTemplate;
    }

    public LeasesFileType fileTemplate(byte[] fileTemplate) {
        this.fileTemplate = fileTemplate;
        return this;
    }

    public void setFileTemplate(byte[] fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public String getFileTemplateContentType() {
        return fileTemplateContentType;
    }

    public LeasesFileType fileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
        return this;
    }

    public void setFileTemplateContentType(String fileTemplateContentType) {
        this.fileTemplateContentType = fileTemplateContentType;
    }

    public LeasesFileModelType getLeasesfileType() {
        return leasesfileType;
    }

    public LeasesFileType leasesfileType(LeasesFileModelType leasesfileType) {
        this.leasesfileType = leasesfileType;
        return this;
    }

    public void setLeasesfileType(LeasesFileModelType leasesfileType) {
        this.leasesfileType = leasesfileType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeasesFileType)) {
            return false;
        }
        return id != null && id.equals(((LeasesFileType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeasesFileType{" +
            "id=" + getId() +
            ", leasesFileTypeName='" + getLeasesFileTypeName() + "'" +
            ", leasesFileMediumType='" + getLeasesFileMediumType() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileTemplate='" + getFileTemplate() + "'" +
            ", fileTemplateContentType='" + getFileTemplateContentType() + "'" +
            ", leasesfileType='" + getLeasesfileType() + "'" +
            "}";
    }
}
