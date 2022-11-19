$("#start-btn").click(() => {
  // get all lesson
  $.ajax({
    type: "GET",
    url: BASE_URL + "/api/exams/lessons",
    headers: {
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
    error: function (response) {},
  });
});

var examRequest = {};

$("#get-exam-form").validate({
  rules: {
    studentCode: {
      required: true,
      notBlank: true,
    },
    studentName: {
      required: true,
      notBlank: true,
    },
    lesson: {
      required: true,
      notBlank: true,
    },
  },
  messages: {
    studentCode: {
      required: "Student code can not be blank",
      notBlank: "Student code can not be blank",
    },
    studentName: {
      required: "Student name can not be blank",
      notBlank: "Student name can not be blank",
    },
    lesson: {
      required: "Lesson can not be blank",
      notBlank: "Lesson can not be blank",
    },
  },

  submitHandler: () => {
    $("#confirm-btn").click();
    $("#close-btn").click(() => {
      $("#open-add-btn").click();
    })
    
    $("#go-exam-btn").click(() => {
      $("#exam-btn").click();
      examRequest = {
      studentCode: $("input[name='studentCode']").val(),
      studentName: $("input[name='studentName']").val(),
      lesson: parseInt($("select[name='lesson']").val()),
    };
    $.ajax({
      type: "POST",
      url: BASE_URL + "/api/exams/generate",
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      },
      data: JSON.stringify(examRequest),
      success: function (response) {
        let exam = response.data;
        console.log(exam);
        renderExam(exam);
        clearInput();
      },
      error: function (response) {},
    });
    });
    
  },
});

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if (new Date().getTime() - start > milliseconds) {
      break;
    }
  }
}

function clearInput() {
  $("input[name='studentCode']").val("");
  $("input[name='studentName']").val("");
}

function renderExam(exam) {
  $("#examId").text(exam.id);
  $("#lesson").text(exam.lesson.name);
  $("#student-info").text(`${exam.studentCode} - ${exam.studentName}`);
  $("#start-time").text(new Date(exam.createTime).toLocaleString());
  $("#total-question").text(exam.totalQuestion);
  $("#total-score").text(exam.totalScore);
  $("#score-pass").text(exam.lesson.scorePass);
  // time count down
  var time = exam.timeExam * 60; // seconds
  var x = setInterval(function () {
    --time;
    // Time calculations for minutes and seconds
    var minutes = Math.floor(time / 60);
    var seconds = Math.floor(time % 60);
    console.log(`${minutes}:${seconds}`);
    $("#time").text(`${minutes}:${seconds}`);
    // If the count down is finished
    if (time <= 0) {
      clearInterval(x);
      // submit exams
      submitExam();
    }
  }, 1000);
  let listQuestion = exam.listQuestion;
  let html = "";
  let index = 1;
  listQuestion.forEach((question) => {
    let answer = "";
    if (question.questionType === "FILL_BLANK") {
      answer = `<div class="col-sm-10 answer-item"><input class="form-control" type="text" name="answer-${question.id}" id="${question.id}"></div>`;
    } else {
      answer = `<div class="col-sm-10 answer-item">`;
      let listOption = question.listOption;
      listOption.forEach((option) => {
        answer += `<div class="form-check">
                <input class="form-check-input" type="radio" name="answer-${question.id}" id="${question.id}" value="${option.key}">
                <label class="form-check-label" for="gridRadios1">
                  ${option.value}
                </label>
              </div>`;
      });
      answer += `</div>`;
    }
    html += `<div class="card">
        <div class="card-body">
          <h5 class="card-title">Question ${index++}: ${question.content}</h5>
            <h6>Answer</h6>
            ${answer}
          </div>
        </div>
      </div>`;
  });
  $("#exam-body").html(html);
  $("#submit-exam-btn").click(() => {
    submitExam();
    clearInterval(x);
    $("#time").text("");
  });
}

// submit exam
function submitExam() {
  let listAnswerDiv = $("div.answer-item").toArray();
  let listAnswer = [];
  listAnswerDiv.forEach((item) => {
    let questionId = parseInt($(item).find("input").attr("id"));
    let answer = "";
    if ($(item).find("input[type='radio']:checked").length) {
      answer = $(item).find("input[type='radio']:checked").val();
    } else if ($(item).find("input[type='text']").length) {
      answer = $(item).find("input[type='text']").val();
    }
    console.log(answer);
    listAnswer.push({
      questionId: questionId,
      answer: answer,
    });
  });
  let examSubmit = {
    examId: $("#examId").text(),
    listAnswer: listAnswer,
  };

  $.ajax({
    type: "POST",
    url: BASE_URL + "/api/exams/submit",
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    data: JSON.stringify(examSubmit),
    success: function (response) {
      let result = response.data;
      console.log(result);
      renderResult(result);
    },
    error: function (response) {},
  });
}

function renderResult(result) {
  $("#result-btn").click();
  let resultElement =
    result.result === "PASS"
      ? `<span class="badge bg-success">Pass</span>`
      : `<span class="badge bg-danger">Fail</span>`;
  let html = `
    <h5>#${result.id}</h5>
    <h6>Lesson: ${result.lessonName}</h6>
    <h6>Student: ${result.studentName} (${result.studentCode})</h6>
    <h6>Create Time: ${new Date(result.createTime).toLocaleString()}</h6>
    <h6>Time Complete: ${result.timeComplete}</h6>
    <h6>Correct questions: ${result.correctQuestion} / ${
    result.totalQuestion
  }</h6>
    <h6>Score: ${result.score} / ${result.totalScore}</h6>
    <h6>Result: ${resultElement}</h6>
    `;

  $("#result-body").html(html);
}
