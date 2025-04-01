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
                document.querySelector('.padding').innerHTML =
                    tempDiv.querySelector('.padding').innerHTML;
                if (tempDiv.querySelector('.success')) {
                    let successMessage = tempDiv.querySelector('.success').textContent.trim();
                    Swal.fire({
                        title: "Password Reset Successfully",
                        text: successMessage,
                        icon: "success",
                        confirmButtonText: "Login Now",
                        allowOutsideClick: false
                    }).then(() => {
                        window.location.href = "/public/showMyLoginPage";
                    });
                }
                reloadReset();
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire({
                    title: "Error",
                    text: errorMessage,
                    icon: "error",
                    confirmButtonText: "OK"
                });
            })
            .finally(() => {
                submitBtn.disabled = false;
            });
    });
}

document.addEventListener('DOMContentLoaded', reloadReset);
