import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    private static String USERNAME = "gwyn.lockett@guardian.co.uk";
    private static String PWD = "xcxrveyzxnywqdvq";
    private static String PREVIEW_URL;
    private static final String ENVIRONMENT_URL = "http://viewer.gutools.co.uk/preview/";
    //private static final String MAPI_URL = "x-gu://preview.mobile-apps.guardianapis.com/items/";
    private static final String MAPI_URL = "https://entry.mobile-apps.guardianapis.com/deeplink/items/";

    Stage window;
    Text txAppInstructions;
    Text txAppTitle;
    Label lbComposerURL;
    Label lbEmail;
    Label lbErrorMsg;
    TextField tfComposerURL;
    TextField tfEmail;
    Button btnSend;
    Button btnExit;
    Image logo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new BorderPane();

        Scene scene = new Scene(root, 700, 300, Color.WHITE);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("style.css");
        primaryStage.show();

        //Set gridpane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints(130);
        ColumnConstraints col2 = new ColumnConstraints(100, 300, 700);
        col2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(col1, col2);


        //Defining elements
        txAppInstructions = new Text("Please enter the composerULR and the Email address where you wish to send the " +
                "link to.");
        lbComposerURL = new Label("Composer URL:");
        lbEmail = new Label("Email address");
        lbErrorMsg = new Label("");
        lbErrorMsg.setStyle("-fx-text-fill: red;");

        tfComposerURL = new TextField();
        tfComposerURL.setPromptText("Enter the composerURL.");
        tfComposerURL.setPrefColumnCount(10);
        tfComposerURL.getText();

        tfEmail = new TextField();
        tfEmail.setPromptText("Enter the email address.");
        tfEmail.setPrefColumnCount(10);
        tfEmail.getText();

        //Defining the buttons
        btnExit = new Button();
        btnExit.setText("Quit the app");
        btnExit.getStyleClass().add("button-Exit");

        //Set action for the Exit button
        btnExit.setOnAction(e -> {
            window.close();
        });


        btnSend = new Button();
        btnSend.setText("Send the link");

        //Set action for the GetPreview button
        btnSend.setOnAction(e -> {

            //When the button is clicked do this:
            String url = tfComposerURL.getText();
            String sendTo = tfEmail.getText();

            if (valdateFields() & valdateEmail()) {

                try {
                    getPreviewURL(url);
                    send(sendTo);
                    clearTextFields();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        //Add elements to gridpane
        GridPane.setHalignment(txAppInstructions, HPos.CENTER);
        gridPane.add(txAppInstructions, 0, 0);
        GridPane.setColumnSpan(txAppInstructions, 2);

        GridPane.setHalignment(lbComposerURL, HPos.RIGHT);
        gridPane.add(lbComposerURL, 0, 1);

        GridPane.setHalignment(lbEmail, HPos.RIGHT);
        gridPane.add(lbEmail, 0, 2);

        GridPane.setHalignment(tfComposerURL, HPos.LEFT);
        gridPane.add(tfComposerURL, 1, 1);

        GridPane.setHalignment(tfEmail, HPos.LEFT);
        gridPane.add(tfEmail, 1, 2);

        GridPane.setHalignment(lbErrorMsg, HPos.CENTER);
        gridPane.add(lbErrorMsg, 0, 3);
        GridPane.setColumnSpan(lbErrorMsg, 2);

        //Set horizontal box for buttons
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(50);   // Gap between nodes
        hbox.getChildren().addAll(btnSend, btnExit);

        //Defining header
        FlowPane header = new FlowPane();
        header.setPrefHeight(50);
        header.getStyleClass().add("header");

        logo = new Image("icon.png");
        ImageView img = new ImageView();
        img.setImage(logo);
        img.setFitWidth(40);
        img.setFitHeight(40);

        txAppTitle = new Text("Mobile Apps - PreviewURL Generator");
        txAppTitle.setStyle("-fx-text-fill: darkblue;");

        header.getChildren().addAll(img, txAppTitle);

        root.setTop(header);
        root.setCenter(gridPane);
        root.setBottom(hbox);

        //Set main window
        primaryStage.setTitle("Composer Preview");

    }

    public void clearTextFields() {
        tfComposerURL.clear();
        tfEmail.clear();
    }

    public static String getPreviewURL(String composerPreview_URL) {

        //Create the PREVIEW_URL
        PREVIEW_URL = composerPreview_URL.replace(ENVIRONMENT_URL, MAPI_URL);

        return PREVIEW_URL;
    }

    public static void send(String emailAddress) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PWD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailAddress));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailAddress));
            message.setSubject("Mobile App - Preview Link");

            message.setContent("<p>Please click the link to launch in the app and follow the on-board authentication " +
                    "prompts.</p><p>(Note: You must be signed in with a Guardian Email address)</p> " +
                    "<a href=//" + PREVIEW_URL + ">" + PREVIEW_URL + "</a>", "text/html");
            Transport.send(message);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success - ");
            alert.setHeaderText(null);
            alert.setContentText("Success - Your Email has been sent");
            alert.showAndWait();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean valdateFields() {
        if (tfComposerURL.getText().isEmpty() | tfEmail.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both the tfComposerURL and the Email address");
            alert.showAndWait();

            return false;

        }
        return true;
    }

    private boolean valdateEmail() {
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m = p.matcher(tfEmail.getText());
        if (m.find() && m.group().equals(tfEmail.getText())) {
            return true;
        } else

        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Email");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid tfEmail address");
            alert.showAndWait();

            return false;
        }

    }
}


