package com.hkurokawa.moshiplayground;

import okio.Buffer;
import okio.BufferedSource;
import org.junit.Test;
import com.squareup.moshi.Moshi;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by hiroshi on 5/10/15.
 */
public class MoshiPlayground {
    @Test
    public void testPOJO() throws Exception {
        final String json = "{\"name\": \"Jake Wharton\", \"age\": 31}";
        final Moshi moshi = new Moshi.Builder().build();
        final User user = moshi.adapter(User.class).fromJson(json);
        assertThat(user.getName()).isEqualTo("Jake Wharton");
        assertThat(user.getAge()).isEqualTo(31);
        assertThat(moshi.adapter(User.class).toJson(user)).isEqualTo("{\"age\":31,\"name\":\"Jake Wharton\"}");
    }

    @Test
    public void testAnnotatedValue() throws Exception {
        final Message msg = new Message();
        msg.setMsg("Test message");
        final Calendar cal = Calendar.getInstance();
        cal.set(2015, Calendar.MAY, 9, 23, 52, 0);
        cal.set(Calendar.MILLISECOND, 0);
        msg.setCreatedAt(cal.getTime());

        final Moshi moshi = new Moshi.Builder().add(Date.class, ISO8601.class, new ISO8601DateAdapter()).build();
        final String json = moshi.adapter(Message.class).toJson(msg);
        assertThat(json).isEqualTo("{\"createdAt\":\"2015-05-09T23:52:00.000+09:00\",\"msg\":\"Test message\"}");
        assertThat(moshi.adapter(Message.class).fromJson(json)).isEqualTo(msg);
    }

    private BufferedSource newBuffer(String input) {
        Buffer buffer = new Buffer();
        buffer.writeUtf8(input);
        return buffer;
    }

    public static class User {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static class Message {
        String msg;
        @ISO8601
        Date createdAt;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Message message = (Message) o;

            if (msg != null ? !msg.equals(message.msg) : message.msg != null) return false;
            return !(createdAt != null ? !createdAt.equals(message.createdAt) : message.createdAt != null);

        }

        @Override
        public int hashCode() {
            int result = msg != null ? msg.hashCode() : 0;
            result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
            return result;
        }
    }
}
