// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;


import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson; 
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/comment")
public class DataServlet extends HttpServlet {
 
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
     String maxCommentsString = request.getParameter("maxComments");

    // Convert the input to an int.
    int maxComments = Integer.parseInt(maxCommentsString);;
    
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> comments = new ArrayList<>();
    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(maxComments))) {
      
      String text = (String) entity.getProperty("text");
       String email = (String) entity.getProperty("email");
       String message = email + " : " + text;      
      comments.add(message);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }



    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
     UserService userService = UserServiceFactory.getUserService();
      
      if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/comment");
      return;
    }


    // Get the input from the form.
    String newComment = request.getParameter("my-comment");
    String email = userService.getCurrentUser().getEmail();
    long timestamp = System.currentTimeMillis();
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", newComment);
    commentEntity.setProperty("timestamp", timestamp);
    commentEntity.setProperty("email", email);
    

    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    response.sendRedirect("/index.html");
  }
}

  


