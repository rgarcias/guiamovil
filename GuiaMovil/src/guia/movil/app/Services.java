package guia.movil.app;

import java.io.IOException;
import java.util.logging.Level;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class Services {
	private static final String NAMESPACE  = "http://Turismo/";
    public static String URL="http://192.168.1.8:8080/TurismoCuricoWebService/TurismoCurico?wsdl";
    //public static String URL="http://172.17.32.185:8080/TurismoCuricoWebService/TurismoCurico?wsdl";
    
    public static String getDescription(String methodname,String soap,String nombre,String parametrovalor, String nombre2, Boolean english){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    
	    PropertyInfo parametro2 = new PropertyInfo();
	    parametro2.setName(nombre2);
	    parametro2.setValue(english.booleanValue());
	    parametro2.setType(Boolean.class);
	    
	    Solicitud.addProperty(parametro);
	    Solicitud.addProperty(parametro2);
	    
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	}   
    
    public static String getLatitude(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	} 
    

    public static String getLongitude(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	}
    
    public static String getPlaceID(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	} 
    public static String getComments(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	} 
    
    public static String getPhotos(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	} 



	public static void addComments(String methodname,String soap,String parametrovalorComment
			,String parametrovalorNick,String parametrovalorplcaeid){
		SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
		PropertyInfo parametroComment = new PropertyInfo();
		parametroComment.setName("comment");
		parametroComment.setValue(parametrovalorComment);
		parametroComment.setType(String.class);
		
		PropertyInfo parametroNick = new PropertyInfo();
		parametroNick.setName("nick");
		parametroNick.setValue(parametrovalorNick);
		parametroNick.setType(String.class);
		
		PropertyInfo parametroplcaeid = new PropertyInfo();
		parametroplcaeid.setName("plcaeId");
		parametroplcaeid.setValue(parametrovalorplcaeid);
		parametroplcaeid.setType(String.class);
		
		
		
		Solicitud.addProperty(parametroComment);
		Solicitud.addProperty(parametroNick);
		Solicitud.addProperty(parametroplcaeid);
		SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
		Envoltorio.setOutputSoapObject (Solicitud);
		HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
		
		try {
		    	TransporteHttp.call (soap, Envoltorio);
		    }
		catch (IOException ex) {
		       
		    }
		catch (XmlPullParserException ex) {
		       
		    }
	}
	
	public static void addRating(String methodname,String soap,String parametrovalorNumber
			,String parametrovalorplcaeid){
		SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
		PropertyInfo parametroNumber = new PropertyInfo();
		parametroNumber.setName("number");
		parametroNumber.setValue(parametrovalorNumber);
		parametroNumber.setType(String.class);

		PropertyInfo parametroplcaeid = new PropertyInfo();
		parametroplcaeid.setName("placeId");
		parametroplcaeid.setValue(parametrovalorplcaeid);
		parametroplcaeid.setType(String.class);
		
		
		
		Solicitud.addProperty(parametroNumber);
		Solicitud.addProperty(parametroplcaeid);
		SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
		Envoltorio.setOutputSoapObject (Solicitud);
		HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
		
		try {
		    	TransporteHttp.call (soap, Envoltorio);
		    }
		catch (IOException ex) {
		       
		    }
		catch (XmlPullParserException ex) {
		       
		    }
	}
	
	
	
	public static String getRatingAverage(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	} 
	
	public static String getPlaces(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	} 
	
	public static String getLocationsSearched(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	} 
	
	
	public static String getPlacesSortAsc(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	}
	
	
	public static String getPlacesSortDsc(String methodname,String soap,String nombre,String parametrovalor){
	    SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
	    PropertyInfo parametro = new PropertyInfo();
	    parametro.setName(nombre);
	    parametro.setValue(parametrovalor);
	    parametro.setType(String.class);
	    Solicitud.addProperty(parametro);
	    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
	    Envoltorio.setOutputSoapObject (Solicitud);
	    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
	    try {
	    	TransporteHttp.call (soap, Envoltorio);
	    }
	    catch (IOException ex) {
	        return null;
	    }
	    catch (XmlPullParserException ex) {
	        return null;
	    }
	    try {
	        String CadenaDevuelta =  Envoltorio.getResponse().toString();
	        return CadenaDevuelta;  
	    } 
	    catch (SoapFault ex) {
	        return null;
	    }
	}

	public static String getTopTen(String methodname,String soap) {
		// TODO Auto-generated method stub
		 SoapObject Solicitud = new SoapObject(NAMESPACE, methodname);
		    
		    
		    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
		    Envoltorio.setOutputSoapObject (Solicitud);
		    HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
		    try {
		    	TransporteHttp.call (soap, Envoltorio);
		    }
		    catch (IOException ex) {
		        return null;
		    }
		    catch (XmlPullParserException ex) {
		        return null;
		    }
		    try {
		        String CadenaDevuelta =  Envoltorio.getResponse().toString();
		        return CadenaDevuelta;  
		    } 
		    catch (SoapFault ex) {
		        return null;
		    }
		
	}
	
	
	
}