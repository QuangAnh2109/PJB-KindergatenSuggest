document.addEventListener("DOMContentLoaded", function () {
    if (window.location.search.includes("logout")) {
        const newUrl = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, newUrl);
    }

    const form = document.querySelector("form[name='loginForm']");
    const loginBtn = document.getElementById("loginBtn");

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        if (loginBtn.disabled) return;
        loginBtn.disabled = true;

        if (!window.validateForm(form)) {
            loginBtn.disabled = false;
            return;
        }
        try {
            const formData = new FormData(form);
            const response = await fetch(form.action, {
                method: "POST",
                body: formData,
                headers: { "Accept": "application/json" }
            });
            const contentType = response.headers.get("content-type");
            if (!contentType || !contentType.includes("application/json")) {
                throw new Error("Invalid JSON response");
            }

            const result = await response.json();
            if (response.ok && result.redirectUrl) {
                window.location.href = result.redirectUrl;
            } else {
                showServerError(result.error || "Unknown error");
            }
        } catch (error) {
            console.error("Fetch error:", error);
            Swal.fire({
                title: "Error",
                text: "An unexpected error occurred. Please try again!",
                icon: "error",
                confirmButtonText: "OK"
            });
        } finally {
            loginBtn.disabled = false;
        }
    });

    function showServerError(message) {
        Swal.fire({
            title: "Error",
            text: message,
            icon: "error",
            confirmButtonText: "OK"
        });
    }
});
