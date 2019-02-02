package com.memory.memory;

/**
 * Created by Maciej Szalek on 2019-02-01.
 */

public class Events {

    public static class EventProduct{
        private String product;
        public EventProduct(String product) {
            this.product = product;
        }
        public String getProduct() {
            return product;
        }
    }

    public static class EventToDo{
        private String toDo;
        public EventToDo(String toDo) {
            this.toDo = toDo;
        }

        public String getToDo() {
            return toDo;
        }
    }
}
