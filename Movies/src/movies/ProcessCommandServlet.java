package movies;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessCommandServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
        Key movieKey = KeyFactory.createKey("Movies", "Purdue");
        
        
      /*you don't need to worry about the variable below, this gets the value of the 
       * string entered in the text area as defined in the movies.jsp file
       */
        String content = req.getParameter("command");
        
        
        /*This string array contains the individual elements of the 
        command entered in the text area, e.g. if commandEls[0] gives "add_actor", 
        commandEls[1] gives the actor name, commandEls[2] gives the gender
        and commandEls[3] gives the date of birth*/ 
        String [] commandEls = content.split(":");
        
        /*This string contains the results to display to the user once a command is entered.
         * For a query, it should list the results of the query. 
         * For an insertion or deletion, it should either contain an error message or 
         * the message "Command executed successfully!"*/
        String results = null;
        
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        /*your implementation starts here*/
        if ( commandEls[0].equals( "add_actor" ) ) {
        	String name = commandEls[1];
        	String gender = commandEls[2];
        	String date_of_birth = commandEls[3];
        	
        	boolean duplicate = false;
        	
        	Entity newActor = new Entity("actor");
        	
        	newActor.setProperty("name",name);
        	newActor.setProperty("gender",gender);
        	newActor.setProperty("date_of_birth",date_of_birth);
        	Filter equalFilter = new FilterPredicate("name",Query.FilterOperator.EQUAL,name);
        	
        	Query q1 = new Query("actor").setFilter(equalFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	for(Entity e: pq.asIterable()){ duplicate=true; }
        	if (!duplicate) {
        		try{
            		datastore.put(newActor);
            		results = "Command executed successfully";
            
            	}catch (Exception e){
            		results = "Error!";
            	}
        	}
        	else {
        		results = "Actor already exists!";
        	}
        }
        else if ( commandEls[0].equals( "add_director" ) ) {
        	String directorname = commandEls[1];
        	String gender = commandEls[2];
        	String date_of_birth = commandEls[3];
        	
        	boolean duplicate = false;
        	
        	Entity newDirector = new Entity("director");
        	
        	newDirector.setProperty("directorname",directorname);
        	newDirector.setProperty("gender",gender);
        	newDirector.setProperty("date_of_birth",date_of_birth);
        	
        	Filter directorFilter = new FilterPredicate("directorname",Query.FilterOperator.EQUAL,directorname);
        	
        	Query q1 = new Query("director").setFilter(directorFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	for(Entity e: pq.asIterable()){ duplicate=true; }
        	if (!duplicate) {
        		try{
            		datastore.put(newDirector);
            		results = "Command executed successfully";
            
            	}catch (Exception e){results = "Error!";}
        	}
        	else {
        		results = "Director already exists!";
        	}
        }
        else if ( commandEls[0].equals( "add_company" ) ) {
        	String companyname = commandEls[1];
        	String address = commandEls[2];
        	
        	boolean duplicate = false;
        	
        	Entity newCompany = new Entity("company");
        	
        	newCompany.setProperty("companyname",companyname);
        	newCompany.setProperty("address",address);
        	
        	Filter companynameFilter = new FilterPredicate("companyname",Query.FilterOperator.EQUAL,companyname);
        	
        	Query q1 = new Query("company").setFilter(companynameFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	for(Entity e: pq.asIterable()){ duplicate=true; }
        	if (!duplicate) {
        		try{
            		datastore.put(newCompany);
            		results = "Command executed successfully";
            
            	}catch (Exception e){
            		results = "Error!";
            	}
        	}
        	else {
        		results = "Company already exists!";
        	}
        }
        else if ( commandEls[0].equals( "add_movie" ) ) {
        	String title = commandEls[1];
        	String release_year = commandEls[2];
        	String length = commandEls[3];
        	String genre = commandEls[4];
        	String plot = commandEls[5];
        	String director = commandEls[6];
        	String company = commandEls[7];
        	
        	
        	boolean duplicate = false, companyCheck=false, directorCheck=false;
        	Entity newMovie = new Entity("movie");
        	
        	newMovie.setProperty("title",title);
        	newMovie.setProperty("release_year",release_year);
        	newMovie.setProperty("length",length);
        	newMovie.setProperty("genre",genre);
        	newMovie.setProperty("plot",plot);
        	newMovie.setProperty("director",director);
        	newMovie.setProperty("company",company);
        	
        	
        	Filter titleFilter = new FilterPredicate("title",Query.FilterOperator.EQUAL,title);
        	Filter releaseyearFilter = new FilterPredicate("release_year",Query.FilterOperator.EQUAL,release_year);
        	Filter combinedFilter = CompositeFilterOperator.and(titleFilter, releaseyearFilter);
        	Filter companynameFilter = new FilterPredicate("companyname",Query.FilterOperator.EQUAL,company);
        	Filter directorFilter = new FilterPredicate("directorname",Query.FilterOperator.EQUAL,director);    	
        	
        	Query q1 = new Query("movie").setFilter(combinedFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	for(Entity e: pq.asIterable()){ duplicate=true; }
        
        	Query q2 = new Query("company").setFilter(companynameFilter);
        	PreparedQuery p2 = datastore.prepare(q2);
        	for(Entity e: p2.asIterable()){ companyCheck=true; }
        	
        	Query q3 = new Query("director").setFilter(directorFilter);
        	PreparedQuery p3 = datastore.prepare(q3);
        	for(Entity e: p3.asIterable()){ directorCheck=true; }
        	
        	if (!duplicate) {
        		if(companyCheck&&directorCheck){
	        		try{
	            		datastore.put(newMovie);
	            		results = "Command executed successfully";
	            
	            	}catch (Exception e){
	            		results = "Error!";
	            	}
        		}else{
            		results ="Foreign key constraints violated!";
            	}
        	}
        	else {
        		results = "movie already exists!";
        	}
        }
        else if ( commandEls[0].equals( "add_awards_event" ) ) {
        	String event_name = commandEls[1];
        	String year = commandEls[2];
        	String venue = commandEls[3];
        	
        	boolean duplicate = false;
        	
        	Entity newAwards_event = new Entity("awards_event");
        	
        	newAwards_event.setProperty("event_name",event_name);
        	newAwards_event.setProperty("year",year);
        	newAwards_event.setProperty("venue",venue);
        	Filter event_nameFilter = new FilterPredicate("event_name",Query.FilterOperator.EQUAL,event_name);
        	Filter yearFilter = new FilterPredicate("year",Query.FilterOperator.EQUAL,year);
        	Filter combinedFilter = CompositeFilterOperator.and(event_nameFilter, yearFilter);

        	
        	Query q1 = new Query("movie").setFilter(combinedFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	for(Entity e: pq.asIterable()){ duplicate=true; }
        	if (!duplicate) {

				try{
	        		datastore.put(newAwards_event);
	        		results = "Command executed successfully";          
	        	}catch (Exception e){results = "Error!";}            	       		 	
        	}
        	else {results = "Awards event already exists!";}       	
        }
        
        else if ( commandEls[0].equals( "add_user" ) ) {
        	String user_id = commandEls[1];
        	
        	boolean duplicate = false;
        	
        	Entity newUser_id = new Entity("user");
        	
        	newUser_id.setProperty("user_id",user_id);
        	Filter user_idFilter = new FilterPredicate("user_id",Query.FilterOperator.EQUAL,user_id);
    
        	Query q1 = new Query("user").setFilter(user_idFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	for(Entity e: pq.asIterable()){ duplicate=true; }
        	if (!duplicate) {
        		try{
            		datastore.put(newUser_id);
            		results = "Command executed successfully";
            	}catch (Exception e){results = "Error!";}
        	}
        	else {results = "user already exists!";}
        }
        
        else if ( commandEls[0].equals( "add_movie_rating" ) ) {
        	String user_id = commandEls[1];
        	String title = commandEls[2];
        	String release_year = commandEls[3];
        	String rating = commandEls[4];
        	
        	boolean duplicate = false,movieCheck = false, userCheck =false;
        	
        	Entity newMovierating = new Entity("movie_rating");
        	
        	newMovierating.setProperty("user_id",user_id);
        	newMovierating.setProperty("title",title);
        	newMovierating.setProperty("release_year",release_year);
        	newMovierating.setProperty("rating",rating);

        	Filter user_idFilter = new FilterPredicate("user_id",Query.FilterOperator.EQUAL,user_id);
        	Filter movie_titleFilter = new FilterPredicate("title",Query.FilterOperator.EQUAL,title);
        	Filter release_yearFilter = new FilterPredicate("release_year",Query.FilterOperator.EQUAL,release_year);
        	Filter combinedFilter = CompositeFilterOperator.and(user_idFilter, release_yearFilter, movie_titleFilter);
        	Filter co_combinedFilter = CompositeFilterOperator.and( movie_titleFilter,release_yearFilter);
        	
        	Query q1 = new Query("movie_rating").setFilter(combinedFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	Query q3 = new Query("movie").setFilter(co_combinedFilter);
         	Query q2 = new Query("user").setFilter(user_idFilter);
         	PreparedQuery p2 = datastore.prepare(q2);
         	PreparedQuery p3 = datastore.prepare(q3);
         	for(Entity g: p3.asIterable()){ movieCheck=true; } 
         	for(Entity f: p2.asIterable()){ userCheck=true; }
        	for(Entity e: pq.asIterable()){ duplicate=true; }
        	if (!duplicate) {
            	if(userCheck&&movieCheck){
		    		try{
		        		datastore.put(newMovierating);
		        		results = "Command executed successfully";
		        
		        	}catch (Exception e){
		        		results = "Error!";
		        	}
            	}else{
            		results ="Foreign key constraints violated!";
            	}
        	}
        	else {
        		results = "movie rating already exists!";
        	}
        }
        
        else if ( commandEls[0].equals( "add_cast" ) ) {
        	String title = commandEls[1];
        	String release_year = commandEls[2];
        	String actorname = commandEls[3];
        	String role = commandEls[4];
        	
        	boolean duplicate = false,movieCheck = false, actorCheck =false;
        	
        	Entity newCast = new Entity("cast");
        	
        	newCast.setProperty("title",title);
        	newCast.setProperty("release_year",release_year);
        	newCast.setProperty("actorname",actorname);
        	newCast.setProperty("role",role);

        	Filter actornameFilter = new FilterPredicate("actorname",Query.FilterOperator.EQUAL,actorname);
        	Filter actorname2Filter = new FilterPredicate("name",Query.FilterOperator.EQUAL,actorname);

        	Filter movie_titleFilter = new FilterPredicate("title",Query.FilterOperator.EQUAL,title);
        	Filter release_yearFilter = new FilterPredicate("release_year",Query.FilterOperator.EQUAL,release_year);
        	Filter combinedFilter = CompositeFilterOperator.and(release_yearFilter, movie_titleFilter,actornameFilter);
        	Filter co_combinedFilter = CompositeFilterOperator.and( movie_titleFilter,release_yearFilter);
   
        	Query q1 = new Query("cast").setFilter(combinedFilter);        	
        	Query q3 = new Query("movie").setFilter(co_combinedFilter);
         	Query q2 = new Query("actor").setFilter(actorname2Filter);
        	PreparedQuery pq = datastore.prepare(q1);
         	PreparedQuery p2 = datastore.prepare(q2);
         	PreparedQuery p3 = datastore.prepare(q3);
        	for(Entity e: pq.asIterable()){ duplicate=true; }
         	for(Entity f: p2.asIterable()){ actorCheck=true; }
         	for(Entity g: p3.asIterable()){ movieCheck=true; } 

        	if (!duplicate) {
            	if(actorCheck&&movieCheck){
		    		try{
		        		datastore.put(newCast);
		        		results = "Command executed successfully";
		        
		        	}catch (Exception e){
		        		results = "Error!";
		        	}
            	}else{
            		results ="Foreign key constraints violated!";
            	}
        	}
        	else {
        		results = "cast already exists!";
        	}
        }
        
        else if ( commandEls[0].equals( "add_nomination_category" ) ) {
        	String category_name = commandEls[1];
        	
        	boolean duplicate = false;
        	
        	Entity newnominationCategory = new Entity("nomination_category");
        	
        	newnominationCategory.setProperty("nomination_category",category_name);
        	Filter nominationCategoryFilter = new FilterPredicate("nomination_category",Query.FilterOperator.EQUAL,category_name);
    
        	Query q1 = new Query("nomination_category").setFilter(nominationCategoryFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	for(Entity e: pq.asIterable()){ duplicate=true; }
        	if (!duplicate) {
        		try{
            		datastore.put(newnominationCategory);
            		results = "Command executed successfully";
            	}catch (Exception e){results = "Error!";}
        	}
        	else {results = "nomination category already exists!";}
        }
        
        else if ( commandEls[0].equals( "add_nomination" ) ) {
        	String title = commandEls[1];
        	String release_year = commandEls[2];
        	String event = commandEls[3];
        	String event_year = commandEls[4];
        	String category = commandEls[5];
        	String won = commandEls[6];

        	
        	boolean duplicate = false,movieCheck = false, eventCheck =false, categoryCheck = false;
        	
        	Entity newCast = new Entity("nomination");
        	
        	newCast.setProperty("movie_title",title);
        	newCast.setProperty("movie_year",release_year);
        	newCast.setProperty("event",event);
        	newCast.setProperty("event_year",event_year);
        	newCast.setProperty("nomination_category",category);
        	newCast.setProperty("won",won);
        	
        	//setting up filters for duplicate
        	Filter eventFilter = new FilterPredicate("event",Query.FilterOperator.EQUAL,event);
        	Filter eventYearFilter = new FilterPredicate("event_year",Query.FilterOperator.EQUAL,event_year);
        	Filter categoryFilter = new FilterPredicate("nomination_category",Query.FilterOperator.EQUAL,category);
        	Filter movie_titleFilter = new FilterPredicate("movie_title",Query.FilterOperator.EQUAL,title);
        	Filter release_yearFilter = new FilterPredicate("movie_year",Query.FilterOperator.EQUAL,release_year);
        	Filter combinedFilter = CompositeFilterOperator.and(release_yearFilter, movie_titleFilter,eventFilter,eventYearFilter,categoryFilter);
   
        	//setting up filter for foreign key constraints violation 
        	
        	Filter movie_titleFilter2 = new FilterPredicate("title",Query.FilterOperator.EQUAL,title);
        	Filter release_yearFilter2 = new FilterPredicate("release_year",Query.FilterOperator.EQUAL,release_year);
        	Filter combinedFilter2 = CompositeFilterOperator.and(release_yearFilter2, movie_titleFilter2);

        	Filter eventFilter2 = new FilterPredicate("event_name",Query.FilterOperator.EQUAL,event);
        	Filter eventYearFilter2 = new FilterPredicate("year",Query.FilterOperator.EQUAL,event_year);
        	Filter combinedFilter3 = CompositeFilterOperator.and(eventYearFilter2, eventFilter2);

        	
        	
        	
        	
        	Query q1 = new Query("nomination").setFilter(combinedFilter);        	
        	Query q2 = new Query("movie").setFilter(combinedFilter2);
         	Query q3 = new Query("awards_event").setFilter(combinedFilter3);
         	Query q4 = new Query("nomination_category").setFilter(categoryFilter);

        	PreparedQuery pq = datastore.prepare(q1);
         	PreparedQuery p2 = datastore.prepare(q2);
         	PreparedQuery p3 = datastore.prepare(q3);
         	PreparedQuery p4 = datastore.prepare(q4);

        	for(Entity e: pq.asIterable()){ duplicate=true; }
         	for(Entity f: p2.asIterable()){ movieCheck=true; }
         	for(Entity g: p3.asIterable()){ eventCheck=true; }	 
         	for(Entity h: p4.asIterable()){ categoryCheck=true; } 


        	if (!duplicate) {
            	if(categoryCheck&&eventCheck&&movieCheck){
		    		try{
		        		datastore.put(newCast);
		        		results = "Command executed successfully";
		        
		        	}catch (Exception e){
		        		results = "Error!";
		        	}
            	}else{
            		results ="Foreign key constraints violated!";
            	}
        	}
        	else {
        		results = "nomination already exists!";
        	}
        }
        
        else if(commandEls[0].equals("get_movie_by_company")){
        	
        	results ="";
        	boolean found = false;
        	String company_name = commandEls[1];
        	Filter companyFilter = new FilterPredicate("company",Query.FilterOperator.EQUAL,company_name);
        	
        	Query q1 = new Query("movie").setFilter(companyFilter);
        	
        	PreparedQuery pq = datastore.prepare(q1);
        	
        	for(Entity e: pq.asIterable()){
        		results += (String)e.getProperty("title");
        		results += ", ";
        		results += (String)e.getProperty("release_year");
        		results += "; ";
        		found = true;
        	}
        	if(found){
        		results= results.substring(0, results.length() - 2);
        	}
        }
        
        else if(commandEls[0].equals("get_movies_by_director")){
        	
        	results ="";
        	boolean found = false;
        	String director_name = commandEls[1];
        	Filter companyFilter = new FilterPredicate("director",Query.FilterOperator.EQUAL,director_name);
        	
        	Query q1 = new Query("movie").setFilter(companyFilter);
        	
        	PreparedQuery pq = datastore.prepare(q1);
        	
        	for(Entity e: pq.asIterable()){
        		results += (String)e.getProperty("title");
        		results += ", ";
        		results += (String)e.getProperty("release_year");
        		results += "; ";
        		found=true;
        	}
        	if(found){
        		results= results.substring(0, results.length() - 2);
        	}
        }
        
        else if(commandEls[0].equals("get_nominations_for_actor")){
        	
        	results ="";
        	String temp="",role="",category_name="";
        	boolean found = false;
        	String actor_name = commandEls[1];
        	Filter actorFilter = new FilterPredicate("actorname",Query.FilterOperator.EQUAL,actor_name);
        	Query q1 = new Query("cast").setFilter(actorFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	
        	for(Entity e: pq.asIterable()){
        		temp = (String)e.getProperty("title");	
        		role = (String)e.getProperty("role");
        		role = "best "+role;
        		Filter nominationFilter = new FilterPredicate("movie_title",Query.FilterOperator.EQUAL,temp);
            	Query q2 = new Query("nomination").setFilter(nominationFilter);
            	PreparedQuery p2 = datastore.prepare(q2);

            	for(Entity f:p2.asIterable()){
        			category_name=(String)f.getProperty("nomination_category");
            		if(temp.equalsIgnoreCase((String)f.getProperty("movie_title"))&&category_name.equalsIgnoreCase(role)){
            			System.out.println("fsdfsdf");
	            		results += (String)f.getProperty("event");
	            		results += ", ";
	            		results += (String)f.getProperty("event_year");
	            		results += ", ";
        				results += category_name;
                		results += ", ";
	            		results += (String)f.getProperty("won");
	            		results += "; ";
	            		found=true;
            		}
            	}
        	}

        	if(found){
        		results= results.substring(0, results.length() - 2);
        	}
        }
        else if(commandEls[0].equals("get_movies_of_genre_for_actor")){
        	
        	results ="";
        	String temp="";
        	boolean found = false;
        	String actor_name = commandEls[1];
        	String genre = commandEls[2];
        	Filter actorFilter = new FilterPredicate("actorname",Query.FilterOperator.EQUAL,actor_name);
        	Filter genreFilter = new FilterPredicate("genre",Query.FilterOperator.EQUAL,genre);

        	Query q1 = new Query("cast").setFilter(actorFilter);
        	Query q2 = new Query("movie").setFilter(genreFilter);
        	
        	PreparedQuery pq = datastore.prepare(q1);
        	PreparedQuery p1 = datastore.prepare(q2);

        	
        	for(Entity e: pq.asIterable()){
        		temp=(String)e.getProperty("title");
        		for(Entity f: p1.asIterable()){
        			if(genre.equalsIgnoreCase((String) f.getProperty("genre"))&&temp.equalsIgnoreCase((String)f.getProperty("title"))){
        				results+=(String)f.getProperty("title");
        				results+=", ";
        				results+=(String)f.getProperty("release_year");
        				results+="; ";
        				found=true;
        			}
        		}
        	}
        	if(found){
        		results= results.substring(0, results.length() - 2);
        	}
        }
        else if(commandEls[0].equals("get_number_of_nominations_for_movie")){
        	
        	results ="";
        	int count=0;
        	String title = commandEls[1];
        	String release_year = commandEls[2];
        	Filter titleFilter = new FilterPredicate("movie_title",Query.FilterOperator.EQUAL,title);
        	Filter releaseyearFilter = new FilterPredicate("movie_year",Query.FilterOperator.EQUAL,release_year);
        	Filter combinedFilter = CompositeFilterOperator.and(titleFilter, releaseyearFilter);

        	Query q1 = new Query("nomination").setFilter(combinedFilter);
        	
        	PreparedQuery pq = datastore.prepare(q1);

        	for(Entity e: pq.asIterable()){
        		count++;
        	}
        	results=Integer.toString(count);
        }
        
        else if(commandEls[0].equals("get_average_rating_for_movie")){
        	
        	results ="";
        	double count=0,sum=0;
        	String title = commandEls[1];
        	String release_year = commandEls[2];
        	Filter titleFilter = new FilterPredicate("title",Query.FilterOperator.EQUAL,title);
        	Filter releaseyearFilter = new FilterPredicate("release_year",Query.FilterOperator.EQUAL,release_year);
        	Filter combinedFilter = CompositeFilterOperator.and(titleFilter, releaseyearFilter);

        	Query q1 = new Query("movie_rating").setFilter(combinedFilter);
        	
        	PreparedQuery pq = datastore.prepare(q1);

        	for(Entity e: pq.asIterable()){
        		sum+=Double.parseDouble((String)e.getProperty("rating"));
        		count++;
        	}
        	if(count!=0){results=Double.toString(sum/count);}
        }
        else if(commandEls[0].equals("get_average_rating_of_user")){
        	results ="";
        	double count=0,sum=0;
        	String user_id = commandEls[1];
        	Filter userFilter = new FilterPredicate("user_id",Query.FilterOperator.EQUAL,user_id);
        	Query q1 = new Query("movie_rating").setFilter(userFilter);
        	PreparedQuery pq = datastore.prepare(q1);
        	for(Entity e: pq.asIterable()){
        		sum+=Double.parseDouble((String)e.getProperty("rating"));
        		count++;
        	}
        	if(count!=0){results=Double.toString(sum/count);}

        }
        else if(commandEls[0].equals("delete_company")){
        	results ="";
        	Key k = null;
        	boolean company_exist=false;
        	String companyname = commandEls[1];
        	Filter userFilter = new FilterPredicate("companyname",Query.FilterOperator.EQUAL,companyname);        	
        	Filter movieFilter = new FilterPredicate("company",Query.FilterOperator.EQUAL,companyname);

        	Query q1 = new Query("company").setFilter(userFilter);
        	Query q2 = new Query("movie").setFilter(movieFilter);

        	PreparedQuery pq = datastore.prepare(q1);
        	PreparedQuery p2 = datastore.prepare(q2);

        	for(Entity e: pq.asIterable()){
        		if(companyname.equals((String)e.getProperty("companyname"))){
        			k = e.getKey();
        			for(Entity f: p2.asIterable()){
        				if(companyname.equals((String)f.getProperty("company"))){
        					company_exist=true;
        				}
        			}
        		}
        		
        	}
        	if(!company_exist){ 
        		datastore.delete(k); 
        		results="Command executed successfully";
        	}else{
        		results="Referential integrity violation!";
        	}
        }
        else if(commandEls[0].equals("delete_user")){
        	results ="";
        	Key k = null;
        	boolean user_exist=false;
        	String user_id = commandEls[1];
        	Filter userFilter = new FilterPredicate("user_id",Query.FilterOperator.EQUAL,user_id);        	

        	Query q1 = new Query("user").setFilter(userFilter);
        	Query q2 = new Query("movie_rating").setFilter(userFilter);

        	PreparedQuery pq = datastore.prepare(q1);
        	PreparedQuery p2 = datastore.prepare(q2);

        	for(Entity e: pq.asIterable()){
        		if(user_id.equals((String)e.getProperty("user_id"))){
        			k = e.getKey();
        			for(Entity f: p2.asIterable()){
        				if(user_id.equals((String)f.getProperty("user_id"))){
        					user_exist=true;
        				}
        			}
        		}		
        	}
        	if(!user_exist){ 
        		datastore.delete(k); 
        		results="Command executed successfully";
        	}else{
        		results="Referential integrity violation!";
        	}
        }
      
 
        
        
        resp.sendRedirect( "/movies.jsp?moviedbName=Purdue&display=" + results );
    }  

}
