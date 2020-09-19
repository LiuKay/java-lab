package com.kay.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamResource {

		/**
		 * Always returns a fresh InputStream for multiple usage.
		 *
		 * @return inputStream
		 */
		InputStream getInputStream() throws IOException;
}
