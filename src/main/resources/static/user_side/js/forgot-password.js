function attachFormHandler() {
    const form = document.querySelector('form[name="resetPasswordForm"]');
    const submitBtn = document.getElementById('submitBtn');

    if (form) {
        form.addEventListener('submit', async function (e) {
            e.preventDefault();
            submitBtn.disabled = true;
            try {
                const response = await fetch(form.action, {
                    method: 'POST',
                    body: new FormData(form),
                });
                if (!response.ok) {
                    const errorMessage = await response.text();
                    throw new Error(errorMessage || 'Unknown error occurred');
                }
                const html = await response.text();
                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = html;
                document.querySelector('.padding40').innerHTML =
                    tempDiv.querySelector('.padding40').innerHTML;
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
                attachFormHandler();
            } catch (error) {
                console.error('Error:', error);
                Swal.fire({
                    title: "Error",
                    text: "An error occurred. Please try again.",
                    icon: "error",
                    confirmButtonText: "OK"
                });
            } finally {
                submitBtn.disabled = false;
            }
        });
    }
}

document.addEventListener('DOMContentLoaded', attachFormHandler);
