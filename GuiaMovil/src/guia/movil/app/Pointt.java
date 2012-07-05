package guia.movil.app;

public class Pointt {
	 String mName;
	 String mDescription;
	 String mIconUrl;
	 double mLatitude;
	 double mLongitude;

	 public Pointt(String mName, String mDescription, String mIconUrl, double mLatitude, double mLongitude) {
		 this.mName = mName;
		 this.mDescription = mDescription;
		 this.mIconUrl = mIconUrl;
		 this.mLatitude = mLatitude;
		 this.mLongitude = mLongitude;
	 }
   
	 public Pointt() {
     }

	 public String getmDescription() {
		 return mDescription;
	 }

	 public void setmDescription(String mDescription) {
		 this.mDescription = mDescription;
	 }

	 public String getmIconUrl() {
		 return mIconUrl;
	 }

	 public void setmIconUrl(String mIconUrl) {
		 this.mIconUrl = mIconUrl;
	 }

	 public double getmLatitude() {
		 return mLatitude;
	 }

	 public void setmLatitude(double mLatitude) {
		 this.mLatitude = mLatitude;
	 }

	 public double getmLongitude() {
		 return mLongitude;
	 }

	 public void setmLongitude(double mLongitude) {
		 this.mLongitude = mLongitude;
	 }

	 public String getmName() {
		 return mName;
	 }

	 public void setmName(String mName) {
		 this.mName = mName;
	 }   
}