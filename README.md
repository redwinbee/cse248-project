# CSE248 Project (Edwin Bermudes)

## Eclipse Importing (ZIP downloaded):

1. Extract the contents of the zip folder which should leave you with just the root folder "cse248-project"
2. Go to a new (or existing) workspace in Eclipse and select "Import projects..."
3. Under the "Maven" folder select "Existing Maven Projects"
4. Click "Browse" and navigate to the root folder and click "Open"
5. Finally, it should show in the "Projects" the `pom.xml` file which means it imported correct
6. Click finish

## Running: This project uses the maven build system. Please use `mvn javafx:run@release` to run the program normally, or `mvn javafx:run@debug` and attach a debugger.

1. The original plan was to use ChatGPT to convert all the data into a JSON file and read it that way, but
it turned out doing in that way was more cumbersome than simply trying to parse the format as-is. Therefore,
this project actually uses the Apache Commons CSV parser to parse the data which means it's more reliable and
refactorable.

2. The application features data persistence which is exposed via the Configuration and all data is backed up
during any CRUD operations via application events and observable list listeners. There is also an optional generator
built into the application which allows for generating an arbitrary number of Instructors for testing purposes.