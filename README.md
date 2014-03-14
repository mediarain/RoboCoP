RoboCoP
=======

RoboCoP is a Java library that can generate a fully-functional ContentProvider from a simple JSON schema file.

Setup
========
There are only a few steps involved in order to set up this tool to work inside of your build environment. The steps should not be difficult but should be done with care. At a high level you must:
1. Download and install the library jar file into your project directory
2. Include your own custom JSON schema file in your project directory
3. Configure your build.gradle file and include a special build task to generate the necessary files
4. Use the Gradle task to generate your ContentProvider and related files
5. Install your new ContentProvider in your AndroidManifest.xml file
6. Enjoy

Download And Install
--------
### Download and Install
1. Download the latest jar from the releases section of this repo page. You can use either the jar with dependencies (recommended) or provide them yourself.
2. Create a new folder in your project directory and name it whatever you want (eg 'RoboCoP'). In a typical Android Studio project, this will be '<MainApplicationFolder>/<mainApplicationModule>/RoboCoP/'
3. Place the RoboCoP jar from step 1 into the RoboCoP directory (it's important that you don't put this jar in your libs folder since this is a buildscript dependency not a runtime dependency and it will cause problems if this is confused)

Create Your JSON Schema Definition
----------
Create a JSON schema definition and place it in the same directory as the RoboCoP jar. You can name this file whatever you like, such as 'schema.json'. A sample schema file can be found in our Wiki: https://github.com/mediarain/RoboCoP/wiki/Example-JSON-schema-definition

### Schema File Structure

    {
      "packageName" : "<the package name you want for your ContentProvider and associated classes/>",
      "providerName" : "<the base name for your provider. eg. 'Example' will yield 'ExampleProvider.java'/>",
      "databaseVersion" : <the numeric value for the current version of your database. if you increment this, the database will upgrade/>,
      "tables" : [], //see below for table definition rules
      "relationships" : [] //see below for table definition rules
    }
#### Table Definition Structure
    {
      "name" : "<name of the table/>",
      "members" : [] //see below for member field definition
    }
##### Table Field Definition Structure
    {
      "type" : "<the ~java data type for this field. Your options are: string, double, int, boolean, long (lower case). These will map to SQLite types/>",
      "name" : "<the name of the field (lower case, underscore separated)/>"
    }
#### Relationship Definition Structure
    {
      "name" : "<the name of this relationship (lower case, underscore separated)/>",
      "left_table" : "<the left side of the join (in a one-to-many this is the 'one' side)/>",
      "right_table" : "<the right side of the join (the 'many' side)/>",
      "type" : "<the type of relationship. Your current option is 'to_many'"/>
    }

Remember that for the code generation to generate nice looking code, you need to write all of your schema values in lower case and underscore-separated.

Gradle Configuration
---------------
The Gradle configuration involves just a few steps:
1. Add a buildscript definition that depends on the RoboCoP library
2. Add an import for the RoboCoP generator class
3. Add a build task for the code generation process

A sample build.gradle file can be found in our Wiki: https://github.com/mediarain/RoboCoP/wiki/Example-build.gradle-file

### buildscript definition

    buildscript {
      repositories {
        mavenCentral()
      }
      dependencies {
        classpath 'com.android.tools.build:gradle:0.9.+'

        //this is the important part, the buildscript needs our library to generate the code.
        classpath files('RoboCoP/RoboCoP-<version-number>-jar-with-dependencies.jar')
      }
    }


### RoboCoP generator import
Place the import statement near the top of the file such as right underneath the android plugin

    apply plugin: 'android'
    import com.rain.utils.android.robocop.generator.*;
    
### Build Task
Place the build task at the root level in your build.gradle file (not inside something like the android{} definition for example)

    task contentProviderGen {
        description = 'Generating a beautiful ContentProvider and required classes'
        doLast {
            System.out.println("Generating ContentProvider...")
            String schemaFilename = 'RoboCoP/agenda_schema.json';//replace with the path to your schema
            String baseOutputDir = 'src/main/java/';
            //if gradle throws an error on the following line, you probably either don't have your import statement set or you have the wrong path in your buildscript definition
            ContentProviderGenerator.generateContentProvider(schemaFilename, baseOutputDir);
        }
    }
    
