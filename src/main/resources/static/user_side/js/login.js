document.addEventListener("DOMContentLoaded", function () {
    document.querySelector("form").addEventListener("submit", function (event) {
        let isValid = true;

        let emailInput = document.getElementById("username");
        let passwordInput = document.getElementById("password");
        let serverError = document.querySelector(".alert-danger");
        document.querySelectorAll(".error-message").forEach(el => el.remove());
        if (serverError) {
            serverError.remove();
        }

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
        }

        if (!isValid) {
            event.preventDefault();
        }
    });

    function showError(inputElement, message) {
        let error = document.createElement("div");
        error.className = "error-message text-danger mt-1";
        error.textContent = message;
        inputElement.parentNode.insertAdjacentElement("afterend", error);
    }

    function validateEmail(email) {
        let re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    document.querySelectorAll("input").forEach(input => {
        input.addEventListener("input", function () {
            let errorMessage = this.parentNode.nextElementSibling;
            if (errorMessage && errorMessage.classList.contains("error-message")) {
                errorMessage.remove();
            }

            let serverError = document.querySelector(".alert-danger");
            if (serverError) {
                serverError.remove();
            }
        });
    });
});
