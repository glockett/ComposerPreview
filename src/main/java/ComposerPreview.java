import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class ComposerPreview extends Application {

    private static String USERNAME = "gwyn.lockett@guardian.co.uk";
    private static String PWD = "vtzqpptoodgwpoks";
    private static String PREVIEW_URL;
    private static final String ENVIRONMENT_URL = "http://viewer.code.dev-gutools.co.uk/preview/";
    private static final String MAPI_URL = "x-gu://preview.mobile-apps.guardianapis.com/items/";
    private static final String SENDER = "gumobtest@gmail.com";

    Stage window;
    Label lbAppTitle;
    Label lbAppInstructions;
    Label lbErrorMsg;
    Label lbSuccessMsg;
    TextField composerURL;
    TextField email;
    Button btnSend;
    Image logo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Composer Preview");
        window = primaryStage;

        //Defining the scene labels
        logo = new Image("icon.png");
        ImageView img = new ImageView();
        img.setImage(logo);
        img.setFitWidth(40);
        img.setFitHeight(40);

        lbAppTitle = new Label("Mobile Apps - PreviewURL Generator");
        lbAppTitle.setStyle("-fx-font-size: 30px;");
        lbAppInstructions = new Label("Please enter the composerULR and the email address you wish to send the link to");
        lbErrorMsg = new Label("");
        lbErrorMsg.setStyle("-fx-text-fill: red;");
        lbSuccessMsg = new Label("");
        lbSuccessMsg.setStyle("-fx-text-fill: green;");

        //Defining the ComposerURL text field
        composerURL = new TextField();
        composerURL.setPromptText("Enter the composerURL.");
        composerURL.setPrefColumnCount(10);
        composerURL.getText();

        //Defining the Last Name text field
        email = new TextField();
        email.setPromptText("Enter the email address.");
        email.setPrefColumnCount(10);
        email.getText();

        //Defining the buttons
        btnSend = new Button();
        btnSend.setText("Send Preview Link");
        btnSend.setOnAction(e -> {

            String url = composerURL.getText();
            String sendTo = email.getText();

            if (url.isEmpty()) {
                lbErrorMsg.setText("* Error! - Please enter the ComposerURL");
            } else if (sendTo.isEmpty()) {
                lbErrorMsg.setText("* Error! - Please enter a valid email!");
            } else if (isValidEmailAddress(sendTo)) {
                sendMail(sendTo);
            } else {
                lbErrorMsg.setText("* Error - Email is not valid!");
            }
        });

        HBox hb = new HBox();
        hb.getChildren().addAll(img, lbAppTitle, lbAppInstructions);
        hb.setSpacing(10);

        VBox vb = new VBox();
        vb.getChildren().addAll(img, lbAppTitle, lbAppInstructions, composerURL, email, lbErrorMsg, btnSend, lbSuccessMsg);
        vb.setSpacing(10);

        Scene scene = new Scene(vb, 700, 400);
        window.setScene(scene);
        scene.getStylesheets().add("style.css");
        window.show();


    }

    public static String getPreviewURL(String composerPreview_URL) {

        //Create the PREVIEW_URL
        String s = composerPreview_URL.replace(ENVIRONMENT_URL, "");

        PREVIEW_URL = MAPI_URL + s;

        return PREVIEW_URL;
    }

    public void sendMail(String emailAddress) {

        String from = "gwyn.lockett@htmail.co.uk";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PWD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailAddress));
            message.setSubject("Mobile App - Preview Link");

            message.setContent("<p>Please click the link to launch in the app and follow the on-board authentication " +
                    "prompts.</p><p>(Note: You must be signed in with a Guardian Email address)</p> " +
                    "<a href=//" + PREVIEW_URL + ">" + PREVIEW_URL + "</a>", "text/html");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}


