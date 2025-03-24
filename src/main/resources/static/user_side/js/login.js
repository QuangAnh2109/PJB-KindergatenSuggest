document.addEventListener("DOMContentLoaded", function () {
    if (window.location.search.includes("logout")) {
        const newUrl = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, newUrl);
    }
    const logoutMessage = document.getElementById("logoutMessage");
    if (logoutMessage) {
        setTimeout(() => {
            logoutMessage.remove();
        }, 3000);
    }

    const form = document.querySelector("form[name='loginForm']");
    const loginBtn = document.getElementById("loginBtn");

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        document.querySelectorAll(".error-message").forEach(el => el.remove());
        let serverError = document.querySelector(".alert-danger");
        if (serverError) serverError.remove();

        const emailInput = document.getElementById("username");
        const passwordInput = document.getElementById("password");

        let isValid = true;

        if (!emailInput.value.trim()) {
            showError(emailInput, "This field is required");
            isValid = false;
        } else if (!validateEmail(emailInput.value.trim())) {
            showError(emailInput, "Please enter a valid email address");
            isValid = false;
        }

        if (!passwordInput.value.trim()) {
            showError(passwordInput, "This field is required");
            isValid = false;
        } else if (passwordInput.value.length < 12 || passwordInput.value.length > 72) {
            showError(passwordInput, "Password length must be between 12 and 72 characters");
            isValid = false;
        }

        if (!isValid) return;

        loginBtn.disabled = true;

        try {
            const formData = new FormData(form);
            const response = await fetch(form.action, {
                method: "POST",
                body: formData,
                headers: {
                    "Accept": "application/json"
                }
            });

            const result = await response.json();

            if (response.ok) {
                window.location.href = result.redirectUrl;
            } else {
                showServerError(result.error || "Login failed, please try again.");
            }
        } catch (error) {
            console.error("Fetch error:", error);
            alert("Having an error while processing. Please try again.");
        } finally {
            loginBtn.disabled = false;
        }
    });

    function showError(inputElement, message) {
        let error = document.createElement("div");
        error.className = "error-message text-danger mt-1";
        error.textContent = message;
        inputElement.parentNode.insertAdjacentElement("afterend", error);
    }

    function showServerError(message) {
        let errorDiv = document.createElement("div");
        errorDiv.className = "alert alert-danger text-center";
        errorDiv.textContent = message;
        document.querySelector(".padding40").insertAdjacentElement("afterbegin", errorDiv);
    }

    function validateEmail(email) {
        let re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }
});
