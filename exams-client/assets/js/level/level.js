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
      renderData(listLevel);
      console.log(listLevel);
    },
    error: function (data) {
      console.log(data.status);
    },
  });
  
  function renderData(data){
    let html = "";
    data.forEach((level) => {
      let levelRow = `<tr>
          <th scope="row">${level.id}</th>
          <td>${level.name}</td>
          <td>${level.score}</td>
          <td>${level.time}</td>
          <td></td>
        </tr>`;
        html += levelRow;
    });
    $('#level-list-body').html(html);
  };
  
