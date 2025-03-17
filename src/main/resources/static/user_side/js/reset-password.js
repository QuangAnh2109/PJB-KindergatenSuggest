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
                if (tempDiv.querySelector('.alert-success')) {
                    const successModal = new bootstrap.Modal(document.getElementById('successModal'), {
                        backdrop: 'static',
                        keyboard: false
                    });
                    successModal.show();
                    document.getElementById('redirectLogin').addEventListener('click', function () {
                        window.location.href = "/public/showMyLoginPage";
                    });
                }
                reloadReset();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error has occurred. Please try again.');
            })
            .finally(() => {
                submitBtn.disabled = false;
            });
    });
}

document.addEventListener('DOMContentLoaded', reloadReset);
