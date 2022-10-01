const pathname = window.location.pathname; // Returns path only (/path/example.html)
const url = window.location.href;     // Returns full URL (https://example.com/path/example.html)
const origin = window.location.origin;   // Returns base URL (https://example.com)

const urlsPageNotAuth = ["/views/login.html", "/views/student-site.html"];

const resetToken = () => {
  sessionStorage.removeItem("token");
};

const backLogin = () => {
    window.location.replace("/views/login.html");
}

if (urlsPageNotAuth.some((url) => url === pathname)) resetToken();

if (urlsPageNotAuth.every((url) => url !== pathname)) {
    if (!sessionStorage.getItem("token")) backLogin();
}