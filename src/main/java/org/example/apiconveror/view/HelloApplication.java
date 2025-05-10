package org.example.apiconveror.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import org.example.apiconveror.model.Divisa;
import org.example.apiconveror.model.TipoDivisa;
import org.example.apiconveror.service.ExchangeRateApiService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


public class HelloApplication extends Application {

    private static String resultado = "0";
    private String amountStatic;

    private final ObservableList<Divisa> data = FXCollections.observableArrayList();




    @Override
    public void start(Stage primaryStage) {




        primaryStage.setTitle("Conversor de Monedas");

        //Background image
        Image backgroundImage = new Image(getClass().getResource("/static/background.jpg").toExternalForm());

        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1060);
        backgroundImageView.setFitHeight(700);
        backgroundImageView.setPreserveRatio(false);


        //Encabezado


        Label titleLabel = new Label("Conversor de Divisa");
        titleLabel.setFont(Font.font("Arail", FontWeight.BOLD, 36));
        Label subTitle = new Label("Consulte los tipos de cambio de divisas en tiempo real");
        subTitle.setFont(Font.font("Arial", FontPosture.REGULAR, 18));

        VBox headerBox = new VBox(10, titleLabel, subTitle);
        headerBox.setAlignment(Pos.CENTER);


        //Contenido Conversion
        TextField amountTextField = new TextField(this.amountStatic);
        amountTextField.setPrefWidth(100);

        //ObservableList<String> divisas = FXCollections.observableArrayList("USD", "EUR", "GBP", "COP");
        ComboBox<TipoDivisa> comboBox1 = new ComboBox<>(
                FXCollections.observableArrayList(TipoDivisa.values())
        );
        comboBox1.setValue(TipoDivisa.COP);

        ComboBox<TipoDivisa> comboBox2 = new ComboBox<>(
                FXCollections.observableArrayList(TipoDivisa.values())
        );
        comboBox2.setValue(TipoDivisa.USD);

        Label resultField = new Label("-");
        resultField.setFont(Font.font("Arial", FontWeight.BOLD, 24));



        Label rateLabel = new Label(String.format("Your rate:\n" +
                        "%s %s = %s %s", amountStatic, comboBox1.getValue(),
                resultado,
                comboBox2.getValue()));

        rateLabel.setFont(Font.font("Arial", 14));

        Button swapButton = new Button("⇄");
        swapButton.setPrefSize(40, 40);

        Button checkButton = new Button("Consultar");
        checkButton.getStyleClass().add("button-modern");

        // Contenedor del formulario
        HBox row1 = new HBox(10, amountTextField, comboBox1, swapButton, resultField, comboBox2);
        row1.setAlignment(Pos.CENTER);
        VBox.setMargin(row1, new Insets(10));


        //Contenido Table

        TableView<Divisa> tblHistorialDivisa = new TableView<>();
        tblHistorialDivisa.setPadding(new Insets(10));

        TableColumn<Divisa, String> idCol = new TableColumn<>("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Divisa, TipoDivisa> base_codeCol = new TableColumn<>("Pais Origen");
        base_codeCol.setCellValueFactory(new PropertyValueFactory<>("base_code"));

        TableColumn<Divisa, Double> monedaOrigenCol = new TableColumn<>("Moneda Origen");
        monedaOrigenCol.setCellValueFactory(new PropertyValueFactory<>("monedaOrigen"));

        TableColumn<Divisa, TipoDivisa> paisConverCol = new TableColumn<>("Pais Final");
        paisConverCol.setCellValueFactory(new PropertyValueFactory<>("paisConver"));

        TableColumn<Divisa, Double> monedaFinalCol = new TableColumn<>("Moneda Origen");
        monedaFinalCol.setCellValueFactory(new PropertyValueFactory<>("monedaFinal"));

        TableColumn<Divisa, LocalDateTime> fechaConsultaCol = new TableColumn<>("Fecha de Consulta");
        fechaConsultaCol.setCellValueFactory(new PropertyValueFactory<>("fechaConsulta"));


        tblHistorialDivisa.getColumns().addAll(idCol, base_codeCol,
                monedaOrigenCol, paisConverCol, monedaFinalCol, fechaConsultaCol);

        data.addAll(Divisa.getHistorial());
        tblHistorialDivisa.setItems(data);

        tblHistorialDivisa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Contenedor Table


        VBox conversionBox = new VBox(15, new Label("Monto"), row1, rateLabel, checkButton);
        conversionBox.setAlignment(Pos.CENTER);
        conversionBox.getStyleClass().add("conversion-box");

        checkButton.setOnAction(new EventHandler<ActionEvent>() {

            ExchangeRateApiService api = new ExchangeRateApiService();



            @Override
            public void handle(ActionEvent actionEvent) {
                data.clear();

                try {
                    amountStatic = amountTextField.getText();
                    System.out.println(Double.valueOf(amountStatic));
                    List<Divisa> listaTry = api.convertir(comboBox1.getValue().toString(), comboBox2.getValue().toString(),Double.valueOf(amountStatic));

                    Double result = listaTry.getFirst().getMonedaFinal();

                    resultField.setText(String.format("%.2f", result));

                    rateLabel.setText(String.format("Your rate:\n" +
                                    "%s %s = %s %s", amountStatic, comboBox1.getValue(),
                            String.valueOf(result),
                            comboBox2.getValue()));

                    /*ObservableList<Divisa> datos = FXCollections.observableArrayList(Divisa.getHistorial());
                    tblHistorialDivisa.setItems(datos);*/




                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                data.addAll(Divisa.getHistorial());  // Aquí agregamos todas las personas al ObservableList

                tblHistorialDivisa.setItems(data);

            }
        });

        VBox mainContent = new VBox(40, headerBox, conversionBox, tblHistorialDivisa);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(50));

        // Fondo con imagen detrás
        StackPane root = new StackPane(backgroundImageView, mainContent);

        Scene scene = new Scene(root, 1060, 700);
        scene.getStylesheets().add(getClass().getResource("/static/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();



        comboBox2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rateLabel.setText(String.format("Your rate:\n" +
                                "%s %s = %s %s", amountStatic, comboBox1.getValue(),
                        resultado,
                        comboBox2.getValue()));
            }
        });

        comboBox1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rateLabel.setText(String.format("Your rate:\n" +
                                "%s %s = %s %s", amountStatic, comboBox1.getValue(),
                        resultado,
                        comboBox2.getValue()));
            }
        });

    }


    public static void main(String[] args) {
        launch();
    }
}