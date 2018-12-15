# VergilPlus ![Travis status](https://travis-ci.org/pow25/vergilplus.svg?branch=master) [![codecov](https://codecov.io/gh/pow25/vergilplus/branch/master/graph/badge.svg)](https://codecov.io/gh/pow25/vergilplus) ![version](https://img.shields.io/badge/version-2.0.0-blue.svg?maxAge=2592000)

### Advanced Software Engineering Team Project ![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)

VergilPlus is a software that makes course planning effortless for students. Whereas currently students have to browse through an entire catalog that contains mostly irrelevant or unavailable courses, VergilPlus lets students bypass the frustration of picking out the courses right for them. Our course recommendation system is based on [sentimental analysis](https://cloud.google.com/natural-language/docs/sentiment-tutorial) of [previous students' reviews](http://culpa.info/), so it will find the best courses that suit you the most.  In particular, given a set of courses that a student has already taken, a professor that the student would like to work with or a topic that student is interest in, VergilPlus outputs the courses that fit these constraints.  

## Build: <br />
```
Vergilplus is managed by maven and Junit test. To build our project:
1. Open terminal enter project directory.
2. run "mvn install". All the unit test cases will run automatically and if all use cases passed, the project is built successfully.
```

## Install: <br />
```
Download the source code and use mvn install to install our project, like stated before.
```

## Test: <br />
```
Our project uses JUnit test tool and Jacoco for testing. To test our project simply run mvn test
```

## Operation:<br />
1. Vergilplus will greet user by welcome message.<br />
2. User can type the following sentences to start searching<br />
    &nbsp;&nbsp;&nbsp;&nbsp;a. Tell me about Professor + "Professor name" / I am interested in professor + "Professor name"<br />
    &nbsp;&nbsp;&nbsp;&nbsp;b. I am interested in + "course topics, like machine learning"<br />
    &nbsp;&nbsp;&nbsp;&nbsp;c. I want course recommendation<br />
3. Vergilplus then asks for the specifics of the chosen functionality.  If the user chooses to inquiry about a professor, then VergilPlus prompts for professor name.  If the user chooses to inquiry about a topic, then VergilPlus prompts for topic.  If the user chooses the third functionality, then VergilPlus directly continues to step 4.   
4. VergilPlus asks for previous taken courses, which users may input using course number, preferably in the format of "COMSW4111".
5. Vergilplus then will perform correlated functionalities<br />
    &nbsp;&nbsp;&nbsp;&nbsp;a. It will search sentimental words of the professor in our database and tell the user what courses th professor teaches.
    &nbsp;&nbsp;b. It will search all courses in our database that are related to the specified topic.<br />
    &nbsp;&nbsp;&nbsp;&nbsp;c. It will recommend courses based on our sentiment analysis of previous course reviews.<br />
    All course recommendations take into account the student's previous courses.
<br />

***Technologies and tools used: Java, Google NLP, Amazon AWS Lex, Mysql, Travis CI, Maven, Spring, JUnit, Python, SonarClound, PMD, Jacoco+Codecov.***
