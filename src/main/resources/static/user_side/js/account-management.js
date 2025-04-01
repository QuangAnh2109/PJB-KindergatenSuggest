document.addEventListener('DOMContentLoaded', function() {
    initializeFormHandlers();
    initializeTabSwitching();
    initializeSuccessModal();
    checkForRecordError();
});
function checkForRecordError() {
    const recordErrorAlert = document.getElementById('recordErrorAlert');
    if (recordErrorAlert) {
        const errorMessage = recordErrorAlert.textContent.trim();
        console.log("Error Message:", errorMessage);
        if (errorMessage) {
            recordErrorAlert.style.display = 'none';
            Swal.fire({
                title: "Error",
                text: errorMessage,
                icon: "error",
                confirmButtonText: "OK"
            }).then(() => {
                window.location.reload();
            });
        }
    }
}

function initializeFormHandlers() {
    // Xử lý form update account
    const updateForm = document.querySelector('form[name="update-form"]');
    const updateBtn = document.getElementById('updateButton');

    if (updateForm && updateBtn) {
        // Store original form values when page loads
        const originalFormData = new FormData(updateForm);
        const originalValues = {};

        for (const [key, value] of originalFormData.entries()) {
            originalValues[key] = value;
        }

        updateForm.addEventListener('submit', function(e) {
            e.preventDefault();

            // Check if data has changed
            const currentFormData = new FormData(updateForm);
            let hasChanges = false;

            for (const [key, value] of currentFormData.entries()) {
                // Skip CSRF token or other fields that might change between requests
                if (key === '_csrf' || key === 'recordNo') {
                    continue;
                }
                if (originalValues[key] !== value) {
                    hasChanges = true;
                    break;
                }
            }

            if (!hasChanges) {
                Swal.fire({
                    title: "No Changes",
                    text: message.dontChange,
                    icon: "info",
                    confirmButtonText: "OK"
                });
                return;
            }

            updateBtn.disabled = true;
            fetch(updateForm.action, {
                method: 'POST',
                body: currentFormData,
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
                .then(response => response.text())
                .then(html => {
                    const tempDiv = document.createElement('div');
                    tempDiv.innerHTML = html;

                    // Check if response contains recordError
                    const recordError = tempDiv.querySelector('#recordErrorAlert');
                    if (recordError) {
                        const errorMessage = recordError.textContent.trim();
                        Swal.fire({
                            title: "Error",
                            text: errorMessage,
                            icon: "error",
                            confirmButtonText: "OK"
                        }).then(() => {
                            window.location.reload();
                        });
                        return;
                    }

                    const newProfileTab = tempDiv.querySelector('#profile');
                    if (newProfileTab) {
                        document.querySelector('#profile').innerHTML = newProfileTab.innerHTML;
                    }
                    var successMessage = document.getElementById("successMessage").textContent;
                    if (tempDiv.querySelector('.alert-success')) {
                        Swal.fire({
                            title: successMessage,
                            icon: "success",
                            timer: 3000,
                            showConfirmButton: false,
                            didOpen: () => {
                                $(".swal2-popup").draggable();
                            }
                        });
                    }

                    initializeFormHandlers();
                    checkForRecordError();
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire({
                        title: "Error",
                        text: messages.errorOccur,
                        icon: "error",
                        confirmButtonText: "OK"
                    });
                })
                .finally(() => {
                    updateBtn.disabled = false;
                });
        });
    }

    const passwordForm = document.querySelector('form[name="changePass"]');
    const submitBtn = document.getElementById('submitButton');

    if (passwordForm && submitBtn) {
        passwordForm.addEventListener('submit', function(e) {
            e.preventDefault();
            submitBtn.disabled = true;

            fetch(passwordForm.action, {
                method: 'POST',
                body: new FormData(passwordForm),
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
                    if (tempDiv.querySelector('.success3') || tempDiv.querySelector('[th\\:if="${successMessage}"]')) {
                        let successMessage= tempDiv.querySelector('.success3').textContent;
                        Swal.fire({
                            title: message.updateSuccess,
                            text: message.loginBackMessage,
                            icon: "success",
                            confirmButtonText: "Login Now",
                            allowOutsideClick: false
                        }).then(() => {
                            window.location.href = "/public/showMyLoginPage";
                        });
                    }
                    initializeFormHandlers();
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire({
                        title: "Error",
                        text: messages.errorOccur,
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

function initializeTabSwitching() {
    const tabs = document.querySelectorAll('[data-bs-toggle="pill"]');

    tabs.forEach(tab => {
        tab.addEventListener('click', function(e) {
            e.preventDefault();

            // Remove active class from all tabs and panes
            document.querySelectorAll('.nav-link').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-pane').forEach(p => {
                p.classList.remove('show');
                p.classList.remove('active');
            });

            // Add active class to clicked tab
            this.classList.add('active');

            // Get target pane ID and activate it
            const targetId = this.getAttribute('data-bs-target').substring(1);
            const targetPane = document.getElementById(targetId);
            targetPane.classList.add('show');
            targetPane.classList.add('active');
        });
    });
    
    const urlParams = new URLSearchParams(window.location.search);
    const tabParam = urlParams.get('tab');

    if (tabParam === 'password') {
        document.getElementById('password-tab').click();
    }
}

function initializeSuccessModal() {
    const redirectLoginBtn = document.getElementById('redirectLogin');
    if (redirectLoginBtn) {
        redirectLoginBtn.addEventListener('click', function() {
            window.location.href = '/public/showMyLoginPage';
        });
    }
    const successUpdate = document.querySelector('[th\\:if="${successUpdate}"]');
    if (successUpdate) {
        const successModal = new bootstrap.Modal(document.getElementById('successModal'));
        successModal.show();
    }
}
