# VergilPlus ![Travis status](https://travis-ci.org/pow25/vergilplus.svg?branch=master) [![codecov](https://codecov.io/gh/pow25/vergilplus/branch/master/graph/badge.svg)](https://codecov.io/gh/pow25/vergilplus) ![version](https://img.shields.io/badge/version-1.1.0-blue.svg?maxAge=2592000)

Advanced Software Engineering Team Project

VirgilPlus is a software that makes course planning effortless for students.  Whereas currently students have to browse through an entire catalog that contains mostly irrelevant or unavailable courses, VirgilPlus lets students bypass the frustration of picking out the courses right for them.  In particular, given a set of courses that a student has already taken and a set of preferences such as graduation year, day of the week or time window, VirgilPlus outputs the courses that fit these constraints up to a threshold, and eases the process of choosing desired courses

Build/Test: The Vergilpls is managed by maven, including Junit test. To build our project simply run mvn build, same for testing

Install:download the source code and use mvn install to install our project

Operation:<br />
1, Vergilplus will greet user by welcom message<br />
2, user will type which functionalities he/she want to user<br />
    a, Tell me about Professor + "Professor name" / I am interested in professor + "Professor name"<br />
    b, I am interested in + "course topics, like machine learning"<br />
    c, I want course recommendation<br />
3, Vergilplus then will perform correlated functionalities<br />
    a, It will search description words of this professor in our database and tell the user correlated courses taught by that professor.<br />
    b, It will search all courses in our database that are related to specified topics<br />
    c, It will recommend 4 courses(usually) based on previous students' reviews of those courses. The recommendation is also based on              user's previous taken courses.<br />  

Technology used: Java, Travis CI, Maven, Spring, Junit, Mysql, python, sonarclound, PMD, Jacoco+Codecov, AWS Lex, Google Cloud Natural Language
