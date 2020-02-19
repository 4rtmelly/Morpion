

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javax.swing.JOptionPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;

import java.lang.Object;
import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Dialog<ButtonType>;
import javafx.scene.control.Alert;
import java.util.*;


public class Toe extends Application{

    /*
    Initialisation
    */

    private boolean turnX = true;
    private boolean jouable = true;
    private List<Combo> combos = new ArrayList<>();
    private Tuile [][] board = new Tuile [3][3];

    /*
    Override la méthode Start dans la classe Application
    */

    @Override
    public void start(Stage primaryStage) {
    
    /*
    Nos 2 boutons de Sortie et Rejouer 
    */

    Button sortie = new Button("Sortie");
    Button rejouer = new Button("Rejouer?");

    /*
    On les colore et adapte leur taille
    */

    sortie.setStyle("-fx-font-size: 18pt; -fx-background-color: #66CDAA; -fx-border-color: #FFDEAD; -fx-border-width: 4px;");
    rejouer.setStyle("-fx-font-size: 18pt;-fx-background-color: #F08080; -fx-border-color: #6B8E23; -fx-border-width: 4px;");
    
    /*
    Attribution de leur fonction ->Sortie = permet de fermer toutes les fenetres du jeu  ->Rejouer = Crée une nouvelle fenetre de jeu
    */

    sortie.setOnAction(new EventHandler<ActionEvent>()
    {
         @Override 
         public void handle(ActionEvent e) {
             Platform.exit();
            }
    });

    rejouer.setOnAction( __ ->
    {
      primaryStage.close();
      Platform.runLater(() -> new Toe().start(new Stage()));
    
   
      primaryStage.show();
  });
 


    /*
    Création de notre fenetre Pane de taille 600x600 qui contient les tuiles 
    */

    Pane root = new Pane();
    root.setPrefSize(600,600);

        for (int i=0; i<3; i++){  
            for (int j=0; j<3; j++){ // j pour X donc en premier  
                Tuile tuile = new Tuile();
                tuile.setTranslateX(j*200); // pour la ligne  
                tuile.setTranslateY(i*200); // pour la colonne
                root.getChildren().add(tuile); // on ajoute la tuile au groupe root qui est notre fenetre 
                board[j][i] = tuile; // on assigne des élements particulier à la liste 
            }
        }

        // combo à la verticale
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        // combo à la horizontale
        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }
        
        // combo à la diagonale
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2])); 

    /*
    On ajoute a borderPane nos boutons Rejouer et Sortie en haut de la fenetre à l'aide de Hbox pour pouvoir les placer à l'horizontale 
    et au centre on met le root qui contient toutes les tuiles donc notre grille de jeu. 
    */

    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(root);
    
    
    HBox buttonBar = new HBox();
    buttonBar.setSpacing(350.0);
    buttonBar.getChildren().addAll(rejouer,sortie);

    borderPane.setTop(buttonBar);
   
    
    /*
    Pour ajouter une icone a la fenetre du jeu
    */

    Image icon = new Image(getClass().getResourceAsStream("morpion.jpg")); 
    primaryStage.getIcons().add(icon);

    /*
    On nomme notre fenetre, crée notre scene et la montre
    */


    Scene scene = new Scene(borderPane, 680, 680);
    primaryStage.setTitle("TicTacToe"); 
    primaryStage.setScene(scene); 
    primaryStage.show();    

    

  }



    /*
    Pour verifier si l'etat du jeu est complet, c'est à dire si il y a un gagnant
    */

    private void Etat_jeu() {
        for (Combo combo : combos) {
            if (combo.EstComplet()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Felicitation, vous avez gagne !");

                alert.showAndWait();
                jouable = false;
                break;
            }
        }
    }

    /*
    On crée une classe qui pourra verifier les états des combos, si ils sont bien remplis ou non
    */

    private class Combo{  // 1 combo = 3 tuiles 
        private Tuile [] tuiles; // la structure Tuile a 3 tuiles donc une liste de tuile 
        public Combo(Tuile... tuiles){ // constructeur 
            this.tuiles = tuiles; // on met la tuile dans le tableau
        } 

        public boolean EstComplet(){
            if (tuiles[0].getValue().isEmpty()) // si la première tuile de la liste tuile est vide on retourne faux donc pas complet 
                return false;
            return tuiles[0].getValue().equals(tuiles[1].getValue()) && tuiles[0].getValue().equals(tuiles[2].getValue());
        }
    }


    /*
    On crée la classe Tuile qui héritent de STACKPANE c'est un type de fenetre qui permet d'empiler les élements les uns sur les autres
    dans la classe on crée un objet rectangle qui va permettre de créer chaque rectangle de notre jeu afin d'obtenir une grille  
    */

    private class Tuile extends StackPane{
        public Tuile(){
            Rectangle rectangle = new Rectangle(200,200);
            rectangle.setFill(null);// pour mettre en blanc 
            // reglage de la couleur de la bordure et de son épaisseur  
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(4);
            setAlignment(Pos.CENTER);
            
            // on ajoute le tout au rectangle des modifs donc le rectangle et le text car c'est dans les tuiles qu'il y a ces modifs

            /* On crée un fond de la grille à l'aide d'une image qu'on ajoute dans la classe Tuile */ 
            Image background = new Image("background1.jpg");
            ImageView mv = new ImageView(background);
            
            // on ajoute tous les nouveaux objets crée donc l'image de fond , le rectangle et le text 
            getChildren().addAll(mv,rectangle,text);
           
            /* on crée un évenement selon le click de la souris
            qui lors du click sur la tuile va permettre de dessiner un X ou O en fonction de la situation du jeu
            */
            setOnMouseClicked(event -> {
                if (jouable){
                    if(event.getButton() == MouseButton.PRIMARY){
                        if(!turnX)
                            return;
                        dessinerX();
                        turnX = false;
                        Etat_jeu();
                        }    
                    else if (event.getButton() == MouseButton.SECONDARY){
                        if (turnX)
                            return; 
                        dessinerO();
                        turnX = true;
                        Etat_jeu();
                    }
                }

            });
    

             text.setFont(Font.font(70)); // taille du texte autrement dit de X et O qu'on agrandit 
             
        }

        /*
        On récupère valeur du texte soit X soit O 
        On dessine le X ou O à l'aide du texte 
        */

        public String getValue(){
            return text.getText();
        }

            private Text text = new Text(); 

            private void dessinerX(){
                text.setText("X");
                text.setFill(Color.PINK); 

            }

            private void dessinerO(){
                text.setText("O");
                text.setFill(Color.GREEN);
            }
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}