#### Explanation of Build Task
The above task definition should be pretty easy to follow. This simply allows gradle to execute our code generation task whenever you want. inside the doLast{} block is where the main customization takes place. the schemaFilename variable should point to the location of your JSON schema file (explained previously). the baseOutputDir specifies at what root directory your generated code should be placed. What your resulting package structure (and thus file structure) will depend on what you put in your schema definition for the 'packageName' variable. So if you put 'com.mycompany.awesomeandroidapp' as your packageName, then the resulting files will be under 'src/main/java/com/mycompany/awesomeandroidapp'. 
#### Alternative Output Directories
The generator tool can write code to any directory for which it has permission. We will often place generated code in its own source folder outside our main src directory. Some prefer to keep all their sources, generated or otherwise, in the same location. So as an example, if you want to place your ContentProvider in another directory like 'src-gen' then your build task would look like this:

    task contentProviderGen {
        description = 'Generating a beautiful ContentProvider and required classes'
        doLast {
            System.out.println("Generating ContentProvider...")
            String schemaFilename = 'RoboCoP/agenda_schema.json';
            String baseOutputDir = 'src-gen/';
            ContentProviderGenerator.generateContentProvider(schemaFilename, baseOutputDir);
        }
    }
    
However, you are also responsible to make sure that 'src-gen' is included as a source folder in your build.gradle file. This can be done as follows:

    sourceSets {
        main {
            java.srcDir 'src-gen'
        }
    }
    
We personally prefer this latter method as it keeps our main source directory uncluttered and focused on our own application code. It works either way though.
    
Running The Generator
-----------
Once all the above setup are complete the last step to getting things up and running is to simply run the custom Gradle build task we created. The easiest way to do this is from Android Studio by right clicking the 'contentProviderGen' task text and selecting the "Run 'gradle:contentProvid...'". This will add a new build configuration in the top menu build configuration drop down. It will also provide you with the option of saving this configuration permanently so that it can be re-run whenever you like. This is a convenient option to select as you will more than likely want to make changes throughout the course of your development process. 

### Caution
Whenever you run this task, all of your generated code will be replaced with the newly generated code. Also, any other classes that are in the same folders as your generated code will be removed. You should never place your hand written code in the same directories as our generated code.

Installing The Provider - Important!!!
---------
In order to use a ContentProvider in your Android app you must install it into your application's AndroidManifest file. The generator creates a special file in the root of your generated code called 'content-provider.xml' to assist with this step. Copy the contents of this file into your AndroidManifest file inside the <Application/> node. The generated code has the provider's exported property set to false. If this is true, then other applications may be able to access your data. Only set this to true if you know what you are doing.

Your Done, Enjoy! - Example Usage of a Generated ContentProvider
----------
To learn more about the ContentProvider class and its usages, please read the official docs: http://developer.android.com/reference/android/content/ContentProvider.html and http://developer.android.com/guide/topics/providers/content-providers.html. The following are some examples that encompass most of our typical usages.

### Using a CursorLoader To Populate a ListView
Using Loaders is a great way to simplify fetching data for which to populate your UI and take away many of the concerns that come with updating data when necessary and managing the lifecycle of your app.
#### Implement LoaderCallbacks<Cursor> in your Activity or Fragment

    public class AgendaListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    
        @Override
        public void onCreate(Bundle savedInstanceState) {

        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

    }
    
The LoaderCallbacks interface provides hooks for creating your Loader and responding to load events. You'll now have to create your Loader which will be a CursorLoader and handling the events.
##### Create a CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), AgendaProvider.AGENDA_CONTENT_URI,new String[]{AgendaTable._ID, AgendaTable.NAME},null,null,AgendaTable._ID + " DESC");
    }
    
The generated ContentProvider has content URI's for all your tables and code completion should help you find the one you want. After that you can specify which fields you want to be returned and any query and query params or ordering need to go into the query.

##### Handle the load event callbacks
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
    
This is about the extent of it. when onLoadFinished will get called whenever a query to your table takes place which could be because you started/restarted the loader explicitly or because your backing data store changed which happens automatically with our generated code. onLoaderReset gets called during (suprise!) resets and so the current data is unavailable and so we pass in null to the cursor for the meantime.

##### Starting the loader
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new AgendaAdapter(getActivity(), null, 0);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }
    
Once it's started, the CursorLoader will query your ContentProvider via the ContentResolver and return your data when it's ready (usually very quickly). Here is what a working ListFragment might look like.

    public class AgendaListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mAdapter = new AgendaAdapter(getActivity(), null, 0);
            setListAdapter(mAdapter);
            getLoaderManager().initLoader(0, null, this);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), AgendaProvider.AGENDA_CONTENT_URI,new String[]{AgendaTable._ID, AgendaTable.NAME},null,null,AgendaTable._ID + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }

    }


### Adding a new record to a table
Building off the previous example, we'll demonstrate how to insert new records that will automatically get loaded into the ListFragment once inserted.

    Agenda agenda = new Agenda();
    agenda.setName("New Agenda");
    getContentResolver().insert(AgendaProvider.AGENDA_CONTENT_URI, agenda.getContentValues());
    
