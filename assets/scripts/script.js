$(document).ready(function () {
  var address = window.location.href;
  if (address.indexOf("pages/index.html") !== -1) {
    queryTableData();
  }

  if (address.indexOf("pages/register.html") !== -1) {
    $('#registerEgg').submit(e => {
      e.preventDefault()
      if (validateFormData(e)) {
        let egg = queryFormData();
        egg.id = generateRandomId();
        createEgg(egg)
      }
    });
  }

  if (address.indexOf("pages/edit.html") !== -1) {
    var id = getIdFromUrl();
    var egg = getEgg(id);
    createEditForm(egg, id);

    $('#editEgg').submit(e => {
      e.preventDefault()
      if (validateFormData(e)) {
        let egg = queryFormData();
        egg.id = id;
        editEgg(egg)
      }
    });
  }
});

// Register functions.
function queryFormData() {
  let egg = {
    name: $('#name').val(),
    birthday: $('#birthday').val(),
    languages: $('input[name="languages[]"]:checked').map(function () { return $(this).val(); }).get(),
    parent: $('#parentSelect').val(),
    second_parent: $('#secondParentSelect').val()
  }
  return egg;
}

function validateFormData(data) {
  let selectedParent = $('#parentSelect');
  let selectedLanguage = $('input[name="languages[]"]');
  let checked = false;

  for (language of selectedLanguage) {
    if (language.checked) {
      checked = true;
      break;
    }
  }

  if (!checked) {
    console.log("At least one language must be selected.")
    return false;
  } else if (selectedParent === "" || selectedParent === null) {
    console.log("Select a parent.")
    return false;
  } else {
    return true;
  }
}

// Edit functions.
function createEditForm(egg, id) {
  $('#eggId').val(id);
  $('#name').val(egg.name);
  $('#birthday').val(egg.birthday);
  egg.languages.forEach(function (language) {
    $('input[name="languages[]"][value="' + language + '"]').prop('checked', true);
  });
  $('#parentSelect').val(egg.parent);
  $('#secondParentSelect').val(egg.second_parent);
}

// Table functions.
function queryTableData() {
  let eggs = getEggs();
  eggs.forEach(egg => {
    createTable(egg);
  });
}

function createTable(egg) {
  var name = egg.name;
  var birthday = egg.birthday;
  var languages = egg.languages;
  var parent = egg.parent;
  var secondParent = egg.second_parent;
  var editButton = $('<button class="btn btn-secondary mx-1" id="editButton">Edit</button>');
  var deleteButton = $('<button class="btn btn-danger mx-1" id="deleteButton">Delete</button>');

  $(editButton).click(e => {
    window.location.href = "./edit.html?id=" + egg.id;
  })
  $(deleteButton).click(e => {
    deleteEgg(egg.id);
  })

  var tableBody = document.getElementById("tableBody");
  var newRow = tableBody.insertRow(tableBody.rows.length);

  var cell1 = newRow.insertCell(0);
  var cell2 = newRow.insertCell(1);
  var cell3 = newRow.insertCell(2);
  var cell4 = newRow.insertCell(3);
  var cell5 = newRow.insertCell(4);
  var cell6 = newRow.insertCell(5);

  cell1.innerHTML = name;
  cell2.innerHTML = birthday;
  cell3.innerHTML = languages.join(", ");
  cell4.innerHTML = parent;
  cell5.innerHTML = secondParent;
  $(cell6).append(editButton).append(deleteButton);
};

function getSelectedLanguages() {
  var selectedLanguages = [];
  var checkboxes = document.querySelectorAll('input[name="languages[]"]:checked');

  checkboxes.forEach(function (checkbox) {
    selectedLanguages.push(checkbox.value);
  });

  return selectedLanguages;
}

// Query functions.
function createEgg(egg) {
  if (egg.id != null) {
    try {
      localStorage.setItem(egg.id, JSON.stringify(egg));
      console.log("Successfully added egg data!")
      return true;
    } catch (e) {
      console.log(e)
    }
    return false;
  }
}

function getEggs() {
  let eggs = [];
  try {
    for (var key in localStorage) {
      if (!isNaN(key)) {
        var egg = JSON.parse(localStorage.getItem(key));
        eggs.push(egg);
      }
    }
    console.log(eggs);
    return eggs;
  } catch (e) {
    console.log(e);
  }
  return null;
}

function getEgg(id) {
  try {
    var egg = JSON.parse(localStorage.getItem(id));
    return egg;
  } catch (e) {
    console.log(e);
  }
  return null;
}

function editEgg(egg) {
  if (egg.id != null) {
    try {
      localStorage.setItem(egg.id, JSON.stringify(egg));
      console.log("Successfully edited egg data!")
      return true;
    } catch (e) {
      console.log(e)
    }
    return false;
  }
}

function deleteEgg(id) {
  try {
    localStorage.removeItem(id);
    console.log("Successfully deleted egg data!");
    cleanTableData();
    queryTableData();
  } catch (e) {
    console.log(e)
  }
}

function getIdFromUrl() {
  var id;
  var url = window.location.href;
  var partes = url.split('?');
  if (partes.length > 1) {
    var parameters = partes[1].split('&');
    for (var i = 0; i < parameters.length; i++) {
      var parameter = parameters[i].split('=');
      if (parameter[0] === 'id') {
        id = parameter[1];
      }
    }
  }
  return id;
}

function cleanTableData() {
  $('#tableBody').empty();
}

function generateNumber() {
  return Math.floor(Math.random() * 1000);
}

function verifyIfIdExists(id) {
  var keysLocalStorage = Object.keys(localStorage);

  for (var i = 0; i < keysLocalStorage.length; i++) {
    var key = keysLocalStorage[i];
    if (parseInt(key) === id) {
      return true;
    }
  }

  return false;
}

function generateRandomId() {
  var randomId = generateNumber();
  while (verifyIfIdExists(randomId)) {
    randomId = generateNumber();
  }
  return randomId;
}
