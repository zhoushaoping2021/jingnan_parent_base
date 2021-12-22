package com.jn;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sublun
 * @Date: 2021/2/10 10:33
 */
public class Hello {
    @Test
    public void test() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        for (int i = 0; i < list.size(); i++) {
            Integer element = list.get(i);
            if (element.intValue() % 2 == 0) {
                list.remove(element);
            }
        }
        list.forEach(System.out::println);
    }

    @Test
    public void test2() {
        System.out.println(1<<8);
    }



}
