package es.iesjandula.remote_printer_client.scheduled_tasks;

import java.awt.print.PrinterJob;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class Print
{

	private String serverUrl = "http://192.168.2.151:8081/";

	@Scheduled(fixedDelayString = "1000", initialDelay = 2000)
	public void print()
	{
		log.info("intento de imprimir");
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		InputStream inputStream = null;
		// GETTING HTTP CLIENT
		httpClient = HttpClients.createDefault();
		
		HttpGet request = new HttpGet(this.serverUrl + "/get/prints");
		try {
			response = httpClient.execute(request);
			
			if (response.containsHeader("Content-Disposition"))
			{				
				inputStream = response.getEntity().getContent();
				
				String numCopies = response.getFirstHeader("numCopies").getValue();
				String printerName = response.getFirstHeader("printerName").getValue();


				this.printFile(printerName, Integer.valueOf(numCopies),inputStream);
			}
		}
		catch (JsonProcessingException exception)
		{
			String error = "Error Json Processing Exception";
			log.error(error, exception);
		}
		catch (UnsupportedEncodingException exception)
		{
			String error = "Error Unsupported Encoding Exception";
			log.error(error, exception);
		}
		catch (ClientProtocolException exception)
		{
			String error = "Error Client Protocol Exception";
			log.error(error, exception);
		}
		catch (IOException exception)
		{
			String error = "Error In Out Exception";
			log.error(error, exception);
		}
		finally
		{
			// --- CERRAMOS ---
			this.closeHttpClientResponse(httpClient, response);
		}
	}
	
	public void printFile(String printerName, int numCopies, InputStream input)
	{

		// --- FLUJOS ---
		DataInputStream dataInputStream = null;
		try
		{
			dataInputStream = new DataInputStream(input);
			
			byte[]data = dataInputStream.readAllBytes();
			
			PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

	        // Buscar la impresora por su nombre
	        PrintService selectedPrinter = null;
	        for (PrintService printer : printServices) {
	            if (printer.getName().equals(printerName)) {
	                selectedPrinter = printer;
	                break;
	            }
	        }

	        if (selectedPrinter != null) {
	            // Crear un trabajo de impresión
	            PrinterJob printerJob = PrinterJob.getPrinterJob();
	            
	            // Cargar el documento a imprimir
	            try {
	     
	                PDDocument pdDocument  = PDDocument.load(data);
	                
//	                for (PDPage page : pdDocument.getPages()) {
//	                    page.setRotation(90); // Rotar 90 grados en sentido horario
//	                }
	                
	                printerJob.setPageable(new PDFPageable(pdDocument));
	                printerJob.setPrintService(selectedPrinter);
	                
	                HashPrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
//	                attributeSet.add(ColorSupported.NOT_SUPPORTED);
//	                attributeSet.add(OrientationRequested.LANDSCAPE);
//	                attributeSet.add(Sides.ONE_SIDED);
	                printerJob.setCopies(numCopies);
	                printerJob.print();
	                
	            } catch (Exception exception)
	    		{
	    			String error = "Error imprimiendo";
	    			log.error(error, exception);
	    		}
	        } else {
	            String error = "Printer erronea";
				log.error(error);
	        }
			
		}
		catch (IOException exception)
		{
			String message = "Error";
			log.error(message, exception);
		}
		finally
		{
			if (dataInputStream != null)
			{
				try
				{
					dataInputStream.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void closeHttpClientResponse(CloseableHttpClient httpClient, CloseableHttpResponse response)
	{
		if (httpClient != null)
		{
			try
			{
				httpClient.close();
			}
			catch (IOException exception)
			{
				String error = "Error In Out Exception";
				log.error(error, exception);
			}
		}
		if (response != null)
		{
			try
			{
				response.close();
			}
			catch (IOException exception)
			{
				String error = "Error In Out Exception";
				log.error(error, exception);
			}
		}
	}
	
	
}
