package com.spring.carebookie;

import java.time.LocalDate;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        String fn = "Oanh";
        String ln = "Pham Van";
        String bd = "poanh1002@gmail.com";

        String userId = fn.toCharArray()[0] + String.valueOf(ln.toCharArray()[0]) + bd.split("@")[0];
        System.out.println(userId);

        String name = "Bệnh Viên Mắt Vỹ Dạ";
        String[] arr = name.split(" ");
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (String a : arr) {
            builder.append(a.toCharArray()[0]);
        }
        System.out.println(builder.append(random.nextInt(10)).append(random.nextInt(10))
                .append(random.nextInt(10)).append(random.nextInt(10)));

        System.out.println("".length());

        Double star = 4.4d;
        System.out.println(star % star.intValue() >= 0.5 ? (double) star.intValue() + 1 : (double)star.intValue());

        System.out.println(LocalDate.of(2023,5,14).getDayOfWeek().getValue());

        for (int i =1; i< 100 ;i++) {
            String id = String.format("%04d", random.nextInt(10000));
            System.out.println(id);
        }
    }
}
