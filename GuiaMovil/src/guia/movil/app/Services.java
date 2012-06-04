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
	private static final String NAMESPACE  = "http://turismo/";
    public static String URL="http://192.168.1.7:8084/TurismoCuricoWebService/TurismoCurico?wsdl";

    public static String getDescription(String methodname,String soap,String nombre,String parametrovalor){
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
}
