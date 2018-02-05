package com.eden.orchid.api.server;

import com.eden.orchid.api.server.files.FileController;
import com.google.inject.ImplementedBy;

@ImplementedBy(FileController.class)
public interface OrchidFileController  {

    OrchidResponse findFile(String targetPath);

}
