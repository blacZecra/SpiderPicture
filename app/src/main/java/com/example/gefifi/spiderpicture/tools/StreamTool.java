package com.example.gefifi.spiderpicture.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by gefifi on 2017/11/28.
 */

public class StreamTool {
    public static byte[] read(InputStream inputStream) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buf)) != -1){
            baos.write(buf, 0, len);
        }
        baos.close();
        return buf;
    }
}
