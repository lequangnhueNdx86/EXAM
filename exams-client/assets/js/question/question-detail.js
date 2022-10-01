const param = getUrlVars();
$.ajax({
  type: "GET",
  url: `${BASE_URL}/api/questions/${param.question_id}`,
  headers: {
    Authorization: "Bearer " + TOKEN,
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
  success: function (response) {
    const question = response.data;

    let questionType =
      question.questionType === "FILL_BLANK"
        ? `<span class="badge bg-success">Fill blank</span>`
        : `<span class="badge bg-primary">Selection</span>`;
    let options = "";
    if (question.questionType === "SELECTION") {
      options = `<div class="col-sm-10 answer-item">`;
      let listOption = question.listOption;
      listOption.forEach((option) => {
        let checked = (option.key === question.correctValue) ? "checked" : ""
        options += `<div class="form-check">
                <input class="form-check-input" type="radio" name="answer" id="${question.id}" value="${option.key}" ${checked}>
                <label class="form-check-label" for="gridRadios1">
                 ${option.key}: ${option.value}
                </label>
              </div>`;
      });
      options += `</div>`;
    }

    $("#question-detail").html(`
        <h6>ID: #${question.id}</h6>
        <h6>Lesson: ${question.lesson.name}</h6>
        <h6>Content: ${question.content}</h6>
        <h6>Question Type: ${questionType}</h6>
        <h6>Level: ${question.level.name}</h6>
        <h6>Score: ${question.level.score}</h6>
        <h6>Time: ${question.level.time} (min)</h6>
        <h6>Correct Value: ${question.correctValue}</h6>
        ${options}
    `);
  },
  error: function (response) {
    if (response.status == 403) {
      backLogin();
    }
  },
});
