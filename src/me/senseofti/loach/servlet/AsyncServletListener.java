package me.senseofti.loach.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AsyncServletListener implements AsyncListener {
	PrintWriter out;
	public AsyncServletListener(HttpServletRequest req,HttpServletResponse res) throws IOException{out = res.getWriter();}
	public void onComplete(AsyncEvent event) throws IOException {
		log("The Async Thread is completed.");
		out.write("</body></html>");
	}

	public void onError(AsyncEvent event) throws IOException {
	}

	public void onStartAsync(AsyncEvent event) throws IOException {
		log("The Async Thread is activated.");
	}

	public void onTimeout(AsyncEvent event) throws IOException {
	}
	
	private void log(String t) {
		out.write("<script>document.getElementById('log').innerHTML+='"
				+ t + "<br/>';</script>");
		out.flush();
	}
}