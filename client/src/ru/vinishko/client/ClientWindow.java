package ru.vinishko.client;

import com.jfoenix.controls.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.vinishko.network.Message;
import ru.vinishko.network.TCPConnection;
import ru.vinishko.network.TCPConnectionListener;
import ru.vinishko.network.User;


import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

public class ClientWindow extends Application  implements TCPConnectionListener, Initializable {


        private static final String CKEY="mvLBiZsiTbGwrfJB";


        private TCPConnection connection;
        private static final String IP_ADDR = "localhost";
        private static final int PORT = 8189;
        private static String login_name;
        private boolean person_in = false;

        @FXML
        private JFXButton but = new JFXButton();
        @FXML
        private JFXButton enter_in_system = new JFXButton();
        @FXML
        private JFXButton reg_in_system = new JFXButton();
        @FXML
        private JFXTextField typeMSG = new JFXTextField();
       // @FXML
        // private JFXTextArea listf = new JFXTextArea();
        @FXML
        private JFXTextField login = new JFXTextField();
        @FXML
        private JFXTextField login_show = new JFXTextField();
        @FXML
        private JFXPasswordField password = new JFXPasswordField();
        @FXML
        private JFXListView<String> listView = new JFXListView<>();

        ObservableList<String> data;


        public static void main(String[] args) {
                launch(args);


                Platform.runLater(new Runnable() {
                                          public void run() {
                                                  new ClientWindow();
                                          }
                                  }
                );

        }


        @Override
        public void start(Stage primaryStage) throws Exception{
                Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
                primaryStage.setTitle("Messenger Vinishko");
                primaryStage.setScene(new Scene(root, 900, 500));
                primaryStage.show();




        }





        @FXML
        public void inSystem(ActionEvent actionEvent) {
            connection.send(new User(login.getText(),password.getText()));
        }


        public void inSystem(User user) {

                login_name = user.getName();

                if(!user.isLogined()||user.notFound()){
                        person_in = false;
                        typeMSG.setText("Неверный логин или пароль");
                }
                else{
                        person_in = true;
                        login_show.setText("Вы вошли как " + login_name);
                        //printMsg("Пользователь " + login_name + " присоединился к диалогу!");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            connection.sendString("Пользователь " + login_name + " присоединился к диалогу!");
                            typeMSG.clear();
                        }
                    });

                    login.setText(null);
                    password.setText(null);
                    login.setVisible(false);
                    password.setVisible(false);
                    enter_in_system.setVisible(false);
                    reg_in_system.setVisible(false);


                }



        }

        @FXML
        public void reg(ActionEvent actionEvent){
            connection.send(new User(login.getText(),password.getText(),true));
        }

        public void reg(User user){
            if(!user.notFound()&&user.isReg()){
                typeMSG.setText("Пользоваетль с таким именем существует");
            } else {
                typeMSG.setText("Регистрация прошла успешно");
                reg_in_system.setDisable(true);
            }
        }


        @FXML
        public void clc(ActionEvent actionEvent) {

                Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                if(person_in == true) {
                                        Message msg = new Message(login_name, typeMSG.getText());
                                        System.out.println(msg);
                                        if (msg.equals("")) return;
                                        typeMSG.setText(null);
                                        connection.send(msg);
                                }
                                else{
                                        typeMSG.setText("Вы в режиме гостя. Для отправки сообщенй войдите в систему.");
                                }
                        }
                });

        }



@FXML
public void changeFocus(){
            password.requestFocus();
}




        @Override
        public void onConnectionReady(TCPConnection tcpConnection) {
               // printMsg("Connection ready...");
        }

        @Override
        public void onReceiveString(TCPConnection tcpConnection, Object value) {
                Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(value.getClass().equals(String.class)){
                                    //printMsg(AESCipher.decrypt(value.toString(),CKEY));
                                printMsg(value.toString());
                            }
                            if(value.getClass().equals(Message.class)){
                                //printMsg(AESCipher.decrypt(value.toString(),CKEY));
                                Message v=(Message) value;
                                printMsg(v.getName()+": "+v.getMsg());
                            }

                            if(value.getClass().equals(User.class)){
                                User user=(User) value;
                                if(user.isReg()){
                                    reg(user);
                                } else inSystem(user);
                            }


                        }
                });

        }

        @Override
        public void onDisconnect(TCPConnection tcpConnection) {
                printMsg("Connection close");
        }

        @Override
        public void onException(TCPConnection tcpConnection, Exception e) {
                printMsg("Connection exception: " + e);
        }

        public void pressButton(ActionEvent actionEvent) {
                System.out.println("hi");
        }

        private synchronized void printMsg(String msg) {

               // listf.appendText("\n" + msg);
                data.add(msg);



        }


        @Override
        public void initialize(URL location, ResourceBundle resources) {
                try {
                        connection = new TCPConnection(this, IP_ADDR, PORT);
                } catch (IOException e) {
                        printMsg("Connection exception: " + e);
                        System.out.println("Connection exception: " + e);

                }

                data = FXCollections.observableArrayList();
                listView.setItems(data);


        }


}
