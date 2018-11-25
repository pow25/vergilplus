package com.vplus;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
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

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.regions.Regions;


@SpringBootApplication
public class Application implements CommandLineRunner {

	// TODO: Figure out how to autowire...
	@Autowired
	private IMasterController masterController;
	private ClassPathXmlApplicationContext ctx;

	public static final String JSON_PATH = "./data/user_input.json";
	private String[] args;

	/**
	 * sets the application context using xml file
	 */
	public void setContext() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ctx.getBean("Application", Application.class);
		ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true); 
	}

	public List<CourseModel> processTakenCourses(List<String> takenCourses){
		List<CourseModel> allCourses = masterController.fetchAllCourses();
		List<CourseModel> recommended = masterController.processTakenCourses(takenCourses, allCourses);
		return recommended;
	}
	//recommend courses based on taken course
	public List<CourseModel> recommendCourses(List<String> takenCourses ) {
		
		List<CourseModel> recommended = masterController.recommendCourses(takenCourses);
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
			Regions region = Regions.fromName("us-east-1");
			AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard()
					.withRegion(region);
			AWSLambda client = builder.build();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String meg = br.readLine();
			List<String> takenCourses = new ArrayList<>();
			List<String> topic = new ArrayList<>();
			String instructor = "";
			while(!meg.toUpperCase().equals("OK")) {
				String body = "{\"userId\": \"dh2914\", \"message\": {\"word\":"+ "\""+meg +"\"" +"}}";
				InvokeRequest req = new InvokeRequest().withFunctionName("Chatbot").withPayload(body); // optional
				InvokeResult result = client.invoke(req);
				String response = new String(result.getPayload().array());
				JSONObject jsonObj = new JSONObject(response);
				String toS = jsonObj.get("greeting").toString();
				if(toS.contains("Hi")){
					System.out.println(toS);
				}
				else if(!toS.contains("COMS")) {
					String[] pieces = toS.split(";");
					String output = pieces[0];
					if(pieces.length>1) {
						String[] topics = pieces[1].split(" ");
						for(int i=0;i<topics.length;i++){
							topic.add(topics[i].replace('\'', ' '));
						}
					}
					if(pieces.length>2)
					{
						instructor = pieces[2];
					}
					System.out.println(output);
				}
				else{
					String[] pieces = toS.split(";");
					String output = pieces[0];
					System.out.println(output);
					if(pieces.length>1) {
						for (int i = 0; i < pieces[1].split(" ").length; ++i) {
							takenCourses.add(pieces[1].split(" ")[i]);
						}
					}
					if(pieces.length>2){
						String[] names = pieces[2].split(" ");
						for(int i=0;i<names.length;i++){
							takenCourses.add(names[i]);
						}
					}
				}
				meg = br.readLine();
			}
			System.out.println("Here is our recomendation for you!");
			if (topic !=null) {
				List<String> converted = getTakenCourses(takenCourses);
				List<CourseModel> rest = processTakenCourses(converted);
				HashSet<String> res2 = searchKeywords(instructor, rest);
				for(int i=0;i<topic.size();i++) {
					HashSet<String> res = searchKeywords(topic.get(i), rest);
					res.forEach(System.out::println);
				}
				if(instructor!="") {
					System.out.println("These courses are delivered by Prof. " + instructor + ":");
					res2.forEach(System.out::println);
				}
			}else{
				List<String> converted = getTakenCourses(takenCourses);
				List<CourseModel> res = recommendCourses(converted);
				res.forEach(System.out::println);
			}

		int exitCode = SpringApplication.exit(ctx, () -> 0);
		System.exit(exitCode);
		return;
	}
	public List<String> getTakenCourses(List<String> takenCourses){
		return masterController.convertCourseForm(takenCourses);
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


	public HashSet<String> searchKeywords(String keyword, List<CourseModel> courses){
		HashSet<String> matchedCourses = new HashSet<>();
		for(CourseModel c: courses){
			if (c.getDescription().toUpperCase().contains(keyword.toUpperCase())||c.getCourseTitle().toUpperCase().contains(keyword.toUpperCase())){
				matchedCourses.add(c.getCourseTitle()+" "+c.getCourseNumber());
			}
		}
		return matchedCourses;
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
