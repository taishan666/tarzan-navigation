package com.tarzan.nav.utils;


import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;

/**
 * @author tarzan
 */
@Slf4j
public class IoUtil extends StreamUtils {
    public IoUtil() {
    }

    public static void closeQuietly(@Nullable Closeable closeable) {
        if (closeable != null) {
            if (closeable instanceof Flushable) {
                try {
                    ((Flushable)closeable).flush();
                } catch (IOException ignored) {
                    log.error(ignored.getMessage());
                }
            }
            try {
                closeable.close();
            } catch (IOException var2) {
                log.error(var2.getMessage());
            }

        }
    }

    public static String readToString(InputStream input) {
        return readToString(input, StandardCharsets.UTF_8);
    }

    public static String readToString(@Nullable InputStream input, Charset charset) {
        String var2 = null;
        try {
            var2 = copyToString(input, charset);
        } catch (IOException var6) {
            log.error(var6.getMessage());
        } finally {
            closeQuietly(input);
        }
        return var2;
    }

    public static byte[] readToByteArray(@Nullable InputStream input) {
        byte[] var1 = new byte[0];
        try {
            var1 = copyToByteArray(input);
        } catch (IOException var5) {
            log.error(var5.getMessage());
        } finally {
            closeQuietly(input);
        }
        return var1;
    }

    public static void write(@Nullable final String data, final OutputStream output) throws IOException {
        write(data,output,StandardCharsets.UTF_8);
    }
    public static void write(@Nullable final String data, final OutputStream output, final Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(encoding));
        }
    }
}
