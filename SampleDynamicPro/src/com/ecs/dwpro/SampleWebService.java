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

import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

@WebServlet(asyncSupported = true, urlPatterns = { "/SampleWebService" })
public class SampleWebService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private MongodbUtils utils = MongodbUtils.getInstance();
	/*
	 * private MongoClient client; private MongoDatabase database;
	 */

	public SampleWebService() {
		super();
		// client = utils.getMongoClientInstance();
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
					handleMultipleRequest(acontext.getRequest(), acontext.getResponse());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					acontext.complete();
				}
			}

			protected void handleMultipleRequest(ServletRequest request, ServletResponse response) throws IOException {
				((HttpServletRequest) request).getSession();
				String data = getBody(request);
				System.out.println("Request data -> " + data);
				MongoClient mongo = new MongoClient("localhost", 27017);
				MongoDatabase database = mongo.getDatabase("local_db");

				MongoCollection<Document> collection = database.getCollection("employee_details");

				MongoCursor<Document> iterator = collection.find().iterator();

				BasicDBList list = new BasicDBList();
				while (iterator.hasNext()) {
					Document doc = iterator.next();
					list.add(doc);
				}

				((HttpServletResponse) response).setHeader("Content-Type", "text/xml; charset=UTF-8");
				response.getWriter().write(JSON.serialize(list));
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
