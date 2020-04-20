package com.xp;

public class A implements Callback{

    private Callback callBack;

    public A(Callback callBack){
        this.callBack = callBack;
    }

    public void Operation(){
        if(callBack != null){
            callBack.notice();
        }
    }

    @Override
    public void notice() {

    }
}
