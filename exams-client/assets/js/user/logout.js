$("#logout").click(() => {
    localStorage.removeItem("token");
    window.location.replace("/views/login.html")
})