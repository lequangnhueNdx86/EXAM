const param = getUrlVars();
// get lesson detail
$.ajax({
  type: "GET",
  url: `${BASE_URL}/api/lessons/${param.lesson_id}`,
  headers: {
    Authorization: "Bearer " + TOKEN,
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
  success: function (response) {
    const lesson = response.data;
    let matrix = "";
    lesson.matrixQuestion.forEach((item) => {
      matrix += `<span class="badge bg-success">${item.quantity} &nbsp; ${item.levelName}</span>&nbsp;`;
    });
    $("#matrix").html(matrix);
    if (matrix === "") {
      $("#add-matrix").html(`<i class="bi bi-plus-circle-fill"></i>`);
    }
    $("#lesson-info").html(`
        <h6>ID: #${lesson.id}</h6>
        <h6>Name: ${lesson.name}</h6>
        <h6>Score pass: ${lesson.scorePass}%</h6>
        <h6>Number of Question: ${lesson.numberOfQuestion}</h6>
        <h6>Number of Completed Exam: ${lesson.numberOfCompletedExams}</h6>
    `);
  },
  error: function (response) {
    if (response.status == 403) {
      backLogin();
    }
  },
});

// get all question by lesson
$.ajax({
  type: "GET",
  url: `${BASE_URL}/api/lessons/${param.lesson_id}/questions`,
  headers: {
    Authorization: "Bearer " + TOKEN,
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
  success: function (response) {
    const questionList = response.data.listQuestion;
    let html = "";
    questionList.forEach((question) => {
      let questionType =
        question.questionType === "FILL_BLANK"
          ? `<span class="badge bg-success">Fill blank</span>`
          : `<span class="badge bg-primary">Selection</span>`;
      html += `<tr>
        <th scope="row"><a href="question-detail.html?question_id=${
          question.id
        }">${question.id}</a></th>
        <td><a href="question-detail.html?question_id=${
          question.id
        }" class="text-primary">${question.content.substring(0, 20)}...</a></td>
        <td>${questionType}</td>
        <td>${question.correctValue}</td>
        <td>${question.level.name}</td>
      </tr>`;
    });
    $("#question-list").html(html);
  },
  error: function (response) {
    if (response.status == 403) {
      backLogin();
    }
  },
});

// get all exam by lesson
$.ajax({
  type: "GET",
  url: `${BASE_URL}/api/lessons/${param.lesson_id}/exams`,
  headers: {
    Authorization: "Bearer " + TOKEN,
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
  },
  success: function (response) {
    const examList = response.data.listExam;
    let html = "";

    examList.forEach((exam) => {
      let result =
        exam.result === "PASS"
          ? `<span class="badge bg-success">Pass</span>`
          : `<span class="badge bg-danger">Fail</span>`;
      html += `<tr>
          <th scope="row"><a href="exams-detail.html?exam_id=${exam.id}">${exam.id}</a></th>
          <td>${exam.studentCode}</td>
          <td>${exam.studentName}</td>
          <td>${exam.correctQuestion}/${exam.totalQuestion}</td>
          <td>${exam.score} / ${exam.totalScore}</td>
          <td>${exam.timeComplete}</td>
          <td>${result}</td>
        </tr>`;
    });
    $("#completed-exam-list").html(html);
  },
  error: function (response) {
    if (response.status == 403) {
      backLogin();
    }
  },
});

// level
$("#add-matrix").click(() => {
  //get all level
  $.ajax({
    type: "GET",
    url: `${BASE_URL}/api/levels`,
    headers: {
      Authorization: "Bearer " + TOKEN,
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    success: function (response) {
      const levelList = response.data.listLevel;
      console.log(levelList);
      let html = "";
      levelList.forEach((level) => {
        html += `<tr>
      <th scope="row">${level.name}</th>
      <td><input type="number" class="form-control w-50" id="level-${level.id}" name="quantity"></td>
    </tr>`;
      });
      $("#matrix-add-modal-body").html(html);
    },
    error: function (response) {
      if (response.status == 403) {
        backLogin();
      }
    },
  });
});

$("#level-add-btn").click(() => {
  let matrixQuestion = [];
  const listInput = $("#matrix-add-modal-body")
    .find("input[name='quantity']")
    .toArray();
  console.log(listInput);
  listInput.forEach((input) => {
    let quantity = $(input).val();
    if (quantity.length > 0 && parseInt(quantity) > 0) {
      console.log($(input).attr("id").substring(6));
      matrixQuestion.push({
        levelId: $(input).attr("id").substring(6),
        quantity: quantity,
      });
    }
  });
  console.log(matrixQuestion);
  $.ajax({
    type: "POST",
    url: `${BASE_URL}/api/lessons/${param.lesson_id}/matrix`,
    headers: {
      Authorization: "Bearer " + TOKEN,
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    data: JSON.stringify(matrixQuestion),
    success: function (response) {
      console.log(response.data);
      window.location.replace(
        "/views/lesson-detail.html?lesson_id=" + param.lesson_id
      );
    },
    error: function (response) {
      if (response.status == 403) {
        backLogin();
      }
    },
  });
});
