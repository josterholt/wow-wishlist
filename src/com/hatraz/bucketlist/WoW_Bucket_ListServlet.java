package com.hatraz.bucketlist;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class WoW_Bucket_ListServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		System.out.println("GAE Test");
	}
}
