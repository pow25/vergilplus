package com.vplus;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.vplus.controller.IMasterController;
import com.vplus.models.CourseModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.context.ConfigurableApplicationContext;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.regions.Regions;
import java.util.Random;

@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private IMasterController masterController;
	private ClassPathXmlApplicationContext ctx;

	public static final String JSON_PATH = "./data/user_input.json";

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
        ArrayList<String> allTopics = new ArrayList<>();
        allTopics.add("computer vision");
        allTopics.add("operating system");
        allTopics.add("machine learning");
        allTopics.add("robotics");
        allTopics.add("natural language processing");
        allTopics.add("graphics");
        allTopics.add("networks");
        allTopics.add("artificial intelligence");
        allTopics.add("database");
        allTopics.add("system");
        allTopics.add("theory");
        allTopics.add("computational biology");
        allTopics.add("algorithm");
		allTopics.add("security");
        return allTopics;
    }
    
    private boolean isValidTopic(String topic){
    	for(String t : getAllTopics()){
    		if(t.equals(topic)){
    			return true;
    		}
    	}
    	return false;
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

    public String getAwsResponse(String input){   	
    	String user = "user";
    	String detect = "false";
    	String body = "{\"userId\": \""+ user + "\", \"message\": {\"word\":"+ "\""+input +";"+detect+"\"" +"}}";
    	InvokeRequest req = new InvokeRequest().withFunctionName("Chatbot").withPayload(body); // optional
        AWSLambda client = createLambda();
        InvokeResult result = client.invoke(req);
    	String res = parseResponse(result);        	
    	return res;
    }
    
    
	public List<CourseModel> processTakenCourses(List<String> takenCourses){
		List<CourseModel> allCourses = masterController.fetchAllCourses();
		List<CourseModel> openCourses = masterController.processTakenCourses(takenCourses, allCourses);
		return openCourses;
	}

	//recommend courses based on taken course
	public List<CourseModel> recommendCourses(List<String> takenCourses ) {
		List<CourseModel> recommended = masterController.recommendCourses(takenCourses);
		return recommended;
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

	public List<CourseModel> searchKeyword(String keyword, List<CourseModel> courses){
		List<CourseModel> matchedCourses = new ArrayList<>();
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

	
	private void promptWelcomeMessage(){
		System.out.println("Welcome to VergilPlus - Course Selection Made Easier.");
		System.out.println("What can we do for you?");
	}

	private void promptFunctionality() {
		System.out.println("Sorry, we didn't quite catch that.");
		System.out.println("What can we do for you?");
	}
	
	private void promptTryAgain(){
		System.out.println("Sorry, we didn't understand that.  "
				+ "Please try again.  "
				+ "Your sentences might be too complex, "
				+ "you might not be following the correct format, "
				+ "or the information you've requested is not in our database.");		
	}
	
	private void promptForceChoiceFunctionality(){
		System.out.println("We are sorry we are having trouble understanding you."
				+ "Here are the things that we can do for you:\n"
				+ "1. Tell me about a professor and recommend that professor's courses\n"
				+ "2. Recommend courses based on a topic.\n"
				+ "3. Recommend courses based on previous courses.\n"
				+ "You may enter 1, 2, or 3.");
	}

	// ask user for functionality through a series of prompts
	// returns a valid functionality that the user chooses
	private int getFunctionality(BufferedReader br) throws IOException{
		int numFails = 0;
		String freeStyleFunctionality = "";
		
		// 1, 2, 3  or -1
		String awsResp = "S";
		
		while(awsResp.substring(0,1).equals("S") && numFails < 3){
			freeStyleFunctionality = br.readLine();
			if(freeStyleFunctionality.trim().length() == 0){
				promptFunctionality();
				numFails++;
				continue;
			}
			
			awsResp = getAwsResponse(freeStyleFunctionality);
			numFails++;
			if(numFails != 3 && awsResp.substring(0,1).equals("S")){
				promptFunctionality();
			}
		}
		//System.out.println(awsResp);
		if(numFails >= 3){
			promptForceChoiceFunctionality();
			String forcedChoice = br.readLine();
			
			while(!forcedChoice.equals("1") && !forcedChoice.equals("2") && !forcedChoice.equals("3")){
				promptTryAgain();
				forcedChoice = br.readLine();
				if(forcedChoice.trim().length() == 0){
					continue;
				}
			}
			
			return Integer.parseInt(forcedChoice);
		}
		return Integer.parseInt(awsResp);
		
	}
	
	private void promptProfessor(){
		System.out.println("Please enter the professor's name. (E.g., Gail Kaiser)");
	}
	
	private String getProfessor(BufferedReader br) throws IOException{
		String userInput = "";
		
		// while prof not found, prompt again
		while(!masterController.isValidProfessor(userInput)){
			promptProfessor();
			userInput = br.readLine();
			
			
			String[] userInputArr = userInput.split("\\s+");
			
			if(userInputArr.length < 2){
				continue;
			}
			
			userInput = userInputArr[1].toUpperCase() + ", " + userInputArr[0].toUpperCase();
			if(!masterController.isValidProfessor(userInput)){
				promptTryAgain();
			}
		}
		
		return userInput;
	}
	
	private List<CourseModel> recommendByProfessor(BufferedReader br) throws IOException{
		String professor = getProfessor(br);
		String words = masterController.getWordsProfessor(professor);
		System.out.println(words);
		
		List<String> takenCourses = getTakenCourses(br);
		List<CourseModel> allCourses = masterController.fetchAllCourses();
		List<CourseModel> profCourses = searchKeyword(professor, allCourses);
		
		List<CourseModel> tmp = new ArrayList<>(profCourses);
		for(CourseModel c : tmp){
			if(takenCourses.contains(c.getCourseNumber())){
				profCourses.remove(c);
			}
		}
		
		return profCourses;
	}
	
	private void promptTopic(){
		System.out.println("Please enter the topic. (E.g., Machine Learning)");
	}

	private String getTopic(BufferedReader br) throws IOException{
		String awsResp = "-1";
		String userInput = "";
		
		// while lex returns error code -1, prompt again
		while(awsResp.equals("-1") || !isValidTopic(awsResp)){
			promptTopic();
			userInput = br.readLine();
			if(userInput.trim().length() == 0){
				continue;
			}
			
			awsResp = getAwsResponse(userInput);
			if(!isValidTopic(awsResp)){
				System.out.println("Sorry, we either didn't understand that, or that is not one of our supported topics.  Please try again.");
			}
		}
		
		return awsResp;
	}
	
	private List<CourseModel> recommendByTopic(BufferedReader br) throws IOException{		
		String topic = getTopic(br);
		List<String> takenCourses = getTakenCourses(br);
		List<CourseModel> allCourses = masterController.fetchAllCourses();
		List<CourseModel> topicCourses = searchKeyword(topic, allCourses);

		List<CourseModel> tmp = new ArrayList<>(topicCourses);
		for(CourseModel c : tmp){
			if(takenCourses.contains(c.getCourseNumber())){
				topicCourses.remove(c);
			}
		}

		return topicCourses;
	}
	
	private void promptTakenCourses(){
		System.out.println("What courses have you taken? (Enter using format WCOMS1234,WCOMS4321)\n"
				+ "Enter none if you have not taken any.");
	}
	
	private boolean validCourses(String courses){
		String[] courseNumbers = courses.split(",");
		for(String c : courseNumbers){
			if(!masterController.isValidCourse(c)){
				return false;
			}
		}
		return true;
	}
	
	private List<String> getTakenCourses(BufferedReader br) throws IOException{
		promptTakenCourses();
		String input = br.readLine();
		List<String> res = new ArrayList<>();
		
		if(input.equals("none")){
			return res;
		} 
		
		while(!validCourses(input)){
			promptTryAgain();
			promptTakenCourses();
			input = br.readLine();
			if(input.equals("none")){
				return res;
			} 
		}
		
		res = Arrays.asList(input.split(","));
		return res;
	}
	
	private List<CourseModel> recommendByPrevCourses(BufferedReader br) throws IOException{
		List<String> takenCourses = getTakenCourses(br);
		List<CourseModel> recCourses = masterController.recommendCourses(takenCourses);
		return recCourses;
	}
	
	@Override
	public void run(String... args) throws Exception {
			setContext();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			// welcome user and ask for functionality
			promptWelcomeMessage();
			int functionality = getFunctionality(br);
			
			List<CourseModel> recommendations = null;
			switch(functionality){
				case 1: recommendations = recommendByProfessor(br);
						break;
				case 2: recommendations = recommendByTopic(br);
						break;
				case 3: recommendations = recommendByPrevCourses(br);
						break;
			}
			
			if(recommendations.isEmpty()){
				System.out.println("We don't have an recommendation for you right now!");
			}
			else{
				System.out.println("Here are our recommendations!");
				recommendations.forEach(
						l -> System.out.println(l+ 
								"\n-------------------------------------"));
			}

	}	

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		ConfigurableApplicationContext ctx = app.run(args);
		SpringApplication.exit(ctx, ()-> 0);
		return;
	}

	public IMasterController getMasterController() {
		return masterController;
	}

	public void setMasterController(IMasterController masterController) {
		this.masterController = masterController;
	}
}
