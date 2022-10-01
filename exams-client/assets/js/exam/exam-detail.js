const param = getUrlVars();
$.ajax({
    type: "GET",
    url: `${BASE_URL}/api/exams/${param.exam_id}`,
    headers: {
      Authorization: "Bearer " + TOKEN,
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    success: function (response) {
      const exam = response.data;
      let matrix = "";
      exam.lesson.matrixQuestion.forEach((item) => {
        matrix += `<span class="badge bg-success">${item.quantity} &nbsp; ${item.levelName}</span>&nbsp;`;
      });
      $("#exam-id").text(exam.id)
      $("#lesson").text(exam.lesson.name)
      $("#matrix").html(matrix)
      $("#student-info").text(`${exam.studentName} (${exam.studentCode})`)
      $("#start-time").text(new Date(exam.createTime).toLocaleString())
      $("#question").text(`${exam.correctQuestion} / ${exam.totalQuestion}` )
      $("#score").text(`${exam.score} / ${exam.totalScore}`)
      $("#time").text(`${exam.timeComplete} / ${exam.timeExam} (min)`)

      let result =
        exam.result === "PASS"
          ? `<span class="badge bg-success">Pass</span>`
          : `<span class="badge bg-danger">Fail</span>`;
      $("#result").html(result)

      let html = "";
      let index = 1;
      exam.listAnswer.forEach(answer => {
        let correct = "";
        if (answer.question.questionType === "FILL_BLANK") {
          correct = `${answer.question.correctValue}`;
        } else {
          correct = `<div class="col-sm-10 answer-item">`;
          let listOption = answer.question.listOption;
          listOption.forEach((option) => {
            let checked = (option.key === answer.question.correctValue) ? "checked" : 'disabled'
            correct += `<div class="form-check">
                    <input class="form-check-input" type="radio" name="${answer.question.id}" value="${option.key}" ${checked}>
                    <label class="form-check-label" for="gridRadios1">
                      ${option.key}: ${option.value}
                    </label>
                  </div>`;
          });
          correct += `</div>`;
        }

        let result = (answer.result) ? `<span class="badge bg-success">true</span>` : `<span class="badge bg-danger">false</span`
        html += `<div class="card">
            <div class="card-body">
              <h5 class="card-title">Question ${index++}: ${answer.question.content}</h5>
              <h6>Answer: <span>${answer.answer}</span></h6>
              <h6>Correct Answer: ${correct}</h6>
              <h6>${result}</h6>  
              </div>
            </div>
          </div>`;
      })

      $("#exam-body").html(html)
    },
    error: function (response) {
      if (response.status == 403) {
        backLogin();
      }
    },
  });