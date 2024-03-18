// upload_button_script.js

function updateButtonText(input) {
  const fileButton = document.getElementById('fileButton');
  if (input.files.length > 0) {
    fileButton.textContent = input.files[0].name;
  }
}
  