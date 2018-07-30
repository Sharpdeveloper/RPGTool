package RPGTool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static int num = -4;

    private TradeController controller;
    private ObservableList<StockListItem> dataItems;

    @FXML
    private ComboBox<String> comboBoxCities;

    @FXML
    private ComboBox<String> comboBoxPlayer;

    @FXML
    private ToggleGroup toggleGroupAmount;

    @FXML
    private TableView tableViewItems;

    @FXML
    private TableColumn tableColumnPurchase;

    @FXML
    private TableColumn tableColumnPurchaseAmount;

    @FXML
    private TableColumn tableColumnPurchasePrice;

    @FXML
    private TableColumn tableColumnItem;

    @FXML
    private TableColumn tableColumnSalePrice;

    @FXML
    private TableColumn tableColumnSaleAmount;

    @FXML
    private TableColumn tableColumnSale;

    @FXML
    private Label labelGold;

    @FXML
    private Button buttonProduce;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        controller = new TradeController();
        if(controller.getFirstStart())
        {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("First Start Notification");
            a.setHeaderText("You will need Cities, Player and Goods to use this tool.\nThose have to be created in a text editor.");
            a.setContentText("You can see examples here:\n\nfile://" + controller.getPath() + "\n\nYour changes will be effective after restart.\n\nDo you want to open the directory now?");
            Optional<ButtonType> result =  a.showAndWait();
            if(result.get() == ButtonType.OK){
                controller.openDirectory(controller.getPath());
            }
        }
        controller.readAllGoods();
        controller.readAllPlayer();
        controller.readAllCities();

        for (City c: controller.getCities())
        {
            comboBoxCities.getItems().add(c.getName());
        }

        comboBoxCities.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(comboBoxCities.getValue() != null){
                setCity(comboBoxCities.getValue());
            }
        });

        for (Player p: controller.getLoadedPlayer())
        {
            comboBoxPlayer.getItems().add(p.getName());
        }

        comboBoxPlayer.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(comboBoxPlayer.getValue() != null){
                setPlayer(comboBoxPlayer.getValue());
            }
        });

        toggleGroupAmount.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(toggleGroupAmount.getSelectedToggle() != null) {
                controller.setAmount(Integer.parseInt(toggleGroupAmount.getSelectedToggle().getUserData().toString()));
                refreshTableViewItems();
            }
        });

        tableColumnPurchase.setCellFactory(new Callback<TableColumn<StockListItem, String>, TableCell<StockListItem, String>>() {

            @Override
            public TableCell<StockListItem, String> call(TableColumn<StockListItem, String> p) {
                return new ButtonCell("Buy");
            }

        });
        tableColumnPurchaseAmount.setCellValueFactory(new PropertyValueFactory<StockListItem,Integer>("amountPurchase"));
        tableColumnPurchasePrice.setCellValueFactory(new PropertyValueFactory<StockListItem,Integer>("pricePurchase"));
        tableColumnItem.setCellValueFactory(new PropertyValueFactory<StockListItem,String>("name"));
        tableColumnSalePrice.setCellValueFactory(new PropertyValueFactory<StockListItem,Integer>("priceSale"));
        tableColumnSaleAmount.setCellValueFactory(new PropertyValueFactory<StockListItem,Integer>("amountSale"));
        tableColumnSale.setCellFactory(new Callback<TableColumn<StockListItem, String>, TableCell<StockListItem, String>>() {

            @Override
            public TableCell<StockListItem, String> call(TableColumn<StockListItem, String> p) {
                return new ButtonCell("Sell");
            }

        });

        refreshTableViewItems();

        buttonProduce.setOnAction(event -> {
            controller.produce();
            refreshTableViewItems();
        });
    }

    private void refreshTableViewItems() {
        if (dataItems != null) {
            dataItems.remove(0, controller.getItems().size());
        }

        controller.refreshItems();

        dataItems = FXCollections.observableArrayList();
        for (StockListItem s : controller.getItems()) {
            dataItems.add(s);
        }

        tableViewItems.setItems(dataItems);

        if (controller.getSelectedPlayer() >= 0) {
            labelGold.setText(String.valueOf(controller.getLoadedPlayer().get(controller.getSelectedPlayer()).getMoney()) + " Coins");
        }
    }

    private void setPlayer(String playerName){
        int a = Player.getIndexOfPlayer(controller.getLoadedPlayer(), playerName);
        controller.setSelectedPlayerIndex(a);
        refreshTableViewItems();
    }

    private void setCity(String cityName){
        int a = City.getIndexOfCity(controller.getCities(), cityName);
        controller.setSelectedCityIndex(a);
        refreshTableViewItems();
    }

    private class ButtonCell extends TableCell<StockListItem, String> {
        private int id;
        final Button cellButton = new Button("Action");

        ButtonCell(String _text){
            id = num;
            num++;

            cellButton.setText(_text);

            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    controller.trade(id/2, id%2 == 0);
                    refreshTableViewItems();
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(String t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
        }
    }
}
