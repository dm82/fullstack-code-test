const listContainer = document.querySelector('#service-list');
let servicesRequest = new Request('/service');
fetch(servicesRequest)
.then(function(response) { return response.json(); })
.then(function(serviceList) {
  serviceList.forEach(service => {
    var li = document.createElement("li");
    li.appendChild(document.createTextNode(service.url + ': ' + service.status));
    listContainer.appendChild(li);
    <!-- DELETE BUTTON -->
    var deleteButton = document.createElement("button");
    deleteButton.setAttribute("id", service._id);
    deleteButton.setAttribute("class", "delete-service");
    deleteButton.appendChild(document.createTextNode("Delete"));
    li.appendChild(deleteButton);
  });
  initDeleteButtons();
});

const saveButton = document.querySelector('#post-service');
saveButton.onclick = evt => {
    let urlName = document.querySelector('#url-name').value;
    fetch('/service', {
    method: 'post',
    headers: {
    'Accept': 'application/json, text/plain, */*',
    'Content-Type': 'application/json'
    },
  body: JSON.stringify({url:urlName})
}).then(res=> location.reload());
}

function initDeleteButtons() {
    const deleteButtons = document.querySelectorAll('.delete-service');
    deleteButtons.forEach(button => {
        button.onclick = evt => {
            fetch('/service/' + button.id, {
            method: 'delete',
            headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
            },
        }).then(res=> location.reload());
        }
    });
}