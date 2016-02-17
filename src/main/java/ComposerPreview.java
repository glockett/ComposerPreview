//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class ComposerPreview extends Application {
//
//    private static String USERNAME = "gwyn.lockett@guardian.co.uk";
//    private static String PWD = "xcxrveyzxnywqdvq";
//    private static String PREVIEW_URL;
//    private static final String ENVIRONMENT_URL = "http://viewer.gutools.co.uk/preview/";
//    //private static final String MAPI_URL = "x-gu://preview.mobile-apps.guardianapis.com/items/";
//    private static final String MAPI_URL = "https://entry.mobile-apps.guardianapis.com/deeplink/items/";
//
//    Stage window;
//    Label lbAppTitle;
//    Label lbAppInstructions;
//    Label lbErrorMsg;
//    TextField tfComposerURL;
//    TextField tfEmail;
//    Button btnSend;
//    Button btnExit;
//    Image logo;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setTitle("Composer Preview");
//        window = primaryStage;
//
//        //Defining the scene labels
//        logo = new Image("icon.png");
//        ImageView img = new ImageView();
//        img.setImage(logo);
//        img.setFitWidth(40);
//        img.setFitHeight(40);
//
//        lbAppTitle = new Label("Mobile Apps - PreviewURL Generator");
//        lbAppTitle.setStyle("-fx-font-size: 30px;");
//        lbAppInstructions = new Label("Please enter the composerULR and the tfEmail address you wish to send the link to");
//        lbErrorMsg = new Label("");
//        lbErrorMsg.setStyle("-fx-text-fill: red;");
//
//        //Defining the ComposerURL text field
//        tfComposerURL = new TextField();
//        tfComposerURL.setPromptText("Enter the tfComposerURL.");
//        tfComposerURL.setPrefColumnCount(10);
//        tfComposerURL.getText();
//
//        //Defining the Last Name text field
//        tfEmail = new TextField();
//        tfEmail.setPromptText("Enter the tfEmail address.");
//        tfEmail.setPrefColumnCount(10);
//        tfEmail.getText();
//
//        //Defining the buttons
//        btnExit = new Button();
//        btnExit.setText("Quit Preview Generator");
//        btnExit.getStyleClass().add("button-Exit");
//        //btnExit.setStyle("-fx-background-color: linear-gradient(#e56e6e,#c30505); -fx-text-fill: #383640;");
//        btnExit.setOnAction(e -> {
//            window.close();
//        });
//
//        //Defining the buttons
//        btnSend = new Button();
//        btnSend.setText("Send Preview Link");
//        btnSend.setOnAction(e -> {
//
//            //When the button is clicked do this:
//            String url = tfComposerURL.getText();
//            String sendTo = tfEmail.getText();
//
//            if (valdateFields() & valdateEmail()) {
//
//                try {
//                    getPreviewURL(url);
//                    send(sendTo);
//                    clearTextFields();
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//
//        HBox hb = new HBox();
//        hb.getChildren().addAll(img, lbAppTitle, lbAppInstructions);
//        hb.setSpacing(10);
//
//        VBox vb = new VBox();
//        vb.getChildren().addAll(img, lbAppTitle, lbAppInstructions, tfComposerURL, tfEmail, lbErrorMsg, btnSend, btnExit);
//        vb.setSpacing(10);
//
//        Scene scene = new Scene(vb, 700, 400);
//        window.setScene(scene);
//        scene.getStylesheets().add("style.css");
//        window.show();
//    }
//
//    public void clearTextFields() {
//        tfComposerURL.clear();
//        tfEmail.clear();
//    }
//
//    public static String getPreviewURL(String composerPreview_URL) {
//
//        //Create the PREVIEW_URL
//        PREVIEW_URL = composerPreview_URL.replace(ENVIRONMENT_URL, MAPI_URL);
//
//        //PREVIEW_URL = MAPI_URL + s;
//        System.out.println(PREVIEW_URL);
//
//        return PREVIEW_URL;
//    }
//
//    public static void send(String emailAddress) {
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.ssl.enable", true);
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "465");
//
//        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.smtp.socketFactory.fallback", "false");
//        props.setProperty("mail.smtp.port", "465");
//        props.setProperty("mail.smtp.socketFactory.port", "465");
//
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(USERNAME, PWD);
//                    }
//                });
//
//        try {
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(emailAddress));
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(emailAddress));
//            message.setSubject("Mobile App - Preview Link");
//
//            message.setContent("<p>Please click the link to launch in the app and follow the on-board authentication " +
//                    "prompts.</p><p>(Note: You must be signed in with a Guardian Email address)</p> " +
//                    "<a href=//" + PREVIEW_URL + ">" + PREVIEW_URL + "</a>", "text/html");
//            Transport.send(message);
//
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Success - ");
//            alert.setHeaderText(null);
//            alert.setContentText("Success - Your Email has been sent");
//            alert.showAndWait();
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    private boolean valdateFields() {
//        if (tfComposerURL.getText().isEmpty() | tfEmail.getText().isEmpty()) {
//
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Validate Fields");
//            alert.setHeaderText(null);
//            alert.setContentText("Please enter both the tfComposerURL and the Email address");
//            alert.showAndWait();
//
//            return false;
//
//        }
//        return true;
//    }
//
//    private boolean valdateEmail() {
//        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
//        Matcher m = p.matcher(tfEmail.getText());
//        if (m.find() && m.group().equals(tfEmail.getText())) {
//            return true;
//        } else
//
//        {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Validate Email");
//            alert.setHeaderText(null);
//            alert.setContentText("Please enter a valid tfEmail address");
//            alert.showAndWait();
//
//            return false;
//        }
//
//    }
//}
//
//
