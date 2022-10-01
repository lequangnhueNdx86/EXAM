$.ajax({
    type: "GET",
    url: `${BASE_URL}/api/exams`,
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
            <td><a href="lesson-detail.html?lesson_id=${exam.lesson.id}">${exam.lesson.name}</a></td>
            <td>${exam.studentCode}</td>
            <td>${exam.studentName}</td>
            <td>${exam.correctQuestion}/${exam.totalQuestion}</td>
            <td>${exam.score} / ${exam.totalScore}</td>
            <td>${exam.timeComplete}</td>
            <td>${result}</td>
          </tr>`;
      });
      $("#exam-list-body").html(html);
    },
    error: function (response) {
      if (response.status == 403) {
        backLogin();
      }
    },
  });