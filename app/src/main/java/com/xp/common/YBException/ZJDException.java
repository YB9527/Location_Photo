package com.xp.common.YBException;

public class ZJDException extends Exception {

    private static final long serialVersionUID = 1L;

    // 提供无参数的构造方法
    public ZJDException() {
    }

    // 提供一个有参数的构造方法，可自动生成
    public ZJDException(String msg) {
        super(msg);// 把参数传递给Throwable的带String参数的构造方法
    }

}