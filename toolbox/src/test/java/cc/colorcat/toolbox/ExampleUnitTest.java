package cc.colorcat.toolbox;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void genericTest() {
        List<Integer> ints = new FixedList<>(10);
        for (int i = 0; i < 20; i++) {
            ints.add(i);
            System.out.println("ints'size = " + ints.size());
        }
        System.out.println(ints.toString());
        System.out.println(JsonUtils.toJson(ints));
        Collections.reverse(ints);
        System.out.println(ints);
        System.out.println(JsonUtils.toJson(ints));
    }

    @Test
    public void testJson() throws Exception {
        User u1 = new User();
        u1.setId(100234);
        u1.setName("AB 李白");
        u1.setAddress("宇宙中国");
        u1.setBirthday(new Date());

        User u2 = new User();
        u2.setId(1001);
        u2.setName("john");
        u2.setTags(Arrays.asList("A1", "B1", "B2"));

        Foo foo = new Foo();
        foo.id = 9012;
        foo.users = Arrays.asList(u1, u2);

        String jsonFoo = JsonUtils.toJson(foo);
        System.out.println(jsonFoo);
        Foo f = JsonUtils.fromJson(jsonFoo, Foo.class);
        System.out.println(f);

//        String json = JsonUtils.toJson(u1);
//        System.out.println(json);
//        System.out.println("----------------------------------------------");
//        User user = JsonUtils.fromJson(json, User.class);
//        System.out.println(user);
    }

    public static class Foo {
        private int id;
        private List<User> users;
        private List<String> tags;
        private int[] ints;

        @Override
        public String toString() {
            return "Foo{" +
                    "id=" + id +
                    ", users=" + users +
                    ", tags=" + tags +
                    ", ints=" + Arrays.toString(ints) +
                    '}';
        }
    }

    public static class User {
        private int id;
        private String name;
        private String address;
        private Date birthday;
        private List<String> tags;

        public User() {
        }

        public User(int id, String name, String address, Date birthday) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.birthday = birthday;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", birthday=" + birthday +
                    ", tags=" + tags +
                    '}';
        }
    }
}