package com.vplus;

import com.vplus.controller.IMasterController;
import com.vplus.models.CourseModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
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
		System.out.println("There are several options below, type integer to choose:");
		System.out.println("1, searchKeywords");
		System.out.println("2, recommendCourses");
		
		//create the buffer read to handle the user input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		int result = Integer.parseInt(input);
		if ( result == 1 ) {
			System.out.println("Please enter key words:");
			input = br.readLine();
			HashSet<String> res = searchKeywords(input);
			res.forEach(System.out::println);
		}
		else {
			System.out.println("Please enter taken courses, seperated by \",\"");
			input = br.readLine();
			String[] buff = input.split(",");
			List<String> takenCourses = new ArrayList<String>();
	        for(int i=0;i<buff.length;++i){
	        	takenCourses.add(buff[i]);
	        }
	        List<CourseModel> res = recommendCourses(takenCourses);
	        res.forEach(System.out::println);
		}
		
		System.out.println("Thank you for using Vergilplus, wish you a good day :)");
		int exitCode = SpringApplication.exit(ctx, () -> 0);
		System.exit(exitCode);
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


	public HashSet<String> searchKeywords(String keyword){
		HashSet<String> matchedCourses = new HashSet<>();
		List<CourseModel> allCourses = masterController.fetchAllCourses();
		for(CourseModel c: allCourses){
			if (c.getDescription().toUpperCase().contains(keyword.toUpperCase())||c.getCourseTitle().toUpperCase().contains(keyword.toUpperCase())){
				matchedCourses.add(c.getCourseTitle()+" "+c.getCourseNumber());
			}
		}
		return matchedCourses;
	}

	public static void main(String[] args){
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
