public abstract class Card {
	public abstract String getName();
	public abstract String getDescription();
	public abstract String getSimpleDescription();
	public abstract int getID();
	public abstract void setID(int ID);

	public abstract Card shallowCopy();
}