const levelFormId = "#level-form";
// handle create new level
$(levelFormId).validate({
  rules: {
    name: {
      required: true,
      notBlank: true,
    },
    score: {
      required: true,
      min: 0,
    },
    time: {
      required: true,
      min: 0,
    },
  },

  messages: {
    name: {
      required: "Name of level can not be blank",
      notBlank: "Name of level can not be blank",
    },
    score: {
      required: "Score can not be blank",
      min: "Score must be greater or equal than 0",
    },
    time: {
      required: "Time per question can not be blank",
      min: "Time per question must be greater or equal than 0",
    },
  },
  submitHandler: () => {
    const levelCreate = Form.getData(levelFormId);
    $.ajax({
      type: "POST",
      url: BASE_URL + "/api/levels",
      headers: {
        Authorization: "Bearer " + TOKEN,
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      },
      data: JSON.stringify(levelCreate),
      success: function (response) {
        window.location.replace("/views/level.html");
      },
      error: function (response) {
        if (response.status == 403) {
          backLogin();
        }
      },
    });
    return false;
  },
});
