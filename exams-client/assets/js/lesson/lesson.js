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
    renderData(listLesson);
    console.log(listLesson);
  },
  error: function (response) {
    if (response.status == 403) { 
        backLogin();
    }
  },
});

function renderData(data){
  let html = "";
  data.forEach((lesson) => {
    let matrix = "";
    lesson.matrixQuestion.forEach((item) => {
        matrix += `<span class="badge bg-success">${item.quantity} &nbsp; ${item.levelName}</span>&nbsp;`;
    })
    let lessonRow = `<tr>
        <th scope="row"><a href="lesson-detail.html?lesson_id=${lesson.id}">${lesson.id}</a></th>
        <td><a href="lesson-detail.html?lesson_id=${lesson.id}" class="text-primary">${lesson.name}</a></td>
        <td>${lesson.numberOfQuestion}</td>
        <td>${lesson.numberOfCompletedExams}</td>
        <td>${matrix}</td>
        <td><span data-bs-toggle="modal" data-bs-target="#delete-modal" onclick="confirm_removed(${lesson.id})"><i class="bi bi-trash-fill"></i></span></td>
      </tr>`;
      html += lessonRow;
  });
  $('#lesson-list-body').html(html);
};

function confirm_removed(lessonId) {
  $("#lesson-id-delete").text(lessonId);
}

$("#remove-btn").click(() => {
  let lessonId =  $("#lesson-id-delete").text();
  $.ajax({
    type: "DELETE",
    url: BASE_URL + "/api/lessons/" + lessonId,
    headers: {
      Authorization: "Bearer " + TOKEN,
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
    },
    success: function (response) {
      window.location.replace("/views/lesson.html");
    },
    error: function (response) {
      if (response.status == 403) {
        backLogin();
      }
    },
  });

})
