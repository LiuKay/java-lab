package com.kay.concurrency;

import com.kay.concurrency.future.example.Client;
import com.kay.concurrency.future.example.Data;
import org.junit.Assert;
import org.junit.Test;

public class MyDemoFutureTests {

		@Test
		public void testFutureDemo() throws InterruptedException {
				Client client = new Client();
				Data data = client.handle("request");

				Thread.sleep(1000);

				Assert.assertEquals("request", data.getResult());
		}
}
