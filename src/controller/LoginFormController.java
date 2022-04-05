package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginFormController {
    public TextField txtUsername;
    public TextField txtPassword;
    public AnchorPane loginContext;
    public JFXButton btnLogin;
    public Label errorMassageContext;
    public Label errorMassageContext1;

    public void openSelectItemForm(ActionEvent actionEvent) {

        /*Check correct password and user name*/
         btnLogin.setOnAction(e -> {
            String pwd = txtPassword.getText();
            String uname = txtUsername.getText();
            if(pwd.equalsIgnoreCase("9926rp")&&uname.equalsIgnoreCase("Admin")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ManageItemForm.fxml"));
                Parent parent = null;
                try {
                    parent = loader.load();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Scene scene = new Scene(parent);
                Stage window = (Stage) loginContext.getScene().getWindow();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                window.close();
            }else if(pwd.equalsIgnoreCase("9926")&&uname.equalsIgnoreCase("Cashier")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ManageCustomerForm.fxml"));
                Parent parent = null;
                try {
                    parent = loader.load();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Scene scene = new Scene(parent);
                Stage window = (Stage) loginContext.getScene().getWindow();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                window.close();
            }else if(pwd.equals("")&&uname.equals("")){
                errorMassageContext1.setText("Please enter user name.");
                errorMassageContext.setText("Please enter password.");
            } else {
                errorMassageContext.setText("Entry user name or password invalid.Try again!");
            }
        });
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/DashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) loginContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }
}
