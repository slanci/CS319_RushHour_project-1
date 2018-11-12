package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

public class How_To extends Pane {
    //Constants
    private final String SOUND_ICON = "soundIcon.png";
    private final String SETTINGS_ICON = "Settings-icon.png";
    private final String BACK_ICON = "backIcon.png";
    private final String TUTORIAL_IMAGE = "tutorialImage.jpg";
    private final String COPYRIGHT_LABEL = "Developed by Royal Flush";
    private final double WIDTH  = 1920;
    private final double HEIGHT = 1080;
    private final double COPYRIGHT_PANEL_SIZE = 60;
    private final int ICON_SIZE = 64;

    //Variables
    int tutorialCount;
    //Labels
    private Label copyRightLabel;
    private Pane copyRightPanel;
    private Label version;

    //Buttons
    private Button soundButton;
    private Button settingsButton;
    private Button backButton;
    private Button[] pagePassButtons;

    //Images
    private Image soundImage;
    private Image settingsImage;
    private Image backImage;
    private Image[] tutorialImage;
    private ImageView tutorial;
    private Image[] pagePassImages;

    public How_To() {
        super();
        initialize();
    }

    public void initialize(){

        //Creating middle panel
        this.setMinHeight(HEIGHT - COPYRIGHT_PANEL_SIZE);
        this.setMinWidth(WIDTH);

        //Creating copyright panel
        copyRightPanel = new Pane();

        //Copyright label
        copyRightLabel = new Label(COPYRIGHT_LABEL);

        //Version label
        version = new Label("Version b0.1");

        //How_To Panel
        //Sound button
        soundImage = new Image(getClass().getResourceAsStream(SOUND_ICON));
        soundButton = new Button();
        soundButton.setGraphic(new ImageView(soundImage));
        //soundButton.setStyle("-fx-background-color: transparent");
        soundButton.setMinSize(ICON_SIZE, ICON_SIZE);
        soundButton.setLayoutX(WIDTH - 100);
        soundButton.setLayoutY(35);

        //Settings button
        settingsImage = new Image(getClass().getResourceAsStream(SETTINGS_ICON));
        settingsButton = new Button();
        settingsButton.setGraphic(new ImageView(settingsImage));
        //settingsButton.setStyle("-fx-background-color: transparent");
        settingsButton.setMinSize(ICON_SIZE, ICON_SIZE);
        settingsButton.setLayoutX(WIDTH - 200);
        settingsButton.setLayoutY(35);

        //Back button
        backImage = new Image(getClass().getResourceAsStream(BACK_ICON));
        backButton = new Button();
        backButton.setGraphic(new ImageView(backImage));
        //backButton.setStyle("-fx-background-color: transparent");
        backButton.setMinSize(ICON_SIZE, ICON_SIZE);
        backButton.setLayoutX(25);
        backButton.setLayoutY(35);

        //Tutorial Images
        tutorialCount = 1;
        tutorialImage = new Image[tutorialCount];
        tutorialImage[0] = new Image(getClass().getResourceAsStream(TUTORIAL_IMAGE));
        tutorial = new ImageView(tutorialImage[0]);
        tutorial.setLayoutX(WIDTH / 2 - 275);
        tutorial.setLayoutY(HEIGHT / 2 - 200);

        //Next and back button for tutorial images
        pagePassButtons = new Button[2];
        pagePassImages = new Image[2];
        pagePassImages[0] = new Image(getClass().getResourceAsStream("play-symbol.png"));
        pagePassImages[1] = new Image(getClass().getResourceAsStream("left-angle-bracket.png"));
        for ( int i = 0; i < 2; i++){
            pagePassButtons[i] = new Button();
            pagePassButtons[i].setMinSize(ICON_SIZE, ICON_SIZE);
            pagePassButtons[i].setGraphic(new ImageView(pagePassImages[i]));
            //backButton[i].setStyle("-fx-background-color: transparent");
            pagePassButtons[i].setLayoutY(HEIGHT / 2 - 75);

        }
        pagePassButtons[0].setLayoutX(WIDTH - 150);
        pagePassButtons[1].setLayoutX(75);

        //Adding buttons to middle panel
        this.getChildren().addAll(soundButton, settingsButton, backButton, tutorial, pagePassButtons[0], pagePassButtons[1]);
        //How_To Panel ends

        this.setCurrentColor(null);
        copyRightPanel.getChildren().add(copyRightLabel);
        copyRightPanel.getChildren().add(version);

        //how_to.updateMiddlePanel(howToPanel);
    }

    public void setCurrentColor(String colorCSS){

        if (colorCSS == null){
            this.setStyle("-fx-background-color: lightblue;");
        }else{
            this.setStyle(colorCSS);
        }
    }
}
