$.ajax({
  type: "GET",
  url: BASE_URL + "/api/questions",
  headers: {
    Authorization: "Bearer " + TOKEN,
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
  success: function (response) {
    const listQuestion = response.data.listQuestion;
    renderData(listQuestion);
  },
  error: function (response) {
    if (response.status == 403) {
      backLogin();
    }
  },
});

function renderData(listQuestion) {
  let html = ``;
  listQuestion.forEach((question) => {
    let questionType =
      question.questionType === "FILL_BLANK"
        ? `<span class="badge bg-success">Fill blank</span>`
        : `<span class="badge bg-primary">Selection</span>`;
    let questionRow = `<tr>
        <th scope="row"><a href="question-detail.html?question_id=${
          question.id
        }">${question.id}</a></th>
        <td><a href="lesson-detail.html?lesson_id=${
          question.lesson.id
        }" class="text-primary">${question.lesson.name}</a></td>
        <td><a href="question-detail.html?question_id=${
          question.id
        }" class="text-primary">${question.content.substring(0, 20)}...</a></td>
        <td>${questionType}</td>
        <td>${question.correctValue}</td>
        <td>${question.level.name}</td>
        <td><span data-bs-toggle="modal" data-bs-target="#delete-modal" onclick="confirm_removed(${
          question.id
        })"><i class="bi bi-trash-fill"></i></span></td>
        <td><span data-bs-toggle="modal" data-bs-target="#edit-modal" onclick="edit_question(${
          question.id
        })"><i class="bi bi-pencil-fill"></i></span></td>
      </tr>`;
    html += questionRow;
  });
  $("#question-list-body").html(html);
}

function confirm_removed(questionId) {
  $("#question-id-delete").text(questionId);
}

$("#remove-btn").click(() => {
  let questionId = $("#question-id-delete").text();
  $.ajax({
    type: "DELETE",
    url: BASE_URL + "/api/questions/" + questionId,
    headers: {
      Authorization: "Bearer " + TOKEN,
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    success: function (response) {
      window.location.replace("/views/question.html");
    },
    error: function (response) {
      if (response.status == 403) {
        backLogin();
      }
    },
  });
});

function edit_question(questionId) {
  $("input[name='questionId']").val(questionId)
  $.ajax({
    type: "GET",
    url: `${BASE_URL}/api/questions/${questionId}`,
    headers: {
      Authorization: "Bearer " + TOKEN,
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    success: function (response) {
      const question = response.data;
      $("input[name='lesson']").val(question.lesson.name);
      $("textarea[name='content']").val(question.content);
      $("#level").html(
        `<span class="badge bg-success">${question.level.name}</span>`
      );
      let questionType =
        question.questionType === "FILL_BLANK"
          ? `<span class="badge bg-success">Fill blank</span>`
          : `<span class="badge bg-primary">Selection</span>`;
      $("#question-type").html(questionType);
      $("input[name='correctValue']").val(question.correctValue);
      let options = "";
      if (question.questionType === "SELECTION") {
        question.listOption.forEach(option => {
          options += `<div class="row mb-3 option-item" id="${option.id}">
          <label class="col-sm-2 col-form-label text-center">${option.key}</label>
          <div class="col-sm-10">
            <input class="form-control" type="text" name="value" value="${option.value}">
          </div>  
          </div>`;
        })
      }
      $("#option-wrapper").html(options);
    },

    error: function (response) {
      if (response.status == 403) {
        backLogin();
      }
    },
  });
}

$("#edit-btn").click(() => {
  let questionId = parseInt($("input[name='questionId']").val());
  let content = $("textarea[name='content']").val();
  let correctValue = $("input[name='correctValue']").val();
  const listOptionItem = $("div.option-item").toArray();
  let listOption = [];
  listOptionItem.forEach(item => {
    listOption.push({
      id: parseInt($(item).attr("id")),
      key: $(item).find("label").text(),
      value: $(item).find("input[name='value']").val()
    })
  })
  console.log(listOption);
  const questionEdit = {
    content: content,
    correctValue: correctValue,
    id: questionId,
    listOption: listOption
  }

  $.ajax({
    type: "PUT",
    url: `${BASE_URL}/api/questions/${questionId}`,
    headers: {
      Authorization: "Bearer " + TOKEN,
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    data: JSON.stringify(questionEdit),
    success: function (response) {
     window.location.replace("/views/question.html")
    },

    error: function (response) {
      if (response.status == 403) {
        backLogin();
      }
    },
  });

})
