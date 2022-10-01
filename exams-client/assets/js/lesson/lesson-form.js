const lessonCreateFormId = "#lesson-create-form";
// create new lesson
$(lessonCreateFormId).validate({
  rules: {
    name: {
      required: true,
      notBlank: true,
    },
    scorePass: {
      required: true,
      min: 0,
      max: 100
    },
  },
  messages: {
    name: {
      required: "Name of lesson can not be blank",
      notBlank: "Name of lesson can not be blank",
    },
    scorePass: {
      required: "Score pass can not be blank",
      min: "Score pass must be greater or equal than 0",
      max: "Score pass must be less or equal than 100"
    },
  },

  submitHandler: (form) => {
    const lesson = Form.getData(lessonCreateFormId);
    $.ajax({
      type: "POST",
      url: BASE_URL + "/api/lessons",
      headers: {
        Authorization: "Bearer " + TOKEN,
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      },
      data: JSON.stringify(lesson),
      success: function (response) {
        window.location.replace(
          "/views/lesson-detail.html?lesson_id=" + response.data.id
        );
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
