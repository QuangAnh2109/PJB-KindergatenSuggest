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
                    document.querySelector('.padding40').innerHTML =
                        tempDiv.querySelector('.padding40').innerHTML;
                    if (tempDiv.querySelector('.alert-success')) {
                        const successModal = new bootstrap.Modal(document.getElementById('successModal'));
                        successModal.show();
                        document.getElementById('redirectLogin').addEventListener('click', function () {
                            window.location.href = "/public/showMyLoginPage";
                        });
                    }
                    reloadHande();
                })
                .catch(error => {
                    console.error('error:', error);
                    alert('An error occur. PLease try again.');
                })
                .finally(() => {
                    submitBtn.disabled = false;
                });
        });
    }
}
document.addEventListener('DOMContentLoaded', reloadHande);
