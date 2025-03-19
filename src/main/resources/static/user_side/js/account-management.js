

document.addEventListener('DOMContentLoaded', function() {
    initializeFormHandlers();
    initializeTabSwitching();
    initializeSuccessModal();
});

function initializeFormHandlers() {
    // Xử lý form update account
    const updateForm = document.querySelector('form[name="update-form"]');
    const updateBtn = document.getElementById('updateButton');

    if (updateForm && updateBtn) {
        updateForm.addEventListener('submit', function(e) {
            e.preventDefault();
            updateBtn.disabled = true;

            fetch(updateForm.action, {
                method: 'POST',
                body: new FormData(updateForm),
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
                .then(response => response.text())
                .then(html => {
                    const tempDiv = document.createElement('div');
                    tempDiv.innerHTML = html;

                    const newProfileTab = tempDiv.querySelector('#profile');
                    if (newProfileTab) {
                        document.querySelector('#profile').innerHTML = newProfileTab.innerHTML;
                    }

                    if (tempDiv.querySelector('.alert-success')) {
                        const successAlert = document.querySelector('.alert-success');
                        if (successAlert) {
                            successAlert.classList.add('fade-in');
                            setTimeout(() => {
                                successAlert.classList.remove('fade-in');
                            }, 3000);
                        }
                    }

                    initializeFormHandlers();
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred. Please try again.');
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

                    if (tempDiv.querySelector('.alert-success') || tempDiv.querySelector('[th\\:if="${successUpdate}"]')) {
                        const successModal = new bootstrap.Modal(document.getElementById('successModal'), {
                            backdrop: 'static',
                            keyboard: false
                        });
                        successModal.show();
                    }

                    initializeFormHandlers();
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