The generated tool includes convenient model classes that map to your table records. They can be constructed/inflated from a cursor and can also export a ContentValues object which is necessary for ContentResolver operations. Because the ListFragment CursorLoader is observing our Agenda table content URI, it, the onLoadFinished will get triggered automatically once this insert operation finishes and our new record will show up in our ListView. Sweet!
### Updating an existing record
Let's now suppose that when the user taps on an agenda item from the list they are presented with a form with which to modify the details of the agenda. The following is an example of how to update the agenda item so that its values are saved and automatically reflected by our CursorLoader previously explained.

    private void updateAgenda() {
        //mAgenda is an Agenda model object generated by this tool, it has all the getters and setters you need
        mAgenda.setName(mAgendaTitle.getText().toString());
        mAgenda.setPersonConducting(mPersonConducting.getText().toString());
        //there are also convenient constants on the generated Table classes for common necessary strings. AgendaTable.WHERE_ID_EQUALS resolves to "_id = ?"
        getContentResolver().update(AgendaProvider.AGENDA_CONTENT_URI,mAgenda.getContentValues(), AgendaTable.WHERE_ID_EQUALS,new String[]{agenda.getRowId().toString()});
    }

### Deleting a Record
Sometimes we get tired of some records and we have to let them go. Here's how to do it.

    getContentResolver().delete(AgendaProvider.AGENDA_CONTENT_URI, AgendaTable.WHERE_ID_EQUALS, agenda.getRowId().toString());

### Performing Batch Operations
Sometimes you have to do a lot of operations and it is much more efficient to do them all as a batch rather than one at a time. Here's a contrived example:

    public void updateLotsOfAgendasAtOnce(List<Agenda> agendas) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (int i = 0; i < agendas.size(); i++) {
            ContentProviderOperation operation = ContentProviderOperation.newUpdate(AgendaProvider.AGENDA_CONTENT_URI)
                    .withSelection(AgendaTable.WHERE_ID_EQUALS, new String[]{agendas.get(i).getRowId().toString()}).build();
            operations.add(operation);
        }
        //just for fun, let's insert a few new ones while we're at it
        for (int i = 0; i < 10; i++) {
            Agenda agenda = new Agenda();
            agenda.setName("New Agenda " + i);
            ContentProviderOperation operation = ContentProviderOperation.newInsert(AgendaProvider.AGENDA_CONTENT_URI).withValues(agenda.getContentValues()).build();
            operations.add(operation);

        }
        try {
            getContentResolver().applyBatch(AgendaProvider.AUTHORITY, operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
    
There's a couple really good blog posts about this pattern. Please chech them out:
1. The basics: https://www.grokkingandroid.com/better-performance-with-contentprovideroperation/
2. withBackReference explained (really useful): https://www.grokkingandroid.com/androids-contentprovideroperation-withbackreference-explained/


FAQ's
----------
### Why should I use a ContentProvider? I thought those were just for sharing code between applications.
Yes, the architecture and design decisions behind the ContentProvider is informed by the requirement that some data needs to be shared across application processes. However, we've found that the design also provides benefits for simply working with your own data and we've had great success using them. The biggest reason that we've found as to why more developers don't use them is simply because they are very labor intensive and really hard to get right when written by hand. This tool takes those drawbacks away. Here are a few of our top reasons for using a ContentProvider (not all of them are necessarily exclusive to ContentProviders).
1. If you do need to share code between Applications or Processes (several of the apps we've built do have multiple processes) then you're already setup.
2. Many of the Android components are designed to work with them out of the box. For example, CursorLoader takes a ContentProvider content URI and query to fetch results and automatically notify your UI of changes to backing data.
3. ContentResolver and ContentProvider are built into the system and relied upon heavily throughout Android (Contacts and Calendar are good examples) and so we benefit from a very robust and hardened system. Our generated code is quite small compared to other data abstraction mechanisms and ORM's and we feel comfort (and have had great success) relying heavily on system components rather than a custom system.
4. We like the client-service-oriented nature of the ContentResolver/ContentProvider architecture. Tables and data are accessed with URI's in much the same way you probably access yourserver-side data. We've observed improvements in the overall application architecture in our apps by communicating between components this way.

### What are the current limitations of your tool?
Currently we only generate code that supports querying one-to-many joined table relationships. Our generated code does not allow you to insert/update joined records as a single insert/update. For example if you queried for the join between person and phone numbers you cannot send a request to insert/update to this joined relationship. You must send your insert/update requests directly to the tables they belong to. The generated code also does not currently support many-to-many join operations. These features are on the roadmap as we feel they are important for a complete solution. However, we've built this tool according to what has been needful on our applications and the nature of mobile applications predisposes our UI's to generally only consume data coming from single tables: lists of friends, lists of documents, list of tweets. Very few data presentations problems are solved more optimally than two queries: query for and show a list of people and then query for that person's contact details when the user taps on their record.



