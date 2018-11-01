package com.vplus;

import com.vplus.controller.IMasterController;
import com.vplus.dao.CourseDAO;
import com.vplus.models.CourseModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;



@SpringBootApplication
public class Application implements CommandLineRunner {

	// TODO: Figure out how to autowire...
	@Autowired
	private IMasterController masterController;
	private ClassPathXmlApplicationContext ctx;
	
	public static final String JSON_PATH = "./data/user_input.json"; 
	
	/**
	 * sets the application context using xml file
	 */
	public void setContext() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Application app  = ctx.getBean("Application", Application.class);
		ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true); 
	}

	//recommend courses based on taken course
	public List<CourseModel> recommendCourses(List<String> takenCourses ) {
		
		List<CourseModel> recommended = new ArrayList<>();
		recommended = masterController.recommendCourses(takenCourses);
		return recommended;
//		System.out.println("output = " + courseService.selectCoursesByNumAndSection("WCOMS4771", 1));
//		System.out.println("DONE");
//		System.out.println("output = " + trackService.selectCoursesByTrackId("Computational Biology").getCourseID());
//		System.out.println("DONE");
		
//		int exitCode = SpringApplication.exit(ctx, () -> 0);
//		System.exit(exitCode);
	}
	
	/**
	 * Run the application
	 */
	@Override
	public void run(String... args) throws Exception {
		setContext();
		//Welcome message
		System.out.println("Hello, welcome to Vergilplus!");
		System.out.println("Type \"exit\" to exit the program.\n");
		System.out.println("Type \"Hi\" to get started.\n");
		//create the buffer read to handle the user input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
		//build the response tree(it is a binary tree, and left tree is the response for 'yes', 
		//and the right tree is response for 'no')
		List<String> responses = new ArrayList<String>();
		//level 0
        responses.add("Are you currently a Columbia University Student?(Y/N)");
		//level 1        
        responses.add("Have you taken any courses already?(Y/N)");
        responses.add("Do you want to enter key words to see some related courses?(Y/N)");
		//level 2
        responses.add("Please input the taken courses list, seperated by comma\',\'");
        responses.add("Do you want Vergilplus recommend some courses for you?(Y/N)");
        responses.add("Please enter the search key words(only one criteria)");
        responses.add("Bye");
		//level 3
        responses.add("Do you want Vergilplus recommend some courses for you?(Y/N)");
        responses.add("Do you want Vergilplus recommend some courses for you?(Y/N)");
        responses.add("recommendCourses");
        responses.add("Bye");
        responses.add("searchKeywords");
        responses.add("searchKeywords");
        responses.add("Bye");
        responses.add("Bye");
		//level 4
        responses.add("recommendCourses");
        responses.add("Bye");
        responses.add("recommendCourses");
        responses.add("Bye");
        responses.add("Bye");
        responses.add("Bye");
        
        //main program to handle, and exit whenever user type "exit"
		String input_current = null;
		String input_prev = null;
		int i = 0;
		while( i < responses.size() ){
			
			input_current = br.readLine();
        	if ( input_current.equals("exit") ) {
        		break;
			}

        	System.out.print("The prev input:");
        	System.out.println(input_prev);
        	String output = null;
        	output = responses.get(i);
        	if (output.equals("Bye"))
        		break;
        	
        	if ( output.equals("recommendCourses") ) {
        		List<String> takenCourses = new ArrayList<String>();
        		
                String[] buff = input_prev.split(",");
                for( int j = 0; j<buff.length; ++j ){
                	takenCourses.add(buff[j]);
                }
                
        		List<CourseModel> recommended = recommendCourses(takenCourses);
        		recommended.forEach(System.out::println);
        	}
        	else if ( output.equals("searchKeywords") ) {
//        		List<String> res = searchKeywords(input_prev);
//        		res.forEach(System.out::println);
        		System.out.println("call searchKeywords");
        	}
        	else {
        		System.out.println(output);
        	}
        	
        	
        	if ( input_current.equals("Y") || input_current.equals("y") ) {
        		i = 2*i + 1;
        	}
        	else if ( input_current.equals("N") || input_current.equals("n") ) {
                i = 2*i + 2;
        	}
        	else {
        		i = 2*i + 2; // if the input_current is something useful, then no matter which tree next,
        					 // it will be fine
        	}
        	
            input_prev = input_current;
        }
		System.out.println("Thank you for using Vergilplus, wish you a good day");

		return;
	}
	
	public List<String> readTakenCourses() throws FileNotFoundException{
		InputStream fis = new FileInputStream(JSON_PATH);
        JsonReader reader = Json.createReader(fis);

        JsonObject personObject = reader.readObject();

        reader.close();

        JsonArray takenCoursesArray = personObject.getJsonArray("takenCourses");

		List<String> takenCourses = new ArrayList<>();
        
        for (JsonValue jsonValue : takenCoursesArray) {
            takenCourses.add(jsonValue.toString().replaceAll("^\"|\"$", ""));
        }
		
		return takenCourses;
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		return;
	}

	public IMasterController getMasterController() {
		return masterController;
	}

	public void setMasterController(IMasterController masterController) {
		this.masterController = masterController;
	}


	
	
//	public ITrackService getTrackService() {
//		return this.trackService;
//	}
//	
//	public void setTrackService(ITrackService trackService) {
//		this.trackService = trackService;
//	}
//
//	public ICourseService getCourseService() {
//		return this.courseService;
//	}
//	
//	public void setCourseService(ICourseService courseService) {
//		this.courseService = courseService;
//	}
}
