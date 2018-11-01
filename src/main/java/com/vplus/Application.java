package com.vplus;

import com.vplus.controller.IMasterController;
import com.vplus.dao.CourseDAO;
import com.vplus.models.CourseModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
	


	
	/**
	 * Run the application
	 */
	@Override
	public void run(String... args) throws Exception {
		setContext();

		List<String> takenCourses = new ArrayList<>();
		
		takenCourses = readTakenCourses();
		for(String s : takenCourses){
			System.out.println(s);
		}
		
		List<CourseModel> recommended = new ArrayList<>();
		recommended = masterController.recommendCourses(takenCourses);
		recommended.forEach(System.out::println);
		
//		System.out.println("output = " + courseService.selectCoursesByNumAndSection("WCOMS4771", 1));
//		System.out.println("DONE");
//		System.out.println("output = " + trackService.selectCoursesByTrackId("Computational Biology").getCourseID());
//		System.out.println("DONE");
		
//		int exitCode = SpringApplication.exit(ctx, () -> 0);
//		System.exit(exitCode);
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


	public List<String> searchKeywords(String keyword){
		List<String> matchedCourses = new ArrayList<>();
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
