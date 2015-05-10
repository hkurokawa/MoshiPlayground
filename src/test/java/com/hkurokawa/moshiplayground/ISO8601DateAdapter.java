package com.hkurokawa.moshiplayground;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hiroshi on 5/11/15.
 */
public class ISO8601DateAdapter extends JsonAdapter<Date> {
    private static final DateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Override
    public Date fromJson(JsonReader reader) throws IOException {
        final String str = reader.nextString();
        try {
            return ISO_8601_FORMAT.parse(str);
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void toJson(JsonWriter writer, Date value) throws IOException {
        final String str = ISO_8601_FORMAT.format(value);
        writer.value(str);
    }
}
