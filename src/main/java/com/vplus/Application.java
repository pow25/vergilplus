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
	public List<String> allTopics = new ArrayList<>();

	/**
	 * sets the application context using xml file
	 */
	public void setContext() {
		try {
			ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
				ctx.getBean("Application", Application.class);
				ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this,
						AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
		}catch(Exception e) {
			System.err.println(e);
		}
	}

	//set up the topic set
    public List<String> getAllTopics(){
        ArrayList<String> aLLTopics = new ArrayList<>();
        aLLTopics.add("computer\'vision");
        aLLTopics.add("operating\'system");
        aLLTopics.add("machine\'learning");
        aLLTopics.add("robotics");
        aLLTopics.add("natural\'language\'processing");
        aLLTopics.add("graphics");
        aLLTopics.add("networks");
        aLLTopics.add("artificial\'intelligence");
        aLLTopics.add("database");
        aLLTopics.add("bioinformatics");
        aLLTopics.add("system");
        aLLTopics.add("theory");
        aLLTopics.add("computational\'biology");
        aLLTopics.add("algorithm");
        return aLLTopics;
    }
    public AWSLambda createLambda(){
        Regions region = Regions.fromName("us-east-1");
        AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard()
                .withRegion(region);
        AWSLambda client = builder.build();
	    return client;
    }

    public String parseResponse(InvokeResult result){
        String response = new String(result.getPayload().array());
        JSONObject jsonObj = new JSONObject(response);
        String toS = jsonObj.get("greeting").toString();
        return toS;
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
	}
	/**
	 * Run the application
	 */
	@Override
	public void run(String... args) throws Exception {
			setContext();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String meg = br.readLine();
			List<String> takenCourses = new ArrayList<>();
			List<String> topic = new ArrayList<>();
			List<String> instructors = new ArrayList<>();
			String detect="false";
			allTopics = getAllTopics();
			List<String> allInstructors = masterController.findAllInstructors();
			while(!meg.toUpperCase().equals("OK")) {
				String body = "{\"userId\": \"dh2914\", \"message\": {\"word\":"+ "\""+meg +";"+detect+"\"" +"}}";
				InvokeRequest req = new InvokeRequest().withFunctionName("Chatbot").withPayload(body); // optional
                AWSLambda client = createLambda();
				InvokeResult result = client.invoke(req);
				String toS = parseResponse(result);
				if(toS.contains("Hi")){
					String[] pieces = toS.split(";");
					String output = pieces[0];
					System.out.println(output);
				}
				else if(!toS.contains("COMS")) {
					System.out.println(toS);
					if(toS.contains(";")){
						String[] pieces = toS.split(";");
						detect = pieces[pieces.length-1];
						if(toS.contains("I can\'t tell")){
							detect = "false";
						}
						String output = pieces[0];
						if(pieces.length>2) {
							for(int j =1; j < pieces.length-1;j++) {
								String keyword = pieces[j];
								if (allTopics.contains(keyword)) {
									topic.add(keyword.replace('\'', ' '));
								}
								if(keyword.contains(" "))
								{
									keyword = keyword.split(" ")[1]+", "+keyword.split(" ")[0];
								}
								if(allInstructors.contains(keyword.toUpperCase())){
									instructors.add(keyword);
								}
							}
						}
						System.out.println(output);
					}
					else{
						System.out.println(toS);
					}
				}
				else{
					String[] pieces = toS.split(";");
					detect = pieces[pieces.length-1];
					String output = pieces[0];
					if(toS.contains("What is the exact")){
						output = output.replace('\'', ' ');
					}
					System.out.println(output);
					if(pieces.length>1) {
						for (int i = 0; i < pieces[1].split(" ").length; ++i) {
							takenCourses.add(pieces[1].split(", ")[i]);
						}
					}

				}
				meg = br.readLine();
			}
			System.out.println("Here is our recomendation for you!");
			if (!topic.isEmpty()) {
				List<String> converted = getTakenCourses(takenCourses);
				List<CourseModel> rest = processTakenCourses(converted);
				for(int i=0;i<topic.size();i++) {
					HashSet<CourseModel> res = searchKeywords(topic.get(i), rest);
					res.forEach(System.out::println);
				}
			}if(!instructors.isEmpty()){
				for(int i =0;i<instructors.size();i++) {
					HashSet<CourseModel> res2 = searchKeywords(instructors.get(i), masterController.fetchAllCourses());
					System.out.println("These courses are delivered by Prof. " + instructors.get(i) + ":");
					res2.forEach(System.out::println);
				}
            }if(topic.isEmpty() && instructors.isEmpty()){
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


	public HashSet<CourseModel> searchKeywords(String keyword, List<CourseModel> courses){
		HashSet<CourseModel> matchedCourses = new HashSet<>();
		for(CourseModel c: courses){
			if(c.getCourseTitle().toUpperCase().contains(keyword.toUpperCase())){
				matchedCourses.add(c);
			}
			else if (c.getDescription().toUpperCase().contains(keyword.toUpperCase())){
				matchedCourses.add(c);
			}
		}
		if(matchedCourses.size()==0) {
			for (CourseModel c : courses) {
				if (c.getInstructor().toUpperCase().contains(keyword.split(" ")[1].toUpperCase())) {
					matchedCourses.add(c);
				}
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
