package inventoryModel;

public class Category {
	private int ID;
	private String nume;
	
	public Category(String nume) {
		super();
		this.ID = 0;
		this.nume = nume;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	@Override
	public String toString() {
		return ID + ", " + nume + " ";
	}
	
}
