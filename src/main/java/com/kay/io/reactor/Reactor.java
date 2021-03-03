package com.kay.io.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Single-threaded Reactor Model http://gee.cs.oswego.edu/dl/cpjslides/nio.pdf
 * <p>
 * Reactor:responds to IO events by dispatching the appropriate handler Handlers: perform
 * non-blocking actions
 */
public class Reactor implements Runnable {

		final Selector selector;
		final ServerSocketChannel serverSocket;

		Reactor(int portNumber) throws IOException {
				selector = Selector.open();
				serverSocket = ServerSocketChannel.open();
				serverSocket.socket().bind(new InetSocketAddress(portNumber));
				SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
				sk.attach(new Acceptor());
		}

		/**
		 * Dispatch Loop
		 */
		@Override
		public void run() {
				try {
						while (!Thread.interrupted()) {
								selector.select();
								Set<SelectionKey> keys = selector.selectedKeys();
								Iterator<SelectionKey> iterator = keys.iterator();
								while (iterator.hasNext()) {
										dispatch(iterator.next());
								}
								keys.clear();
						}
				} catch (IOException e) {
						e.printStackTrace();
				}
		}

		private void dispatch(SelectionKey key) {
				Runnable r = (Runnable) key.attachment();
				if (r != null) {
						r.run();
				}
		}


		class Acceptor implements Runnable {

				@Override
				public void run() {
						try {
								SocketChannel socketChannel = serverSocket.accept();
								if (socketChannel != null) {
										new Handler(selector, socketChannel);
								}
						} catch (IOException e) {
								e.printStackTrace();
						}
				}
		}

}
