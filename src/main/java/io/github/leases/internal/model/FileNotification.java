package io.github.leases.internal.model;

import io.github.leases.domain.enumeration.LeasesFileModelType;

/**
 * This is a notification which contains metadata of a recently uploaded which a listener might act upon
 */
public class FileNotification implements TokenizableMessage<String> {
    private static final long serialVersionUID = -6472961232578342431L;

    private String fileId;

    private long timestamp;

    private String filename;

    private String messageToken;

    private String description;

    private LeasesFileModelType leasesfileModelType;

    public FileNotification(String fileId, long timestamp, String filename, String messageToken, String description, LeasesFileModelType leasesfileModelType) {
            this.fileId = fileId;
            this.timestamp = timestamp;
            this.filename = filename;
            this.messageToken = messageToken;
            this.description = description;
            this.leasesfileModelType = leasesfileModelType;
        }

        public FileNotification() {
        }

        public static FileNotificationBuilder builder() {
            return new FileNotificationBuilder();
        }

        public String getFileId() {
            return this.fileId;
        }

        public long getTimestamp() {
            return this.timestamp;
        }

        public String getFilename() {
            return this.filename;
        }

        public String getMessageToken() {
            return this.messageToken;
        }

        public String getDescription() {
            return this.description;
        }

        public LeasesFileModelType getleasesfileModelType() {
            return this.leasesfileModelType;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public void setMessageToken(String messageToken) {
            this.messageToken = messageToken;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setleasesfileModelType(LeasesFileModelType leasesfileModelType) {
            this.leasesfileModelType = leasesfileModelType;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof FileNotification)) return false;
            final FileNotification other = (FileNotification) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$fileId = this.getFileId();
            final Object other$fileId = other.getFileId();
            if (this$fileId == null ? other$fileId != null : !this$fileId.equals(other$fileId)) return false;
            if (this.getTimestamp() != other.getTimestamp()) return false;
            final Object this$filename = this.getFilename();
            final Object other$filename = other.getFilename();
            if (this$filename == null ? other$filename != null : !this$filename.equals(other$filename)) return false;
            final Object this$messageToken = this.getMessageToken();
            final Object other$messageToken = other.getMessageToken();
            if (this$messageToken == null ? other$messageToken != null : !this$messageToken.equals(other$messageToken))
                return false;
            final Object this$description = this.getDescription();
            final Object other$description = other.getDescription();
            if (this$description == null ? other$description != null : !this$description.equals(other$description))
                return false;
            final Object this$leasesfileModelType = this.getleasesfileModelType();
            final Object other$leasesfileModelType = other.getleasesfileModelType();
            if (this$leasesfileModelType == null ? other$leasesfileModelType != null : !this$leasesfileModelType.equals(other$leasesfileModelType))
                return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof FileNotification;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $fileId = this.getFileId();
            result = result * PRIME + ($fileId == null ? 43 : $fileId.hashCode());
            final long $timestamp = this.getTimestamp();
            result = result * PRIME + (int) ($timestamp >>> 32 ^ $timestamp);
            final Object $filename = this.getFilename();
            result = result * PRIME + ($filename == null ? 43 : $filename.hashCode());
            final Object $messageToken = this.getMessageToken();
            result = result * PRIME + ($messageToken == null ? 43 : $messageToken.hashCode());
            final Object $description = this.getDescription();
            result = result * PRIME + ($description == null ? 43 : $description.hashCode());
            final Object $leasesfileModelType = this.getleasesfileModelType();
            result = result * PRIME + ($leasesfileModelType == null ? 43 : $leasesfileModelType.hashCode());
            return result;
        }

        public String toString() {
            return "FileNotification(fileId=" + this.getFileId() + ", timestamp=" + this.getTimestamp() + ", filename=" + this.getFilename() + ", messageToken=" + this.getMessageToken() + ", description=" + this.getDescription() + ", leasesfileModelType=" + this.getleasesfileModelType() + ")";
        }

        public static class FileNotificationBuilder {
            private String fileId;
            private long timestamp;
            private String filename;
            private String messageToken;
            private String description;
            private LeasesFileModelType leasesfileModelType;

            FileNotificationBuilder() {
            }

            public FileNotification.FileNotificationBuilder fileId(String fileId) {
                this.fileId = fileId;
                return this;
            }

            public FileNotification.FileNotificationBuilder timestamp(long timestamp) {
                this.timestamp = timestamp;
                return this;
            }

            public FileNotification.FileNotificationBuilder filename(String filename) {
                this.filename = filename;
                return this;
            }

            public FileNotification.FileNotificationBuilder messageToken(String messageToken) {
                this.messageToken = messageToken;
                return this;
            }

            public FileNotification.FileNotificationBuilder description(String description) {
                this.description = description;
                return this;
            }

            public FileNotification.FileNotificationBuilder leasesfileModelType(LeasesFileModelType leasesfileModelType) {
                this.leasesfileModelType = leasesfileModelType;
                return this;
            }

            public FileNotification build() {
                return new FileNotification(fileId, timestamp, filename, messageToken, description, leasesfileModelType);
            }

            public String toString() {
                return "FileNotification.FileNotificationBuilder(fileId=" + this.fileId + ", timestamp=" + this.timestamp + ", filename=" + this.filename + ", messageToken=" + this.messageToken + ", description=" + this.description + ", leasesfileModelType=" + this.leasesfileModelType + ")";
            }
        }
}
