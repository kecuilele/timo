package com.linln;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BootApplicationTests {

    @Test
    public void contextLoads() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("w");
        String w = df.format(date);
        System.out.println(w);

    }

}
