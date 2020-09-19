package com.kay.io;

import java.io.File;

public interface Resource extends InputStreamResource {

		boolean exist();

		File getFile();

		String getPath();
}
