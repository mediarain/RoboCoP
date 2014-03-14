RoboCoP
=======

RoboCoP is a Java library that can generate a fully-functional ContentProvider from a simple JSON schema file.

Setup
========
*For a working implementation of RoboCoP please see the "My Representative" Android project(//insert link here)

Installation And Configuration
--------
### Download and Install
1. Download the latest jar: //TODO link to the latest jar
2. Create a new folder in your project directory and name it whatever you want (eg 'RoboCoP'). In a typical Android Studio project, this will be '<MainApplicationFolder>/<mainApplicationModule>/RoboCoP/'
3. Place the RoboCoP jar from step 1 into the RoboCoP directory (it's important that you don't put this jar in your libs folder since this is a buildscript dependency not a runtime dependency and it will cause problems if this is confused)

### Create Your Schema Definition
Create a JSON schema definition and place it in the same directory as the RoboCoP jar. You can name this file whatever you like, such as 'schema.json'.

#### Schema File Structure

    {
      "packageName" : "<the package name you want for your ContentProvider and associated classes/>",
      "providerName" : "<the base name for your provider. eg. 'Example' will yield 'ExampleProvider.java'/>",
      "databaseVersion" : <the numeric value for the current version of your database. if you increment this, the database will upgrade/>,
      "tables" : [], //see below for table definition rules
      "relationships" : [] //see below for table definition rules
    }
##### Table Definition Structure
    {
      "name" : "<name of the table/>",
      "members" : [] //see below for member field definition
    }
###### Table Field Definition Structure
    {
      "type" : "<the ~java data type for this field. Your options are: string, double, int, boolean, long (lower case). These will map to SQLite types/>",      
      "name" : "<the name of the field (lower case, underscore separated)/>"
    }
##### Relationship Definition Structure
    {
      "name" : "<the name of this relationship (lower case, underscore separated)/>",
      "left_table" : "<the left side of the join (in a one-to-many this is the 'one' side)/>",
      "right_table" : "<the right side of the join (the 'many' side)/>",
      "type" : "<the type of relationship. Your current option is 'to_many'"/>
    }

Remember that for the code generation to generate nice looking code, you need to write all of your schema values in lower case and underscore-separated.

Gradle Configuration
---------------
RoboCoP is designed to run as a Gradle build task but can be run from the command line as well. This document will only cover gradle configuration.
### buildscript definition

    buildscript {
      repositories {
        mavenCentral()
      }
      dependencies {
        classpath 'com.android.tools.build:gradle:0.9.+'        classpath files('libs/ContentProviderGenerator-1.0-SNAPSHOT-jar-with-dependencies.jar')
      }
    }
