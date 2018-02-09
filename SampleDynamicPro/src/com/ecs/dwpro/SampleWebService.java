package com.ecs.dwpro;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(asyncSupported = true, urlPatterns = { "/SampleWebService" })
public class SampleWebService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SampleWebService() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final AsyncContext acontext = req.startAsync();
		acontext.start(new Runnable() {
			@Override
			public void run() {
				try {
					handleMultipleRequest (acontext.getRequest(),
							 acontext.getResponse());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					acontext.complete();
				}
			}

			protected void handleMultipleRequest(ServletRequest request, ServletResponse response) throws IOException {
				HttpSession session = ((HttpServletRequest) request).getSession();
				String data = getBody(request);
				System.out.println("Request data -> " + data);
				
				
				((HttpServletResponse) response).setHeader("Content-Type", "text/xml; charset=UTF-8");
				response.getWriter().write(data);
				return;
			}

			private String getBody(ServletRequest request) throws IOException {
				String body = null;
				StringBuilder sb = new StringBuilder();

				try {
					String s;
					while ((s = request.getReader().readLine()) != null) {
						sb.append(s);
					}
				} catch (IOException ex) {
					throw ex;
				}

				body = sb.toString();
				return body;
			}

		});

	}

}
