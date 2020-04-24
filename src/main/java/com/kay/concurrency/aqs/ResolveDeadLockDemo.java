package com.kay.concurrency.aqs;

public class ResolveDeadLockDemo {

    private class Account{
        private long id; //unique
        private int balance;

        public void transfer(Account target, int amount) {
            synchronized (this) {
                synchronized (target) {
                    // transfer...
                }
            }
        }

        /**
         * synchronized by order
         * @param target
         * @param amount
         */
        public void transferByOrder(Account target, int amount) {
            Account from;
            Account to;
            if (this.id < target.id) {
                from = this;
                to = target;
            }else {
                from = target;
                to = this;
            }

            synchronized (from) {
                synchronized (to) {
                    // transfer...
                }
            }

        }

    }


}
