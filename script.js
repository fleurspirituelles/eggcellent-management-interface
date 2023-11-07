
$(document).ready(function () {

  $('#registerEgg').submit(e => {
    e.preventDefault()
    if (validateFormData(e)) {
      let egg = queryFormData();
      egg.id = getDBLength();
      console.log(egg);
      createEgg(egg)
    }
  })

  queryTableData();

})

//Register functions.

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
  console.log(selectedLanguage);
  let checked = false;

  for (language of selectedLanguage) {
    console.log(language);
    if (language.checked) {
      checked = true;
      break;
    }
  }

  if (!checked) {
    console.log("At least one language must be selected.")
    return false;
  } else if (
    selectedParent === "" ||
    selectedParent === null
  ) {
    console.log("Select a parent.")
    return false;
  } else {
    //let response = updateData();
    return true;
  }
}

//Edit functions.

//Table functions.
function queryTableData() {
  let eggs = getEggs();
  console.log(eggs);
  eggs.forEach(egg => {
    createTable(egg);
  });
}

function createTable(egg) {
  var name = egg.name;
  var birthday = egg.birthday;
  var languages = egg.languages;
  var parent = egg.parent
  var secondParent = egg.secondParent

  var tableBody = document.getElementById("tableBody");
  console.log(tableBody);
  var newRow = tableBody.insertRow(tableBody.rows.length);
  console.log(newRow);

  var cell1 = newRow.insertCell(0);
  var cell2 = newRow.insertCell(1);
  var cell3 = newRow.insertCell(2);
  var cell4 = newRow.insertCell(3);
  var cell5 = newRow.insertCell(4);

  cell1.innerHTML = name;
  cell2.innerHTML = birthday;
  cell3.innerHTML = languages.join(", ");
  cell4.innerHTML = parent;
  cell5.innerHTML = secondParent;
};

function getSelectedLanguages() {
  var selectedLanguages = [];
  var checkboxes = document.querySelectorAll('input[name="languages[]"]:checked');

  checkboxes.forEach(function (checkbox) {
    selectedLanguages.push(checkbox.value);
  });

  return selectedLanguages;
}

//Query functions.

function createEgg(egg) {
  if (egg.id != null) {
    try {
      localStorage.setItem(egg.id, JSON.stringify(egg));
      console.log("Succesfully added egg data!")
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
    for (let index = 0; index < localStorage.length; index++) {
      eggs.push(JSON.parse(localStorage.getItem(index)));
    }
    console.log(eggs);
    return eggs;
  } catch (e) {
    console.log(e);
  }
  return null;
}

function getDBLength() {
  try {
    return localStorage.length;
  } catch (e) {
    console.log(e);
  }
  return null;
}

function editEgg() {
}

function deleteEgg() {
}