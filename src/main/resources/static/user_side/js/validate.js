document.addEventListener("DOMContentLoaded", function () {
    window.validateForm = function (form) {
        document.querySelectorAll(".error-message").forEach(el => el.remove());

        const emailInput = document.getElementById("username");
        const passwordInput = document.getElementById("password");

        let isValid = true;

        if (!emailInput.value.trim()) {
            showError(emailInput, messages.requiredField);
            isValid = false;
        } else if (!validateEmail(emailInput.value.trim())) {
            showError(emailInput, messages.invalidEmail);
            isValid = false;
        }

        if (!passwordInput.value.trim()) {
            showError(passwordInput, messages.requiredField);
            isValid = false;
        } else if (passwordInput.value.length < 12 || passwordInput.value.length > 72) {
            showError(passwordInput, messages.passwordLength);
            isValid = false;
        }

        return isValid;
    };

    function showError(inputElement, message) {
        let error = document.createElement("div");
        error.className = "error-message text-danger mt-1";
        error.textContent = message;
        inputElement.parentNode.insertAdjacentElement("afterend", error);
    }

    function validateEmail(email) {
        let re = /^(?=.{1,255}$)[a-zA-Z0-9._%+-]+@gmail\.com$/;
        return re.test(email);
    }
});
