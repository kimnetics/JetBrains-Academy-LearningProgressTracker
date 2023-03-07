# JetBrains Academy Learning Progress Tracker Project

An example of a passing solution to the final phase of the JetBrains Academy Java Learning Progress Tracker project.

## Description

This project is a command line application that allows the user to add students and course points to a simulated data store. The application is structured as if a database is being used. There are record type classes and data layer classes to simulate access to a data store.

The application also does some summarization and statistical calculations against the entered data.

This project introduced Gradle. It is the first JetBrains Academy project I have encountered that has Gradle configuration as part of the project.

This project also introduced JUnit. It was not covered in the project, but I also added Mockito to allow mocking of some data layer classes.

## Notes

The relative directory structure was kept the same as the one used in my JetBrains Academy solution.

After copying these directories from the JetBrains Academy directory structure, I needed to tweak the build.gradle file a bit to get things running again.

The build.gradle file in the JetBrains Academy directory structure looked like this:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.jetbrains:annotations:23.0.0'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.2'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.2'
    testImplementation 'org.junit.jupiter:junit-jupiter-params:5.9.2'
    testImplementation 'org.mockito:mockito-core:5.1.1'
    testImplementation 'org.mockito:mockito-junit-jupiter:5.1.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.2'
}

test {
    useJUnitPlatform()
    testLogging {
        events "passed", "skipped", "failed", "standard_out", "standard_error"
    }
}
```

When run, the application is missing some prompts to give better context of what mode it is in. The behavior is as specified in the requirements and needed to be this way to pass the automated testing.

Here is an example session:

(User entered items were given a '> ' prefix, which did not actually show in the console.)
```
Learning Progress Tracker
> add students
Enter student credentials or 'back' to return:
> Robert James rjames@test.com
The student has been added.
> Wendy Watson wwatson@test.com
The student has been added.
> Penelope Smith psmith@test.com
The student has been added.
> back
Total 3 students have been added.
> list
Students:
10000
10001
10002
> find
Enter an id or 'back' to return:
> 10000
10000 points: Java=0; DSA=0; Databases=0; Spring=0
> back
> add points
Enter an id and points or 'back' to return:
> 10000 100 0 0 0
Points updated.
> 10000 0 200 0 0
Points updated.
> 10001 0 0 300 0
Points updated.
> 10001 0 0 0 400
Points updated.
> 10002 0 30 30 0
Points updated.
> 10002 0 50 50 0
Points updated.
> back
> find
Enter an id or 'back' to return:
> 10000
10000 points: Java=100; DSA=200; Databases=0; Spring=0
> back
> add points
Enter an id and points or 'back' to return:
> 10000 0 0 0 550
Points updated.
> back
> statistics
Type the name of a course to see details or 'back' to quit:
Most popular: DSA, Databases
Least popular: n/a
Highest activity: DSA, Databases
Lowest activity: n/a
Easiest course: Spring
Hardest course: n/a
> dsa
DSA
id      points completed
10001   200    50.0%
10005   50     13.0%
10004   30     8.0%
> spring
Spring
id      points completed
10006   550    100.0%
10003   400    73.0%
> back
> notify
To: rjames@test.com
Re: Your Learning Progress
Hello, Robert James! You have accomplished our Spring course!
Total 1 students have been notified.
> exit
Bye!
```
