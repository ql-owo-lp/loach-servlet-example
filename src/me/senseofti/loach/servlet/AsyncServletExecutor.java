package me.senseofti.loach.servlet;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

public class AsyncServletExecutor implements Runnable {
	// define how much time we need to wait
	private static int[] waitTime = { 1, 5, 3, 2 };
	private static ExecutorService executor = Executors.newFixedThreadPool(500,
			new ThreadFactory() {
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setName("Service Thread " + t.getId());
					t.setDaemon(true);
					return t;
				}
			});

	private AsyncContext actx = null;
	int count = 0;

	public AsyncServletExecutor(AsyncContext actx) {
		this.actx = actx;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			ServletResponse res = actx.getResponse();

			final PrintWriter writer = res.getWriter();
			List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();

			tasks.add(new Callable<Boolean>() {
				public Boolean call() {
					try {
						Thread.sleep(waitTime[0] * 1000);
						pagelet(writer, 1, "Head", "#4a8af4");
					} catch (InterruptedException e) {
						return false;
					}
					return true;
				}
			});
			tasks.add(new Callable<Boolean>() {
				public Boolean call() {
					try {
						Thread.sleep(waitTime[1] * 1000);
						pagelet(writer, 2, "Content", "#3c8f01");
					} catch (InterruptedException e) {
						return false;
					}
					return true;
				}
			});
			tasks.add(new Callable<Boolean>() {
				public Boolean call() {
					try {
						Thread.sleep(waitTime[2] * 1000);
						pagelet(writer, 3, "Sidebar 1", "#e9503e");
					} catch (InterruptedException e) {
						return false;
					}
					return true;
				}
			});
			tasks.add(new Callable<Boolean>() {
				public Boolean call() {
					try {
						Thread.sleep(waitTime[3] * 1000);
						pagelet(writer, 4, "Sidebar 2", "#f5b400");
					} catch (InterruptedException e) {
						return false;
					}
					return true;
				}
			});
			try {
				executor.invokeAll(tasks, 150000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException ignored) {
				// ignored
			}
			actx.complete();
		} catch (Exception e) {
		}
	}

	private void pagelet(PrintWriter writer, int id, String content,
			String color) {
		if (writer.checkError())
			return;
		writer.write("<script>contentArrived(\"content" + id + "\", \""
				+ content + " is Loaded!\",\"" + color + "\");</script>\n");
		writer.flush();
	}

}
