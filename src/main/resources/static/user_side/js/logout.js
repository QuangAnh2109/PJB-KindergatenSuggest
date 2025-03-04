function showPopup() {
    document.querySelector(".overlay").classList.add("active");
}

function hidePopup() {
    document.querySelector(".overlay").classList.remove("active");
}

function confirmLogout() {
    document.getElementById("logoutForm").submit();
}
