// get all lesson
$.ajax({
  type: "GET",
  url: BASE_URL + "/api/lessons",
  headers: {
    Authorization: "Bearer " + TOKEN,
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
  success: function (response) {
    const listLesson = response.data.listLesson;
    let html = "";
    listLesson.forEach((lesson) => {
      html += `<option value="${lesson.id}">${lesson.name}</option>`;
    });
    $("select[name='lesson']").html(html);
  },
  error: function (response) {
    if (response.status == 403) {
      backLogin();
    }
  },
});

// get all level
$.ajax({
  type: "GET",
  url: BASE_URL + "/api/levels",
  headers: {
    Authorization: "Bearer " + TOKEN,
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
  success: function (response) {
    const listLevel = response.data.listLevel;
    let html = "";
    listLevel.forEach((level) => {
      html += `<div class="form-check">
        <input class="form-check-input" type="radio" name="level" value="${level.id}" checked>
        <label class="form-check-label" for="gridRadios1">
          ${level.name}
        </label>
      </div>`;
    });
    $("#level-radius").html(html);
  },
  error: function (data) {
    if (response.status == 403) {
      backLogin();
    }
  },
});

// handle if fill blank question
$("input[name='questionType']").change(() => {
  let type = $("input[name='questionType']:checked").val();
  if (type === "SELECTION") {
    $("#option-div").attr("style", "display: block");
  } else {
    $("#option-div").attr("style", "display: none");
  }
});

// add option
var seq = 0;
$("#add-option").click(() => {
  html = `<div class="input-group mb-3 option-element" id="option-${seq++}">
    <input type="text" class="form-control" placeholder="Key" aria-label="Key" name="key" required>
    <input type="text" class="form-control" placeholder="Value" aria-label="Value" name="value" required> &nbsp; &nbsp;
    <span id="remove-option" onclick="removeOption(this)"><i class="bi bi-x-lg"></i></span>
  </div>
  
</div>`;
  $("#options").append(html);
});

function removeOption(option) {
  $(option).parent().remove();
}

$("#question-add-form").validate({
  rules: {
    lesson: {
      required: true,
    },
    level: {
      required: true,
    },
    content: {
      required: true,
      notBlank: true,
    },
    correctValue: {
      required: true,
      notBlank: true,
    },
  },
  messages: {
    lesson: {
      required: "Lesson can not be blank",
    },
    level: {
      required: "Level can not be blank",
    },
    content: {
      required: "Content can not be blank",
      notBlank: "Content can not be blank",
    },
    correctValue: {
      required: "Correct value can not be blank",
      notBlank: "Correct value can not be blank",
    },
  },

  submitHandler: () => {
    let lesson = parseInt($("select[name='lesson']").val());
    let question = {
      content: $("textarea[name='content']").val(),
      correctValue: $("input[name='correctValue']").val(),
      lesson: lesson,
      level: parseInt($("input[name='level']:checked").val()),
      questionType: $("input[name='questionType']:checked").val(),
    };
    let listOption = [];
    let optionElements = $("div.option-element").toArray();
    optionElements.forEach((option) => {
      listOption.push({
        key: $(option).find("input[name='key']").val(),
        value: $(option).find("input[name='value']").val(),
      });
    });
    question.listOption = listOption;
    $.ajax({
      type: "POST",
      url: `${BASE_URL}/api/questions`,
      headers: {
        Authorization: "Bearer " + TOKEN,
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      },
      data: JSON.stringify(question),
      success: function (response) {
        console.log(response.data);
        window.location.replace(
          "/views/lesson-detail.html?lesson_id=" + lesson
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
