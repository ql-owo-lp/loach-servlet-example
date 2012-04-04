package me.senseofti.loach.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The default servlet (A-sync) that process everything. By default the servlet
 * will process the path of url pattern "/". Override this configuration in
 * web.xml
 * 
 * @author Kevin
 * @since Apr 3, 2012
 * 
 */

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/async/servlet3" }, asyncSupported = true, name = "LoachServlet3", displayName = "LoachServlet3")
public class AsyncServlet extends HttpServlet {

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		final PrintWriter writer = res.getWriter();
		long servletStartTime = new Date().getTime();
		String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" > <head> <meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" /><script type=\"text/javascript\">"
				+ "var loadTime=new Date().getTime();"
				+ "function getLoadTime() {var val= new Date().getTime(), timeLapse = val-loadTime;loadTime=val;return timeLapse;}"
				+ "function contentArrived($id, $content,$backgroundColor) {document.getElementById($id).innerHTML = $content;document.getElementById($id).style.background = $backgroundColor;recordTime($id);}"
				+ "function recordTime($id){document.getElementById('log').innerHTML += 'content['+$id+'] takes ' +getLoadTime() +  ' ms to load;<br/>';}"
				+ "</script><title>Async sample</title><style>body { font-family:Verdana; font-size:14px; margin:0;}#container { margin:0 auto; width:500px; }#content1 { height:80px; background:rgb(192, 192, 192); margin-bottom:5px;}#mainContent { height:400px; margin-bottom:5px;}#sidebar { float:right; width:200px; }#content3 { height:280px; background:rgb(192, 192, 192); margin-bottom:5px; }#content2 { margin-right:205px !important; margin-right:202px; height:400px; background:rgb(192, 192, 192);}#content4 { height:115px; background:rgb(192, 192, 192);}</style></head><body><div id=\"container\" style=\"color:white\"> <div id=\"content1\"></div> <div id=\"mainContent\"> <div id=\"sidebar\"><div id=\"content3\"></div><div id=\"content4\"></div></div> <div id=\"content2\"></div> </div><div id=\"log\" style=\"color:black\"></div></div></body></html>";

		writer.write(html);
		writer.flush();
		AsyncContext actx = req.startAsync();
		actx.addListener(new AsyncServletListener(req,res));
		actx.setTimeout(30 * 1000);
		actx.start(new AsyncServletExecutor(actx));
		log(writer, new Date().getTime() - servletStartTime);
	}

	private void log(PrintWriter w, long t) {
		w.write("<script>document.getElementById('log').innerHTML+='Servlet load takes "
				+ t + " ms.<br/>';</script>");
		w.flush();
	}
}
