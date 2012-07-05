package guia.movil.app;

public class Road {
	 public String mName;
	 public String mDescription;
	 public int mColor;
	 public int mWidth;
	 public double[][] mRoute = new double[][] {};
	 public Pointt[] mPoints = new Pointt[] {};

	 public Road(String mName, String mDescription, int mColor, int mWidth) {
		 this.mName = mName;
	     this.mDescription = mDescription;
	     this.mColor = mColor;
	     this.mWidth = mWidth;
	 }
	 
	 public Road() {
	 }

	 public int getmColor() {
		 return mColor;
	 }

	 public void setmColor(int mColor) {
	     this.mColor = mColor;
	 }

	 public String getmDescription() {
	     return mDescription;
	 }

	 public void setmDescription(String mDescription) {
	     this.mDescription = mDescription;
	 }

	 public String getmName() {
	     return mName;
	 }

	 public void setmName(String mName) {
	     this.mName = mName;
	 }

	 public Pointt[] getmPoints() {
	     return mPoints;
	 }

	 public void setmPoints(Pointt[] mPoints) {
	     this.mPoints = mPoints;
	 }

	 public double[][] getmRoute() {
	     return mRoute;
	 }

	 public void setmRoute(double[][] mRoute) {
	     this.mRoute = mRoute;
	 }

	 public int getmWidth() {
	     return mWidth;
	 }

	 public void setmWidth(int mWidth) {
	     this.mWidth = mWidth;
	 }
}