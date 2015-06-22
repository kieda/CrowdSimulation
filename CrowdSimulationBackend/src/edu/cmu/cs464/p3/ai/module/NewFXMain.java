/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.cs464.p3.ai.module;

import edu.cmu.cs464.p3.modulelang.PropertiesModuleFactory;
import static edu.cmu.cs464.p3.modulelang.PropertiesModuleFactory.ModuleConstructor;
import edu.cmu.cs464.p3.modulelang.parser.ParseProgram;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author zkieda
 */
public class NewFXMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        
        try{
            String modules = 
                "import "
                    + "javafx.scene.* "
                    + "javafx.scene.layout.* "
                    + "javafx.scene.control.* "
                + "end\n" 
                + "FlowPane : has "
                    + " Button "
                    + " Button "
                    + " Button "
                + "end";
            PropertiesModuleFactory<Node> n = new PropertiesModuleFactory<>();
            ModuleConstructor<Node> cc = n.build();
            Pane node = (Pane)cc.construct(ParseProgram.parseAndLink(new ByteArrayInputStream(modules.getBytes())));
            Scene scene = new Scene(node, 300, 250);

            primaryStage.setTitle("Hello World!");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    static void moo(Object o){
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
