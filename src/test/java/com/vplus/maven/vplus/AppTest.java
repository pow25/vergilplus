package com.vplus.maven.vplus;

import com.vplus.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Rigourous Test :-)
     */
	Application app_test;
    public void testApp1()
    {
    	
    	try {
    		app_test.run("999");
    	}catch(Exception e) { 
    		assertTrue( false );
    	}  
    }
}
