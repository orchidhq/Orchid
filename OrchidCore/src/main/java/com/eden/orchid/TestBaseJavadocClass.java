package com.eden.orchid;

import com.eden.orchid.api.generators.OrchidGenerator;

public class TestBaseJavadocClass<T extends OrchidGenerator> {

    public final String name;
    public final T obj;

    public TestBaseJavadocClass(String name, T obj) {
        this.name = name;
        this.obj = obj;
    }

    public static class InnerClass {

    }

    public static class InnerClass2<T extends InnerClass> {

    }

    public static class InnerClass4<T extends InnerClass2<InnerClass>> {

        public final T itemOne;
        public final InnerClass4<T> parent;

        public InnerClass4(T itemOne, InnerClass4<T> parent) {
            this.itemOne = itemOne;
            this.parent = parent;
        }

        public T getItemOne() {
            return itemOne;
        }

        public InnerClass4<T> getParent() {
            return parent;
        }

        public <U extends T> U getU() {
            return null;
        }

        public static <U extends InnerClass2> U getU(int a) {
            return null;
        }
    }

}
