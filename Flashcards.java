package flashcards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.stage.Stage;
import javafx.util.Duration;



public class Flashcards extends Application {     
         
    
    private Tile selected = null;   
    
    private int clickCount = 2;

    public Parent createContent(int n, int x, int escalaX, int escalaY) { 
        Image [] imagenes = new Image[n];
       
        Pane root = new Pane();
        root.setPrefSize(800, 450);
        root.setTranslateX(escalaX);
        root.setTranslateY(escalaY);
                               
        //LLENAMOS EL ARRAY CON IMAGENES
        for (int i = 0; i < n; i++){                        
            imagenes[i] = new Image(getClass().getResourceAsStream(i+".jpg"));
        }
        
        
        List<Tile> tiles = new ArrayList<>();
        
        //AQUI HACE DOS ADD PARA METER DOS LETRAS, IGUALES PARA FORMAR UNA PAREJA, LUEGO HACE
        //C++ PARA PASAR A LA SIGUIENTE LETRA, EN EL PRIMER CASO A LA B.
        
        for (int i = 0; i < n; i++) {
            tiles.add(new Tile(imagenes[i]));
            tiles.add(new Tile(imagenes[i]));            
        }
        
        //BARAJA LAS LETRAS PARA QUE NO ESTEN POSICIONADAS SEGUIDAS
        Collections.shuffle(tiles);
        
        //COLOCA LAS LETRAS EN SUS CASILLAS

        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            tile.setTranslateX(130 * (i % x));
            tile.setTranslateY(155 * (i / x));
            root.getChildren().add(tile);
        }

        return root;
    }
    
    //EMPIEZA    
    Scene scene1;  
    
    @FXML
    private Button botonEasy; 
    @FXML
    private Button botonMedium;       
    @FXML
    private Button botonHard; 
   
    
    @Override
    public void start(Stage primaryStage) throws Exception {         
              
        Parent root = FXMLLoader.load(getClass().getResource("flashcards.fxml"));        
        Scene scene = new Scene(root);        
        primaryStage.setScene(scene);
        primaryStage.show();          
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;       
        Scene scene;
        
        if(event.getSource()== botonEasy){              
            stage = (Stage) botonEasy.getScene().getWindow();  
            scene = new Scene(createContent(6, 4, 450, 150));  
            scene.setFill(Color.WHITESMOKE);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show(); 
        }   
        if(event.getSource()== botonMedium){             
            stage = (Stage) botonEasy.getScene().getWindow();  
            scene = new Scene(createContent(12,8, 200, 150));
            scene.setFill(Color.WHITESMOKE);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show(); 
        }
        if (event.getSource() == botonHard){            
            stage  = (Stage) botonEasy.getScene().getWindow();  
            scene = new Scene(createContent(25,10, 75, 25));
            scene.setFill(Color.WHITESMOKE);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show(); 
        }
              
    }     

    private class Tile extends StackPane {
        
        ImageView text = new ImageView(); 
        

        public Tile(Image value) {
            
            //CREA LA PANTALLA         
            
            Rectangle border = new Rectangle(125, 150);
            border.setFill(null);
            border.setStroke(Color.BLACK);
            text = new ImageView(value);
            
            

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(this::handleMouseClick);
            close();
        }

        
        public void handleMouseClick(MouseEvent event) {
            //AQUI COMPRUEBA QUE SI ESA CASILLA ESTA DESCUBIERTA O YA SE HAN DADO DOS CLICKS, NO DEVUELVA NADA
            if (isOpen() || clickCount == 0)
                return;
            clickCount--;
            
            //SI LA CASILLA NO ESTA DESCUBIERTA, LA MUESTRA Y LA GUARDA
            
            if (selected == null) {                
                selected = this;
                open(() -> {});
            }
            
            //SI CLICA UNA SEGUNDA VEZ EN OTRA CASILLA, COMPARA LA LETRA RECIBIDA CON LA SELECCIONADA.
            else {
                //SI SON DIFERENTES, LAS ESCONDE NUEVAMENTE Y EL VALOR DE LA AUXILIAR SELECTED VUELVE A SER NADA
                //Y EL CONTADOR DE CLICS VUELVE A VALER 2
                
                open(() -> {
                    if (!hasSameValue(this)) { 
                        selected.close();
                        this.close();
                    }
                    selected = null;
                    clickCount = 2;
                });
            }
        }
        
        //AQUI DETERMINAMOS LA OPACIDAD PARA DESCUBRIR LA LETRA.
        
        public boolean isOpen() {
            return text.getOpacity() == 1;
        }

        public void open(Runnable action) {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
            ft.setToValue(1);
            ft.setOnFinished(e -> action.run());
            ft.play();
        }
        
        //VUELVE A ESCONDER LA LETRA CON LA OPACIDAD AL 0
        public void close() {
            //FADETRANSITION PARA DARLE EFECTO A LA DESAPARICION DE LA LETRA
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
            ft.setToValue(0);
            ft.play();
        }
        //COMPARA LAS DOS LETRAS PARA SABER SI SON IGUALES
        public boolean hasSameValue(Tile other) {            
            return selected.text.getImage().equals(other.text.getImage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}