package com.raket.silverstripe.file;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.raket.silverstripe.file.SilverStripeFileType;
import org.jetbrains.annotations.NotNull;

public class SilverStripeFileTypeFactory extends FileTypeFactory {

    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(SilverStripeFileType.INSTANCE, SilverStripeFileType.DEFAULT_EXTENSION);
    }
}
