package com.kay.concurrency.lockissues;

import com.kay.concurrency.annotations.ThreadSafe;

public class AcquireLockByOrderDemo {

		private class Account {

				private final long id; //unique and immutable
				private int balance;

				public Account(long id) {
						this.id = id;
				}

				public void transfer(Account target, int amount) {
						synchronized (this) {
								synchronized (target) {
										// transfer...
								}
						}
				}

				/**
				 * synchronized by order
				 */
				@ThreadSafe
				public void transferByOrder(Account target, int amount) {
						Account from;
						Account to;
						if (this.id < target.id) {
								from = this;
								to = target;
						} else {
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
