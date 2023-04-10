package com.creativityfactory.swiftserver.app;

/**
 * The framework client could map this class in web.xml file with a path should be ended with *
 */
public class ApplicationRunner extends Application{
    @Override
    protected void execute() throws Exception {
        generateRest();
    }
}
