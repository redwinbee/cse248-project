1.  The data was manipulated entirely using ChatGPT where I had to explain the context of what each entry meant, and
    how they related to each other. It took some time, but eventually I was able to have ChatGPT produce a table that
    I could visually inspect for errors, and once it was okay, I told it to give me the data in JSON format.

2.  From the JSON it gave me, I created a project using the Maven build system and used the jackson library for
    manipulating JSON files. I initially tried to use the annotations to have jackson automatically deserialize
    the data for me, but the data was too complex, so I had to create a deserializer specifically for the file
    structure and register it as a jackson deserializer. Therefore, the code that actually does the deserialization
    is in InstructorDeserializer.

Side Note: This project _should_ be runnable in the Eclipse IDE, but if all else fails it is definitely runnable
           in IntelliJ IDEA 2023.2.1