require 'rest-client'
require 'json'
# Main controller 
class MainController < ApplicationController

  # postPdf control 
  def postPdf

    if params[:exampleFormControlFile1].present?

      # The file content from the web form
      @file = params[:exampleFormControlFile1]
      # The printer name from the web form
      @printer = params[:selected_option]
      # The number of copies from the web form
      @numCopies = params[:integer_selector]

      @color = params[:selected_color]

      @orientation = params[:selected_orientation]

      # Ensure @file is an UploadedFile object
      if @file.is_a?(ActionDispatch::Http::UploadedFile)
        begin
          response = RestClient.post(
            "http://192.168.1.215:8081/print",
            { file: @file, printerName: @printer, numCopies: @numCopies, color: @color, orientation: @orientation },
            multipart: true
          )
          puts response.code

        rescue RestClient::ExceptionWithResponse => e
          puts "Error: #{e.response}"
        end
      else
        puts "Invalid file format or file missing"
      end
      redirect_to root_path
    else
      puts "No se realizo la petición, parametro vacio"
    end
  end

  # home control logic
  def home 
  end
  
  # uploadPdf control logic
  def uploadPdf   
    url = "http://192.168.1.215:8081/get/printers"
    begin
      response = RestClient.get(
        url
      ) 
      @result = JSON.parse(response.body)
      puts @result
      puts response.code
    rescue RestClient::ExceptionWithResponse => e
      puts "Error: #{e.response}"
    end    
  end
end