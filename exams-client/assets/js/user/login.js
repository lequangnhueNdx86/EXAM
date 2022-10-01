
const loginFormId = "#login-form";

$('#login-btn').click(() => {
    const user = Form.getData(loginFormId);
    $.ajax({
        type: "POST",
        url: BASE_URL + "/api/login",
        headers: {
            'Content-Type':'application/json',
            'Access-Control-Allow-Origin': "*"
        },
        // crossDomain: true,
        data: JSON.stringify(user),
        success: function(response) {
            // save token in session storage
            sessionStorage.setItem("token", response.token); 
            // redirect to index page
            window.location.replace(`/views/index.html`)
        },
        error: function(data) {
           console.log(data.status);
        }
    })
})