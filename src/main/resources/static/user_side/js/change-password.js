function reloadHande() {
    const form = document.querySelector('form[name="changePass"]');
    const submitBtn = document.getElementById('submitButton');

    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            submitBtn.disabled = true;

            fetch(form.action, {
                method: 'POST',
                body: new FormData(form),
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
                .then(response => response.text())
                .then(html => {
                    const tempDiv = document.createElement('div');
                    tempDiv.innerHTML = html;
                    const newPasswordTab = tempDiv.querySelector('#password');
                    if (newPasswordTab) {
                        document.querySelector('#password').innerHTML = newPasswordTab.innerHTML;
                    }
                    if (tempDiv.querySelector('.alert-success') || tempDiv.querySelector('[th\\:if="${successUpdate}"]')) {
                        const successModal = new bootstrap.Modal(document.getElementById('successModal'), {
                            backdrop: 'static',
                            keyboard: false
                        });
                        successModal.show();
                        document.getElementById('redirectLogin').addEventListener('click', function () {
                            window.location.href = "/public/showMyLoginPage";
                        });
                    }

                    document.getElementById('password-tab').click();

                    reloadHande();
                })
                .catch(error => {
                    console.error('error:', error);
                    alert('An error occurred. Please try again.');
                })
                .finally(() => {
                    submitBtn.disabled = false;
                });
        });
    }
}

document.addEventListener('DOMContentLoaded', reloadHande);
