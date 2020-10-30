package io.github.leases.internal.fileProcessing;

import io.github.leases.internal.model.FileNotification;

public interface FileUploadProcessor<T> {

    T processFileUpload(T fileUpload, FileNotification fileNotification);
}
