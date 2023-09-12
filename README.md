# CSE248 Project

## Eclipse Importing (ZIP downloaded):

1. Extract the contents of the zip folder which should leave you with just the root folder "cse248-project"
2. Go to a new (or existing) workspace in Eclipse and select "Import projects..."
3. Under the "Maven" folder select "Existing Maven Projects"
4. Click "Browse" and navigate to the root folder and click "Open"
5. Finally, it should show in the "Projects" the `pom.xml` file which means it imported correct
6. Click finish

## Running: This project uses the maven build system. Please use `mvn javafx:run` to properly start the application.

1. The data was manipulated entirely using ChatGPT where I had to explain the context of what each entry meant, and
   how they related to each other. It took some time, but eventually I was able to have ChatGPT produce a table that
   I could visually inspect for errors, and once it was okay, I told it to give me the data in JSON format.

2. From the JSON it gave me, I created a project using the Maven build system and used the jackson library for
   manipulating JSON files. I initially tried to use the annotations to have jackson automatically deserialize
   the data for me, but the data was too complex, so I had to create a deserializer specifically for the file
   structure and register it as a jackson deserializer. Therefore, the code that actually does the deserialization
   is in InstructorDeserializer.