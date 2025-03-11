function updateReloadHandle() {
    const updateForm = document.querySelector('form[name="update-form"]');
    const submitBtn = document.getElementById('updateButton');

    if (updateForm && submitBtn) {
        updateForm.addEventListener('submit', function (e) {
            e.preventDefault();
            submitBtn.disabled = true;
            fetch(updateForm.action, {
                method: 'POST',
                body: new FormData(updateForm)
            })
                .then(response => response.text())
                .then(html => {
                    const tempDiv = document.createElement('div');
                    tempDiv.innerHTML = html;
                    document.querySelector('.padding40').innerHTML =
                        tempDiv.querySelector('.padding40').innerHTML;
                    updateReloadHandle();
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred. Please try again.');
                })
                .finally(() => {
                    submitBtn.disabled = false;
                });
        });
    }
}

document.addEventListener('DOMContentLoaded', updateReloadHandle);
