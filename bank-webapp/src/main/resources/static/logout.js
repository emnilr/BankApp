document.addEventListener("DOMContentLoaded", () => {
    const logoutLink = document.getElementById("logout-link");

    if (logoutLink) {
        logoutLink.addEventListener("click", (e) => {
            e.preventDefault(); 
            localStorage.removeItem("jwt");
            window.location.href = "/login.html"; 
        });
    }
});