package com.h6ah4i.logansquare_polymorphicparser.data;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

/**
 * Created by hasegawa on 3/20/15.
 */
public class AnimalCollectionTest {
    private static final double FLOAT_COMPARE_TOLERANCE = 0.00001;

    @BeforeClass
    public static void oneTimeSetUp() {
        LoganSquare.registerTypeConverter(AnimalBase.class, new AnimalBaseTypeConverter());
    }

    @Test
    public void parseCat() throws IOException {
        String json = "{ \"animals\": [ { \"name\" : \"cat\", \"coat_color\" : \"black\" } ] }";
        AnimalCollection parsed = LoganSquare.parse(json, AnimalCollection.class);

        assertThat(parsed.animals.size(), is(1));
        assertThat(parsed.animals.get(0), is(instanceOf(Cat.class)));

        Cat cat = (Cat) parsed.animals.get(0);
        assertThat(cat.name, is("cat"));
        assertThat(cat.coatColor, is("black"));
    }

    @Test
    public void parseDog() throws IOException {
        String json = "{ \"animals\": [ { \"name\" : \"dog\", \"breed\" : \"John\" } ] }";
        AnimalCollection parsed = LoganSquare.parse(json, AnimalCollection.class);

        assertThat(parsed.animals.size(), is(1));
        assertThat(parsed.animals.get(0), is(instanceOf(Dog.class)));

        Dog dog = (Dog) parsed.animals.get(0);
        assertThat(dog.name, is("dog"));
        assertThat(dog.breed, is("John"));
    }

    @Test
    public void parseCatAndDog() throws IOException {
        String json = "{ \"animals\": [ { \"name\" : \"cat\", \"coat_color\" : \"black\" }, { \"name\" : \"dog\", \"breed\" : \"John\" } ] }";
        AnimalCollection parsed = LoganSquare.parse(json, AnimalCollection.class);

        assertThat(parsed.animals.size(), is(2));
        assertThat(parsed.animals.get(0), is(instanceOf(Cat.class)));
        assertThat(parsed.animals.get(1), is(instanceOf(Dog.class)));

        Cat cat = (Cat) parsed.animals.get(0);
        assertThat(cat.name, is("cat"));
        assertThat(cat.coatColor, is("black"));

        Dog dog = (Dog) parsed.animals.get(1);
        assertThat(dog.name, is("dog"));
        assertThat(dog.breed, is("John"));
    }

    @Test
    public void parseTestAnimal() throws IOException {
        String json = JSON_TEST_ANIMAL_COLLECTION;

        AnimalCollection parsed = LoganSquare.parse(json, AnimalCollection.class);

        assertThat(parsed.animals.size(), is(1));
        assertThat(parsed.animals.get(0), is(instanceOf(TestAnimal.class)));

        TestAnimal animal = (TestAnimal) parsed.animals.get(0);
        assertThat(animal.name, is("test"));
        assertThat(animal.f_string, is("String Field"));
        assertThat(animal.f_int, is(123));
        assertThat(animal.f_long, is(456L));
        assertThat((double) animal.f_float, closeTo(123.456f, 0.00001f));
        assertThat(animal.f_double, closeTo(456.789, FLOAT_COMPARE_TOLERANCE));

        TestObject obj = animal.f_object;
        checkTestObject(obj);

        assertThat(animal.list_string.size(), is(3));
        assertThat(animal.list_string.get(0), is("A"));
        assertThat(animal.list_string.get(1), is("B"));
        assertThat(animal.list_string.get(2), is("C"));

        assertThat(animal.list_int.size(), is(3));
        assertThat(animal.list_int.get(0), is(1));
        assertThat(animal.list_int.get(1), is(-2));
        assertThat(animal.list_int.get(2), is(3));

        assertThat(animal.list_long.size(), is(3));
        assertThat(animal.list_long.get(0), is(-4L));
        assertThat(animal.list_long.get(1), is(5L));
        assertThat(animal.list_long.get(2), is(-6L));

        assertThat(animal.list_float.size(), is(3));
        assertThat((double) animal.list_float.get(0), closeTo(7.8f, FLOAT_COMPARE_TOLERANCE));
        assertThat((double) animal.list_float.get(1), closeTo(-9.10f, FLOAT_COMPARE_TOLERANCE));
        assertThat((double) animal.list_float.get(2), closeTo(11.12f, FLOAT_COMPARE_TOLERANCE));

        assertThat(animal.list_double.size(), is(3));
        assertThat(animal.list_double.get(0), closeTo(-13.14, FLOAT_COMPARE_TOLERANCE));
        assertThat(animal.list_double.get(1), closeTo(15.16, FLOAT_COMPARE_TOLERANCE));
        assertThat(animal.list_double.get(2), closeTo(-17.18, FLOAT_COMPARE_TOLERANCE));

        assertThat(animal.list_object.size(), is(3));
        checkTestObject(animal.list_object.get(0));
        checkTestObject(animal.list_object.get(1));
        checkTestObject(animal.list_object.get(2));
    }

    @Test
    public void parseAndSerializeTestObject() throws IOException, JSONException {
        String origJson = JSON_TEST_ANIMAL_COLLECTION;
        AnimalCollection parsed = LoganSquare.parse(origJson, AnimalCollection.class);
        String serializedJson = LoganSquare.serialize(parsed);

        JSONAssert.assertEquals(origJson, new JSONObject(serializedJson), true);
    }

    private static void checkTestObject(TestObject obj) {
        assertThat(obj.f_string, is("String Field"));
        assertThat(obj.f_int, is(123));
        assertThat(obj.f_long, is(456L));
        assertThat((double) obj.f_float, closeTo(123.456f, 0.00001f));
        assertThat(obj.f_double, closeTo(456.789, FLOAT_COMPARE_TOLERANCE));
    }

    static final String JSON_TEST_Object = "{" +
            "\"f_string\" : \"String Field\"" + ", " +
            "\"f_int\" : 123" + ", " +
            "\"f_long\" : 456" + ", " +
            "\"f_float\" : 123.456" + ", " +
            "\"f_double\" : 456.789" +
            "}";

    static final String JSON_TEST_ANIMAL = "{" +
            "\"name\" : \"test\"" + ", " +
            "\"f_string\" : \"String Field\"" + ", " +
            "\"f_int\" : 123" + ", " +
            "\"f_long\" : 456" + ", " +
            "\"f_float\" : 123.456" + ", " +
            "\"f_double\" : 456.789" + ", " +
            "\"f_object\" : " + JSON_TEST_Object + ", " +
            "\"list_string\" : [\"A\", \"B\", \"C\"]" + ", " +
            "\"list_int\" : [1, -2, 3]" + ", " +
            "\"list_long\" : [-4, 5, -6]" + ", " +
            "\"list_float\" : [7.8, -9.10, 11.12]" + ", " +
            "\"list_double\" : [-13.14, 15.16, -17.18]" + ", " +
            "\"list_object\" : [" + JSON_TEST_Object + ", " + JSON_TEST_Object + ", " + JSON_TEST_Object + "]" +
            "}";

    static final String JSON_TEST_ANIMAL_COLLECTION = "{ \"animals\": [ " + JSON_TEST_ANIMAL + " ] }";
}
