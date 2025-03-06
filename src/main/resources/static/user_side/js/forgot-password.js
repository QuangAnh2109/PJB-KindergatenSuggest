function attachFormHandler() {
    const form = document.querySelector('form[name="resetPasswordForm"]');
    const submitBtn = document.getElementById('submitBtn');

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

                    document.querySelector('.padding40').innerHTML =
                        tempDiv.querySelector('.padding40').innerHTML;
                    attachFormHandler();
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

document.addEventListener('DOMContentLoaded', attachFormHandler);
