
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

function editEgg(params) {
}

function deleteEgg(params) {
}

function getEgg(params) {
}

function getDBLength() {
  try {
    return localStorage.length;
  } catch (e) {
    console.log(e);
  }
  return null;
}