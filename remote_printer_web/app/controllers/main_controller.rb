require 'rest-client'
require 'json'
# Main controller 
class MainController < ApplicationController

  # postPdf control 
  def postPdf
    # The file content from the web form
    @file = params[:exampleFormControlFile1]
    # The printer name from the web form
    @printer = params[:selected_option]
    # The number of copies from the web form
    @numCopies = params[:integer_selector]

    # Ensure @file is an UploadedFile object
    if @file.is_a?(ActionDispatch::Http::UploadedFile)
      begin
        response = RestClient.post(
          "http://192.168.2.151:8081/print",
          { file: @file, printerName: @printer, numCopies: @numCopies },
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
  end

  # home control logic
  def home 
  end
  
  # uploadPdf control logic
  def uploadPdf   
    url = "http://192.168.2.151:8081/get/printers"
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