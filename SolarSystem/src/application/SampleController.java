package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SampleController {
	@FXML private TextField text;
	@FXML private Button clickMe;
	
	private JDBCConnector connection;
	
	//Constructor instantiates new JDBCConector object
	public SampleController() {
		connection = new JDBCConnector();
		connection.connect(5432, "nmbivnex", "G5puBKB6ou3lMcrSd6npBsQxb7p42WMd");
	}
	
	public void handleClickMe(ActionEvent e)
	{

		{
			System.out.println("Så ses vi");
		}
	}
		public void changeScreen(ActionEvent e) throws IOException 
	{
		Parent newViewParent = FXMLLoader.load(getClass().getResource("First2.fxml"));
		Scene changeScreen= new Scene(newViewParent);
		Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
		window.setScene(changeScreen);
		window.show();
	}

	public void changeScreenBack(ActionEvent e) throws IOException
	{
		Parent newViewParent = FXMLLoader.load(getClass().getResource("Sample.fxml"));
		Scene changeScreen= new Scene(newViewParent);
		Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
		window.setScene(changeScreen);
		window.show();
	}


	public void maxEnergyButtonClicked() { 
		connection.retrieveMaxEnergy();
	}
	
	public void maxEnergyDateButtonClicked() { 
		connection.retrieveDateWithMaxEnergy();
	}
}
