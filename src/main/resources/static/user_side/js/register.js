function reloadHande() {
    const form = document.querySelector('form[name="registerForm"]');
    const submitBtn = document.getElementById('registerButton');

    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            submitBtn.disabled = true;

            fetch(form.action, {
                method: 'POST',
                body: new FormData(form)
            })
                .then(response => response.text())
                .then(html => {
                    const tempDiv = document.createElement('div');
                    tempDiv.innerHTML = html;
                    document.querySelector('.register').innerHTML =
                        tempDiv.querySelector('.register').innerHTML;
                    const successAlert = tempDiv.querySelector('.alert.alert-success');
                    if (successAlert) {
                        const successMessage = successAlert.textContent.trim();
                        Swal.fire({
                            title: "Success",
                            text: successMessage,
                            icon: "success",
                            confirmButtonText: "OK"
                        })
                    }
                    reloadHande();
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire({
                        title: "Error",
                        text: message.errorMessage,
                        icon: "error",
                        confirmButtonText: "OK"
                    });
                })
                .finally(() => {
                    submitBtn.disabled = false;
                });
        });
    }
}

document.addEventListener('DOMContentLoaded', reloadHande);
