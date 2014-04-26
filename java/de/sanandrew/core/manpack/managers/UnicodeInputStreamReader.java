package de.sanandrew.core.manpack.managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
	
/** Original class made by LexManos and cpw. An InputStreamReader which supports UTF encoded files which have a BOM **/
public class UnicodeInputStreamReader extends Reader
{
    private final InputStreamReader input;

    public UnicodeInputStreamReader(InputStream source, String encoding) throws IOException {
        String enc = encoding;
        byte[] data = new byte[4];

        PushbackInputStream pbStream = new PushbackInputStream(source, data.length);
        int read = pbStream.read(data, 0, data.length);
        int size = 0;

        int bom16 = (data[0] & 0xFF) << 8 | (data[1] & 0xFF);
        int bom24 = bom16 << 8 | (data[2] & 0xFF);
        int bom32 = bom24 << 8 | (data[3] & 0xFF);

        if( bom24 == 0xEFBBBF ) {
            enc = "UTF-8";
            size = 3;
        } else if( bom16 == 0xFEFF ) {
            enc = "UTF-16BE";
            size = 2;
        } else if( bom16 == 0xFFFE ) {
            enc = "UTF-16LE";
            size = 2;
        } else if( bom32 == 0x0000FEFF ) {
            enc = "UTF-32BE";
            size = 4;
        } else if( bom32 == 0xFFFE0000 ) {
            enc = "UTF-32LE";
            size = 4;
        }

        if( size < read ) {
            pbStream.unread(data, size, read - size);
        }

        this.input = new InputStreamReader(pbStream, enc);
    }

    public String getEncoding() {
        return input.getEncoding();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return input.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        input.close();
    }
}