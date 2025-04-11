// Gắn lại sự kiện toggle icon con mắt sau khi DOM render
function initializeTogglePassword() {
    document.querySelectorAll('.toggle-password').forEach(function (toggle) {
        toggle.removeEventListener('click', togglePasswordHandler); // tránh lặp event
        toggle.addEventListener('click', togglePasswordHandler);
    });
}

// Hàm xử lý khi click icon con mắt
function togglePasswordHandler() {
    const input = document.querySelector(this.getAttribute('toggle'));
    if (!input) return;

    const isPassword = input.getAttribute('type') === 'password';
    input.setAttribute('type', isPassword ? 'text' : 'password');

    this.classList.toggle('fa-eye');
    this.classList.toggle('fa-eye-slash');
}

// Gắn lại sự kiện submit form reset
function reloadReset() {
    const form = document.querySelector('form[name="contactForm"]');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const submitBtn = document.getElementById('reset-button');
        submitBtn.disabled = true;

        fetch(form.action, {
            method: 'POST',
            body: new FormData(form)
        })
            .then(response => response.text())
            .then(html => {
                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = html;

                // Cập nhật phần content chính
                const newContent = tempDiv.querySelector('.padding');
                if (newContent) {
                    document.querySelector('.padding').innerHTML = newContent.innerHTML;

                    // Gắn lại toggle password và sự kiện submit
                    initializeTogglePassword();
                    reloadReset();
                }

                // Nếu có success message
                const success = tempDiv.querySelector('.success');
                if (success) {
                    let successMessage = success.textContent.trim();
                    Swal.fire({
                        title: "Password Reset Successfully",
                        text: successMessage,
                        icon: "success",
                        confirmButtonText: "Login Now",
                        allowOutsideClick: false
                    }).then(() => {
                        window.location.href = "/public/sign-in";
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire({
                    title: "Error",
                    text: typeof errorMessage !== "undefined" ? errorMessage : "An error occurred. Please try again.",
                    icon: "error",
                    confirmButtonText: "OK"
                });
            })
            .finally(() => {
                submitBtn.disabled = false;
            });
    });
}
document.addEventListener('DOMContentLoaded', function () {
    reloadReset();
    initializeTogglePassword();
});
