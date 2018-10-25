package com.vplus;

import com.vplus.service.ITrackService;
import com.vplus.service.ICourseService;

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
	private ITrackService trackService;
	private ICourseService courseService;
	private ClassPathXmlApplicationContext ctx;
	
	/**
	 * sets the application context using xml file
	 */
	public void setContext() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Application app  = ctx.getBean("Application", Application.class);
		this.setTrackService(app.getTrackService());
		ctx.getAutowireCapableBeanFactory().autowireBeanProperties(this,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true); 
	}
	
	/**
	 * Run the application
	 */
	@Override
	public void run(String... args) throws Exception {
		setContext();
		System.out.println("output = " + courseService.selectCoursesByNumAndSection("WCOMS4771", 1));
		System.out.println("DONE");
		System.out.println("output = " + trackService.selectCoursesByTrackId("Computational Biology").getCourseID());
		System.out.println("DONE");

		int exitCode = SpringApplication.exit(ctx, () -> 0);
		System.exit(exitCode);
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
	
	public ITrackService getTrackService() {
		return this.trackService;
	}
	
	public void setTrackService(ITrackService trackService) {
		this.trackService = trackService;
	}

	public ICourseService getCourseService() {
		return this.courseService;
	}
	
	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}
}
