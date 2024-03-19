
function dragOverHandler(event) {
  event.preventDefault();
  event.stopPropagation();
}

function dropHandler(event) {
  event.preventDefault();
  event.stopPropagation();
  const fileInput = document.getElementById('fileInput');
  fileInput.files = event.dataTransfer.files;
  updateButtonText(fileInput);
}

