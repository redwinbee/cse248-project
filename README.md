# CSE248 Project

# NOTE: This project uses the maven build system. Please use `mvn javafx:run` to properly start the application.

3. The data was manipulated entirely using ChatGPT where I had to explain the context of what each entry meant, and
   how they related to each other. It took some time, but eventually I was able to have ChatGPT produce a table that
   I could visually inspect for errors, and once it was okay, I told it to give me the data in JSON format.

2. From the JSON it gave me, I created a project using the Maven build system and used the jackson library for
   manipulating JSON files. I initially tried to use the annotations to have jackson automatically deserialize
   the data for me, but the data was too complex, so I had to create a deserializer specifically for the file
   structure and register it as a jackson deserializer. Therefore, the code that actually does the deserialization
   is in InstructorDeserializer.