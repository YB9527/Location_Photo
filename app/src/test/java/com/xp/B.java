package com.xp;

import org.junit.Test;

public class B {
    @Test
    public  void fun() {
       Callback callback = new Callback() {
           @Override
           public void notice() {
                System.out.println("dkjdkjfkdjfkdjkfj");
           }
       };
        callback.notice();
    }

}