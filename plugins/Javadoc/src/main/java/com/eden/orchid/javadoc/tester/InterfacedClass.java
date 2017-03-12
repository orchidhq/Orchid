package com.eden.orchid.javadoc.tester;

public class InterfacedClass implements Runnable, CharSequence {
    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    @Override
    public void run() {

    }
}
