package com.theone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void test1() {
        Person person = new Person();
        person.setName("小王");
        person.setAge(11);
        System.out.println("person = " + person);
        //Java对象转化成Json格式的字符串
        String jsonString = JSONObject.toJSONString(person);
        System.out.println("jsonString = " + jsonString);
        // Json格式字符串转换为Json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        System.out.println("jsonObject = " + jsonObject);
        // Json对象转换为Java对象
        Person javaObject = JSONObject.toJavaObject(jsonObject, Person.class);
        System.out.println("javaObject = " + javaObject);

        JSONObject json = new JSONObject();
        json.put("code", 200);
        System.out.println("json = " + json);
        List<Integer> list = Arrays.asList(1, 2, 3);
        json.put("list", list);
        System.out.println("json = " + json);
        System.out.println("json.getString(\"code\") = " + json.getString("code"));
        JSON.parseArray(json.getJSONArray("list").toJSONString(), Integer.class);

        List<String> uuidList = new ArrayList<>();
        uuidList.add("61e46d258f4f238e244e200a233f9f16");
        uuidList.add("61e46d258f4f238e244e200a233f9f16");
        System.out.println("uuidList.toString() = " + uuidList.toString());
        System.out.println("uuidList = " + uuidList);
    }
}
