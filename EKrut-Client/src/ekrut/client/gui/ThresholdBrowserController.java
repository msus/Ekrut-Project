package ekrut.client.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import ekrut.client.EKrutClient;
import ekrut.client.EKrutClientUI;
import ekrut.client.managers.ClientInventoryManager;
import ekrut.entity.InventoryItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ThresholdBrowserController implements Initializable{

    @FXML
    private VBox thresholdsVbox;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		EKrutClient client = EKrutClientUI.getEkrutClient();
		ClientInventoryManager CIM = client.getClientInventoryManager();
		String currArea = client.getClientSessionManager().getUser().getArea();
		
		ArrayList<String> ekrutLocationsInArea = CIM.fetchAllEkrutLocationsByArea(currArea);
		ObservableList<Node> children = thresholdsVbox.getChildren();
		for (String ekrutLocation : ekrutLocationsInArea) {
			ArrayList<InventoryItem> inventoryItemsInEkrutLocation = CIM.fetchInventoryItemsByEkrutLocation(ekrutLocation);
			if (inventoryItemsInEkrutLocation.size() == 0)
				continue;
			int currThreshold = inventoryItemsInEkrutLocation.get(0).getItemThreshold();
			children.add(new ThresholdSingleViewController(CIM, ekrutLocation, currThreshold));
		}
	}
}