package com.example.core.file.ops.impls;

import com.example.core.file.dto.DeleteFile;
import com.example.core.file.ops.Deleter;
import org.springframework.stereotype.Component;

@Component
public class LocalStorageDeleter extends Deleter {
    @Override
    public void delete(DeleteFile deleteFile) {
    }
}
