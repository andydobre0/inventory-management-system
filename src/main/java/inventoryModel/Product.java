package inventoryModel;

import java.sql.Date;

//import java.util.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
	
	private IntegerProperty idProperty;
	private StringProperty nameProperty;
	private StringProperty categoryProperty;
	private DoubleProperty priceProperty;
	private IntegerProperty qtyProperty;
	private StringProperty unitProperty; 
	private String date;
	
	
	public Product() {
		super();
		this.idProperty = new SimpleIntegerProperty();
		this.nameProperty = new SimpleStringProperty();
		this.categoryProperty = new SimpleStringProperty();
		this.qtyProperty = new SimpleIntegerProperty();
		this.unitProperty = new SimpleStringProperty();
	}
	
	public Integer getId() {
		return idProperty.get();
	}
	public IntegerProperty getIdProperty() {
		return idProperty;
	}
	public void setIdProperty(int id) {
		this.idProperty.set(id);;
	}
	
	
	public String getName() {
		return nameProperty.get();
	}
	public StringProperty getNameProperty() {
		return nameProperty;
	}
	public void setNameProperty(String name) {
		this.nameProperty.set(name);
	}
	
	
	public String getCategory() {
		return categoryProperty.get();
	}
	public StringProperty getCategoryProperty() {
		return categoryProperty;
	}
	public void setCategoryProperty(String category) {
		this.categoryProperty.set(category);
	}
	
	
	public Double getPrice() {
		return priceProperty.get();
	}
	public DoubleProperty getPriceProperty() {
		return priceProperty;
	}
	public void setPriceProperty(Double price) {
		this.priceProperty.set(price);
	}
	
	
	public Integer getQty() {
		return qtyProperty.get();
	}
	public IntegerProperty getQtyProperty() {
		return qtyProperty;
	}
	public void setQtyProperty(int quantity) {
		this.qtyProperty.set(quantity);;
	}
	
	public String getUnit() {
		return unitProperty.get();
	}
	public StringProperty getUnitProperty() {
		return unitProperty;
	}
	public void setUnitProperty(String unit) {
		this.unitProperty.set(unit);
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
