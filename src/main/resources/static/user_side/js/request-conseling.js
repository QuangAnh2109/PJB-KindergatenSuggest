// Function for request counseling
function requestCounseling(schoolId) {
    // Store the school ID in a data attribute for submission
    document.getElementById('counselingModal').setAttribute('data-school-id', schoolId);

    // Show the counseling modal
    const counselingModal = new bootstrap.Modal(document.getElementById('counselingModal'));
    counselingModal.show();
}

function submitRequestCounselingForm() {
    // Clear previous error messages
    document.querySelectorAll('.error-message').forEach(e => e.remove());

    let fullName = document.getElementById("fullName").value.trim();
    let email = document.getElementById("email").value.trim();
    let mobile = document.getElementById("mobile").value.trim();
    let inquiries = document.getElementById("inquiries").value.trim();
    let schoolId = document.getElementById('counselingModal').getAttribute('data-school-id');

    // Create FormData object
    const formData = new FormData();
    formData.append("fullName", fullName);
    formData.append("email", email);
    formData.append("phone", mobile);
    formData.append("inquiries", inquiries);
    formData.append("schoolId", schoolId);

    // Send the request
    fetch('/api/createRequest', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            // Check if response is ok (status 200-299)
            if (response.ok) {
                return response.text().then(text => {
                    // Show success toast
                    showToast('success', 'Request sent successfully!');

                    // Hide the modal
                    const counselingModal = bootstrap.Modal.getInstance(document.getElementById('counselingModal'));
                    counselingModal.hide();

                    // Reset the form fields
                    document.getElementById("email").value = "";
                    document.getElementById("mobile").value = "";
                    document.getElementById("inquiries").value = "";

                    return {success: true};
                });
            } else {
                // For error responses, try to parse as JSON
                return response.json().then(errorData => {
                    if (errorData.error) {
                        // General error message
                        showToast('error', errorData.error);
                    } else {
                        // Field-specific validation errors
                        Object.keys(errorData).forEach(field => {
                            showErrorMessage(field, errorData[field]);
                        });
                        showToast('error', 'Please correct the errors in the form');
                    }
                    return {success: false};
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('error', 'An error occurred. Please try again.');
        });
}

// Function to show field-specific error messages (expand your existing one)
function showErrorMessage(inputId, message) {
    // Map backend field names to frontend input IDs
    const fieldMapping = {
        'phone': 'mobile'  // Backend 'phone' field maps to 'mobile' input ID
    };

    // Get the correct input ID using the mapping, or use the original if not mapped
    const actualInputId = fieldMapping[inputId] || inputId;

    const inputElement = document.getElementById(actualInputId);
    if (inputElement) {
        const errorElement = document.createElement("div");
        errorElement.className = "error-message text-danger mt-1 small";
        errorElement.innerText = message;
        inputElement.parentElement.appendChild(errorElement);
    } else {
        console.warn(`Input element with ID '${actualInputId}' not found for field '${inputId}'`);
    }
}

// Universal toast function for both success and error messages
function showToast(type, message) {
    const toastContainer = document.querySelector('.position-fixed.top-0.end-0.p-3');

    // Create container if it doesn't exist
    if (!toastContainer) {
        const newContainer = document.createElement('div');
        newContainer.className = 'position-fixed top-0 end-0 p-3';
        newContainer.style.zIndex = '1050';
        document.body.appendChild(newContainer);
    }

    const bgClass = type === 'success' ? 'bg-success' : 'bg-danger';
    const icon = type === 'success' ? '✅' : '❌';

    const toastElement = document.createElement('div');
    toastElement.className = `toast align-items-center text-white ${bgClass} border-0`;
    toastElement.setAttribute('role', 'alert');
    toastElement.setAttribute('aria-live', 'assertive');
    toastElement.setAttribute('aria-atomic', 'true');

    toastElement.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${icon} ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;

    const container = document.querySelector('.position-fixed.top-0.end-0.p-3') || newContainer;
    container.appendChild(toastElement);

    const toast = new bootstrap.Toast(toastElement, {
        autohide: true,
        delay: 5000
    });
    toast.show();

    // Remove the toast element after it's hidden
    toastElement.addEventListener('hidden.bs.toast', function () {
        toastElement.remove();
    });
}


function showLoginToast() {
    // Create login toast if it doesn't exist
    if (!document.getElementById('loginToast')) {
        const toastContainer = document.querySelector('.position-fixed.top-0.end-0.p-3') ||
            document.body.appendChild(document.createElement('div'));

        if (!toastContainer.classList.contains('position-fixed')) {
            toastContainer.className = 'position-fixed top-0 end-0 p-3';
            toastContainer.style.zIndex = '1050';
        }

        const toastElement = document.createElement('div');
        toastElement.id = 'loginToast';
        toastElement.className = 'toast align-items-center text-white bg-danger border-0 fade';
        toastElement.setAttribute('role', 'alert');
        toastElement.setAttribute('aria-live', 'assertive');
        toastElement.setAttribute('aria-atomic', 'true');

        toastElement.innerHTML = `
                <div class="d-flex">
                    <div class="toast-body">
                        ⚠️ You need to log in to use this feature!
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                            aria-label="Close"></button>
                </div>
            `;

        toastContainer.appendChild(toastElement);
    }

    let toastElement = document.getElementById('loginToast');
    let toast = new bootstrap.Toast(toastElement);

    // Add animation classes before showing
    toastElement.classList.add('animate__animated', 'animate__fadeInDown');

    toast.show();

    // Hide the toast after 2 seconds with fade-out effect
    setTimeout(() => {
        toastElement.classList.remove('animate__fadeInDown');
        toastElement.classList.add('animate__fadeOutUp');

        // Redirect after animation ends (0.5s delay)
        setTimeout(() => {
            window.location.href = '/showMyLoginPage';
        }, 500);
    }, 2000);
}
