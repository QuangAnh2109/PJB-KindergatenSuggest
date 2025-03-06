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
