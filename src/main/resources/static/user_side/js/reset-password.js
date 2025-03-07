document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form[name="contactForm"]');
    const submitBtn = document.getElementById('reset-button');
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
                document.querySelector('.padding').innerHTML =
                    tempDiv.querySelector('.padding').innerHTML;
            })
            .catch(error => {
                console.log('error', error);
                alert('An error has occur. Please try again.')
            })
            .finally(() => {
                submitBtn.disabled = false
            });
    });
});
